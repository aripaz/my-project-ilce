<%--
    Document   : index
    Created on : 24/03/2011, 11:06:02 AM
    Author     : Omar Flores
--%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="jQuery/css/cupertino/jquery-ui-1.8.7.custom.css" rel="stylesheet" />
        <link type="text/css" href="jQuery/css/plataforma_ilce.css" rel="stylesheet" />
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.7.custom.min.js"></script>
        <script type="text/javascript" src="jQuery/js/login.pi.js"></script>

        <script type="text/javascript" src="resource/js/validaLogin.js"></script>
        <title>JSP Page</title>
    </head>
    <body >
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <div id="center" >

            <table width="261" border="0" align="center">
                <tr>
                    <th scope="row" colspan="2" ><img src="resource/img/logoILCEgrande.jpg" width="298" height="229" alt="logoILCEgrande"/>
                    </th>
                </tr>
                <form action="srvLogin" method="post" name="frmLogin" onsubmit="return validaForma(this);">
                    <tr>
                        <th width="89" scope="row">CORREO</th>
                        <td width="162"><input name="lgn" type="text"></td>
                    </tr>
                    <tr>
                        <th scope="row">PASSWORD</th>
                        <td><input name="psw" type="password"></td>
                    </tr>
                    <tr>
                        <td><button id="iniciarsesion" type="submit">Iniciar sesi&oacute;n</button></td>
                    </tr>
                </form>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <th scope="row" colspan="2" ><a href="#" target="_blank" >Recuperar Contrase&ntilde;a</a></th>
                </tr>
                <tr>
                <td>
                    <div class="ui-widget" id="cuadro_mensaje">
                        <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                            <div id="texto_mensaje"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
                                <strong>Error!!</strong></div>
                        </div>
                    </div>
                </td>
            </tr>
            </table>
        </div>
    </body>
</html>