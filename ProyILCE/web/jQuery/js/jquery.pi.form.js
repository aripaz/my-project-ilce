/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
( function($) {
    $.fn.form = function(opc){

        $.fn.form.settings = {
            titulo:"",
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
                    })
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

        var oCampos= $(xml).find("registro").children();
        oCampos.each(
        function(){
            oCampo=$(this);
            
            //Genera etiqueta
            if (oCampo.find('alias_campo').text()!='') {
                sRenglon += '<td class="etiqueta_forma">' + oCampo.find('alias_campo').text() + '</td>';
            }
            else {
                sRenglon += '<td class="etiqueta_forma">' + oCampo[0].nodeName + '</td>';
            }
            
            //Genera liga para forma foranea
            var nFormaForanea=$(this).find('foraneo').attr("clave_forma");
                        
            if (nFormaForanea!=undefined) {
                sRenglon+='<td><select ';
                
                if ($.fn.form.options.modo!="busqueda_avanzada") {
                    sRenglon+='class="inputWidgeted"' }
                else {
                    sRenglon+='class="singleInput"' }                   

                sRenglon+='id="' +  oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';
                sRenglon +="<option selected='selected'></option>";
                oCamposForaneos=oCampo.find('registro_' + oCampo[0].nodeName)
                
                oCamposForaneos.each(
                function(){
                    oCampoForaneo=$(this);
                    sRenglon +="<option value='" + oCampoForaneo.children()[0].textContent  +"' >" + oCampoForaneo.children()[1].textContent + "</option>";
                }
            )
                                
                sRenglon +='</select>';
                if ($.fn.form.options.modo!="busqueda_avanzada") {
                    sRenglon +="<img src='img/browse_catalog2.jpg' align='absbottom' onclick='alert(\"Funcionalidad por implementar\");' />";
                }
                
                sRenglon+='</td>|';
            }
            else {
                if (oCampo.find('tipo_control').text()=="textarea") {
                    sRenglon += '<textarea class="singleInput" id="' + oCampo[0].nodeName + '" name="' +  oCampo[0].nodeName + '" ' +
                        oCampo.find('evento').text() +
                        '>' + oCampo[0].childNodes[0].data + '</textarea></td>|';
                }
                else if ($(this).find('tipo_control').text()!="") {
                    sRenglon += '<td><input class="singleInput" type="' + oCampo.find('tipo_control').text() + '" value="' + oCampo[0].childNodes[0].data + '" ' +
                        oCampo.find('evento').text() +
                        ' /></td>|';
                }
                else {
                    sRenglon += '<td><input class="singleInput" type="text" value="' + oCampo[0].childNodes[0].data + '" ' +
                        oCampo.find('evento').text() +
                        ' /></td>|';
                }
            }
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
                sEncabezado+="<td colspan='2'>";
                sPie+="<td colspan='2'>";
                if ($.fn.form.options.modo=="busqueda_avanzada") {
                    sEncabezado+="<h3 class='searchtitle'>Busqueda avanzada</h3>";     }
                else  {
                    sEncabezado+="<h3 class='searchtitle'>" + $.fn.form.options.titulo + "</h3>"; }
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
                    sPie+="<div align='right'><input type='hidden' id='$cmd' name='$cmd' value='nuevo_registro'><button id='newRecord'>Guardar</button></div>";
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
        return "<table class='forma'>" + sForm + "</table>";
    }

})(jQuery);