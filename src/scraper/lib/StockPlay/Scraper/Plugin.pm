################################################################################
# Configuration
#

# Package definition
package StockPlay::Scraper::Plugin;

=pod

=head1 NAME

StockPlay::Scraper::Plugin - StockPlay scraper base plugin

=head1 DESCRIPTION

The C<StockPlay::Scraper::Plugin> package contains base functionality for all
plugins.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote;

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

=pod

=head2 C<exchanges>

This method returns an array containing all the exchanges the plugin can
fetch.

=cut

has 'exchanges' => (
	is		=> 'ro',
	isa		=> 'ArrayRef[StockPlay::Exchange]',
	builder		=> '_build_exchanges',
	lazy		=> 1
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;
	
	# Build lazy-attributes
	$self->exchanges;
}

=pod

=head2 C<$plugin->getQuotes(@securities)>

=cut

requires 'getQuotes';

=pod

=head2 C<$plugin->clean()>

This method prepares the object to be dumped. Subclasses can augment this
method to remove certain attributes before performing a dump.

=cut

sub clean {
	my ($self) = @_;
	
	return 1;
}

=pod

=head2 C<$plugin->isOpen($exchange, $datetime)>

This method which should be implemented by subclasses, is used to check whether
a certain exchange is open.

=cut

requires 'isOpen';

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
