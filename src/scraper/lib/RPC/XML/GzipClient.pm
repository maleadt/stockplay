###############################################################################
#
# This file copyright (c) 2001-2010 Randy J. Ray, all rights reserved
#
# Copying and distribution are permitted under the terms of the Artistic
# License 2.0 (http://www.opensource.org/licenses/artistic-license-2.0.php) or
# the GNU LGPL (http://www.opensource.org/licenses/lgpl-2.1.php).
#
###############################################################################
#
#   Description:    This class implements an RPC::XML client, using LWP to
#                   manage the underlying communication protocols. It relies
#                   on the RPC::XML transaction core for data management.
#
#   Functions:      new
#                   send_request
#                   simple_request
#                   uri
#                   useragent
#                   request
#
#   Libraries:      LWP::UserAgent
#                   HTTP::Request
#                   URI
#                   RPC::XML
#
#   Global Consts:  $VERSION
#
###############################################################################

package RPC::XML::GzipClient;

use 5.006001;
use strict;
use warnings;
use vars qw($VERSION $COMPRESSION_AVAILABLE);
use subs qw(new simple_request send_request uri useragent request
            fault_handler error_handler combined_handler timeout);

use LWP::UserAgent;
use HTTP::Request;
use URI;
use Scalar::Util 'blessed';
use File::Temp;
use IO::Handle;

use RPC::XML;
require RPC::XML::ParserFactory;

BEGIN
{
    # Check for compression support
    if (eval { require Compress::Zlib; 1; })
    {
        $COMPRESSION_AVAILABLE = ($@) ? q{} : 'gzip';
    }
    else
    {
        $COMPRESSION_AVAILABLE = q{};
    }
}

$VERSION = '1.32';
$VERSION = eval $VERSION; ## no critic (ProhibitStringyEval)

###############################################################################
#
#   Sub Name:       new
#
#   Description:    Create a LWP::UA instance and add some extra material
#                   specific to our purposes.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $class    in      scalar    Class to bless into
#                   $location in      scalar    URI path for requests to go to
#                   %attrs    in      hash      Extra info
#
#   Globals:        $VERSION
#
#   Returns:        Success:    object reference
#                   Failure:    error string
#
###############################################################################
sub new
{
    my ($class, $location, %attrs) = @_;

    $class = ref($class) || $class;
    if (! $location)
    {
        return "${class}::new: Missing location argument";
    }

    my ($self, $UA, $REQ);

    # Start by getting the LWP::UA object
    $UA = LWP::UserAgent->new((exists $attrs{useragent}) ?
                              @{$attrs{useragent}} : ()) or
        return "${class}::new: Unable to get LWP::UserAgent object";
    $UA->agent(sprintf '%s/%s %s', $class, $VERSION, $UA->agent);
    $self->{__useragent} = $UA;
    delete $attrs{useragent};

    # Next get the request object for later use
    $REQ = HTTP::Request->new(POST => $location) or
        return "${class}::new: Unable to get HTTP::Request object";
    $self->{__request} = $REQ;
    $REQ->header(Content_Type => 'text/xml');
    $REQ->protocol('HTTP/1.0');

    # Note compression support
    $self->{__compress} = $COMPRESSION_AVAILABLE;
    # It looks wasteful to keep using the hash key, but it makes it easier
    # to change the string in just one place (BEGIN block, above) if I have to.
    # Also (for now) I prefer to manipulate the private keys directly, before
    # blessing $self, rather than using accessors. This is just for performance
    # and I might change my mind later.
    if ($self->{__compress})
    {
        $REQ->header(Accept_Encoding => $self->{__compress});
    }
    $self->{__compress_thresh} = $attrs{compress_thresh} || 4096;
    $self->{__compress_re} = qr/$self->{__compress}/;
    # They can change this value with a method
    $self->{__compress_requests} = 0;
    delete $attrs{compress_thresh};

    # Note and preserve any error or fault handlers. Check the combo-handler
    # first, as it is superceded by anything more specific.
    if (ref $attrs{combined_handler})
    {
        $self->{__error_cb} = $attrs{combined_handler};
        $self->{__fault_cb} = $attrs{combined_handler};
        delete $attrs{combined_handler};
    }
    if (ref $attrs{fault_handler})
    {
        $self->{__fault_cb} = $attrs{fault_handler};
        delete $attrs{fault_handler};
    }
    if (ref $attrs{error_handler})
    {
        $self->{__error_cb} = $attrs{error_handler};
        delete $attrs{error_handler};
    }

    # Get the RPC::XML::Parser instance from the ParserFactory
    $self->{__parser} =
        RPC::XML::ParserFactory->new($attrs{parser} ? @{$attrs{parser}} : ())
              or return "${class}::new: Unable to get RPC::XML::Parser object";
    delete $attrs{parser};

    # Now preserve any remaining attributes passed in
    for (keys %attrs)
    {
        $self->{$_} = $attrs{$_};
    }

    return bless $self, $class;
}

