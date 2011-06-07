/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.appgrid = function(opc){

        $.fn.appgrid.settings = {
            xmlUrl : "srvGrid", // "xml_tests/widget.grid.xml"
            wsParameters:"",
            titulo:"",
            leyendas:[],
            app:"",
            entidad:"",
            pk:"",
            suffix:"",
            colNames: [],
            colModel: [{}],
            sortname:"",
            tab:"",
            insertarEnEscritorio:"1",
            width:"650",
            height:"",
            openKardex:false,
            loadMode:"",
            removeGridTitle:false,
            inQueue:false

        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var suffix =  "_" + nApp + "_" + nEntidad;
            //Registra el grid como habilitado para abrir kardex
            if ($.fn.appgrid.options.openKardex)
                $("#_vk_").val($("#_vk_").val()+"," +suffix);

            obj = $(this);
            var nForma = obj.attr(name);

             //Verifica si el objeto padre es un tabEntity
             //Si así es toma de su id el sufijo app + entidad principal + entidad foranea
            $(this).html("<table width='100%' id='grid" + suffix + "'>" +
                         "</table><div id='pager" + suffix +"'><div align='center' id='loader" + suffix +"'><br><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

             $.fn.appgrid.getGridDefinition();
        });

    };

    $.fn.appgrid.getGridDefinition = function(){
         $.ajax(
            {
            url: $.fn.appgrid.options.xmlUrl + "?$cf=" + $.fn.appgrid.options.entidad + "&$dp=body&$w=" + $.fn.appgrid.options.wsParameters,
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
                var nApp=$.fn.appgrid.options.app;
                var nEntidad=$.fn.appgrid.options.entidad;

                /* Agrega la liga para quitar filtro desde el contructor    */
                if ($.fn.appgrid.options.wsParameters!="") 
                    $.fn.appgrid.options.titulo+="&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nApp + "_" + nEntidad+"'>(Quitar filtro)</a>";

                /*Crea cadena a partir de objeto xml*/
                /*var sXML="";
                if (window.ActiveXObject)
                    sXML = xml;
                // code for Mozilla, Firefox, Opera, etc.
                else
                   sXML = (new XMLSerializer()).serializeToString(xml);*/
                if ($.fn.appgrid.options.loadMode=='delayed')
                   sDataType="local";
                else
                   sDataType="xml"

                var oGrid=$("#grid" + suffix).jqGrid(
                           {//datatype: "xmlstring",
                            //datastr: sXML,
                            url:$.fn.appgrid.options.xmlUrl + "?$cf="+ nEntidad + "&$dp=body&$w=" + $.fn.appgrid.options.wsParameters,
                            datatype: sDataType,
                            colNames:$.fn.appgrid.options.colNames,
                            colModel:$.fn.appgrid.options.colModel,
                            rowNum:10,
                            autowidth: true,
                            height:$.fn.appgrid.options.height,
                            rowList:[10,20,30],
                            pager: jQuery('#pager' + suffix),
                            sortname:  $.fn.appgrid.options.sortname+suffix,
                            viewrecords: true,
                            sortorder: "desc",                            
                            ondblClickRow: function(id){
                                  var openKardex=false;
                                  var aValidKardex= $("#_vk_").val().split(",");
                                  for (var i=0;i<=aValidKardex.length-1;i++) {
                                      if (aValidKardex[i]==suffix){
                                          openKardex=true;
                                          break;
                                      }
                                  }

                                  if (openKardex) {
                                     var nApp=this.id.split("_")[1];
                                     var nForm=this.id.split("_")[2];
                                     $.fn.appgrid.openKardex(nApp,nForm,id);
                                  }
                            },
                            caption:$.fn.appgrid.options.titulo}).navGrid('#pager' + suffix,
                                                    {edit:false,add:false,del:false,search:false, view:false}).navButtonAdd("#pager" + suffix,
                                                                                                   {caption:"",
                                                                                                     buttonicon:"ui-icon-plus",
                                                                                                     onClickButton:function() {
                                                                                                             $("body").form({aplicacion: nApp,
                                                                                                                             forma:nEntidad,
                                                                                                                             modo:"insert",
                                                                                                                             titulo: $.fn.appgrid.options.leyendas[0],
                                                                                                                             columnas:1,
                                                                                                                             pk:0,
                                                                                                                             height:400,
                                                                                                                             width:500});
                                                                                                            //$(this).trigger("reloadGrid");
                                                                                                        },
                                                                                                     position: "last", title:"Nuevo registro", cursor: "pointer"}).navButtonAdd("#pager" + suffix,
                                                                                                        {caption:"",
                                                                                                          buttonicon:"ui-icon-pencil",
                                                                                                          onClickButton:function() {
                                                                                                            nRow=$(this).getGridParam('selrow');
                                                                                                            if (nRow) {
                                                                                                                nPK= $(this).getCell(nRow,0);
                                                                                                                $("body").form({aplicacion: nApp,
                                                                                                                            forma:nEntidad,
                                                                                                                            modo:"update",
                                                                                                                            titulo: $.fn.appgrid.options.leyendas[1],
                                                                                                                            columnas:1,
                                                                                                                            pk:nPK,
                                                                                                                            height:"500",
                                                                                                                            width:"500"});
                                                                                                                //$(this).trigger("reloadGrid");
                                                                                                            }
                                                                                                            else {
                                                                                                                alert('Seleccione un registro');
                                                                                                            }
                                                                                                          },
                                                                                                     position: "last", title:"Editar registro",  cursor: "pointer"}).navButtonAdd("#pager" + suffix,
                                                                                                        {caption:"",
                                                                                                         buttonicon:"ui-icon-search",
                                                                                                         onClickButton:  function() {
                                                                                                               $("body").form({aplicacion: nApp,
                                                                                                                               forma:nEntidad,
                                                                                                                               modo:"lookup",
                                                                                                                               titulo: "B&uacute;squeda de registros",
                                                                                                                               columnas:1,
                                                                                                                               pk:0});
                                                                                                            /*$(this).trigger("reloadGrid");*/
                                                                                                          },
                                                                                                      position: "last",title:"Filtrar",cursor: "pointer"}).navButtonAdd("#pager" + suffix,
                                                                                                        {caption:"", 
                                                                                                         buttonicon:"ui-icon-document",
                                                                                                         onClickButton:  function() {
                                                                                                            var nApp=this.id.split("_")[1];
                                                                                                            var nForm=this.id.split("_")[2];
                                                                                                            nRow=$(this).getGridParam('selrow');
                                                                                                            if (nRow) {
                                                                                                                nPK= $(this).getCell(nRow,0);
                                                                                                                $.fn.appgrid.openKardex(nApp,nForm,nPK); }
                                                                                                            else
                                                                                                               alert('Seleccione un registro');
                                                                                                          },
                                                                                                      position: "last",title:"Abrir kardex",cursor: "pointer"});
               //Remueve del dom el loader
               $("#loader"+ suffix).remove();
               if ($.fn.appgrid.options.insertarEnEscritorio=="1")
                $("#grid" + suffix).jqGrid().navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false, view:false}).navButtonAdd("#pager" + suffix,{caption:"Insertar en escritorio",
                                                                                                         onClickButton:  function() {
                                                                                                             alert('Por implementar');
                                                                                                          },
                                                                                                      position: "last",title:"",cursor: "pointer"});

               //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                if ($.fn.appgrid.options.wsParameters!="") {
                    $("#lnkRemoveFilter_grid_" + nApp + "_" + nEntidad).click(function() {
                        nApp=this.id.split("_")[2];
                        nForma=this.id.split("_")[3];
                        var sGridId="#grid_" + this.id.split("_")[2] + "_" + + this.id.split("_")[3];
                        $(sGridId).jqGrid('setGridParam',{url:"srvGrid?$cf=" + nForma + "&$dp=body"}).trigger("reloadGrid")
                        $(this).remove();
                    });
                }


               //remueve toolbar por default
               $($("#pager" + suffix + "_left")[0].children[1]).remove();

               //Verifica si es posible abrir el kardex desde el toolbar
                var openKardex=false;
                aValidKardex=$("#_vk_").val().split(",");
                for (var i=0;i<=aValidKardex.length-1;i++) {
                  if (aValidKardex[i]==suffix){
                      openKardex=true;
                      break;
                  }
                }

                //Remueve el botón de kardex si no está especificado en el constructor
                if (!openKardex)    
                    $(".ui-icon-document", $("#pager"+suffix)).remove()

               //Verifica si el grid está en una cola
               if ($.fn.appgrid.options.inQueue)
                   //$('.foreign_grids:first').gridqueue({height: $('#_gq_').val()+'%'})
                   setTimeout("$('.foreign_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",2000);

               if ($.fn.appgrid.options.removeGridTitle)
                     $('.ui-jqgrid-titlebar',oGrid).remove();

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
             var sParent=$(this).parent()[0].tagName;
             oColumna={name:sParent+suffix,
                       index:sParent+suffix,
                       width:$(oTamano[iCol]).text()
                   };
             $.fn.appgrid.options.colNames[iCol]=$(this).text();
             $.fn.appgrid.options.colModel[iCol]=oColumna;
             iCol++;
        });

    $.fn.appgrid.openKardex = function(nApp, nEntidad, id) {
         var suffix =  "_" + nApp + "_" + nEntidad;

         var $tabs = $('#tabs').tabs({
                tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
            });

         //Verifica si ya está abierto el tab en modo de edición
         if ($("#tabEditEntity"+suffix + "_" + id).length) {
             $tabs.tabs( "select", "#tabEditEntity"+suffix+"_"+id);
         }
         else {
             oGrid=$('#grid'+ suffix);
             var nRow=oGrid.getGridParam('selrow');
             sTabTitulo=oGrid.jqGrid()[0].p.colNames[1] + ' ' + oGrid.getCell(nRow,1);
             sEntidad=$('#grid'+ suffix).jqGrid()[0].id.split("_")[2];
             $tabs.tabs( "add", "#tabEditEntity"+suffix+"_"+id, sTabTitulo);
             $tabs.tabs( "select", "#tabEditEntity"+suffix+"_"+id);
             //Crea la interfaz de la aplicación abierta
             $("#tabEditEntity"+suffix+"_"+id).html("<div id='divEditEntity_" + suffix + "'>" +
                 "<div id='tvApp" + suffix + "_" + id +"' class='treeContainer'></div>" +
                 "<div id='divForeignGrids" + suffix + "_" + id +"' class='gridContainer'></div>" +
                 "</div>");
             $("#tvApp" + suffix + "_" + id).treeMenu({
                 app:nApp,
                 entidad:nEntidad,
                 pk:id
            });

            }
        }

    }
})(jQuery);