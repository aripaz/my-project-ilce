<%-- 
    Document   : LoginHandler
    Created on : 16/03/2011, 02:10:38 PM
    Author     : vaio
--%>

<%@page import="mx.ilce.handler.LoginHandler"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
<%
    LoginHandler execHand = (LoginHandler) request.getSession().getAttribute("loginHand");
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
