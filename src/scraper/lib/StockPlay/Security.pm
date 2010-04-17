################################################################################
# Configuration
#

# Package definition
package StockPlay::Security;

=pod

=head1 NAME

StockPlay::Security - StockPlay security data object

=head1 DESCRIPTION

The C<StockPlay::Security> package contains a container for all security-related
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

has 'isin' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

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
