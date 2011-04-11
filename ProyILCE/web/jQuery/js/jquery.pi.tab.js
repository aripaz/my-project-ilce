/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.apptab = function(opc){

        $.fn.apptab.settings = {
            xmlUrl : "xml_tests/widget.tabs.xml?entidad=",
            entidad:"",
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
            $.fn.apptab.options = $.extend($.fn.apptab.settings, opc);
            obj = $(this);
            obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/loading.gif' /></div>")
            $.fn.apptab.ajax(obj);
        });

    };

     $.fn.apptab.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.apptab.options.xmlUrl,
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
                obj.html($.fn.apptab.handleTab(xml));
                var $entityTab = $("#tabEntity_" + $.fn.apptab.options.app + "_" + $.fn.apptab.options.entidad).tabs({
                        select: function(event, ui) {
                            //Aqui se debe carga el grid
                        }}
                );

                //Inicializa forma de edición en la primera página
                $("#tabEntity_" + $.fn.apptab.options.app + "_" + $.fn.apptab.options.entidad+"_1").form({
                        forma:1,
                        modo:"edita_entidad",
                        titulo: "Edici&oacute;n"
                });

            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.apptab.handleTab = function(xml){

        var sUl='<ul>';
        var sDivs='';
        i=1;
        $(xml).find("alias_tab").each(function(){
            //Carga los datos del xml en la variable de configuración
            sUl+='<li><a href="#tabEntity_' + $.fn.apptab.options.app + '_' + $.fn.apptab.options.entidad + '_' + i + '">' + this.childNodes[0].data + '</a></li>';
            sDivs+='<div id="tabEntity_' + $.fn.apptab.options.app + '_' + $.fn.apptab.options.entidad + '_' + i + '"></div>';
            i++;
        });

        //Construye html de acuerdo a configuración recuperada
        var sHtml =
         '<div id="tabEntity_' + $.fn.apptab.options.app + '_' + $.fn.apptab.options.entidad + '">' +
         sUl + sDivs + '</div>';

        return sHtml
    }

})(jQuery);

