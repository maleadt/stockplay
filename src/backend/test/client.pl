#!/usr/bin/perl

#
# Configuration
#

# Packages
use XML::RPC;
use Data::Dumper;

# Write nicely
use strict;
use warnings;

# XML::RPC object
my $xmlrpc = new XML::RPC('http://localhost:8080') || die("could not connect to backend ($!)");


#
# Main
#

# Backend stats
my $stats = $xmlrpc->call('System.Backend.Stats');
print "* Backend stats:\n", Dumper(\$stats);

