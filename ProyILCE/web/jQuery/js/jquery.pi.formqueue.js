/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.formqueue = function(opc){

        $.fn.formqueue.settings = {
            titulo:"",
            app:"",
            forma:"",
            pk:"",
            pk_name:"",
            xmlUrl : "srvForma", //"xml_tests/forma.app.xml",
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
            showRelationships:"false"
        };
           
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
          $.fn.form.options = $.extend($.fn.form.settings, opc);
          
          $("body").formqueue({
            app: nApp,
            forma:nEntidad,
            datestamp:$(this).attr("datestamp"),
            modo:"update",
            titulo: $.fn.appgrid.options.leyendas[1],
            columnas:1,
            pk:sResultado,
            filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
            height:"500",
            width:"500",
            originatingObject: $(this).id,
            showRelationships:$(this).attr("callFormWithRelationships")
        });    
        });

 };


})(jQuery);