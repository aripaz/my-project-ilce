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
            showFilterLink:true,
            inQueue:false

        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var nPK=$.fn.appgrid.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad;

            obj = $(this);
            var nForma = obj.attr(name);

             //Verifica si el objeto padre es un tabEntity
             //Si así es toma de su id el sufijo app + entidad principal + entidad foranea
            $(this).html("<table width='100%' id='grid" + suffix +
                         "' titulo='" + $.fn.appgrid.options.titulo +
                         "' wsParameters='"+ $.fn.appgrid.options.wsParameters +
                         "' openkardex='" + $.fn.appgrid.options.openKardex +
                         "' leyendas='" + $.fn.appgrid.options.leyendas[0] + "," + $.fn.appgrid.options.leyendas[1]+ "'>" +
                         "</table><div id='pager" + suffix +"' security=''><div align='center' id='loader" + suffix +"'><br><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

             $.fn.appgrid.getGridDefinition();
        });

    };

    $.fn.appgrid.getGridDefinition = function(){
         $.ajax(
            {
            url: $.fn.appgrid.options.xmlUrl + "?$cf=" + $.fn.appgrid.options.entidad.split('-')[0] + "&$dp=body&$w=" + $.fn.appgrid.options.wsParameters,
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

                if ($.fn.appgrid.options.colModel==null) {
                    obj.html("<div class='etiqueta_perfil' align='center'><br><br><br><br><br>Permisos insuficientes para consultar este catálogo, consulte con el administrador del sistema<br><br><br><br><br></div>");
                    return true;
                }

                var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad;

                /* Inicia implementación del grid */
                var nApp=$.fn.appgrid.options.app;
                var nEntidad=$.fn.appgrid.options.entidad.split('-')[0];

                /* Agrega la liga para quitar filtro desde el contructor    */
                if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink)
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
                   sDataType="xml";

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
                            caption:$.fn.appgrid.options.titulo})

                //Va estableciendo botones de acuerdo a permisos
                sP=$("#pager"+suffix).attr("security");

                if (sP.indexOf("2")>-1) { 
                    oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false})
                    .navButtonAdd("#pager" + suffix,{
                        caption:"",
                        buttonicon:"ui-icon-plus",
                        onClickButton:function() {
                            nEditingApp=this.id.split("-")[1];
                            $("body").form({aplicacion: nApp,
                                forma:nEntidad,
                                modo:"insert",
                                titulo: $.fn.appgrid.options.leyendas[0],
                                columnas:1,
                                pk:0,
                                filtroForaneo:"2=clave_aplicacion=" + nEditingApp,
                                height:400,
                                width:500});},
                        position: "last",
                        title:"Nuevo registro",
                        cursor: "pointer"});
                }

                if (sP.indexOf("3")>-1) {
                    oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false})
                    .navButtonAdd("#pager" + suffix,{
                        caption:"",
                        buttonicon:"ui-icon-pencil",
                        onClickButton:function() {
                                nRow=$(this).getGridParam('selrow');
                                if (nRow) {
                                    nPK= $(this).getCell(nRow,0);
                                    nEditingApp=this.id.split("-")[1];
                                    $("body").form({aplicacion: nApp,
                                                forma:nEntidad,
                                                modo:"update",
                                                titulo: $.fn.appgrid.options.leyendas[1],
                                                columnas:1,
                                                pk:nPK,
                                                filtroForaneo:"2=clave_aplicacion=" + nEditingApp,
                                                height:"500",
                                                width:"500"});
                                }
                                else {
                                    alert('Seleccione el registro a editar');
                                }
                              },
                         position: "last", title:"Editar registro",  cursor: "pointer"});
               }

              if (sP.indexOf("4")>-1) { 
                    oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false})
                    .navButtonAdd("#pager" + suffix,{
                        caption:"",
                        buttonicon:"ui-icon-trash",
                        onClickButton:function() {
                                nRow=$(this).getGridParam('selrow');
                                if (nRow) {
                                    nPK= $(this).getCell(nRow,0);
                                    if (confirm("¿Está seguro que desea eliminar el registro? No es posible deshacer esta acción.")){
                                        $.ajax(
                                            {url: "srvFormaDelete?$cf="+ nEntidad + "&$pk="+ nPK,
                                             dataType: "text",
                                             success:  function(data){                                                
                                                oGrid.jqGrid('delRowData',nRow);
                                            },
                                            error:function(xhr,err){
                                                alert("Error al eliminar registro");}
                                         });                                                
                                    }
                                }
                                else {
                                    alert('Seleccione el registro a eliminar');
                                }
                        },
                        position: "last", title:"Eliminar registro",  cursor: "pointer"});
               }
                        
                   
              oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false})
                    .navButtonAdd("#pager" + suffix,{
                        caption:"",
                        buttonicon:"ui-icon-search",
                        onClickButton:  function() {
                               $("body").form({aplicacion: nApp,
                                               forma:nEntidad,
                                               modo:"lookup",
                                               titulo: "Filtrado de registros",
                                               columnas:1,
                                               pk:0});
                        },
                        position: "last",title:"Filtrar",cursor: "pointer"});

              oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false})
                    .navButtonAdd("#pager" + suffix,{
                        caption:"",
                        buttonicon:"ui-icon-document",
                        onClickButton:  function() {
                            var nApp=this.id.split("_")[1];
                            var nForm=this.id.split("_")[2];
                            var nGridPK=this.id.split("_")[3];
                            nRow=$(this).getGridParam('selrow');
                            if (nRow) {
                                nPK= $(this).getCell(nRow,0);
                                $.fn.appgrid.openKardex(nApp,nForm,nPK);}
                            else
                               alert('Seleccione un registro');
                          },
             position: "last",title:"Abrir kardex",cursor: "pointer"});

               //Remueve del dom el loader
               $("#loader"+ suffix).remove();
               if ($.fn.appgrid.options.insertarEnEscritorio=="1")

                    oGrid.navGrid('#pager' + suffix,{edit:false,add:false,del:false,search:false, view:false})
                        .navButtonAdd("#pager" + suffix,{
                            caption:"Insertar en escritorio",
                            onClickButton:  function() {
                                nApp=this.id.split("_")[1];
                                nForma=this.id.split("_")[2];
                                postConfig = "$cf=1&$ta=insert&$pk=0"+
                                "&clave_aplicacion=" + nApp +
                                "&clave_empleado="+ $("#_ce_").val() +
                                "&parametro=escritorio.grid"+
                                "&valor=" +escape("app:"+nApp+
                                                  ",entidad:" + nForma +
                                                  ",wsParameters:" + oGrid.attr("wsParameters") +
                                                  ",titulo:" + oGrid.attr("titulo") +
                                                  ",leyendas:" + oGrid.attr("leyendas") +
                                                  ",openKardex:" + oGrid.attr("openKardex")
                                                  );
                                $.post("srvFormaInsert", postConfig);

                                //Inserta el html para agragar el grid en el escritorio
                                $("#tabUser").append("<div class='queued_grids' app='" + nApp +
                                    "' form='" + nForma +
                                    "' wsParameters='" + oGrid.attr("wsParameters") +
                                    "' titulo='" + oGrid.attr("titulo") +
                                    "' leyendas='" + oGrid.attr("leyendas") +
                                    "' openKardex='" + oGrid.attr("openKardex") + "'></div>");

                                setTimeout("$('.queued_grids:first').gridqueue()",2000);
                                alert("Se agregó el grid al escritorio");

                            },
                            position: "last",
                            title:"",
                            cursor: "pointer"});

               //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink) {
                    $("#lnkRemoveFilter_grid_" + nApp + "_" + nEntidad).click(function() {
                        nApp=this.id.split("_")[2];
                        nForma=this.id.split("_")[3];
                        var sGridId="#grid_" + this.id.split("_")[2] + "_" + + this.id.split("_")[3];
                        $(sGridId).jqGrid('setGridParam',{url:"srvGrid?$cf=" + nForma + "&$dp=body"}).trigger("reloadGrid")
                        $(this).remove();
                    });
                }


               //remueve los botones refresh agregados por default
               $("table","#pager" + suffix + "_left").each( function(){
                    if($(this).index()>0)
                        $(this).remove();
               });

                //Remueve el botón de kardex si no está especificado en el constructor
                if (oGrid.attr("openKardex")!="true")
                    $(".ui-icon-document", $("#pager"+suffix)).remove()

               //Verifica si el grid está en una cola
               if ($.fn.appgrid.options.inQueue)
                   setTimeout("$('.queued_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",2000);

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

        var sPermiso="";
        var oPermisos=$(xml).find("clave_permiso");
        oPermisos.each( function() {
             sPermiso+=$(this).text()+",";
        })
        sPermiso=sPermiso.substr(0,sPermiso.length-1);

        if (sPermiso.indexOf("1")==-1) {
            $.fn.appgrid.options.colModel=null;
            return true;
        }

        oTamano=oColumnas.find('tamano');
        oAlias= oColumnas.find('alias_campo');
        var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad;
        oAlias.each( function() {
             
             var sParent=$(this).parent()[0].tagName;
             if (sPermiso.indexOf("5")==-1 && $($(this).parent()).find("dato_sensible").text()=="1")
                 return true;
             if (sParent=='column_definition') return true;
             oColumna={name:sParent+suffix,
                       index:sParent+suffix,
                       width:$(oTamano[iCol]).text()
                   };
             $.fn.appgrid.options.colNames[iCol]=$(this).text();
             $.fn.appgrid.options.colModel[iCol]=oColumna;
             iCol++;
        });


        $("#pager"+ suffix).attr("security", sPermiso) ;
    }

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
             $("#tabEditEntity"+suffix+"_"+id).html("<div id='divEditEntity_" + suffix + "' class='etiqueta_perfil'>" +
                 "<div id='tvApp" + suffix + "_" + id +"' class='treeContainer' behaviour='kardex'></div>" +
                 "<div id='divForeignGrids" + suffix + "_" + id +"' class='gridContainer'></div>" +
                 "</div>");
             $("#tvApp" + suffix + "_" + id).treeMenu({
                 app:nApp,
                 entidad:nEntidad,
                 pk:id
            });

            }
        }
})(jQuery);