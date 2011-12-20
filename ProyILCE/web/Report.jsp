<%--
    Document   : Report
    Created on : 19/09/2011, 10:30:19 AM
    Author     : ccatrilef
--%>
<%@page import="mx.ilce.report.Structure"%>
<%@page import="mx.ilce.report.ElementStruct"%>
<%@page import="java.util.HashMap"%>
<%@page import="mx.ilce.report.Config"%>
<%@page import="mx.ilce.report.Report"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String frmADDRPT = (String) request.getSession().getAttribute("frmADDRPT");
    String frmUPDRPT = (String) request.getSession().getAttribute("frmUPDRPT");
    String idReport = (String) request.getSession().getAttribute("idReport");
    Report reportUPD = (Report) request.getSession().getAttribute("reportUPD");
    List lstReport = (List) request.getSession().getAttribute("lstReport");

    String frmSTR = (String) request.getSession().getAttribute("frmSTR");

    String frmADDSTR = (String) request.getSession().getAttribute("frmADDSTR");
    String frmUPDSTR = (String) request.getSession().getAttribute("frmUPDSTR");
    String idTypeStructure = (String) request.getSession().getAttribute("idTypeStructure");
    String idStructure = (String) request.getSession().getAttribute("idStructure");
    List lstTypeStructure = (List) request.getSession().getAttribute("lstTypeStructure");
    List lstStructure = (List) request.getSession().getAttribute("lstStructure");
    Structure confSTR = (Structure) request.getSession().getAttribute("confSTR");
    Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");
    String idConfigStructure = (String) request.getSession().getAttribute("idConfigStructure");
    String ADDSTR = (String) request.getSession().getAttribute("ADDSTR");

    String frmCNFSTR = (String) request.getSession().getAttribute("frmCNFSTR");
    String frmADDCNFSTR = (String) request.getSession().getAttribute("frmADDCNFSTR");
    String frmUPDCNFSTR = (String) request.getSession().getAttribute("frmUPDCNFSTR");
    List lstTypeConfig = (List) request.getSession().getAttribute("lstTypeConfig");
    List lstConfigStructure = (List) request.getSession().getAttribute("lstConfigStructure");
    List lstTypeValue = (List) request.getSession().getAttribute("lstTypeValue");
    List lstConfigValue = (List) request.getSession().getAttribute("lstConfigValue");
    List lstMeasure = (List) request.getSession().getAttribute("lstMeasure");
    String idTypeConfig = (String) request.getSession().getAttribute("idTypeConfig");
    String idTypeValue = (String) request.getSession().getAttribute("idTypeValue");
    String idConfigValue = (String) request.getSession().getAttribute("idConfigValue");
    String idMeasure = (String) request.getSession().getAttribute("idMeasure");
    String txtConfigValue = (String) request.getSession().getAttribute("txtConfigValue");
    String ADDCNFSTR = (String) request.getSession().getAttribute("ADDCNFSTR");

    String seeBUTTON = (String) request.getSession().getAttribute("seeBUTTON");


    List lstElement = (List) request.getSession().getAttribute("lstElement");
    List lstTypeElement = (List) request.getSession().getAttribute("lstTypeElement");
    String frmADDELEM = (String) request.getSession().getAttribute("frmADDELEM");
    String btnUPDELEM = (String) request.getSession().getAttribute("btnUPDELEM");
    String idElement = (String) request.getSession().getAttribute("idElement");
    String frmUPDELEM = (String) request.getSession().getAttribute("frmUPDELEM");
    String idTypeElement = (String) request.getSession().getAttribute("idTypeElement");
    String txtElementValue = (String) request.getSession().getAttribute("txtElementValue");
    List lstConfigElem = (List) request.getSession().getAttribute("lstConfigElem");
    String frmCNFELEM = (String) request.getSession().getAttribute("frmCNFELEM");
    String frmADDCNFELEM = (String) request.getSession().getAttribute("frmADDCNFELEM");
    String frmUPDCNFELEM = (String) request.getSession().getAttribute("frmUPDCNFELEM");
    List lstTypeConfigElem = (List) request.getSession().getAttribute("lstTypeConfigElem");
    List lstTypeValueElem = (List) request.getSession().getAttribute("lstTypeValueElem");
    List lstConfigValueElem = (List) request.getSession().getAttribute("lstConfigValueElem");
    List lstMeasureElem = (List) request.getSession().getAttribute("lstMeasureElem");
    String idTypeConfigElem = (String) request.getSession().getAttribute("idTypeConfigElem");
    String idTypeValueElem = (String) request.getSession().getAttribute("idTypeValueElem");
    String idConfigValueElem = (String) request.getSession().getAttribute("idConfigValueElem");
    String idMeasureElem = (String) request.getSession().getAttribute("idMeasureElem");
    String txtConfigValueElem = (String) request.getSession().getAttribute("txtConfigValueElem");
    String idConfigElement = (String) request.getSession().getAttribute("idConfigElement");
    Config configQUERY = (Config) request.getSession().getAttribute("configQUERY");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
