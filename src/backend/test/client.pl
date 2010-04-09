#!/usr/bin/perl

#
# Configuration
#

# Packages
use RPC::XML;
use RPC::XML::Client;
use Data::Dumper;

# Write nicely
use strict;
use warnings;

# XML::RPC object
my $xmlrpc = new RPC::XML::Client(
	'http://localhost:6800/backend/public',
	error_handler	=> \&doError,
	fault_handler	=> \&doFault
) or die("could not connect to backend ($!)");


#
# Main
#

my $return;

# Server hello
$return = $xmlrpc->send_request('User.Hello', "perl_testclient/0.1", 1);
print "* Server hello:\n", Dumper(\$return), "\n";

# Backend stats
$return = $xmlrpc->send_request('System.Backend.Stats');
print "* Backend stats\n", Dumper(\$return), "\n";

# Database stats
$return = $xmlrpc->send_request('System.Database.Stats');
print "* Database stats\n", Dumper(\$return), "\n";


#
# Routines
#

sub doError {
	die("XML-RPC request failed at transport level ($_)");
}

sub doFault {
	my $fault = shift;
	my $code = $fault->{faultCode}->value;
	my $message = $fault->{faultString}->value;
	die("XML-RPC request failed at XMLRPC level (code $code: $message)");
}

