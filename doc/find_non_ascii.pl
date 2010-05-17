#!/usr/bin/perl
use strict;
use warnings;
my @files = glob("*.tex");
foreach my $file (@files) {
	print "* Processing '$file'\n";
	open(my $read, "<", $file);
	while (<$read>) {
		my $zin = $_;
		my @characters = split(//, $zin);
		foreach my $c (@characters) {
			if (ord($c) >= 128) {
				my $t = $zin;
				chomp $t;
				print "! Found non-ASCII character at sentence:\n";
				print "  $t"
			}
		}
	}
	close($read);
}
