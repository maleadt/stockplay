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

# List of exchanges
$return = $xmlrpc->call('Finance.Exchange.List', "symbol EQUALS 'BSE'");
print "* Available exchanges:\n", Dumper(\$return), "\n";

# List of indexes
$return = $xmlrpc->call('Finance.Index.List', "exchange EQUALS 'BSE'");
print "* Available indexes\n", Dumper(\$return), "\n";

# List of securities
$return = $xmlrpc->call('Finance.Security.List', "exchange EQUALS 'BSE'");
print "* Available securities\n", Dumper(\$return), "\n";