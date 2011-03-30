/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "xml_tests/widget.accordion.xml?perfil=",
            perfil : 1,
            menu: [/*{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}*/]
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/wait30.gif' /></div>")
             $.fn.appmenu.ajax(obj);
        });

    };

    $.fn.appmenu.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.appmenu.options.xmlUrl,
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
                obj.html($.fn.appmenu.handleAccordion(xml));
                $(obj).accordion({
                active: false,
                autoHeight: false,
                collapsible: true,
                change: function() {
                  $(this).find('h3').blur();
                 }
                });

                //Crea el control tab aqui, puesto que desde este control se va a manipular
                var $tabs = $('#tabs').tabs({
                    add: function(event, ui) {
                    $tabs.tabs('select', ui.tab.hash);
                        }
                    });

                //Hace el binding de las ligas con sus eventos
                //seleccionando todas las ligas

                for (i=0;i<$.fn.appmenu.options.menu.length;i++) {
                
                    for (var k=0;k<$.fn.appmenu.options.menu[i].elementos_menu.length;k++) {
                        var nEntidad=$.fn.appmenu.options.menu[i].elementos_menu[k].entidad;
                        if ($.fn.appmenu.options.menu[i].elementos_menu[k].funcion=="insertar") {
                            liga="#nuevaEntidad" + nEntidad;}
                        else {
                            liga="#mostrarEntidad" + nEntidad;}

                        $(liga).click(function() {
                            //Verifica si existe
                            //Si no existe crea nueva tab
                            var tab_title = this.text || "tab " + nEntidad;
                                $tabs.tabs( "add", "#tab" + nEntidad, tab_title);
                        });
                    }
                 }
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.appmenu.handleAccordion = function(xml){
        var i=0;
        var aInsertar ={};
        var aMostrar={};

        $(xml).find("registro").each(function(){
            var nEntidad=$(this).find("clave_forma").text();
            var sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            var sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            if ($(this).find("insertar").text()=="1") {
                aInsertar={etiqueta: sAliasNuevaEntidad,
                           entidad: nEntidad,
                           funcion:"insertar"};
            }

            if ($(this).find("mostrar").text()=="1") {
                aMostrar={etiqueta:sAliasMostrarEntidad,
                           entidad:nEntidad,
                           funcion:"mostrar"};
            }

            $.fn.appmenu.options.menu[i]={aplicacion:$(this).find("aplicacion").text(), elementos_menu:[aInsertar,aMostrar]};
            i++;
        })
        
        var sHtml=""

        //Construye menu de acuerdo a configuración recuperada
        for (i=0;i<$.fn.appmenu.options.menu.length;i++) {
            sHtml+="<h3><a href='#' >" + $.fn.appmenu.options.menu[i].aplicacion + "</a></h3>" +
                "<div>";
            for (var k=0;k<$.fn.appmenu.options.menu[i].elementos_menu.length;k++) {
                if ($.fn.appmenu.options.menu[i].elementos_menu[k].funcion=="insertar") {
                    tipoliga="nuevaEntidad";
                }
                else {
                    tipoliga="mostrarEntidad";
                }

                sHtml+="<div><a href='#' id='" + tipoliga+$.fn.appmenu.options.menu[i].elementos_menu[k].entidad + "'>"+$.fn.appmenu.options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            sHtml+="</div>"
        }
        return sHtml;
    }

})(jQuery);