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
            groupFields:[],
            sortname:"",
            tab:"",
            insertInDesktopEnabled:"1",
            width:"650",
            height:"",
            openKardex:false,
            loadMode:"",
            removeGridTitle:false,
            showFilterLink:true,
            inQueue:false,
            inDesktop:false,
            editingApp:"",
            datestamp: sDateTime(new Date()),
            originatingObject:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var nPK=$.fn.appgrid.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad + "_" + $.fn.appgrid.options.datestamp;

            obj = $(this);
            var nForma = obj.attr(name);

            //Verifica si el objeto padre es un tabEntity
            //Si así es toma de su id el sufijo app + entidad principal + entidad foranea            
            $(this).html("<table width='100%' id='grid"+ suffix +
                "' titulo='" + $.fn.appgrid.options.titulo +
                "' wsParameters='"+ $.fn.appgrid.options.wsParameters +
                "' openkardex='" + $.fn.appgrid.options.openKardex +
                "' editingApp='" + $.fn.appgrid.options.editingApp +
                "' leyendas='" + $.fn.appgrid.options.leyendas[0] + "," + $.fn.appgrid.options.leyendas[1]+
                "' datestamp='" + $.fn.appgrid.options.datestamp +
                "' originatingObject='"+ $.fn.appgrid.options.originatingObject +
                "'>" +
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
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                    }
                }
                else {
                    xml = data;
                }
                $.fn.appgrid.handleGridDefinition(xml);

                if ($.fn.appgrid.options.colModel==null) {
                    obj.html("<div class='etiqueta_perfil' align='center'><br><br><br><br><br>Permisos insuficientes para consultar este catálogo, consulte con el administrador del sistema<br><br><br><br><br></div>");
                    return true;
                }

                var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.datestamp;

                /* Inicia implementación del grid */
                var nApp=$.fn.appgrid.options.app;
                var nEntidad=$.fn.appgrid.options.entidad.split('-')[0];

                /* Agrega la liga para quitar filtro desde el contructor    */
                if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink)
                    $.fn.appgrid.options.titulo+="&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid" + suffix+"'>(Quitar filtro)</a>";

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
                    rowNum:50,
                    autowidth: true,
                    shrinkToFit: false,
                    height:$.fn.appgrid.options.height,
                    rowList:[50,100,200],
                    pager: jQuery('#pager' + suffix),
                    toppager:true,
                    sortname: $.fn.appgrid.options.colModel[0],//$.fn.appgrid.options.sortname,//+suffix,
                    viewrecords: true,
                    sortorder: "desc",
                    //loadonce: true,
                    caption:$.fn.appgrid.options.titulo
                /*grouping: true,
                    groupingView : {
                            groupField : $.fn.appgrid.options.groupFields[0],
                            groupColumnShow : [true],
                            groupText : ['<b>{0}</b>'],
                            groupCollapse : false,
                            groupOrder: ['asc'],
                            groupSummary : [false],
                            groupDataSorted : true
                    },
                    footerrow: true,
                    userDataOnFooter: true,*/
                })


                //Quita agrupamiento
                //oGrid.jqGrid('groupingRemove',true);

                //Verifica si ya existen los botones para
                //evitar duplicarlos
                if ($('#grid'+ suffix+'_toppager_left').html()!="")
                    return true;

                //Va estableciendo botones de acuerdo a permisos
                sP=$("#pager"+suffix).attr("security");

                if (sP.indexOf("2")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-plus",
                        onClickButton:function() {
                            $("#pager"+suffix+"_left").html("<img src='img/throbber.gif'>&nbsp;Generando forma...");

                            nEditingApp=$(this).attr("editingApp");
                            $("body").form({
                                app: nApp,
                                forma:nEntidad,
                                datestamp:$(this).attr("datestamp"),
                                modo:"insert",
                                titulo: $.fn.appgrid.options.leyendas[0],
                                columnas:1,
                                pk:0,
                                filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                                height:400,
                                width:500,
                                originatingObject:oGrid.id
                            });
                        },
                        position: "last",
                        title:"Nuevo registro",
                        cursor: "pointer"
                    });
                }

                if (sP.indexOf("3")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-pencil",
                        onClickButton:function() {
                            nRow=$(this).getGridParam('selrow');
                            if (nRow) {
                                $("#pager"+suffix+"_left").html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                                nPK= $(this).getCell(nRow,0);
                                nEditingApp=$(this).attr("editingApp");
                                $("body").form({
                                    app: nApp,
                                    forma:nEntidad,
                                    datestamp:$(this).attr("datestamp"),
                                    modo:"update",
                                    titulo: $.fn.appgrid.options.leyendas[1],
                                    columnas:1,
                                    pk:nPK,
                                    filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                                    height:"500",
                                    width:"500",
                                    originatingObject: $(this).id
                                });
                            }
                            else {
                                alert('Seleccione el registro a editar');
                            }
                        },
                        position: "last",
                        title:"Editar registro",
                        cursor: "pointer"
                    });
                }

                if (sP.indexOf("4")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-trash",
                        onClickButton:function() {
                            nRow=$(this).getGridParam('selrow');
                            if (nRow) {
                                nPK= $(this).getCell(nRow,0);
                                if (confirm("¿Está seguro que desea eliminar el registro? No es posible deshacer esta acción.")){
                                    $("#pager"+suffix+"_left").html("<img src='img/throbber.gif'>&nbsp;Eliminando registro...");
                                    $.ajax(
                                    {
                                        url: "srvFormaDelete?$cf="+ nEntidad + "&$pk="+ nPK,
                                        dataType: "text",
                                        success:  function(data){
                                            oGrid.jqGrid('delRowData',nRow);
                                            //Actualiza árbol
                                            if (nEntidad=="3") {
                                                 sTvId=oGrid.attr("originatingObject");
                                                $("#"+sTvId).treeMenu.getTreeDefinition($("#"+sTvId));
                                            }
                                        },
                                        error:function(xhr,err){
                                            alert("Error al eliminar registro");
                                        }
                                    });
                                    $("#pager"+suffix+"_left").html("");
                                }
                            }
                            else {
                                alert('Seleccione el registro a eliminar');
                            }
                        },
                        position: "last",
                        title:"Eliminar registro",
                        cursor: "pointer"
                    });
                }


                oGrid.navGrid('#grid'+ suffix+'_toppager',{
                    edit:false,
                    add:false,
                    del:false,
                    search:false
                })
                .navButtonAdd('#grid'+ suffix+'_toppager',{
                    caption:"",
                    buttonicon:"ui-icon-search",
                    onClickButton:  function() {
                        $("#pager"+suffix+"_left").html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                        $("body").form({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"lookup",
                            titulo: "Filtrado de registros",
                            columnas:1,
                            pk:0,
                            originatingObject: oGrid.id
                        });
                        
                    },
                    position: "last",
                    title:"Filtrar",
                    cursor: "pointer"
                });

                oGrid.navGrid('#grid'+ suffix+'_toppager',{
                    edit:false,
                    add:false,
                    del:false,
                    search:false
                })
                .navButtonAdd('#grid'+ suffix+'_toppager',{
                    caption:"",
                    buttonicon:"ui-icon-document",
                    onClickButton:  function() {
                        var nApp=this.id.split("_")[1];
                        var nForm=this.id.split("_")[2];
                        var sDateStamp=this.id.split("_")[3];
                        nRow=$(this).getGridParam('selrow');
                        if (nRow) {
                            $("#pager"+suffix+"_left").html("<img src='img/throbber.gif'>&nbsp;Abriendo kardex...");
                            nPK= $(this).getCell(nRow,0);
                            $.fn.appgrid.openKardex(nApp,nForm,sDateStamp,nPK);
                        }
                        else
                            alert('Seleccione un registro');
                    },
                    position: "last",
                    title:"Abrir kardex",
                    cursor: "pointer"
                });

                //Remueve del dom el loader
                $("#loader"+ suffix).remove();
                if ($.fn.appgrid.options.insertInDesktopEnabled=="1")

                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false,
                        view:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
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
                                ",leyendas:" + oGrid.attr("leyendas").replace(",", "/") +
                                ",openKardex:" + oGrid.attr("openKardex") +
                                ",editingApp:" + oGrid.attr("editingApp") +
                                ",inDesktop:true"
                                );
                            $.post("srvFormaInsert", postConfig);

                            //Inserta el html para agragar el grid en el escritorio

                            $('#isotope').append("<div class='queued_grids'" +
                                " id='divDesktopGrid_" + nApp + "_" + nForma + "' " +
                                " app='" + nApp + "' " +
                                " form='" + nForma + "' " +
                                " wsParameters='" + oGrid.attr("wsParameters") + "' " +
                                " titulo='" + oGrid.attr("titulo") + "' " +
                                " leyendas='" +oGrid.attr("leyendas")+ "' "  +
                                " openKardex='" + oGrid.attr("openKardex") + "' " +
                                " inDesktop='true'" +
                                " class='queued_grids'," +
                                " insertInDesktopEnabled='0'></div>"+
                                "<div class='desktopGridContainer' ><br>&nbsp;&nbsp;&nbsp;&nbsp;<br><br></div><br>"
                                );

                            setTimeout("$('.queued_grids:first').gridqueue()",2000);
                            alert("Se agregó el grid al escritorio");

                        },
                        position: "last",
                        title:"",
                        cursor: "pointer"
                    });

                //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink) {
                    $("#lnkRemoveFilter_grid" + suffix).click(function() {
                        nApp=this.id.split("_")[2];
                        nForma=this.id.split("_")[3];
                        sDS=this.id.split("_")[4];
                        var sGridId="#grid_" + nApp + "_" + nForma+ "_"+ sDS;
                        $(sGridId).jqGrid('setGridParam',{
                            url:"srvGrid?$cf=" + nForma + "&$dp=body"
                        }).trigger("reloadGrid")
                        $(this).remove();
                    });
                }


                //remueve los botones refresh agregados por default
                $("table","#grid"+ suffix+"_toppager_left").each( function(){
                    if($(this).index()>0)
                        $(this).remove();
                });

                //Quita el paginador del la barra de herramientas superior
                $("#grid"+ suffix+"_toppager_center").remove();

                //Se reemplaza el contenido del toppager_right con
                // el combo para agrupar
                /*var sOptions="<option value='clear'>Quitar agrupamiento</option>";
                        for (i = 0; i < $.fn.appgrid.options.groupFields.length; i++) {
                            sOptions+="<option value='" + $.fn.appgrid.options.groupFields[i]+ "'>" +
                                $.fn.appgrid.options.groupFields[i] +
                                "</option>"
                        }

                        $("#grid"+ suffix+"_toppager_right").html("<select id='cbGroups"+suffix+"'>"+sOptions+"</select>"); */

                //Establece evento para select
                /*$("#cbGroups"+suffix).change(function() {
                    var suffix=this.id.split("_")[1] + "_" +
                    this.id.split("_")[2] + "_" +
                    this.id.split("_")[3];
                    var sVal=$(this).val();
                    if(sVal=='clear')
                        oGrid.jqGrid('groupingRemove',true);
                    else
                        oGrid.jqGrid('groupingGroupBy',sVal);
                });*/

                //Remueve el botón de kardex si no está especificado en el constructor
                if (oGrid.attr("openKardex")!="true")
                    $(".ui-icon-document", $("#grid"+ suffix+"_toppager_left")).remove()

                //Verifica si el grid está en una cola
                if ($.fn.appgrid.options.inQueue)
                    setTimeout("$('.queued_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",3000);

                if ($.fn.appgrid.options.removeGridTitle)
                    $('.ui-jqgrid-titlebar',oGrid).remove();

            /* Finaliza implementación de grid */

            },
            error:function(xhr,err){
                alert("Error al recuperar definición de grid\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);
            }
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
        var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad+ "_"+ $.fn.appgrid.options.datestamp;
        //var suffix = "-" + sDateTime(new Date());
        oAlias.each( function() {
             
            var sParent=$(this).parent()[0].tagName;
            if (sPermiso.indexOf("5")==-1 && $($(this).parent()).find("dato_sensible").text()=="1")
                return true;
            if (sParent=='column_definition') return true;
            oColumna={
                name:sParent+suffix,
                index:sParent+suffix,
                width:$(oTamano[iCol]).text(),
                sortable:true
            };

            $.fn.appgrid.options.colNames[iCol]=$(this).text();
            $.fn.appgrid.options.colModel[iCol]=oColumna;
            $.fn.appgrid.options.groupFields[iCol]=$(this).text();
            iCol++;
        });

        $("#pager"+ suffix).attr("security", sPermiso) ;
    }

    $.fn.appgrid.openKardex = function(nApp, nEntidad,sDateStamp, id) {
        var suffix =  "_" + nApp + "_" + nEntidad +"_" + sDateStamp;

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
                "<div id='tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp + "' class='treeContainer' behaviour='kardex'></div>" +
                "<div id='divForeignGrids" + suffix + "_" + id +"' class='gridContainer'></div>" +
                "</div>");
            $("#tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp).treeMenu({
                app:nApp,
                entidad:nEntidad,
                pk:id
            });

        }
        $("#pager"+suffix+"_left").html("");
    }
})(jQuery);