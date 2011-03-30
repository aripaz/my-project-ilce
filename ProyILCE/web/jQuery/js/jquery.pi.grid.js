/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.appgrid = function(opc){

        $.fn.appgrid.settings = {
            xmlUrl : "xml_tests/grid.header.xml?entidad=",
            titulo:"",
            entidad:"",
            colNames: [],
            colModel: [{}],
            sortname:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html('<div id="divGrid' + $.fn.appgrid.options.entidad +'"><table width="365" id="grid' +  $.fn.appgrid.options.entidad + '"></table><div id="pager' + $.fn.appgrid.options.entidad +'"></div></div>');

             $.fn.appgrid.ajax(obj);
        });

    };

    $.fn.appgrid.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.appgrid.options.xmlUrl,
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
                obj.html($.fn.appgrid.handleAccordion(xml));
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.appgrid.handleGrid = function(xml){
            $("#grid" + $.fn.appgrid.options.entidad).jqGrid(
            {url:"xml_tests/grid.body.xml?entidad=" + $.fn.appgrid.options.entidad,
            datatype: "xml",
            /* Parte dinámica */
            colNames:$.fn.appgrid.options.colNames,
            colModel:$.fn.appgrid.options.colModel,
            rowNum:10,
            autowidth: true,
            rowList:[10,20,30],
            pager: jQuery('#pager' + $.fn.appgrid.options.entidad),
            sortname:  $.fn.appgrid.options.sortname,
            viewrecords: true,
            sortorder: "desc",
            ondblClickRow: function(id){
               $( "#dialog" ).dialog({
                autoOpen: true,
                height: 500,
                width: 650,
		modal: true});
                //handleXML('tabs.xml?entity=' + nEntidad,createTabs);
                //handleDialog(nEntidad, handleTabs)
                //handleTabs(oParentDiv, nEntidad)

            },
            caption:"Aplicaciones"}).navGrid('#pager',{edit:false,add:false,del:false});
    }
/*
        var i=0;
        var aInsertar ={};
        var aMostrar={};

        $(xml).find("registro").each(function(){
            var nEntidad=$(this).find("clave_forma").text();
            var sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            var sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            if ($(this).find("insertar").text()=="1") {
                aInsertar={etiqueta: sAliasNuevaEntidad,
                           entidad: nEntidad,
                           funcion:"insertar"};
            }

            if ($(this).find("mostrar").text()=="1") {
                aMostrar={etiqueta:sAliasMostrarEntidad,
                           entidad:nEntidad,
                           funcion:"mostrar"};
            }

            $.fn.appgrid.options.menu[i]={aplicacion:$(this).find("aplicacion").text(), elementos_menu:[aInsertar,aMostrar]};
            i++;
        })

        var sHtml=""

        //Construye menu de acuerdo a configuración recuperada
        for (i=0;i<$.fn.appgrid.options.menu.length;i++) {
            sHtml+="<h3><a href='#' >" + $.fn.appgrid.options.menu[i].aplicacion + "</a></h3>" +
                "<div>";
            for (var k=0;k<$.fn.appgrid.options.menu[i].elementos_menu.length;k++) {
                if ($.fn.appgrid.options.menu[i].elementos_menu[k].funcion=="insertar") {
                    tipoliga="nuevaEntidad";
                }
                else {
                    tipoliga="mostrarEntidad";
                }

                sHtml+="<div><a href='#' id='" + tipoliga+$.fn.appgrid.options.menu[i].elementos_menu[k].entidad + "'>"+$.fn.appgrid.options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            sHtml+="</div>"
        }
        return sHtml;
    }
*/
})(jQuery);