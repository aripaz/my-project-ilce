<%-- 
    Document   : forgotPass
    Created on : 06-sep-2011, 15:55:00
    Author     : ccatrilef
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String strMsgExist = (String) request.getSession().getAttribute("msgExist");
    String _strEmail = (String) request.getSession().getAttribute("e_mail");
    String strMail = request.getParameter("e_mail");

    if (strMsgExist==null){strMsgExist="";}
    if (strMail==null){strMail = (_strEmail==null)?"":_strEmail;}

%>
<script language="javascript">
function actualizarForm(entrada){
    if (entrada){
        document.frmForgotPass.action = "srvForgotPass";
        document.frmForgotPass.submit();
    }
}

function validarDatos(){
    var strEmail = document.frmForgotPass.e_mail.value;
    var strError = "";
    var sld = true;

    if (strEmail.trim().length==0){
        strError = strError + "Email, ";
    }
    if (strError.toString().length>0){
        alert("Los siguientes datos se encuentran vacios:\n" + strError.toString().substr(0,strError.length-2));
        sld = false;
    }
    return sld;
}
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.7.custom.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <title>Recuperacion de Password</title>
    </head>
    <body>
    <form action="" method="post" name="frmForgotPass" id="frmForgotPass">
        <table width="25%" border="0" align="center" cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <img src="img/logo_plataforma_ilce.jpg" width="197" height="154" />
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    Para la recuperacion de su password, ingrese su email de usuario.
                    Un correo sera enviado a dicha direccion con su password.
                </td>
            </tr>
            <tr>
                <td>
                    <div  align="center">
                        <table width="100%" align="center">
                        <tr>
                            <td width="50%">
                                <div id="e_mail" align="left" class="etiqueta_forma">Email</div>
                            </td>
                            <td width="50%" class="etiqueta_forma">
                                <div align="left">
                                    <input name="e_mail" type="text" id="e_mail" size="24" value="<%=strMail%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div align="center">
                                    <button id="iniciarsesion" onclick="actualizarForm(validarDatos())">Ingresar</button>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan ="2">
<%
if (strMsgExist.length()>0){
%>
                                <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                    <p id="msjLogin"><%=strMsgExist%></p></div>
<%}%>
                            </td>
                        </tr>
                    </table>
                    </div>
                </td>
            </tr>
        </table>
    </form>
    </body>
</html>
