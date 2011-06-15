/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "", //"/ProyILCE/xml_tests/widget.accordion.xml",
            usuario:"",
            menu: [/*{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}*/],
            ts:""

        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             if ($.fn.appmenu.options.xmlUrl!="") {
                 obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>"); $.fn.appmenu.options.ts="U2FsdGVkX1+K/UZ+8JLyZRxlM2+sjv0subeoJS4mtaQ=";
                 $.fn.appmenu.ajax(obj);
             }
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

                        $(link_id).click(function(e, data) {
                            //Verifica si existe
                            var nAplicacion=this.id.split("_")[1];
                            var nEntidad=this.id.split("_")[2];
                            var sTitulo=this.childNodes[0].data;

                            if ($("#tab"+this.id).length) {
                                //Selecciona el tab correspondiente
                                $tabs.tabs( "select", "#tab"+this.id);

                                //Recarga el grid por si tiene algún filtro
                                if (data==undefined){
                                    data="";
                                   $("#lnkRemoveFilter_grid_" + nAplicacion + "_" + nEntidad).remove();
                                }
                                else {
                                    //Si no existe el link para quitar filtro, lo establece
                                    if ($("#lnkRemoveFilter_grid_" + nAplicacion + "_" + nEntidad).length==0) {
                                        oGridHeader=$("span.ui-jqgrid-title, #grid_" +nAplicacion + "_" + nEntidad);
                                        nAplicacion=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                                        nForma=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                                        $(oGridHeader[0]).append("<a style='margin-left:10px' href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nEntidad +"'>(Quitar filtro)</a>");

                                        //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                        $("#lnkRemoveFilter_grid_" + nAplicacion + "_" + nEntidad).click(function() {
                                            var sGridId="#grid_" + this.id.split("_")[2] + "_" + + this.id.split("_")[3];
                                            $(sGridId).jqGrid('setGridParam',{url:"srvGrid?$cf=" + nEntidad + "&$dp=body"}).trigger("reloadGrid")
                                            $(this).remove();
                                        });
                                    }
                                }

                               $("#grid_"+nAplicacion+"_"+nEntidad).jqGrid('setGridParam',{url:"srvGrid?$cf=" + nEntidad + "&$dp=body&$w=" +data}).trigger("reloadGrid");
                               
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
                                    var sLeyendaNuevoRegistro=$("#newEntity_" + nAplicacion + "_" + nEntidad ).text();
                                    var sLeyendaEditaRegistro="Edita " + sLeyendaNuevoRegistro.split(" ")[1];

                                    $("#tab"+this.id).appgrid({app: nAplicacion,
                                                               entidad: nEntidad,
                                                               wsParameters:data,
                                                               titulo:sTitulo,
                                                               height:"70%",
                                                               leyendas:[sLeyendaNuevoRegistro, sLeyendaEditaRegistro],
                                                               openKardex:true
                                                           });
                                }
                            }
                        });

                    }

                 }

                //Mecanismo para forzar a que el DOM no se cargue del cache
                // ya que esto hace que se dupliquen los ids de los grid en cola
                if ($("#_ts_").val()!="") {
                    $.post("srvFormaSearch?$cf=1&$ta=select&$w=parametro='cache-pragma'", function(data) {
                         if (typeof data == "string") {
                             xmlCache = new ActiveXObject("Microsoft.XMLDOM");
                             xmlCache.async = false;
                             xmlCache.validateOnParse="true";
                             xmlCache.loadXML(data);
                             if (xmlCache.parseError.errorCode>0) {
                                    alert("Error de compilación xml:" + xmlCache.parseError.errorCode +"\nParse reason:" + xmlCache.parseError.reason + "\nLinea:" + xmlCache.parseError.line);}
                            }
                             else {
                                xmlCache = data;}

                            h=$(xmlCache).find("valor").text();
                            if (h!=undefined)
                                $("#_ts_").val(h);
                            else
                                $("#_ts_").val($.fn.appmenu.options.ts);
                    });
                 }
                 //Incluye los queries almacenados de cada aplicacion
                 $.fn.appmenu.getSearchs();
                 
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

   $.fn.appmenu.getSearchs = function() {
       $.ajax(
            {
            url: "srvFormaSearch?$cf=1&$ta=select&$w=" + escape("c.clave_empleado=" +$("#_ce_").val()+ " AND c.parametro like 'menu.busqueda.%'"),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlGs = new ActiveXObject("Microsoft.XMLDOM");
                 xmlGs.async = false;
                 xmlGs.validateOnParse="true";
                 xmlGs.loadXML(data);
                 if (xmlGs.parseError.errorCode>0)
                    alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}                
                 else 
                    xmlGs = data;
                
                var sBusquedas="<br>";
                var nAplicacion="";
                //Limpia los div appQris para evitar duplicidad al recargar
                $(".appMenu",$("#apps_menu")).each(function(){
                    $("#appQries_" +this.id.split("_")[1]).html("<br><span class='app_search_title'>Mis filtros<span>>><br />");
                })
                
                $(xmlGs).find("registro").each( function(){
                    nClave=$(this).find("clave_parametro")[0].firstChild.data;
                    sParametro=$(this).find("parametro")[0].firstChild.data
                    nForma=sParametro.split(".")[2];
                    sEtiqueta=sParametro.split(".")[3];
                    nAplicacion =$(this).find("clave_aplicacion")[0].firstChild.data;
                    sValor=escape($(this).find("valor")[0].firstChild.data);
                    sSuffix =nAplicacion + "_" + nForma + "_" + nClave;
                    sBusquedas="<div class='link_toolbar'>"+
                               "<a class='appMenu' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sValor+ "' forma='" + nForma + "' pk='" + nClave + "' >" + sEtiqueta + "</a>"+
                               "<div style='float:right'><div title='Eliminar filtro' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + nForma + "'></div></div>" +
                               "</div>";

                    $("#appQries_"+nAplicacion).append(sBusquedas);

                    //Oculta botones
                    $(".ui-icon-close", "#appQries_"+nAplicacion).hide();
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sValor=this.id.split("_")[4];
                        /*var newE = $.Event('click');
                        newE.gridFilter=$(this).attr("data");*/
                        data=$(this).attr("data");
                        $("#showEntity_" + nAplicacion + "_" + nForma).trigger("click",data);
                    });                    
                });

                //Hace bind con los divs padres del link en el evento hover
                $(".link_toolbar").hover(
                    function () {
                    //$(this).addClass('active_filter');
                    $(".closeLnkFiltro",this).show();
                    },
                    function () {
                    //$(this).removeClass('active_filter');
                    $(".closeLnkFiltro",this).hide();
                    }
                );
                
                //Hace bind con los botones de cerrar en el evento hover
                $(".closeLnkFiltro").hover(
                    function () {
                    $(this).parent().addClass('ui-state-default');
                    $(this).parent().addClass('ui-corner-all');
                    },
                    function () {
                    $(this).parent().removeClass('ui-state-default');
                    $(this).parent().removeClass('ui-corner-all');
                    }
                );

                //Hace bind del botón de búsqueda
                $(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;
                      $.post("srvFormaDelete","$cf=1&$pk=" + $(this).attr("pk"));
                      $(this).parent().remove();
                });


            }
       });
   }

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

            $.fn.appmenu.options.menu[i]={clave_aplicacion: nAplicacion,
                                          aplicacion:$(this).find("aplicacion").text(),
                                          elementos_menu:[aInsertar,aMostrar]};
            i++;
        })
        
        var sHtml=""

        //Construye menu de acuerdo a configuración recuperada
        for (i=0;i<$.fn.appmenu.options.menu.length;i++) {
            sHtml+="<h3><a href='#' class='appMenuTitle' >" + $.fn.appmenu.options.menu[i].aplicacion + "</a></h3>" +
                "<div id='mnuApp_" + $.fn.appmenu.options.menu[i].clave_aplicacion + "' >";
            for (var k=0;k<$.fn.appmenu.options.menu[i].elementos_menu.length;k++) {
                if ($.fn.appmenu.options.menu[i].elementos_menu[k].funcion=="insertar") {
                    tipoliga="newEntity_" + $.fn.appmenu.options.menu[i].elementos_menu[k].aplicacion + "_";
                }
                else {
                    tipoliga="showEntity_" + $.fn.appmenu.options.menu[i].elementos_menu[k].aplicacion + "_";
                }

                sHtml+="<div class='appMenu'><a href='#' id='" + tipoliga+$.fn.appmenu.options.menu[i].elementos_menu[k].entidad + "' class='appMenu'>"+$.fn.appmenu.options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            
            sHtml+="<div id='appQries_" + $.fn.appmenu.options.menu[i].clave_aplicacion + "'><br /></div></div>"
        }
        return sHtml;
    }
    
})(jQuery);