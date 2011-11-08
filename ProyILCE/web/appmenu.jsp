<?xml version='1.0' encoding='ISO-8859-1'?><%@page contentType="text/xml" pageEncoding="UTF-8"
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.edu.ilce.modelo.*"
%><% 
response.setContentType("text/xml"); 
Usuario user=(Usuario)request.getSession().getAttribute("usuario");

if (user == null) { %>
    <sesion><error><!CDATA[Su sesión expiró]]</error></sesion>
<% return; }
%>
<qry source="(sesion de usuario - aplicaciones)">
<%
//Listado de aplicaciones del usuario
for (int i=0;i<user.getAplicaciones().size();i++) {
    out.println("<registro id='" + i + "'>");
    out.println("<clave_aplicacion ><![CDATA[" + user.getAplicaciones().get(i).getClaveAplicacion() +"]]></clave_aplicacion>");
    out.println("<aplicacion><![CDATA[" + user.getAplicaciones().get(i).getAplicacion() +"]]></aplicacion>");
    out.println("<alias_menu_nueva_entidad><![CDATA[" + user.getAplicaciones().get(i).getAliasMenuNuevaEntidad() +"]]></alias_menu_nueva_entidad>");
    out.println("<alias_menu_mostrar_entidad><![CDATA[" + user.getAplicaciones().get(i).getAliasMenuMuestraEntidad() +"]]></alias_menu_mostrar_entidad>");
    out.println("<clave_forma><![CDATA[" + user.getAplicaciones().get(i).getClaveFormaPrincipal() +"]]></clave_forma>");
    for (int j=0; j<user.getAplicaciones().get(i).getFormas().size();j++) {
        if (user.getAplicaciones().get(i).getClaveFormaPrincipal()==user.getAplicaciones().get(i).getFormas().get(j).getClaveForma()) {
            out.println("<forma><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).getForma() +"]]></forma>");
            out.println("<mostrar><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).isSelect() +"]]></mostrar>");
            out.println("<insertar><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).isInsert() +"]]></insertar>");
            out.println("<actualizar><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).isUpdate() +"]]></actualizar>");
            out.println("<eliminar><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).isDelete() +"]]></eliminar>");
            out.println("<mostrar_informacion_sensible><![CDATA[" + user.getAplicaciones().get(i).getFormas().get(j).isSensitiveData() +"]]></mostrar_informacion_sensible>");
            break;
      }
    }    

    out.print("</registro>");
}
    
%></qry>
