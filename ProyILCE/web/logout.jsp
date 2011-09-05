<%-- 
    Document   : logout
    Created on : 05-sep-2011, 11:02:10
    Author     : ccatrilef
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.7.custom.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <title>Logout</title>
    </head>
    <body>
        <table width="28%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <img src="img/logo_plataforma_ilce.jpg" width="197" height="154" />
                    </div></td>
            </tr>
            <tr>
                <td><div align="center">Usuario Desconectado</div></td>
            </tr>
            <tr>
                <td><div align="center"><a href="<%=request.getContextPath()%>/login.jsp" class="sesion_menu">Volver a Ingresar</a></div></td>
            </tr>
        </table>
    </body>
</html>
