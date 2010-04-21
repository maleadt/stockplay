################################################################################
# Configuration
#

# Package definition
package StockPlay::Object;

=pod

=head1 NAME

StockPlay::Object - StockPlay Object data object

=head1 DESCRIPTION

The C<StockPlay::Object> package contains a container for all Object-related
data. This essentially inserts a "private data container", which can be used
to save application-specific data for which no attributes have been reserved.

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

has 'private' => (
	is		=> 'ro',
	isa		=> 'HashRef',
	default		=> sub { {} }
);

################################################################################
# Methods
#

=pod

=head1 METHODS

=head2 C<$object->set($key, $value)>

Sets a key in the private data container.

=cut

sub set {
	my ($self, $key, $value) = @_;
	
	$self->private->{$key} = $value;
}

=pod

=head2 C<$object->get($key)>

Gets a key out of the private data container.

=cut

sub get {
	my ($self, $key) = @_;
	
	return $self->private->{$key};
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
