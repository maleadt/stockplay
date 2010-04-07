<%@ page import="java.io.*, com.kapti.filter.*, com.kapti.filter.parsing.*" %>
<%
    String iFilter = request.getParameter("filter");

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
        <p>Deze pagina dient om filters te debuggen, door na het renderen de AST weer te geven.</p>
        <form action="filter.jsp" method="post">
            <table>
                <tr>
                    <td>De filter:</td>
                    <td><input type="text" name="filter" value="" size="30" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="submit" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>

<%
    } else {
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
    }
%>