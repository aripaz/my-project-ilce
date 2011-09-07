<%-- 
    Document   : forgotPassResp
    Created on : 07-sep-2011, 10:12:32
    Author     : ccatrilef
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    String strRespuesta = (String) request.getSession().getAttribute("xmlTab");

    String[] strError = strRespuesta.split("<error>");
    String[] strQuery = strRespuesta.split("<query>");

    String strSalida = "";
    if ((strError!=null)&&(strError.length > 1)){
        String[] desc1 = strError[1].split("<descripcion>");
        String[] desc2 = desc1[1].split("</descripcion>");
        strSalida = desc2[0];
    }else if((strQuery!=null)&&(strQuery.length>1)){
        String[] query1 = strQuery[1].split("</query>");
        strSalida = query1[0];
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.7.custom.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <title>Respuesta Recuperacion de Password</title>
    </head>
    <body>
    <form action="" method="post" name="frmForgotPassResp" id="frmForgotPassResp">
        <table width="25%" border="0" align="center" cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <img src="img/logo_plataforma_ilce.jpg" width="197" height="154" />
                    </div>
                </td>
            </tr>
            <tr>
                <td><%=strSalida%></td>
            </tr>
        </table>
    </form>
    </body>
</html>