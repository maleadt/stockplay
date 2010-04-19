################################################################################
# Configuration
#

# Package definition
package StockPlay::Quote;

=pod

=head1 NAME

StockPlay::Quote - StockPlay quote data object

=head1 DESCRIPTION

The C<StockPlay::Quote> package contains a container for all quote-related
data.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use DateTime;

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTE

=cut

has 'time' => (
	is		=> 'ro',
	isa		=> 'DateTime',
	required	=> 1
);

# TODO: nodig? Zoja, mss ook in Security & Index
has 'security' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1,
);

has [qw/price bid ask low high open/] => (
	is		=> 'ro',
	isa		=> 'Num'
);

has 'volume' => (
	is		=> 'ro',
	isa		=> 'Int',
	required	=> 1
);

has 'delay' => (
	is		=> 'rw',
	isa		=> 'Num',
	default		=> 60
);

has 'fetchtime' => (	# TODO: as private
	is		=> 'rw',
	isa		=> 'Int',
	default		=> 0
);

################################################################################
# Methods
#

=pod

=head1 METHODS

=cut

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
