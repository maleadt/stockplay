################################################################################
# Configuration
#

# Package definition
package StockPlay::Scraper::Daemon;

=pod

=head1 NAMEbase functionality for all
plugins.

StockPlay::Scraper::Daemon - StockPlay scraper plugin daemon

=head1 DESCRIPTION

The C<StockPlay::Scraper::Daemon> package contains the daemon which performs
the actual quote requests and pushes the resulting data towards the
XML-RPC backend.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use XML::RPC;
use Data::Dumper;
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

has 'server' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

has 'xmlrpc' => (
	is		=> 'ro',
	isa		=> 'XML::RPC',
	lazy		=> 1,
	builder		=> '_build_xmlrpc'
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
	
	# Build data members which require other data members
	$self->xmlrpc;
	
	# Verify server connection
	eval {
		$self->call("SCALAR", 'User.Hello', "scraper/0.1", 1);
	};
	if ($@) {
		if ($@ =~ m{^no data}) {
			die("! Connection failed, is the server running?\n");
		} else {
			die("! Connection failed: $@");
		}
	}
	
	# Process all plugins
	foreach my $plugin (@{$self->plugins}) {
		print "DEBUG: checking database structure for plugin ", $plugin->infohash->{name}, "\n";
		
		# Check exchanges
		my @s_exchanges = $self->call("ARRAY", 'Finance.Exchange.List', '');
		foreach my $exchange (@{$plugin->exchanges}) {
			print "DEBUG: processing exchange ", $exchange->name, "\n";
			
			# Add the exchange
			print "DEBUG: adding exchange ", $exchange->name, "\n";
			unless (grep { $_->{SYMBOL} eq $exchange->symbol } @s_exchanges) {
				my %s_exchange = (
					SYMBOL		=> $exchange->symbol,
					NAME		=> $exchange->name,
					LOCATION	=> $exchange->location
				);
				eval {
					$self->call("SCALAR", 'Finance.Exchange.Create', \%s_exchange);
				}; if ($@) {
					print "ERROR: could not insert exchange ", $exchange->name, " ($@)\n";
				}
				push(@s_exchanges, %s_exchange);
			}
			
			# Add the indexes
			print "DEBUG: processing indexes\n";
			my @s_indexes = $self->call("ARRAY", 'Finance.Index.List', "exchange EQUALS '" . $exchange->symbol . "'");
			foreach my $index (@{$exchange->indexes}) {
				print "DEBUG: adding index ", $index->name, "\n";
				unless (grep { $_->{NAME} eq $index->name } @s_indexes) {
					my %s_index = (
						NAME		=> $index->name,
						EXCHANGE	=> $exchange->symbol,
					);
					eval {
						$self->call("SCALAR", 'Finance.Index.Create', \%s_index);
					}; if ($@) {
						print "ERROR: could not insert index ", $index->name, " ($@)\n";
					}
					push(@s_indexes, \%s_index);
				}				
			}
			
			# Add the securities
			print "DEBUG: processing securities\n";
			my @s_securities = $self->call("ARRAY", 'Finance.Security.List', "exchange EQUALS '" . $exchange->symbol . "'");
			foreach my $security (@{$exchange->securities}) {
				print "DEBUG: adding security ", $security->isin, "\n";
				unless (grep { $_->{ISIN} eq $security->isin } @s_securities) {
					my %s_security = (
						SYMBOL		=> $security->symbol,
						ISIN		=> $security->isin,
						NAME		=> $security->name,
						EXCHANGE	=> $exchange->symbol
					);
					eval {
						$self->call("SCALAR", 'Finance.Security.Create', \%s_security);
					}; if ($@) {
						print "ERROR: could not insert security ", $security->isin, " ($@)\n";
					}
					push(@s_securities, \%s_security);
				}			
			}
		}
	}
}

sub _build_xmlrpc {
	my ($self) = @_;
	
	my $xmlrpc = new XML::RPC($self->server);
	
	return $xmlrpc;
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
							my $olddelay = $security->quote->delay;
							if ($olddelay/2 > $quote->delay) {
								$quote->delay($olddelay/2);
							}
						}
						$security->quote($quote);
					} else {
						# Doubling of the delay (see big comment block above)
						$security->quote->delay($security->quote->delay * 2);
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
		foreach my $quote (@quotes) {
			my %s_quote = (
				isin	=> $quote->security,
				time	=> sub { to_datetime($quote->time) },
				price	=> $quote->price,
				bid	=> $quote->bid,
				ask	=> $quote->ask,
				low	=> $quote->low,
				high	=> $quote->high,
				open	=> $quote->open,
				volume	=> $quote->volume
			);
			eval {
				$self->call("SCALAR", 'Finance.Security.Update', \%s_quote);
			}; if ($@) {
				print "ERROR: could not update security ", $quote->security, " ($@)\n";
			}
		}
		
		# Wait
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

sub call {
	my ($self, $type, @params) = @_;
	
	my $result = $self->xmlrpc->call(@params);
	
	if (ref $result eq "HASH" && defined $result->{"faultCode"}) {
		die($result->{"faultString"});
	}
	
	if (wantarray && ref $result ne $type) {
		die("Wrong type received");
	}
	
	if ($type eq "SCALAR") {
		return $result;
	} elsif ($type eq "ARRAY") {
		return @{$result};
	} elsif ($type eq "HASH") {
		return %{$result};
	} else {
		return $result;
	}
}

sub to_datetime {
	my ($datetime) = @_;
	
	return { "dateTime.iso8601", $datetime->strftime('%Y%m%dT%H:%M:%S') };
}

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
