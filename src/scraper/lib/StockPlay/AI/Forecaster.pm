###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI::Forecaster;
# TODO: also load through PluginManager

=pod

=head1 NAME

StockPlay::AI::Forecaster - StockPlay AI forecaster role

=head1 DESCRIPTION

This package serves as main role for forecasters, aiding that by providing
some helper routines, as well as defining the interface forecasters should
implement.

=head1 SYNPOSIS

=cut

# Packages
use Moose::Role;

# Roles
with 'StockPlay::Plugin';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut


################################################################################
# Methods

=pod

=head1 METHODS

=head2 C<$forecaster->preprocess_input(@inputs)>

Input preprocessor. This is used to convert raw input data to an internal
representation (which doesn't even have to be StockPlay::AI::Data objects).

=cut

requires 'preprocess_input';

=pod

=head2 C<$forecaster->preprocess_output(@outputs)>

Output preprocessor. This is used to convert raw output data to an internal
representation (which doesn't even have to be StockPlay::AI::Data objects).

=cut

requires 'preprocess_output';

=pod

=head2 C<$forecaster->postprocess_output(@internal_outputs)>

Output postprocessor. This is used to convert internal output data back to the
regular representation in terms of StockPlay::AI::Data objects.

=cut

requires 'postprocess_output';

=pod

=head2 C<$forecaster->train($inputs, $outputs)>

This method is called with sets of known data to train the forcaster.

=cut

requires 'train';

=pod

=head2 C<$forecaster->run(@inputs)>

This method runs (but does not train) the network against unknown values.
Predicted output values are calculated and returned as an array. The first
returned value is the mean square error of the output values.

=cut

requires 'run';

=pod

=head2 C<test($dataset)>

A stub testing routine.

=cut

sub test {
	my ($self, $dataset) = @_;
	
	my $mse = 0;
	foreach my $pair (@{$dataset}) {
		my ($input, $output) = @{$pair};
		my $forecast = $self->run($input);
		
		#$mse += $outputs->mse($forecast);
	}
	$mse /= @{$dataset};
	
	return $mse;
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

=head1 COPYRIGHT

Copyright 2010 The StockPlay development team as listed in the AUTHORS file.

This software is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public Licence (GPL) as published by the
Free Software Foundation (FSF).

The full text of the license can be found in the
LICENSE file included with this module.

=cut
