################################################################################
# Configuration
#

# Package definition
package StockPlay::Plugin;

=pod

=head1 NAME

StockPlay::Plugin - StockPlay scraper base plugin

=head1 DESCRIPTION

The C<StockPlay::Plugin> package contains base functionality for all
plugins.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;

# Roles
with 'StockPlay::Logger';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<infohash>

The plugin-specific infohash, containing the info keys defined in the plugin
file. This infohash is constructed in C<StockPlay::Scraper::PluginManager::parse>,
so look there for more information.

=cut

has 'infohash' => (
	is		=> 'ro',
	isa		=> 'HashRef',
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
