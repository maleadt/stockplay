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
use DateTime::Format::ISO8601;

# Roles
with 'StockPlay::Logger';

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
	default		=> 'http://localhost:6800/backend/public'
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
		chomp $@;
		die("could not connect to backend ($@)\n");
	}
}

sub _build_xmlrpc {
	my ($self) = @_;

	# Disable compression with
	#$RPC::XML::Client::COMPRESSION_AVAILABLE = "";
	
	my $xmlrpc = new RPC::XML::Client(
		$self->server,
		error_handler	=> \&doError,
		fault_handler	=> \&doFault
	) or die("could not connect to backend ($!)");

	if ($xmlrpc->{__compress} eq "gzip") {
		$self->logger->info("using Gzip-compression for requests and replies");
		$xmlrpc->compress_requests(1);
	}
	
	return $xmlrpc;
}

sub buildExchanges {
	my ($self, $selector) = @_;
	
	# Get all exchanges
	my @exchanges = $self->getExchanges();
	if (defined $selector) {
		@exchanges = $selector->(@exchanges);
	}
	
	# Get all securities
	foreach my $exchange (@exchanges) {
		my @securities = $self->getSecurities($exchange);
		push(@{$exchange->securities}, @securities);
	}
	
	# Get all indexes
	foreach my $exchange (@exchanges) {
		my @indexes = $self->getIndexes($exchange);
		push(@{$exchange->indexes}, @indexes);
		
		# Get all securities on the index
		foreach my $index (@{$exchange->indexes}) {
			my @securities = $self->getIndexSecurities($exchange, $index);
			push(@{$index->securities}, @securities);
		}
	}
	
	# Get all the latest quotes
	foreach my $exchange (@exchanges) {
		my @quotes = $self->getLatestQuotes(@{$exchange->securities});
		foreach my $quote (@quotes) {
			my $security = (grep { $_->isin eq $quote->security } @{$exchange->securities})[0];
			if (defined $security) {
				$security->quote($quote);
			}
		}
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
			symbol		=> $s_exchange->{SYMBOL}
		);
		if (defined $s_exchange->{NAME}) {
			$exchange->name($s_exchange->{NAME});
		}
		if (defined $s_exchange->{LOCATION}) {
			$exchange->location($s_exchange->{LOCATION});
		}
		push(@exchanges, $exchange);
	}
	
	return @exchanges;	
}

sub getIndexes {
	my ($self, $exchange) = @_;
	
	# Request indexes from server
	my @s_indexes = @{$self->xmlrpc->send_request(
		'Finance.Index.List',
		"exchange == '" . $exchange->symbol . "'"
	)->value};
	
	# Build StockPlay::Index objects
	my @indexes;
	foreach my $s_index (@s_indexes) {
		my $index = new StockPlay::Index(
			isin		=> $s_index->{ISIN},
			symbol		=> $s_index->{SYMBOL}
		);
		$index->name($s_index->{NAME}) if (defined $s_index->{NAME});
		push(@indexes, $index);
	}
	
	return @indexes;	
}

sub getSecurities {
	my ($self, $exchange) = @_;
	
	# Request securities from the server
	my @s_securities = @{$self->xmlrpc->send_request(
		'Finance.Security.List',
		"exchange == '" . $exchange->symbol . "'"
	)->value};
	
	# Build StockPlay::Security objects
	my @securities;
	foreach my $s_security (@s_securities) {
		my $security = new StockPlay::Security(
			isin		=> $s_security->{ISIN},
			symbol		=> $s_security->{SYMBOL}
		);
		$security->name($s_security->{NAME}) if (defined $s_security->{NAME});
		push(@securities, $security);
	}
	
	return @securities;	
}

sub getIndexSecurities {
	my ($self, $exchange, $index) = @_;
	
	# Request IndexSecurities from the server
	my @s_indexsecurities = @{$self->xmlrpc->send_request(
		'Finance.IndexSecurity.List',
		"index_isin == '" . $index->isin . "'"
	)->value};
	
	# Propagate those settings in the StockPlay::Security objects
	my @securities;
	foreach my $s_indexsecurity (@s_indexsecurities) {
		# Look for the security
		my $security_isin = $s_indexsecurity->{SECURITY_ISIN};
		my $security = (grep { $_->isin eq $security_isin } @{$exchange->securities})[0];		
		if (not defined $security) {
			$self->logger->error("could not find security $security_isin on exchange ", $exchange->name);
			next;
		}
		
		push(@securities, $security);
	}
	
	return @securities;
}

