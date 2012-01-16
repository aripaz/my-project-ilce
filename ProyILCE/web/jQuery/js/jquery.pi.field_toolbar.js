/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    
    //Liz    
    $("#td_motivo_cancelacion").parent().hide();

    $('#clave_estatus_proyecto').change(function() {
    if ($("#clave_estatus_proyecto").val()==6) { 
           $("#motivo_cancelacion").addClass("obligatorio");
           $("#td_motivo_cancelacion").parent().show();  }
   else {
           $("#td_motivo_cancelacion").parent().hide() }
    });
    
    //Dirección general
    $("#td_motivo_cancelacion").parent().hide();

    $('#clave_estatus_proyecto').change(function() {
    if ($("#clave_estatus_proyecto").val()==6) { 
           $("#motivo_cancelacion").addClass("obligatorio");
           $("#td_motivo_cancelacion").parent().show();  }
    else {
           if ($("#clave_estatus_proyecto").val()==10) {
           $("#td_motivo_cancelacion").parent().show();  
            }
           else
           $("#td_motivo_cancelacion").parent().hide() }
    });  
    
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
             sTipoBoton=obj.attr("tipo")

             if (sTipoBoton=="foreign_toolbar") {
                 sHtml="<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                       "<td class='ui-pg-button ui-corner-all' title='Insertar registro'><div class='ui-pg-div'>" +
                       "<span id='spnInsrt" + suffix + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' class='ui-icon ui-icon-plus'></span></div></td>"  + //Botón de insertar
                       "<td class='ui-pg-button ui-corner-all' title='Editar registro'><div class='ui-pg-div'>" +
                       "<span id='spnUpdt" + suffix + "' control='" + obj.attr("control")+ "' forma='" + obj.attr("forma") + "' titulo_editar='" + obj.attr("titulo_editar") + "' class='ui-icon ui-icon-pencil'></span></div></td>" + // Botón de editar
                       "</tr></table>"
             }

            if (sTipoBoton=="calendar_button") {
                 sHtml="<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                       "<td class='ui-pg-button ui-corner-all' title='Muestra calendario'><div class='ui-pg-div'>" +
                       "<span id='spnCalendar" + suffix + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' control='" + obj.attr("control")+ "' class='ui-icon ui-icon-calendar'></span></div></td>"  + //Botón de calendario
                       "</tr></table>"
             }

            if (sTipoBoton=="calculator_button") {
                 sHtml="<div id='div_spnCalculator" + suffix + "' >"+
                       "<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                       "<td class='ui-pg-button ui-corner-all' title='Muestra calculadora'><div class='ui-pg-div'>" +
                       "<span id='spnCalculator" + suffix + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' control='" + obj.attr("control")+ "' class='ui-icon ui-icon-calculator calculator-trigger'></span></div></td>"  + //Botón de insertar
                       "</tr></table>"
             }

             obj.html(sHtml);

             $(".ui-pg-button").hover(
                 function () {
                    $(this).addClass('ui-state-hover');
                  },
                function () {
                    $(this).removeClass('ui-state-hover');
                }
             );

            if (sTipoBoton=="foreign_toolbar") {

             $("#spnInsrt" + suffix).click(function(){
                $("body").form({app:  $.fn.fieldtoolbar.options.app,
                                forma: $(this).attr("forma"),
                                modo:"insert",
                                titulo: $(this).attr("titulo_agregar"),
                                columnas:1,
                                pk: 0,
                                height:400,
                                width:"80%",
                                updateControl:suffix,
                                updateForeignForm:$(this).attr("forma"),
                                originatingObject:$(this).id

                            });
             })

             $("#spnUpdt" + suffix).click(function(){

                    nPK=$("#" + $(this).attr("control")+ " :selected").val();
                    if (nPK=="") {
                        alert('Seleccione un elemento de la lista para poder editarlo');
                        return;
                    }

                    $("body").form({app:  $.fn.fieldtoolbar.options.app,
                                forma:$(this).attr("forma") ,
                                modo:"update",
                                titulo: $(this).attr("titulo_editar"),
                                columnas:1,
                                pk: nPK,
                                height:400,
                                width:"80%",
                                updateControl:suffix,
                                updateForeignForm:$(this).attr("forma"),
                                originatingObject:$(this).id
                            });

             })
            }

            if (sTipoBoton=="calendar_button") {
            //Se activa el datepicker para los campos con seudoclase fecha
                $("#spnCalendar" + suffix).click(function(){
                    $("#"+$(this).attr("control")).datepicker('show');
                });
            }

            if (sTipoBoton=="calculator_button") {
            //Se activa el datepicker para los campos con seudoclase fecha
                $("#spnCalculator" + suffix).click(function(){
                    $("#"+$(this).attr("control")).calculator('show');
                });
            }
        });

    };

})(jQuery);