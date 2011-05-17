/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.desktop = function(opc){

        $.fn.desktop.settings = {
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
        $.fn.desktop.options = $.extend($.fn.desktop.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.desktop.ajax(obj);
        });

    };

    $.fn.desktop.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.desktop.options.xmlUrl,
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
                obj.html($.fn.desktop.handleSession(xml));

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
                         $("#tabConfiguracion").appgrid({app: 1,
                                              entidad: 1,
                                              wsParameters:"clave_empleado=" + $.fn.desktop.options.empleado,
                                              titulo:"Par&aacute;metros de configuraci&oacute;n",
                                              leyendas:["Nuevo par&aacute;metro", "Edición de par&aacute;metro"]});;

                    }
                    
                });
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.desktop.handleSession = function(xml){
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de coniguración
            $.fn.desktop.options.empleado=$(this).find("clave_empleado").text();
            $.fn.desktop.options.nombre=$(this).find("nombre").text();
            $.fn.desktop.options.apellido_paterno=$(this).find("apellido_paterno").text();
            $.fn.desktop.options.apellido_materno=$(this).find("apellido_materno").text();
            $.fn.desktop.options.email=$(this).find("email").text();
            $.fn.desktop.options.perfil=$(this).find("clave_perfil").text();
            $.fn.desktop.options.foto=$(this).find("foto").text();
        })

        //Construye html de acuerdo a configuración recuperada
        if ($.fn.desktop.options.foto=="")
            $.fn.desktop.options.foto='img/sin_foto.jpg'
        var sHtml='<table border="0" cellspacing="0" cellpadding="0">'+
                  '<tr>' +
                  '<td valign="top">' +
                  '<table border="0" align="center" cellpadding="5" cellspacing="5">' +
                  '<tr>'+
                  '<td class="session_menu">'+
                  '<div align="right">'+
                  'Bienvenid@ ' + $.fn.desktop.options.nombre + ' ' + $.fn.desktop.options.apellido_paterno + '<br />' +
                  '<a class="sesion_menu" href="#" id="lnkConfiguracion">Configuraci&oacute;n</a><br />' +
                  '<a class="sesion_menu" href="#" id="lnkCerrarSesion">Cerrar sesi&oacute;n </a>'+
                  '</div></td>' +
                  '</tr>'+
                  '</table>'+
                  '</td>' +
                  '<td><img src="' + $.fn.desktop.options.foto + '" width="75" height="86" border="1" /></td>' +
                  '</tr>'+
                '</table>';
        return sHtml;
    }

})(jQuery);