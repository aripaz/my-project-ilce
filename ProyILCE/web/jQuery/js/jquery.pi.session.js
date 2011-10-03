/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.sessionmenu = function(opc){

        $.fn.sessionmenu.settings = {
            xmlUrl : "/ProyILCE/resource/jsp/xmlSession.jsp",
            empleado:"",
            nombre:"",
            apellido_paterno:"",
            apellido_materno:"",
            email:"",
            perfil:"",
            foto:"",
            ultima_app:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.sessionmenu.options = $.extend($.fn.sessionmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.sessionmenu.ajax(obj);
             
        });

    };

    $.fn.sessionmenu.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.sessionmenu.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xml = new ActiveXObject("Microsoft.XMLDOM");
                 xml.async = false;
                 xml.validateOnParse="true";
                 xml.loadXML(data);
                 if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}
                }
                 else {
                    xml = data;}
                obj.html($.fn.sessionmenu.handleSession(xml));

                $("#lnkConfiguracion").click(function() {

                    //Crea el control del tab
                    var $tabs = $('#tabs').tabs({
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                    });

                    if ($("#tabConfiguracion").length) {
                         //Selecciona el tab correspondiente
                         $tabs.tabs( "select", "#tabConfiguracion");    }
                    else {
                         $tabs.tabs( "add", "#tabConfiguracion", "Configuraci&oacute;n");
                         $tabs.tabs( "select", "#tabConfiguracion");

                         $("#tabConfiguracion").appgrid({app: "1",
                                              entidad: "1",
                                              pk:"0",
                                              wsParameters:"e.clave_empleado=" + $.fn.sessionmenu.options.empleado,
                                              titulo:"Par&aacute;metros de configuraci&oacute;n",
                                              inQueue:false,
                                              height:"70%",
                                              showFilterLink:false,
                                              editingApp:"1",
                                              openKardex:false,
                                              leyendas:["Nuevo par&aacute;metro", "Edición de par&aacute;metro"]});
                    }
                    
                });
                
                //Inicializa el escritorio
                $("#tabUser").desktop();
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.sessionmenu.handleSession = function(xml){
        oRegistro=$(xml).find("registro");
            //Carga los datos del xml en la variable de coniguración
        $.fn.sessionmenu.options.empleado=oRegistro.find("clave_empleado").text();
        $.fn.sessionmenu.options.nombre=oRegistro.find("nombre").text();
        $.fn.sessionmenu.options.apellido_paterno=oRegistro.find("apellido_paterno").text();
        $.fn.sessionmenu.options.apellido_materno=oRegistro.find("apellido_materno").text();
        $.fn.sessionmenu.options.email=oRegistro.find("email").text();
        $.fn.sessionmenu.options.perfil=oRegistro.find("clave_perfil").text();
        $.fn.sessionmenu.options.foto=oRegistro.find("foto").text();


        //Construye html de acuerdo a configuración recuperada
        if ($.fn.sessionmenu.options.foto=="")
            $.fn.sessionmenu.options.foto='img/sin_foto.jpg'
        var sHtml='<table border="0" cellspacing="0" cellpadding="0">'+
                  '<tr>' +
                  '<td valign="top">' +
                  '<table border="0" align="center" cellpadding="5" cellspacing="5">' +
                  '<tr>'+
                  '<td class="session_menu">'+
                  '<div align="right"  class="ui-widget">'+
                  '<span id="_un_" class="ui-state-default session_menu"><strong>Bienvenid@ ' + $.fn.sessionmenu.options.nombre + ' ' + $.fn.sessionmenu.options.apellido_paterno + '</strong></span><br />' +
                  '<a class="ui-state-default session_menu" href="#" id="lnkConfiguracion">Configuraci&oacute;n</a><br />' +
                  '<a class="ui-state-default session_menu" href="srvLogout" id="lnkCerrarSesion">Cerrar sesi&oacute;n </a>'+
                  '</div></td>' +
                  '</tr>'+
                  '</table>'+
                  '</td>' +
                  '<td><img src="' + $.fn.sessionmenu.options.foto + '" width="75" height="86" border="1" /></td>' +
                  '</tr>'+
                '</table>';
        return sHtml;
    }

})(jQuery);