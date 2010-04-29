###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI;

=pod

=head1 NAME

StockPlay::AI - StockPlay AI manager

=head1 DESCRIPTION

This is the main library used to construct an artificial player. It gathers
and manipulates the needed datasets, instantiates plugins, manages the 
portfolio, etc.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use StockPlay::PluginManager;
use StockPlay::Factory;
use StockPlay::Exchange;
use StockPlay::Index;
use StockPlay::Security;
use StockPlay::Quote;
use Date::Manip;
use DateTime::Format::DateManip;
use StockPlay::AI::Data::Input;
use StockPlay::AI::Data::Output;

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

has 'factory' => (
	is		=> 'ro',
	isa		=> 'StockPlay::Factory',
	required	=> 1
);

has 'forecasters' => (
	is		=> 'ro',
	isa		=> 'ArrayRef',
	lazy		=> 1,
	builder		=> '_build_forecasters'
);

sub _build_forecasters {
	my ($self) = @_;	
	$self->logger->info("loading all forecasters");
	
	# Get infohashes
	$self->pluginmanager->load_group('StockPlay::AI::Forecaster');
	my @infohashes = $self->pluginmanager->get_group('StockPlay::AI::Forecaster');

	# Load plugins
	my @forecasters;
	foreach my $infohash (@infohashes) {
		$self->logger->info("loading plugin " . $infohash->{name});
		eval {
			my $forecaster = $self->pluginmanager->instantiate($infohash);
			push(@forecasters, $forecaster);
		};
		if ($@) {
			chomp $@;
			$self->logger->error("failed to load plugin ($@)");
		}
	}
	
	unless (@forecasters) {
		die("no plugins managed to load correctly");
	}
	
	return \@forecasters;
}

has 'pluginmanager' => (
	is		=> 'ro',
	isa		=> 'StockPlay::PluginManager',
	required	=> 1
);

# Session-wide cache, needs incremental data fetching
has 'quotes' => (
	is		=> 'ro',
	isa		=> 'HashRef[ArrayRef[StockPlay::Quote]]',
	default		=> sub { {} }
);

# Per-session cache
has 'latestquote' => (
	is		=> 'ro',
	isa		=> 'HashRef[StockPlay::Quote]',
	default		=> sub { {} }
);


################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$daemon->BUILD>

The object constructor. Builds pseudo-lazy attributes which depend on values
passed by constructor.

=cut

sub BUILD {
	my ($self) = @_;
	
	# Build lazy attributes which depend on passed values
	$self->forecasters;
	
	return;
}

=pod

=head2 C<$manager->run>

Main run loop. This will continuously scan all securities on all exchanges,
attempt to accuratly predict the course for the following day (using the
available forecasters, and pick the one with the smallest rate of error), and
using those predicted courses collect an optimal portfolio.

=cut


sub run {
	my ($self) = @_;
	$self->logger->info("entering main loop");
	
	# Get references to the active exchange and index
	my $exchange = (grep { $_->symbol eq "BSE" } $self->factory->getExchanges())[0] or die();
	push(@{$exchange->securities}, $self->factory->getSecurities($exchange));
	my $index = (grep { $_->isin eq "BE0389555039" } $self->factory->getIndexes($exchange))[0] or die();
	push(@{$index->securities}, $self->factory->getIndexSecurities($exchange, $index));
	
	# Process all securities
	my @forecasts;
	foreach my $security (@{$index->securities}) {
		$self->logger->info("processing " . $security->name);
		
		# Check if we got up-to-date data
		my ($start, $end) = $self->factory->getQuoteRange($security);
		my $time_ago = DateTime->now->subtract_datetime($end);
		if ($time_ago->delta_days > 1) {	# TODO: check if after weekend (or dont run saturday/sunday)
			$self->logger->warn("most recent quote was too long ago, skipping");
			next;
		}
		$start = $start->truncate(to => 'day');
		
		my ($input, $output) = $self->forecast($exchange, $index, $security, $start, $end);
		push(@forecasts, {
			security	=> $security,
			input		=> $input,
			output		=> $output,
			delta		=> $output->closing - $input->closing
		});
	}
	
	# Select optimal portfolio
	my @portfolio = (
		sort { $a->delta / $a->input->closing  <=>  $b->delta / $b->input->closing }
		@forecasts
	)[1..5];
	
	return;	
}

