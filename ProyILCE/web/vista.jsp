<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Plataforma ILCE</title>
        <link rel="stylesheet" href="jQuery/development-bundle/themes/cupertino/jquery.ui.all.css" />
        <link rel="stylesheet" href="jQuery/development-bundle/demos/demos.css" />

        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jQuery/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.7.custom.min.js"></script>

        <script type="text/javascript" src="jQuery/js/splitter.js" type="text/javascript"</script>

        <!-- Theme Switcher Widget -->
        <script type="text/javascript" src="http://jqueryui.com/themeroller/themeswitchertool/"></script>
        <!-- jqGrid -->
        <script src="jQuery/jqGrid/js/i18n/grid.locale-es.js" type="text/javascript"></script>
        <script src="jQuery/jqGrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui-1.8.7.custom.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <script src="jQuery/js/jquery.pi.session.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.accordion.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.form.js" type="text/javascript"></script>
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
                <div id="search">
                    <div id="simple_search">
                        <input id="txtBusquedaSencilla" type="text" /> <input type="button" id="btnBusquedaSencilla" value="Buscar" /> <a href="#" id="lnkBusqueda">Búsqueda avanzada</a>
                    </div>
                    <div id="advanced_search">
                    </div>
                </div>
                <div id="divGrid">
                    <table width="100%" id="gridApp">
                    </table>
                    <div id="pager"></div>
                </div>
                <div id="tabcontainer">
                    <div id="tabs">
                        <ul>
                            <li><a href="#tab1">Datos destacados</a></li>
                        </ul>
                        <div id="tab1">
                            <p>Aquí van a ir breves reportes y alertas</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="dialog" title="Aplicación">
            <div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                <ul id="tablist">
                    <li id="tabs-1-tab"><a href="#tabs-1">#</a></li>
                </ul>
                <div id="tabs-1"></div>
            </div>
        </div>
    </body>
</html>
