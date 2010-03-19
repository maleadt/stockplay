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

sub _build_exchanges {
	my ($self) = @_;
	
	# Euronext Brussel
	my $brussel = new StockPlay::Exchange(
		id		=> "euronext-brussel",
		name		=> "Euronext Brussel",
		location	=> "Brussel",
		securities	=> [$self->getSecurities('http://www.tijd.be/beurzen/euronext-brussel/continumarkt')]
	);
	
	# Euronext Parijs
	my $parijs = new StockPlay::Exchange(
		id		=> "euronext-parijs",
		name		=> "Euronext Parijs",
		location	=> "Parijs",
		securities	=> [$self->getSecurities('http://www.tijd.be/beurzen/euronext-parijs/frencha'), $self->getSecurities('http://www.tijd.be/beurzen/euronext-parijs/frenchb'), $self->getSecurities('http://www.tijd.be/beurzen/euronext-parijs/frenchc')]
	);
	
	return [$brussel, $parijs];
}

sub getSecurities {
	my ($self, $url) = @_;
	my @securities;	
	
	# Process all pages
	my $page = 1;
	while (1) {
		# Fetch HTML
		print "DEBUG: processing page $page\n";
		my $res = $self->browser->get($url . "?p=$page") || die();

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
		
		# Extract securities
		$table->look_down(
			'_tag'	=> 'td',
			sub {
				my $cell = shift;
				return unless $cell->parent()->parent()->tag eq "tbody";
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
					my ($symbol, $isin);
					print "DEBUG: processing $name\n";
					my $res2 = $self->browser->get('http://www.tijd.be/beurzen/' . $site_id) || die();
					my $tree2 = HTML::TreeBuilder->new();
					$tree2->parse($res2->decoded_content);
					$tree2->look_down(
						'_tag'	=> 'dl',
						sub {
							my $list = shift;
							return unless(defined $list->attr('class') && $list->attr('class') eq 'stockdeflist');
							
							my @items;
							foreach my $child ($list->content_list) {
								if (ref($child) eq 'HTML::Element' && $child->tag eq 'dd') {
									push(@items, $child->as_text);
								}								
							}
							($isin, $symbol) = @items[1..2];
						}
					);
					$tree2->delete();
					return unless (defined $isin and defined $symbol);
					
					push(@securities, new StockPlay::Security({
						id		=> $symbol,
						isin		=> $isin,
						name		=> $name,
						private		=> {
							site_id	=> $site_id
						}
					}));
				}
				return 0;
			}
		);
		$tree->delete;
		
		# Check for more pages
		last unless ($res->decoded_content =~ 'Volgende');
		$page++;	
		
	}
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
		my %data = %{$koersen->{stocks}->{$site_id}};
		
		my $security = (grep { $_->get('site_id') == $site_id } @securities)[0]
			or die("Could not connect data to security");
		eval {
			push(@quotes, new StockPlay::Quote({
				time		=> $datetime_parser->parse_datetime($data{time}),
				security	=> $security->id,
				price		=> $data{last},
				bid		=> $data{bid},
				ask		=> $data{ask},
				low		=> $data{low},
				high		=> $data{high},
				open		=> $data{open},
				volume		=> $data{volume},
				delay		=> $koersen->{delay}
			}));
		};
		if ($@) {
			print "ERROR: could not create a quote for $site_id\n";
		}
		
	}
	
	return @quotes;
}


################################################################################
# Methods

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