/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.apptab = function(opc){

        $.fn.apptab.settings = {
            xmlUrl : "xml_tests/widget.tabs.xml?entidad=",
            entidad:"",
            pk:"",
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
            $.fn.apptab.options = $.extend($.fn.apptab.settings, opc);
            obj = $(this);
            obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/loading.gif' /></div>")
            $.fn.apptab.getForeignTabs(obj);
        });

    };

     $.fn.apptab.getForeignTabs = function(obj){
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
                            /*Aqui se debe carga el grid
                             *Primero se verifica si existe el elemento HTML
                             *Si existe quiere decir que ya está cargado el grid
                             *con sus grids foraneos
                             */

                            if ($("#grid" + ui).length==0) {
                               $.fn.apptab.getForeignGrids(fn.apptab.options.entidad);
                                 $("#grid" +  this.id).jqGrid(
                                 {url:"xml_tests/grid.body.xml?entidad=" + this.id.split("_")[1],
                                  datatype: "xml",
                                  /* Parte dinámica */
                                  colNames:oColNames,
                                  colModel:oColModel,
                                  rowNum:20,
                                  autowidth: true,
                                  rowList:[10,20,30],
                                  pager: jQuery('#pager' + nEntidad),
                                  sortname:  oSortName,
                                  viewrecords: true,
                                  sortorder: "desc",
                                  ondblClickRow: function(id){
                                      //Verifica si ya está abierto el tab en modo de edición
                                      if ($("#tabEditEntity"+id).length) {
                                           $tabs.tabs( "select", "#tabEditEntity"+id);
                                      }
                                      else {
                                            sTabTitulo=this.p.colNames[1] + ' ' + this.rows[id].cells[1].innerHTML;
                                            $tabs.tabs( "add", "#tabEditEntity"+id, sTabTitulo);
                                            $tabs.tabs( "select", "#tabEditEntity"+id);
                                            $("#tabEditEntity"+id).apptab({
                                                entidad:id,
                                                app:nAplicacion
                                            });

                                      }
                                    },
                                    caption:sTitulo}).navGrid('#pager' + this.id,{edit:false,add:false,del:false});
                            }


                        }}
                );

                //Inicializa forma de edición en la primera página
                $("#tabEntity_" + $.fn.apptab.options.app + "_" + $.fn.apptab.options.entidad+"_1").form({
                        aplicacion: $.fn.apptab.options.app,
                        forma:$.fn.apptab.options.entidad,
                        pk:$.fn.apptab.options.pk,
                        modo:"update",
                        titulo: "Datos generales"
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

    $.fn.apptab.getForeignGrids=function (nForm) {
        $.ajax(
            {
            url: $.fn.apptab.options.xmlUrl + "?nForm=" + nForm,
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
               $.fn.apptab.handleForeignGrids(xml);
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    }
    
    $.fn.apptab.handleForeignGrids=function(xml) {
        $(xml).find("clave_forma").each(function() {
            s='<div id="gridContainer' + this.value + '"><table width="100%" id="grid' + this.id + '">' +
                                        '</table><div id="pager' + this.id +'"></div></div>'
        });
    }

})(jQuery);

