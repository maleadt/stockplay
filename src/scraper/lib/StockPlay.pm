################################################################################
# Configuration
#

# Package definition
package StockPlay;

=pod

=head1 NAME

StockPlay - StockPlay instantiation class

=head1 DESCRIPTION

The C<StockPlay> package contains functions to instantiate (and configure)
main objects in the StockPlay hierarchy.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use StockPlay::Configuration;
use StockPlay::PluginManager;
use StockPlay::AI;
use StockPlay::Scraper;

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

=cut

has 'config' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Configuration',
	builder		=> '_build_config'
);

sub _build_config {
	my ($self) = @_;
	
	my $config = new StockPlay::Configuration;
	if (-f '~/.stockplay/config') {
		$config->file_read('~/.stockplay/config');
	}
	
	return $config;
}

################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub getFactory {
	my ($self, @params) = @_;
	
	$self->logger->info('loading factory');
	return new StockPlay::Factory(
		config		=> $self->config->get_section("factory"),
		@params
	);
}

sub getPluginManager {
	my ($self, @params) = @_;
	
	$self->logger->info('loading plugin manager');
	return new StockPlay::PluginManager(
		config		=> $self->config->get_section("pluginmanager"),
		@params
	);
}

sub getScraper {
	my ($self, @params) = @_;
	
	$self->logger->info('loading scraper');
	return new StockPlay::Scraper(
		config		=> $self->config->get_section("scraper"),
		pluginmanager	=> getPluginManager(),
		@params
	);
}

sub getAI {
	my ($self, @params) = @_;
	
	$self->logger->info('loading artificial intelligence');
	return new StockPlay::AI(
		config		=> $self->config->get_section("ai"),
		pluginmanager	=> getPluginManager(),
		@params
	);
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
