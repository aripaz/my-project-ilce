/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.sessionmenu = function(opc){

        $.fn.sessionmenu.settings = {
            xmlUrl : "xml_tests/widget.session.xml?perfil=",
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
             obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/wait30.gif' /></div>")
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
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.sessionmenu.handleSession = function(xml){
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de coniguración
            $.fn.sessionmenu.options.empleado=$(this).find("clave_empleado").text();
            $.fn.sessionmenu.options.nombre=$(this).find("nombre").text();
            $.fn.sessionmenu.options.apellido_paterno=$(this).find("apellido_paterno").text();
            $.fn.sessionmenu.options.apellido_materno=$(this).find("apellido_materno").text();
            $.fn.sessionmenu.options.email=$(this).find("email").text();
            $.fn.sessionmenu.options.perfil=$(this).find("clave_perfil").text();
            $.fn.sessionmenu.options.foto=$(this).find("foto").text();
        })

        //Construye html de acuerdo a configuración recuperada

        var sHtml='<table border="0" cellspacing="0" cellpadding="0">'+
                  '<tr>' +
                  '<td valign="top">' +
                  '<table border="0" align="center" cellpadding="5" cellspacing="5">' +
                  '<tr>'+
                  '<td class="session_menu">'+
                  '<div align="right">'+
                  'Bienvenid@ ' + $.fn.sessionmenu.options.nombre + ' ' + $.fn.sessionmenu.options.apellido_paterno + '<br />' +
                  '<a class="sesion_menu" href="#" id="lnkConfiguracion">Configuraci&oacute;n</a><br />' +
                  '<a class="sesion_menu" href="#" id="lnkCerrarSesion">Cerrar sesi&oacute;n </a>'+
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