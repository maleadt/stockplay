################################################################################
# Configuration
#

# Package definition
package StockPlay::Factory;

=pod

=head1 NAME StockPlay::Scraper::Factory - StockPlay object factory

=head1 DESCRIPTION

The C<StockPlay::Scraper::Factory> package contains functionality to load and
push data to a remove StockPlay backend.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use RPC::XML qw/:types/;
use RPC::XML::Client;
use Compress::Zlib;
use HTTP::Message;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote;

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<plugins>

=cut

has 'server' => (
	is		=> 'ro',
	isa		=> 'Str',
	required	=> 1
);

has 'xmlrpc' => (
	is		=> 'ro',
	isa		=> 'RPC::XML::Client',
	lazy		=> 1,
	builder		=> '_build_xmlrpc'
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;
	
	# Build data members which require other data members
	$self->xmlrpc;
	
	# Verify server connection & protocol version by sending a HELLO
	eval {
		$self->xmlrpc->send_request('User.Hello', "perl_factory/0.1", 1);
	};
	if ($@) {
		die("! Connection failed: $@");
	}
}

sub _build_xmlrpc {
	my ($self) = @_;
	
	my $xmlrpc = new RPC::XML::Client(
		$self->server,
		error_handler	=> \&doError,
		fault_handler	=> \&doFault,
		useragent	=> [
			default_header => [ 'Accept-Encoding' => scalar HTTP::Message::decodable() ]
		]
	) or die("could not connect to backend ($!)");
	
	return $xmlrpc;
}

sub buildExchanges {
	my ($self, $selector) = @_;
	
	# Get all exchanges
	my @exchanges = $self->getExchanges();
	if (defined $selector) {
		@exchanges = $selector->(@exchanges);
	}
	
	# Get all indexes
	foreach my $exchange (@exchanges) {
		my @indexes = $self->getIndexes($exchange);
		push(@{$exchange->indexes}, @indexes);
	}
	
	# Get all securities
	foreach my $exchange (@exchanges) {
		my @securities = $self->getSecurities($exchange);
		push(@{$exchange->securities}, @securities);
	}
	
	return @exchanges;
}

sub getExchanges {
	my ($self) = @_;
	
	# Request exchanges from server
	my @s_exchanges = @{$self->xmlrpc->send_request(
		'Finance.Exchange.List'
	)->value};
	
	# Build StockPlay::Exchange objects
	my @exchanges;
	foreach my $s_exchange (@s_exchanges) {
		my $exchange = new StockPlay::Exchange(
			symbol		=> $s_exchange->{SYMBOL},
			name		=> $s_exchange->{NAME},
			location	=> $s_exchange->{LOCATION}
		);
		push(@exchanges, $exchange);
	}
	
	return @exchanges;	
}

sub getIndexes {
	my ($self, $exchange) = @_;
	
	# Request indexes from server
	my @s_indexes = @{$self->xmlrpc->send_request(
		'Finance.Index.List',
		"exchange EQUALS '" . $exchange->symbol . "'"
	)->value};
	
	# Build StockPlay::Index objects
	my @indexes;
	foreach my $s_index (@s_indexes) {
		my $index = new StockPlay::Index(
			name		=> $s_index->{NAME} # TODO: securities
		);
		push(@indexes, $index);
	}
	
	return @indexes;	
}

sub getSecurities {
	my ($self, $exchange) = @_;
	
	# Request securities from server
	my @s_securities = @{$self->xmlrpc->send_request(
		'Finance.Security.List',
		"exchange EQUALS '" . $exchange->symbol . "'"
	)->value};
	
	# Build StockPlay::Security objects
	my @securities;
	foreach my $s_security (@s_securities) {
		my $security = new StockPlay::Security(
			isin		=> $s_security->{ISIN},
			symbol		=> $s_security->{SYMBOL},
			name		=> $s_security->{NAME}
		);
		push(@securities, $security);
	}
	
	return @securities;	
}

sub createQuotes {
	my ($self, @quotes) = @_;
	
	# Build an XML-RPC compatible representation of the quotes
	my @s_quotes;
	foreach my $quote (@quotes) {
		my %s_quote = (
			isin	=> RPC_STRING($quote->security),
			time	=> hack_datetime(RPC_DATETIME_ISO8601($quote->time)),
			price	=> RPC_DOUBLE($quote->price || 0),
			low	=> RPC_DOUBLE($quote->low || 0),
			high	=> RPC_DOUBLE($quote->high || 0),
			open	=> RPC_DOUBLE($quote->open || 0),
			bid	=> RPC_DOUBLE($quote->bid || 0),
			ask	=> RPC_DOUBLE($quote->ask || 0),
			volume	=> RPC_INT($quote->volume || 0)			
		);
		push(@s_quotes, \%s_quote);
	}
	
	# Send the quotes to the server
	$self->xmlrpc->send_request('Finance.Security.UpdateBulk', \@s_quotes);	
}

sub createExchange {
	my ($self, $exchange) = @_;
	
	# Build an XML-RPC compatible representation of the exchange
	my %s_exchange = (
		symbol		=> RPC_STRING($exchange->symbol),
		name		=> RPC_STRING($exchange->name),
		location	=> RPC_STRING($exchange->location || ''),
	);
	
	# Send the exchange to the server
	$self->xmlrpc->send_request('Finance.Exchange.Create', \%s_exchange);
}

sub createIndex {
	my ($self, $exchange, $index) = @_;
	
	# Build an XML-RPC compatible representation of the index
	my %s_index = (
		name		=> RPC_STRING($index->name),
		exchange	=> RPC_STRING($exchange->symbol),
	);
	
	# Send the index to the server
	$self->xmlrpc->send_request('Finance.Index.Create', \%s_index);
}

sub createSecurity {
	my ($self, $exchange, $security) = @_;
	
	# Build an XML-RPC compatible representation of the security
	my %s_security = (
		isin		=> RPC_STRING($security->isin),
		symbol		=> RPC_STRING($security->symbol),
		name		=> RPC_STRING($security->name),
		exchange	=> RPC_STRING($exchange->symbol)
	);
	
	# Send the security to the server
	$self->xmlrpc->send_request('Finance.Security.Create', \%s_security);
}


################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut

sub doError {
	my $error = shift;
	die("XML-RPC request failed at transport level ($error)\n");
}

sub doFault {
	my $fault = shift;
	my $code = $fault->{faultCode}->value;
	my $message = $fault->{faultString}->value;
	die("XML-RPC request failed at XMLRPC level (code $code: $message)\n");
}

# RPC::XML sends the invalid, but by-spec requested YYYY-MM-DD (...) format,
# while ws-xmlrpc is only able to parse the more widely used yet here invalid
# YYYYMMDD format (FYI: this is extremely nasty).
sub hack_datetime {
	my $datetime = shift;
	my $value = $datetime->value;
	$value =~ s/-//g;
	return bless \$value, "RPC::XML::datetime_iso8601";
}

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