###############################################################################
#
#   Sub Name:       simple_request
#
#   Description:    Simplify the request process by both allowing for direct
#                   data on the incoming side, and for returning a native
#                   value rather than an object reference.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Class instance
#                   @args     in      list      Various args -- see comments
#
#   Globals:        $RPC::XML::ERROR
#
#   Returns:        Success:    value
#                   Failure:    undef, error in $RPC::XML::ERROR
#
###############################################################################
sub simple_request
{
    my ($self, @args) = @_;

    my ($return, $value);

    $RPC::XML::ERROR = q{};

    $return = $self->send_request(@args);
    if (! ref $return)
    {
        $RPC::XML::ERROR = ref($self) . "::simple_request: $return";
        return;
    }

    return $return->value;
}

###############################################################################
#
#   Sub Name:       send_request
#
#   Description:    Take a RPC::XML::request object, dispatch a request, and
#                   parse the response. The return value should be a
#                   RPC::XML::response object, or an error string.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Class instance
#                   $req      in      ref       RPC::XML::request object
#
#   Returns:        Success:    RPC::XML::response object instance
#                   Failure:    error string
#
###############################################################################
sub send_request ## no critic (ProhibitExcessComplexity)
{
    my ($self, $req, @args) = @_;

    my ($me, $message, $response, $reqclone, $content, $can_compress, $value,
        $do_compress, $req_fh, $tmpdir, $com_engine);

    $me = ref($self) . '::send_request';

    if (! (blessed $req and $req->isa('RPC::XML::request')))
    {
        # Assume that $req is the name of the routine to be called
        if (! ($req = RPC::XML::request->new($req, @args)))
        {
            return "$me: Error creating RPC::XML::request object: " .
                $RPC::XML::ERROR;
        }
    }

    # Start by setting up the request-clone for using in this instance
    $reqclone = $self->request->clone;
    $reqclone->header(Host => URI->new($reqclone->uri)->host);
    $can_compress = $self->compress; # Avoid making 4+ calls to the method
    if ($self->compress_requests and $can_compress and
        $req->length >= $self->compress_thresh)
    {
        # If this is a candidate for compression, set a flag and note it
        # in the Content-encoding header.
        $do_compress = 1;
        $reqclone->content_encoding($can_compress);
    }
    
	# Treat the content strictly in-memory
	$content = $req->as_string;
	RPC::XML::utf8_downgrade($content);
	if ($do_compress)
	{
	    $content = Compress::Zlib::memGzip($content);
	}
	$reqclone->content($content);
	# Because $content has been force-downgraded, length() should work
	$reqclone->content_length(length $content);

    # Content is now handled in-memory again
    my $compression;

    $response = $self->useragent->request($reqclone);
    if (! $response->is_success)
    {
        $message =  "$me: HTTP server error: " . $response->message;
        return ('CODE' eq ref $self->error_handler) ?
            $self->error_handler->($message) : $message;
    }

    # Whee. No errors from the server. Parse the data
    if (! eval { $value = $self->parser->parse($response->decoded_content); 1; })
    {
        if ($@)
        {
            # One of the die's was triggered
            return ('CODE' eq ref $self->error_handler) ?
                $self->error_handler->($@) : $@;
        }
    }

    # Check if there is a callback to be invoked in the case of
    # errors or faults
    if (! ref $value)
    {
        $message =  "$me: parse-level error: $value";
        return ('CODE' eq ref $self->error_handler) ?
            $self->error_handler->($message) : $message;
    }
    elsif ($value->is_fault)
    {
        return ('CODE' eq ref $self->fault_handler) ?
            $self->fault_handler->($value->value) : $value->value;
    }

    return $value->value;
}

