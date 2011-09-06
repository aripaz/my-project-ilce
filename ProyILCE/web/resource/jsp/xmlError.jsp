<%response.setContentType("text/html");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("pragma","no-cache");
StringBuffer xmlSession = (StringBuffer) request.getSession().getAttribute("xmlError");
if (xmlSession==null){
    StringBuffer str = new StringBuffer();
    str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
    str.append("<error>\n");
    str.append("<row>");
    str.append("ERROR:");
    str.append("</row>").append("\n");
    str.append("</error>");
    xmlSession=str;
}
out.append(xmlSession.toString());%>