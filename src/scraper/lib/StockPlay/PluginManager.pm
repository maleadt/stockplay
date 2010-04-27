################################################################################
# Configuration
#

# Package definition
package StockPlay::PluginManager;

=pod

=head1 NAME

StockPlay::PluginManager - StockPlay scraper plugin manager

=head1 DESCRIPTION

This is the plugin manager, which is used to discover, parse, check, and
finally instantiate plugins. It provides some additional functionality, like
fancy error reporting upon problems, robust plugin instantiation, or parsing
of the code files to load a set of informative tags.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use File::Find;
use Carp;

# Roles
with 'StockPlay::Logger';
with 'StockPlay::Configurable';

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<plugins>

Read-only attribute, for internal use only. It contains all the registered
loggers in a key/value hash. The key, a string, represents a regular expression
which ought to match any URI to be processed by the logger. The value is an
info-hash created by the C<parse> function, which imposes certain restrictions
(have a look at the documentation of C<parse>). The plugin hash its key is the
plugin's package, while the value is a reference to the info hash.

=cut

has 'plugins' => (
	is		=> 'ro',
	isa		=> "HashRef",
	default		=> sub { {} }
);

=pod

=head2 C<$pluginmanager->load_group($base)>

This method loads plugins from a given base package, by attempting to find
a suitable directory layout in the C<@INC> variable. This means that the base
package must exist as a specific folder layout, and not only virtually as
package definitions!

=cut

sub load_group {
	my ($self, $base) = @_;
	
	# Discover all plugins
	my %plugins = discover($base)
		or die("error discovering plugins: $!");
	
	# Process all plugins
	for my $package (sort keys %plugins) {
		my $file = $plugins{$package};
		
		# Extract info hash
		my %infohash;
		eval {
			%infohash = parse($file);
			$infohash{package} = $package;
		};
		if ($@) {
			chomp $@;
			$self->logger->error("error loading plugin $package: $@");
			next;
		}
		
		# Check if we got a concrete plugin
		if (defined $infohash{type} && $infohash{type} eq "abstract") {
			next;
		}
		
		# Load the plugin
		my $status = do $file;
		if (!$status) {
			if ($@) {
				chomp $@;
				$self->logger->error("error loading plugin $package ($@)");
			}
			elsif ($!) {
				chomp $!;
				$self->logger->error("error loading plugin $package ($!)");
			}
			else {
				$self->logger->error("error loading plugin $package (unknown failure)");
			}
			next;
		}
		die("cannot reload identically named plugin")
			if (defined $self->plugins->{$package});
		$self->plugins->{$package} = \%infohash;
	}
	
	return scalar (keys %plugins);
}

################################################################################
# Methods
#

=pod

=head1 METHODS

=head2 C<$pluginmanager->get_plugin($base, $name, @params)>

This method serves as main method to instantiate a plugin. It searches all
plugins, selects those which package matches the given base, and returns a
single plugin which matches the given name.

=cut

sub get_plugin {
	my ($self, $base, $name, @params) = @_;
	
	my @infohashes = grep {
		$_->{name} eq $name
	} $self->get_group($base);
	
	die("given parameters matched multiple plugins") if (scalar @infohashes > 1);
	die("given parameters didn't match any plugin") unless (scalar @infohashes);
	
	return $self->instantiate($infohashes[0], @params);
}

=pod

=head2 C<$pluginmanager->get_group($base)>

This method selects a group of plugins, contrary to the C<get_plugin> method
which only selects a single plugin.
Logically, this method only requires the base package which selects the correct
plugins.

=cut

sub get_group {
	my ($self, $base, @params) = @_;
	
	my @infohashes =
		map { $self->plugins->{$_} }
		grep { $_ =~ m{^$base} } keys %{$self->plugins};
	
	return @infohashes;	
}

=pod

=head2 C<$pluginmanager->instantiate(\%infohash, @params)

This method instantiates a plugin based on its info hash.

=cut

sub instantiate {
	my ($self, $infohash, @params) = @_;
	my $package = $infohash->{package};
	$infohash->{time} = time;
	return new $package (infohash => $infohash, @params);
}

################################################################################
# Auxiliary
#

=pod

=head1 AUXILIARY

=head2 C<discover>

The static C<discover> method needs a base package-path as parameter, and scans
that base for plugins. To do that, it scans @INC to find the folder containing
the given base package layout, and subsequently scans that folder for
Perl-modules. All modules found (possible plugins) are returned in an hash
linking each file to its package name.

Returns a plugin hash (package => file) upon success, and dies upon failure.
=cut

sub discover {
	my ($base) = @_;
	
	# Find the appropriate root folder
	my $subfolders = $base;
	$subfolders =~ s{::}{/}g;
	my $root;
	foreach my $directory (@INC) {
		my $pluginpath = "$directory/$subfolders";
		if (-d $pluginpath) {
			$root = $pluginpath;
			last;
		}
	}
	die("no inclusion directory matched plugin structure") unless defined $root;
	
	# Scan for Perl-modules
	my %plugins;
	find( sub {
		my $file = $File::Find::name;
		if ($file =~ m{$root/(.*)\.pm$}) {
			my $package = "$base/" . $1;
			$package =~ s{\/+}{::}g;
			$plugins{$package} = $file;
		}
	}, $root);
	
	return %plugins;
}

=pod

=head2 C<parse($file, $inforef)>

Parse a given plugin, and save all keys in a hash. The info-hash is constructed
by reading the plugin file, and extracting all key/values formatted as:
  ## KEY = VALUE

Each plugin should contain certain keys in its info hash, namely:
=over
=item C<name>: the human-readable name of the plugin
=item C<source>: where the plugin gets its data from
=item C<description>: a short description
=back

Returns an info-hash upon success and dies upon failure.

=cut

sub parse {
	my ($file) = @_;
	my %info = (file => $file);
	
	# Open and read the file
	open(my $read, '<', $file) or die("could not open potential plugin '$file' for parsing ($!)");
	while (<$read>) {
		if (m{^##\s*(.+?)\s*(=+)\s*(.*?)$}) {
			my ($key, $value) = (lc($1), $3);
			$info{$key} = $value;
			# TODO? push(@{$info{$key}}, $value);
		}
	}
	close($read);
	
	# Check for missing keys
	check_info(\%info, qw{name description});
	# TODO: removed "source" tag, make it obligatory when instructed
	#       by an abstract superclass-plugin (eg Website forces "source")
	return %info;
}

=pod

=head2 C<check_info($hashref, @keys)>

Checks a given hash for required keys. Its functionality is the same as
C<map { defined $hash{$_} || die() } qw/foo bar/ }>, but makes it more
readable and provides an easy way to log all missing keys instead of only one.

Returns true upon success and dies upon failure.

=cut

sub check_info {
	my ($hashref, @keys) = @_;
	my %hash = %$hashref;
	
	my @missing = grep {
		not defined $hash{$_};
	} @keys;
	
	die("missing plugin keys " . join(", ", @missing)) if (@missing);
	return 1;
}

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
