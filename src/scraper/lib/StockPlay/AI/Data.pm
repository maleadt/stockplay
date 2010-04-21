###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI::Data;

=pod

=head1 NAME

StockPlay::AI::Data - StockPlay AI data object

=head1 DESCRIPTION

This object represents a set of data which will be provided (or requested)
from a forecaster. It contains all relevant data, meaning the forecaster
should not have to look up or calculate data fields from several data objects
(eg. calculate the closing course from the opening course at data object i+1).

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut


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
