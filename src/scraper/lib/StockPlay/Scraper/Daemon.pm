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
use StockPlay::Quote;

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
			die("passed plugin doesn't implement correct coles");
		}
	}
	
	# Process all plugins
	foreach my $plugin (@{$self->plugins}) {
		print "DEBUG: checking database structure for plugin ", $plugin->infohash->{name}, "\n";
		
		# Check exchanges
		my @s_exchanges = $self->factory->getExchanges();
		foreach my $exchange (@{$plugin->exchanges}) {
			print "DEBUG: processing exchange ", $exchange->name, "\n";
			
			# Add the exchange
			print "DEBUG: adding exchange ", $exchange->name, "\n";
			unless (grep { $_->symbol eq $exchange->symbol } @s_exchanges) {
				$self->factory->createExchange($exchange);
				push(@s_exchanges, $exchange);
			}
			
			# Add the indexes
			print "DEBUG: processing indexes\n";
			my @s_indexes = $self->factory->getIndexes($exchange);
			foreach my $index (@{$exchange->indexes}) {
				print "DEBUG: adding index ", $index->name, "\n";
				unless (grep { $_->name eq $index->name } @s_indexes) {
					$self->factory->createIndex($exchange, $index);
					push(@s_indexes, $index);
				}				
			}
			
			# Add the securities
			print "DEBUG: processing securities\n";
			my @s_securities = $self->factory->getSecurities($exchange);
			foreach my $security (@{$exchange->securities}) {
				print "DEBUG: adding security ", $security->name, " (ISIN ", $security->isin, ")\n";
				unless (grep { $_->isin eq $security->isin } @s_securities) {
					$self->factory->createSecurity($exchange, $security);
					push(@s_securities, $security);
				}			
			}
		}
	}
}

sub run {
	my ($self) = @_;	
	
	while (1) {
		# Process all plugins
		print "- Processing plugins\n";
		my @quotes;
		foreach my $plugin (@{$self->plugins}) {
			my $pluginname = $plugin->infohash->{name};
			print "  -> $pluginname\n";
			
			# Check which plugins need to be updated
			foreach my $exchange (@{$plugin->exchanges}) {
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
				print "     Fetching quotes for ", join(", ", map { $_->name } @securities ), "\n";
				my @quotes_local = $plugin->getQuotes($exchange, @securities);
				
				# Save them (if no errors && updated)
				foreach my $quote (@quotes_local) {
					my $security = (grep { $_->isin eq $quote->security } @securities)[0];
					if (not defined $security) {
						print "ERROR: received not-requested quote for security ", $quote->security, "\n";
						next;
					}
					
					if (not $security->has_quote or $security->quote->time != $quote->time) {
						push (@quotes, $quote);
						
						# All quotes in a single quote fetch have the same delay time, also if some of
						# those aren't updated nearly that frequently. That's why we don't juse replace
						# the delay with the new one, but divide it in half. Consistently, when a  quote
						# didn't seem to be updated, the delay time is doubled.
						if ($security->has_quote) {
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
		print "- Checking delays\n";
		my $delay = 60;
		foreach my $quote (@quotes) {
			if ($quote->delay - (time - $quote->fetchtime) < $delay) {
				$delay = $quote->delay - (time - $quote->fetchtime);
			}
		}
		
		# Push the changes to the server
		print "- Saving changes\n";
		$self->factory->updateBulk(@quotes);
		
		# Wait
		if ($delay < 60) {
			$delay = 60;
		}
		$delay = int($delay);
		print "  Waiting $delay seconds\n";
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
