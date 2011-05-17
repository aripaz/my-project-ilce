<%response.setContentType("text/html;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("pragma","no-cache");
String xmlSession = (String) request.getSession().getAttribute("xmlTab");
out.append(xmlSession);%>
