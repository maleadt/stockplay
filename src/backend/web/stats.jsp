<%-- 
    Document   : index
    Created on : 6-apr-2010, 14:08:43
    Author     : tim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%!
    public String seconds2readable(long iSeconds) {
        StringBuffer tOutput = new StringBuffer();
        if (iSeconds >= 3600*24) {
            int tDays = (int)(iSeconds / (3600*24));
            iSeconds -= tDays * 3600*24;
            tOutput.append(tDays + " days");
        }
        if (iSeconds >= 3600) {
            int tHours = (int)(iSeconds / (3600));
            iSeconds -= tHours * 3600;
            if (tOutput.length() > 0)
                tOutput.append(", ");
            tOutput.append(tHours + " hours");
        }
        if (iSeconds >= 60) {
            int tMinutes = (int)(iSeconds / (60));
            iSeconds -= tMinutes * 60;
            if (tOutput.length() > 0)
                tOutput.append(", ");
            tOutput.append(tMinutes + " minutes");
        }
        if (iSeconds > 0) {
            if (tOutput.length() > 0)
                tOutput.append(" and ");
            tOutput.append((int)iSeconds + " seconds");
        }

        return tOutput.toString();
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>StockPlay -- Statistics</title>
    </head>
    <body>
        <h1>Statistics</h1>

        <%
            // Define XML-RPC client object
            org.apache.xmlrpc.client.XmlRpcClientConfigImpl tConfig = new org.apache.xmlrpc.client.XmlRpcClientConfigImpl();
            tConfig.setServerURL(new java.net.URL("http://localhost:6800/backend/public"));
            org.apache.xmlrpc.client.XmlRpcClient tClient = new org.apache.xmlrpc.client.XmlRpcClient();
            tClient.setConfig(tConfig);
        %>
        
        <h2>Backend</h2>
        <% java.util.HashMap<String, Object> tStatsBackend = (java.util.HashMap<String, Object>)(tClient.execute("System.Backend.Stats", new Object[]{})); %>
        <p>
            <b>Requests served</b>: <%=tStatsBackend.get("req")%>.<br />
            <b>Uptime</b>: <%=seconds2readable(Long.parseLong((String)tStatsBackend.get("uptime")))%>.<br />
            <b>Users logged in</b>: <i>not implemented</i>.<br />
        </p>

        <h2>Database</h2>
        <% java.util.HashMap<String, Object> tStatsDatabase = (java.util.HashMap<String, Object>)(tClient.execute("System.Database.Stats", new Object[]{})); %>
        <p>
            <b>Transaction rate</b>: <%=tStatsDatabase.get("rate")%> transactions per minute.<br />
            <b>Uptime</b>: <%=seconds2readable(Long.parseLong((String)tStatsDatabase.get("uptime")))%>.<br />
        </p>

        <h2>Scraper</h2>
        <p>
            <i>Not implemented.</i>
        </p>
    </body>
</html>
