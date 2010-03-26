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
		$self->xmlrpc->call('User.Hello', "scraper/0.1", 1);
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
		foreach my $exchange (@{$plugin->exchanges}) {
			print "DEBUG: processing exchange ", $exchange->name, "\n";
			
			# Add the exchange
			print "DEBUG: adding exchange ", $exchange->name, "\n";
			my $result = $self->xmlrpc->call('Finance.Exchange.List', 'symbol EQUALS \'' . $exchange->id . '\'');
			if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
				die("Internal error");
			}
			if (ref $result eq "ARRAY" && scalar @{$result} != 1) {
				$result = $self->xmlrpc->call('Finance.Exchange.Create', {
					SYMBOL		=> $exchange->id,
					NAME		=> $exchange->name,
					LOCATION	=> $exchange->location
				});
				if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
					die("Internal error");
				}
			}
			
			# Add the indexes (TODO fix this, ID etc)
			print "DEBUG: processing indexes\n";
			foreach my $index (@{$exchange->indexes}) {
				print "DEBUG: adding index ", $index->id, "\n";
				
				$result = $self->xmlrpc->call('Finance.Index.List', 'name EQUALS \'' . $index->id . '\' AND exchange EQUALS \'' . $exchange->id . '\'');
				if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
					die("Internal error");
				}
				if (ref $result eq "ARRAY" && scalar @{$result} != 1) {
					$result = $self->xmlrpc->call('Finance.Security.Create', {
						NAME		=> $index->id,
						EXCHANGE	=> $index->exchange,
					});
					if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
						die("Internal error");
					}
				}				
			}
			
			# Add the securities
			print "DEBUG: processing securities\n";
			foreach my $security (@{$exchange->securities}) {
				print "DEBUG: adding security ", $security->isin, "\n";
				
				$result = $self->xmlrpc->call('Finance.Security.List', 'isin EQUALS \'' . $security->isin . '\'');
				if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
					die("Internal error");
				}
				if (ref $result eq "ARRAY" && scalar @{$result} != 1) {
					$result = $self->xmlrpc->call('Finance.Security.Create', {
						SYMBOL		=> $security->id,
						ISIN		=> $security->isin,
						NAME		=> $security->name,
						EXCHANGE	=> $exchange->id
					});
					if (ref $result eq "HASH" && defined $result->{'faultCode'}) {
						die("Internal error");
					}
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
