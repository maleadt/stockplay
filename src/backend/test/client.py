#!/usr/bin/python

# Import XML-RPC library
import xmlrpclib

# Login to the server
print "* Connecting to backend"
Backend = None
try:
	server_url = 'http://user:password@localhost:8080'
	Backend = xmlrpclib.Server(server_url)
except:
	print "! Connection failed"
print "  Connected"


# Check the backend status
print "* Checking backend status"
try:
	status = Backend.System.Backend.Status()
	print "  Status is: %s" % "running" if status==1 else "down"
except xmlrpclib.ProtocolError as e:
	print "! HTTP error %d: %s" % (e.errcode, e.errmsg)
except xmlrpclib.Fault as e:
	print "! XML-RPC error %d: %s" % (e.faultCode, e.faultString)
except:
	print "! Unrecognized error"
	raise

