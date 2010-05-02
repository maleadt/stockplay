################################################################################
# Configuration
#

# Package definition
package StockPlay::Factory;

=pod

=head1 NAME StockPlay::Scraper::Factory - StockPlay object factory

=head1 DESCRIPTION

The C<StockPlay::Scraper::Factory> package contains functionality to load and
push data to a remove StockPlay backend. It is the only module which directly
uses XML-RPC to contact the backend, and should provide all necessary
functionality for other modules to never use XML-RPC itself.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use RPC::XML qw/:types/;
use RPC::XML::GzipClient;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote;
use DateTime::Format::ISO8601;
use StockPlay::Configuration;

# Roles
with 'StockPlay::Logger';
with 'StockPlay::Configurable';

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

has 'xmlrpc' => (
	is		=> 'ro',
	isa		=> 'RPC::XML::GzipClient',
	lazy		=> 1,
	builder		=> '_build_xmlrpc'
);

=pod

=head2 C<$factory->_build_xmlrpc>

Private method, builds the xmlrpc attribute. This method is also responsible
for compression management, containing a boolean which enables (or disables)
content encoding.

=cut

sub _build_xmlrpc {
	my ($self) = @_;

	# Disable compression with
	#$RPC::XML::Client::COMPRESSION_AVAILABLE = "";
	
	my $xmlrpc = RPC::XML::GzipClient->new(
		$self->config->get('server'),
		error_handler	=> \&doError,
		fault_handler	=> \&doFault
	) or die("could not connect to backend ($!)");

	if ($xmlrpc->{__compress} eq "gzip") {
		$self->logger->info("using Gzip-compression for requests and replies");
		$xmlrpc->compress_requests(1);
	}
	
	return $xmlrpc;
}


################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$factory->BUILD>

Object constructor, loads lazy attributes, connects to the server, and sends
an initial HELLO message.

=cut

sub BUILD {
	my ($self) = @_;
	
	# Default configuration
	$self->config->set_default('server', 'http://localhost:6800/backend/public');
	
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

=pod

=head2 C<$factory->buildExchanges($selector)>

This method builds a deep hierarchy of objects, by recursivly fetching
all data available (with exception to quotes: only the latest one is fetched).
The only argument is a selector, which is used to narrow the selection of
exchanges.

# TODO: ook selector voor indexes enzo

=cut

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

=pod

=head2 C<$factory->getExchanges>

This method fetches a list of exchanges, and converts it to StockPlay::Exchange
objects.

=cut

sub getExchanges {
	my ($self) = @_;
	
	# Request exchanges from server
	my @s_exchanges = @{$self->xmlrpc->send_request(
		'Finance.Exchange.List'
	)->value};
	
	# Build StockPlay::Exchange objects
	my @exchanges;
	foreach my $s_exchange (@s_exchanges) {
		my $exchange = StockPlay::Exchange->new(
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

=pod

=head2 C<$factory->getIndexes($exchange)>

This method fetches a list of indexes on a given exchange, and converts it to
StockPlay::Exchange objects. It does however not fetches the securities linked
with the index (although that is a field of the Index object).

=cut

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
		my $index = StockPlay::Index->new(
			isin		=> $s_index->{ISIN},
			symbol		=> $s_index->{SYMBOL}
		);
		$index->name($s_index->{NAME}) if (defined $s_index->{NAME});
		push(@indexes, $index);
	}
	
	return @indexes;	
}

=pod

=head2 C<$factory->getSecurities($exchange)>

This method fetches a list of securities on a given exchange, and converts it to
StockPlay::Exchange objects. It does however not fetches the quotes linked
with the securities (although that is a field of the Security object).

=cut

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
		my $security = StockPlay::Security->new(
			isin		=> $s_security->{ISIN},
			symbol		=> $s_security->{SYMBOL}
		);
		$security->name($s_security->{NAME}) if (defined $s_security->{NAME});
		push(@securities, $security);
	}
	
	return @securities;	
}

=pod

=head2 C<$factory->getIndexSecurities($exchange, $index)>

This method fetches a list of securities on a given index, and returns them.
The objects are no new instantiations, but are references to the existing ones
in the exchange object.

=cut

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

=pod

=head2 C<$factory->getLatestQuotes(@securities)>

This method fetches the latest quotes linked to each passed security, and
returns them.