###############################################################################
#
#   Sub Name:       timeout
#
#   Description:    Get or set the timeout() setting on the underlying
#                   LWP::UserAgent object.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Object of this class
#                   $time     in      scalar    New timeout value, if passed
#
#   Returns:        Return value from LWP::UserAgent->timeout()
#
###############################################################################
sub timeout ## no critic (RequireArgUnpacking)
{
    my $self = shift;

    return $self->useragent->timeout(@_);
}

###############################################################################
#
#   Sub Name:       uri
#
#   Description:    Get or set the URI portion of the request
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Object of this class
#                   $uri      in      scalar    New URI, if passed
#
#   Returns:        Current URI, undef if trying to set an invalid URI
#
###############################################################################
sub uri ## no critic (RequireArgUnpacking)
{
    my $self = shift;

    return $self->request->uri(@_);
}

###############################################################################
#
#   Sub Name:       credentials
#
#   Description:    Set basic-auth credentials on the underlying user-agent
#                   object
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Object of this class
#                   $realm    in      scalar    Realm to authenticate for
#                   $user     in      scalar    User name to authenticate
#                   $pass     in      scalar    Password for $user
#
#   Returns:        $self
#
###############################################################################
sub credentials
{
    my ($self, $realm, $user, $pass) = @_;

    my $uri = URI->new($self->uri);
    $self->useragent->credentials($uri->host_port, $realm, $user, $pass);

    return $self;
}

# Immutable accessor methods
BEGIN
{
    no strict 'refs'; ## no critic (ProhibitNoStrict)

    for my $method (qw(useragent request compress_re compress parser))
    {
        *{$method} = sub { shift->{"__$method"} }
    }
}

# Fetch/set the compression threshhold
sub compress_thresh
{
    my $self = shift;
    my $value = shift || 0;

    my $old = $self->{__compress_thresh};
    if ($value)
    {
        $self->{__compress_thresh} = $value;
    }

    return $old;
}

# This doesn't actually *get* the original value, it only sets the value
sub compress_requests
{
    my ($self, $value) = @_;

    if (! $value)
    {
        return $self->{__compress_requests};
    }

    return $self->{__compress_requests} = $value ? 1 : 0;
}

# These are get/set accessors for the fault-handler, error-handler and the
# combined fault/error handler.
sub fault_handler
{
    my ($self, $newval) = @_;

    my $val = $self->{__fault_cb};
    if ($newval and ref $newval)
    {
        $self->{__fault_cb} = $newval;
    }
    # Special: an explicit undef is used to clear the callback
    if (@_ == 2 and (! defined $newval))
    {
        $self->{__fault_cb} = undef;
    }

    return $val;
}

sub error_handler
{
    my ($self, $newval) = @_;

    my $val = $self->{__error_cb};
    if ($newval and ref $newval)
    {
        $self->{__error_cb} = $newval;
    }
    # Special: an explicit undef is used to clear the callback
    if (@_ == 2 and (! defined $newval))
    {
        $self->{__error_cb} = undef;
    }

    return $val;
}

sub combined_handler
{
    my ($self, $newval) = @_;

    return ($self->fault_handler($newval), $self->error_handler($newval));
}

1;

__END__

=pod

=head1 NAME

RPC::XML::Client - An XML-RPC client class

=head1 SYNOPSIS

    require RPC::XML;
    require RPC::XML::Client;

    $cli = RPC::XML::Client->new('http://www.localhost.net/RPCSERV');
    $resp = $cli->send_request('system.listMethods');

    print ref $resp ? join(', ', @{$resp->value}) : "Error: $resp";

=head1 DESCRIPTION

This is an XML-RPC client built upon the B<RPC::XML> data classes, and using
B<LWP::UserAgent> and B<HTTP::Request> for the communication layer. This
client supports the full XML-RPC specification.

=head1 SUBROUTINES/METHODS

