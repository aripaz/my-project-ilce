<%-- 
    Document   : jspPrueba
    Created on : 16/11/2011, 11:27:22 AM
    Author     : ccatrilef
--%>

<%@page import="mx.ilce.importDB.Archivo"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Pruebas</title>
    </head>
<script type="text/javascript">
    function actualizarFormArch(parametro){
        if (parametro != ""){
            document.A1.action = parametro;
            document.A1.submit();
        }
    }
</script>
    <body>
        <form action="" method="post" name="A1" enctype="multipart/form-data">
            Archivo: <input type="file" id="archivoBD" name="archivoBD">
            <br>
<%
    List lstArch = (List) request.getSession().getAttribute("lstArch");
%>
            Tipo: <select id="typeFile" name="typeFile">
                <option value="Seleccionar"></option>
<%
    if (lstArch!=null){
        Iterator it = lstArch.iterator();
        while (it.hasNext()){
            Archivo arch = (Archivo) it.next();
%>
                <option value="<%=arch.getIdArchivoCarga()%>"><%=arch.getArchivoCarga()%></option>
<%
        }
    }
%>
            </select>
            <br>
            Control Error: <select id="stopError" name="stopError">
                <option value="Seleccionar"></option>
                <option value="Y">Si</option>
                <option value="N">No</option>
            </select>
            <br>
            Display Error: <select id="display" name="display">
                <option value="Seleccionar"></option>
                <option value="XML">XML</option>
                <option value="N">Normal</option>
            </select>
            <br>
            <a href="javascript:actualizarFormArch('srvImportDB');">cargar srvImportBD</a>
            <br>
            <br>
<%
    StringBuffer xmlForma = (StringBuffer) request.getSession().getAttribute("xmlForma");
    if (xmlForma!=null){
%>
    <p><%=xmlForma %></p>
<%
    }
%>

        </form>
    </body>
</html>
