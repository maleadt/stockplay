################################################################################
# Configuration
#

# Package definition
package StockPlay::Logger;

=pod

=head1 NAME

StockPlay::Logger - StockPlay logger

=head1 DESCRIPTION

The C<StockPlay::Logger> package contains a base role for packages needing a
logger. By implementing this role, a module gets hold of a "logger" attribute,
an instantiation of C<Log::Log4perl> which can be used to log as much as needed.
Before doing anything though, the main script should use the static C<setup>
method to set the library up.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;
use Log::Log4perl qw(get_logger);
use Carp;

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut

my $IS_SETUP = 0;


################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$object->logger>
=head2 C<StockPlay::Logger->logger($name)>

This method, available as object method as well as static method, creates a
new instantiation of a C<Log::Log4perl> object with the tag already set up
correctly. When calling statically, the tag is guessed from the callstack, but
a manual tag may be used as well by passing it as parameter.

=cut

sub logger {
	croak("Logger not properly set-up") unless $IS_SETUP;
	
	# Logger for role implementors
	my $self = shift;
	if (ref $self) {
		return get_logger(ref $self);
	}
	
	# Specific logger request
	my $package = shift || (caller(0))[0];
	return get_logger($package);
}

=pod

=head2 C<StockPlay:Logger->setup($name)>

This method is to be called once before using an logger objects provided
by this class, as some initialization stuff needs to happen. The method should
get passed the name suffix of the application (eg. "ai" or "scraper"), which
will then (prepended with "stockplay-") be used as syslog tag.

This method will also register last-resort signal handlers, causing modules
not to have use "logdie" or "logwarn", but the usual Perl counterparts.

=cut

sub setup {
	my ($package, $name) = @_;
	croak("logger should be set-up statically") if (ref $package);
	croak("cannot be set-up twice") if ($IS_SETUP);
	$name = "anonymous" unless defined $name;
	
	# Initialize Log4Perl
	Log::Log4perl->init(\<<EOT);
		log4perl.logger = DEBUG, screen, syslog
		
		# Syslog appender
		log4perl.appender.syslog=Log::Dispatch::Syslog
		log4perl.appender.syslog.Facility=user
		log4perl.appender.syslog.ident=stockplay-$name
		log4perl.appender.syslog.layout=PatternLayout
		log4perl.appender.syslog.layout.ConversionPattern=%c - %m%n


		# Console appender
		log4perl.appender.screen=Log::Log4perl::Appender::Screen
		log4perl.appender.screen.layout=PatternLayout
		log4perl.appender.screen.layout.ConversionPattern=%c - %m%n
EOT
	
	# Configure last-resort error loggers
	$SIG{__DIE__} = sub {
		die(@_) unless (defined $^S && $^S == 0);
		exit 1 if (defined caller(2) && (caller(2))[3] =~ m{Log::Log4perl::Logger::logdie});
		
		my $error = shift;
		chomp $error;
		StockPlay::Logger->logger((caller(0))[0])->logexit($error);
	};
	$SIG{__WARN__} = sub {
		warn(@_) unless (defined $^S && $^S == 0);
		return if (defined caller(2) && (caller(2))[3] =~ m{Log::Log4perl::Logger::logwarn});
		
		my $warning = shift;
		chomp $warning;
		StockPlay::Logger->logger((caller(0))[0])->warn($warning);
	};

	# Set the flag
	$IS_SETUP = 1;
	return 1;
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
