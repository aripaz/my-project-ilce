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

            $( ".column" ).sortable({
			connectWith: ".column"
            });
            
            $( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
			.find( ".portlet-header" )
				.addClass( "ui-widget-header ui-corner-all" )
				.prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
				.end()
			.find( ".portlet-content" );

            $( ".portlet-header .ui-icon" ).click(function() {
			$( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
			$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
            });

            $( ".column" ).disableSelection();
             $.fn.desktop.ajax(obj);


        });

    };

    $.fn.desktop.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.desktop.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
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

                $.fn.desktop.handleSession(xmlConfig);
                $('.queued_grids:first').gridqueue();
                
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.desktop.handleSession = function(xml){
        var sFondo="";
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de configuración
            sParametro=$(this).find("parametro").text().split("\n")[0];

            if (sParametro=='escritorio.imagen de fondo') {
               sFondo=$(this).find("valor").text().split("\n")[0];
               if (sFondo!='')
                   obj.css('background-image', 'url('+sFondo+')');
            }

            if(sParametro==='escritorio.grid') {
                sValor=$(this).find("valor").text().split("\n")[0];
                nApp=sValor.split(",")[0].split(":")[1];
                nForm=sValor.split(",")[1].split(":")[1];
                wsParameters=sValor.split(",")[2].split(":")[1];
                titulo=sValor.split(",")[3].split(":")[1];
                leyendas=sValor.split(",")[4].split(":")[1].replace("/",",");
                openKardex=sValor.split(",")[5].split(":")[1];
                inDesktop=sValor.split(",")[6].split(":")[1];
                obj.append("<div class='queued_grids'" + 
                                     " id='divDesktopGrid_" + nApp + "_" + nForm + "' " +
                                     " app='" + nApp + "' " + 
                                     " form='" + nForm + "' " +
                                     " wsParameters='" + wsParameters + "' " +
                                     " titulo='" + titulo + "' " + 
                                     " leyendas='" +leyendas+ "' "  + 
                                     " openKardex='" + openKardex + "' " + 
                                     " inDesktop='" + inDesktop + "' " +
                                     " class='queued_grids'," +
                                     " editingApp='1',"+
                                     " insertInDesktopEnabled='0'></div>"+
                                     "<div class='desktopGridContainer' ><br>&nbsp;&nbsp;&nbsp;&nbsp;<br><br></div><br>"
                                 );
            }

        })
        
    }

})(jQuery);