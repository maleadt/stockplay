################################################################################
# Configuration
#

# Package definition
package StockPlay::Exchange;

=pod

=head1 NAME

StockPlay::Exchange - StockPlay exchange data object

=head1 DESCRIPTION

The C<StockPlay::Exchange> package contains a container for all exchange-related
data.

=head1 SYNPOSIS

=cut

# Packages
use Moose;

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

has 'symbol' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

has 'name' => (
	is		=> 'rw',
	isa		=> 'Str',
	predicate	=> 'has_name'
);

has 'location' => (
	is		=> 'rw',
	isa		=> 'Str',
	predicate	=> 'has_location'
);

has 'securities' => (
	is		=> 'ro',
	isa		=> 'ArrayRef[StockPlay::Security]',
	default		=> sub { [] }
);

has 'indexes' => (
	is		=> 'ro',
	isa		=> 'ArrayRef[StockPlay::Index]',
	default		=> sub { [] }
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
