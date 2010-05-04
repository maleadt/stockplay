################################################################################
# Configuration
#

# Package definition
package StockPlay::Scraper;

=pod

=head1 NAME StockPlay::Scraper - StockPlay scraper plugin daemon

=head1 DESCRIPTION

The C<StockPlay::Scraper> package contains the daemon which performs
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
use StockPlay::Quote;
use StockPlay::PluginManager;
use File::Path;

# Roles
with 'StockPlay::Logger';
with 'StockPlay::Configurable';

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

has 'factory' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Factory',
	required	=> 1
);

has 'plugins' => (
	is		=> 'ro',
	isa		=> 'ArrayRef',
	lazy		=> 1,
	builder		=> '_build_plugins'
);

sub _build_plugins {
	my ($self) = @_;	
	$self->logger->info("loading all plugins");
	
	# Get infohashes
	$self->pluginmanager->load_group('StockPlay::Scraper::Source');
	my @infohashes = $self->pluginmanager->get_group('StockPlay::Scraper::Source');

	# Check homefolder
	my $dumpfolder = $self->config->get('dump_folder') . '/';
	mkpath $dumpfolder unless (-d $dumpfolder);

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
					or die("could not load dump ($!)");
				
				if (time - $plugin->infohash->{time} > $PLUGIN_MAX_AGE) {
					$self->logger->debug("loaded dump out of date");			
					$plugin = undef;
				}
			}
			
			# Instantiate a plugin, if neccesary
			if (not defined $plugin) {
				$self->logger->debug("instantiating plugin");
				$plugin = $self->pluginmanager->instantiate($infohash);
			}
			
			# Check roles	
			if (not $plugin->does('StockPlay::Scraper::Source')) {
				die("passed plugin doesn't implement correct roles");
			}
			
			# Check exchanges
			# TODO: veel duplicate code, misschien beter buildExchanges met selector?
			my @s_exchanges = $self->factory->getExchanges();
			foreach my $exchange (@{$plugin->exchanges}) {
				$self->logger->debug("processing exchange " . $exchange->name);
				
				# Add the exchange
				$self->logger->debug("adding exchange " . $exchange->name);
				unless (grep { $_->symbol eq $exchange->symbol } @s_exchanges) {
					$self->factory->createExchange($exchange);
					push(@s_exchanges, $exchange);
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
				
				# Add the indexes
				$self->logger->debug("processing indexes");
				my @s_indexes = $self->factory->getIndexes($exchange);
				foreach my $index (@{$exchange->indexes}) {
					unless (grep { $_->name eq $index->name } @s_indexes) {
						$self->factory->createIndex($exchange, $index);
						push(@s_indexes, $index);
					}
					
					# Add the securities on the index
					my @s_indexsecurities = $self->factory->getIndexSecurities($exchange, $index);
					foreach my $security (@{$index->securities}) {
						unless (grep { $_->isin eq $security->isin } @s_indexsecurities) {
							$self->factory->createIndexSecurity($index, $security);
							push(@s_indexsecurities, $security);
						}
					}
				}
				
				# Add the latest quotes
				$self->logger->debug("fetching latest quotes");
				my @quotes = $self->factory->getLatestQuotes(@{$exchange->securities});
				foreach my $quote (@quotes) {
					my $quotable = (grep { $_->isin eq $quote->quotable } @{$exchange->securities})[0];
					if (defined $quotable) {
						$quotable->quote($quote);
					}				
				}
			}
			
			# Update the dump
			$self->logger->debug("updating dump");
			$plugin->clean();
			store $plugin, $dumpfolder . $infohash->{name} . '.dump';
			
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

has 'pluginmanager' => (
	is		=> 'ro',
	isa		=> 'StockPlay::PluginManager',
	required	=> 1
);


################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$daemon->BUILD>

The object constructor. Builds pseudo-lazy attributes which depend on values
passed by constructor.

=cut

sub BUILD {
	my ($self) = @_;
	
	# Default configuration
	$self->config->set_default('dump_folder', $ENV{'HOME'} . '/.stockplay/scraper/dumps');
	
	# Build lazy attributes which depend on passed values
	$self->plugins;
	
	return;
}

=pod

=head2 C<$pluginmanager->run>

Main application loop. During each loop all exchanges and their securities get
checked, eventually updated, an finally pushed to the backend.

=cut

sub run {
	my ($self) = @_;	
	
	while (1) {
		# Process all plugins
		my (@quotes, @quotables);
		foreach my $plugin (@{$self->plugins}) {
			my $pluginname = $plugin->infohash->{name};
			$self->logger->info("processing plugin $pluginname");
			eval {			
				# Check all exchanges separately
				foreach my $exchange (@{$plugin->exchanges}) {
					unless ($plugin->isOpen($exchange, DateTime->now())) {
						# Remove all quotes (so they can't be used as
						# time reference the next day)
						foreach my $quotable ((@{$exchange->securities}, @{$exchange->indexes})) {
							delete $quotable->{quote};
						}
						next;
					}
					
					# Check if the delay has already passed
					my @quotables_local;
					foreach my $quotable ((@{$exchange->securities}, @{$exchange->indexes})) {
						# Don't update securities which error'd before			
						if ($quotable->wait != 0) {
							$quotable->wait($quotable->wait-1);
							next;
						}

						if (not $quotable->has_quote or (time-$quotable->quote->fetchtime) > $quotable->quote->delay) {
							push(@quotables_local, $quotable);
						}
					}
				
					# Update them
					$self->logger->debug("fetching " . scalar @quotables_local . " quotes from " . $exchange->name . " (plugin " . $plugin->infohash->{name} . ")");
					my @quotes_local = $plugin->getLatestQuotes($exchange, @quotables_local);
								
					# Save them (if no errors && updated)
					foreach my $quote (@quotes_local) {
						my $quotable = (grep { $_->isin eq $quote->quotable } @quotables_local)[0];
						if (not defined $quotable) {
							$self->logger->warn("received non-requested quote for security " . $quote->quotable);
							next;
						}
						
						if (not $quotable->has_quote or DateTime->compare($quotable->quote->time, $quote->time) != 0) {
							push (@quotes, $quote);
							push(@quotables, $quotable);
							
							# All quotes in a single quote fetch have the same delay time, also if some of
							# those aren't updated nearly that frequently. That's why we don't juse replace
							# the delay with the new one, but divide it in half. Consistently, when a  quote
							# didn't seem to be updated, the delay time is doubled.
							if ($quotable->has_quote && $quotable->quote->fetchtime != 0) {
								my $olddelay = (time-$quotable->quote->fetchtime);
								if ($olddelay/2 > $quote->delay) {
									$quote->delay($olddelay/1.5);
								}
							}
						} else {
							# Doubling of the delay (see big comment block above)
							if ($quotable->quote->fetchtime != 0) {
								$quotable->quote->delay((time-$quotable->quote->fetchtime) * 2);
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
		if (@quotes) {
			$self->logger->info("sending quotes to backend");
			$self->logger->debug("saving " . scalar @quotes . " quotes");
			eval {
				$self->factory->createQuotes(@quotes);
				
				# Now save the quotes locally and calculate an optimal delay
				foreach my $quote (@quotes) {
					if ($quote->delay - (time - $quote->fetchtime) < $delay) {
						$delay = $quote->delay - (time - $quote->fetchtime);
					}
					
					my $quotable = (grep { $_->isin eq $quote->quotable } @quotables)[0];
					$quotable->quote($quote);
				}
			};
			if ($@) {
				chomp $@;
				$self->logger->error("saving quotes failed ($@)");
			}
		}
		
		# Wait
		if ($delay < $MINDELAY) {
			$delay = $MINDELAY;
		}
		$delay = int($delay);
		$self->logger->debug("sleeping $delay seconds");
		sleep($delay);
	}
	
	return;
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
