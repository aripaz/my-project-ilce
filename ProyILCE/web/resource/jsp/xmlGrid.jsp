<%response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("pragma","no-cache");
StringBuffer xmlSession = (StringBuffer) request.getSession().getAttribute("xmlGrid");
out.append(xmlSession);%>