=cut

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
		my $quote = StockPlay::Quote->new(
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

=pod

=head2 C<$factory->getQuotes($start, $end, $security)>

This method fetches and returns all the quotes from a given security between
two dates.

=cut

sub getQuotes {
	my ($self, $start, $end, $security, $span) = @_;
	
	my @s_quotes;
	if (defined $span) {
		# Build a filter
		my $isin = $security->isin;
		my $filter = "isin == '" . $isin . "'s";
		
		# Request quotes from the server
		@s_quotes = @{$self->xmlrpc->send_request(
			'Finance.Security.Quotes',
			hack_datetime(RPC_DATETIME_ISO8601($start)),
			hack_datetime(RPC_DATETIME_ISO8601($end)),
			RPC_INT($span),
			$filter
		)->value};
	} else {	
		# Get appropriate dates
		$start->set_time_zone("UTC");
		$end->set_time_zone("UTC");
		my $start_string = $start->strftime('%Y-%m-%dT%H:%MZ');
		my $end_string = $end->strftime('%Y-%m-%dT%H:%MZ');
		
		# Build a filter
		my $isin = $security->isin;
		my $filter = "timestamp >= '" . $start_string . "'d && timestamp < '" . $end_string . "'d && isin == '" . $isin . "'s";
		
		# Request quotes from the server
		@s_quotes = @{$self->xmlrpc->send_request(
			'Finance.Security.Quotes',
			$filter
		)->value};
	}
	
	# Build StockPlay::Quote objects
	my @quotes;
	foreach my $s_quote (@s_quotes) {
		my $quote = StockPlay::Quote->new(
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

sub getQuoteRange {
	my ($self, $security) = @_;
	
	# Request range from the server
	my @s_range = @{$self->xmlrpc->send_request(
		'Finance.Security.QuoteRange',
		RPC_STRING($security->isin)
	)->value};
	
	return (DateTime::Format::ISO8601->parse_datetime($s_range[0]), DateTime::Format::ISO8601->parse_datetime($s_range[1]));	
}

=pod

=head2 C<$factory->createQuotes(@quotes)>

This method sends new quotes to the backend. The security field in the Quote
object is currently used to link the given quotes to the correct security.
This method uses bulk upload, so if one quote fails, none of them get saved.

# TODO: deze ref wordt nooit gedaan buiten bij quotes. Mss pairs van
# security -> quote?

=cut

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
	
	return 1;
}

=pod

=head2 C<$factory->createExchange($exchange)>

This method sends a new exchange to the remote backend.

=cut

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
	
	return 1;
}

=pod

=head2 C<$factory->createIndex($exchange, $index)>

This method sends a new index to the remote backend.

=cut

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
	
	return 1;
}

=pod

=head2 C<$factory->createIndexSecurity($index, $security)>

This method remotely links an existing security to an existing index.

=cut

sub createIndexSecurity {
	my ($self, $index, $security) = @_;
	
	# Build an XML-RPC compatible representation of the IndexSecuritiy
	my %s_indexsecurity = (
		index_isin	=> RPC_STRING($index->isin),
		security_isin	=> RPC_STRING($security->isin)
	);
	
	# Send the index to the server
	$self->xmlrpc->send_request('Finance.IndexSecurity.Create', \%s_indexsecurity);
	
	return 1;
}

=pod

=head2 C<$factory->createSecurity($exchange, $security)>

This method sends a new exchange to the remote backend.

=cut

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
	
	return 1;
}


################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=head2 C<doError>

This static method is used as error handler to the internally-used C<RPC::XML>
object. It intelligently handles eval- and parse-time errors to avoid
bloat in the logfiles. Errors are reported when problems occur on the
transport level (eg. HTTP or TCP).

=cut

sub doError {
	# Don't add bloat when happening in eval cause
	die($_[0]."\n") unless (defined $^S && $^S == 0);
	
	my $error = shift;
	chomp $error;
	die("XML-RPC request failed at transport level ($error)\n");
}

=pod

=head2 C<doFault>

This static method is used as fault handler to the internally-used C<RPC::XML>
object. It intelligently handles eval- and parse-time errors to avoid
bloat in the logfiles. Faults are reported when problems occur on the
XML-RPC error (eg. XLM-RPC errors).

=cut

sub doFault {
	my $fault = shift;
	my $code = $fault->{faultCode}->value;
	my $message = $fault->{faultString}->value;
	chomp $message;
	
	# DO add bloat (because the RPC fault hash needs to be stringified
	unless (defined $^S && $^S == 0) {
		die("code $code: $message\n");
	} else {		
		die("XML-RPC request failed at XMLRPC level (code $code: $message)\n");
	}
}

=pod

=head2 C<hack_datetime($datetime)>

The XML-RPC spec is a bit strange concerning datetimes. The spec uses the
ISO-invalid YY-MM-DD (...) format, which is correctly implemented by the
RPC::XML library. Java's ws-xmlrpc however decided not to support that
datetime-format, but uses the ISO-correct YYYYMMDD (...) format. As however
ws-xmlrpc also fails at decoding the by-spec format, we kinda hack the 
RPC::XML::DateTime object to get it parsed by ws-xmlrpc after transmission.

=cut

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
