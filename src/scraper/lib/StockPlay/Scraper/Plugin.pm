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

################################################################################
# Methods
#

=pod

=head1 METHODS

=head2 C<$plugin->getExchanges()>

This method returns an array containing all the exchanges the plugin can
fetch.

=cut

requires 'getExchanges';

=pod

=head2 C<$plugin->getIndexes($exchange)>

This method returns an array of available indexes, each of them packed in
an L<StockPlay::Index> object.

=cut

requires 'getIndexes';

=pod

=head2 C<$plugin->getSecurities($exchange)>

This method returns all quotes available at the given exchange. These quotes,
packed in an array, are all instantiations of the L<StockPlay::Quote> object.

=cut

requires 'getSecurities';

=pod

=head2 C<$plugin->getQuote($security)>

=cut

requires 'getQuote';

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
