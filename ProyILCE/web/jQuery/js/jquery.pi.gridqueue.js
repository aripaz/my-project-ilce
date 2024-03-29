/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.gridqueue = function(opc){

        $.fn.gridqueue.settings = {
            height:"400"
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
                 $("#divwait").dialog( "close" );
                 //$("#divwait").dialog("destroy");
             }
        });

 };

$.fn.gridqueue.getGridConfig= function(obj){

//Si está adentro de un tab, establece el tab actual
//Pasa el control al tab del grid para cálculo de ancho
//Devuelve el control al tab anterior

/*
 *                $('#tabUser').tabs( "select", "#tabFavoritos");  
                
 **/
    //$("#divwait").dialog("close");
    var restaura=false;
    var parentId= obj.parent().parent()[0].id;
    var tabIndex = $("#tabs").tabs('option', 'selected');
    var nWidth=obj.parent().width();
    if (nWidth==0) {
         /*restaura=true;
         $("#tabs").tabs('select',0); 
         $('#tabUser').tabs( "select", "#" + parentId);
         $('#'+parentId).tabs( "select", "#tabMisFavoritos_"+nClave);*/
        nWidth=$("#grid_1_101_0").width();
    } 
    
    obj.appgrid({app: obj.attr("app"),
          entidad: obj.attr("form"),
          wsParameters: obj.attr("wsParameters"),
          titulo:obj.attr("titulo"),
          leyendas:obj.attr("leyendas").split(","),
          inDesktop:obj.attr("indesktop"),
          height:"300",
          openKardex:obj.attr("openKardex"),
          removeGridTitle:true,
          showFilterLink:false,
          inQueue:true,
          insertInDesktopEnabled:0,
          editingApp:"1",
          width:nWidth
     });
     
     if (restaura) {
         $("#tabs").tabs('select',tabIndex); 
         $('#tabUser').tabs( "select", "#tabPendientes");
         $('#tabFavoritos').tabs( "select", 0);
     }
     
    //$("#divwait").dialog('open');    

}

})(jQuery);