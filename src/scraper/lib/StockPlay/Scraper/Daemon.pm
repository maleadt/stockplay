################################################################################
# Configuration
#

# Package definition
package StockPlay::Scraper::Daemon;

=pod

=head1 NAME StockPlay::Scraper::Daemon - StockPlay scraper plugin daemon

=head1 DESCRIPTION

The C<StockPlay::Scraper::Daemon> package contains the daemon which performs
the actual quote requests and pushes the resulting data towards the
XML-RPC backend.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use Storable;
use StockPlay::Factory;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote

# Roles
with 'StockPlay::Logger';

# Write nicely
use strict;
use warnings;

# Constants
my $PLUGIN_MAX_AGE = 3600*24*7;
my $MINDELAY = 60;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<plugins>

=cut

has 'plugins' => (
	is		=> 'ro',
	isa		=> 'ArrayRef',
	lazy		=> 1,
	builder		=> '_build_plugins'
);

has 'factory' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Factory',
	required	=> 1
);

has 'pluginmanager' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Scraper::PluginManager',
	builder		=> '_build_pluginmanager'
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;
	
	# Build lazy attributes which depend on passed values
	$self->plugins;
}

sub _build_pluginmanager {
	my ($self) = @_;
	
	# Plugin manager
	$self->logger->debug("loading plugin manager");
	my $pluginmanager = new StockPlay::Scraper::PluginManager;
	
	return $pluginmanager;
}

sub _build_plugins {
	my ($self) = @_;	
	$self->logger->info("loading all plugins");
	
	# Get infohashes	
	my @infohashes = $self->pluginmanager->get_group('StockPlay::Scraper::Plugin');

	# Check homefolder
	my $dumpfolder = $ENV{'HOME'} . '/dumps/';
	mkdir $dumpfolder unless (-d $dumpfolder);

	# Load plugins
	my @plugins;
	foreach my $infohash (@infohashes) {
		$self->logger->info("loading plugin " . $infohash->{name});
		eval {		
			# Manage a dump
			my $plugin;
			if (-f $dumpfolder . $infohash->{name} . '.dump') {
				$self->logger->debug("loading from dump");
				$plugin = retrieve($dumpfolder . $infohash->{name} . '.dump')
					or $logger->logdie("could not load dump ($!)");
				
				if (time - $plugin->infohash->{time} > $PLUGIN_MAX_AGE) {
					$self->logger->debug("loaded dump out of date");			
					$plugin = undef;
				}
			}
			
			# Instantiate a plugin, if neccesary
			if (not defined $plugin) {
				$self->logger->debug("creating new dump");
				$plugin = $self->pluginmanager->instantiate($infohash);
				$plugin->clean();
				store $plugin, $dumpfolder . $infohash->{name} . '.dump';
			}
			
			# Check roles	
			if (not $plugin->does('StockPlay::Scraper::Plugin')) {
				die("passed plugin doesn't implement correct coles");
			}
			
			# Check exchanges
			my @s_exchanges = $self->factory->getExchanges();
			die("no exchanges provided") unless @s_exchanges;
			foreach my $exchange (@{$plugin->exchanges}) {
				$self->logger->debug("processing exchange " . $exchange->name);
				
				# Add the exchange
				$self->logger->debug("adding exchange " . $exchange->name);
				unless (grep { $_->symbol eq $exchange->symbol } @s_exchanges) {
					$self->factory->createExchange($exchange);
					push(@s_exchanges, $exchange);
				}
				
				# Add the indexes
				$self->logger->debug("processing indexes");
				my @s_indexes = $self->factory->getIndexes($exchange);
				foreach my $index (@{$exchange->indexes}) {
					unless (grep { $_->name eq $index->name } @s_indexes) {
						$self->factory->createIndex($exchange, $index);
						push(@s_indexes, $index);
					}				
				}
				
				# Add the securities
				$self->logger->debug("processing securities");
				my @s_securities = $self->factory->getSecurities($exchange);
				foreach my $security (@{$exchange->securities}) {
					unless (grep { $_->isin eq $security->isin } @s_securities) {
						$self->factory->createSecurity($exchange, $security);
						push(@s_securities, $security);
					}	
				}
				
				# Add the latest quotes
				$self->logger->debug("fetching latest quotes");
				my @quotes = $self->factory->getLatestQuotes(@{$exchange->securities});
				foreach my $quote (@quotes) {
					my $security = (grep { $_->isin eq $quote->security } @{$exchange->securities})[0];
					if (defined $security) {
						$security->quote($quote);
					}				
				}
			}
			
			push(@plugins, $plugin);
		};
		if ($@) {
			chomp $@;
			$self->logger->error("failed to load plugin ($@)");
		}
	}
	
	unless (@plugins) {
		die("no plugins managed to load correctly");
	}
	
	return \@plugins;
}