function actualizarForm(entrada){
    if (entrada){
        document.RP.action = "SrvReport";
        document.RP.submit();
    }
}
function selectOper(entrada){
    document.RP.oper.value = entrada
    actualizarForm(true);
}
function validateReport(entrada){
    var strNombre = document.RP.nombreRPT.value;
    if ((strNombre==null)||(strNombre.length<=0)){
        alert("Debe ingresar un nombre para el Reporte");
    }else{
        selectOper(entrada);
    }
}

function validateDelStructure(){
    var strStructure = null;
    var largo = document.RP.idConfigReport.length;

    for(var i=0; (i<largo) && (strStructure==null); i++) {
        if(document.RP.idConfigReport[i].checked) {
            strStructure = document.RP.idConfigReport[i].value;
        }
    }
    if (strStructure==null){
        alert("Debe seleccionar la Estructura a borrar");
    }else{
        var strData = strStructure.split('-');
        if (strData[1]==1){
            alert("Esta Estructura no puede ser eliminada");
        }else{
            selectOper('DELSTR');
        }
    }
}

function validateStructure(){
    var strNombre = document.RP.nameStructure.value;
    var strTipo = document.RP.idTypeStructure.value;
    var strData = "";

    if ((strNombre==null)||(strNombre.length<=0)){
        strData = " - Nombre Estuctura";
    }
    if (strTipo==0){
        if (strData==""){
            strData = " - Tipo Estructura";
        }else{
            strData = strData + "\n - Tipo Estructura";
        }
    }
    if (strData==""){
        selectOper('ADDSTR');
    }else{
        alert("No ha ingresado los datos:\n"+strData);
    }
}

function validateConfiguration(){
    var strValor = document.RP.valueTypeConfigStructure.value;
    var strTipo = document.RP.idTypeConfigStructure.value;
    var strData = "";

    if (strTipo==0){
        strData = " - Tipo Configuracion";
    }
    if ((strValor==null)||(strValor.length<=0)) {
        if (strData==""){
            strData = " - Valor Configuracion";
        }else{
            strData = strData + "\n - Valor Configuracion";
        }
    }
    if (strData==""){
        selectOper('ADDCNF');
    }else{
        alert("No ha ingresado los datos:\n"+strData);
    }
}
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<title>JSP Report</title>
</head>
<body>
<form action="" name="RP" method="post" enctype="multipart/form-data">
<table>
<tr>
    <td>MODULO DE CONFIGURACION DE REPORTES</td>
