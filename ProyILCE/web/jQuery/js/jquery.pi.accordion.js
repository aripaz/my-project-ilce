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
                            link_id="#newEntity_" + nAplicacion + "_" + nEntidad; }
                        else {
                            link_id="#showEntity_" + nAplicacion + "_" + nEntidad;  }

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

                                //Si no existe crea nueva tab
                                $tabs.tabs( "add", "#tab"+this.id, this.childNodes[0].data);
                                $tabs.tabs( "select", "#tab"+this.id);
                                if (this.id.split("_")[0]=="newEntity") {
                                    $("#tab"+this.id).form({
                                                    aplicacion:nAplicacion,
                                                    forma:nEntidad,
                                                    pk:0,
                                                    modo:"insert",
                                                    titulo: sTitulo
                                     });
                                }
                                else {
                                    $tabs.tabs( "select", "#tab"+this.id);

                                    $("#tab"+this.id).append('<div id="gridContainer' + this.id + '"><table width="100%" id="grid' + this.id + '">' +
                                        '</table><div id="pager' + this.id +'"></div></div>');

                                    if (nEntidad==2) {
                                        oColNames=['Clave','Aplicaci&oacute;n', 'Estatus'];
                                        oColModel=[ {name:'clave_aplicacion',index:'clave_aplicacion', width:5},
                                                {name:'aplicacion',index:'aplicacion', width:15},
                                                {name:'estatus',index:'estatus', width:	80}  ]
                                        oSortName="clave_aplicacion";   }
                                    else if (nEntidad==7) {
                                        
                                    }

                                    $("#grid" +  this.id).jqGrid(
                                         {url:"xml_tests/grid.body.xml?entidad=" + this.id.split("_")[1],
                                          datatype: "xml",
                                          /* Parte dinámica */
                                          colNames:oColNames,
                                          colModel:oColModel,
                                          rowNum:20,
                                          autowidth: true,
                                          rowList:[10,20,30],
                                          pager: jQuery('#pager' + nEntidad),
                                          sortname:  oSortName,
                                          viewrecords: true,
                                          sortorder: "desc",
                                          caption:sTitulo,
                                          ondblClickRow: function(id){
                                              //Verifica si ya está abierto el tab en modo de edición
                                              if ($("#tabEditEntity"+id).length) {
                                                   $tabs.tabs( "select", "#tabEditEntity"+id);
                                              }
                                              else {
                                                    sTabTitulo=this.p.colNames[1] + ' ' + this.rows[id].cells[1].innerHTML;
                                                    sEntidad=this.id.split("_")[2];
                                                    $tabs.tabs( "add", "#tabEditEntity"+id, sTabTitulo);
                                                    $tabs.tabs( "select", "#tabEditEntity"+id);
                                                    $("#tabEditEntity"+id).apptab({
                                                        entidad:sEntidad,
                                                        pk:id,
                                                        app:nAplicacion
                                                    });

                                              }
                                            }
                                            });

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