/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.desktop = function(opc){

        $.fn.desktop.settings = {
            xmlUrl : "srvFormaSearch?$cf=1&$ta=select&$w=" + escape("c.clave_empleado=" +$("#_ce_").val()+ " AND c.parametro like 'escritorio.%'")
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.desktop.options = $.extend($.fn.desktop.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.desktop.ajax(obj);
        });

    };

    $.fn.desktop.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.desktop.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            contentType: "application/x-www-form-urlencoded",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlConfig = new ActiveXObject("Microsoft.XMLDOM");
                 xmlConfig.async = false;
                 xmlConfig.validateOnParse="true";
                 xmlConfig.loadXML(data);
                 if (xmlConfig.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xmlConfig.parseError.errorCode +"\nParse reason:" + xmlConfig.parseError.reason + "\nLinea:" + xmlConfig.parseError.line);}
                }
                 else {
                    xmlConfig= data;}
                obj.html($.fn.desktop.handleSession(xmlConfig));

            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.desktop.handleSession = function(xml){
        var sFondo="";
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de coniguración
            sParametro=$(this).find("parametro").text().split("\n")[0];
            if (sParametro=='escritorio.imagen de fondo') {
               sFondo=$(this).find("valor").text().split("\n")[0];
               if (sFondo!='')
                   obj.css('background-image', 'url('+sFondo+')');
                   obj.html("");
            }


        })

    }

})(jQuery);