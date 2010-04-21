#!perl -T

use Test::More tests => 1;

BEGIN {
    use_ok( 'StockPlay' ) || print "Bail out!
";
}

diag( "Testing StockPlay $StockPlay::VERSION, Perl $], $^X" );
