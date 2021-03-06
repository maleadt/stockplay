################################################################################
# Configuration
#

# Package definition
package StockPlay::User;

=pod

=head1 NAME

StockPlay::User - StockPlay user data object

=head1 DESCRIPTION

The C<StockPlay::User> package contains a container for all user-related
data.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use DateTime;

# Consume roles
with 'StockPlay::Object';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTE

=cut

has 'id' => (
	is		=> 'ro',
	isa		=> 'Int',
	required	=> 1
);

has [qw/nickname firstname lastname/] => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

has [qw/cash startamount/] => (
	is		=> 'ro',
	isa		=> 'Num',
	required	=> 1
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
