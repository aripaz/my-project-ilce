/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.gridqueue = function(opc){

        $.fn.gridqueue.settings = {
            height:"100%"
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.gridqueue.options = $.extend($.fn.gridqueue.settings, opc);
             obj = $(this);
             obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>")
             $.fn.gridqueue.getGridConfig(obj);
             obj.removeClass("foreign_grids");
             obj.addClass("gridContainer");
        });

 };

$.fn.gridqueue.getGridConfig= function(obj){
      obj.appgrid({app: obj.attr("app"),
          entidad: obj.attr("form"),
          wsParameters: obj.attr("rel"),
          titulo:obj.attr("titulo"),
          leyendas:["Nuevo registro", "Edición de registro"],
          height:$.fn.gridqueue.options.height,
          removeGridTitle:true,
          inQueue:true
     });

}

})(jQuery);