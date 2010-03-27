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
			unless (grep { $_->{SYMBOL} eq $exchange->id } @s_exchanges) {
				my %s_exchange = (
					SYMBOL		=> $exchange->id,
					NAME		=> $exchange->name,
					LOCATION	=> $exchange->location
				);
				eval {
					$self->call("SCALAR", 'Finance.Exchange.Create', \%s_exchange);
				}; if ($@) {
					print "ERROR: could not insert exchange ", $exchange->id, " ($@)\n";
				}
				$self->call("SCALAR", 'Finance.Exchange.Create', \%s_exchange);
			}
			
			# Add the indexes (TODO fix this, ID etc)
			print "DEBUG: processing indexes\n";
			my @s_indexes = $self->call("ARRAY", 'Finance.Index.List', "exchange EQUALS '" . $exchange->id . "'");
			foreach my $index (@{$exchange->indexes}) {
				print "DEBUG: adding index ", $index->id, "\n";
				unless (grep { $_->{NAME} eq $index->id } @s_indexes) {
					my %s_index = (
						NAME		=> $index->id,
						EXCHANGE	=> $index->exchange,
					);
					eval {
						$self->call("SCALAR", 'Finance.Security.Create', \%s_index);
					}; if ($@) {
						print "ERROR: could not insert index ", $index->id, " ($@)\n";
					}
					push(@s_indexes, \%s_index);
				}				
			}
			
			# Add the securities
			print "DEBUG: processing securities\n";
			my @s_securities = $self->call("ARRAY", 'Finance.Security.List', "exchange EQUALS '" . $exchange->id . "'");
			foreach my $security (@{$exchange->securities}) {
				print "DEBUG: adding security ", $security->isin, "\n";
				unless (grep { $_->{ISIN} eq $security->isin } @s_securities) {
					my %s_security = (
						SYMBOL		=> $security->id,
						ISIN		=> $security->isin,
						NAME		=> $security->name,
						EXCHANGE	=> $exchange->id
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
			my @securities;
			
			# Check which plugins need to be updated
			foreach my $exchange (@{$plugin->exchanges}) {
				foreach my $security (@{$exchange->securities}) {
					if (not $security->has_quote or (time-$security->quote->time) > $security->quote->delay) {
						push(@securities, $security);
					}
				}
			}
			
			# Update them
			print "     Fetching quotes for ", join(", ", map { $_->id } @securities ), "\n";
			my @quotes_local = $plugin->getQuotes(@securities);
			
			# Save them
			push(@quotes, @quotes_local);
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
		foreach my $quote (@quotes) {
			my %s_quote = (
				ISIN	=> $quote->security,
				TIME	=> sub { to_datetime($quote->time) },
				PRICE	=> $quote->price,
				BID	=> $quote->bid,
				ASK	=> $quote->ask,
				LOW	=> $quote->low,
				HIGH	=> $quote->high,
				OPEN	=> $quote->open,
				VOLUME	=> $quote->volume
			);
			eval {
				$self->call("SCALAR", 'Finance.Security.Update', \%s_quote);
			}; if ($@) {
				print "ERROR: could not update security ", $quote->security, " ($@)\n";
			}
			print "DEBUG: Sent request: ", $self->xmlrpc->xml_out(), "\n";
			exit();
		}
		
		# Wait
		print "  Waiting $delay seconds\n";	
		sleep($delay);
		exit();
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
