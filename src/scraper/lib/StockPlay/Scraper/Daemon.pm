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
}

sub run {
	my ($self) = @_;	
	
	while (1) {
		print "- Initializing a run\n";
		
		# Process all plugins
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
		my $delay = 60;
		foreach my $quote (@quotes) {
			if ($quote->delay - (time - $quote->time) < $delay) {
				$delay = $quote->delay - (time - $quote->time);
			}
		}
		
		# Push the changes to the server
		print "     Waiting $delay seconds\n";	
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
