/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.gridqueue = function(opc){

        $.fn.gridqueue.settings = {
            height:"250px"
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.gridqueue.options = $.extend($.fn.gridqueue.settings, opc);
             obj = $(this);
             obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>")
             $.fn.gridqueue.getGridConfig(obj);
             obj.removeClass("queued_grids");
             obj.addClass("desktopGridContainer");
             
             //Verifica si hay grids pendientes en la cola y destruye el dialogo de espera
             //si este es el caso
             if ($(".queued_grids").length==0) {
                 $("#divwait").dialog( "close" )
                 $("#divwait").dialog("destroy");
             }
        });

 };

$.fn.gridqueue.getGridConfig= function(obj){
      obj.appgrid({app: obj.attr("app"),
          entidad: obj.attr("form"),
          wsParameters: obj.attr("wsParameters"),
          titulo:obj.attr("titulo"),
          leyendas:obj.attr("leyendas").split(","),
          inDesktop:obj.attr("indesktop"),
          height:$.fn.gridqueue.options.height,
          openKardex:obj.attr("openKardex"),
          removeGridTitle:true,
          showFilterLink:false,
          inQueue:true,
          insertInDesktopEnabled:0,
          editingApp:"1"
     });

}

})(jQuery);