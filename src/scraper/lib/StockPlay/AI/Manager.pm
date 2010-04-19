###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI::Manager;

=pod

=head1 NAME StockPlay::AI::Manager - StockPlay AI manager

=head1 DESCRIPTION

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use StockPlay::Factory;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote;
use Date::Manip;
use DateTime::Format::DateManip;
use StockPlay::AI::Data::Input;
use StockPlay::AI::Data::Output;
use StockPlay::AI::Forecaster::Neural;

# Roles
with 'StockPlay::Logger';

# Write nicely
use strict;
use warnings;

# Constants
my $DATA_START = DateTime::Format::DateManip->parse_datetime(ParseDate("2 year ago"));
my $DATA_END = DateTime::Format::DateManip->parse_datetime(ParseDate("1 month ago"));


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<plugins>

=cut

has 'factory' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Factory',
	required	=> 1
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;
}


sub run {
	my ($self) = @_;
	
	my $exchange = (grep { $_->symbol eq "BSE" } $self->factory->getExchanges())[0] or die();
	my $security = (grep { $_->isin eq "BE0003755692" } $self->factory->getSecurities($exchange))[0] or die();
	my $index = (grep { $_->isin eq "BE0389555039" } $self->factory->getIndexes($exchange))[0] or die();
	
	
	# Load the quotes
	print "- Fetching data for security\n";
	my @quotes = $self->factory->getQuotes($DATA_START, $DATA_END, $security);
	@quotes = quote_truncate( @quotes );

	# Load the index quotes
	print "- Fetching data for index\n";
	my @quotes_index = $self->factory->getQuotes($DATA_START, $DATA_END, $index);
	@quotes_index = quote_truncate( @quotes_index );
	die() unless @quotes_index;

	# Build a set of training data
	print "- Building training data\n";
	my $index_closing = 0;
	my (@inputs, @outputs);
	for (my $i = 0; $i < @quotes-2; $i++) {
		my $quote = $quotes[$i];
		
		# Look for index closing price
		my $index_quote = (grep { DateTime->compare($_->time, $quotes[$i+1]->time) == 0} @quotes_index)[0];
		if (defined $index_quote) {
			$index_closing = $index_quote->open;
		} else {
			print "! Warning: could not find index quote for " . $quote->time->year . "-" . $quote->time->month . "-". $quote->time->day . "\n";
		}
		
		# Generate inputs
		push(@inputs, new StockPlay::AI::Data::Input(
			closing		=> $quotes[$i+1]->open,
			low		=> $quote->low,
			high		=> $quote->high,
			volume		=> $quote->volume,
			closing_index	=> $index_closing,
			date		=> $quote->time
		));
		
		# Generate outputs
		push(@outputs, new StockPlay::AI::Data::Output(
			closing		=> $quotes[$i+2]->open
		));
	}
	
	# Pass data to forecaster
	my $forecaster = new StockPlay::AI::Forecaster::Neural;
	my @inputs_proc = $forecaster->preprocess_input(@inputs);
	my @outputs_proc = $forecaster->preprocess_output(@outputs);
	my (@inputs_test, @outputs_test);
	for (1 .. 10) {
		unshift(@outputs_test, pop(@outputs_proc));
		unshift(@inputs_test, pop(@inputs_proc));
	}
	$forecaster->train(\@inputs_proc, \@outputs_proc);
	for (my $i = 0; $i < @inputs_test; $i++) {
		my ($input_proc, $output_proc) = ($inputs_test[$i], $outputs_test[$i]);
		my $forecast_proc = $forecaster->run($input_proc);
		
		my $output = ($forecaster->postprocess_output($output_proc))[0];
		my $forecast = ($forecaster->postprocess_output($forecast_proc))[0];
		
		print "Forecasted ", $forecast->closing, " while expected ", $output->closing, "\n";
	}
	
}

################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut

sub quote_truncate {
	my (@quotes) = @_;
	@quotes = sort { DateTime->compare($a->time, $b->time) } @quotes;
	my @quotes_truncated;
	my $quote_previous;
	foreach my $quote (@quotes) {
		# New quote
		if (not defined $quote_previous) {
			$quote_previous = $quote;
		}
		
		# Quote at same day
		elsif ($quote->time->year == $quote_previous->time->year
				&& $quote->time->month == $quote_previous->time->month
				&& $quote->time->day == $quote_previous->time->day) {
			# Prefer most recent quote
			if (DateTime->compare($quote_previous->time, $quote->time) == -1) {
				$quote_previous = $quote;
			}
		}
		
		# Other day
		else {
			push @quotes_truncated, $quote_previous;
			$quote_previous = $quote;
		}
	}
	return @quotes_truncated;
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


