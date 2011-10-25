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
            events:[]
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
            sBusqueda = "<tr><td class='etiqueta_forma' style='width:50%'>Guardar filtro como: </td><td class='etiqueta_forma'><input name='$b' id='$b' value='' class='singleInput' /></td></tr>";
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
                               
                    $(xmlRelation).find("registro").each(function() {
                        $(this).find("clave_forma").each(function() {
                            formaForanea=$(this).text().split("\n")[0];
                            nombreForma=$($(xmlRelation).find("registro").find("forma")[$(this).index()]).text().split("\n")[0];
                            sUlTabs+="<li><a href='#formTab_" + $.fn.form.options.app +"_"+ formaForanea +"'>"+ nombreForma + "</a></li>";
                            sDivTabs+="<div id='formTab_" + $.fn.form.options.app +"_"+ formaForanea+"'>"+
                            "<div id='formGrid_"+ $.fn.form.options.app+"_"+formaForanea+
                            "' app='" + $.fn.form.options.app +
                            "' forma='" + formaForanea +
                            "' titulo='" + nombreForma +
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
                    alert(sDescripcionError);
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
                
                //Aplica el codigo proveniente del XML y que aplica en la forma
                evento=$(xml).find('configuracion_forma').find('evento').text();
                if (evento!="")
                  $.globalEval(evento);
              
                //Ahora carga los eventos relacionados con los campos
                for (i=1; i<$.fn.form.options.events.length; i++)
                    $.globalEval($.fn.form.options.events[i]);
                
                //Establece atributo de seguridad
                $("#formTab_" + formSuffix).attr("security",sPermiso);
                
                //Borra la leyenda del grid
                
                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("");
                var oForm=$("#form_" + formSuffix);

                //Se asigna evento a botón de guardar
                $("#btnGuardar_"+ formSuffix).click(function() {
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
                            url: "srvFormaInsert"       // override for form's 'action' attribute 
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
                            sData=sData.substring(0,sData.length-1)
                            oGridHeader=$("#grid_"+gridSuffix).parent().parent().parent().find("span.ui-jqgrid-title");
                            oMenuAccordion=$("#grid_"+gridSuffix).parent().parent().parent().parent().parent().parent().prev().prev().children().children();
                            sBitacoraId=oMenuAccordion[1].id;
                            sBusquedasId=oMenuAccordion[3].id;
                            nAplicacion=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                            nForma=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                            sDateStamp=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[4];
                            $(oGridHeader[0]).append("&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nForma + "_" + sDateStamp + "'>(Quitar filtro)</a>");

                            //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                            $("#lnkRemoveFilter_grid_" + gridSuffix).click(function() {
                                var sGridId="#grid_" +gridSuffix ;
                                $(sGridId).jqGrid('setGridParam',{
                                    url:"srvGrid?$cf=" + nForma + "&$dp=body"
                                }).trigger("reloadGrid")
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
            
                //Se crea el diálogo con el HTML completo
                $("#dlgModal_"+ formSuffix).dialog({
                    modal: true,
                    title: sTitulo,    
                    /*height:$.fn.form.options.height, */
                    top:$.fn.form.options.top,
                    width:$.fn.form.options.width,
                    close: function(event, ui) {
                        $(this).dialog("destroy");
                        $(this).remove();
                    }
                });
            
                //Se crean los tabs
                $("#formTab_" + formSuffix).tabs();
            
                //Se llama a cola de grids
                if ($(".queued_grids:first").length>0) {
                
                    //Entrega los valores del formulario
                    //para establecer posible relación de forma y grid
                    sWSParameters="";
                    aWSParameters=$("#form_" + formSuffix).serialize().split("&");
                    for (i=0; i<aWSParameters.length;i++) {
                        sWSParameters+=(i+4)+"="+aWSParameters[i].replace("_"+formSuffix,"")+"&";
                    }
                    oGrid=$(".queued_grids:first")[0]; 
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
                    'width:"100",'+
                    'leyendas:["Nuev@ ' + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+'",'+
                    '"Edición de ' + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+'"],'+
                    'openKardex:false,'+
                    'originatingObject:"",'+
                    'showFilterLink:false,'+
                    'insertInDesktopEnabled:"0"});';
                    setTimeout(sGridDef,1000);
                }
                //nWidth=$("#divFormProfiles_" + suffix).width();


                // Se ocultan los mensajes de validación
                oForm.find('.obligatorio').each(function() {
                    $("#msgvalida_" + this.name).hide();
                });

                //Se ocultan los campos con clase invisible
                $(".invisible").parent().hide();
             
                $(".fecha").datepicker({
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
                });

                $(".money").calculator({
                    useThemeRoller: true,
                    prompt: 'Calculadora'
                });

                //Se activa el foreign toolbar para editar registros foraneos
                oForm.find('.widgetbutton').fieldtoolbar({
                    app:$.fn.form.options.app
                });

                
                function validateForm(formData, jqForm, options) { 
                   var bCompleto=true;
                    
                   $(jqForm[0]).find('.obligatorio').each(function() {
                        if ($.trim(this.value)=="") {
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
                    
                    var nApp=$("#formTab_" + formSuffix).attr("app")
                    var nForma=$("#formTab_" + formSuffix).attr("forma");
                    var nPK=$("#formTab_" + formSuffix).attr("pk")
                    
                    sResultado=$(xmlResult).find("resultado").text();
                    
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
                        width:"500",
                        originatingObject: $.fn.form.options.originatingObject,
                        showRelationships: $.fn.form.options.showRelationships
                    }); 
                }
                
            },
            error:function(xhr,err){
                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("Error al recuperar la forma");
                $("#dlgModal_"+ formSuffix).remove();
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
            sAlias=oCampo.find('alias_campo').text();
            bDatoSensible=oCampo.find('dato_sensible').text();
            bActivo=oCampo.find('activo').text();
            sValorPredeterminado=oCampo.find('valor_predeterminado').text();
            bVisible=oCampo.find('visible').text();
            bNoPermitirValorForaneoNulo=oCampo.find('no_permitir_valor_foraneo_nulo').text();
            
            if (bAutoIncrement) return true;
            if (bDatoSensible=="1" && !bVDS) return true;

            sRenglon += '<td id="td_' +oCampo[0].nodeName + '" ';
            sRenglon += ' class="etiqueta_forma' 
            if (bVisible=='0')
                sRenglon +=' invisible';
                
            sRenglon += '">';    
                    
            if (sAlias!='') {
                sRenglon+=sAlias;
            }
            else {
                sRenglon+=oCampo[0].nodeName;
            }

            //Verifica si el campo es obligatorio para incluir la leyenda en el alias
            if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                sRenglon += ' (<span id="msgvalida_' + oCampo[0].nodeName + '">Obligatorio</span>*)</td>'
            }
            else {
                sRenglon += '</td>'
            }
            
            //Genera liga para forma foranea
            var nFormaForanea=$(this).find('foraneo').attr("clave_forma");
            var nEditaForaneos=$(this).find('foraneo').attr("agrega_registro");
            if (nFormaForanea!=undefined) {
                sRenglon+='<td class="etiqueta_forma"><select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ';
                
                if (bActivo!="1") 
                    sRenglon+=' disabled="disabled" ';
                 
                if ($.fn.form.options.modo!="lookup" && nEditaForaneos=="true") {
                    sRenglon+='class="inputWidgeted'
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
                    if ($.fn.form.options.modo!='update')
                        sRenglon+="selected='selected' ";
                    sRenglon +="></option>";
                }
                oCamposForaneos=oCampo.find('registro_' + oCampo[0].nodeName)
                
                oCamposForaneos.each(
                    function(){
                        oCampoForaneo=$(this);
                        sRenglon +="<option ";
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
                    sRenglon+='<td class="etiqueta_forma">' +
                    '<textarea tabindex="' + tabIndex + '" rows="10" ';
                
                    if (bActivo!="1") 
                        sRenglon+=' disabled="disabled" ';
                         
                    sWidgetButton="";

                    if (sTipoCampo=='money') {
                        sRenglon+='class="inputWidgeted';
                        sWidgetButton='<div class="widgetbutton" tipo="calculator_buton" control="' + oCampo[0].nodeName + sSuffix +'"></div>';
                        sRenglon +="<div class='widgetbutton' tipo='foreign_toolbar' control='" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                    } else if (sTipoCampo=='datetime') {
                        sRenglon+='class="inputWidgeted';
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
                    sRenglon += '<td class="etiqueta_forma">' +
                    '<div style="width:10px; margin: 0px; padding: 0px"><input type="checkbox" value="1" tabindex="' + tabIndex +
                    '" id="'+ oCampo[0].nodeName+ '" name="' + oCampo[0].nodeName + '" ';

                    if (bActivo!="1") 
                        sRenglon+=' disabled="disabled" ';
                     
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
                    sRenglon += '<td class="etiqueta_forma">' + 
                    '<input tipo_dato="' + sTipoCampo + '" id="'+ oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '"' +
                    'tabindex="' + tabIndex + '" ';

                    if (bActivo!="1") 
                        sRenglon+=' disabled="disabled" ';
                     
                    sWidgetButton="";

                    if (sTipoCampo=='money') {
                        sRenglon+='class="inputWidgeted';
                        sWidgetButton='<div class="widgetbutton" tipo="calculator_button" control="' + oCampo[0].nodeName + sSuffix +'"></div>';
                    } else if (sTipoCampo=='datetime') {
                        sRenglon+='class="inputWidgeted';
                        sWidgetButton='<div class="widgetbutton" tipo="calendar_button" control="' + oCampo[0].nodeName + sSuffix +'"></div>';
                    }
                    else
                        sRenglon+='class="singleInput';

                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")
                        sRenglon +=' obligatorio';

                    if (sTipoCampo=="datetime")
                        sRenglon +=' fecha';

                    if (sTipoCampo=="money")
                        sRenglon +=' money';

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
        var nCols= $.fn.form.options.columnas;
        if (aRows.length>18)
            nCols=2;
        var nRows = Math.round(aRows.length/nCols);
        var sForm="";
        var i;
        for (i=0; i<nRows; i++) {
            sForm+="<tr >";
            sForm+=aRows[i];
            if (aRows.length>nRows+i && nCols>1) {
                sForm+="<td>&nbsp;</td>"+aRows[nRows+i] ;
            }
            sForm+="</tr>";
        }

        //Llena la primer pestaña con la forma de la entidad principal
        var formSuffix =$.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        sForm="<form class='forma' id='form_" + formSuffix + "' name='form_"  + formSuffix + "' method='POST' ><table class='forma'>" + sForm + "</table>"+
              "<input type='hidden' id='$ta' name='$ta' value='" + $.fn.form.options.modo + "' />" +
              "<input type='hidden' id='$ca' name='$ca' value='" + $.fn.form.options.app+ "' />" +
              "<input type='hidden' id='$cf' name='$cf' value='" + $.fn.form.options.forma+ "' />" +
              "<input type='hidden' id='$pk' name='$pk' value='" + $.fn.form.options.pk + "' /></form>"

        return sForm;
    }

})(jQuery);