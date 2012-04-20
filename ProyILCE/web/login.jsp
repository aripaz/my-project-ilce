 <%--
    Document   : login
    Created on : 13/12/2010, 09:32:14 AM
    Author     : Administrador
--%>
<%@page import="mx.ilce.handler.ExecutionHandler" %>
<%
    ExecutionHandler execHand = (ExecutionHandler) request.getSession().getAttribute("loginHand");
    if (execHand == null) {
        execHand = new ExecutionHandler();
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.7.custom.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <link type="text/css" href="css/agile_carousel.css" rel="stylesheet"/>
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/login.js"></script>
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js"></script>
        <title> SIAP2.0 / Iniciar sesión</title>
        <style>
            .agile_carousel .numbered_button {
               float:none !important;
            }
        </style>
    </head>

    <body>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>Probando</p>
        <table width="35%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
                <td>
                    <br /><br />
                    <div align="center">
                        <table style="width:600px">
                            <tr>
                                <td><img src="img/logo sicap1.png" /></td>
                                <td style="width:1000px">&nbsp;</td>
                                <td><img src="img/logo ilce.jpg" /></td>
                            </tr>
                        </table>
                    </div>
                    <br /><br/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="divCarousel">
                        <div id="divLogin">
                            <form action="srvLogin" method="post" name="frmLogin" id="frmLogin">
                                <table width="75%" border="0" align="center" cellpadding="5" cellspacing="0">
                                    <tr>
                                        <td width="48%" ><div id="usuario" align="right" class="etiqueta_forma">Usuario</div></td>
                                        <td width="52%"><div align="right">
                                                <input name="lgn" type="text" id="lgn" size="24" />
                                            </div></td>
                                    </tr>
                                    <tr>
                                        <td ><div id="contrasena" align="right" class="etiqueta_forma">Contrase&ntilde;a</div></td>
                                        <td><div align="right">
                                                <input name="psw" type="password" id="psw" size="24" />
                                            </div></td>
                                    </tr>
                                    <tr>
                                        <td><div align="right">
                                            </div></td>
                                        <td><div align="right">
                                                <button id="iniciarsesion">Iniciar sesi&oacute;n</button>
                                            </div></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="ui-widget" id="divMsgLogin">
                                                <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                                    <p id="msjLogin"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span><%=execHand.getTextExecution()%></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>                                
                                </table>
                            </form>
                        </div>
                        <div id="divLostPw">
                            <table width="75%" border="0" align="center" cellpadding="5" cellspacing="0">
                                <tr>
                                    <td width="48%"><div id="usuario" align="right" class="etiqueta_forma">Usuario</div></td>
                                    <td width="52%"><div align="right">
                                            <input name="rc" type="text" id="rc" size="24" />
                                        </div></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div align="right">
                                            <button id="btnRecuperarPw">Recuperar contrase&ntilde;a</button>
                                        </div></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="ui-widget" id="divMsjRecuperaPW">
                                            <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                                <p id="msjRecuperaPW"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span><strong></strong></p>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>
<% execHand.setTextExecution("");%>