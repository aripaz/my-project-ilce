/*
 * Plugin de jQuery para cargar arbol a partir de un webservice
 *
 */

( function($) {
    $.fn.treeMenu = function(opc){

        $.fn.treeMenu.settings = {
            xmlUrl : "srvFormaSearch",
            app: "",
            entidad:"",
            pk:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.treeMenu.options = $.extend($.fn.treeMenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.treeMenu.getTreeDefinition(this);
        });
    }

    $.fn.treeMenu.getTreeDefinition=function(obj) {
        $.ajax(
        {
            url: $.fn.treeMenu.options.xmlUrl + "?$cf=" + $.fn.treeMenu.options.entidad + "&$ta=relationship",
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlGT = new ActiveXObject("Microsoft.XMLDOM");
                    xmlGT.async = false;
                    xmlGT.validateOnParse="true";
                    xmlGT.loadXML(data);
                    if (xmlGT.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlGT.parseError.errorCode +"\nParse reason:" + xmlGT.parseError.reason + "\nLinea:" + xmlGT.parseError.line);
                }
                else
                    xmlGT = data;

                var sNodeParentId="";
                var oTypes = {
                    "max_depth" : -2,
                    "max_children" : -2,
                    "types":{}
                };

                oRegistros = $(xmlGT).find("registro")
                
                var sTypes="";
                var sXML="";
                oRegistros.each( function() {
                   sTypes+= '"'+$.trim($(this).find('tabla').text().replace('\n','')) + '":{"icon":{"image":"' + $.trim($(this).find('icono').text().replace('\n','')) + '"}},';
                   nClaveNodo= $.trim($(this).find('clave_forma').text().replace('\n',''));
                   sTabla=$.trim($(this).find('tabla').text().replace('\n',''));
                   sForma=$.trim($(this).find('forma').text().replace('\n','')).replace("&aacute;","á").replace("&eacute;","é").replace("&iacute;","í").replace("&oacute;","ó").replace("&uacute;","ú");
                   nClaveNodoPadre=$.trim($(this).find('clave_forma_padre').text().replace('\n',''));
                   sXML+="<item id='" + nClaveNodo + "' parent_id='" + nClaveNodoPadre + "' rel='" + sTabla +"' state='open'><content><name><![CDATA[" + sForma + "]]></name></content></item>";
                });

                sXML="<root>" + sXML + "</root>";
                sTypes="{"+sTypes.substring(0,sTypes.length-1)+"}";
                oTypes.types = $.parseJSON(sTypes);

                $(obj).jstree({
                    "plugins" : [ "themes", "contextmenu", "xml_data","types","ui" ],
                    "xml_data" : {
                        "data" : sXML
                    },
                    "themes" : {
                        "theme" : "default",
                        "dots" : true,
                        "icons": true
                    },
                    "types": oTypes
                    }
                );

                $("#" + obj.id+ " a").live("click", function(e) {
                  if (this.parentNode.parentNode.parentNode.id==obj.id)
                    return;
                
                    //Abre ajax con relationship de entidad foranea
                    $.ajax({
                            url: $.fn.treeMenu.options.xmlUrl + "?$cf=" + this.parentNode.id + "&$ta=relationship",
                            dataType: ($.browser.msie) ? "text" : "xml",
                            success:  function(data){
                                if (typeof data == "string") {
                                    xmlRel = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlRel.async = false;
                                    xmlRel.validateOnParse="true";
                                    xmlRel.loadXML(data);
                                    if (xmlRel.parseError.errorCode>0)
                                        alert("Error de compilación xml:" + xmlRel.parseError.errorCode +"\nParse reason:" + xmlRel.parseError.reason + "\nLinea:" + xmlGT.parseError.line);
                                }
                                else
                                    xmlRel = data;

                               //Crea html de tables para formar grids
                               var sHtml="";
                               var i=0;

                               $(xmlRel).find("registro").each(function() {
                                  nApp=$('clave_aplicacion:first',this).text().split("\n")[0];
                                  nForm=$('clave_forma:first',this).text().split("\n")[0];
                                  nRel=$('llave_primaria:first',this).text().split("\n")[0]+"="+nForm;
                                  sTitulo=$('forma:first',this).text().split("\n")[0];
                                  if  (i==0)
                                      sHtml+= "<div id='sGrid_" + nApp + "_" + nForm + "' app='" + nApp +"' form='" + nForm + "' rel='' titulo='" + sTitulo +"' class='foreign_grids'></div>";
                                  else
                                      sHtml+= "<div id='sGrid_" + nApp + "_" + nForm + "' app='" + nApp +"' form='" + nForm + "' rel='" +nRel + "' titulo='" + sTitulo +"' class='foreign_grids'></div>";
                                  i++;
                               });

                               $(obj.nextSibling).html(sHtml);

                               //Llama grids
                               oForeignGrids=$(".foreign_grids");
                               nSize=75/oForeignGrids.length;
                               oForeignGrids.foreign_grid({height:nSize+"%"});

                            },
                           error:function(xhr,err){
                                alert("Error al recuperar definición de relación de entidades foraneas\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                                alert("responseText: "+xhr.responseText);}
                    });
                 })
            },
            error:function(xhr,err){
                alert("Error al recuperar definición de arbol\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };
   
})(jQuery);
