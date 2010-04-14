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
	required	=> 1
);

has 'factory' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Factory',
	required	=> 1
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;;
	
	# Verify roles
	for my $plugin (@{$self->plugins}) {
		if (not $plugin->does('StockPlay::Scraper::Plugin')) {
			$self->logger->logdie("passed plugin doesn't implement correct coles");
		}
	}
	
	# Process all plugins
	foreach my $plugin (@{$self->plugins}) {
		$self->logger->debug("checking database structure for plugin " . $plugin->infohash->{name});
		
		# Check exchanges
		my @s_exchanges = $self->factory->getExchanges();
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
			my @quotes = $self->factory->getQuotes(@{$exchange->securities});
			foreach my $quote (@quotes) {
				my $security = (grep { $_->isin eq $quote->security } @{$exchange->securities})[0];
				if (defined $security) {
					$security->quote($quote);
				}				
			}
		}
	}
}

sub run {
	my ($self) = @_;	
	
	while (1) {
		# Process all plugins
		my @quotes;
		foreach my $plugin (@{$self->plugins}) {
			my $pluginname = $plugin->infohash->{name};
			$self->logger->debug("processing plugin $pluginname");
			
			# Check which plugins need to be updated
			foreach my $exchange (@{$plugin->exchanges}) {
				next unless $plugin->isOpen($exchange, DateTime->now());
				
				# Check if the delay has already passed
				my @securities;
				foreach my $security (@{$exchange->securities}) {
					# Don't update securities which error'd before			
					if ($security->wait != 0) {
						$security->wait($security->wait-1);
						next;
					}

					if (not $security->has_quote or (time-$security->quote->fetchtime) > $security->quote->delay) {
						push(@securities, $security);
					}
				}
			
				# Update them
				$self->logger->info("fetching " . scalar @securities . " quotes");
				my @quotes_local = $plugin->getQuotes($exchange, @securities);
				
				# Save them (if no errors && updated)
				foreach my $quote (@quotes_local) {
					my $security = (grep { $_->isin eq $quote->security } @securities)[0];
					if (not defined $security) {
						$self->logger->warn("received non-requested quote for security " . $quote->security);
						next;
					}
					
					if (not $security->has_quote or DateTime->compare($security->quote->time, $quote->time) != 0) {
						push (@quotes, $quote);
						
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
						$security->quote($quote);
					} else {
						# Doubling of the delay (see big comment block above)
						$security->quote->delay((time-$security->quote->fetchtime) * 2);
					}
				}
			}
		}
			
		# Check delays
		my $delay = 60;
		foreach my $quote (@quotes) {
			if ($quote->delay - (time - $quote->fetchtime) < $delay) {
				$delay = $quote->delay - (time - $quote->fetchtime);
			}
		}
		
		# Push the changes to the server
		$self->logger->info("saving " . scalar @quotes . " quotes");
		$self->factory->createQuotes(@quotes);
		
		# Wait
		if ($delay < 60) {
			$delay = 60;
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
