/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.desktop = function(opc){

        $.fn.desktop.settings = {
            xmlUrl : "srvFormaSearch?$cf=1&$ta=select&$w=" + escape("c.clave_empleado=" +$("#_ce_").val()+ " AND c.parametro like 'escritorio.%'")  //"srvControl?$cmd=form&$cf=1&$ta=select&$w=" + escape("c.clave_empleado=" +$("#_ce_").val()+ " AND c.parametro like 'escritorio.%'")
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.desktop.options = $.extend($.fn.desktop.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);

            $( ".column" ).sortable({
			connectWith: ".column"
            });
            
            $( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
			.find( ".portlet-header" )
				.addClass( "ui-widget-header ui-corner-all" )
				.prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
				.end()
			.find( ".portlet-content" );

            $( ".portlet-header .ui-icon" ).click(function() {
			$( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
			$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
            });

            $( ".column" ).disableSelection();
            
            $('#tabUser').tabs({
            tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
            });

            $('#tabMisFavoritos').tabs({
                tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"/*,
                select: function(event, ui) { 
                    gridId=$(ui.panel).children().children()[0].id.replace("gbox_","");
                    oGrid= $("#"+gridId);
                    oGrid.setGridWidth(oGrid.parent().width());
                    return true;
                }*/
            });
            
            $( "#tabMisFavoritos span.ui-icon-close" ).live( "click", function() {
                    var index = $( "li", $("#tabMisFavoritos")).index( $( this ).parent() );
                    var nPK=$("#tabMisFavoritos").children()[index+1].id.split("_")[1];
                    $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Eliminando catálogo favorito del usuario</p>")
                    .attr('title','Eliminando favorito...') 
                    .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                    });
                    
                    $.ajax(
                    {
                        url: "srvFormaDelete?$cf=1&$pk="+ nPK,
                        dataType: "text",
                        success:  function(data){
                            $("#tabMisFavoritos").tabs( "remove", index );
                            $("#divwait").dialog( "close" )
                            $("#divwait").dialog("destroy");                            
                        },
                        error:function(xhr,err){
                            alert("Error al eliminar registro");
                            $("#divwait").dialog( "close" )
                            $("#divwait").dialog("destroy");                            

                        }
                    });
            });
            
            //Selecciona temporalmente e tab favoritos para audar a calcular el tamaño del carrusel
            $('#tabUser').tabs( "select", "#tabFavoritos" );

            //Activa el carrousel de ayuda de favoritos
            $("#divCarouselMisFavoritos").agile_carousel({
                carousel_data: [{
                                "content": $("#ayudaComoAgregarAFavoritos").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoEliminarFavoritos").html(),
                                "content_button": ""
                                }],
                carousel_outer_height: $("#divCarouselMisFavoritos").height(),
                carousel_height: $("#divCarouselMisFavoritos").height(),
                slide_height: $("#divCarouselMisFavoritos").height()+2,
                carousel_outer_width: $("#divCarouselMisFavoritos").width(),
                slide_width: $("#divCarouselMisFavoritos").width(), 
                transition_time: 300,
                continuous_scrolling: false,
                control_set_1: "previous_button,next_button",
                control_set_2: "numbered_buttons"
            });
            
             $('#tabUser').tabs( "select", "#tabPendientes" );
             
             $("#tabs").tabs( "select", "#tabAplicaciones" );
             //Activa el carrousel de ayuda de favoritos
             $("#divCarouselMisAplicaciones").agile_carousel({
                carousel_data: [{
                                "content": $("#ayudaComoUsarMisAplicaciones").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoAgregarUnRegistro").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoEditarUnRegistro").html(),
                                "content_button": ""
                                },{
                                "content": $("#ayudaComoEliminarUnRegistro").html(),
                                "content_button": ""
                                },{
                                "content": $("#ayudaComoFiltrarRegistros").html(),
                                "content_button": ""
                                },{
                                "content": $("#ayudaComoAgregarCatalogoAFavoritos").html(),
                                "content_button": ""
                                }
                                
                            ],
                carousel_outer_height: $("#divCarouselMisAplicaciones").height(),
                carousel_height: $("#divCarouselMisAplicaciones").height(),
                slide_height: $("#divCarouselMisAplicaciones").height()+2,
                carousel_outer_width: $("#divCarouselMisAplicaciones").width(),
                slide_width: $("#divCarouselMisAplicaciones").width(), 
                transition_time: 300,
                continuous_scrolling: false,
                number_slides_visible: 1,
                control_set_1: "previous_button,next_button",
                control_set_2: "numbered_buttons"
            });   
            
            $("#tabs").tabs( "select", "#tabMapaDelSitio" );
            
            //Activa los tooltips de los links con clase tooltipLink
            $(".tooltipLink").tooltip({
                    bodyHandler: function() {
                            return $("<img/>").attr("src", this.id);
                    },
                    showURL: false
            });
            
             $("#tabs").tabs( "select", "#tabInicio" );
            //$("#tabMisFavoritos").tabs( "remove", 0);
            
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
                 xmlConfig = new ActiveXObject("Microsoft.XMLDOM");
                 xmlConfig.async = false;
                 xmlConfig.validateOnParse="true";
                 xmlConfig.loadXML(data);
                 if (xmlConfig.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xmlConfig.parseError.errorCode +"\nParse reason:" + xmlConfig.parseError.reason + "\nLinea:" + xmlConfig.parseError.line);}
                }
                 else {
                    xmlConfig= data;}

                $.fn.desktop.handleSession(xmlConfig);
                
                //Activa las ligas del mapa de sitio
                $('.maplink').click(function() {
                    aTabsSecuence=this.id.split("-");
                    for (i=1;i<aTabsSecuence.length;i++) {
                        if (i==1)
                            $("#tabs").tabs( "select", "#"+aTabsSecuence[i] );
                        else
                            $("#"+aTabsSecuence[i-1]).tabs( "select", "#"+aTabsSecuence[i] );
                    }
                });
                
                $('.queued_grids:first').gridqueue();
                
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.desktop.handleSession = function(xml){
        var sFondo="";
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de configuración
            sParametro=$(this).find("parametro").text().split("\n")[0];

            if (sParametro=='escritorio.imagen de fondo') {
               sFondo=$(this).find("valor").text().split("\n")[0];
               if (sFondo!='')
                   obj.css('background-image', 'url('+sFondo+')');
            }

            if(sParametro==='escritorio.grid') {
                
                nClave=$(this).find("clave_parametro").text().split("\n")[0]
                sValor=$(this).find("valor").text().split("\n")[0];
                nApp=sValor.split(",")[0].split(":")[1];
                nForm=sValor.split(",")[1].split(":")[1];
                wsParameters=sValor.split(",")[2].split(":")[1];
                titulo=sValor.split(",")[3].split(":")[1];
                leyendas=sValor.split(",")[4].split(":")[1].replace("/",",");
                openKardex=sValor.split(",")[5].split(":")[1];
                inDesktop=sValor.split(",")[6].split(":")[1];
                

                $('#tabMisFavoritos').tabs( "add", "#tabMisFavoritos_"+nClave, titulo);

                $("#tabMisFavoritos_"+nClave).append("<div class='queued_grids'" + 
                                     " id='divDesktopGrid_" + nApp + "_" + nForm + "' " +
                                     " app='" + nApp + "' " + 
                                     " form='" + nForm + "' " +
                                     " wsParameters='" + wsParameters + "' " +
                                     " titulo='" + titulo + "' " + 
                                     " leyendas='" +leyendas+ "' "  + 
                                     " openKardex='" + openKardex + "' " + 
                                     " inDesktop='" + inDesktop + "' " +
                                     " class='queued_grids' " +
                                     " editingApp='1' "+
                                     " insertInDesktopEnabled='0'></div>");
              
              //Agrega el favorito al mapa del sitio
              $("#tabMisFavoritos_in_map").append("<dt><a id='mapLink-tabInicio-tabUser-tabFavoritos-tabMisFavoritos-tabMisFavoritos_"+nClave+ "' class='maplink' href='#'>"+titulo+"</a></dt>");
            }

        });
                
    }
})(jQuery);