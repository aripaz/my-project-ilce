<%--
    Document   : login
    Created on : 13/12/2010, 09:32:14 AM
    Author     : Administrador
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.7.custom.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/login.js"></script>
        <title>Iniciar sesión</title>
    </head>

    <body>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <table width="28%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
                <td><div align="center"><img src="img/logo_plataforma_ilce.jpg" width="197" height="154" /></div></td>
            </tr>
            <tr>
                <td>
                    <div id="divLogin">
                        <table width="75%" border="0" align="center" cellpadding="5" cellspacing="0">
                            <tr>
                                <td width="48%" ><div id="usuario" align="right" class="etiqueta_forma">Usuario</div></td>
                                <td width="52%"><div align="right">
                                        <input name="u" type="text" id="u" size="24" />
                                    </div></td>
                            </tr>
                            <tr>
                                <td ><div id="contrasena" align="right" class="etiqueta_forma">Contrase&ntilde;a</div></td>
                                <td><div align="right">
                                        <input name="c" type="password" id="c" size="24" />
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
                                <td><div align="right">
                                    </div></td>
                                <td><div align="right">
                                        <a href="#" id="lnkRecuperaPw" class="sesion_menu">Olvidé mi contrase&ntilde;a</a>
                                    </div></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="ui-widget" id="divMsgLogin">
                                        <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                            <p id="msjLogin"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
                                                <strong>Error!!</strong></p>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
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
                                <td>&nbsp;</td>
                                <td><div align="right"><a href="#" id="lnkIniciarSesion" name="lnkIniciarSesion" class="sesion_menu">Ir a iniciar sesi&oacute;n</a></div></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="ui-widget" id="divMsjRecuperaPW">
                                        <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                            <p id="msjRecuperaPW"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
                                                <strong>Error!!</strong></p>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>
