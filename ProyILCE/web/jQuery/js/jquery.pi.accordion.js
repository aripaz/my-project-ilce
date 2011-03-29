/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */


( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "xml_tests/widget.accordion.xml?perfil=",
            perfil : 1,
            menu: [{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}]
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             $.fn.appmenu.ajax();
             $(obj).accordion({
                active: false,
                autoHeight: false,
                collapsible: true,
                change: function() {
                  $(this).find('h3').blur();
                }

       });
        });

    };


    $.fn.appmenu.ajax = function(){
         $.ajax(
            {
            url: $.fn.appmenu.settings.xmlUrl,
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
                $.fn.appmenu.handleAccordion(xml);
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.appmenu.handleAccordion = function(xml){
        var i=0;
        $(xml).find("registro").each(function(){
            $.fn.appmenu.settings.menu[i].aplicacion=$(this).find("aplicacion").text();

            if ($(this).find("insertar").text()="1") {
                $.fn.appmenu.settings.menu[i].elementos_menu[$.fn.appmenu.settings.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_nueva_entidad").text(),entidad:$(this).find("alias_menu_nueva_entidad").text(),funcion:"insertar"}];
            }

            if ($(this).find("mostrar").text()="1") {
                $.fn.appmenu.settings.menu[i].elementos_menu[$.fn.appmenu.settings.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_mostrar_entidad").text(),funcion:"mostrar"}];
            }

            i++;
        })

        //Construye menu de acuerdo a configuración recuperada
        for (i=0;i<$.fn.appmenu.settings.accordion.menu.length;i++) {
            sHtml="<h3><a href='#' >" + $.fn.appmenu.settings.menu[i].aplicacion + "</a></h3>" +
                "<div>";
            for (var k=0;k<$.fn.appmenu.settings.accordion.menu[i].elementos_menu.length;k++) {
                if ($.fn.appmenu.settings.menu[i].elementos_menu[k].funcion=="insertar") {
                    tipoliga="nuevaEntidad";
                }
                else {
                    tipoliga="mostrarEntidad";
                }

                sHtml+="<div><a href='#' id='" + tipoliga+$.fn.appmenu.settings.menu[i].elementos_menu[k].entidad + "'>"+$.fn.appmenu.settings.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            sHtml+="</div>"
        }
    }

})(jQuery);