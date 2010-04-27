use strict;
use warnings;
use Module::Build;

my $builder = Module::Build->new (
    module_name         => 'StockPlay',
    dist_name           => 'StockPlay',
    dist_version_from   => 'lib/StockPlay.pm',
    dist_author         => 'The StockPlay team <http://code.google.com/p/stockplay/>',
    dist_abstract       => 'The StockPlay framework',
    license             => 'gpl',
    requires            => {
        'perl'                          => 5,
        'Moose'		                => '0.26',
        'DateTime'                      => 0,
        'Date::Manip'                   => 0,
        'DateTime::Format::ISO8601'     => 0,
        'DateTime::Format::DateManip'   => 0,
        'Carp'                          => 0,

        # StockPlay::PluginManager
        'File::Find'                    => 0,
        'Storable'                      => 0,
        
        # StockPlay::Factory
        'RPC::XML'		        => 0,
        
        # StockPlay::Logger
        'Log::Log4perl'                 => 0,
        'Log::Dispatch'                 => 0,
    },
    recommends => {
        # StockPlay::Scraper::Source::Website
        'WWW::Mechanize'        => 0,
        'Time::HiRes'           => 0,
        
        # StockPlay::Scraper::Source::Website::DeTijd
        'JSON'                  => 0,
        'HTML::TreeBuilder'     => 0,
        
        # StockPlay::AI::Forecaster
        'AI::FANN'              => 0,
        'List::Util'            => 0,
        
        # StockPlay::Historic::Euronext
        'LWP::UserAgent'         => 0
    },
    add_to_cleanup      => [ 'StockPlay-*' ]
);
$builder->create_build_script;