sub getLatestQuotes {
	my ($self, @securities) = @_;
	
	# Build a filter
	my @conditions = map { "isin == '" . $_->isin . "'" } @securities;
	my $filter = shift @conditions;
	map { $filter = "$_ || $filter" } @conditions;
	
	# Request quotes from the server
	my @s_quotes = @{$self->xmlrpc->send_request(
		'Finance.Security.LatestQuotes',
		$filter
	)->value};
	
	# Build StockPlay::Quote objects
	my @quotes;
	foreach my $s_quote (@s_quotes) {
		my $quote = new StockPlay::Quote(
			security	=> $s_quote->{ISIN},
			time		=> DateTime::Format::ISO8601->parse_datetime($s_quote->{TIME}),
			volume		=> $s_quote->{VOLUME},
			price		=> $s_quote->{PRICE},
			bid 		=> $s_quote->{BID},
			ask		=> $s_quote->{ASK},
			low		=> $s_quote->{LOW},
			high		=> $s_quote->{HIGH},
			open		=> $s_quote->{OPEN}
		);
		push(@quotes, $quote);
	}
	
	return @quotes;
}

sub getQuotes {
	my ($self, $start, $end, $security) = @_;
	
	# Get appropriate dates
	$start->set_time_zone("UTC");
	$end->set_time_zone("UTC");
	my $start_string = $start->strftime('%Y-%m-%dT%H:%MZ');
	my $end_string = $end->strftime('%Y-%m-%dT%H:%MZ');
	
	# Build a filter
	my $isin = $security->isin;
	my $filter = "timestamp >= '" . $start_string . "'d && timestamp < '" . $end_string . "'d && isin == '" . $isin . "'s";
	
	# Request quotes from the server
	my @s_quotes = @{$self->xmlrpc->send_request(
		'Finance.Security.Quotes',
		$filter
	)->value};
	
	# Build StockPlay::Quote objects
	my @quotes;
	foreach my $s_quote (@s_quotes) {
		my $quote = new StockPlay::Quote(
			security	=> $s_quote->{ISIN},
			time		=> DateTime::Format::ISO8601->parse_datetime($s_quote->{TIME}),
			volume		=> $s_quote->{VOLUME},
			price		=> $s_quote->{PRICE},
			bid 		=> $s_quote->{BID},
			ask		=> $s_quote->{ASK},
			low		=> $s_quote->{LOW},
			high		=> $s_quote->{HIGH},
			open		=> $s_quote->{OPEN}
		);
		push(@quotes, $quote);
	}
	
	return @quotes;
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
		symbol		=> RPC_STRING($exchange->symbol)
	);
	$s_exchange{name} = RPC_STRING($exchange->name) if $exchange->has_name;
	$s_exchange{location} = RPC_STRING($exchange->location) if $exchange->has_location;
	
	# Send the exchange to the server
	$self->xmlrpc->send_request('Finance.Exchange.Create', \%s_exchange);
}

sub createIndex {
	my ($self, $exchange, $index) = @_;
	
	# Build an XML-RPC compatible representation of the index
	my %s_index = (
		isin		=> RPC_STRING($index->isin),
		symbol		=> RPC_STRING($index->symbol),
		exchange	=> RPC_STRING($exchange->symbol),
	);
	$s_index{name} = RPC_STRING($index->name) if $index->has_name;
	
	# Send the index to the server
	$self->xmlrpc->send_request('Finance.Index.Create', \%s_index);		
}

sub createIndexSecurity {
	my ($self, $index, $security) = @_;
	
	# Build an XML-RPC compatible representation of the IndexSecuritiy
	my %s_indexsecurity = (
		index_isin	=> RPC_STRING($index->isin),
		security_isin	=> RPC_STRING($security->isin)
	);
	
	# Send the index to the server
	$self->xmlrpc->send_request('Finance.IndexSecurity.Create', \%s_indexsecurity);
}

sub createSecurity {
	my ($self, $exchange, $security) = @_;
	
	# Build an XML-RPC compatible representation of the security
	my %s_security = (
		isin		=> RPC_STRING($security->isin),
		symbol		=> RPC_STRING($security->symbol),
		exchange	=> RPC_STRING($exchange->symbol)
	);
	$s_security{name} = RPC_STRING($security->name) if $security->has_name;
	
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
	# Don't add bloat when happening in eval cause
	die($_[0]."\n") unless (defined $^S && $^S == 0);
	
	my $error = shift;
	chomp $error;
	die("XML-RPC request failed at transport level ($error)\n");
}

sub doFault {
	my $fault = shift;
	my $code = $fault->{faultCode}->value;
	my $message = $fault->{faultString}->value;
	chomp $message;
	
	# DO add bloat (because the RPC fault hash needs to be stringified
	unless (defined $^S && $^S == 0) {
		die("code $code: $message");
	} else {		
		die("XML-RPC request failed at XMLRPC level (code $code: $message)\n");
	}
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
