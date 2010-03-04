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
use Time::HiRes;

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


################################################################################
# Methods
#

sub getExchanges {
	my ($self) = @_;
	
	# Fetch HTML
	my $res = $self->browser->get('http://www.tijd.be/beurzen/euronext-brussel') || die();

	# Build a HTML-tree
	my $tree = HTML::TreeBuilder->new();
	$tree->parse($res->decoded_content);

	# Find menu with exchange enumeration
	my $enumeration = $tree->look_down(
		'_tag' => 'ul',
		sub {
			defined $_[0]->attr('class') && $_[0]->attr('class') =~ q{tabnav};
		}
	);
	die("Could not find enumeration") unless $enumeration;
	
	# Extract exchanges
	my @exchanges;
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
				push(@exchanges, new StockPlay::Exchange({
					id		=> $id,
					location	=> $name
				}));
			}
			return 0;
		}
	);
	
	$tree->delete;
	return @exchanges;
}

sub getIndexes {
	my ($self, $exchange) = @_;

	# Fetch HTML
	my $res = $self->browser->get('http://www.tijd.be/beurzen/' . $exchange->id) || die();

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
	my @indexes;
	$enumeration->look_down(
		'_tag'	=> 'li',
		sub {
			my $item = shift;
			my $name = $item->as_text;
			my $site_id;
			foreach my $child ($item->content_list) {
				if ($child->tag eq "a") {
					my $url = $child->attr('href');
					my @paths = split(/\//, $url);
					$site_id = $paths[-1];
				}
			}
			if (defined $site_id) {
				push(@indexes, new StockPlay::Index({
					id	=> $name,
					private	=> {
						site_id	=> $site_id
					}
				}));
			}
			return 0;
		}
	);
	
	$tree->delete;
	return @indexes;
}

sub getSecurities {
	my ($self, $exchange) = @_;
	
	my @securities;
	my @indexes = $self->getIndexes($exchange);
		foreach my $index (@indexes) {	
		# Fetch HTML
		my $res = $self->browser->get('http:/www.tijd.be/beurzen/' . $exchange->id . '/' . $index->id) || die();

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
		my @securities;
		$table->look_down(
			'_tag'	=> 'td',
			sub {
				my $cell = shift;
				return unless defined $cell->attr('class') && $cell->attr('class') =~ m{st_name};
				my ($name, $site_id);
				foreach my $child ($cell->content_list) {
					if ($child->tag eq "a") {
						$name = $child->as_text;
					} elsif ($child->tag eq "form") {
						$child->look_down(
							'_tag', 'input',
							sub {
								if ($_[0]->attr('name') eq "id") {
									$site_id = $_[0]->attr('value');
								}
								return 0;
							}
						);
					}
				}
				if (defined $site_id) {
					push(@securities, new StockPlay::Security({
						id		=> $name,
						exchange	=> $exchange->id,
						index		=> [ $index->id ],
						private		=> {
							site_id	=> $site_id
						}
					}));
				}
				return 0;
			}
		);
		
		$tree->delete;
	}	
	return @securities;
}

sub getQuote {
	my ($self, @securities) = @_;
	
	# Query-parameters invullen
	my %parameters = (
		reqtype		=> "simple",
		quotes		=> join(';', map {$_->get('site_id')} @securities),
		datetime	=> int(Time::HiRes::time*1000)
	);

	# Query genereren
	my $query = 'http://1.ajax.tijd.be/rtq/?' . join('&', map {
		$_ . '=' . $parameters{$_}
	} keys %parameters);

	# JSON data ophalen en verwerken
	my $json = $self->browser->get($query)->content;
	my $koersen;
	if ($json =~ m{try \{ _parseRtq\((.*)\) \} catch\(err\) \{  \}}) {
		$koersen = from_json($1);
	} else {
		die("Kon JSON data niet extraheren");
	}
	
	use Data::Dumper;
	print Dumper($koersen);
	die();	
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