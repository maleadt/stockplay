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
my $xmlrpc = new XML::RPC('http://localhost:8080/backend/public') || die("could not connect to backend ($!)");


#
# Main
#

my $return;

# Server hello
$return = $xmlrpc->call('User.Hello', "perl_testclient/0.1", 1);
print "* Server hello:\n", Dumper(\$return), "\n";

# Backend stats
$return = $xmlrpc->call('System.Backend.Stats');
print "* Backend stats\n", Dumper(\$return), "\n";

# Database stats
$return = $xmlrpc->call('System.Database.Stats');
print "* Database stats\n", Dumper(\$return), "\n";
