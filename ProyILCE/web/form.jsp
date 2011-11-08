<?xml version='1.0' encoding='ISO-8859-1'?><%@page contentType="text/xml" pageEncoding="UTF-8"
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.edu.ilce.modelo.*"
%><% 
response.setContentType("text/xml"); 
String error="";
int forma=0;;
String tipoAccion="";
String w = "";
String source="";
Usuario user=(Usuario)request.getSession().getAttribute("usuario");

if (user == null) {
   request.getRequestDispatcher("/index.jsp");
}

if (request.getParameter("$cf")==null)
    error="Falta parámetro $cf";
else
    forma= Integer.parseInt(request.getParameter("$cf"));

if (request.getParameter("$ta")==null)
    error="Falta parámetro $ta";
else  
    tipoAccion=request.getParameter("$ta");

int pk=0;
if (request.getParameter("$pk")!=null)
    pk = Integer.parseInt(request.getParameter("$pk"));
   
if (request.getParameter("$w")!=null)
    w=request.getParameter("$w");

if (error.equals("")) {
    user.setConsulta(forma, tipoAccion, pk, w);
    source=user.getConsulta().getSQL();
    error=user.getConsulta().getError();
}    
%>
<qry source="<%=source%>"><%
if (error!=null) { %>
<error><![CDATA[<%=error%>]]></error>
</qry>
<%    return;
}

ArrayList<Campo> campos = user.getConsulta().getCampos();

if(user.getConsulta().getError()!=null) { %>
<error><%=user.getConsulta().getError()%></error>
<%  return; } 

ArrayList<ArrayList> registros = user.getConsulta().getRegistros();

if(user.getConsulta().getError()!=null) { %>
<error><%=user.getConsulta().getError()%></error>
<%  return; } 
/* Definición de campos */
 int i=0;
 
 /* Datos */
 for (ArrayList registro : registros) {
    i=0;
    for (Object dato : registro) {
         out.println(campos.get(i).toXMLDatosDeEntidadConDiccionarioYForaneos(dato.toString()));
         i++;
    }
}
%></qry>