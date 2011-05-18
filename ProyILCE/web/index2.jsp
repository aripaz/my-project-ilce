<%--
    Document   : index
    Created on : 24/03/2011, 11:06:02 AM
    Author     : Omar Flores
--%>
<%@page import="mx.ilce.handler.ExecutionHandler" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link type="text/css" href="jQuery/css/cupertino/jquery-ui-1.8.7.custom.css" rel="stylesheet" />
        <link type="text/css" href="jQuery/css/plataforma_ilce.css" rel="stylesheet" />
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.7.custom.min.js"></script>
        <script type="text/javascript" src="jQuery/js/login.pi.js"></script>

        <script type="text/javascript" src="resource/js/validaLogin.js"></script>
        <title>ILCE</title>

        <style type="text/css">
            <!--
            .Estilo1 {font-family: sylfaen}
            .Estilo2 {color: red}
            -->
        </style>
    </head>
    <%
    ExecutionHandler execHand = (ExecutionHandler) request.getSession().getAttribute("loginHand");
    if (execHand == null){
        execHand = new ExecutionHandler();
    }
    %>

    <script type="text/javascript" >
    function focus(forma){
    var lgn = forma.lgn;
        lgn.focus();
        lgn.select();
    }
    </script>

    <body bgcolor="#F4F9FF" onLoad="focus(frmLogin);">

        <div>
            <table width="1000" height="129" border="0" align="center" >
                <tr>
                    <th scope="row" rowspan="2"><img src="resource/img/logoILCEgrande.jpg" width="150" height="100" align="right"></th>

                    <td height="6"><h1 class="Estilo1" align="left">Instituto Latinoamericano de la Comunicaci&oacute;n Educativa </h1>
                    </td>
                </tr>
                <tr>
                    <td height="10">
                        <h4>Organismo Internacional </h4>
                    </td>
                <tr>
            </table>

            <hr width="900">
        </div>

        <div id="Center" >
            <table align="center" bordercolor="#094B9D" style="border-width:thin" >
                <tr>
                    <td>
                        <table width="300" border="0" align="center" bgcolor="#c3d9ff" >
                            <form action="srvLogin" method="post" name="frmLogin" onSubmit="return validaForma(this);">
                                <tr>
                                    <td>
                                        <br>					</td>
                                </tr>

                                <tr>
                                    <td colspan="2" width="300" align="center">
                                        <P>Accede a tu cuenta en</P>					</td>
                                </tr>

                                <tr>
                                    <td colspan="2" width="300" align="center"><b>Plataforma ILCE</b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <br>					</td>
                                </tr>

                                <tr>
                                    <td width="134" align="right">Nombre de Usuario  </td>
                                    <td width="156"><input name="lgn" type="text" ></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;					</td>
                                    <td>
					ejemplo@ilce.edu.mx					</td>
                                </tr>

                                <tr>
                                    <td scope="row" align="right">Contrase&ntilde;a</td>
                                    <td><input name="psw" type="password" style="background:#F5FAAD"></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td class ="Estilo2"><%= execHand.getTextExecution()%></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td><button id="iniciarsesion" type="submit">Iniciar sesi&oacute;n</button></td>
                                </tr>
                            </form>
                            <tr>
                                <td colspan="2"></td>
                            </tr>
                            <tr>
                                <th scope="row" colspan="2" >&nbsp;</th>
                            </tr>
                            <tr>
                                <th scope="row" colspan="2" >&nbsp;</th>
                            </tr>
                            <tr>
                                <th scope="row" colspan="2" ><a href="#" target="_blank" style="font-size:12px">Recuperar Contrase&ntilde;a</a></th>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <hr width="900">
    </body>
</html>