sub forecast {
	my ($self, $exchange, $index, $security, $start, $end) = @_;
	
	# Get historic data
	my ($inputs, $outputs) = $self->get_data_historic($index, $security, $start, $end);
	die("weird amount of data received") unless (scalar @{$inputs} == scalar @{$outputs});
	
	# Get todays data
	my ($input_today) = $self->get_data_today($index, $security);
	
	# Check all available forecasters
	my ($output_today, $mse);	
	foreach my $forecaster (@{$self->forecasters}) {	
		# Pass data to forecaster
		my @inputs_proc = $forecaster->preprocess_input(@{$inputs}, $input_today);
		my @outputs_proc = $forecaster->preprocess_output(@{$outputs});
		my $input_today_proc = pop(@inputs_proc);
		$forecaster->train(\@inputs_proc, \@outputs_proc);
		
		# Forecast a value for tomorrow
		my ($mse_forecaster, @outputs_today_proc) = $forecaster->run($input_today_proc);
		if (not defined $mse or $mse_forecaster < $mse) {
			my @outputs_today = $forecaster->postprocess_output($outputs_today_proc[0]);
			$output_today = $outputs_today[0];
			$mse = $mse_forecaster;			
		}
	}
	
	return ($input_today, $output_today);
}

sub get_data_historic {
	my ($self, $index, $security, $start, $end) = @_;
	$self->logger->debug("generating training data for " . $security->name);
	
	# Load the quotes
	unless (defined $self->quotes->{$security->isin}) {
		$self->logger->debug("fetching quotes for security");
		my @temp = $self->factory->getQuotes($start, $end, $security, 60*60*24);
		$self->quotes->{$security->isin} = \@temp;
	}
	my @quotes = @{$self->quotes->{$security->isin}};
	die("could not find any quotes for security") unless (@quotes);

	# Load the index quotes
	unless (defined $self->quotes->{$index->isin}) {
		$self->logger->debug("fetching quotes for index");
		my @temp = $self->factory->getQuotes($start, $end, $index, 60*60*24);
		$self->quotes->{$index->isin} = \@temp;
	}
	my @quotes_index = @{$self->quotes->{$index->isin}};
	die("could not find any quotes for index") unless (@quotes_index);

	# Build a set of training data
	my $index_closing = 0;
	my (@inputs, @outputs);
	for (my $i = 0; $i < @quotes-2; $i++) {
		my $quote = $quotes[$i];
		
		# Look for index closing price
		my $index_quote = (grep { DateTime->compare($_->time, $quotes[$i+1]->time) == 0} @quotes_index)[0];
		if (defined $index_quote) {
			$index_closing = $index_quote->open;
		} else {
			$self->logger->warn("could not find index quote for " . $quote->time->ymd);
		}
		
		# Generate inputs
		push(@inputs, StockPlay::AI::Data::Input->new(
			closing		=> $quotes[$i+1]->open,
			low		=> $quote->low,
			high		=> $quote->high,
			volume		=> $quote->volume,
			closing_index	=> $index_closing,
			date		=> $quote->time
		));
		
		# Generate outputs
		push(@outputs, StockPlay::AI::Data::Output->new(
			closing		=> $quotes[$i+2]->open
		));
	}
	
	return (\@inputs, \@outputs);
}

sub get_data_today {
	my ($self, $index, $security) = @_;
	$self->logger->debug("generating latest data for " . $security->name);
	
	# Load the latest quote
	unless (defined $self->latestquote->{$security->isin}) {
		$self->logger->debug("fetching latest quote for security");
		($self->latestquote->{$security->isin}) = $self->factory->getLatestQuotes($security);
	}
	my ($quote) = $self->latestquote->{$security->isin};
	die("could not find latest quote for security") unless ($quote);

	# Load the latest index quote
	unless (defined $self->latestquote->{$index->isin}) {
		$self->logger->debug("fetching latest quote for index");
		($self->latestquote->{$index->isin}) = $self->factory->getLatestQuotes($index);
	}
	my ($quote_index) = $self->latestquote->{$index->isin};
	die("could not find latest quote for index") unless ($quote_index);
	
	return StockPlay::AI::Data::Input->new(
			closing		=> $quote->price,
			low		=> $quote->low,
			high		=> $quote->high,
			volume		=> $quote->volume,
			closing_index	=> $quote_index->price,
			date		=> $quote->time
	);
}

################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut
1;

__END__

=pod

=head1 TODO

- Cache fetched quotes between sessions
- Only fetch newest quotes since most recent fetch
- Calculate input value from LatestQuote

=head1 COPYRIGHT

Copyright 2010 The StockPlay development team as listed in the AUTHORS file.

This software is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public Licence (GPL) as published by the
Free Software Foundation (FSF).

The full text of the license can be found in the
LICENSE file included with this module.

=cut
