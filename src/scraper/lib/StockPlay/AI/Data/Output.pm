###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI::Data::Output;

=pod

=head1 NAME

StockPlay::AI::Data::Output - StockPlay AI output data object

=head1 DESCRIPTION

This is the input data object, see the L<ATTRIBUTES> section for a detailled
listing.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use StockPlay::AI::Data;

# Write nicely
use strict;
use warnings;

# Consume roles
with 'StockPlay::AI::Data';


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut

has 'closing' => (
	is		=> 'ro',
	isa		=> 'Num',
	required	=> 1
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut

__PACKAGE__->meta->make_immutable;

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
