<%@ page import="mx.ilce.bean.User" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
        <script src="jQuery/js/grid.subgrid.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.jstree.js"  type="text/javascript"></script>
        <!-- Calculator -->
        <script src="jQuery/js/jquery.calculator.min.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.calculator-es.js" type="text/javascript"></script>
        <!-- Menu -->
        <script src="jQuery/js/superfish.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui-1.8.7.custom.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/calculator/jquery.calculator.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/superfish.css"/>

        <script src="jQuery/js/funciones.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.desktop.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.gridqueue.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.field_toolbar.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.session.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.form.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.formqueue.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.accordion.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.tab.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.grid.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.treeMenu.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.menu.js" type="text/javascript"></script>
        <script src="jQuery/js/vista.js" type="text/javascript"></script>


    </head>
    <body>
        <div id="banner">
            <img src="img/logo ilce.jpg" />
            <img src="img/logo_plataforma.png" class="logo_plataforma"/>
        </div>
        <div id="nav_menu">
            <div id="sf-menu" >
                <ul id='app_menu' class='sf-menu'>
                </ul>
            </div>
            <div > </div>
        </div>

        <div id="tabcontainer">
            <div id="tabs">
                <ul>
                    <li><a href="#tabUser">Escritorio</a></li>
                </ul>
                <div id="tabUser">

                    <div class="column1 ui-sortable">
                        <div class="portlet">
                            <div class="portlet-header">Mis pendientes</div>
                            <div class="portlet-content">
                                <div id='notification_1_101_0' class='notification'></div>
                                <div id="grid_1_101_0" class="queued_grids" app="1" form="101" wsParameters="" titulo="Actividades pendientes" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" ></div>
                            </div>
                        </div>
                        <div class="portlet">
                            <div class="portlet-header">Pendientes que asigné</div>
                            <div class="portlet-content">
                                <div id='notification_1_103_0' class='notification'></div>
                                <div id="grid_1_102_0" class="queued_grids" app="1" form="102" wsParameters="" titulo="Actividades que yo asigné" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" /></div>
                        </div>
                    </div>
                    <div class="portlet">
                        <div class="portlet-header">Mis favoritos</div>
                        <div class="portlet-content">
                            <div id='notification_1_103_0' class='notification'>(No hay listados registrados)</div>
                        </div>
                    </div>
                </div>
                <div class="column2 ui-sortable">
                    <div class="portlet">
                        <div class="portlet-header">Avisos</div>
                        <div class="portlet-content">
                            <p>Bienvenid@ a la plataforma ILCE, esperamos que esta herramienta sea de su utilidad</p>
                        </div>
                    </div>
                    <div class="portlet">
                        <div class="portlet-header">Recursos</div>
                        <div class="portlet-content">
                            <p>En esta sección encontrarás recursos de propósito general
                            <ul>
                                <li>Base de conocimiento
                                    <ul>
                                        <li><a href="#" onclick="alert('Por implementar');">Vistazo general a la plataforma</a></li>
                                        <li><a href="#" onclick="alert('Por implementar');">Manual del módulo de promoción</a></li>
                                        <li><a href="#" onclick="alert('Por implementar');">Manual del módulo de proyectos</a></li>
                                    </ul>
                                </li>
                                <li>Foros</li>
                                <ul>
                                    <li><a href="#" onclick="alert('Por implementar');">General</a></li>
                                    <li><a href="#" onclick="alert('Por implementar');">Módulo de promoción</a></li>
                                    <li><a href="#" onclick="alert('Por implementar');">Módulo de proyectos</a></li> 
                                </ul> 
                            </ul>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" name="_ce_" id="_ce_" value="<%=user.getClaveEmpleado()%>" />
    <input type="hidden" name="_cp_" id="_cp_" value="<%=user.getClavePerfil()%>" />
    <input type="hidden" name="_gq_" id="_gq_" value="" />
    <input type="hidden" name="_ts_" id="_ts_" value="" />
</body>
</html>