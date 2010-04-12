#!/usr/bin/perl

#
# Configuration
#

use lib '/home/tim_arch/Documents/School/Hoger (Hogeschool Gent)/3 Bachelor Toegepaste Ingenieurswetenschappen Informatica/Vakoverschrijdend project/src/scraper/lib';

# Packages
use RPC::XML;
use RPC::XML::Client;
use Data::Dumper;
use Compress::Zlib;
use HTTP::Message;
use HTTP::Headers;

# Write nicely
use strict;
use warnings;

# Compressions
my @compressions = HTTP::Message::decodable();
print "* Supported compressions: ", join(", ", @compressions), "\n";

# XML::RPC object
print "* Setting up XML-RPC\n";
$RPC::XML::Client::COMPRESSION_AVAILABLE = "gzip";
my $xmlrpc = new RPC::XML::Client(
	'http://localhost:6800/backend/public',
	error_handler	=> \&doError,
	fault_handler	=> \&doFault
) or die("could not connect to backend ($!)");
$xmlrpc->compress_requests(1);
$xmlrpc->compress_thresh(1); # Pick good threshold

#
# Main
#

my $return;

# Server hello
print "* Server hello\n";
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
	my $fault = shift;
	die("XML-RPC request failed at transport level ($fault)");
}

sub doFault {
	my $fault = shift;
	my $code = $fault->{faultCode}->value;
	my $message = $fault->{faultString}->value;
	die("XML-RPC request failed at XMLRPC level (code $code: $message)");
}

