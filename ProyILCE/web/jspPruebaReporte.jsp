<%-- 
    Document   : jspPruebaReporte
    Created on : 12/12/2011, 02:19:47 PM
    Author     : Administrador
--%>

<%@page import="mx.ilce.report.Report"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="mx.ilce.report.AdmReport"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
<script type="text/javascript">
    function actualizarForm(parametro){
        if (parametro != ""){
            document.A1.action = parametro;
            document.A1.submit();
        }
    }
</script>
<%
    AdmReport adm = new AdmReport();
    List lst = adm.getListReport();
%>
    <body>
<form action="" method="post" name="A1">
Oper<input type="text" id="oper" name="oper" value="genReport"/><br>
$1<input type="text" id="$1" name="$1" value="7"/><br>
$rep<select id="$rep" name="$rep">
        <option value="0">Seleccione..</option>
<%
    if (lst!=null){
        Iterator it = lst.iterator();
        while (it.hasNext()){
            Report rpt = (Report) it.next();
%>
        <option value="<%=rpt.getIdReport() %>"><%=rpt.getReport()%></option>
<%
        }
    }
%>
    </select>
    <br>
        <a href="javascript:actualizarForm('SrvReport');">cargar SrvReport</a>
</form>
    </body>
</html>
