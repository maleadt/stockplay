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

# Roles
with 'StockPlay::Scraper::Plugin::Website';

# Write nicely
use strict;
use warnings;

# Proper DateTime conversion
sub parse_datetime {
	my ($timezone, $string) = @_;

	# Get a localized datetime to fill in missing parameters
	my $datetime_reference = DateTime->now(time_zone => $timezone);

	# Create a new datetime object with all parameters filled in
	my @items = split(/:/, $string);
	die("invalid time specification") unless $#items == 2;
	my $datetime = new DateTime (
		time_zone	=> $timezone,
		year		=> $datetime_reference->year,
		month		=> $datetime_reference->month,
		day		=> $datetime_reference->day,
		hour		=> $items[0],
		minute		=> $items[1],
		second		=> $items[2],
	);

	# Convert back to UTC and return
	$datetime->set_time_zone("UTC");
	return $datetime;
}


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
		symbol		=> "BSE",
		name		=> "Euronext Brussels",
		location	=> "Brussel",
		private => {
			time_zone	=> new DateTime::TimeZone(name => "Europe/Brussels")
		}
	);
	$self->addSecurities($brussel, 'http://www.tijd.be/beurzen/euronext-brussel/continumarkt');
	my $bel20 = new StockPlay::Index(
		name		=> "bel20",
		symbol		=> "BEL20",
		isin		=> "BE0389555039",
		private => {
			site_id	=> 190015497
		}
	);
	$self->addSecuritiesIndex($brussel, $bel20, 'http://www.tijd.be/beurzen/euronext-brussel/bel20');
	push(@{$brussel->indexes}, $bel20);
	
	# Euronext Parijs
	my $parijs = new StockPlay::Exchange(
		symbol		=> "PA",
		name		=> "Euronext Paris",
		location	=> "Parijs",
		private => {
			time_zone	=> new DateTime::TimeZone(name => "Europe/Paris")
		}
	);
	$self->addSecurities($parijs, 'http://www.tijd.be/beurzen/euronext-parijs/frencha');
	$self->addSecurities($parijs, 'http://www.tijd.be/beurzen/euronext-parijs/frenchb');
	$self->addSecurities($parijs, 'http://www.tijd.be/beurzen/euronext-parijs/frenchc');
	my $cac40 = new StockPlay::Index(
		name		=> "cac40",
		symbol		=> "PX1",
		isin		=> "FR0003500008",
		private => {
			site_id	=> 360015511
		}
	);
	$self->addSecuritiesIndex($parijs, $cac40, 'http://www.tijd.be/beurzen/euronext-parijs/cac40');
	push(@{$parijs->indexes}, $cac40);
	
	return [$brussel, $parijs];
}

sub addSecuritiesIndex {
	my ($self, $exchange, $index, $url) = @_;
	my @securities;
	
	# Process all pages
	my $page = 1;
	while (1) {
		# Fetch HTML
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
		die("could not find main table") unless $table;
		
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
					$self->logger->debug("processing $name");
					foreach my $security (@{$exchange->securities}) {
						if ($security->get('site_id') eq $site_id) {
							push(@securities, $security);
							return 0;
						}
					}
					
					# TODO: maybe scan for others?
					$self->logger->warn("could not find requested security on exchange, you might want to check other exchanges");
				}
				return 0;
			}
		);
		$tree->delete;
		
		# Check for more pages
		last unless ($res->decoded_content =~ 'Volgende');
		$page++;
	}
	
	push(@{$index->securities}, @securities);
	
}

sub addSecurities {
	my ($self, $exchange, $url) = @_;
	my @securities;	
	
	# Process all pages
	my $page = 1;
	while (1) {
		# Fetch HTML
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
						$name =~ s/[^[:ascii:]]//g;
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
					$self->logger->debug("processing $name");
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
														
							# Check if the security exists on the exchange
							if ($items[0] ne $exchange->name) {
								$self->logger->error("$name mentioned on " . $exchange->name . ", but exchange of origin is " . $items[0]);
								return 0;
							}							
							
							($isin, $symbol) = @items[1..2];
						}
					);
					$tree2->delete();
					
					return unless (defined $isin and defined $symbol);
					push(@securities, new StockPlay::Security({
						symbol		=> $symbol,
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
	
	push(@{$exchange->securities}, @securities);
}

sub getLatestQuotes {
	my ($self, $exchange, @securities) = @_;
	
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
			my $datetime = parse_datetime($exchange->get('time_zone'), $data{time});
			die("could not parse time") unless $datetime;
			push(@quotes, new StockPlay::Quote({
				time		=> $datetime,
				security	=> $security->isin,
				price		=> $data{last},
				bid		=> $data{bid},
				ask		=> $data{ask},
				low		=> $data{low},
				high		=> $data{high},
				open		=> $data{open},
				volume		=> $data{volume},
				delay		=> $koersen->{delay},
				fetchtime	=> time
			}));
			$security->errors(0);
		};
		if ($@) {
			chomp $@;
			$self->logger->warn("could not create a quote for " . $security->name . " ($@)");
			$security->errors($security->errors + 1);
			$security->wait($security->errors);
		}		
	}
	
	return @quotes;
}

sub isOpen {
	my ($self, $exchange, $datetime) = @_;
	
	if ($exchange->symbol eq "BSE") {
		$datetime->set_time_zone($exchange->get("time_zone"));
		
		if ($datetime->day_of_week > 5) {
			return 0;
		}
		
		if ($datetime->hour < 9 || $datetime->hour > 18) {
			return 0;
		}
		
		return 1;
	}
	
	elsif ($exchange->symbol eq "PA") {
		$datetime->set_time_zone($exchange->get("time_zone"));
		
		if ($datetime->day_of_week > 5) {
			return 0;
		}
		
		if ($datetime->hour < 9 || $datetime->hour > 18) {
			return 0;
		}
		
		return 1;		
	}
	
	else {
		die("unknown exchange");
	}
	
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
