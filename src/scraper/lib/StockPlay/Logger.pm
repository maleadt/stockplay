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
logger.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;
use Log::Log4perl qw(get_logger);

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

BEGIN {
	Log::Log4perl->init(\<<EOT);
		log4perl.logger = DEBUG, screen
		
		# Syslog appender
		log4perl.appender.syslog=Log::Dispatch::Syslog
		log4perl.appender.syslog.Facility=user
		log4perl.appender.syslog.ident=stockplay-scraper
		log4perl.appender.syslog.layout=PatternLayout
		log4perl.appender.syslog.layout.ConversionPattern=%c - %m%n


		# Console appender
		log4perl.appender.screen=Log::Log4perl::Appender::Screen
		log4perl.appender.screen.layout=PatternLayout
		log4perl.appender.screen.layout.ConversionPattern=%c - %m%n
EOT
	
}


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub logger {	
	# Logger for role implementors
	my $self = shift;
	if (ref $self) {
		return get_logger(ref $self);
	}
	
	# Specific logger request
	my $package = shift || (caller(0))[0];
	return get_logger($package);
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
