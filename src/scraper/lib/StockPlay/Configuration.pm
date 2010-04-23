################################################################################
# Configuration
#

# Package definition
package StockPlay::Configuration;

=pod

=head1 NAME

StockPlay::Configuration - StockPlay serverside configuration handler

=head1 DESCRIPTION

The C<StockPlay::Configuration> package contains functionality to
configure static application aspects. It reads structured key/value data
from a certain (local) file, and presents those values in a hash-like way
to be used by the application its subsystems.

=head1 SYNPOSIS

=cut

# Packages
use Moose;
use StockPlay::Configuration::Entry;

# Write nicely
use strict;
use warnings;


################################################################################
# Attributes
#

=pod

=head1 ATTRIBUTES

=head2 C<parent>

Internally-used attribute, pointing to the parent C<Config> object when this
object only represents a subsection of the configuration tree. This is used
internally to preserve tree-consistency when adding or altering data.

=cut

has 'parent' => (
	isa		=> 'StockPlay::Configuration',
	predicate	=> 'has_parent'
);

=pod

=head2 C<section>

Internally-used attribute, defining the subsection this C<Config> object is
responsible for. When set, the C<parent> attribute will be set as well.

=cut

has 'section' => (
	is	=> 'ro',
	isa	=> 'Str'
);

=pod

=head2 C<items>

The internally-used hashmap containing references to all configuration entries.
This should not be used directly, see the C<get> method instead.

=cut

has 'items' => (
	is	=> 'ro',
	isa	=> 'HashRef[StockPlay::Configuration::Entry]',
	default	=> sub { {} }
);

################################################################################
# Methods
#

=pod

=head1 METHODS

=head2 C<init>

Internally-used method, used to construct the initial key. This includes
checking whether the key does not exist yet, and adding the key/value pair
at the correct spot (parent, or self).

=cut

sub init {
	my ($self, $key) = @_;
	
	if ($self->defines($key)) {
		warn("attempt to overwrite existing key through initialisation");
		return 0;
	}
	
	# Add at right spot (self or parent)
	if ($self->has_parent) {
		$self->{parent}->init($self->section() . ":" . $key);
		$self->items->{$key} = $self->{parent}->{items}->{$self->section() . ":" . $key};
	} else {
		$self->items->{key} = {};
		$self->items->{$key}->{mutable} = 1;
		$self->items->{$key}->{default} = undef;
		$self->items->{$key}->{value} = undef;
	}
	
	return 1;
}

=pod

=head2 $configuration->defines($key)

Check whether a specific key has been entered in the configuration (albeit by
default or customized value). This does not look at the value itself,
which can e.g. be 'undef'. This method should only be used internally. If you
want to check if the user specified a value, you should rather specify a
default value of C<undef> and check that using C<defined($config->get('foo'))>.
This will give you a warning every time you use an undefined key, which provides
you with another layer of protection. Also, it provides you with code implicitely
listing all possible configuration keys, easing development if documentation is
absent or incomplete.

=cut

sub defines {
	my ($self, $key) = @_;
	die("no key specified") unless $key;
	if (exists $self->items->{$key}) {
		return 1;
	} else {
		return 0;
	}
}

=pod

=head2 $configuration->set_default($key, $value)

Adds a new item into the configuration, with $value as default value. This happens
always, even when the key has been marked as protected. Any previously entered
values do not get overwritten, which makes it possible to enter or re-enter a
default value after actual values has been entered.

=cut

sub set_default {
	my ($self, $key, $value) = @_;
	
	# Check if key already exists
	unless ($self->defines($key)) {
		$self->init($key);
	}
	
	# Update the default value
	$self->items->{$key}->{default} = $value;
}

=pod

=head2 $configuration->get_default($key)

Returns the default value, or undef if not specified.

=cut

sub get_default {
	my ($self, $key) = @_;
	
	if ($self->defines($key)) {
		return $self->items->{$key}->{default};
	}
	return;
}

=pod

=head2 $configuration->get_value($key)

Returns the value of the key, or undef if not specified. Does not return the default
value.

=cut

sub get_value {
	my ($self, $key) = @_;
	
	if ($self->defines($key)) {
		return $self->items->{$key}->{value};
	}
	return;
}

=pod

=head2 $configuration->get($key)

Return the value for a specific key. If not value specified, returns the default value.
Returns undef if no default value specified either.

