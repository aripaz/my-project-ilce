<?xml version='1.0' encoding='ISO-8859-1'?><%@ page contentType="text/xml" %><%@ page import="java.util.ArrayList" %><%@ page import="mx.edu.ilce.modelo.*"
%><% 
String error="";
int forma=0;;
String tipoAccion="";
String dp="body";
String w = "";
String source="";
Integer pagina=1;
Integer registros=50;
String sidxName="";
String sidIndex="";
int sidxWidth=0;
boolean sidxSortable=true;
String orden="desc";

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

if (request.getParameter("$dp")!=null)
    dp=request.getParameter("$dp");

if (request.getParameter("page")!=null)
    pagina=Integer.parseInt(request.getParameter("page"));

if (request.getParameter("rows")!=null)
    registros= Integer.parseInt(request.getParameter("rows"));

if (request.getParameter("sidx[name]")!=null)
    sidxName= request.getParameter("sidx[name]");

if (request.getParameter("sidx[index]")!=null)
    sidIndex= request.getParameter("sidx[index]");

if (request.getParameter("sidx[width]")!=null)
    sidxWidth= Integer.parseInt(request.getParameter("sidx[width]"));

if (request.getParameter("sidx[sortable]")!=null)
    sidxSortable= request.getParameter("sidx[sortable]").equals("true")?true:false;

if (request.getParameter("sord")!=null)
    orden= request.getParameter("sord");
            
if (error.equals("")) {
    user.setConsulta(forma, tipoAccion, pk, w);
    source=user.getConsulta().getSQL();
    error=user.getConsulta().getError();
}     

%>
<qry source="<%=source%>"><%
if (error!=null) { %>
<error><![CDATA[<%=error%>]]></error></qry>
<%    return;
}

if(user.getConsulta().getError()!=null) { %>
<error><%=user.getConsulta().getError()%></error></qry><%

ArrayList<Campo> campos = user.getConsulta().getCampos();

if(user.getConsulta().getError()!=null) { %>
<error><%=user.getConsulta().getError()%></error></qry>
<%  return; } 

ArrayList<ArrayList> aRegistros = user.getConsulta().getRegistros();

if(user.getConsulta().getError()!=null) { %>
<error><%=user.getConsulta().getError()%></error></qry>
<%  return; } 
%><rows>
<page><%=pagina%></page>
<total><%= aRegistros.size() %></total>
<records><%=registros%></records>
<column_definition><%
 for (ArrayList registro : aRegistros) {
    int i=0;
    for (Object dato : registro) {
         out.println(campos.get(i).toXMLDatosDeEntidadConDiccionario(dato.toString()));
         i++;
    }
}
%></column_definition>
<permisos><%

boolean formaEncontrada=false;
for (int i=0; i< user.getAplicaciones().size(); i++) {
    for (int k=0; k< user.getAplicaciones().get(i).getFormas().size(); k++) {
        if (user.getAplicaciones().get(i).getFormas().get(k).getClaveForma()==forma) {

            if (user.getAplicaciones().get(i).getFormas().get(k).isSelect()) 
                out.println("<clave_permiso>1</clave_permiso>");
                
            if (user.getAplicaciones().get(i).getFormas().get(k).isInsert()) 
                out.println("<clave_permiso>2</clave_permiso>");

            if (user.getAplicaciones().get(i).getFormas().get(k).isUpdate()) 
                out.println("<clave_permiso>3</clave_permiso>");

            if (user.getAplicaciones().get(i).getFormas().get(k).isUpdate()) 
                out.println("<clave_permiso>4</clave_permiso>");

            if (user.getAplicaciones().get(i).getFormas().get(k).isSensitiveData()) 
                out.println("<clave_permiso>5</clave_permiso>");
            
            formaEncontrada=true;                                                        
            break;
        }   
        if (formaEncontrada)
            break;
    }
} %></permisos><%
int primerRegistro=registros*pagina;
int ultimoRegistro=(registros*pagina)+registros;
if (aRegistros.size()<ultimoRegistro)
    ultimoRegistro=aRegistros.size();

if (!dp.equals("header")) {
    for (int i=primerRegistro; i< ultimoRegistro; i++) {
        out.println("<rows>" );
        for (int k=0; k<aRegistros.get(i).size(); k++) 
            out.print("<cell><![CDATA["+aRegistros.get(i).get(k).toString()+"]]></cell>");
        out.println("</rows>" );
    }
} 
%>
</qry>
