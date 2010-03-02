################################################################################
# Configuration
#
## NAME		= DeTijd
## SOURCE	= http://www.tijd.be/
## DESCRIPTION	= Scraper voor de website van De Tijd.
## TYPE		= concrete

# Package definition
package StockPlay::Scraper::Plugin::Website::DeTijd;

=pod

=head1 NAME

StockPlay::Scraper::Plugin::DeTijd - Scraper voor de website van De Tijd.

=head1 DESCRIPTION

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use JSON;
use HTML::TreeBuilder;
use StockPlay::Scraper::Plugin::Website;

# Roles
with 'StockPlay::Scraper::Plugin::Website';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=cut

has 'exchanges' => (
	is		=> 'ro',
	isa		=> 'HashRef[Str]',
	lazy		=> 1,
	builder		=> '_build_exchanges'
);

sub _build_exchanges {
	my ($self) = @_;
	
	# Fetch HTML
	my $res = $self->browser->get('http://www.tijd.be/beurzen/euronext-brussel') || die();

	# Build a HTML-tree
	my $tree = HTML::TreeBuilder->new();
	$tree->parse($res->content());

	# Find menu with exchange enumeration
	my $enumeration = $tree->look_down(
		'_tag' => 'ul',
		sub {
			defined $_[0]->attr('class') && $_[0]->attr('class') =~ q{tabnav};
		}
	);
	die("Could not exchange enumeration") unless $enumeration;
	
	# Extract exchanges
	my %exchanges;
	$enumeration->look_down(
		'_tag'	=> 'li',
		sub {
			my $item = shift;
			my $name = $item->as_text;
			my $id;
			foreach my $child ($item->content_list) {
				if ($child->tag eq "a") {
					my $url = $child->attr('href');
					my @paths = split(/\//, $url);
					$id = $paths[-2];
				}
			}
			if (defined $id) {
				$exchanges{$id} = $name;
			}
			return 0;
		}
	);
	
	$tree->delete;
	return %exchanges;
}

has 'indexes' => (
	is		=> 'ro',
	isa		=> 'HashRef[HashRef[Str]]',
	lazy		=> 1,
	builder		=> '_build_indexes'
);

sub _build_indexes {
	my ($self) = @_;
	
	my %indexes;
	foreach my $exchange (values %{$self->exchanges}) {
		# Fetch HTML
		my $res = $self->browser->get("http:\/\/www.tijd.be\/beurzen\/$exchange") || die();

		# Build a HTML tree
		my $tree = HTML::TreeBuilder->new();
		$tree->parse($res->content());
		
		# Check if realtime support
		my $realtime = $tree->look_down(
			'_tag'	=> 'div',
			sub {
				defined $_[0]->attr('id') && $_[0]->attr('id') =~ q{realtime_switch};
			}
		);
		return () unless (defined $realtime);

		# Find submenu
		my $div = $tree->look_down(
			'_tag' => 'div',
			sub {
				defined $_[0]->attr('id') && $_[0]->attr('id') =~ q{subtabnav};
			}
		);
		die("Could not find submenu") unless $div;
		
		# Find index enumeration
		my $enumeration = $div->look_down('_tag' => 'ul');
		die("Could not find index enumeration") unless $enumeration;
		
		# Extract indexes
		my %indexes_exchange;
		$enumeration->look_down(
			'_tag'	=> 'li',
			sub {
				my $item = shift;
				my $name = $item->as_text;
				my $id;
				foreach my $child ($item->content_list) {
					if ($child->tag eq "a") {
						my $url = $child->attr('href');
						my @paths = split(/\//, $url);
						$id = $paths[-1];
					}
				}
				if (defined $id) {
					$indexes_exchange{$id} = $name;
				}
				return 0;
			}
		);
		
		$tree->delete;
		$indexes{$exchange} = \%indexes_exchange;
	}
	return \%indexes;
}

has 'securities' => (
	is		=> 'ro',
	isa		=> 'HashRef[HashRef[HashRef[Str]]]',
	lazy		=> 1,
	builder		=> '_build_securities'
);

sub _build_securities {
	my ($self) = @_;
	
	my %securities;
	foreach my $exchange (values %{$self->exchanges}) {
		my %securities_exchange;
		foreach my $index (values %{$self->indexes->{$exchange}}) {
			# Fetch HTML
			my $res = $self->browser->get("http:\/\/www.tijd.be\/beurzen\/$land\/$beurs") || die();

			# Build a HTML tree
			my $tree = HTML::TreeBuilder->new();
			$tree->parse($res->content());

			# Find main table
			my $table = $tree->look_down(
				'_tag' => 'table',
				sub {
					$_[0]->attr('class') =~ m{maintable};
				}
			);
			die("Could not find main table") unless $table;

			# Extract shares
			my %securities_index;
			$table->look_down(
				'_tag'	=> 'td',
				sub {
					my $cell = shift;
					return unless defined $cell->attr('class') && $cell->attr('class') =~ m{st_name};
					my ($name, $id);
					foreach my $child ($cell->content_list) {
						if ($child->tag eq "a") {
							$name = $child->as_text;
						} elsif ($child->tag eq "form") {
							$child->look_down(
								'_tag', 'input',
								sub {
									if ($_[0]->attr('name') eq "id") {
										$id = $_[0]->attr('value');
									}
									return 0;
								}
							);
						}
					}
					$securities_index{$id} = $name;
					return 0;
				}
			);
			
			$tree->delete;
			$securities_exchange{$index} = \%securities_index;
		}
		$securities{$exchange} = \%securities_exchange;
	}
	return \%securities;
}


################################################################################
# Methods
#


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