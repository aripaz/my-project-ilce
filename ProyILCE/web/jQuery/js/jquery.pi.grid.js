/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.appgrid = function(opc){
        $.fn.appgrid.settings = {
            xmlUrl : "srvGrid" , // "srvControl", "srvGrid" "xml_tests/widget.grid.xml"
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
            width:"",
            height:"",
            openKardex:false,
            loadMode:"",
            removeGridTitle:false,
            showFilterLink:true,
            inQueue:false,
            inDesktop:false,
            editingApp:"",
            datestamp: sDateTime(new Date()),
            originatingObject:"",
            callFormWithRelationships:false,
            updateTreeAfterPost:false,
            logPhrase:"",
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var nPK=$.fn.appgrid.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad + "_" + $.fn.appgrid.options.datestamp;

            obj = $(this);
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
                "' callFormWithRelationships='"+$.fn.appgrid.options.callFormWithRelationships+
                "' updateTreeAfterPost='" + $.fn.appgrid.options.updateTreeAfterPost +
                "'>" +
                "</table><div id='pager" + suffix +"' security=''><div align='center' id='loader" + suffix +"'><br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

            $.fn.appgrid.getGridDefinition();
        });

    };

    $.fn.appgrid.getGridDefinition = function(){
        $.ajax(
        {
            url: $.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf=" + $.fn.appgrid.options.entidad.split('-')[0] + "&$ta=select&$dp=header&$w=" + $.fn.appgrid.options.wsParameters,
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

                if ($.fn.appgrid.options.error!="") {       
                    obj.html("<div class='ui-widget'>"+
                    "<br><br><br><br><br><br><br><br><br><br><br><div style='padding: 0 .7em; width: 80%; margin-left: auto; margin-right: auto;text-align: center;' class='ui-state-error ui-corner-all'>"+
                    "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                    $.fn.appgrid.options.error+"</p>"+
                    "</div></div>");
                    
                    /* Captura el click del link */
                    $(".editLink").click(function(){
                        nApp=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                    })

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
                    url:$.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid.options.wsParameters,
                    datatype: sDataType,
                    colNames:$.fn.appgrid.options.colNames,
                    colModel:$.fn.appgrid.options.colModel,
                    rowNum:50,
                    /*autowidth: true,*/
                    width:$.fn.appgrid.options.width,
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
                    userDataOnFooter: true */,
                    gridComplete:function(){
                        /* Establece eventos a los link del interior del grid*/ 
                        $(".gridlink").click(function(e, data) {
                            var nApp=this.id.split("_")[1];
                            var nEntidad=this.id.split("_")[2];
                            var nPK=this.id.split("_")[3];
                            var sW=this.id.split("_")[4];
                            var sModo="";
                            
                            if (nPK=="0") 
                                sModo="insert";
                            else
                                sModo="update";
                            
                            $("body").form({
                                app: nApp,
                                forma:nEntidad,
                                datestamp:oGrid.attr("datestamp"),
                                modo:sModo,
                                columnas:1,
                                pk:0,
                                filtroForaneo:"",
                                height:400,
                                width:"80%",
                                originatingObject:oGrid.id,
                                updateControl:""
                            });
                    });
                    
                    $(".progressbar").each( function(){
                        $(this).progressbar({value: $(this).attr("avance")});
                    });
                    
                     //oGrid.setGridWidth(oGrid.parent().width(),true);

                }});


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
                            $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                            nEditingApp=$(this).attr("editingApp");
                            
                            //Verifica si se actualiza árbol
                            sUpdateControl="";
                            if ($(this).attr("updateTreeAfterPost")=="true") 
                                    sUpdateControl=oGrid.attr("originatingObject");
                                
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
                                width:"80%",
                                originatingObject:oGrid.id,
                                showRelationships:$(this).attr("callFormWithRelationships"),
                                updateControl:sUpdateControl
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
                                $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                                nPK= $(this).getCell(nRow,0);
                                nEditingApp=$(this).attr("editingApp");
                                
                                //Verifica si se actualiza árbol
                                sUpdateControl="";
                                if ($(this).attr("updateTreeAfterPost")=="true")
                                    sUpdateControl=oGrid.attr("originatingObject");
                                            
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
                                    width:"80%",
                                    originatingObject: $(this).id,
                                    showRelationships:$(this).attr("callFormWithRelationships"),
                                    updateControl:sUpdateControl
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
                                    $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Eliminando registro...");
                                    $.ajax(
                                    {
                                        url: "srvFormaDelete?$cf="+ nEntidad + "&$pk="+ nPK,
                                        dataType: "text",
                                        success:  function(data){
                                            oGrid.jqGrid('delRowData',nRow);
                                            //Actualiza árbol
                                            if ($.fn.appgrid.options.updateTreeAfterPost) {
                                                sTvId=oGrid.attr("originatingObject");
                                                $("#"+sTvId).treeMenu.getTreeDefinition($("#"+sTvId));
                                            }
                                        },
                                        error:function(xhr,err){
                                            alert("Error al eliminar registro");
                                        }
                                    });
                                    $("#grid"+suffix+"_toppager_right").children(0).html("");
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
                        $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
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
                    buttonicon:"ui-icon-newwin",
                    onClickButton:  function() {
                        var nApp=this.id.split("_")[1];
                        var nForm=this.id.split("_")[2];
                        var sDateStamp=this.id.split("_")[3];
                        nRow=$(this).getGridParam('selrow');
                        if (nRow) {
                            $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Abriendo kardex...");
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
                        caption:"",
                        buttonicon:"ui-icon-heart",
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
                            
                            $.ajax(
                                    {
                                        url: "srvFormaInsert?"+postConfig,
                                        dataType: ($.browser.msie) ? "text" : "xml",
                                        success:  function(favorito){
                                             if (typeof favorito == "string") {
                                             xmlFav = new ActiveXObject("Microsoft.XMLDOM");
                                             xmlFav.async = false;
                                             xmlFav.validateOnParse="true";
                                             xmlFav.loadXML(favorito);
                                             if (xmlFav.parseError.errorCode>0) {
                                                    alert("Error de compilación xml:" + xmlFav.parseError.errorCode +"\nParse reason:" + xmlFav.parseError.reason + "\nLinea:" + xmlFav.parseError.line);}
                                            }
                                             else {
                                                xmlFav= favorito;}
                                            
                                            if ($(xmlFav).find("error").length>0) {
                                                alert("Error al insertar grid en favoritos");
                                                return;
                                            }
                                                
                                            nClave=$(xmlFav).find("pk").text();
                                            //Inserta el html para agragar el grid en el escritorio
                                            $('#tabMisFavoritos').tabs( "add", "#tabMisFavoritos_"+nClave, oGrid.attr("titulo"));

                                            $("#tabMisFavoritos_"+nClave).append("<div class='queued_grids'" +
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
                                        error:function(xhr,err){
                                            alert("Error al eliminar registro");
                                        }
                            }); 
                            
                        },
                        position: "last",
                        title:"Insertar en favoritos",
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
                
                $("#grid"+ suffix+"_toppager_left")[0].style.width="";
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
                    $(".ui-icon-newwin", $("#grid"+ suffix+"_toppager_left")).remove()

                //Verifica si el grid está en una cola
                if ($.fn.appgrid.options.inQueue)
                    setTimeout("$('.queued_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",3000);

                if ($.fn.appgrid.options.removeGridTitle)
                    $('.ui-jqgrid-titlebar',oGrid).remove();

            /* Finaliza implementación de grid */

            },
            error:function(xhr,err){
                sTipoError='Problemas al recuperar definición de grid.\n';
                if (xhr.responseText.indexOf('NullPointerException')>-1)
                    sTipoError+='Problemas de conexión a la base de datos, verifique la conexión a la red.'
                else
                    sTipoError+=xhr.responseText;

                suffix=obj.children()[1].id.replace("pager","");
                $("#loader"+suffix).html("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>"+
                    "<div class='ui-widget'>"+
                    "<div style='padding: 0 .7em; width: 80%' class='ui-state-error ui-corner-all'>"+
                    "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                    sTipoError+"</p>"+
                    "</div></div>");
            }
        });
    };

    $.fn.appgrid.handleGridDefinition = function(xml){
        var iCol=0;
        
        //Manejo de errores
        var error=$(xml).find("error");
        
        if (error.length>0) {
            if (error.find("tipo").text()=="SQLServerException" && $("#_cp_").val()=="1") {                
                 $.fn.appgrid.options.error+="Hay un error en la consulta (" + 
                     error.find("general").text() + ". " +  
                     error.find("descripcion").text() + "), haga click <a href='#' id='lnkEditQuery_" + 
                    $.fn.appgrid.options.app +"_" +  $.fn.appgrid.options.entidad +"' class='editLink'>aqui</a> para editarla "
                return true;    
                }
       }      
       
       //Titulo del grid
       $.fn.appgrid.options.titulo=$(xml).find("configuracion_grid").find("forma").text();
       
       //Titulo de forma de alta y de edición
       if ($(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[0]=="el")
           nuevo="Nuevo ";
       else
           nuevo="Nueva ";
       
       $.fn.appgrid.options.leyendas[0]=nuevo+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
       $.fn.appgrid.options.leyendas[1]="Edición de "+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
       $.fn.appgrid.options.logPhrase=$(xml).find("configuracion_grid").find("alias_tab").text();
       
       var oColumnas=$(xml).find("column_definition");
        $.fn.appgrid.options.sortname=oColumnas.children()[0];

        var sPermiso="";
        var oPermisos=$(xml).find("clave_permiso");
        
        if (oPermisos.length==0) {
            $.fn.appgrid.options.error="<p>Permisos insuficientes para consultar este catálogo, consulte con el administrador del sistema</p>";
            return;
        }
                 
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

        $("#pager"+ suffix).attr("security", sPermiso);
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
                "<div id='splitterContainer_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='splitterContainer'>"+
                "   <div id='leftPane_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='leftPane'>"+
                "       <div id='accordion_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "' class='accordionContainer'>"+
                "               <h3>&nbsp;Catálogos relacionados</h3>" +
                "                   <div id='tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp + "' class='treeContainer' behaviour='kardex'></div>" +
                "               <h3>&nbsp;Actividad reciente</h3>" +
                "                   <div id='bitacora_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "'></div>"+
                "               <h3>&nbsp;Mis filtros</h3>" +
                "                   <div id='filtros_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "'></div>"+
                "       </div>"+                
                "   </div>"+
                "   <div id='rigthPane_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp+"' class='rigthPane'>"+
                "       <div id='divForeignGrids_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='gridKardexContainer'>" +
                "               <br><br><br><br><br><br><br><br><br><br><br>"+
                "               <div class='ui-widget mensaje-info' >" +
                "                  <div class='ui-state-highlight ui-corner-all' style='padding: 0 .7em;'>" +
                "                        <p ><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span><strong>En este espacio se muestran los catálogos relacionados a " + $.fn.appgrid.options.logPhrase + " '" + oGrid.getCell(nRow,1) + "', seleccione una carpeta del menú de la izquierda</strong></p>"+
                "                        </div>" + 
                "                  </div>"  + 
                "        </div>" +
                "   </div>"+
                "</div>"+
                "</div>");

                                  
            //Crea splitter           
            $("#splitterContainer_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp).splitter({
		type: "v",
		outline: true,
		minLeft: 100, sizeLeft: 200, minRight: 500,
		resizeToWidth: true,
		cookie: "vs"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp,
		accessKey: '1'
            });
            
            // Crea árbol
            $("#tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp).treeMenu({
                app:nApp,
                entidad:nEntidad,
                pk:id
            });

            //Crea acordeón
            $("#accordion_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp).accordion();
            
            //Quita el padding al acordeón
            $("#tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp).addClass('paddingzero');
            
        }
        $("#grid"+suffix+"_toppager_right").children(0).html("");
    }
})(jQuery);