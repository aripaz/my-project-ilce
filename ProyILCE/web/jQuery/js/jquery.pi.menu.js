/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.menu = function(opc){

        $.fn.menu.settings = {
            xmlUrl : "", //"/ProyILCE/xml_tests/widget.accordion.xml",
            usuario:"",
            ts:""

        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.menu.options = $.extend($.fn.menu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);
            if ($.fn.menu.options.xmlUrl!="") {
                obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>");
                $.fn.menu.options.ts="U2FsdGVkX1+K/UZ+8JLyZRxlM2+sjv0subeoJS4mtaQ=";
                $.fn.menu.ajax(obj);
            }
        });

    };

    $.fn.menu.ajax = function(obj){
        $.ajax(
        {
            url: $.fn.menu.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xml = new ActiveXObject("Microsoft.XMLDOM");
                    xml.async = false;
                    xml.validateOnParse="true";
                    xml.loadXML(data);
                    if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                    }
                }
                else {
                    xml = data;
                }
                obj.html($.fn.menu.handleMenu(xml));
                $("#apps_menu").superfish({ 
                    pathClass:  'current' 
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
                $("a.menu").each( function(){
                    link_id="#"+this.id;

                    $(link_id).click(function(e, data) {
                        //Verifica si existe
                        var nAplicacion=this.id.split("_")[1];
                        var nEntidad=this.id.split("_")[2];
                        var sTitulo=this.childNodes[0].data;

                        if ($("#tab"+this.id).length) {
                            //Selecciona el tab correspondiente
                            $tabs.tabs("select", "#tab"+this.id);

                            //Recupera el id del grid del tab
                            sGridIdSuffix=$("#tab" + this.id).children()[0].children[0].id.replace("gbox_grid_","");

                            //Recarga el grid por si tiene algún filtro
                            if (data==undefined){
                                data="";
                                $("#lnkRemoveFilter_grid_" +sGridIdSuffix ).remove();
                            }
                            else {
                                //Si no existe el link para quitar filtro, lo establece
                                if ($("#lnkRemoveFilter_grid_" + sGridIdSuffix).length==0) {                                        
                                    oGridHeader=$("#gview_grid_" +sGridIdSuffix).find("span.ui-jqgrid-title");
                                    $(oGridHeader[0]).append("<a style='margin-left:10px' href='#' id='lnkRemoveFilter_grid_" + sGridIdSuffix +"'>(Quitar filtro)</a>");

                                    //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                    $("#lnkRemoveFilter_grid_" + sGridIdSuffix).click(function() {
                                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                                            url:"srvGrid?$cf=" + nEntidad + "&$dp=body&page=1"
                                            }).trigger("reloadGrid")
                                        $(this).remove();
                                    });
                                }
                            }

                            $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                                url:"srvGrid?$cf=" + nEntidad + "&$dp=body&$w=" +data
                                }).trigger("reloadGrid");

                        }
                        else {

                            if (this.id.split("_")[0]=="newEntity") {
                                $("body").form({
                                    app: nAplicacion,
                                    forma:nEntidad,
                                    modo:"insert",
                                    pk:0,
                                    titulo: sTitulo,
                                    columnas:1,
                                    height:400,
                                    width:500,
                                    originatingObject:obj.id
                                });
                            }
                            else {
                                $tabs.tabs( "add", "#tab"+this.id, this.childNodes[0].data);
                                $tabs.tabs( "select", "#tab"+this.id);
                                oTabPanel=$("#tab"+this.id);
                                    
                                //Se inserta el div para el grid
                                oTabPanel.html("<div id='grid_"+nAplicacion + "_" + nEntidad+"_0' class='gridContainer'/>"+
                                    "<div id='accordion_"+nAplicacion + "_" + nEntidad+"_0' class='accordionContainer'>"+
                                        "<h3>&nbsp;Actividad reciente</h3>" +
                                        "<div id='bitacora_"+nAplicacion + "_" + nEntidad+"_0'></div>"+
                                        "<h3>&nbsp;Mis filtros</h3>" +
                                        "<div id='filtros_"+nAplicacion + "_" + nEntidad+"_0'></div>"+
                                    "</div>"  );
                                var sLeyendaNuevoRegistro=$("#newEntity_" + nAplicacion + "_" + nEntidad ).text();
                                var sLeyendaEditaRegistro="Edita " + sLeyendaNuevoRegistro.split(" ")[1];

                                $("#grid_"+nAplicacion + "_" + nEntidad+"_0").appgrid({
                                    app: nAplicacion,
                                    entidad: nEntidad,
                                    pk:0,
                                    editingApp:nAplicacion,
                                    wsParameters:data,
                                    titulo:sTitulo,
                                    height:"70%",
                                    leyendas:[sLeyendaNuevoRegistro, sLeyendaEditaRegistro],
                                    openKardex:true,
                                    originatingObject:obj[0].id
                                });
                                
                                        
                                $.fn.menu.getSearchs("#filtros_"+nAplicacion + "_" + nEntidad+"_0");
                                setTimeout('$.fn.menu.getLog("#bitacora_'+nAplicacion + '_' + nEntidad+'_0",'+nAplicacion+','+nEntidad+')',4000);
                                //$.fn.menu.getLog("#bitacora_"+nAplicacion + "_" + nEntidad+"_0",nAplicacion,nEntidad);    
                            }
                        }
                    });
                });



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
                                alert("Error de compilación xml:" + xmlCache.parseError.errorCode +"\nParse reason:" + xmlCache.parseError.reason + "\nLinea:" + xmlCache.parseError.line);
                            }
                        }
                        else {
                            xmlCache = data;
                        }

                        h=$(xmlCache).find("valor").text();
                        if (h!=undefined)
                            $("#_ts_").val(h);
                        else
                            $("#_ts_").val($.fn.menu.options.ts);
                    });
                }
                 
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);
            }
        });
    };

    $.fn.menu.getSearchs = function(sDiv) {
        $(sDiv).html("");
        var nApp=sDiv.split("_")[1];
        var nForma=sDiv.split("_")[2];
        $.ajax(
        {
            url: "srvFormaSearch?$cf=93&$ta=select&$w=" + escape("clave_empleado=" +$("#_ce_").val()+ " AND clave_forma="+nForma),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlGs = new ActiveXObject("Microsoft.XMLDOM");
                    xmlGs.async = false;
                    xmlGs.validateOnParse="true";
                    xmlGs.loadXML(data);
                    if (xmlGs.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                }                
                else 
                    xmlGs = data;
                
                var sBusquedas="<br>";
                var nAplicacion="";
                
                $(xmlGs).find("registro").each( function(){
                    nClave=$(this).find("clave_filtro")[0].firstChild.data;
                    if (nClave=="") return false
                    sFiltro=$(this).find("filtro")[0].firstChild.data
                    nForma=$(this).find("clave_forma")[0].firstChild.data;
                    nAplicacion =$(this).find("clave_aplicacion")[0].firstChild.data;
                    sW=escape($(this).find("consulta")[0].firstChild.data);
                    sSuffix =nAplicacion + "_" + nForma + "_" + nClave;
                    sBusquedas="<div class='link_toolbar'>"+
                    "<div class='linkSearch'><a class='linkSearch' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sW+ "' forma='" + nForma + "' pk='" + nClave + "' >" + sFiltro + "</a></div>"+
                    "<div style='float:right'><div title='Eliminar filtro' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + nForma + "'></div></div>" +
                    "</div>";

                    $(sDiv).append(sBusquedas);

                    //Oculta botones
                    $(".ui-icon-close", "#filtros_"+nAplicacion+"_"+nForma+"_0").hide();
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
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
                    $.post("srvFormaDelete","$cf=93&$pk=" + $(this).attr("pk"));
                    $(this).parent().parent().remove();
                });


            }
        });
    }
    
    $.fn.menu.getLog = function(sDiv,nApp,nForm) {
        $(sDiv).html("");
        $.ajax(
        {
            url: "srvBitacora?$cf=91&$ta=select&$w=" + escape("ba.clave_forma=" +nForm),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlLog = new ActiveXObject("Microsoft.XMLDOM");
                    xmlLog.async = false;
                    xmlLog.validateOnParse="true";
                    xmlLog.loadXML(data);
                    if (xmlLog.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                }                
                else 
                    xmlLog = data;
                
                
                $(xmlLog).find("registro").each( function(){
                    sHtml="";
                    nClave=$(this).find("clave_bitacora")[0].firstChild.data;
                    dFecha=$(this).find("fecha_bitacora")[0].firstChild.data;
                    sFoto=$(this).find("foto")[0].firstChild.data.toLowerCase();
                    sNombre=$(this).find("nombre")[0].firstChild.data;
                    sTipoEvento=$(this).find("clave_tipo_evento")[0].firstChild.data;
                    sForma=$(this).find("consulta")[0].firstChild.data;
                    sBitacora=$(this).find("bitacora")[0].firstChild.data;
                    nAplicacion=$(this).find("clave_aplicacion")[0].firstChild.data;
                    nForma=$(this).find("clave_forma")[0].firstChild.data;
                    nRegistro=$(this).find("clave_registro")[0].firstChild.data;
                    if (nClave!="") 
                        sHtml="<div class='bitacora'>" +
                                sFoto +
                              sNombre + " " + sTipoEvento + " " + sForma + " " + 
                              "<a href='#' id='lnkBitacora_" + nAplicacion + "_" + nForma + "_" + nRegistro + "'>"+
                                sBitacora  + "</a> a las " + dFecha +
                                "</div>"
                    $(sDiv).append(sHtml);
                    //Hace bind del liga del búsqueda
                    if (nAplicacion!="")
                         $("#lnkBitacora_" + nAplicacion + "_" + nForma + "_" + nRegistro).click(function(){
                            $("body").form({
                                app: this.id.split("_")[1],
                                forma:this.id.split("_")[2],
                                pk:this.id.split("_")[3],
                                datestamp:$(this).attr("datestamp"),
                                modo:"update",
                                titulo: "Edita " + sForma.split(" ")[1],
                                columnas:1,
                                filtroForaneo:"2=clave_aplicacion=" + nApp,
                                height:400,
                                width:550,
                                originatingObject:"#lnkBitacora_" + nApp + "_" + nForma + "_" + nRegistro
                            });
                    }); 
                    
                });

                $("#accordion_"+nApp + "_" + nForm +"_0").accordion({
                                    /*active: false,
                                    fillSpace:true,
                                    collapsible: true,*/
                                    change: function() {
                                        $(this).find('h3').blur();
                                    }
                             }
                   );
            }
        });
    }
   
    $.fn.menu.handleMenu = function(xml){
        sHtml="<ul id='apps_menu' class='sf-menu'>"/*+
                "<li>"+
                "<a class='sf-with-ul' href='#'>Aplicaciones<span class='sf-sub-indicator'> &#187;</span></a>"+
                "<ul>"*/;

        $(xml).find("registro").each(function(){
            nAplicacion=$(this).find("clave_aplicacion").text();
            sTituloAplicacion=$(this).find("aplicacion").text()
            nEntidad=$(this).find("clave_forma").text();
            sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            nInsertar = $(this).find("insertar").text();
            nMostrar = $(this).find("mostrar").text();

            sHtml+="<li>" +
            "<a href='#' id='showEntity_" + nAplicacion + "_" + nEntidad +"' class='menu'>" + sTituloAplicacion + "</a>"+
            "</li>"

        })
        sHtml+="</ul></il></ul>"

        return sHtml;
    }
    
})(jQuery);