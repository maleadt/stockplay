###############################################################################
# Configuration
#
## NAME		= Neural
## DESCRIPTION	= Forecaster based on neural networks.
## TYPE		= concrete

# Package definition
package StockPlay::AI::Forecaster::Neural;

=pod

=head1 NAME StockPlay::AI::Forecaster::Neural - StockPlay AI neural forecaster

=head1 DESCRIPTION

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use AI::FANN qw(:all);
use List::Util qw(max min);
use StockPlay::AI::Data::Input;
use StockPlay::AI::Data::Output;

# Write nicely
use strict;
use warnings;

# Consume roles
with 'StockPlay::AI::Forecaster';



################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut

has 'network' => (
	is	=> 'ro',
	isa	=> 'AI::FANN',
	lazy	=> 1,
	builder	=> '_builder_network'
);

sub _builder_network {
	my ($self) = @_;
	my $ann;
	$ann = AI::FANN->new_shortcut(10, 1);
	$ann->training_algorithm(FANN_TRAIN_RPROP);
	$ann->hidden_activation_function(FANN_SIGMOID_SYMMETRIC);
	$ann->output_activation_function(FANN_LINEAR_PIECE);
	$ann->train_error_function(FANN_ERRORFUNC_LINEAR);
	return $ann;	
}

has 'scales' => (
	is	=> 'ro',
	isa	=> 'HashRef[Num]',
	default	=> sub { { } }
);


################################################################################
# Methods

=pod

=head1 METHODS

=cut

sub BUILD {
	my ($self) = @_;
	
	# Build lazy attributes
	$self->network;
	
}

sub preprocess_input {
	my ($self, @inputs) = @_;
	
	# Expand
	my @inputs_proc;
	foreach my $input (@inputs) {
		push(@inputs_proc, [
			$input->closing,
			$input->high,
			$input->low,
			$input->volume,
			$input->closing_index,
			$input->date->dow == 1 ? 1 : 0,
			$input->date->dow == 2 ? 1 : 0,
			$input->date->dow == 3 ? 1 : 0,
			$input->date->dow == 4 ? 1 : 0,
			$input->date->dow == 5 ? 1 : 0
		]);		
	}
	
	# Scale
	$self->scale("input", \@inputs_proc, 0);
	$self->scale("input", \@inputs_proc, 1);
	$self->scale("input", \@inputs_proc, 2);
	$self->scale("input", \@inputs_proc, 3);
	$self->scale("input", \@inputs_proc, 4);
	
	return @inputs_proc;
}

sub preprocess_output {
	my ($self, @outputs) = @_;
	
	# Expand
	my @outputs_proc;
	foreach my $output (@outputs) {
		push(@outputs_proc, [
			$output->closing
		]);
	}
	
	# Scale
	$self->scale("output", \@outputs_proc, 0);
	
	return @outputs_proc;
}

sub postprocess_output {
	my ($self, @outputs_proc) = @_;
	
	# Descale
	$self->descale("output", \@outputs_proc, 0);
	
	# Implode
	my @outputs;
	foreach my $output_proc (@outputs_proc) {
		push(@outputs, new StockPlay::AI::Data::Output(
			closing	=> $output_proc->[0]
		));
	}
		
	return @outputs;
}


sub train {
	my ($self, $inputs, $outputs) = @_;
	
	# Merge datasets
	my @dataset;
	die("I need equal amounts of input and output data") unless (@{$inputs} == @{$outputs});
	my $sets = @{$inputs};
	for (my $i = 0; $i < $sets; $i++) {
		my ($input, $output) = ($inputs->[$i], $outputs->[$i]);
		push(@dataset, $input, $output);
	}
	
	# Create training data
	use Data::Dumper;
	print Dumper(\@dataset);
	exit;
	my $train = AI::FANN::TrainData->new(@dataset);
	$self->network->cascadetrain_on_data(
		$train,
		40,	# Amount of neurons
		1,	# Neurons between information prints
		0.0001	# Desired rate of error
	);
	$self->network->print_connections();
}

sub run {
	my ($self, $input) = @_;
	
	return $self->network->run($input);
}


################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=cut

sub scale {
	my ($self, $tag, $arrayref, $index) = @_;
	
	my $max = max map { $_->[$index] } @$arrayref;
	my $min = min map  { $_->[$index] } @$arrayref;
	for (my $i = 0; $i < @$arrayref; $i++) {
		$arrayref->[$i]->[$index] = ($arrayref->[$i]->[$index] - $min) / ($max - $min);
	}
	
	$self->scales->{$tag.$index."min"} = $min;
	$self->scales->{$tag.$index."max"} = $max;
}

sub descale {
	my ($self, $tag, $arrayref, $index) = @_;
	my $min = $self->scales->{$tag.$index."min"};
	my $max = $self->scales->{$tag.$index."max"};
	
	for (my $i = 0; $i < @$arrayref; $i++) {
		$arrayref->[$i]->[$index] = ($max - $min) * $arrayref->[$i]->[$index] + $min;
	}
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
