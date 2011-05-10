/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.appgrid = function(opc){

        $.fn.appgrid.settings = {
            xmlUrl : "xml_tests/widget.grid.xml?entidad=",
            titulo:"",
            app:"",
            entidad:"",
            pk:"",
            suffix:"",
            colNames: [],
            colModel: [{}],
            sortname:"",
            tab:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            /*$.fn.appgrid.options.app=this.id.split("_")[1];
            $.fn.appgrid.options.entidad=this.id.split("_")[2];*/
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var suffix =  "_" + nApp + "_" + nEntidad;
             obj = $(this);

             //Verifica si el objeto padre es un tabEntity
             //Si así es toma de su id el sufijo app + entidad principal + entidad foranea
            $(this).append("<div id='search_" + nApp + "'>" +
                       "<div id='simple_search_" + nApp + "'>" +
                       "<input id='txtBusquedaSencilla_" + nApp + "' type='text' class='txtBusquedaSencilla'/>"+
                       "<input id='btnBusquedaSencilla_" + nApp + "' type='button' class='btnBusquedaSencilla' value='Buscar' />" +
                       "&nbsp;<a href='#' id='lnkBusqueda_" + nApp + "'>B&uacute;squeda avanzada</a>" +
                       "</div>" +
                       "<div id='advanced_search_" + nApp + "'></div>" +
                       "<div id='gridContainer" + suffix + "'><table width='100%' id='grid" + suffix + "'>" +
                       "</table><div id='pager" + suffix +"'></div></div>");

            $("#advanced_search_" + nApp).hide();
            $("#advanced_search_" + nApp).form({
                                     aplicacion: nApp,
                                     forma:nEntidad,
                                     modo:"lookup",
                                     pk:0
            });

            //Habilita mecanismo para expandir / colapsar el formulario de búsqueda avanzada
            $("#lnkBusqueda_" +nApp).click(function(){
                    nApp=this.id.split("_")[1];
                    $("#simple_search_"+ nApp).slideToggle();
                    $("#advanced_search_"+ nApp).slideToggle();
             });

             $.fn.appgrid.getGridDefinition();
        });

    };

    $.fn.appgrid.getGridDefinition = function(obj){
         $.ajax(
            {
            url: $.fn.appgrid.options.xmlUrl,
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
                $.fn.appgrid.handleGridDefinition(xml);

                var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad;

                /* Inicia implementación del grid */

                $("#grid" + suffix).jqGrid(
                            {url:"xml_tests/widget.grid.xml?entidad=" + $.fn.appgrid.options.entidad,
                            datatype: "xml",
                            colNames:$.fn.appgrid.options.colNames,
                            colModel:$.fn.appgrid.options.colModel,
                            rowNum:10,
                            autowidth: true,
                            rowList:[10,20,30],
                            pager: jQuery('#pager' + suffix),
                            sortname:  $.fn.appgrid.options.sortname+suffix,
                            viewrecords: true,
                            sortorder: "desc",
                            ondblClickRow: function(id){
                                  //inicializa valiable de tabs
                                  var $tabs = $('#tabs').tabs({
                                        tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                                  });

                                  //Verifica si ya está abierto el tab en modo de edición
                                  if ($("#tabEditEntity"+suffix + "_" + id).length) {
                                       $tabs.tabs( "select", "#tabEditEntity"+suffix);
                                  }
                                  else {
                                        sTabTitulo=this.p.colNames[1] + ' ' + this.rows[id].cells[1].innerHTML;
                                        sEntidad=this.id.split("_")[2];
                                        $tabs.tabs( "add", "#tabEditEntity"+suffix+"_"+id, sTabTitulo);
                                        $tabs.tabs( "select", "#tabEditEntity"+suffix+"_"+id);
                                        //Crea la interfaz de la aplicación abierta
                                        $("#tabEditEntity"+suffix+"_"+id).html("<div id='divEditEntity_" + suffix + "'>" +
                                                                               "<div id='tvApp" + suffix + "_" + id +"' class='treeContainer'></div>" +
                                                                               "<div id='frm" + suffix + "_" + id +"' class='formContainer'></div>" +
                                                                               "</div>");
                                        $("#tvApp" + suffix + "_" + id).treeMenu({app:$.fn.appgrid.options.app,
                                                                       entidad:$.fn.appgrid.options.entidad,
                                                                       pk:id });
                                        /*$("#tabEditEntity"+suffix+"_"+id).apptab({
                                            entidad:$.fn.appgrid.options.entidad,
                                            pk:id,
                                            app:$.fn.appgrid.options.app
                                        });*/

                                  }
                            },
                            caption:"Aplicaciones"}).navGrid('#pager' + suffix,
                                                    {edit:true,add:true,del:false},
                                                    {},// default settings for edit
                                                    {},// defautl settings for add
                                                    {}, // delete instead that del:false we need this
                                                    {"drag":true,"resize":true,"closeOnEscape":true,closeAfterSearch:true,multipleSearch:true});

                /* Finaliza implementación de grid */

            },
            error:function(xhr,err){
                alert("Error al recuperar definición de grid\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.appgrid.handleGridDefinition = function(xml){
        var iCol=0;
        var oColumnas=$(xml).find("column_definition");
        $.fn.appgrid.options.sortname=oColumnas.children()[0];
        
        oTamano=oColumnas.find('tamano');
        oAlias= oColumnas.find('alias_campo');
        oAlias.each( function() {
             suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad;
             oColumna={name:$(this)[0].nodeName+suffix,
                       index:$(this)[0].nodeName+suffix,
                       width:$(oTamano[iCol]).text()
                   };
             $.fn.appgrid.options.colNames[iCol]=$(this).text();
             $.fn.appgrid.options.colModel[iCol]=oColumna;
             iCol++;
        });

    }
})(jQuery);