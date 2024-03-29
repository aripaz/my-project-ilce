/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
( function($) {
    $.fn.form = function(opc){

        $.fn.form.settings = {
            titulo:"",
            app:"",
            forma:"",
            pk:"",
            pk_name:"",
            xmlUrl : "srvForma" , // "srvControl" "xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 2,
            modo:"",
            top: 122,
            height:500,
            width:510,
            datestamp:"",
            updateControl:"",
            updateForeignForm:"",
            originatingObject:"",
            showRelationships:"false",
            events:[],
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.form.options = $.extend($.fn.form.settings, opc);
            obj = $(this);
            $.fn.form.getGUI(obj);          
        });
 
    };
   
    
    $.fn.form.getGUI = function(obj){
        //Crea clave unica para forma
        var formSuffix =$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var sDialogo="";
        var sMainDivTabs="";
        var sDivTabs="";
        var sUlTabs="";
        var sBotonera="";
        //1. Primero crear el HTML necesario para contruir la interfaz de las relaciones
       
        sMainDivTabs="<div id='formTab_" + formSuffix +"' security='"+
        "' datestamp='" + $.fn.form.options.datestamp +
        "' app='" + $.fn.form.options.app +
        "' forma='" + $.fn.form.options.forma +
        "' pk='" + $.fn.form.options.pk +
        "' modo='" + $.fn.form.options.modo +
        "' originatingObject='" + $.fn.form.options.originatingObject +
        "' updateControl='" +  $.fn.form.options.updateControl +
        "' updateForeignForm='" + $.fn.form.options.updateForeignForm +
        "' >";
    
        var sBusqueda="";        
        if ($.fn.form.options.modo!='lookup') {
            sTituloTab="General";
            sButtonCaption='Guardar';
        }
        else {
            sTituloTab="Seleccione los criterios de b&uacute;queda";
            sButtonCaption='Buscar'
            sBusqueda = "<tr><td class='etiqueta_forma1' style='width:50%'>Guardar filtro como: </td><td class='etiqueta_forma1'><input name='$b' id='$b' value='' class='singleInput' /></td></tr>";
        }

        sUlTabs+="<ul><li><a href='#divFormGeneral_" + formSuffix +"'>"+ sTituloTab + "</a></li>";
        sDivTabs+="<div id='divFormGeneral_" + formSuffix +"' >" +
        "<div align='center'><br /><br />Cargando informaci&oacute;n... <br /> <br />"+
        "<img src='img/loading.gif' />"+
        "</div>"+
        "</div>";
    
        sBotonera+="<div align='right' style='clear:left'><table style='width:100%'>"+ sBusqueda + "<tr><td align='left' id='tdEstatus_" +formSuffix+"' class='estatus_bar'>&nbsp;</td><td align='right'>"+
        "<input type='button' class='formButton' id='btnGuardar_" + formSuffix +"' value='" + sButtonCaption + "' /></td></tr></table></div>";
                    
        if ($.fn.form.options.showRelationships=='true' &&
            $.fn.form.options.modo=='update') {
            //Busca las relaciones en la base de datos basadas en los
            //las formas hijos
            $.ajax(
            {
                url: "srvFormaSearch?$cf=3&$pk=" + $.fn.form.options.forma + "&$ta=select&$w=clave_forma_padre=" +$.fn.form.options.forma,
                dataType: ($.browser.msie) ? "text" : "xml",
                success:  function(data){
                    if (typeof data == "string") {
                        xmlRelation = new ActiveXObject("Microsoft.XMLDOM");
                        xmlRelation.async = false;
                        xmlRelation.validateOnParse="true";
                        xmlRelation.loadXML(data);
                        if (xmlRelation.parseError.errorCode>0) {
                            alert("Error de compilación xml:" + xmlRelation.parseError.errorCode +"\nParse reason:" + xmlRelation.parseError.reason + "\nLinea:" + xmlRelation.parseError.line);
                        }
                    }
                    else {
                        xmlRelation = data;
                    }
                    
                    oTabs= $(xmlRelation).find("registro"); 
                     $.each(oTabs, function(i, oTab){
                        $(oTab).find("clave_forma").each(function() {
                            formaForanea=$(this).text().split("\n")[0];
                            nombreForma=$($(oTabs[i]).find("forma")[$(this).index()]).text().split("\n")[0];
                            sUlTabs+="<li><a href='#formTab_" + $.fn.form.options.app +"_"+ formaForanea +"'>"+ nombreForma + "</a></li>";
                            sDivTabs+="<div id='formTab_" + $.fn.form.options.app +"_"+ formaForanea+"'>"+
                            "<div id='formGrid_"+ $.fn.form.options.app+"_"+formaForanea+
                            "' app='" + $.fn.form.options.app +
                            "' forma='" + formaForanea +
                            "' titulo='" + nombreForma +
                            "' leyendas='" + 
                            "' align='center' class='queued_grids'>"+
                            "<br /><br />Cargando informaci&oacute;n... <br /> <br />"+
                            "<img src='img/loading.gif' />"+
                            "</div>"+
                            "</div>";  
                        })
                    });
                    sUlTabs+="</ul>";
                    sMainDivTabs+=sUlTabs+sDivTabs+sBotonera+"</div>";
                    /* Verifica si se está construyendo una forma previa */ 
                    sDialogo+="<div id='dlgModal_"+ formSuffix + "' title='" + $.fn.form.options.titulo +"'>" + sMainDivTabs + "</div>";
                    obj.append(sDialogo);
                    $.fn.form.setFormObjects();
                },
                error:function(xhr,err){
                    $("#tdEstatus_" +formSuffix).html("Error al recuperar las relaciones de la forma");
                    alert("Error al recuperar las relaciones de la forma: \n"+ +xhr.responseText);
                }
            });
        } 
        else {
            sUlTabs+="</ul>";
            sMainDivTabs+=sUlTabs+sDivTabs+sBotonera+"</div>";
            sDialogo+="<div id='dlgModal_"+ formSuffix + "' title='" + $.fn.form.options.titulo +"'>" + sMainDivTabs + "</div>";
            obj.append(sDialogo);
            $.fn.form.setFormObjects();
        }
        
    };
    
    $.fn.form.setFormObjects = function(){  
   
        var formSuffix =$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var gridSuffix=$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.datestamp;
            
        $.ajax(
        {   
            url: $.fn.form.options.xmlUrl + "?$cf=" + $.fn.form.options.forma + "&$pk=" + $.fn.form.options.pk + "&$ta=" + $.fn.form.options.modo +"&1=clave_aplicacion=" + $.fn.form.options.pk + "&" + $.fn.form.options.filtroForaneo,
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
            
                /*Verifica el estatus de error*/
                var oError=$(xml).find("error");
                if (oError.length>0) {
                    var sDescripcionError=oError.find("descripcion").text();
                    $("#grid_"+gridSuffix+"_toppager_right").children(0).html(sDescripcionError);
                    $("#dlgModal_"+ formSuffix).remove();
                    if (oError.find("general").text()=="Error de Respuesta Vacia" && $("#_cp_").val()=="1")
                        if (confirm("No hay una consulta establecida para dicha función, ¿desea configurarla?"))
                            $("body").formqueue({
                                app: 1,
                                forma:8,
                                datestamp:$.fn.form.options.datestamp,
                                modo:"insert",
                                columnas:1,
                                pk:0,
                                filtroForaneo:"2=clave_aplicacion=1&3=clave_forma="+$.fn.form.options.forma+"&4=clave_perfil=1&5=clave_forma="+$.fn.form.options.forma+"&6=tipo_accion='lookup'",
                                height:"500",
                                width:"500",
                                originatingObject:"",
                                showRelationships:false                        
                            });
                    else
                        alert(sDescripcionError);
                    
                    $("#divwait").dialog( "close" );                 
                    return false;
                }
            
                formSuffix =$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
            
                /* Procesamiento de permisos */
                var sPermiso="";
                var oPermisos=$(xml).find("clave_permiso");
                oPermisos.each( function() {
                    sPermiso+=$(this).text()+",";
                })
                sPermiso=sPermiso.substr(0,sPermiso.length-1);

                if (sPermiso.indexOf("2")==-1 && $.fn.form.options.modo=='insert') {
                    alert("Su perfil no cuenta con permisos para insertar registros de esta forma, consulte al administrador del sistema");
                    return false;
                }

                if (sPermiso.indexOf("3")==-1 && $.fn.form.options.modo=='update') {
                    alert("Su perfil no cuenta con permisos para actualizar registros de esta forma, consulte al administrador del sistema");
                    return false;
                }
            
                //Se extraen datos generales de la forma
                sAliasLog=$(xml).find("configuracion_forma").find("alias_tab").text();
            
                sTitulo="";
                if (sAliasLog.split(' ').length>1) {
                    if (sAliasLog.split(' ')[0]=='la' &&
                        $.fn.form.options.modo=='insert')
                        sTitulo="Nueva " + sAliasLog.split(' ')[1];
            
                    if (sAliasLog.split(' ')[0]=='el' && 
                        $.fn.form.options.modo=='insert')
                        sTitulo="Nuevo "+sAliasLog.split(' ')[1];
            
                    if ($.fn.form.options.modo=='update')
                        sTitulo="Edición de "+sAliasLog.split(' ')[1];
                }
                
                //Se genera el HTML de la forma general
                $("#divFormGeneral_" + formSuffix).html($.fn.form.handleForm(xml));
                
                //Se extrae posibles escenarios que se podrían disparar al guardar la forma, 
                //dependiendo del valor del campo de seguimiento
                // Esto sólo aplica cuando el modo de la forma es Insert o Update
                if ($("#formTab_" + formSuffix).attr("modo")!="lookup") {
            
                var sEscenario="";
                actores= $(xml).find("fd_actores");
                $.each(actores, function(){
                    sEscenario+=$(this).find('fd_email_responsable').text()+
                                '|'+$(this).find('fd_responsable').text()+
                                '|'+$(this).find('fd_flujo_dato').text()+
                                '|'+$(this).find('fd_proceso').text()+
                                '|'+$(this).find('fd_campo_seguimiento_estatus').text()+
                                '|'+$(this).find('fd_secuencia').text()+
                                '|'+$(this).find('fd_asunto').text() +
                                '|'+$(this).find('fd_notificacion').text()+"#";
                });
                
                     $("#_e").val(sEscenario);
                }
                //Aplica el codigo proveniente del XML y que aplica en la forma
                evento=$(xml).find('configuracion_forma').find('evento').text();
                if (evento!="")
                  $.globalEval(evento);
              
                //Ahora carga los eventos relacionados con los campos
                for (i=1; i<$.fn.form.options.events.length; i++) {
                    if ($.fn.form.options.events[i]!=undefined && $.fn.form.options.events[i]!="" )
                        $.globalEval($.fn.form.options.events[i]);
                }
                
                //Establece atributo de seguridad
                $("#formTab_" + formSuffix).attr("security",sPermiso);
                
                //Borra la leyenda del grid                
                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("");
                var oForm=$("#form_" + formSuffix);

                //Activa los tooltips para ayuda 
                $(".tooltipField").tooltip({
                    bodyHandler: function() {
                            return $(this).attr("ayuda");
                    },
                    showURL: false,
                    extraClass: "pretty", 
                    fixPNG: true
                });
                
                //Se asigna evento a botón de guardar
                $("#btnGuardar_"+ formSuffix).button().click(function() {
                    nApp=this.id.split("_")[1];
                    nForma=this.id.split("_")[2];
                    nPK=this.id.split("_")[3];
                    formSuffix =this.id.split("_")[1] + "_" + this.id.split("_")[2] + "_" + this.id.split("_")[3];

                    $("#btnGuardar_"+formSuffix).disabled=true;
                    //Actualiza el estatus bar
                    $("#tdEstatus_" +formSuffix).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");
                    
                    // inside event callbacks 'this' is the DOM element so we first 
                    // wrap it in a jQuery object and then invoke ajaxSubmit 
                    if ($("#formTab_" + formSuffix).attr("modo")!="lookup") {

                        var options = { 
                            beforeSubmit:  validateForm,  // pre-submit callback 
                            success:       processXml,  // post-submit callback 
                            dataType:  ($.browser.msie) ? "text" : "xml",
                            url: "srvFormaInsert",       // override for form's 'action' attribute 
                            error:function(xhr,err){
                                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("Error al guardar registro");
                                $("#dlgModal_"+ formSuffix).remove();
                                alert("Error al guardar registro: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
                            }
                            //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                            //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                            //clearForm: true        // clear all form fields after successful submit 
                            //resetForm: true        // reset the form after successful submit 

                            // $.ajax options can be used here too, for example: 
                            //timeout:   3000 
                        }; 

                        oForm.ajaxSubmit(options); 

                        // !!! Important !!! 
                        // always return false to prevent standard browser submit and page navigation 
                        return false; 
                    }
                    else {
                        //Valida que traiga al menos un dato:
                        sData = "";
                        oCampos =oForm.serializeArray();
                        $.each(oCampos, function(i, oCampo){
                            sTipoDato=$(document.getElementById(oCampos[i].name)).attr("tipo_dato");
                            sNombreCampo=oCampo.name.replace("_"+formSuffix,"");
                            if ($.trim(oCampo.value)!="" && 
                                    sNombreCampo!="$ta" &&
                                    sNombreCampo!="$ca" &&
                                    sNombreCampo!="$cf" &&
                                    sNombreCampo!="$pk" )
                                if (sTipoDato=="string")
                                    sData+=sNombreCampo+" like '"+oCampo.value + "%'&";
                                else if (sTipoDato=='date')
                                    sData+=sNombreCampo+"='"+oCampo.value + "'&";
                                else
                                    sData+=sNombreCampo+"="+oCampo.value + "&";
                        });
                        
                        
                        if (sData=="") {
                            alert("Es necesario especificar al menos un criterio de búsqueda, verifique");
                            $("#tdEstatus_" +formSuffix).html(" Es necesario especificar al menos un criterio de b&uacute;squeda, verifique");
                        }    
                        else {
                            sData=sData.substring(0,sData.length-1).replace("&"," AND ");
                            oGridHeader=$("#grid_"+gridSuffix).parent().parent().parent().find("span.ui-jqgrid-title");
                            oMenuAccordion=$("#grid_"+gridSuffix).parent().parent().parent().parent().parent().parent().parent().parent().prev().prev().children().children();
                            sBitacoraId=oMenuAccordion[1].id;
                            sBusquedasId=oMenuAccordion[3].id;
                            nAplicacion=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                            nForma=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                            sDateStamp=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[4];
                            $(oGridHeader[0]).append("&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nForma + "_" + sDateStamp + "'>(Quitar filtro)</a>");

                            //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                            $("#lnkRemoveFilter_grid_" + gridSuffix).click(function() {
                                var sGridId="#grid_" +gridSuffix ;
                                if ($(sGridId).attr("requeriesFilter")=="1") {
                                        $("body").form({
                                            app: nApp,
                                            forma:nEntidad,
                                            datestamp:gridSuffix.split("_")[2],
                                            modo:"lookup",
                                            titulo: "Filtrado de registros",
                                            columnas:1,
                                            height:"500",
                                            width:"80%",
                                            pk:0,
                                            originatingObject: sGridId
                                        }); 
                                 }                                    
                                else
                                    $(sGridId).jqGrid('setGridParam',{
                                        url:"srvGrid?$cf=" + nForma + "&$dp=body"
                                    }).trigger("reloadGrid");
                                
                                $(this).remove();
                            });

                            // Si el usuario le dió un nombre a la consulta
                            // Significa que la desea guardar
                            //sData=escape(sData.substring(0,sData.length-1).replace("&"," AND "));
                            sData=escape(sData);
                            
                            if (document.getElementById("$b").value!="") {
                                sBusqueda=document.getElementById("$b").value;
                                postConfig = "$cf=93&$ta=insert&$pk=0"+
                                "&clave_aplicacion=" + $.fn.form.options.app +
                                "&clave_forma="+$.fn.form.options.forma+
                                "&clave_empleado="+ $("#_ce_").val() +
                                "&filtro="+escape(sBusqueda) +
                                "&consulta=" +sData;
                                $.post("srvFormaInsert",postConfig);
                                    
                                // Aqui va método del filtro para actualizarlo
                                sMenuDivPrefix=sBitacoraId.split("_")[1]+"_"+
                                sBitacoraId.split("_")[2]+"_"+
                                sBitacoraId.split("_")[3];
                                sMenuDivPrefix+=(sBitacoraId.split("_").length>4)?"_"+sBitacoraId.split("_")[4]:"";
                                $("#accordion_"+sMenuDivPrefix).appmenu.getFullMenu(sMenuDivPrefix,
                                    $.fn.form.options.app,
                                    $.fn.form.options.forma)

                            }

                            $("#grid_" + gridSuffix).jqGrid('setGridParam',{
                                url:"srvGrid?$cf=" +  $.fn.form.options.forma + "&$w=" + sData+ "&$dp=body&page=1"
                            }).trigger("reloadGrid")
                            $("#dlgModal_"+ formSuffix).dialog("destroy");
                            $("#dlgModal_"+ formSuffix).remove();
                            return false;
                        }                       
                    }
                        
                });
                
                //Fuerza a que se haga scroll a la página
                location.href=location.href.replace(location.hash,"") +"#top";
                
                //Se crea el diálogo con el HTML completo
                $("#dlgModal_"+ formSuffix).dialog({
                    modal: true,
                    title: sTitulo,    
                    /*height:$.fn.form.options.height, */
                    top:document.body.scrollTop+350,
                    width:$.fn.form.options.width,
                    open: function(event, ui) { 
                       $(this).dialog( "option", "position","center" ); 
                    },
                    close: function(event, ui) {
                        $(this).dialog("destroy");
                        $(this).remove();
                    }
                    
                });
            
                //Se crean los tabs
                $("#formTab_" + formSuffix).tabs();
            
                //Se llama a cola de grids
                for (i=0; i<$(".queued_grids").length;i++) {
                
                    //Entrega los valores del formulario
                    //para establecer posible relación de forma y grid
                    sWSParameters="&4=" + $.fn.form.options.pk_name+"="+$.fn.form.options.pk+"&";
                    aWSParameters=$("#form_" + formSuffix).serialize().split("&");
                    for (k=0; k<aWSParameters.length;k++) {
                        sWSParameters+=(k+5)+"="+aWSParameters[k].replace("_"+formSuffix,"")+"&";
                    }
                    
                    $("#formTab_" + formSuffix).tabs('select',i+1);
                   
                   //Se agrega la leyenda en la etiqueta del grid                   
                    oGrid=$(".queued_grids:first")[0]; 
                    
                    $(oGrid).attr("leyendas","Nuev@ " + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+","+
                    "Edición de " + sTitulo.substring(0,sTitulo.length-1).toLowerCase());
                
                    $(oGrid).removeClass('queued_grids').addClass('gridForeignContainer');
                    sTitulo=$(oGrid).attr("titulo");
                    gridId=oGrid.id;
                    sGridDef='$("#'+gridId+'").appgrid({'+
                    'app: "'+$.fn.form.options.app+'",'+
                    'entidad:"'+ gridId.split("_")[2]+'",'+
                    'pk:"0",'+
                    'editingApp:"1",'+
                    'wsParameters:"' +$.fn.form.options.pk_name+"="+$.fn.form.options.pk+'&'+sWSParameters+'",'+
                    'titulo:"'+sTitulo+'",'+
                    'height:"250",'+
                    'leyendas:["Nuev@ ' + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+'",'+
                    '"Edición de ' + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+'"],'+
                    'openKardex:false,'+
                    'originatingObject:"",'+
                    'showFilterLink:false,'+
                    'insertInDesktopEnabled:"0"});';
                    setTimeout(sGridDef,1000);
                }
                //nWidth=$("#divFormProfiles_" + suffix).width();
                
                //Reestablece la pestaña general
                $("#formTab_" + formSuffix).tabs('select',0);
                // Se ocultan los mensajes de validación
                oForm.find('.obligatorio').each(function() {
                    $("#msgvalida_" + this.name).hide();
                });

                //Se ocultan los campos con clase invisible
                //$(".invisible").hide().next().hide();
             
                $(".fecha").datepicker({
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
                });
                
                $(".fechayhora").datetimepicker({
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
                    timeOnlyTitle: 'Seleccione hora',
                    timeText: 'Hora seleccionada',
                    hourText: 'Hora',
                    minuteText: 'Minutos',
                    secondText: 'Segundos',
                    currentText: 'Ahora',
                    closeText: 'Cerrar'
                });
                

                $(".money").calculator({
                    useThemeRoller: true,
                    prompt: 'Calculadora',
                    showOn: 'operator'
                });

                //Se activa el foreign toolbar para editar registros foraneos
                oForm.find('.widgetbutton').fieldtoolbar({
                    app:$.fn.form.options.app
                });
                
                //Se activan el click de las liga orientadas a editar definiciones de campo
                if ($("#_cp_").val()=="1") {
                     $(".edit_field").die("click", edita_diccionario)
                     $(".edit_field").live("click", edita_diccionario)
                }
                //Función para editar diccionario desde la liga del alias
                function edita_diccionario() {
                         aId= this.id.split("-");
                         nApp=aId[1]; 
                         nForma=aId[2];
                         nPk=aId[3];
                         
                         //Si la forma ya está presente aborta llamado
                         if ($("#dlgModal_" + nApp + "_"+ nForma + "_" + nPk).length>0)
                             return false;
                         
                         sModo="update";
                         if (nPk==0)
                             sModo="insert"
                             
                        $("body").form({
                            app: nApp,
                            forma:13,
                            datestamp:obj.attr("datestamp"),
                            modo:sModo,
                            titulo: "Diccionario de datos ",
                            columnas:1,
                            pk:nPk,
                            filtroForaneo:aId[4],
                            height:"500",
                            width:"80%",
                            originatingObject: obj.id,
                            updateControl:obj.id
                        });       
                        
                        return;
                }
                
                function validateForm(formData, jqForm, options) { 
                   var bCompleto=true;
                    
                   $(jqForm[0]).find('.obligatorio').each(function() {
                       
                        if ($.trim(this.value)=="" && $(this).attr("type")!="checkbox") {
                            $("#td_" + this.name).addClass("errorencampo")
                            $(this).addClass("errorencampo");
                            $("#msgvalida_" + this.name).show();
                            bCompleto=false;
                        }
                        else if ($(this).attr("type")=="checkbox" && !this.checked)  {
                            $("#td_" + this.name).addClass("errorencampo")
                            $(this).addClass("errorencampo");
                            $("#msgvalida_" + this.name).show();
                            bCompleto=false;                            
                        }
                        else {
                            $("#td_" + this.name).removeClass("errorencampo")
                            $("#msgvalida_" + this.name).hide();
                            $(this).removeClass("errorencampo");
                        }
                    });

                    if (!bCompleto){
                            $("#tdEstatus_" +formSuffix).html("Falta dato obligatorio, verifique");
                        return false;
                     }
                     else {
                         $("#tdEstatus_" +formSuffix).html("<img src='img/throbber.gif'>&nbsp;Enviando información...");
                         return true;
                     }                        
                }

                function processXml(data) { 
                    // 'responseXML' is the XML document returned by the server; we use 
                    // jQuery to extract the content of the message node from the XML doc 
                    if (typeof data == "string") {
                        xmlResult = new ActiveXObject("Microsoft.XMLDOM");
                        xmlResult.async = false;
                        xmlResult.validateOnParse="true";
                        xmlResult.loadXML(data);
                        if (xmlResult.parseError.errorCode>0) {
                            alert("Error de compilación xml:" + xmlResult.parseError.errorCode +"\nParse reason:" + xmlResult.parseError.reason + "\nLinea:" + xmlResult.parseError.line);
                        }
                    }
                    else {
                        xmlResult = data;
                    }

                    var error = $(xmlResult).find("error");
                    
                    if (error.length>0) {
                            $.fn.form.options.error="Ocurrió un problema al guardar el registro (" + 
                                 error.find("general").text() + ". " +  
                                 error.find("descripcion").text() + ")";
                                
                                if ($("#_cp_").val()=="1")
                                    $.fn.form.options.error+=", haga click <a href='#' id='lnkEditQuery_" + 
                                    $.fn.form.options.app +"_" +  $.fn.form.options.entidad +"' class='editLink'>aqui</a> para editarla ";
                                
                                $("#tdEstatus_" +formSuffix).html($.fn.form.options.error);
                                $("#grid_"+gridSuffix+"_toppager_right").children(0).html($.fn.form.options.error);
                                
                            if (error.find("descripcion").text()=='La suma de las suficiencias rebasa el techo presupuestal, solicite una extensión.') {
                                if (confirm('La suma de las suficiencias rebasa el techo presupuestal, ¿desea elaborar una solicitud de extensión de techo presupuestal en este momento?')) {
                                    //Manda a llamar a webservice para actualizar estatus de proyecto 
                                    //postConfig = "$ca=51&$cf=72&$ta=update&$pk="+$("#clave_proyecto").val()+"&clave_estatus_proyecto=3";
                                    //$.post("srvFormaInsert",postConfig);
                                    $("body").formqueue({
                                        app: 1,
                                        forma:222,
                                        datestamp:$.fn.form.options.datestamp,
                                        modo:"update",
                                        pk:$("#clave_proyecto").val(),
                                        filtroForaneo:"2=clave_aplicacion=1",
                                        height:"500",
                                        width:"500",
                                        originatingObject:"",
                                        showRelationships:false                        
                                    });                                    
                                }
                            }   
                            return false;    
                   }      
                    
                    var nApp=$("#formTab_" + formSuffix).attr("app")
                    var nForma=$("#formTab_" + formSuffix).attr("forma");
                    var nPK=$("#formTab_" + formSuffix).attr("pk")
                    
                    sResultado=$(xmlResult).find("pk").text();
                    
                    /*Si la forma viene de la tabla de Mis pendientes, entonces se debe marcar la actividad como
                    //atendida
                    originatingObject=$("#formTab_" + formSuffix).attr("originatingObject")
                    if (originatingObject.split('_')[0] =='grid' && 
                        originatingObject.split('_')[1] =='1' &&
                        originatingObject.split('_')[2] =='101') {
                        nPKActividad=originatingObject.split('_')[4];
                        
                        postConfig = "$cf=101&$ta=update&$pk="+nPKActividad+
                                    "&clave_estatus=1";                            

                       $.post("srvFormaInsert",postConfig);
                    }
                    */    
    
                    //Verifica el flujo de datos                    
                    if ($("#_e").val()!="") {
                        aEscenarios=$("#_e").val().split("#");
                        for (var i=0; i<aEscenarios.length; i++) {
               
                            //Si el valor de campo de seguimiento es igual, se desencadena la notificación
                            if (aEscenarios[i].split("|")[4]!=undefined) {
                                if ($("#" + aEscenarios[i].split("|")[4]).val()== aEscenarios[i].split("|")[5]) {
                                    postConfig="from=plataforma@ilce.edu.mx&to=" + aEscenarios[i].split("|")[0] + "&subject=" + aEscenarios[i].split("|")[6] +
                                    "&message=Estimad@ " + aEscenarios[i].split("|")[1].split(" ")[0] + '\n\n'+ aEscenarios[i].split("|")[7];
                                    $.post("srvSendMail",postConfig);
                                }
                            }
                        }
                    }
                    
                    
                    //Verifica el tipo de control por actualizar
                    sControl=$("#formTab_" + formSuffix).attr("updateControl");
                    if (sControl=="") {
                        /*Si no fue definido el control, por default se actualia el grid*/
                        $("#grid_" + gridSuffix).jqGrid().trigger("reloadGrid"); 
                    } else {
                        oControl=$("#"+sControl);
                        /*Verifica si en realidad existe el control ...*/
                        if (oControl.length>0) {
                            
                            /* Verifica si es un arbol */
                            if (oControl[0].nodeName="DIV" && 
                                oControl[0].className.indexOf("jstree",0)>1) {
                                $("#"+sControl).treeMenu.getTreeDefinition($("#"+sControl))
                                $("#grid_" + gridSuffix).jqGrid().trigger("reloadGrid"); 
                            }
                            /* en caso de que no lo sea actualiza un combo*/ 
                            else
                            setXMLInSelect3(sControl,$("#formTab_" + formSuffix).attr("updateForeignForm"),'foreign',null)
                            
                        }
                    }
                    
                    //Cierra el dialogo
                    $("#dlgModal_"+ formSuffix).dialog("destroy");
                    $("#dlgModal_"+ formSuffix).remove();
                    
                    //La forma se vuelve a abrir cuando se solicita
                    //que muestre sus relaciones
                    //y es una alta 
                    if ($.fn.form.options.showRelationships=='true' &&
                        $.fn.form.options.modo=="insert")
                    
                    $("body").formqueue({
                        app: $.fn.form.options.app,
                        forma:$.fn.form.options.forma,
                        datestamp:$.fn.form.options.datestamp,
                        modo:"update",
                        titulo: $.fn.form.options.titulo,
                        columnas:1,
                        pk:sResultado,
                        filtroForaneo:$.fn.form.options.filtroForaneo,
                        height:"500",
                        width:"80%",
                        originatingObject: $.fn.form.options.originatingObject,
                        showRelationships: $.fn.form.options.showRelationships
                    }); 
                }
                
                //Cierra el dialogo de espera
                $("#divwait").dialog( "close" );                

                
            },
            error:function(xhr,err){
                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("Error al recuperar la forma");
                $("#dlgModal_"+ formSuffix).remove();
                
                //Cierra el dialogo de espera
                $("#divwait").dialog( "close" );                

                alert("Error al recuperar forma: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);          
            }
        });
    }
    
    $.fn.form.handleForm = function(xml){
        var sRenglon='';
        var nFormaForanea=0;
        var nApp=$.fn.form.options.app;
        var sSuffix= '_' + $.fn.form.options.app  + '_' + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var oCampos= $(xml).find("registro").children();
        var tabIndex=1;
        bVDS=$("#formTab" + sSuffix).attr("security").indexOf("5")!=-1?true:false;
        sInvisibleInputs="";
        var bAutoIncrement=false;
        oCampos.each(function(){
            sValorPredeterminado="";
            oCampo=$(this);
            sTipoCampo= oCampo.attr("tipo_dato").toLowerCase();
            if (oCampo.find('evento').text()!="")
                $.fn.form.options.events[tabIndex-1]=oCampo.find('evento').text();
            
            bAutoIncrement=(oCampo.attr("autoincrement")!=undefined)?true:false;
            if (bAutoIncrement)
                $.fn.form.options.pk_name=oCampo[0].nodeName;
            //Genera etiqueta
            nClave_campo=(oCampo.find('clave_campo').text()==undefined)?0:oCampo.find('clave_campo').text();
            sAlias=oCampo.find('alias_campo').text();
            bDatoSensible=oCampo.find('dato_sensible').text();
            bActivo=oCampo.find('activo').text();
            sValorPredeterminado=oCampo.find('valor_predeterminado').text();
            bVisible=oCampo.find('visible').text();
            bNoPermitirValorForaneoNulo=oCampo.find('no_permitir_valor_foraneo_nulo').text();
            sAyuda=oCampo.find('ayuda').text();
            nObligatorio=oCampo.find('obligatorio').text();
            
            if (bAutoIncrement) return true;
            if (bDatoSensible=="1" && !bVDS) return true;
            if (bVisible=='0' || bVisible=='') {
                sInvisibleInputs+='<input type="hidden" ' + 'id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" value="'
                if ($.fn.form.options.modo=='insert') 
                   sInvisibleInputs+=(sValorPredeterminado!="")?eval(sValorPredeterminado):"";
                else 
                   sInvisibleInputs+=oCampo[0].childNodes[0].data;
               
                sInvisibleInputs+='" />';
                return true;
            }
                
            sRenglon += '<td id="td_' +oCampo[0].nodeName + '" ';
            sRenglon += ' class="etiqueta_forma1' 
            if (bVisible=='0')
                sRenglon +=' invisible';
                
            sRenglon += '">';    
                    
            if (sAlias!='') {
               if ($("#_cp_").val()=="1") 
                   sRenglon+="<a id='lnkEditFieldDef-1-13-"+nClave_campo+"-2=clave_aplicacion="+$.fn.form.options.app+"3=clave_forma=" + $.fn.form.options.forma +"' href='#' class='edit_field' title='Haga clic aqui para abrir su definición en el diccionario de datos'>"+sAlias+"</a>"
               else{    
                   //Establece la seudoclase para mostrar la ayuda
                   if (sAyuda!="")
                       sRenglon+="<a class='tooltipField' ayuda='" +sAyuda+ "' href='#'>"+ sAlias+"</a>";
                   else    
                       sRenglon+=sAlias;
               }    
            }
            else {
                sRenglon+="<a id='lnkEditFieldDef-1-13-"+nClave_campo+"-2=clave_aplicacion="+$.fn.form.options.app+"3=clave_forma=" + $.fn.form.options.forma +"' href='#' class='edit_field' title='El campo no cuenta con alias, haga clic aqui para abrir su definición en el diccionario de datos'>"+oCampo[0].nodeName+"</a>";
            }

            //Verifica si el campo es obligatorio para incluir la leyenda en el alias
            if ($.fn.form.options.modo!="lookup" && nObligatorio=="1")  {
                sRenglon += ' (<span id="msgvalida_' + oCampo[0].nodeName + '">Obligatorio</span>*)</td>'
            }
            else {
                sRenglon += '</td>'
            }
            
            //Genera liga para forma foranea
            var nFormaForanea=$(this).find('foraneo').attr("clave_forma");
            var nEditaForaneos=$(this).find('foraneo').attr("agrega_registro");
            if (nFormaForanea!=undefined) {
                sRenglon+='<td class="etiqueta_forma_control1"><select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ';
                
                if (bActivo!="1") 
                    sRenglon+=' disabled="disabled" ';
                 
                if ($.fn.form.options.modo!="lookup" && nEditaForaneos=="true") {
                    sRenglon+='class="inputWidgeted1'
                }
                else {
                    sRenglon+='class="singleInput'
                }

                //Establece seudoclase a select

                if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                    sRenglon+=' obligatorio" '
                }
                else {
                    sRenglon+='" '
                }

                //sRenglon+='id="' + oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" >';
                sRenglon+='id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';
                if ($.fn.form.options.modo=="lookup" || bNoPermitirValorForaneoNulo!="1") {
                    sRenglon+="<option ";
                    if ($.fn.form.options.modo!='update' && sValorPredeterminado=="")
                        sRenglon+="selected='selected' ";
                    sRenglon +="></option>";
                }
                oCamposForaneos=oCampo.find('registro_' + oCampo[0].nodeName)
                
                oCamposForaneos.each(
                    function(){
                        oCampoForaneo=$(this);
                        sRenglon +="<option ";
                        
                        if ($.fn.form.options.modo=='insert' && sValorPredeterminado!="") {
                            if(eval(sValorPredeterminado)==oCampoForaneo.children()[0].childNodes[0].data)
                                sRenglon +="selected='selected'";
                        }

                        if ($.fn.form.options.modo=='update' && oCampo[0].childNodes[0].data==oCampoForaneo.children()[0].childNodes[0].data)
                            sRenglon +="selected='selected'";
                        sRenglon +=" value='" + oCampoForaneo.children()[0].childNodes[0].data  +"' >" + oCampoForaneo.children()[1].childNodes[0].data + "</option>";
                    }
                    )
                                
                sRenglon +='</select>';
                if ($.fn.form.options.modo!="lookup" && nEditaForaneos=="true") {
                    sRenglon +="<div class='widgetbutton' tipo='foreign_toolbar' control='" + oCampo[0].nodeName  + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                }
                
                sRenglon+='</td>|';
            }
            else {
                if (oCampo.find('tipo_control').text()=="textarea" || sTipoCampo=="text") {
                    sRenglon+='<td class="etiqueta_forma_control1">' +
                    '<textarea tabindex="' + tabIndex + '" rows="10" ';
                
                    if (bActivo!="1") 
                        sRenglon+=' readonly="readonly" ';
                         
                    sWidgetButton="";

                    if (sTipoCampo=='money' && bActivo=="1" ) {
                        sRenglon+='class="inputWidgeted1';
                        sWidgetButton='<div class="widgetbutton" tipo="calculator_buton" control="' + oCampo[0].nodeName + sSuffix +'"></div>';
                        sRenglon +="<div class='widgetbutton' tipo='foreign_toolbar' control='" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                    } else if (sTipoCampo=='datetime' && bActivo=="1") {
                        sRenglon+='class="inputWidgeted1';
                        sWidgetButton='<div class="widgetbutton" tipo="calendar_buton" control="' + oCampo[0].nodeName + sSuffix +'"></div>';
                    }
                    else
                        sRenglon+='class="singleInput';

                    //Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  
                        sRenglon+=' obligatorio"';
                    else 
                        sRenglon+='"';

                    //sRenglon += ' id="' + oCampo[0].nodeName + sSuffix + '" name="' +  oCampo[0].nodeName + sSuffix + '" ' +
                    sRenglon += ' id="' + oCampo[0].nodeName + '" name="' +  oCampo[0].nodeName + '" >';
                    
                    if ($.fn.form.options.modo=='insert')
                        sRenglon+=(sValorPredeterminado!="")?eval(sValorPredeterminado):"";
                    else 
                        sRenglon+=oCampo[0].childNodes[0].data;
                    
                    sRenglon+='</textarea></td>|';
                }
                else if ($(this).find('tipo_control').text()=="checkbox" || sTipoCampo=="bit") {
                    sRenglon += '<td class="etiqueta_forma_control1">' +
                    '<div style="width:10px; margin: 0px; padding: 0px"><input type="checkbox" value="1" tabindex="' + tabIndex +
                    '" id="'+ oCampo[0].nodeName+ '" name="' + oCampo[0].nodeName + '" ';

                    if (bActivo!="1") 
                        sRenglon+=' readonly="readonly" ';
                     
                    // Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput obligatorio" ';
                    }
                    else {
                        sRenglon+='class="singleInput" ';
                    }
                    
                    if ($.fn.form.options.modo=='insert')
                        sRenglon+=(sValorPredeterminado=='1')?'checked="checked" ':'';
                    else 
                        sRenglon+=(oCampo[0].childNodes[0].data=='1')?'checked="checked" ':'';

                    sRenglon+=' /></div></td>|';
                }   
                else {
                    sRenglon += '<td class="etiqueta_forma_control1">' + 
                    '<input tipo_dato="' + sTipoCampo + '" id="'+ oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" ' +
                    'tabindex="' + tabIndex + '" ';

                    if (bActivo!="1") 
                        sRenglon+=' readonly="readonly" ';
                     
                    sWidgetButton="";

                    if (sTipoCampo=='money' && bActivo=="1") {
                        sRenglon+='class="inputWidgeted1';
                        sWidgetButton='<div class="widgetbutton" tipo="calculator_button" control="' + oCampo[0].nodeName +'"></div>';
                    } else if (sTipoCampo=='datetime' && bActivo=="1") {
                        sRenglon+='class="inputWidgeted1';
                        sWidgetButton='<div class="widgetbutton" tipo="calendar_button" control="' + oCampo[0].nodeName +'"></div>';
                    }
                    else
                        sRenglon+='class="singleInput';

                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")
                        sRenglon +=' obligatorio';
                    
                    if (bActivo=="1") {
                        if (sTipoCampo=="datetime" )
                            sRenglon +=' fechayhora';
                        else if (sTipoCampo=="smalldatetime")
                            sRenglon +=' fecha';
                        else  if (sTipoCampo=="money")
                            sRenglon +=' money';
                    }

                    if (oCampo.find('tipo_control').text()=="file" && $.fn.form.options.modo!="lookup")
                        sRenglon +=' file" type="' + oCampo.find('tipo_control').text() + '" value="';
                    else
                        sRenglon +='" type="text" value="';

                    if ($.fn.form.options.modo=='insert')
                        sRenglon+=(sValorPredeterminado!="")?(eval(sValorPredeterminado)):"";
                    else 
                        sRenglon+=oCampo[0].childNodes[0].data;
                    
                    sRenglon+='" ';

                    //Validación para inputs estandar de acuerdo al tipo de datos del campo
                    if (sTipoCampo=="integer" /*|| sTipoCampo=="money"*/) {
                        sRenglon+=" onBlur='javascript:check_number(this)'";
                    }
                    else if (sTipoCampo=="date") {
                        sRenglon+=" onBlur='javascript:check_date(this)' "
                    }

                    sRenglon+= ' />' + sWidgetButton + ' </td>|';
                }
            }
            tabIndex++;
        }) //oCampos.each

        //Distribución en columnas
        sRenglon=sRenglon.substring(0,sRenglon.length-1);
        var aRows=sRenglon.split('|');
        var aRowsWithTextAreas=[];
        var nCols= $.fn.form.options.columnas;
        if (aRows.length>18) {
            nCols=2;
            //Vacía los textareas en otro arreglo
            var indexOfRowWithTextAreas=0;
            for (i=0; i<aRows.length;i++) {
                if (aRows[i].indexOf("textarea")>-1) {
                    aRowsWithTextAreas[indexOfRowWithTextAreas]=aRows[i];
                    aRows.splice(i,1);
                    i--;
                    indexOfRowWithTextAreas++;
                }
                    
            }
        }    
        var nRows = Math.round(aRows.length/nCols);
        var sForm="";
        var i;
        for (i=0; i<nRows; i++) {
            sForm+="<tr >";

            sForm+=aRows[i].replace(/etiqueta_forma_control1/g,"etiqueta_forma_control"+nCols).replace(/etiqueta_forma1/g,"etiqueta_forma"+nCols).replace(/inputWidgeted1/g,"inputWidgeted"+nCols);
            if (aRows.length>nRows+i && nCols>1) {
                    sForm+="<td>&nbsp;</td>"+aRows[nRows+i].replace(/etiqueta_forma_control1/g,"etiqueta_forma_control"+nCols).replace(/etiqueta_forma1/g,"etiqueta_forma"+nCols).replace(/inputWidgeted1/g,"inputWidgeted"+nCols);
            }
            sForm+="</tr>";
        }

        if (nCols>1) {
           for (i=0; i<aRowsWithTextAreas.length; i++) {
                sForm+="<tr >"+aRowsWithTextAreas[i].replace(/class="etiqueta_forma_control1"/g,'class="etiqueta_forma_control'+nCols+'" colspan="4"').replace(/etiqueta_forma1/g,"etiqueta_forma"+nCols).replace(/inputWidgeted1/g,"inputWidgeted"+nCols)+"</tr>";
            }
        }
        
        //Llena la primer pestaña con la forma de la entidad principal
        var formSuffix =$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        sForm="<form class='forma' id='form_" + formSuffix + "' name='form_"  + formSuffix + "' method='POST' ><table class='forma'>" + sForm + "</table>"+
               sInvisibleInputs +
              "<input type='hidden' id='_e' name='_e' value='' />" +
              "<input type='hidden' id='$ta' name='$ta' value='" + $.fn.form.options.modo + "' />" +
              "<input type='hidden' id='$ca' name='$ca' value='" + $.fn.form.options.app+ "' />" +
              "<input type='hidden' id='$cf' name='$cf' value='" + $.fn.form.options.forma+ "' />" +
              "<input type='hidden' id='$pk' name='$pk' value='" + $.fn.form.options.pk + "' /></form>"

        return sForm;
    }
   
})(jQuery);