NOTE: recently behaviour of this function has altered, there it now supports and correctly
returns values which Perl evaluates to FALSE. When checking if a configuration value
is present, please use defined() now instead of regular boolean testing.

=cut

sub get {
	my ($self, $key) = @_;
	
	# Check if it contains the key (not present returns false)
	if (! $self->defines($key)) {
		warn("access to undefined key '$key'");
		return;
	}
	
	# Return value or default
	if (defined(my $value = $self->get_value($key))) {
		return $value;
	} elsif (defined(my $default = $self->get_default($key))) {
		return $default;
	}
	return;
}

=pod

=head2 $configuration->set($key, $value)

Set a key to a given value. This is separated from the default value, which can still
be accessed with the default() call.

=cut

sub set {
	my ($self, $key, $value) = @_;
	
	# Check if contains
	if (!$self->defines($key)) {
		$self->init($key);
	}
	
	# Check if mutable
	if (! $self->items->{$key}->{mutable}) {
		warn("attempt to modify protected key '$key'");
		return 0;
	}
	
	# Modify value
	$self->items->{$key}->{value} = $value;
	return 1;
}

=pod

=head2 $configuration->protect($key)

Protect the values of a key from further modification. The default value always remains
mutable.

=cut

sub protect {
	my ($self, $key) = @_;
	if ($self->defines($key)) {
		$self->items->{$key}->{mutable} = 0;
		return 1;
	}
	warn("attempt to protect undefined key '$key'");
	return 0;
}

=pod

=head2 $configuration->file_read($file)

Read configuration data from a given file. The file is interpreted as a set of key/value pairs.
Pairs separated by a single '=' indicate mutable entries, while a double '==' means the entry
shall be protected and thus immutable.

=cut

sub file_read {
	my ($self, $filename) = @_;
	my $prepend = "";	# Used for section seperation
	open(my $file, '<', $filename) or die("could not open configuration file '$filename'");
	while (<$file>) {
		chomp;
		
		# Skip comments, and leading & trailing spaces
		s/#.*//;
		s/^\s+//;
		s/\s+$//;
		next unless length;
		
		# Get the key/value pair
		if (my($key, $separator, $value) = /^(.+?)\s*(=+)\s*(.*?)$/) {

			# Replace '~' with the home directory
			$value =~ s#^~/#$ENV{'HOME'}/#;
			
			# Substitute negatively connoted values
			$value =~ s/^(|off|none|disabled|false|no)$/0/i;
			
			if ($key =~ m/(:)/) {
				warn("ignored configuration entry due to protected string in key ('$1')");
			} else {
				$self->set($prepend.$key, $value);
				$self->protect($prepend.$key) if (length($separator) >= 2);
			}
		}
		
		# Get section identifier
		elsif (/^\[(.+)\]$/) {
			my $section = lc($1);
			if ($section =~ m/^\w+$/) {
				$prepend = "$section:";
			} else {
				warn("ignored non-alphanumeric subsection entry");
			}
		}
		
		# Invalid entry
		else {
			warn("ignored invalid configuration entry '$_'");
		}
	}
	close($file);
}

=pod

=head2 $configuration->get_section($section)

Returns a new Configuration object, only containing key/value pairs listed in the given section.
This can be used to seperate the configuration of several parts. Keys are
internally identified by the key and a section preposition, which makes it possible to use
identical keys in different sections. The internally used seperation of preposition and key
(a ":") is protected in order to avoid a security leak.
Values in the section object are references to the main object, adjusting them will this
adjust the main object.
NOTE: section entries are case-insensitive.
IMPORTANT NOTE: do _not_ use the ":" token to get/set values within a section, _always_ use
the section("foo")->get/set construction.

=cut

sub get_section {
	my ($self, $section) = @_;
	$section = lc($section);
	
	# Prohibit higher-level hierarchies
	return error("can only split section from top-level configuration object") if ($self->has_parent);
	
	# Extract subsection
	my $configsection = new StockPlay::Configuration(
		parent	=> $self,
		section	=> $section
	);
	foreach my $key (keys %{$self->items}) {
		if ($key =~ m/^$section:(.+)$/) {
			$configsection->{items}->{substr($key, length($section)+1)} = $self->items->{$key};
		}
	}
	
	return $configsection;
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
