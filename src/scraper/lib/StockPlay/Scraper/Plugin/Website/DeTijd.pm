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
use Moose::Util::TypeConstraints;
use JSON;
use HTML::TreeBuilder;
use StockPlay::Scraper::Plugin::Website;
use Time::HiRes;
use DateTime;
use DateTime::Format::Strptime;
use POSIX;

# Roles
with 'StockPlay::Scraper::Plugin::Website';

# Write nicely
use strict;
use warnings;

# DateTime from String coercion
class_type 'DateTime';
class_type 'DateTime';
class_type 'DateTime';
my ($day, $month, $year) = (localtime())[3..5];
my $datetime_parser = DateTime::Format::Strptime->new(
	time_zone	=> strftime("%Z", localtime()),	# TODO: 28 maart, verandert dit naar CEST? Mss via module?
	pattern		=> '%H:%M:%S',
	#year		=> $year+1900,
	#month		=> $month+1,
	#day		=> $day
);


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
			defined $_[0]->attr('class') && $_[0]->attr('class') =~ m{tabnav};
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
					id	=> $id,
					name	=> $name
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
	$tree->parse($res->decoded_content);
	
	# Check if realtime support
	my $realtime = $tree->look_down(
		'_tag'	=> 'div',
		sub {
			defined $_[0]->attr('id') && $_[0]->attr('id') =~ m{realtime_switch};
		}
	);
	die("no support for realtime courses") unless (defined $realtime);

	# Find submenu
	my $div = $tree->look_down(
		'_tag' => 'div',
		sub {
			defined $_[0]->attr('id') && $_[0]->attr('id') =~ m{subtabnav};
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
			my $id;
			foreach my $child ($item->content_list) {
				if ($child->tag eq "a") {
					my $url = $child->attr('href');
					my @paths = split(/\//, $url);
					$id = $paths[-1];
				}
			}
			if (defined $id && $id !~ m{\?}) {
				push(@indexes, new StockPlay::Index({
					id	=> $id,
					name	=> $name
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
		my $res = $self->browser->get('http://www.tijd.be/beurzen/' . $exchange->id . '/' . $index->id) || die();

		# Build a HTML tree
		my $tree = HTML::TreeBuilder->new();
		$tree->parse($res->decoded_content);

		# Find main table
		my $table = $tree->look_down(
			'_tag' => 'table',
			sub {
				defined $_[0]->attr('class') && $_[0]->attr('class') =~ m{maintable};
			}
		);
		die("Could not find main table") unless $table;
		
		# Extract shares
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
	print "Returning\n";
	return @securities;
}

sub getQuotes {
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

	# JSON data ophalen
	my $json = $self->browser->get($query)->content;
	my $koersen;
	if ($json =~ m{try \{ _parseRtq\((.*)\) \} catch\(err\) \{  \}}) {
		$koersen = from_json($1);
	} else {
		die("Kon JSON data niet extraheren");
	}
	
	# Data verwerken naar Quotes
	my @quotes;
	foreach my $site_id (keys %{$koersen->{stocks}}) {
		print "Processing $site_id\n";
		my %data = %{$koersen->{stocks}->{$site_id}};
		use Data::Dumper;
		print Dumper(\%data);
		
		my $security = (grep { $_->get('site_id') == $site_id } @securities)[0]
			or die("Could not connect data to security");
		push(@quotes, new StockPlay::Quote({
			time		=> $datetime_parser->parse_datetime($data{time}),
			security	=> $security->id,
			price		=> $data{last},
			bid		=> $data{bid},
			ask		=> $data{ask},
			low		=> $data{low},
			high		=> $data{high},
			open		=> $data{open},
			volume		=> $data{volume}
		}));
		
	}
	
	return @quotes;
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