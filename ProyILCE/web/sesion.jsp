<?xml version='1.0' encoding='ISO-8859-1'?><%@page contentType="text/xml" pageEncoding="UTF-8"
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.edu.ilce.modelo.*"
%><% 
response.setContentType("text/xml"); 
Usuario user=(Usuario)request.getSession().getAttribute("usuario");

if (user == null) {
   request.getRequestDispatcher("/index.jsp");
}
%>
<qry source="(sesion de usuario - datos)">
    <registro id="1">
        <clave_empleado tipo_dato="string"><![CDATA[<%= user.getClave() %>]]></clave_empleado>
        <nombre tipo_dato="string"><![CDATA[<%= user.getNombre() %>]]></nombre>
        <apellido_paterno tipo_dato="string"><![CDATA[ <%= user.getApellidoPaterno()%> ]]></apellido_paterno>
        <apellido_materno tipo_dato="string"><![CDATA[ <%= user.getApellidoMaterno()%>]]></apellido_materno>
        <email tipo_dato="string"><![CDATA[<%= user.getEmail()%>]]></email>
        <clave_perfil tipo_dato="integer"><![CDATA[<%= user.getClavePerfil()%>]]></clave_perfil>
        <foto tipo_dato="string"><![CDATA[<%=user.getFoto()%>]]></foto>
    </registro>
</qry>