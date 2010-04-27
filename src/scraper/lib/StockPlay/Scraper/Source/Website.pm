################################################################################
# Configuration
#
## NAME		= Website
## SOURCE	= allerhande websites
## DESCRIPTION	= Functionaliteit voor website-scrapers.
## TYPE		= abstract

# Package definition
package StockPlay::Scraper::Source::Website;

=pod

=head1 NAME

StockPlay::Scraper::Source::Website - StockPlay website scraper functionality

=head1 DESCRIPTION

Plugin with specific functionality for web-based sources.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;
use WWW::Mechanize;
use StockPlay::Scraper::Source;

# Roles
with 'StockPlay::Scraper::Source';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<browser>

=cut

has 'browser' => (
	is		=> 'ro',
	isa		=> 'WWW::Mechanize',
	lazy		=> 1,	# Triggers a rebuild when undef'ing (retrieve after clean+dump)
	builder		=> '_build_browser'
);

=pod

=head2 C<$source->_build_browser>

Private method, builds the browser attribute.

=cut

sub _build_browser {
	my ($self) = @_;
	
	my $browser = WWW::Mechanize->new();
	$browser->agent('StockPlay/0.1');
	
	return $browser;
}


################################################################################
# Methods
#

after 'clean' => sub {
	my ($self) = @_;
	
	delete $self->{browser};
};


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