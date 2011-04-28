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
            xmlUrl : "xml_tests/forma.app.xml?forma=",
            columnas: 2,
            modo:"edicion"
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.form.options = $.extend($.fn.form.settings, opc);
            obj = $(this);
            obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/loading.gif' /></div>")
            $.fn.form.ajax(obj);
        });
 
    };

    $.fn.form.ajax = function(obj){
        $.ajax(
        {
            url: $.fn.form.options.xmlUrl,
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

                //Se incorpora funcionalidad a los botones del modo de búsqueda
                if ($.fn.form.options.modo=="busqueda_avanzada") {
                    //Botón para cerrar formulario de búsqueda avanzada
                    $("#closeAdvancedSearch").click(function(){
                        $("#simple_search").slideToggle();
                        $("#advanced_search").slideToggle();
                        return false;
                    })
                }
                else{
                    //Crea clave unica para forma
                    oForm=$("#form_" + $.fn.form.options.aplicacion  + "_" + $.fn.form.options.forma);

                    // Se ocultan los mensajes de validación
                    oForm.find('.obligatorio').each(function() {
                         $("#msgvalida_" + this.name).hide();
                    });

                    //Se activa el datepicker para los campos con seudoclase fecha
                    oForm.find('.fecha').datepicker( { dateFormat: 'dd/mm/yy',
                                                       dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                                                       monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'] });

                    //Se captura el submit
                    oForm.submit(function() {
                        var bCompleto=true;
                        oForm.find('.obligatorio').each(function() {
                            if (this.   value.trim()=="") {
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
                        var sData="";
                        var oCampos = oForm.serializeArray();
                        jQuery.each(oCampos, function(i, oCampo){
                            sData+=oCampo.name.split("_")[0]+"="+oCampo.value;
                        });

                        var $tabs = $('#tabs').tabs();
                        $tabs.tabs( "remove", $tabs.tabs('option', 'selected') );
                        return false;

                        $.ajax({
                                type: "POST",
                                url: "insertEntity.jsp",
                                data: sData,
                                success: function(){
                                    //Cierra el tab y abre otro
                                    $tabs .tabs( "remove", index );
                                }
                            });

                        return false;
                    });

                    }

            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
        });
    };

    $.fn.form.handleForm = function(xml){
        var sRenglon='';
        var nFormaForanea=0;
        var sSuffix= '_' + $.fn.form.options.aplicacion  + '_' + $.fn.form.options.forma;
        var oCampos= $(xml).find("registro").children();
        var tabIndex=1;
        oCampos.each(
        function(){
            oCampo=$(this);
            sTipoCampo= oCampo.attr("tipo_dato").toLowerCase();
            //Genera etiqueta
            if (oCampo.find('alias_campo').text()!='') {
                sRenglon += '<td class="etiqueta_forma" id="td_' +oCampo[0].nodeName + sSuffix + '">' + oCampo.find('alias_campo').text();
            }
            else {
                sRenglon += '<td class="etiqueta_forma" id="td_' + oCampo[0].nodeName + sSuffix + '">' + oCampo[0].nodeName;
            }

            //Verifica si el campo es obligatorio para incluir la leyenda en el alias
            if ($.fn.form.options.modo!="busqueda_avanzada" && oCampo.find('obligatorio').text()=="1")  {
                sRenglon += ' (<span id="msgvalida_' + oCampo[0].nodeName + sSuffix + '">Obligatorio</span>*)</td>'
            }
            else {
                sRenglon += '</td>'
            }
            
            //Genera liga para forma foranea
            var nFormaForanea=$(this).find('foraneo').attr("clave_forma");
                        
            if (nFormaForanea!=undefined) {
                sRenglon+='<td class="etiqueta_forma"><select tabindex="' + tabIndex + '" ';
                
                if ($.fn.form.options.modo!="busqueda_avanzada") {
                    sRenglon+='class="inputWidgeted'}
                else {
                    sRenglon+='class="singleInput'}                   

                //Establece seudoclase a select

                if ($.fn.form.options.modo!="busqueda_avanzada" && oCampo.find('obligatorio').text()=="1")  {
                    sRenglon+=' obligatorio" '}
                else {
                    sRenglon+='" '}


                sRenglon+='id="' + oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" >';
                sRenglon +="<option selected='selected'></option>";
                oCamposForaneos=oCampo.find('registro_' + oCampo[0].nodeName)
                
                oCamposForaneos.each(
                function(){
                    oCampoForaneo=$(this);
                    sRenglon +="<option value='" + oCampoForaneo.children()[0].childNodes[0].data  +"' >" + oCampoForaneo.children()[1].childNodes[0].data + "</option>";
                }
            )
                                
                sRenglon +='</select>';
                if ($.fn.form.options.modo!="busqueda_avanzada") {
                    sRenglon +="<img src='img/browse_catalog2.jpg' align='absbottom' onclick='alert(\"Funcionalidad por implementar\");' />";
                }
                
                sRenglon+='</td>|';
            }
            else {
                if (oCampo.find('tipo_control').text()=="textarea" || sTipoCampo=="text") {
                    sRenglon+='<td class="etiqueta_forma">' +
                              '<textarea tabindex="' + tabIndex + '" ';
                    
                    //Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="busqueda_avanzada" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput obligatorio"'}
                    else {
                        sRenglon+='class="singleInput"'}

                    sRenglon += 'id="' + oCampo[0].nodeName + sSuffix + '" name="' +  oCampo[0].nodeName + sSuffix + '" ' +
                        oCampo.find('evento').text() +
                        '>' + oCampo[0].childNodes[0].data + '</textarea></td>|';
                }
                else if ($(this).find('tipo_control').text()!="") {
                    sRenglon += '<td class="etiqueta_forma">' +
                                '<input tabindex="' + tabIndex + '" id="'+ oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" ';

                    // Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo!="busqueda_avanzada" && oCampo.find('obligatorio').text()=="1")  {
                        sRenglon+='class="singleInput"';}
                    else {
                        sRenglon+='class="singleInput obligatorio"';}

                    sRenglon+=' type="' + oCampo.find('tipo_control').text() + '" value="' + oCampo[0].childNodes[0].data + '" ' +
                        oCampo.find('evento').text() +
                        ' /></td>|';
                }
                else {
                    sRenglon += '<td class="etiqueta_forma">' + 
                                '<input id="'+ oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '"' +
                                'tabindex="' + tabIndex + '" ' +
                                ' class="singleInput';
                    
                    if ($.fn.form.options.modo!="busqueda_avanzada" && oCampo.find('obligatorio').text()=="1")  
                        sRenglon +=' obligatorio"';

                    if (sTipoCampo=="date")
                        sRenglon +=' fecha ';

                    sRenglon +='" type="text" value="' + oCampo[0].childNodes[0].data + '" ' + oCampo.find('evento').text();

                    //Validación para inputs estandar de acuerdo al tipo de datos del campo
                    if (sTipoCampo=="integer") {
                        sRenglon+=" onBlur='javascript:check_number(this)'"; }
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

        
        var sEncabezado="";
        var sPie="";
        //Se multiplican las columnas por dos por que se agrega una columna separadora
        //para cada columna existente
        nCols=(nCols*2)+1

        for (i=1; i<=nCols; i++) {
            
            if (i==1) {
                sEncabezado+="<td colspan='2' class='etiqueta_forma'>";
                sPie+="<td colspan='2' class='etiqueta_forma'>";
                if ($.fn.form.options.modo=="busqueda_avanzada") {
                    sEncabezado+="<h3 class='searchtitle'>B&uacute;squeda avanzada</h3>";}
                else  {
                    sEncabezado+="<h3 class='searchtitle'>" + $.fn.form.options.titulo + "</h3>";}
            }
            else {
                sEncabezado+="<td>&nbsp;";
                sPie+="<td>";
            }
            sEncabezado+="</td>";


            if (i==nCols) {
                if ($.fn.form.options.modo=="busqueda_avanzada") {
                    sPie+="<div align='right'><input type='hidden' id='$cmd' name='$cmd' value='busqueda_avanzada'><button id='advancedSearch'>Buscar</button><button id='closeAdvancedSearch'>Cerrar</button></div>";
                }
                else if ($.fn.form.options.modo=="inserta_entidad") {
                    sPie+="<div align='right'><input type='hidden' id='$cmd' name='$cmd' value='nuevo_registro'><input type='submit' class='formButton'  value='Guardar' id='btnInsertEntity_" + $.fn.form.options.aplicacion  + "_" + $.fn.form.options.forma + "' /></div>";
                }
                else {

                }
            }
            else {
                sPie+="&nbsp;";
            }

            sPie+="</td>";
            if (i==1) {i++}
        }

        sForm="<tr>"+sEncabezado + "</tr>"+sForm+"<tr>"+sPie+"</tr>" ;

        //Llena la primer pestaña con la forma de la entidad principal
        if ($.fn.form.options.modo=="edita_entidad") {
            sForm="<br><br><form class='forma' id='form_"  + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "' name='form_"  + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "'><table class='forma'>" + sForm + "</table></form>"
        } else {
            sForm="<form class='forma' id='form_"  + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "' name='form_"  + $.fn.form.options.aplicacion + "_" + $.fn.form.options.forma + "'><table class='forma'>" + sForm + "</table></form>"
        }

        return sForm;
    }

})(jQuery);