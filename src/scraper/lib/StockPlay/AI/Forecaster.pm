###############################################################################
# Configuration
#

# Package definition
package StockPlay::AI::Forecaster;
# TODO: also load through PluginManager

=pod

=head1 NAME StockPlay::AI::Forecaster - StockPlay AI forecaster interface

=head1 DESCRIPTION

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

=cut

requires 'preprocess_input';
requires 'preprocess_output';

requires 'postprocess_output';

requires 'train';

requires 'run';

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
