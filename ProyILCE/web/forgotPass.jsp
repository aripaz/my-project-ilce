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

    String strRespuesta = (String) request.getSession().getAttribute("xmlTab");

    if (strRespuesta==null){strRespuesta="";}

    String[] strError = strRespuesta.split("<error>");
    String[] strQuery = strRespuesta.split("<resultado>");
    String strPart = "";

    String strSalida = "";
    if ((strError!=null)&&(strError.length > 1)){
        String[] desc1 = strError[1].split("<descripcion>");
        String[] desc2 = desc1[1].split("</descripcion>");
        strPart = "Se ha producido un error en el envio del mail de Recuperacion";
        strSalida = desc2[0];
    }else if((strQuery!=null)&&(strQuery.length>1)){
        String[] query1 = strQuery[1].split("</resultado>");
        strSalida = query1[0] + "<br><br>Revice su correo y siga las instrucciones";
    }

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
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
    <form action="" method="post" name="frmForgotPass" id="frmForgotPass">
        <table width="35%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <img src="img/logo_plataforma_ilce.jpg" width="197" height="154" />
                    </div>
                </td>
            </tr>
<%
    if (strRespuesta.length()==0){
%>
            <tr>
                <td align="center">
                    <br>
                    <span style="font-size:12pt" >
                    Para la recuperacion de su password, ingrese su email de usuario.<br>
                    Un correo sera enviado a dicha direccion con su password.
                    </span>
                    <br><br><br>
                </td>
            </tr>
            <tr>
                <td>
                    <div  align="left">
                        <table width="100%" align="center">
                        <tr>
                            <td width="15%">
                                <div id="e_mail" align="left" class="etiqueta_forma">Email</div>
                            </td>
                            <td width="85%" class="etiqueta_forma">
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
                <%if (strMsgExist.length()>0){%>
                                <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                    <p id="msjLogin"><%=strMsgExist%></p></div>
                <%}%>
                            </td>
                        </tr>
                    </table>
                    </div>
                </td>
            </tr>
<%}else{%>
            <tr>
                <td align="center">
                    <br><br><br>
                    <%if (strPart.length()>0){%>
                        <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                            <p id="msjLogin"><%=strPart%></p>
                        </div>
                        <br><br>
                        <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                            <p id="msjLogin">
                                <%=strSalida%></p>
                        </div>
                    <%}else{%>
                        <span style="font-size: 12pt">
                            <p><%=strSalida%></p>
                        </span>
                    <%}%>
                </td>
            </tr>
<%}%>
        </table>
    </form>
    </body>
</html>