sub run {
	my ($self) = @_;	
	
	while (1) {
		# Process all plugins
		my (@quotes, @securities);
		foreach my $plugin (@{$self->plugins}) {
			my $pluginname = $plugin->infohash->{name};
			$self->logger->info("processing plugin $pluginname");
			eval {			
				# Check all exchanges separately
				foreach my $exchange (@{$plugin->exchanges}) {
					next unless $plugin->isOpen($exchange, DateTime->now());
					
					# Check if the delay has already passed
					my @securities_local;
					foreach my $security (@{$exchange->securities}) {
						# Don't update securities which error'd before			
						if ($security->wait != 0) {
							$security->wait($security->wait-1);
							next;
						}

						if (not $security->has_quote or (time-$security->quote->fetchtime) > $security->quote->delay) {
							push(@securities_local, $security);
						}
					}
				
					# Update them
					$self->logger->debug("fetching " . scalar @securities_local . " quotes from " . $exchange->name . " (plugin " . $plugin->infohash->{name} . ")");
					my @quotes_local = $plugin->getLatestQuotes($exchange, @securities_local);
								
					# Save them (if no errors && updated)
					foreach my $quote (@quotes_local) {
						my $security = (grep { $_->isin eq $quote->security } @securities_local)[0];
						if (not defined $security) {
							$self->logger->warn("received non-requested quote for security " . $quote->security);
							next;
						}
						
						if (not $security->has_quote or DateTime->compare($security->quote->time, $quote->time) != 0) {
							push (@quotes, $quote);
							push(@securities, $security);
							
							# All quotes in a single quote fetch have the same delay time, also if some of
							# those aren't updated nearly that frequently. That's why we don't juse replace
							# the delay with the new one, but divide it in half. Consistently, when a  quote
							# didn't seem to be updated, the delay time is doubled.
							if ($security->has_quote && $security->quote->fetchtime != 0) {
								my $olddelay = (time-$security->quote->fetchtime);
								if ($olddelay/2 > $quote->delay) {
									$quote->delay($olddelay/1.5);
								}
							}
						} else {
							# Doubling of the delay (see big comment block above)
							if ($security->quote->fetchtime != 0) {
								$security->quote->delay((time-$security->quote->fetchtime) * 2);
							}
						}
					}
				}
			};
			if ($@) {
				chomp $@;
				$self->logger->error("plugin processing failed ($@)");
			}
		}
		
		# Push the changes to the server
		my $delay = $MINDELAY;
		$self->logger->info("sending quotes to backend");
		$self->logger->debug("saving " . scalar @quotes . " quotes");
		eval {
			$self->factory->createQuotes(@quotes);
			
			# Now save the quotes locally and calculate an optimal delay
			foreach my $quote (@quotes) {
				if ($quote->delay - (time - $quote->fetchtime) < $delay) {
					$delay = $quote->delay - (time - $quote->fetchtime);
				}
				
				my $security = (grep { $_->isin eq $quote->security } @securities)[0];
				$security->quote($quote);
			}
		};
		if ($@) {
			chomp $@;
			$self->logger->error("saving quotes failed ($@)");
		}
		
		# Wait
		if ($delay < $MINDELAY) {
			$delay = $MINDELAY;
		}
		$delay = int($delay);
		$self->logger->debug("sleeping $delay seconds");
		sleep($delay);
	}
}

################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut

1;

__END__

=pod

=head1 COPYRIGHT

Copyright 2010 The StockPlay development team as listed in the AUTHORS file.

This software is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public Licence (GPL) as published by the
Free Software Foundation (FSF).

The full text of the license can be found in the
LICENSE file included with this module.

=cut
