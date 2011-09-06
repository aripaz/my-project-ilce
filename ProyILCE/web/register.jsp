<%-- 
    Document   : register
    Created on : 05-sep-2011, 11:47:33
    Author     : ccatrilef
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    List lstAreas = (List) request.getSession().getAttribute("lstAreas");
    String strMsgExist = (String) request.getSession().getAttribute("msgExist");
    String _strNombre = (String) request.getSession().getAttribute("nombre");
    String _strAppPat = (String) request.getSession().getAttribute("appPat");
    String _strAppMat = (String) request.getSession().getAttribute("appMat");
    String _strEmail = (String) request.getSession().getAttribute("e_mail");
    String _strPassw1 = (String) request.getSession().getAttribute("passw1");
    String _strPassw2 = (String) request.getSession().getAttribute("passw2");

    String strNombre = request.getParameter("nombre");
    String strAppPat = request.getParameter("appPat");
    String strAppMat = request.getParameter("appMat");
    String strEmail = request.getParameter("e_mail");
    String strCmbArea = request.getParameter("cmbArea");
    String strPassw1 = request.getParameter("passw1");
    String strPassw2 = request.getParameter("passw2");

    if (strMsgExist==null){strMsgExist="";}
    if (lstAreas==null){lstAreas = new ArrayList();}
    if (strNombre==null){strNombre = (_strNombre==null)?"":_strNombre;}
    if (strAppPat==null){strAppPat = (_strAppPat==null)?"":_strAppPat;}
    if (strAppMat==null){strAppMat = (_strAppMat==null)?"":_strAppMat;}
    if (strEmail==null){strEmail = (_strEmail==null)?"":_strEmail;}
    if (strCmbArea==null){strCmbArea="0";}
    if (strPassw1==null){strPassw1 = (_strPassw1==null)?"":_strPassw1;}
    if (strPassw2==null){strPassw2 = (_strPassw2==null)?"":_strPassw2;}
%>
<script>
function retipePass(entrada){
    var strPass1 = document.frmRegister.passw1.value;
    var strPass2 = document.frmRegister.passw2.value;
    var sld = true;

    if (entrada){
        if ((strPass1.length==0)||(strPass2.length==0)){
            alert("La password no puede ser vacia");
            sld = false;
        }else{
            if (strPass1!=strPass2){
                alert("Las Password no coinciden");
                sld = false;
            }
        }
    }else{
        sld = entrada;
    }
    return sld;
}

function actualizarForm(entrada){
    if (entrada){
        document.frmRegister.action = "srvRegister";
        document.frmRegister.submit();
    }
}

function validarDatos(){
    var strNombre = document.frmRegister.nombre.value;
    var strAppPat = document.frmRegister.appPat.value;
    var strAppMap = document.frmRegister.appMat.value;
    var strEmail = document.frmRegister.e_mail.value;
    var intArea = document.frmRegister.cmbArea.value;
    var strError = "";
    var sld = true;

    if (strNombre.trim().length==0){
        strError = strError + "Nombre, ";
    }
    if (strAppPat.trim().length==0){
        strError = strError + "Apellido Paterno, ";
    }
    if (strAppMap.trim().length==0){
        strError = strError + "Apellido Materno, ";
    }
    if (strEmail.trim().length==0){
        strError = strError + "Email, ";
    }
    if (intArea==0){
        strError = strError + "Area, ";
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
        <title>Registro de Usuarios</title>
    </head>
    <body>
    <form action="" method="post" name="frmRegister" id="frmRegister">
        <input type="hidden" id="hidMsg" name="hidMsg" value="<%=strNombre%>"/>
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
                    <div  align="center">
                        <table width="100%" align="center">
                        <tr>
                            <td width="50%">
                                <div id="nombre" align="left" class="etiqueta_forma">Nombre</div>
                            </td>
                            <td width="50%" class="etiqueta_forma">
                                <div align="left">
                                    <input name="nombre" type="text" id="nombre" size="24" value="<%=strNombre%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="appPat" align="right" class="etiqueta_forma">Apellido Paterno</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                    <input name="appPat" type="text" id="appPat" size="24" value="<%=strAppPat%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="appMat" align="right" class="etiqueta_forma">Apellido Materno</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                    <input name="appMat" type="text" id="appMat" size="24" value="<%=strAppMat%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="e_mail" align="right" class="etiqueta_forma">E-Mail</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                    <input name="e_mail" type="text" id="e_mail" size="24" value="<%=strEmail%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="e_mail" align="right" class="etiqueta_forma">Area</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                <select name="cmbArea" id="cmbArea" class="inputWidgeted" >
                                    <option id="0" value="0">Seleccione</option>
                                    <%
                                        if (!lstAreas.isEmpty()){
                                            Iterator it = lstAreas.iterator();
                                            while (it.hasNext()){
                                                String[] str = (String[]) it.next();
                                                if (strCmbArea.equals(str[0])){
                                    %>
                                    <option id="<%=str[0]%>" value="<%=str[0]%>" selected><%=str[1]%></option>
                                    <%
                                                }else{
                                    %>
                                    <option id="<%=str[0]%>" value="<%=str[0]%>"><%=str[1]%></option>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2"><div style="color: red">&nbsp;</div></td>
                        </tr>
                        <tr>
                            <td>
                                <div id="passw1" align="right" class="etiqueta_forma">Password</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                    <input name="passw1" type="password" id="passw1" size="24" value="<%=strPassw1%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="passw2" align="right" class="etiqueta_forma">Repita Password</div>
                            </td>
                            <td class="etiqueta_forma">
                                <div align="left">
                                    <input name="passw2" type="password" id="passw2" size="24" value="<%=strPassw2%>"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div align="center">
                                    <button id="iniciarsesion" onclick="actualizarForm(retipePass(validarDatos()))">Ingresar</button>
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
