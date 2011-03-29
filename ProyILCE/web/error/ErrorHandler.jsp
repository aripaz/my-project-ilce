<%-- 
    Document   : ErrorHandler
    Created on : 15/03/2011, 10:01:16 AM
    Author     : vaio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="mx.ilce.handler.ExecutionHandler" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Error Page</title>
    </head>
<%
    ExecutionHandler execHand = (ExecutionHandler) request.getSession().getAttribute("execHand");
%>
    <body>
        <h1>Error detectado</h1>
        <table>
            <tr>
                <td>Title:</td>
                <td><%=execHand.getTitleExecution() %></td>
            </tr>
            <tr>
                <td>Text:</td>
                <td><%=execHand.getTextExecution() %></td>
            </tr>
        </table>
    </body>
</html>
