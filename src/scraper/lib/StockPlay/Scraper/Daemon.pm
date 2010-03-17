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
	isa		=> 'ArrayRef[StockPlay::Scraper::Plugin]',
	required	=> 1
);

has 'quotes' => (
	is		=> 'ro',
	isa		=> 'HashRef[HashRef[StockPlay::Quote]]',	# Plugin -> ISIN -> Quote
	default		=> sub { {} }
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub run {
	my ($self) = @_;
	
	my @securities_updated;
	while (1) {
		# Process all plugins
		foreach my $plugin (@{$self->plugins}) {
			my $pluginname = $plugin->infohash->{name};
			my $min_delay = 60;
			my @securities;
			
			# Check which plugins need to be updates
			foreach my $security (@{$plugin->securities}) {
				if (not $security->has_quote or time-$security->quote->time > $security->quote->delay) {
					push(@securities, $security);
				}
			}
			
			# Update them
			# TODO
			
			# Save them
			push(@securities_updated, @securities);
		}
		
		# Push the changes to the server
		
		
		$#securities_updated = -1;	
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
