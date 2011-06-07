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
            xmlUrl : "srvForma", //"xml_tests/forma.app.xml"
            columnas: 2,
            modo:"",
            top: 122,
            height:500,
            width:510
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.form.options = $.extend($.fn.form.settings, opc);
            var suffix=$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk
            obj = $(this);

            if ($.fn.form.options.aplicacion=="1" && $.fn.form.options.modo!='lookup')
                sTabs="<div id='formTab_" + suffix +"'>"+
                        "<ul><li><a href='#divFormGeneral_" + suffix +"'>General</a></li>"+
                            "<li><a href='#divFormPerfiles_" + suffix +"'>Perfiles de seguridad</a></li></ul>"+
                        "<div id='divFormGeneral_" + suffix +"'>" +
                            "<div align='center'><br><br />Cargando informaci&oacute;n... <br /> <br />"+
                                        "<img src='img/loading.gif' /></div>"+
                        "</div>"+
                        "<div id='divFormPerfiles_" + suffix +"' class='etiqueta_perfil'>Seleccione los perfiles con autorizaci&oacute;n para accesar al objeto<div id='divFormProfiles_" + suffix +"' class='treeProfiles'></div></div>"+
                       "</div>"
            else {

                if ($.fn.form.options.modo!='lookup')
                    sTituloTab="General"
                else
                    sTituloTab="Seleccione los criterios de b&uacute;queda"

                sTabs="<div id='formTab_" + suffix + "'>"+
                      "<ul><li><a href='#divFormGeneral_" + suffix +"'>" + sTituloTab + "</a></li></ul>"+
                      "<div id='divFormGeneral_" + suffix +"''>" +
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
               sBusqueda = "<span class='formButton'> Guardar filtro como: </span><input name='$b' id='$b' value=''  class='formButton' />&nbsp;&nbsp;";
           }

           sTabs+="<br><div align='right'><input type='hidden' id='$cmd' name='$cmd' value='" + $.fn.form.options.modo + "'>" +
                  sBusqueda +  
                  "<input type='button' class='formButton' id='btnGuardar_" + suffix +"'   value='" + sButtonCaption + "'/></div>";
              
           obj.append("<div id='dlgModal_"+ suffix + "' title='" + $.fn.form.options.titulo +"'>" + sTabs + "</div>");
           $.fn.form.ajax($("#divFormGeneral_" + suffix));
           $("#btnGuardar_"+ suffix).click(function() {
                $("#form_" + suffix).submit();

           })

           if ($.fn.form.options.modo!='lookup')
                $.fn.form.ajax_profiles($("#divFormProfiles_" + suffix));

           $("#dlgModal_"+ suffix).dialog({modal: true,
                                           /*height:$.fn.form.options.height, */
                                           top:$.fn.form.options.top,
                                           width:$.fn.form.options.width,
                                           close: function(event, ui) {
                                                $(this).dialog("destroy");
                                                $(this).remove();
                                           }
                                   });
                                      
           $("#formTab_" + suffix).tabs();

        });
 
    };

    $.fn.form.ajax_profiles =function(obj){
        $.ajax({
            url:'srvGrid?$cf=5&$dp=body&$w=a.clave_aplicacion='+$.fn.form.options.aplicacion,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xml1 = new ActiveXObject("Microsoft.XMLDOM");
                    xml1.async = false;
                    xml1.validateOnParse="true";
                    xml1.loadXML(data);
                    if (xml1.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}
                }
                else {
                    xml1 = data;}
               var s="<form id='frmPerfiles_" + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk + "'><table>";
               var oRows=$(xml1).find("row");
               oRows.each( function() {
                   oCell=$(this).find("cell");
                   s+="<tr><td><img src='http://localhost:8088/ProyILCE/img/perfiles10.png'></td>" +
                           "<td class='etiqueta_perfil'><input type='checkbox' name='clave_perfil' value='" +  $(oCell[0]).text()+ "'" + (($(oCell[1]).text()=="Administrador")?" checked='checked'":"") + " />" + $(oCell[1]).text() + "</td>"+
                      "</tr>";
               })
               s+="</table></form>";
               obj.html(s);
            }
        });
    }

    $.fn.form.ajax = function(obj){
        $.ajax(
        {
            url: $.fn.form.options.xmlUrl + "?$cf=" + $.fn.form.options.forma + "&$pk=" + $.fn.form.options.pk + "&$ta=" + $.fn.form.options.modo +"&1=clave_aplicacion=" + $.fn.form.options.pk,
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
                obj.html($.fn.form.handleForm(xml));

                //Crea clave unica para forma
                var formSuffix =$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
                oForm=$("#form_" + formSuffix);

                // Se ocultan los mensajes de validación
                oForm.find('.obligatorio').each(function() {
                         $("#msgvalida_" + this.name).hide();
                    });

                //Se activa el datepicker para los campos con seudoclase fecha
                oForm.find('.fecha').datepicker( {dateFormat: 'dd/mm/yy',
                                                   dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                                                   monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']});

               //Se activa el foreign toolbar para editar registros foraneos
               oForm.find('.foreign_toolbar').fieldtoolbar({app:$.fn.form.options.aplicacion});

                //Se captura el submit
                oForm.submit(function() {
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
                                    bCompleto=false;}
                                else {
                                    $("#td_" + this.name).removeClass("errorencampo")
                                    $("#msgvalida_" + this.name).hide();
                                    $(this).removeClass("errorencampo");}
                            });

                            if (!bCompleto) return false;

                            //Preparando la información para enviarla via POST
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
                                success: function(data){
                                    if (data=='0') {
                                        alert('Error al insertar registro');
                                        return false;
                                    }
                                    //Envía perfiles asociados a la forma
                                    oCampos=$("#frmPerfiles_" + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk ).serializeArray();
                                    sData="";
                                    sNombreCampo='';
                                    $.each(oCampos, function(i,oCampo){
                                        sData+=oCampo.name+"="+oCampo.value + "&clave_aplicacion=" + data + "&activo=1&$cf=10&$pk=0&$ta="+ $.fn.form.options.modo;
                                        $.post(sWS, sData);
                                        sData="";
                                    });

                                   //Cierra el dialogo
                                   var suffix=$.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk
                                   $("#dlgModal_"+ suffix).dialog("destroy");
                                   $("#dlgModal_"+ suffix).remove();

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
                                alert("Es necesario especificar al menos un criterio de b&uacute;squeda, verifique");}
                            else {
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

                                    oGridHeader=$("span.ui-jqgrid-title, #grid_" + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma);
                                    nAplicacion=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                                    nForma=oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                                    $(oGridHeader[0]).append("&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nForma +"'>(Quitar filtro)</a>");

                                    //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                    $("#lnkRemoveFilter_grid_" + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma).click(function() {
                                        var sGridId="#grid_" + this.id.split("_")[2] + "_" + + this.id.split("_")[3];
                                        $(sGridId).jqGrid('setGridParam',{url:"srvGrid?$cf=" + $.fn.form.options.forma + "&$dp=body"}).trigger("reloadGrid")
                                        $(this).remove();
                                    });
                                    
                                    // Aqui va método del accordion para actualizarlo
                                    $("#apps_menu").appmenu().appmenu.getSearchs($.fn.form.options.aplicacion)

                                }

                                sData = sData.substring(0,sData.length-1)
                                $("#grid_" + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma).jqGrid('setGridParam',{url:"srvGrid?$cf=" + $.fn.form.options.forma + "&$w=" + escape(sData)+ "&$dp=body"}).trigger("reloadGrid")
                                $("#dlgModal_"+ formSuffix).dialog("destroy");
                                $("#dlgModal_"+ formSuffix).remove();
                            }
                        }

                        return false;

                    });


            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
        });
    };

    $.fn.form.handleForm = function(xml){
        var sRenglon='';
        var nFormaForanea=0;
        var nApp=$.fn.form.options.aplicacion;
        var sSuffix= '_' + $.fn.form.options.aplicacion  + '_' + $.fn.form.options.forma + "_" + $.fn.form.options.pk;;
        var oCampos= $(xml).find("registro").children();
        var tabIndex=1;
        var bAutoIncrement=false;
        oCampos.each(function(){
            oCampo=$(this);
            sTipoCampo= oCampo.attr("tipo_dato").toLowerCase();
            bAutoIncrement=(oCampo.attr("autoincrement")!=undefined)?true:false;
            //Genera etiqueta
            sAlias= oCampo.find('alias_campo').text();

            if (bAutoIncrement) return true;

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
                sRenglon+='<td class="etiqueta_forma"><select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ';
                
                if ($.fn.form.options.modo!="lookup" && nEditaForaneos=="true") {
                    sRenglon+='class="inputWidgeted'}
                else {
                    sRenglon+='class="singleInput'}                   

                //Establece seudoclase a select

                if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                    sRenglon+=' obligatorio" '}
                else {
                    sRenglon+='" '}


                sRenglon+='id="' + oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" >';
                sRenglon+="<option ";
                if ($.fn.form.options.modo=='insert')
                    sRenglon+="selected='selected'>";
                sRenglon +="</option>";

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
                        sRenglon+='class="singleInput obligatorio"'}
                    else {
                        sRenglon+='class="singleInput"'}

                    sRenglon += 'id="' + oCampo[0].nodeName + sSuffix + '" name="' +  oCampo[0].nodeName + sSuffix + '" ' +
                        oCampo.find('evento').text() +
                        '>' + oCampo[0].childNodes[0].data + '</textarea></td>|';
                }
                else if ($(this).find('tipo_control').text()!="") {
                    sRenglon += '<td class="etiqueta_forma">' +
                                '<input tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" id="'+ oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" ';

                    // Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="lookup" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput"';}
                    else {
                        sRenglon+='class="singleInput obligatorio"';}

                    sRenglon+=' type="' + oCampo.find('tipo_control').text() + '" value="' + oCampo[0].childNodes[0].data + '" ' +
                        oCampo.find('evento').text() +
                        ' /></td>|';
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
                        sRenglon+=" onBlur='javascript:check_number(this)'";}
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