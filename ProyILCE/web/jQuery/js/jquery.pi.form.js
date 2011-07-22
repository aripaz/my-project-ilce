/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
( function($) {
    $.fn.form = function(opc){

        $.fn.form.settings = {
            titulo:"",
            aplicacion:"",
            forma:"",
            pk:"",
            xmlUrl : "srvForma", //"xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 2,
            modo:"",
            top: 122,
            height:500,
            width:510,
            datestamp:"",
            updateControl:"",
            updateForeignForm:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.form.options = $.extend($.fn.form.settings, opc);
            obj = $(this);
            $.fn.form.ajax();          
        });
 
    };

    $.fn.form.getProfileTree =function(obj){

        obj.treeMenu({
            app:"1",
            entidad:($.fn.form.options.forma=="3")?"16":"5",
            pk:$.fn.form.options.pk
        });
    }

    $.fn.form.ajax = function(){
        //Crea clave unica para forma
        var formSuffix =$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;

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

                /* Creación de la forma hasta que el webservice se ejecute exitosamente */
                var suffix=$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk
                if ($.fn.form.options.aplicacion=="1" &&
                    ($.fn.form.options.forma=="2" || $.fn.form.options.forma=="3") &&
                    $.fn.form.options.modo!='lookup')
                    sTabs="<div id='formTab_" + suffix +"' security='"+ sPermiso + "' datestamp='" + $.fn.form.options.datestamp + "'>"+
                          "<ul><li><a href='#divFormGeneral_" + suffix +"'>General</a></li>"+
                          "<li><a href='#divFormPerfiles_" + suffix +"'>Perfiles de seguridad</a></li></ul>"+
                          "<div id='divFormGeneral_" + suffix +"' >" +
                          "<div align='center'><br><br />Cargando informaci&oacute;n... <br /> <br />"+
                          "<img src='img/loading.gif' /></div>"+
                          "</div>"+
                          "<div id='divFormPerfiles_" + suffix +"' class='etiqueta_perfil'>Seleccione los perfiles con autorizaci&oacute;n para accesar al objeto<div id='divFormProfiles_" + suffix +"' class='treeProfiles' behaviour='profile' originalForm='" + $.fn.form.options.forma + "'></div></div>"+
                          "</div>";
                else {

                    if ($.fn.form.options.modo!='lookup')
                        sTituloTab="General";
                    else
                        sTituloTab="Seleccione los criterios de b&uacute;queda";

                    sTabs="<div id='formTab_" + suffix + "' security='" + sPermiso + "'>"+
                    "<ul><li><a href='#divFormGeneral_" + suffix +"'>" + sTituloTab + "</a></li></ul>"+
                    "<div id='divFormGeneral_" + suffix +"'>" +
                    "<div align='center'><br><br />Cargando informaci&oacute;n... <br /> <br />"+
                    "<img src='img/loading.gif' /></div>"+
                    "</div>"+
                    "</div>";
                }

                var sBusqueda="";
                if ($.fn.form.options.modo!='lookup')
                    sButtonCaption='Guardar';
                else {
                    sButtonCaption='Buscar'
                    sBusqueda = "<tr><td class='etiqueta_forma' style='width:50%'>Guardar filtro como: </td><td class='etiqueta_forma'><input name='$b' id='$b' value='' class='singleInput' /></td></tr>";
                }

                sTabs+="<br><div align='right'><table style='width:100%'>"+ sBusqueda + "<tr><td align='left' id='tdEstatus_" +suffix+"' class='estatus_bar'>&nbsp;</td><td align='right'><input type='hidden' id='$cmd' name='$cmd' value='" + $.fn.form.options.modo + "'>" +
                "<input type='button' class='formButton' id='btnGuardar_" + suffix +"'   value='" + sButtonCaption + "'/></td></tr></table></div>";

                obj.append("<div id='dlgModal_"+ suffix + "' title='" + $.fn.form.options.titulo +"'>" + sTabs + "</div>");

                /*--- */
                $("#divFormGeneral_" + suffix).html($.fn.form.handleForm(xml));
                /* Inclusión de nuevo código*/
                 $("#btnGuardar_"+ suffix).click(function() {
                    $("#form_" + suffix).submit();
                 })

                $("#dlgModal_"+ suffix).dialog({
                    modal: true,
                    /*height:$.fn.form.options.height, */
                    top:$.fn.form.options.top,
                    width:$.fn.form.options.width,
                    close: function(event, ui) {
                        $(this).dialog("destroy");
                        $(this).remove();
                    }
                });

                $("#formTab_" + suffix).tabs();
                /**/
    

                if ($.fn.form.options.modo!='lookup')
                    $.fn.form.getProfileTree($("#divFormProfiles_" + formSuffix));
                
                oForm=$("#form_" + formSuffix);

                // Se ocultan los mensajes de validación
                oForm.find('.obligatorio').each(function() {
                    $("#msgvalida_" + this.name).hide();
                });

                //Se activa el datepicker para los campos con seudoclase fecha
                oForm.find('.fecha').datepicker( {
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
                    });

                //Se activa el foreign toolbar para editar registros foraneos
                oForm.find('.foreign_toolbar').fieldtoolbar({
                    app:$.fn.form.options.aplicacion
                    });

                var gridSuffix=$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.datestamp;

                //Se captura el submit
                oForm.submit(function() {
                    //Deshabilita el botón guardar
                    $("#btnGuardar_"+formSuffix).disabled=true;
                    //Actualiza el estatus bar
                    $("#tdEstatus_" +formSuffix).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");

                    var sWS="";
                    var oCampos;
                    var sData="";


                    if ($.fn.form.options.modo!="lookup") {
                        var bCompleto=true;
                        oForm.find('.obligatorio').each(function() {
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
                            $("#tdEstatus_" +formSuffix).html("Falta dato obligatorio, verifique    ");
                            return false;
                        }

                        //Preparando la información para enviarla via POST
                        $("#tdEstatus_" +formSuffix).html("<img src='img/throbber.gif'>&nbsp;Guardando informacion...");
                        sWS="srvFormaInsert";

                        oCampos = oForm.serializeArray();
                        jQuery.each(oCampos, function(i, oCampo){
                            sNombreCampo=oCampo.name.replace("_"+formSuffix,"");
                            sData+=sNombreCampo+"="+oCampo.value + "&";
                        });
                        sData+="$cf=" +$.fn.form.options.forma +
                        "&$pk=" + $.fn.form.options.pk +
                        "&$ta=" + $.fn.form.options.modo

                        $.ajax({
                            type: "POST",
                            url: sWS,
                            data:sData,
                            dataType: ($.browser.msie) ? "text" : "xml",
                            success: function(data){
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

                                sResultado=$(xmlResult).find("resultado").text();
                                
                                $("#tdEstatus_" +formSuffix).html("<img src='img/throbber.gif'>&nbsp;Guardando perfiles de seguridad...");
                                //Envía perfiles asociados a la forma aplicación
                                if ($.fn.form.options.forma=="2") {
                                    $("#divFormProfiles_" + formSuffix).find('li.jstree-checked').each(function(){
                                      //Se deben borrar los perfiles anteriores!!
                                      nPerfil=this.id.split("-")[1]
                                      $.post('srvFormaDelete','$cf=10&$w=clave_aplicacion='+sResultado+" AND clave_perfil="+nPerfil);
                                      sData='clave_perfil='+nPerfil+"&clave_aplicacion=" + sResultado + "&activo=1&$cf=10&$pk=0&$ta=insert";
                                      setTimeout("$.post('"+sWS+"','"+ sData+ "')",1000);
                                      /*$("#apps_menu").appmenu().appmenu.getSearchs($.fn.form.options.aplicacion)
                                      $.fn.treeMenu.getTreeDefinition*/

                                    });
                                }

                                //Envía perfiles asociados a la forma forma
                                if ($.fn.form.options.forma=="3") {
                                    //Se necesita recuperar los perfiles padres
                                    oPermisos=$("#divFormProfiles_" + formSuffix).find('li');
                                    oPermisos.each(function(){
                                       
                                       sNodoId=this.id;
                                       sTipoNodo=sNodoId.split("-")[0];
                                       nPerfil=sNodoId.split("-")[1];
                                       nPermiso=sNodoId.split("-")[2];

                                       if (sTipoNodo=="perfil") ///Se deben borrar los permisos anteriores
                                             $.post('srvFormaDelete','$cf=14&$w=clave_forma='+$.fn.form.options.pk+"&clave_perfil="+nPerfil);

                                       if (sTipoNodo=="permiso") {
                                           if ($.jstree._reference("#divFormProfiles_" + formSuffix).is_checked("#permiso-" + nPerfil + "-" + nPermiso))
                                                sData ="clave_forma="+sResultado+ "&clave_perfil="+nPerfil+"&clave_permiso="+nPermiso+"&$cf=14&$pk=0&$ta=insert";
                                                setTimeout("$.post('" + sWS+"','"+ sData +"')",1000);
                                       }
                                      
                                    });
                                }

                                //Cierra el dialogo
                                $("#dlgModal_"+ suffix).dialog("destroy");
                                $("#dlgModal_"+ suffix).remove();
                                if ($.fn.form.options.updateControl=="")
                                    $("#grid_" + gridSuffix).jqGrid().trigger("reloadGrid");
                                else
                                    setXMLInSelect3($.fn.form.options.updateControl,$.fn.form.options.updateForeignForm,'foreign',null)
                            },
                        error:function(xhr,err){
                            $("#tdEstatus_" +formSuffix).html("Error al actualizar registro");
                            alert("Error al actualizar registro: \n"+ +xhr.responseText);
                        }
                        });
                    }
                    else {
                        //Valida que traiga al menos un dato:
                        sData = "";
                        oCampos = oForm.serializeArray();
                        $.each(oCampos, function(i, oCampo){
                            sTipoDato=$("#" + oCampo.name).attr("tipo_dato");
                            sNombreCampo=oCampo.name.replace("_"+formSuffix,"");
                            if ($.trim(oCampo.value)!="")
                                if (sTipoDato=="string")
                                    sData+=sNombreCampo+" like '"+oCampo.value + "%'&";
                                else if (sTipoDato=='date')
                                    sData+=sNombreCampo+"='"+oCampo.value + "'&";
                                else
                                    sData+=sNombreCampo+"="+oCampo.value + "&";
                        });

                        if (sData=="") {
                            alert("Es necesario especificar al menos un criterio de b&uacute;squeda, verifique");
                        }
                        else {

                            $("#grid_" + gridSuffix)
                            oGridHeader=$("span.ui-jqgrid-title, #grid_"+gridSuffix );
                            nAplicacion=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                            nForma=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                            sDateStamp=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[4];
                            $(oGridHeader[0]).append("&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nForma + "_" + sDateStamp + "'>(Quitar filtro)</a>");

                            //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                            $("#lnkRemoveFilter_grid_" + gridSuffix).click(function() {
                                var sGridId="#grid_" +gridSuffix ;
                                $(sGridId).jqGrid('setGridParam',{
                                    url:"srvGrid?$cf=" + $.fn.form.options.forma + "&$dp=body"
                                    }).trigger("reloadGrid")
                                $(this).remove();
                            });

                            // Si el usuario le dió un nombre a la consulta
                            // Significa que la desea guardar
                            if (document.getElementById("$b").value!="") {
                                sBusqueda=document.getElementById("$b").value;
                                postConfig = "$cf=1&$ta=insert&$pk=0"+
                                "&clave_aplicacion=" + $.fn.form.options.aplicacion +
                                "&clave_empleado="+ $("#_ce_").val() +
                                "&parametro=menu.busqueda."+$.fn.form.options.forma+"."+sBusqueda +
                                "&valor=" +escape(sData.substring(0,sData.length-1));
                                $.post("srvFormaInsert", postConfig);
                                    
                                // Aqui va método del accordion para actualizarlo
                                $("#apps_menu").appmenu().appmenu.getSearchs($.fn.form.options.aplicacion)
                            }

                            sData = sData.substring(0,sData.length-1)
                            $("#grid_" + gridSuffix).jqGrid('setGridParam',{
                                url:"srvGrid?$cf=" + $.fn.form.options.forma + "&$w=" + escape(sData)+ "&$dp=body&page=1"
                                }).trigger("reloadGrid")
                            $("#dlgModal_"+ formSuffix).dialog("destroy");
                            $("#dlgModal_"+ formSuffix).remove();
                        }
                    }


                    return false;

                });

                $("#pager_"+ gridSuffix+"_left").html("");

            },
            error:function(xhr,err){
                if ($("#tdEstatus_" +formSuffix).length>0)
                    $("#tdEstatus_" +formSuffix).html("Error al actualizar registro");
                alert("responseText: "+xhr.responseText);
            }
        });
    };

    $.fn.form.handleForm = function(xml){
        var sRenglon='';
        var nFormaForanea=0;
        var nApp=$.fn.form.options.aplicacion;
        var sSuffix= '_' + $.fn.form.options.aplicacion  + '_' + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var oCampos= $(xml).find("registro").children();
        var tabIndex=1;
        bVDS=$("#formTab" + sSuffix).attr("security").indexOf("5")!=-1?true:false;
        var bAutoIncrement=false;
        oCampos.each(function(){
            oCampo=$(this);
            sTipoCampo= oCampo.attr("tipo_dato").toLowerCase();
            bAutoIncrement=(oCampo.attr("autoincrement")!=undefined)?true:false;
            //Genera etiqueta
            sAlias= oCampo.find('alias_campo').text();
            bDatoSensible=oCampo.find('dato_sensible').text();

            if (bAutoIncrement) return true;
            if (bDatoSensible=="1" && !bVDS) return true;

            if (sAlias!='') {
                sRenglon += '<td class="etiqueta_forma" id="td_' +oCampo[0].nodeName + sSuffix + '">' +sAlias;
            }
            else {
                sRenglon += '<td class="etiqueta_forma" id="td_' + oCampo[0].nodeName + sSuffix + '">' + oCampo[0].nodeName;
            }

            //Verifica si el campo es obligatorio para incluir la leyenda en el alias
            if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                sRenglon += ' (<span id="msgvalida_' + oCampo[0].nodeName + sSuffix + '">Obligatorio</span>*)</td>'
            }
            else {
                sRenglon += '</td>'
            }
            
            //Genera liga para forma foranea
            var nFormaForanea=$(this).find('foraneo').attr("clave_forma");
            var nEditaForaneos=$(this).find('foraneo').attr("agrega_registro");
            if (nFormaForanea!=undefined) {
                sRenglon+='<td class="etiqueta_forma"><select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ' + oCampo.find('evento').text() + ' ';
                
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


                sRenglon+='id="' + oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" >';
                sRenglon+="<option ";
                if ($.fn.form.options.modo=='insert')
                    sRenglon+="selected='selected' ";
                sRenglon +="></option>";

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
                    sRenglon +="<div class='foreign_toolbar' control='" + oCampo[0].nodeName + sSuffix + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' class='fieldToolbar'></div>";
                }
                
                sRenglon+='</td>|';
            }
            else {
                if (oCampo.find('tipo_control').text()=="textarea" || sTipoCampo=="text") {
                    sRenglon+='<td class="etiqueta_forma">' +
                    '<textarea tabindex="' + tabIndex + '" ';
                    
                    //Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput obligatorio"'
                        }
                    else {
                        sRenglon+='class="singleInput"'
                        }

                    sRenglon += 'id="' + oCampo[0].nodeName + sSuffix + '" name="' +  oCampo[0].nodeName + sSuffix + '" ' +
                    oCampo.find('evento').text() +
                    '>' + oCampo[0].childNodes[0].data + '</textarea></td>|';
                }
                else if ($(this).find('tipo_control').text()=="checkbox" || sTipoCampo=="bit") {
                    sRenglon += '<td class="etiqueta_forma">' +
                    '<div style="width:1px; margin: 0px; padding: 0px"><input type="checkbox" value="1" tabindex="' + tabIndex +
                    '" id="'+ oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" ';

                    // Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput" ';
                    }
                    else {
                        sRenglon+='class="singleInput obligatorio" ';
                    }

                    sRenglon+=(oCampo[0].childNodes[0].data=='1')?'checked="checked" ':''
                    sRenglon+=oCampo.find('evento').text() + ' /></div></td>|';
                }
                else {
                    sRenglon += '<td class="etiqueta_forma">' + 
                    '<input tipo_dato="' + sTipoCampo + '" id="'+ oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '"' +
                    'tabindex="' + tabIndex + '" ' +
                    ' class="singleInput';
                    
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")
                        sRenglon +=' obligatorio';

                    if (sTipoCampo=="date")
                        sRenglon +=' fecha';

                    sRenglon +='" type="text" value="' + oCampo[0].childNodes[0].data + '" ' + oCampo.find('evento').text();

                    //Validación para inputs estandar de acuerdo al tipo de datos del campo
                    if (sTipoCampo=="integer") {
                        sRenglon+=" onBlur='javascript:check_number(this)'";
                    }
                    else if (sTipoCampo=="date") {
                        sRenglon+=" onBlur='javascript:check_date(this)' "
                    }

                    sRenglon+= ' /></td>|';
                }
            }
            tabIndex++;
        }) //oCampos.each

        //Distribución en columnas
        sRenglon=sRenglon.substring(0,sRenglon.length-1);
        var aRows=sRenglon.split('|');
        var nCols= $.fn.form.options.columnas;
        var nRows = Math.round(aRows.length/nCols);
        var sForm="";
        var i;
        for (i=0; i<nRows; i++) {
            sForm+="<tr>";
            sForm+=aRows[i];
            if (aRows.length>nRows+i && nCols>1) {
                sForm+="<td>&nbsp;</td>"+aRows[nRows+i] ;
            }
            sForm+="</tr>";
        }

        //Llena la primer pestaña con la forma de la entidad principal
        var formSuffix =$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        sForm="<form class='forma' id='form_" + formSuffix + "' name='form_"  + formSuffix + "' enctype='multipart/form-data' ><table class='forma'>" + sForm + "</table></form>"


        return sForm;
    }

})(jQuery);