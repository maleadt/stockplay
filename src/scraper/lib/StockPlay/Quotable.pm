################################################################################
# Configuration
#

# Package definition
package StockPlay::Quotable;

=pod

=head1 NAME

StockPlay::Security - StockPlay quotable role

=head1 DESCRIPTION

The C<StockPlay::Quotable> package contains a role for objects which can have
a quote assigned to them.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;
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

#requires 'isin';
# TODO: doesn't, work, though the docs say:
# "Note that attribute accessors also count as methods for the purposes of satisfying the requirements of a role."

has 'quote' => (
	is		=> 'rw',
	isa		=> 'StockPlay::Quote',
	predicate	=> 'has_quote'
);

has [qw/errors wait/] => (
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