</tr>
<%
    //frmADDRPT: FORMULARIO AGREGAR-MODIFICAR REPORTE
    if (frmADDRPT!=null){
        //botones: GUARDAR-CANCELAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="2">NUEVO REPORTE</td>
            </tr>
            <tr>
                <td>Nombre</td>
                <td>
                    <input type="text" value="" id="nombreRPT" name="nombreRPT">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="validateReport('ADDRPT')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('ADDRPTX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
    //frmADDRPT: FORMULARIO REPORTE
    }else{
        //botones: AGREGAR-MODIFICAR-ELIMINAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td></td>
                <td>
                    <select id="idReport" name="idReport" onchange="selectOper('frmSTR')">
                        <option value="0">Seleccione</option>
<%
        if (lstReport!=null){
            Iterator itRPT = lstReport.iterator();
            while (itRPT.hasNext()){
                Report rpt = (Report) itRPT.next();
                if ((idReport!=null)&&(rpt.getIdReport().toString().equals(idReport))){
%>
                        <option value="<%=rpt.getIdReport()%>" selected><%=rpt.getReport()%></option>
<%
                }else{
%>
                        <option value="<%=rpt.getIdReport()%>"><%=rpt.getReport()%></option>
<%
                }
            }
        }
%>
                    </select>
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        //botones FORMULARIO REPORTE
        if (frmUPDRPT==null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDRPT')">
                </td>
<%
            if ((idReport!=null)&&(!"0".equals(idReport))){
%>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('frmUPDRPT')">
                </td>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELRPT')">
                </td>
<%
            }
%>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
    }
    //frmUPDRPT: FORMULARIO MODIFICACION REPORTE
    if (frmUPDRPT!=null){
        //botones: GUARDAR-CANCELAR
        if (reportUPD!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="2">MODIFICAR NOMBRE REPORTE</td>
            </tr>
            <tr>
                <td>Nombre</td>
                <td>
                    <input type="text" value="<%=reportUPD.getReport()%>" id="nombreRPT" name="nombreRPT">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="validateReport('UPDRPT')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('UPDRPTX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
    }
    //frmADDSTR: FORMULARIO AGREGAR ESTRUCTURA
    if (frmADDSTR!=null){
        //botones: GUARDAR-CANCELAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>Nombre</td>
                <td>
                    <input type="text" id="nombreSTR" name="nombreSTR">
                </td>
            </tr>
            <tr>
                <td>Tipo</td>
                <td>
                    <select id="idTypeStructure" name="idTypeStructure">
                        <option value="0">Seleccione</option>
<%
        if (lstTypeStructure!=null){
            Iterator itSTRType = lstTypeStructure.iterator();
            while (itSTRType.hasNext()){
                Structure struct = (Structure) itSTRType.next();
                if ((idTypeStructure!=null)&&(struct.getIdTypeStructure().toString().equals(idTypeStructure))){
%>
                        <option value="<%=struct.getIdTypeStructure()%>" selected><%=struct.getTypeStructure()%></option>
<%
                }else{
%>
                        <option value="<%=struct.getIdTypeStructure()%>"><%=struct.getTypeStructure()%></option>
<%
                }
            }
        }
%>
                    </select>
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('ADDSTR')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('ADDSTRX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
    }
    //frmSTR: FORMULARIO ESTRUCTURA
    if (frmSTR!=null){
        //lstStructure: EXISTE LISTADO ESTRUCTURA
        if ((lstStructure!=null)&&(!lstStructure.isEmpty())){
            //botones: AGREGAR-ELIMINAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="4">
                    ESTRUCTURA DEL REPORTE
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>Nombre</td>
                <td>Tipo</td>
                <td>Principal</td>
            </tr>
<%
                Iterator itSTR = lstStructure.iterator();
                while (itSTR.hasNext()){
%>
            <tr>
                <td>
<%
                    Structure struct = (Structure) itSTR.next();
                    if ((idStructure!=null)&&(struct.getIdStructure().toString().equals(idStructure))){
%>
                    <input type="radio" id="idStructure" name="idStructure" value="<%=struct.getIdStructure()%>" onclick="selectOper('frmCNFSTR')" checked="checked">
<%
                    }else{
%>
                    <input type="radio" id="idStructure" name="idStructure" value="<%=struct.getIdStructure()%>" onclick="selectOper('frmCNFSTR')">
<%
                    }
%>
                </td>
                <td><%=struct.getStructure()%></td>
                <td><%=struct.getTypeStructure()%></td>
                <td><%=struct.getMainFig()%></td>
            </tr>
<%
                }
                boolean modButton = false;
                boolean delButton = false;
                if (confSTRMAIN!=null){
                    int val = confSTRMAIN.getMainFig();
                    modButton = true;
                    if (val==0){
                        delButton = true;
                    }
                }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDSTR')">
                </td>
<%
                if (modButton){
%>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('frmUPDSTR')">
                </td>
<%
                    if (delButton){
%>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELSTR')">
                </td>
<%
                    }
                }
%>
            </tr>
        </table>
    </TD>
</TR>
<%
        }else{
            //lstStructure: NO EXISTE LISTADO ESTRUCTURA
            if (ADDSTR!=null){
                //botones: AGREGAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    ESTRUCTURA DEL REPORTE
                </td>
            </tr>
            <tr>
                <td>No existe estructura para este Reporte</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDSTR')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
        //frmUPDSTR: FORMULARIO MODIFICACION ESTRUCTURA
        if (frmUPDSTR!=null){
            //botones: GUARDAR-CANCELAR
            //debe existir una configuracion que modificar
            if (confSTR!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="2">MODIFICACION ESTRUCTURA</td>
            </tr>
            <tr>
                <td>Estructura</td>
                <td>
                    <input type="text" id="nombreSTR" name="nombreSTR" value="<%=confSTR.getStructure()%>">
                </td>
            </tr>
<%
            idTypeStructure = confSTR.getIdTypeStructure().toString();
            if (confSTR.getMainFig()==1){
%>
            <tr>
                <td>
                    <input type="hidden" id="idTypeStructure" name="idTypeStructure" value="<%=idTypeStructure%>">
                </td>
            </tr>
<%
            }else{
%>
            <tr>
                <td>Tipo</td>
                <td>
                    <select id="idTypeStructure" name="idTypeStructure">
                        <option>Seleccione</option>
<%
                if (lstTypeStructure!=null){
                    Iterator itSTRType = lstTypeStructure.iterator();
                    while (itSTRType.hasNext()){
                        Structure struct = (Structure) itSTRType.next();
                        if ((idTypeStructure!=null)&&(struct.getIdTypeStructure().toString().equals(idTypeStructure))){
%>
                        <option value="<%=struct.getIdTypeStructure()%>" selected><%=struct.getTypeStructure()%></option>
<%
                        }else{
%>
                        <option value="<%=struct.getIdTypeStructure()%>"><%=struct.getTypeStructure()%></option>
<%
                        }
                    }
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('UPDSTR')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('UPDSTRX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
    }
    //frmADDCNFSTR: FORMULARIO AGREGAR CONFIGURACION ESTRUCTURA
    if (frmADDCNFSTR!=null){
        //botones: GUARDAR-CANCELAR
        //debe existir el listado de tipo de estructura
        if (lstTypeConfig!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="2">AGREGAR CONFIGURACION ESTRUCTURA</td>
            </tr>
            <tr>
                <td>Tipo Configuracion</td>
                <td>
                    <select id="idTypeConfig" name="idTypeConfig" onchange="selectOper('frmADDCNFSTR')">
                        <option value="0">Seleccione</option>
<%
            Iterator itCNFSTR = lstTypeConfig.iterator();
            while (itCNFSTR.hasNext()){
                Config conf = (Config) itCNFSTR.next();
                if ((idTypeConfig!=null)&&(conf.getIdTypeConfig().toString().equals(idTypeConfig))){
%>
                        <option value="<%=conf.getIdTypeConfig()%>" selected><%=conf.getTypeConfig()%></option>
<%
                }else{
%>
                        <option value="<%=conf.getIdTypeConfig()%>"><%=conf.getTypeConfig()%></option>
<%
                }
            }
%>
                    </select>
                </td>
            </tr>
<%
            if (lstTypeValue!=null){
%>
            <tr>
                <td>Tipo Valor</td>
                <td>
                    <select id="idTypeValue" name="idTypeValue">
                        <option value="0">Seleccione</option>
<%
                String strLTV = "";
                if (lstTypeValue.size()==1){
                    strLTV = "selected";
                }
                Iterator itLTV = lstTypeValue.iterator();
                while(itLTV.hasNext()){
                    Config conf = (Config) itLTV.next();
%>
                        <option value="<%=conf.getIdTypeValue()%>" <%=strLTV%>><%=conf.getTypeValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
            if (lstConfigValue!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <select id="idConfigValue" name="idConfigValue" >
                        <option value="0">Seleccione</option>
<%
                String strLCV = "";
                if (lstConfigValue.size()==1){
                    strLCV = "selected";
                }
                Iterator itLCV = lstConfigValue.iterator();
                while (itLCV.hasNext()){
                    Config conf = (Config) itLCV.next();
%>
                        <option value="<%=conf.getIdConfigValue()%>" <%=strLCV%>><%=conf.getConfigValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }else{
                if (lstMeasure!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <input type="text" id="txtConfigValue" name="txtConfigValue">
                </td>
            </tr>
            <tr>
                <td>Medida</td>
                <td>
                    <select id="idMeasure" name="idMeasure">
                        <option>Seleccione</option>
<%
                    Iterator itM = lstMeasure.iterator();
                    while (itM.hasNext()){
                        Config conf = (Config) itM.next();
%>
                        <option value="<%=conf.getIdMeasure()%>"><%=conf.getMeasure()%></option>
<%
                    }
%>
                    </select>
                </td>
            </tr>
<%
                }
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('ADDCNFSTR')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('ADDCNFSTRX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
    //frmCNFSTR: FORMULARIO CONFIGURACION ESTRUCTURA
    }
    if (frmCNFSTR!=null){
        //lstConfigStruct: EXISTE LISTADO CONFIGURACION ESTRUCTURA
        if((lstConfigStructure!=null)&& (!lstConfigStructure.isEmpty())){
            //botones: AGREGAR-MODIFICAR-ELIMINAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="4">CONFIGURACION ESTRUCTURA</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>Tipo</td>
                <td>Tipo Configuracion</td>
                <td>Valor</td>
            </tr>
<%
            Iterator itSTR = lstConfigStructure.iterator();

            while (itSTR.hasNext()){
                Config conf = (Config) itSTR.next();
%>
            <tr>
                <td>
<%
                if ((idConfigStructure!=null)&&(conf.getIdConfigStructure().toString().equals(idConfigStructure))){
%>
                    <input type="radio" id="idConfigStructure" name="idConfigStructure" value="<%=conf.getIdConfigStructure()%>" onclick="selectOper('btnUPDCNFSTR')" checked="checked">
<%
                }else{
%>
                    <input type="radio" id="idConfigStructure" name="idConfigStructure" value="<%=conf.getIdConfigStructure()%>" onclick="selectOper('btnUPDCNFSTR')">
<%
                }
%>
                </td>
                <td><%=conf.getTypeConfig() %></td>
                <td><%=conf.getTypeValue() %></td>
                <td><%=conf.getConfigValue()%></td>
            </tr>
<%
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDCNFSTR')">
                </td>
<%
            if (idConfigStructure!=null){
%>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('frmUPDCNFSTR')">
                </td>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELCNFSTR')">
                </td>
<%
            }
%>
            </tr>
        </table>
    </TD>
</TR>
<%
        }else{
            //lstConfigStruct: NO EXISTE LISTADO CONFIGURACION ESTRUCTURA
            if (ADDCNFSTR!=null){
            //botones: AGREGAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>CONFIGURACION ESTRUCTURA</td>
            </tr>
            <tr>
                <td>No existe configuracion para la estructura</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDCNFSTR')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
        //frmUPDCNFSTR: FORMULARIO MODIFICACION CONFIGURACION ESTRUCTURA
        if (frmUPDCNFSTR!=null){
            //botones: GUARDAR-CANCELAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td colspan="2">MODIFICACION DE CONFIGURACION DE ESTRUCTURA</td>
            </tr>
            <tr>
                <td>Tipo Configuracion</td>
                <td>
                    <select id="idTypeConfig" name="idTypeConfig" onchange="selectOper('frmUPDCNFSTRCH')">
                        <option value="0">Seleccione</option>
<%
            String strCNFSTR = "";
            Iterator itCNFSTR = lstTypeConfig.iterator();
            while (itCNFSTR.hasNext()){
                Config conf = (Config) itCNFSTR.next();
                if ((idTypeConfig!=null)&& (conf.getIdTypeConfig().toString().equals(idTypeConfig))){
                    strCNFSTR = "selected";
                }else{
                    strCNFSTR = "";
                    if (lstTypeConfig.size()==1){
                        strCNFSTR = "selected";
                    }
                }
%>
                        <option value="<%=conf.getIdTypeConfig()%>" <%=strCNFSTR%>><%=conf.getTypeConfig()%></option>
<%
            }
%>
                    </select>
                </td>
            </tr>
<%
            if (lstTypeValue!=null){
%>
            <tr>
                <td>Tipo Valor</td>
                <td>
                    <select id="idTypeValue" name="idTypeValue">
                        <option value="0">Seleccione</option>
<%
                String strLTV = "";
                Iterator itLTV = lstTypeValue.iterator();
                while(itLTV.hasNext()){
                    Config conf = (Config) itLTV.next();
                    if ((idTypeValue!=null)&&(conf.getIdTypeValue().toString().equals(idTypeValue))){
                        strLTV= "selected";
                    }else{
                        strLTV= "";
                        if (lstTypeValue.size()==1){
                            strLTV = "selected";
                        }
                    }
%>
                        <option value="<%=conf.getIdTypeValue()%>" <%=strLTV%>><%=conf.getTypeValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
            if (lstConfigValue!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <select id="idConfigValue" name="idConfigValue" >
                        <option value="0">Seleccione</option>
<%
                String strLCV = "";
                Iterator itLCV = lstConfigValue.iterator();
                while (itLCV.hasNext()){
                    Config conf = (Config) itLCV.next();
                    if ((idConfigValue!=null)&&(conf.getIdConfigValue().toString().equals(idConfigValue))){
                        strLCV = "selected";
                    }else{
                        strLCV = "";
                        if (lstConfigValue.size()==1){
                            strLCV = "selected";
                        }
                    }
%>
                        <option value="<%=conf.getIdConfigValue()%>" <%=strLCV%>><%=conf.getConfigValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }else{
                if (lstMeasure!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <input type="text" id="txtConfigValue" name="txtConfigValue" value="<%=txtConfigValue%>">
                </td>
            </tr>
            <tr>
                <td>Medida</td>
                <td>
                    <select id="idMeasure" name="idMeasure">
                        <option>Seleccione</option>
<%
                    String strM ="";
                    Iterator itM = lstMeasure.iterator();
                    while (itM.hasNext()){
                        Config conf = (Config) itM.next();
                        if ((idMeasure!=null)&&(conf.getIdMeasure().toString().equals(idMeasure))){
                            strM ="selected";
                        }else{
                            strM ="";
                            if (lstMeasure.size()==1){
                                strM = "selected";
                            }
                        }
%>
                        <option value="<%=conf.getIdMeasure()%>" <%=strM%>><%=conf.getMeasure()%></option>
<%
                    }
%>
                    </select>
                </td>
            </tr>
<%
                }
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('UPDCNFSTR')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('UPDCNFSTRX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
    }
    //seeBUTTON: VER LOS BOTONES DE TEXTO Y QUERY
    if (seeBUTTON=="OKBUTTON"){
        //botones: VER TEXTO-VER QUERY
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="VER ELEMENTOS" onclick="selectOper('SEEELEM')">
                </td>
                <td>
                    <input type="button" value="VER QUERY" onclick="selectOper('SEEQUERY')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
    //SECCION DE TEXTO
    }else if (seeBUTTON=="SEEELEM"){
        if (frmADDELEM!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>AGREGAR ELEMENTO A ESTRUCTURA</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Tipo Elemento</td>
                <td>
                    <select id="idTypeElement" name="idTypeElement">
                        <option>Seleccione</option>
<%
                if (lstTypeElement!=null){
                    Iterator itTE = lstTypeElement.iterator();
                    while(itTE.hasNext()){
                        ElementStruct elem = (ElementStruct) itTE.next();
%>
                        <option value="<%=elem.getIdTypeElement()%>" ><%=elem.getTypeElement()%></option>
<%
                    }
                }
%>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Valor elemento</td>
                <td>
                    <input type="text" id="txtElementValue" name="txtElementValue">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('ADDELEM')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('SEEELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
        //configTEXT: Existe Texto
        if ((lstElement!=null)&&(!lstElement.isEmpty())){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>ELEMENTOS DE LA ESTRUCTURA</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>&nbsp;</td>
                <td>Elemento</td>
                <td>Tipo de Elemento</td>
            </tr>
<%
            Iterator itELEM = lstElement.iterator();
            while (itELEM.hasNext()){
                ElementStruct elem = (ElementStruct) itELEM.next();
%>
            <tr>
                <td>
<%
                if ((idElement!=null)&&(elem.getIdElementStruct().toString().equals(idElement))){
%>
                    <input type="radio" id="idElement" name="idElement" value="<%=elem.getIdElementStruct()%>" onclick="selectOper('btnUPDELEM')" checked="checked">
<%
                }else{
%>
                    <input type="radio" id="idElement" name="idElement" value="<%=elem.getIdElementStruct()%>" onclick="selectOper('btnUPDELEM')">
<%
                }
%>
                </td>
                <td><%=elem.getValueElement()%></td>
                <td><%=elem.getTypeElement()%></td>
            </tr>
<%
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDELEM')">
                </td>
<%
            if (btnUPDELEM!=null){
%>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('frmUPDELEM')">
                </td>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELELEM')">
                </td>
<%
            }
%>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('SEEELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }else{
            if(frmADDELEM==null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>ELEMENTOS DE LA ESTRUCTURA</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>No exiten elementos para la Estructura</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDELEM')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('SEEELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
        if (frmUPDELEM!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>MODIFICAR ELEMENTO DE ESTRUCTURA</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Tipo Elemento</td>
                <td>
                    <select id="idTypeElement" name="idTypeElement">
                        <option>Seleccione</option>
<%
                if (lstTypeElement!=null){
                    String strTE = "";
                    Iterator itTE = lstTypeElement.iterator();
                    while(itTE.hasNext()){
                        ElementStruct elem = (ElementStruct) itTE.next();
                        if ((idTypeElement!=null)&&(elem.getIdTypeElement().toString().equals(idTypeElement))){
                            strTE = "selected";
                        }else{
                            strTE = "";
                        }
%>
                        <option value="<%=elem.getIdTypeElement()%>" <%=strTE%>><%=elem.getTypeElement()%></option>
<%
                    }
                }
%>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Valor elemento</td>
                <td>
                    <input type="text" id="txtElementValue" name="txtElementValue" value="<%=txtElementValue%>">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('UPDELEM')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('UPDELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
        if (frmCNFELEM!=null){
            if ((lstConfigElem!=null)&&(!lstConfigElem.isEmpty())){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>CONFIGURACION DEL ELEMENTO</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>&nbsp;</td>
                <td>Tipo</td>
                <td>Tipo Configuracion</td>
                <td>Valor</td>
            </tr>
<%
                Iterator itCE = lstConfigElem.iterator();
                while (itCE.hasNext()){
                    Config conf = (Config) itCE.next();
%>
            <tr>
                <td>
<%
                    if ((idConfigElement!=null)&&(conf.getIdConfigElement().toString().equals(idConfigElement))){
%>
                    <input type="radio" id="idConfigElement" name="idConfigElement" value="<%=conf.getIdConfigElement()%>" checked="checked">
<%
                    }else{
%>
                    <input type="radio" id="idConfigElement" name="idConfigElement" value="<%=conf.getIdConfigElement()%>">
<%
                    }
%>
                </td>
                <td><%=conf.getTypeConfig()%></td>
                <td><%=conf.getTypeValue()%></td>
                <td><%=conf.getConfigValue() %></td>
            </tr>
<%
                }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDCNFELEM')">
                </td>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('frmUPDCNFELEM')">
                </td>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELCNFELEM')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }else{
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>CONFIGURACION DEL ELEMENTO</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>No existe configuracion para el elemento</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('frmADDCNFELEM')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
        if (frmADDCNFELEM!=null){
            if (lstTypeConfigElem!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>AGREGAR CONFIGURACION AL ELEMENTO</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Tipo Configuracion</td>
                <td>
                    <select id="idTypeConfigElem" name="idTypeConfigElem" onchange="selectOper('frmADDCNFELEMCH')">
                        <option>Seleccione</option>
<%
                String strTCE = "";
                Iterator itTCE = lstTypeConfigElem.iterator();
                while (itTCE.hasNext()){
                    ElementStruct elem = (ElementStruct) itTCE.next();
                    if ((idTypeConfigElem!=null)&&(elem.getIdTypeConfig().toString().equals(idTypeConfigElem))){
                        strTCE = "selected";
                    }else{
                        strTCE = "";
                    }
%>
                        <option value="<%=elem.getIdTypeConfig()%>" <%=strTCE%>><%=elem.getTypeConfig()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            if (lstTypeValueElem!=null){
%>
            <tr>
                <td>Tipo valor</td>
                <td>
                    <select id="idTypeValueElem" name="idTypeValueElem">
                        <option>Seleccione</option>
<%
                String strTCVE = "";
                if (lstTypeValueElem.size()==1){
                    strTCVE = "selected";
                }
                Iterator itTCVE = lstTypeValueElem.iterator();
                while (itTCVE.hasNext()){
                    ElementStruct elem = (ElementStruct) itTCVE.next();
%>
                        <option value="<%=elem.getIdTypeValue()%>" <%=strTCVE%>><%=elem.getTypeValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
            if (lstConfigValueElem!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <select id="idConfigValueElem" name="idConfigValueElem">
                        <option>Seleccione</option>
<%
                String strCVE = "";
                if (lstConfigValueElem.size()==1){
                    strCVE = "selected";
                }
                Iterator itCVE = lstConfigValueElem.iterator();
                while (itCVE.hasNext()){
                    ElementStruct elem = (ElementStruct) itCVE.next();
%>
                        <option value="<%=elem.getIdConfigValue()%>" <%=strCVE%>><%=elem.getConfigValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }else{
                if (lstMeasureElem!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <input type="" id="txtConfigValueElem" name="txtConfigValueElem">
                </td>
            </tr>
            <tr>
                <td>Medida</td>
                <td>
                    <select id="idMeasureElem" name="idMeasureElem">
                        <option>Seleccione</option>
<%
                    Iterator itME = lstMeasureElem.iterator();
                    while (itME.hasNext()){
                        ElementStruct elem = (ElementStruct) itME.next();
%>
                        <option value="<%=elem.getIdMeasure()%>"><%=elem.getMeasure()%></option>
<%
                    }
%>
                    </select>
                </td>
            </tr>
<%
                }
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('ADDCNFELEM')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('frmADDCNFELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
        if (frmUPDCNFELEM!=null){
            if (lstTypeConfigElem!=null){
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>MODIFICAR CONFIGURACION DEL ELEMENTO</td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Tipo Configuracion</td>
                <td>
                    <select id="idTypeConfigElem" name="idTypeConfigElem" onchange="selectOper('frmUPDCNFELEMCH')">
                        <option>Seleccione</option>
<%
                String strTCE = "";
                Iterator itTCE = lstTypeConfigElem.iterator();
                while (itTCE.hasNext()){
                    ElementStruct elem = (ElementStruct) itTCE.next();
                    if ((idTypeConfigElem!=null)&&(elem.getIdTypeConfig().toString().equals(idTypeConfigElem))){
                        strTCE = "selected";
                    }else{
                        strTCE = "";
                    }
%>
                        <option value="<%=elem.getIdTypeConfig()%>" <%=strTCE%>><%=elem.getTypeConfig()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            if (lstTypeValueElem!=null){
%>
            <tr>
                <td>Tipo valor</td>
                <td>
                    <select id="idTypeValueElem" name="idTypeValueElem">
                        <option>Seleccione</option>
<%
                String strTCVE = "";
                Iterator itTCVE = lstTypeValueElem.iterator();
                while (itTCVE.hasNext()){
                    ElementStruct elem = (ElementStruct) itTCVE.next();
                    if ((idTypeValueElem!=null)&&(elem.getIdTypeValue().toString().equals(idTypeValueElem))){
                        strTCVE = "selected";
                    }else{
                        if (lstTypeValueElem.size()==1){
                            strTCVE = "selected";
                        }else{
                            strTCVE = "";
                        }
                    }
%>
                        <option value="<%=elem.getIdTypeValue()%>" <%=strTCVE%>><%=elem.getTypeValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
            if (lstConfigValueElem!=null){
%>
            <tr>
                <td>Valor</td>
                <td>
                    <select id="idConfigValueElem" name="idConfigValueElem">
                        <option>Seleccione</option>
<%
                String strCVE = "";
                Iterator itCVE = lstConfigValueElem.iterator();
                while (itCVE.hasNext()){
                    ElementStruct elem = (ElementStruct) itCVE.next();
                    if ((idConfigValueElem!=null)&&(elem.getIdConfigValue().toString().equals(idConfigValueElem))){
                        strCVE = "selected";
                    }else{
                        if (lstConfigValueElem.size()==1){
                            strCVE = "selected";
                        }else{
                            strCVE = "";
                        }
                    }
%>
                        <option value="<%=elem.getIdConfigValue()%>" <%=strCVE%>><%=elem.getConfigValue()%></option>
<%
                }
%>
                    </select>
                </td>
            </tr>
<%
            }else{
                if (txtConfigValueElem==null){
                    txtConfigValueElem = "";
                }
%>
            <tr>
                <td>Valor</td>
                <td>
                    <input type="" id="txtConfigValueElem" name="txtConfigValueElem" value="<%=txtConfigValueElem%>">
                </td>
            </tr>
            <tr>
                <td>Medida</td>
                <td>
                    <select id="idMeasureElem" name="idMeasureElem">
                        <option>Seleccione</option>
<%
                String strME = "";
                if (lstMeasureElem!=null){
                    Iterator itME = lstMeasureElem.iterator();
                    while (itME.hasNext()){
                        ElementStruct elem = (ElementStruct) itME.next();
                        if ((idMeasureElem!=null)&&(elem.getIdMeasure().toString().equals(idMeasureElem))){
                            strME = "selected";
                        }else{
                            strME = "";
                        }
%>
                        <option value="<%=elem.getIdMeasure()%>" <%=strME%>><%=elem.getMeasure()%></option>
<%
                    }
                }
%>
                    </select>
                </td>
            </tr>
<%
            }
%>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="GUARDAR" onclick="selectOper('UPDCNFELEM')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('frmUPDCNFELEMX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
            }
        }
    }else if (seeBUTTON=="SEEQUERY"){
        //configQUERY: EXISTE QUERY
        if (configQUERY!=null){
            //botones: MODIFICAR-ELIMINAR-CANCELAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>QUERY ASOCIADA</td>
            </tr>
            <tr>
                <td>
                    <textarea cols="50" rows="5" id="txtQuery" name="txtQuery"><%=configQUERY.getQuery()%></textarea>
                <td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Es Externa</td>
                <td>
<%
            String strSI = "";
            String strNO = "";
            if (configQUERY.getIsExtern()==1){
                strSI = "checked=\"chequed\"";
            }else{
                strNO = "checked=\"chequed\"";
            }
%>
                    <input type="radio" id="isExtern" name="isExtern" value="1" <%=strSI%>>SI
                </td>
                <td>
                    <input type="radio" id="isExtern" name="isExtern" value="0" <%=strNO%>>NO
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="MODIFICAR" onclick="selectOper('UPDQUERY')">
                </td>
                <td>
                    <input type="button" value="ELIMINAR" onclick="selectOper('DELQUERY')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('SEEQUERYX')">
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="hidden" id="idQueryReport" name="idQueryReport" value="<%=configQUERY.getIdQuery()%>">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        //configQUERY: NO EXISTE QUERY
        }else{
            //botones: AGREGAR-CANCELAR
%>
<TR>
    <TD>
        <table>
            <tr>
                <td>No existe query asociada</td>
            </tr>
            <tr>
                <td>
                    <textarea cols="50" rows="5" id="txtQuery" name="txtQuery"></textarea>
                <td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>Es Externa</td>
                <td>
                    <input type="radio" id="isExtern" name="isExtern" value="1">SI
                </td>
                <td>
                    <input type="radio" id="isExtern" name="isExtern" value="0">NO
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD>
        <table>
            <tr>
                <td>
                    <input type="button" value="AGREGAR" onclick="selectOper('ADDQUERY')">
                </td>
                <td>
                    <input type="button" value="CANCELAR" onclick="selectOper('SEEQUERYX')">
                </td>
            </tr>
        </table>
    </TD>
</TR>
<%
        }
    }
%>
<TR>
    <TD>
        <input type="hidden" id="oper" name="oper" value="">
    </TD>
</TR>
</table>
</form>
</body>
</html>
