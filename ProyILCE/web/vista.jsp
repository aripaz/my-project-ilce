<%@ page import="mx.ilce.bean.User" %>
<%
User user = (User) request.getSession().getAttribute("user");
if (user == null){
 request.getRequestDispatcher("/index.jsp");
}
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Plataforma ILCE</title>

        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.7.custom.min.js"></script>

        <script type="text/javascript" src="jQuery/js/splitter.js" type="text/javascript"</script>

        <!-- Theme Switcher Widget -->
        <script type="text/javascript" src="http://jqueryui.com/themeroller/themeswitchertool/"></script>
        <!-- jqGrid -->
        <script src="jQuery/js/grid.locale-es.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.jqGrid.min.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.jstree.js"  type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui-1.8.7.custom.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        
        <script src="jQuery/js/funciones.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.foreign_grid.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.field_toolbar.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.session.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.form.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.accordion.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.tab.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.grid.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.treeMenu.js" type="text/javascript"></script>
        <script src="jQuery/js/vista.js" type="text/javascript"></script>

    </head>
    <body>
        <div id="header">
            <div id="banner">
                <img src="img/logo plataforma ilce 2.jpg" width="254" height="76" /></div>
            <div id="session_menu">
            </div>
        </div>
        <div id="splitterContainer">
            <div id="leftPane">
                <div id="apps_menu">

                </div>
            </div>
            <div id="rightPane">
                <div id="app_menu">
                </div>
                <div id="tabcontainer">
                    <div id="tabs">
                        <ul>
                            <li><a href="#tabUser">Escritorio</a></li>
                        </ul>
                        <div id="tabUser">
                            <p>Aquí van a ir breves reportes y alertas</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="_ce_" id="_ce_" value="<%=user.getClaveEmpleado()%>" />
        <input type="hidden" name="_cp_" id="_cp_" value="<%=user.getClavePerfil()%>" />
        <input type="hidden" name="_vk_" id="_vk_" value="" />
    </body>
</html>