The following methods are available:

=over 4

=item new (URI [, ARGS])

Creates a new client object that will route its requests to the URL provided.
The constructor creates a B<HTTP::Request> object and a B<LWP::UserAgent>
object, which are stored on the client object. When requests are made, these
objects are ready to go, with the headers set appropriately. The return value
of this method is a reference to the new object. The C<URI> argument may be a
string or an object from the B<URI> class from CPAN.

Any additional arguments are treated as key-value pairs. Most are attached to
the object itself without change. The following are recognized by C<new> and
treated specially:

=over 4

=item parser

If this parameter is passed, the value following it is expected to be an array
reference. The contents of that array are passed to the B<new> method of the
B<RPC::XML::ParserFactory>-generated object that the client object caches for
its use. See the B<RPC::XML::ParserFactory> manual page for a list of
recognized parameters to the constructor.

=item useragent

This is similar to the C<parser> argument above, and also expects an array
reference to follow it. The contents are passed to the constructor of the
B<LWP::UserAgent> class when creating that component of the client object.
See the manual page for B<LWP::UserAgent> for supported values.

=item error_handler

If passed, the value must be a code reference that will be invoked when a
request results in a transport-level error. The closure will receive a
single argument, the text of the error message from the failed communication
attempt. It is expected to return a single value (assuming it returns at all).

=item fault_handler

If passed, the value must be a code reference. This one is invoked when a
request results in a fault response from the server. The closure will receive
a single argument, a B<RPC::XML::fault> instance that can be used to retrieve
the code and text-string of the fault. It is expected to return a single
value (if it returns at all).

=item combined_handler

If this parameter is specified, it too must have a code reference as a value.
It is installed as the handler for both faults and errors. Should either of
the other parameters be passed in addition to this one, they will take
precedence over this (more-specific wins out over less). As a combined
handler, the closure will get a string (non-reference) in cases of errors, and
an instance of B<RPC::XML::fault> in cases of faults. This allows the
developer to install a simple default handler, while later providing a more
specific one by means of the methods listed below.

=back

See the section on the effects of callbacks on return values, below.

=item uri ([URI])

Returns the B<URI> that the invoking object is set to communicate with for
requests. If a string or C<URI> class object is passed as an argument, then
the URI is set to the new value. In either case, the pre-existing value is
returned.

=item useragent

Returns the B<LWP::UserAgent> object instance stored on the client object.
It is not possible to assign a new such object, though direct access to it
should allow for any header modifications or other needed operations.

=item request

Returns the B<HTTP::Request> object. As with the above, it is not allowed to
assign a new object, but access to this value should allow for any needed
operations.

=item simple_request (ARGS)

This is a somewhat friendlier wrapper around the next routine (C<send_request>)
that returns Perl-level data rather than an object reference. The arguments may
be the same as one would pass to the B<RPC::XML::request> constructor, or there
may be a single request object as an argument. The return value will be a
native Perl value. If the return value is C<undef>, an error has occurred and
C<simple_request> has placed the error message in the global variable
C<B<$RPC::XML::ERROR>>.

=item send_request (ARGS)

Sends a request to the server and attempts to parse the returned data. The
argument may be an object of the B<RPC::XML::request> class, or it may be the
arguments to the constructor for the request class. The return value will be
either an error string or a data-type object. If the error encountered was a
run-time error within the RPC request itself, then the call will return a
C<RPC::XML::fault> value rather than an error string.

If the return value from C<send_request> is not a reference, then it can only
mean an error on the client-side (a local problem with the arguments and/or
syntax, or a transport problem). All data-type classes now support a method
called C<is_fault> that may be easily used to determine if the "successful"
return value is actually a C<RPC::XML::fault> without the need to use
C<UNIVERSAL::ISA>.

=item error_handler ([CODEREF])

=item fault_handler ([CODEREF])

=item combined_handler ([CODEREF])

These accessor methods get (and possibly set, if CODEREF is passed) the
specified callback/handler. The return value is always the current handler,
even when setting a new one (allowing for later restoration, if desired).

=item credentials (REALM, USERNAME, PASSWORD)

