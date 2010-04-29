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
use StockPlay::Logger;

# Roles
with 'StockPlay::Logger';

# Write nicely
use strict;
use warnings;

# Specify version
use vars qw($VERSION);
$VERSION = '0.01';




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
	
	my $config = StockPlay::Configuration->new;
	if (-f $ENV{'HOME'} . '/.stockplay/config') {
		$config->file_read($ENV{'HOME'} . '/.stockplay/config');
	}
	
	return $config;
}

has 'app' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$stockplay->BUILD>

Object constructor, loads lazy attributes, connects to the server, and sends
an initial HELLO message.

=cut

sub BUILD {
	my ($self) = @_;
	
	StockPlay::Logger->setup($self->app);
	
	$self->logger->info("initialising " . $self->app);
	
	return;
}

=pod

=head2 C<$stockplay->getFactory(@params)>

Instantiate a new C<StockPlay::Factory>.

=cut

sub getFactory {
	my ($self, @params) = @_;
	
	$self->logger->info('loading factory');
	return StockPlay::Factory->new(
		config		=> $self->config->get_section("factory"),
		@params
	);
}

=pod

=head2 C<$stockplay->getPluginManager(@params)>

Instantiate a new C<StockPlay::PluginManager>.

=cut

sub getPluginManager {
	my ($self, @params) = @_;
	
	$self->logger->info('loading plugin manager');
	return StockPlay::PluginManager->new(
		config		=> $self->config->get_section("pluginmanager"),
		@params
	);
}

=pod

=head2 C<$stockplay->getScraper(@params)>

Instantiate a new C<StockPlay::Scraper>. This automatically instantiates the
required plugin manager as well.

=cut

sub getScraper {
	my ($self, @params) = @_;
	
	$self->logger->info('loading scraper');
	return StockPlay::Scraper->new(
		config		=> $self->config->get_section("scraper"),
		pluginmanager	=> $self->getPluginManager(),
		factory		=> $self->getFactory(),
		@params
	);
}

=pod

=head2 C<$stockplay->getAI(@params)>

Instantiate a new C<StockPlay::AI>. This automatically instantiates the
required plugin manager as well.

=cut

sub getAI {
	my ($self, @params) = @_;
	
	$self->logger->info('loading artificial intelligence');
	return StockPlay::AI->new(
		config		=> $self->config->get_section("ai"),
		pluginmanager	=> $self->getPluginManager(),
		factory		=> $self->getFactory(),
		@params
	);
}

=pod

=head2 C<$stockplay->getLogger>

Instantiate a new C<Log::Log4perl> logger. This logger is guaranteed to be
properly set up and ready for use.

=cut

sub getLogger {
	my ($self) = @_;
	
	return $self->logger;
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
