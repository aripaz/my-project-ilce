/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "/ProyILCE/resource/jsp/xmlMenu.jsp", //"/ProyILCE/xml_tests/widget.accordion.xml",
            perfil : 1,
            menu: [/*{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}*/]
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>")
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
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                    });

                $( "#tabs span.ui-icon-close" ).live( "click", function() {
			var index = $( "li", $tabs ).index( $( this ).parent() );
			$tabs .tabs( "remove", index );
                });
                
                //Hace el binding de las ligas con sus eventos
                //seleccionando todas las ligas

                for (i=0;i<$.fn.appmenu.options.menu.length;i++) {
                
                    for (var k=0;k<$.fn.appmenu.options.menu[i].elementos_menu.length;k++) {
                        var nEntidad=$.fn.appmenu.options.menu[i].elementos_menu[k].entidad;
                        var nAplicacion=$.fn.appmenu.options.menu[i].elementos_menu[k].aplicacion;
                        if ($.fn.appmenu.options.menu[i].elementos_menu[k].funcion=="insertar") {
                            link_id="#newEntity_" + nAplicacion + "_" + nEntidad;}
                        else {
                            link_id="#showEntity_" + nAplicacion + "_" + nEntidad;}

                        $(link_id).click(function() {
                            //Verifica si existe
                            var nAplicacion=this.id.split("_")[1];
                            var nEntidad=this.id.split("_")[2];
                            var sTitulo=this.childNodes[0].data;

                            if ($("#tab"+this.id).length) {
                                //Selecciona el tab correspondiente
                                $tabs.tabs( "select", "#tab"+this.id);
                            }
                            else {

                                if (this.id.split("_")[0]=="newEntity") {
                                    $("body").form({aplicacion: nAplicacion,
                                                                       forma:nEntidad,
                                                                       modo:"insert",
                                                                       titulo: sTitulo,
                                                                       columnas:1,
                                                                       pk:0,
                                                                       height:400,
                                                                       width:500
                                                                   });
                                }
                                else {
                                    $tabs.tabs( "add", "#tab"+this.id, this.childNodes[0].data);
                                    $tabs.tabs( "select", "#tab"+this.id);
                                    oTabPanel=$("#tab"+this.id);
                                    var sLeyendaNuevoRegistro=$.fn.appmenu.options.menu[nAplicacion-1].elementos_menu[0].etiqueta;
                                    var sLeyendaEditaRegistro="Edita " + sLeyendaNuevoRegistro.split(" ")[1];
                                    $("#tab"+this.id).appgrid({app: nAplicacion,
                                                               entidad: nEntidad,
                                                               leyendas:[sLeyendaNuevoRegistro, sLeyendaEditaRegistro] });

                                }
                            }
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
            var nAplicacion=$(this).find("clave_aplicacion").text();
            var nEntidad=$(this).find("clave_forma").text();
            var sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            var sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            if ($(this).find("insertar").text()=="1") {
                aInsertar={etiqueta: sAliasNuevaEntidad,
                           entidad: nEntidad,
                           aplicacion:nAplicacion,
                           funcion:"insertar"};
            }

            if ($(this).find("mostrar").text()=="1") {
                aMostrar={etiqueta:sAliasMostrarEntidad,
                          entidad:nEntidad,
                          aplicacion:nAplicacion,
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
                    tipoliga="newEntity_" + $.fn.appmenu.options.menu[i].elementos_menu[k].aplicacion + "_";
                }
                else {
                    tipoliga="showEntity_" + $.fn.appmenu.options.menu[i].elementos_menu[k].aplicacion + "_";
                }

                sHtml+="<div><a href='#' id='" + tipoliga+$.fn.appmenu.options.menu[i].elementos_menu[k].entidad + "'>"+$.fn.appmenu.options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            sHtml+="</div>"
        }
        return sHtml;
    }
    
})(jQuery);