This sets the username and password for a given authentication realm at the
location associated with the current request URL. Needed if the RPC location
is protected by Basic Authentication. Note that changing the target URL of the
client object to a different (protected) location would require calling this
with new credentials for the new realm (even if the value of C<$realm> is
identical at both locations).

=item timeout ([INTEGER])

Get or set the current time-out value on the underlying B<LWP::UserAgent>
object that this object uses for sending requests. This is just a proxy
through to the method of the same name in the B<LWP::UserAgent> class. The
return value is the current time-out value (prior to change, if a new value
is given).

=back

=head2 Support for Content Compression

The B<RPC::XML::Server> class supports compression of requests and responses
via the B<Compress::Zlib> module available from CPAN. Accordingly, this class
also supports compression. The methods used for communicating compression
support should be compatible with the server and client classes from the
B<XMLRPC::Lite> class that is a part of the B<SOAP::Lite> package (also
available from CPAN).

Compression support is enabled (or not) behind the scenes; if the Perl
installation has B<Compress::Zlib>, then B<RPC::XML::Client> can deal with
compressed responses. However, since outgoing messages are sent before a
client generally has the chance to see if a server supports compression, these
are not compressed by default.

=over 4

=item compress_requests(BOOL)

If a client is communicating with a server that is known to support compressed
messages, this method can be used to tell the client object to compress any
outgoing messages that are longer than the threshhold setting in bytes.

=item compress_thresh([MIN_LIMIT])

With no arguments, returns the current compression threshhold; messages
smaller than this number of bytes will not be compressed, regardless of the
above method setting. If a number is passed, this is set to the new
lower-limit. The default value is 4096 (4k).

=back

=head2 Callbacks and Return Values

If a callback is installed for errors or faults, it will be called before
either of C<send_request> or C<simple_request> return. If the callback calls
B<die> or otherwise interrupts execution, then there is no need to worry about
the effect on return values. Otherwise, the return value of the callback
becomes the return value of the original method (C<send_request> or
C<simple_request>). Thus, all callbacks are expected, if they return at all,
to return exactly one value. It is recommended that any callback return values
conform to the expected return values. That is, an error callback would
return a string, a fault callback would return the fault object.

=head1 DIAGNOSTICS

All methods return some type of reference on success, or an error string on
failure. Non-reference return values should always be interpreted as errors,
except in the case of C<simple_request>.

=head1 CAVEATS

This began as a reference implementation in which clarity of process and
readability of the code took precedence over general efficiency. It is now
being maintained as production code, but may still have parts that could be
written more efficiently.

=head1 BUGS

Please report any bugs or feature requests to
C<bug-rpc-xml at rt.cpan.org>, or through the web interface at
L<http://rt.cpan.org/NoAuth/ReportBug.html?Queue=RPC-XML>. I will be
notified, and then you'll automatically be notified of progress on
your bug as I make changes.

=head1 SUPPORT

=over 4

=item * RT: CPAN's request tracker

L<http://rt.cpan.org/NoAuth/Bugs.html?Dist=RPC-XML>

=item * AnnoCPAN: Annotated CPAN documentation

L<http://annocpan.org/dist/RPC-XML>

=item * CPAN Ratings

L<http://cpanratings.perl.org/d/RPC-XML>

=item * Search CPAN

L<http://search.cpan.org/dist/RPC-XML>

=item * Source code on GitHub

L<http://github.com/rjray/rpc-xml>

=back

=head1 LICENSE AND COPYRIGHT

This file and the code within are copyright (c) 2010 by Randy J. Ray.

Copying and distribution are permitted under the terms of the Artistic
License 2.0 (L<http://www.opensource.org/licenses/artistic-license-2.0.php>) or
the GNU LGPL 2.1 (L<http://www.opensource.org/licenses/lgpl-2.1.php>).

=head1 CREDITS

The B<XML-RPC> standard is Copyright (c) 1998-2001, UserLand Software, Inc.
See <http://www.xmlrpc.com> for more information about the B<XML-RPC>
specification.

=head1 SEE ALSO

L<RPC::XML>, L<RPC::XML::Server>

=head1 AUTHOR

Randy J. Ray C<< <rjray@blackperl.com> >>

=cut
