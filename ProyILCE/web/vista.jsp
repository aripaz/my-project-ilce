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
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.16.custom.min.js"></script>
             
        <!-- jqGrid -->
        <script type="text/javascript" src="jQuery/js/grid.locale-es.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript" src="jQuery/js/grid.subgrid.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.jstree.js"></script>
        
        <!-- splitter -->
        <script type="text/javascript" src="jQuery/js/splitter.js" type="text/javascript"</script>

        <!-- Cookie for splitter -->
        <script type="text/javascript"  src="jQuery/js/jquery.cookie.js" ></script>
        
        <!--Datetime picker -->
        <script src="jQuery/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
        
        <!-- Calculator -->
        <script type="text/javascript" src="jQuery/js/jquery.calculator.min.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.calculator-es.js" ></script>
        
        <!-- Menu -->
        <script src="jQuery/js/jquery.ui.menu.js" type="text/javascript"></script>
        
        <!-- form plugin para considerar uploads  -->
        <script type="text/javascript" src="jQuery/js/jquery.form.js"></script>
       
        <link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/calculator/jquery.calculator.css"/>
        
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
        <script src="jQuery/js/jquery.pi.appmenu.js" type="text/javascript"></script>
        <script src="jQuery/js/vista.js" type="text/javascript"></script>
        
        
    </head>
    <body>
        <script type="text/javascript"
        src="http://jqueryui.com/themeroller/themeswitchertool/">
        </script>
        <div id="banner">
            <img src="img/logo ilce.jpg" />
            <img src="img/logo_plataforma.png" class="logo_plataforma"/>
            <div id="session_menu" style="float:right"></div>
        </div>
        <!-- 
        <div class="menus_plataforma">
            <div id="app_menu" style="float:left">
                <div>
                    <a href='#' id='menu_inicio' >Inicio</a>
                    <a href='#' id='menu_aplicaciones' >Aplicaciones</a>
                    <a href='#' id='menu_splitter' >&nbsp;</a>
                    <a href='#' id='menu_mapa' >Mapa del sitio</a>
                    <a href='#' id='menu_ayuda' >Ayuda</a>
                    <a href='#' id='menu_contacto' >Contacto</a>
                </div>
                <ul id="apps">
                </ul>    
            </div>
        </div>
        -->
        <div id="tabcontainer">
            <div id="tabs">
                <ul>
                    <li><a href="#tabInicio">Inicio</a></li>
                    <li><a href="#tabAplicaciones">Aplicaciones</a>   
                    </li>
                    <li><a href="#tabMapaDelSitio">Mapa del sitio</a></li>
                    <li><a href="#tabAyuda">Ayuda</a></li>
                    <li><a href="#tabContacto">Contacto</a></li>
                    &nbsp;&nbsp;<input id='txtBusquedaGlobal' type='text' />
                    <div id="switcher" style="float:right; padding-right: 5px;  padding-top: 3px;"></div>
                </ul>
                <div id="tabInicio">
                    <div id="tabUser">    
                        <ul>
                            <li><a href="#tabPendientes">Pendientes</a></li>
                            <li><a href="#tabFavoritos">Favoritos</a></li>
                        </ul>
                        <div id="tabPendientes">
                            <div id="grid_1_101_0" class="queued_grids" app="1" form="101" wsParameters="" titulo="Mis pendientes" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" ></div>
                            <div class="blank_space">&nbsp;</div>
                            <div id="grid_1_102_0" class="queued_grids" app="1" form="102" wsParameters="" titulo="Pendientes que yo asigné" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" /></div>
                        </div>
                        <div id="tabFavoritos">
                            <div id='tabMisFavoritos'>
                                <ul>
                                    <li><a href='#tabMisFavoritos1'>Cómo usar mis favoritos</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>
                                </ul>
                                <div id='tabMisFavoritos1'>
                                    <div id='divCarouselMisFavoritos'>
                                    <table id='ayudaComoAgregarAFavoritos'>
                                        <tr>
                                            <td colspan='2'>
                                                <h3>Agrega a tus favoritos los catálogos que utilizas más frecuentemente</h3>
                                            </td>
                                        </tr>
                                        <tr>
                                        <td>
                                            <img src='img/favoritos.png'  border='1'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                En este espacio puedes agregar tus catálogos favoritos, para agregar un favorito haz lo siguiente:</p>
                                            <ol>
                                                <li class='instrucciones'>Ve a la pestaña Aplicación</li>
                                                <li class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir</li>
                                                <li class='instrucciones'>Haz clic en el botón <span class='ui-icon ui-icon-heart' style='display:inline-block'></span> de la barra de herramientas del catálogo que deseas hacer tu favorito</li>
                                            </ol>        
                                        </td>
                                        </tr>
                                    </table>
                                    <hr />
                                       <table id='ayudaComoEliminarFavoritos'>
                                        <tr>
                                            <td colspan='2'>
                                                <h3>Elimina los favoritos que ya no utilizas</h3>
                                            </td>
                                        </tr>
                                        <tr>
                                        <td>
                                            <img src='img/cerrar_favorito.png'  border='1'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para eliminar un favorito solo haz clic encima del botón <span class='ui-icon ui-icon-circlesmall-close' style='display:inline-block'></span>de la pestaña que contiene al catálogo</p>
                                        </td>
                                        </tr>
                                    </table>

                                </div>
                                </div>
                            </div>                           
                        </div>                    
                    </div>
                    <div class="column ui-sortable">
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
                <div id="tabAplicaciones">
                    <div id="menu_apps"></div>
                    <div id="tabMisApps"><ul></ul></div>
                </div>
                <div id="tabMapaDelSitio">
                </div>
                <div id="tabAyuda">
                </div>
                <div id="tabContacto">
                </div>
                <div id="tabBusqueda">
                </div>                 
        </div>
    </div>
    <input type="hidden" name="_ce_" id="_ce_" value="<%=user.getClaveEmpleado()%>" />
    <input type="hidden" name="_cp_" id="_cp_" value="<%=user.getClavePerfil()%>" />
    <input type="hidden" name="_gq_" id="_gq_" value="" />
    <input type="hidden" name="_ts_" id="_ts_" value="" />
    <div id="divwait" title="Espere un momento, por favor"><br /><p style="text-align: center"><img src='img/throbber.gif' />&nbsp;Cargando preferencias del usuario</p></div>
</body>
</html>