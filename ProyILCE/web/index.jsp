<%--
    Document   : index
    Created on : 24/03/2011, 11:06:02 AM
    Author     : Omar Flores
--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>ILCE</title>
    </head>
    <script type="text/javascript" >
    function goLogin(){
        document.main.action = '<%=request.getContextPath()%>/login.jsp';
        document.main.submit();
    }
    </script>

    <body bgcolor="#F4F9FF" onLoad="goLogin();">
        <form action="" method="post" name="main"></form>
    </body>
</html>
