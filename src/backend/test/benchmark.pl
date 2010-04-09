#!/usr/bin/perl

#
# Configuration
#

# Packages
use RPC::XML;
use RPC::XML::Client;
use Benchmark::Timer;

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

test('User.Hello', "benchmark_perl", 1);
test('System.Backend.Stats');
test('System.Database.Stats');
test('Finance.Exchange.List', "symbol EQUALS 'BSE'");
test('Finance.Index.List', "exchange EQUALS 'BSE'");
test('Finance.Security.List', "exchange EQUALS 'BSE'");


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

sub test {
	my $method = shift;
	print "* $method: ";
	my $t = Benchmark::Timer->new(skip => 1, confidence => 97.5, error => 2);
	my @output;
	while($t->need_more_samples($method)) {
		$t->start($method);
		@output = $xmlrpc->send_request($method, @_);
		$t->stop($method);
	}
	print $t->result($method)*1000, " ms per request\n";
	return @output;
}
