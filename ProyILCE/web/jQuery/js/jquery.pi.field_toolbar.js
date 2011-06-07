/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.fieldtoolbar = function(opc){

        $.fn.fieldtoolbar.settings = {
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.fieldtoolbar.options = $.extend($.fn.fieldtoolbar.settings, opc);
             obj = $(this);
             /*$.fn.fieldtoolbar.options.form=$(this).attr("forma");
             $.fn.fieldtoolbar.options.control=$(this).attr("control");
             $.fn.fieldtoolbar.options.titulo_agregar=$(this).attr("titulo_agregar");
             $.fn.fieldtoolbar.options.titulo_editar=$(this).attr("titulo_editar");*/

             var suffix=obj[0].previousSibling.id;

             sHtml="<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                   "<td class='ui-pg-button ui-corner-all' title='Insertar registro'><div class='ui-pg-div'>" +
                   "<span id='spnInsrt" + suffix + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' class='ui-icon ui-icon-plus'></span></div></td>"  + //Botón de insertar
                   "<td class='ui-pg-button ui-corner-all' title='Editar registro'><div class='ui-pg-div'>" +
                   "<span id='spnUpdt" + suffix + "' control='" + obj.attr("control")+ "' forma='" + obj.attr("forma") + "' titulo_editar='" + obj.attr("titulo_editar") + "' class='ui-icon ui-icon-pencil'></span></div></td>" + // Botón de editar
                   "</tr></table>"
             obj.html(sHtml);

             $(".ui-pg-button").hover(
                 function () {
                    $(this).addClass('ui-state-hover');
                  },
                function () {
                    $(this).removeClass('ui-state-hover');
                }
             );


             $("#spnInsrt" + suffix).click(function(){
                $("body").form({aplicacion:  $.fn.fieldtoolbar.options.app,
                                forma: $(this).attr("forma"),
                                modo:"insert",
                                titulo: $(this).attr("titulo_agregar"),
                                columnas:1,
                                pk: 0,
                                height:"500",
                                width:"500"});
             })

             $("#spnUpdt" + suffix).click(function(){

                    nPK=$("#" + $(this).attr("control")+ " :selected").val();
                    if (nPK=="") {
                        alert('Seleccione un elemento de la lista para poder editarlo');
                        return;
                    }

                    $("body").form({aplicacion:  $.fn.fieldtoolbar.options.app,
                                forma:$(this).attr("forma") ,
                                modo:"update",
                                titulo: $(this).attr("titulo_editar"),
                                columnas:1,
                                pk: nPK,
                                height:"500",
                                width:"500"});

             })

             //$.fn.fieldtoolbar.ajax(obj);
        });

    };

})(jQuery);