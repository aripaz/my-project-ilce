<%-- 
    Document   : setterLogin
    Created on : 25/03/2011, 11:05:29 AM
    Author     : USUARIO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page USER</title>
    </head>
    <body>
        <jsp:useBean id="user" class="mx.ilce.bean.User" scope="session"/>
                USER:<jsp:getProperty name="user" property="login"/>
                <br>
                PASS:<jsp:getProperty name="user" property="password"/>
                <br>
                NAME:<jsp:getProperty name="user" property="nombre"/>
                <br>
                URL:<jsp:getProperty name="user" property="urlAvatar"/>
    </body>
</html>
