#!/usr/bin/env perl

use Module::Build;

my $build = Module::Build->new (
    module_name         => 'StockPlay::Scraper',
    dist_name           => 'StockPlay::Scraper',
    dist_version        => '0.1',
    dist_author         => 'The StockPlay team <http://code.google.com/p/stockplay/>',
    dist_abstract       => 'The StockPlay scraper',
    license             => 'gpl',
    requires            => {
        'perl'                  => 5,
        'Moose'		        => '0.26',
        'XML::RPC'		=> 0,
    },
    recommends => {
        'WWW::Mechanize'        => 1,
        'JSON'                  => 1,
        'HTML::TreeBuilder'     => 1,
        'DBI'                   => 1
    }
);
$build->create_build_script;
