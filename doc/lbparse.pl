#!/usr/bin/perl -n
use List::Util qw/sum/;
($name,$min)=($1,0) if /section\{(.+)\}/;
if (/lbentry(?:\{([^\}]+)\}){4}/) {
	split(/:/,$1);
	$_[0]*=60;
	$min+=sum(@_);
}
print "$name: ".join(":",map{$_<10?"0".$_:$_}(int($min/60),$min%60))."\n" if /lbstop/;
