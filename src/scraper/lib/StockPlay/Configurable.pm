################################################################################
# Configuration
#

# Package definition
package StockPlay::Configurable;

=pod

=head1 NAME

StockPlay::Configurable - StockPlay role for configurable objects

=head1 DESCRIPTION

The C<StockPlay::Configurable> is a simple role, which extends classes with
a configuration object.

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

has 'config' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Configuration',
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
