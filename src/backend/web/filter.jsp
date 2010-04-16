<%@ page import="java.io.*, com.kapti.filter.*, com.kapti.filter.parsing.*" %>
<%@ page import="java.util.Queue" %>
<%
    String iFilter = request.getParameter("filter");
    String iAction = request.getParameter("submit");

    if (iFilter == null) {
        response.setContentType("text/html");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>StockPlay -- Filter test</title>
    </head>
    <body>
        <h1>Filter debug page</h1>
        <form action="filter.jsp" method="post">
            <table>
                <tr>
                    <td>De filter:</td>
                    <td><input type="text" name="filter" value="" size="30" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="submit" value="Tokenize" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="submit" value="Postfix" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="submit" value="Parse" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="submit" value="Compile" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>

<%
    } else if (iAction.equalsIgnoreCase("parse")) {
        // Parse filter
        com.kapti.filter.Filter tFilter = Parser.getInstance().parse(iFilter);
        tFilter.debug("/tmp/AST");

        // Display AST
        FileInputStream fis = new FileInputStream("/tmp/AST.png");
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] bytes = new byte[bis.available()];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        bis.read(bytes);
        os.write(bytes);
    } else if (iAction.equalsIgnoreCase("compile")) {
        // Parse filter
        com.kapti.filter.Filter tFilter = Parser.getInstance().parse(iFilter);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>StockPlay -- Filter test</title>
    </head>
    <body>
        <h1>Filter debug page</h1>
        <pre><%=tFilter.compile()%></pre>
    </body>
</html>
<%
    } else if (iAction.equalsIgnoreCase("tokenize")) {
        // Tokenize filter
        java.util.List<Token> tTokens = Parser.getInstance().tokenize(iFilter);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>StockPlay -- Filter test</title>
    </head>
    <body>
        <h1>Filter debug page</h1>
        <pre><%
            for (Token tToken : tTokens)
                out.println(tToken);
        %></pre>
    </body>
</html>
<%
    } else if (iAction.equalsIgnoreCase("postfix")) {
        // Tokenize filter
        java.util.List<Token> tTokens = Parser.getInstance().tokenize(iFilter);
        java.util.Queue<Token> tTokensPostfix = Parser.getInstance().infix_to_postfix(tTokens);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>StockPlay -- Filter test</title>
    </head>
    <body>
        <h1>Filter debug page</h1>
        <pre><%
            for (Token tToken : tTokensPostfix)
                out.println(tToken);
        %></pre>
    </body>
</html>
<%
    }
%>