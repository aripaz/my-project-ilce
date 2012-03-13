<%@ page import="mx.ilce.bean.User" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>Sistema de Administración de Proyectos / SIAP2.0</title>

        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.16.custom.min.js"></script>

        <!-- jqGrid -->
        <script type="text/javascript" src="jQuery/js/grid.locale-es.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript" src="jQuery/js/grid.subgrid.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.jstree.js"></script>

        <!-- splitter -->
        <script type="text/javascript" src="jQuery/js/splitter.js" ></script>

        <!-- Cookie for splitter -->
        <script type="text/javascript"  src="jQuery/js/jquery.cookie.js" ></script>

        <!--Datetime picker -->
        <script src="jQuery/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>

        <!-- Calculator -->
        <script type="text/javascript" src="jQuery/js/jquery.calculator.min.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.calculator-es.js" ></script>

        <!-- Menu -->
        <script src="jQuery/js/jquery.ui.menu.js" type="text/javascript"></script>

        <!-- Carrousel -->
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js"></script>

        <!-- Tooltip -->
        <script src="jQuery/js/jquery.tooltip.min.js" type="text/javascript"></script>
        
        <!-- Gráficas -->
        <script language="javascript" type="text/javascript" src="jQuery/jqPlot/jquery.jqplot.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
        <link rel="stylesheet" type="text/css" hrf="jQuery/jqPlot/jquery.jqplot.min.css" />
        
        <!-- form plugin para considerar uploads  -->
        <script type="text/javascript" src="jQuery/js/jquery.form.js"></script>

        <link type="text/css" href="css/agile_carousel.css" rel="stylesheet"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.tooltip.css"/>
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
        <a name="top"></a>
        <div id="banner">
            <div style="float: left; position: relative; "><img src="img/logo ilce.jpg" />
            <img src="img/logo sicap1.png" class="logo_plataforma"/></div>
            <div id="session_menu" style="float: right; position: relative; "></div>
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
                </ul>
                <div style="padding-right: 5px; float: right; padding-top: 3px; position: relative; top: -30px; width:300px"><!--<input id='txtBusquedaGlobal' type='text' style="clear:none; float:left"/>-->
                    <div id="switcher"  style="float:right"></div></div>
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
                                    <div id='ayudaComoAgregarAFavoritos'>
                                        <table >
                                            <tr>
                                                <td colspan='2'>
                                                    <h2>Agrega a tus favoritos los catálogos que utilizas más frecuentemente</h2>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <img src='img/favoritos.png'  class='helpScreen'/>
                                                </td>
                                                <td class='instrucciones'>
                                                    <p class='instrucciones'>
                                                        En este espacio puedes agregar tus catálogos favoritos, para agregar un favorito haz lo siguiente:</p>
                                                    <ol>
                                                        <li class='instrucciones'>Ve a la pestaña "Aplicaciones"</li>
                                                        <li class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir</li>
                                                        <li class='instrucciones'>Haz clic en el botón <span class='ui-icon ui-icon-star' style='display:inline-block'></span> de la barra de herramientas del catálogo que deseas hacer tu favorito</li>
                                                    </ol>        
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div id='ayudaComoEliminarFavoritos'>
                                        <table >
                                            <tr>
                                                <td colspan='2'>
                                                    <h2>Elimina los favoritos que ya no utilizas</h2>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <img src='img/cerrar_favorito.png' class='helpScreen'/>
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
                </div>
                <div class="column ui-sortable">
                    <div class="portlet">
                        <div class="portlet-header">Avisos</div>
                        <div class="portlet-content">
                            <p>Bienvenid@ SIAP2.0, esperamos que esta herramienta sea de su utilidad</p>
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
                <div id="tabMisApps">
                    <ul>
                        <li><a href='#tabMisAplicaciones'>Cómo usar mis aplicaciones</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>
                    </ul>
                    <div id='tabMisAplicaciones'>
                        <div id='divCarouselMisAplicaciones'>
                            <div id='ayudaComoUsarMisAplicaciones'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Comienza a usar tus aplicaciones</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_usar_aplicaciones.png'  class='helpScreen'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                En este espacio puedes abrir tus aplicaciones, para comenzar haz lo siguiente:</p>
                                            <ol>
                                                <li class='instrucciones'>Ve a la pestaña "Aplicaciones"</li>
                                                <li class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir; inmediatamente después se abrirá una pestaña con el nombre de la aplicación seleccionada con el catálogo principal</li>
                                            </ol>        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoAgregarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Agrega un registro nuevo al catálogo</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_agregar_registros.png'  class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para agregar un registro sigue los siguientes pasos:</p>
                                            <ol>
                                                <li>Haz clic encima del botón <span class='ui-icon ui-icon-plus' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página desplegará una ventana solicitando la información que se requiere para agregar el nuevo registro. Los campos marcados con (*) son obligatorios.
                                                </li>
                                                <li>Ingresa los datos conforme se solicitan</li>
                                                <li>Presiona el botón "Guardar"; esto cerrara la ventana e incorporará el nuevo registro al catálogo. </li>
                                            </ol>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoEditarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Edita un registro del catálogo</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_editar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para editar un registro sigue los siguientes pasos:</p>
                                            <ol>
                                                <li>Selecciona del catálogo el registro que deseas editar.</li>    
                                                <li>Haz clic encima del botón <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página desplegará una ventana con la información del registro seleccionado para que lo edites. 
                                                </li>
                                                <li>Edita los campos necesarios,  aquellos marcados con (*) son obligatorios.</li>
                                                <li>Presiona el botón "Guardar"; esto cerrara la ventana y actualizará el registro</li>
                                            </ol>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoEliminarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Elimina un registro del catálogo</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_eliminar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para eliminar un registro sigue los siguientes pasos:</p>
                                            <ol>
                                                <li>Selecciona del catálogo el registro que deseas eliminar.</li>    
                                                <li>Haz clic encima del botón <span class='ui-icon ui-icon-trash' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página solicitará que confirmes la eliminación. 
                                                </li>
                                                <li>Presiona OK en el dialogo de confirmación para proceder con el borrado</li>
                                            </ol>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoFiltrarRegistros'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Filtra los registros del catálogo</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_filtrar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Filtra para encontrar el conjunto de registros que cumplen con tus criterios de filtrado. Para filtrar registros sigue los siguientes pasos:</p>
                                            <ol> 
                                                <li>Haz clic encima del botón <span class='ui-icon ui-icon-search' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página desplegará una ventana para ingresar los criterios de filtrado 
                                                </li>
                                                <li>Ingresa al menos un criterio de filtrado; puedes seleccionar más de uno. Mientras más criterios utilices más reducida será el resultado de la búsqueda.</li>
                                                <li>Si deseas guardar el filtro para su posterior uso, asignale un nombre en el campo <a href="javascript:void(0);" class="tooltipLink" id="img/guardar_filtro_como.png">Guardar filtro como</a>:"; recuperalo posteriormente en el menú <a href="javascript:void(0);" class="tooltipLink" id="img/mis_filtros.png">"Mis filtros"</a> que se encuentra a la izquierda del catálogo. </li>
                                                <li>Presiona el botón "Buscar"; esto cerrará la ventana y mostrará el resultado en el catálogo. Para remover el filtro y restaurar los registros haz clic encima de la liga (Quitar filtro) que se encuentra junto al titulo del catálogo." </li>
                                            </ol>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoAgregarCatalogoAFavoritos'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h2>Agrega a tus favoritos los catálogos que utilizas más frecuentemente</h2>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/favoritos.png'  class='helpScreen'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Puedes agregar a la pestaña de inicio los catálogos que utilizas más frecuentemente en "Mis favoritos", para agregar un favorito haz lo siguiente:</p>
                                            <ol>
                                                <li class='instrucciones'>Ve a la pestaña "Aplicaciones"</li>
                                                <li class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir</li>
                                                <li class='instrucciones'>Haz clic en el botón <span class='ui-icon ui-icon-star' style='display:inline-block'></span> de la barra de herramientas del catálogo que deseas hacer tu favorito</li>
                                            </ol>        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>       
                </div>
            </div>    
            <div id="tabMapaDelSitio">
                <div id="divCarouselMapa">
                    <div id="mapa">
                    <table>
                        <tr>
                            <td>
                                <h2 class="instrucciones">Mapa del sitio</h2>
                            </td>
                        </tr>
                        <tr>
                            <td class="instrucciones">
                                <p class="instrucciones">Aquí se muestran todas y cada una de las partes que componen al sistema para el perfil actual, selecciona la liga correspondiente para acceder al componente deseado.</p>
                                <dl>
                                    <dt class="instrucciones" ><a id="mapLink-tabInicio" class="maplink" href="#">Inicio</a>
                                        <dl class="instrucciones">
                                            <dt class="instrucciones"><a id="mapLink-tabInicio-tabUser-tabPendientes" class="maplink" href="#">Pendientes</a></dt>
                                            <dt><a id="mapLink-tabInicio-tabUser-tabFavoritos" class="maplink" href="#">Favoritos</a>
                                                <dl id="tabMisFavoritos_in_map">

                                                </dl>
                                            </dt>
                                        </dl>
                                    </dt>
                                    <dt class="instrucciones"><a id="mapLink-tabAplicaciones" class="maplink" href="#">Aplicaciones</a>
                                        <dl id="app_menu_in_map">
                                        </dl>
                                    </dt>
                                    <dt class="instrucciones"><a id="mapLink-tabMapaDelSitio" class="maplink" href="#">Mapa de sitio</a></dt>
                                    <dt class="instrucciones"><a id="mapLink-tabAyuda" class="maplink" href="#">Ayuda</a></dt>
                                    <dt class="instrucciones"><a id="mapLink-tabContacto" class="maplink" href="#">Contacto</a></dt>
                                    <dt class="instrucciones">Búsqueda <input type="text" size="100" id="b_in_map" value="" /></dt>
                                </dl>
                            </td>
                        </tr>
                    </table>
                    </div>
                </div>
            </div>
            <div id="tabAyuda">
                <div id="divCarouselAyuda">
                    <div id="ayudaIndice">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Indice de temas de ayuda</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <div style="text-align: center;">
                                        <table style="margin: 0 auto;text-align: left;">
                                            <tr>
                                                <td style="vertical-align:top;  text-align: left; width:450px">
                                                    <p class="instrucciones">
                                                        <ol> 
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-2">Vistazo general al sistema</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-3">Inicio</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-4">Aplicaciones</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-14">Mapa del sitio</a></li>
                                                        </ol>            
                                                    </p> 
                                                </td>
                                                <td>
                                                    <img src='img/portada_ayuda.png' style='border: 1px'  class='helpScreen'></img>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaVistazo">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Vistazo general al sistema</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        La plataforma cuenta con 5 partes principales: <br />
                                        <ul>
                                            <li>Inicio: en donde se encuentran las pestañas "Pendientes" y "Favoritos".
                                                <ul>
                                                    <li>En "Pendientes" puedes ver y atender tus actividades</li>
                                                    <li>En "Favoritos" puedes tener los catálogos que uses con mayor frecuencia a la mano</li>
                                                </ul>
                                            </li>
                                            <li>Aplicaciones: aquí encontrarás los módulos que conforman la plataforma. Cada aplicación cuenta con un catálogo principal y del cual se desprenden otros catálogos relacionados en forma de kardex.</li>
                                            <li>Mapa del sitio: es el listado con las ligas hacia todos las partes de la plataforma.</li>
                                            <li>Ayuda: contiene el índice del manual en línea.</li>
                                            <li>Contacto: aquí se presenta la información para contactar al administrador de la plataforma.</li>
                                        </ul>
                                    </p>
                                    <p class="instrucciones">
                                        Por otra parte, los catálogos se presentan en una cuadrícula y dependiendo de los permisos de tu perfil se mostrarán botones para insertar <span class='ui-icon ui-icon-plus' style='display:inline-block'></span> , editar <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>, eliminar<span class='ui-icon ui-icon-trash' style='display:inline-block'></span>, hacer búsquedas de registros<span class='ui-icon ui-icon-search' style='display:inline-block'></span>, asi como abrir el kardex<span class='ui-icon ui-icon-newwin' style='display:inline-block'></span> con los catálogos relacionados al registro seleccionado.
                                    </p>
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaInicio">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Inicio</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        Dentro de la pestaña "Inicio"  hay otras dos pestañas: "Pendientes" y "Favoritos". <br />
                                        En "Pendientes" puedes ver las actividades por realizar así como la liga para ingresar los datos propios del pendiente; cada perfil tiene sus propios pendientes, y son configurados por el administrador del sistema. <br /><br/>
                                        En "Favoritos" podrás acceder a catálogos que marcaste como favoritos, evitando navegar en las aplicaciones.
                                    </p>
                                    <p class="instrucciones">
                                        <strong>Temas relacionados</strong>
                                        <ul>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-4">Agrega a tus favoritos los catálogos que más utilizas </a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-5">Elimina los favoritos que ya no utilizas</a></li>
                                        </ul>
                                    </p> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaAplicacion">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Aplicaciones</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        <ul>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-7">Comienza a usar tus aplicaciones</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-8">Agrega un registro nuevo al catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-9">Edita un registro del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-10">Elimina un registro del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-11">Filtra los registros del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-12">Agrega a tus favoritos los catálogos que utilizas más frecuentemente</a></li>
                                        </ul>
                                    </p> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaMapa">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Mapa del sitio</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        Aquí se muestran todas y cada una de las partes que componen al sistema para el perfil actual, selecciona la liga correspondiente para acceder al componente deseado.
                                    </p> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div id="tabContacto">
                <div>
                    <table>
                        <tr>
                            <td>
                                <h2>Contacto</h2>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p class="instrucciones">Envíanos tus sugerencias, comentarios, solicitudes y reportes de errores a nuestro correo electrónico.</p>
                                <h3>Administrador del sistema</h3>
                                <p class="instrucciones"><span class='ui-icon ui-icon-mail-closed' style='display:inline-block'></span>                                    
                                    <a href="mailto:plataforma@ilce.edu.mx">plataforma@ilce.edu.mx</a>
                                </p>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="tabBusqueda">
            </div>                 
    </div>
    <input type="hidden" name="_ce_" id="_ce_" value="<%=user.getClaveEmpleado()%>" />
    <input type="hidden" name="_cp_" id="_cp_" value="<%=user.getClavePerfil()%>" />
    <input type="hidden" name="_cp_" id="_ca_" value="<%=user.getClaveArea()%>" />
    <input type="hidden" name="_gq_" id="_gq_" value="" />
    <input type="hidden" name="_ts_" id="_ts_" value="" />
    <div id="divwait" title="Espere un momento, por favor"><br /><p style="text-align: center"><img src='img/throbber.gif' />&nbsp;Cargando preferencias del usuario</p></div>
</body>
</html>