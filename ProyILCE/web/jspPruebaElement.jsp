<%-- 
    Document   : jspPruebaElement
    Created on : 15/12/2011, 10:02:05 AM
    Author     : Administrador
--%>

<%@page import="mx.ilce.report.ElementStruct"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    List listElement = (List) request.getSession().getAttribute("listElement");
%>
<script type="text/javascript">
function actualizarForm(entrada){
    if (entrada){
        document.RP.action = entrada;
        document.RP.submit();
    }
}
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Obtención de Elementos</title>
    </head>
    <body>
        <form action="" method="post" name="RP" enctype="multipart/form-data">
        <table>
            <TR>
                <TD>Nombre</TD>
                <td>
                    <input type="text" name="nombreRPT"/>
                </td>
            </TR>
            <TR>
                <TD>Listado de elementos a seleccionar</TD>
            </TR>
<%
    if (listElement!=null){
        Iterator it = listElement.iterator();
        boolean writeTitle = true;
        String nameType = "";
        while (it.hasNext()){
            ElementStruct elem = (ElementStruct) it.next();
            if (writeTitle){
                nameType = elem.getTypeStructure().toUpperCase();
%>
            <TR><TD>
                <table>
                    <tr>
                        <td><%=nameType%></td>
                    </tr>

<%
                writeTitle=false;
            }else{
                if (!nameType.equals(elem.getTypeStructure().toUpperCase())){
                    nameType = elem.getTypeStructure().toUpperCase();
%>
                </table>
            </TD></TR>
            <TR><TD>
                <table>
                    <tr>
                        <td><%=nameType%></td>
                    </tr>
<%
                }
            }
%>
                    <tr>
                        <td>
                            <input type="checkbox" name="ID<%=elem.getIdStructure()%>" value="<%=elem.getIdStructure()%>"/>
                        </td>
                        <td><%= elem.getStructure() %></td>
                        <td></td>
                    </tr>
<%
        }
%>
                </table>
            </TD></TR>
<%
    }
%>
            <tr>
                <td><a href="javascript:actualizarForm('SrvElement');">cargar SrvReport</a></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" id="oper" name="oper" value="genReport">
                </td>
            </tr>
        </table>
        </form>
    </body>
</html>
