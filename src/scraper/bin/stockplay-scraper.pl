#!/usr/bin/env perl

################################################################################
# Configuration
#

# XXX: library location
use lib '../lib';

# Packages
use StockPlay::Scraper::PluginManager;

# Write nicely
use warnings;
use strict;


################################################################################
# Main
#

print "* Loading scraper\n";

# Plugin manager
print "- Loading plugin manager\n";
my $pluginmanager = new StockPlay::Scraper::PluginManager;

#$blabla->run();

exit(0);

__END__

################################################################################
# Documentation
#

=pod

=head1 NAME

stockplay-scraper - Scraper component of StockPlay

=head1 SYNOPSIS

=head1 COPYRIGHT

Copyright 2010 The StockPlay development team as listed in the AUTHORS file.

This software is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public Licence (GPL) as published by the
Free Software Foundation (FSF).

The full text of the license can be found in the
LICENSE file included with this module.

=cut
