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
            url: $.fn.treeMenu.options.xmlUrl + "?$cf=" + $.fn.treeMenu.options.entidad + "&$ta=children",
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
                  var sNodeId=this.parentNode.id;
                  var sTitulo=$.trim(this.text);
                  var nApp=sNodeId.split("-")[1];
                  var nForma=sNodeId.split("-")[2];
                  var sW=sNodeId.split("-").length>3?sNodeId.split("-")[3]:"";
                  
                  //Llama grids
                  if (nForma==undefined) return false;

                   $(obj.nextSibling).appgrid({app: nApp,
                      entidad: nForma,
                      wsParameters: sW,
                      titulo:sTitulo,
                      leyendas:["Nuevo registro", "Edición de registro"],
                      height:"75%"
                   });

                    });

            },
            error:function(xhr,err){
                alert("Error al recuperar definición de arbol\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };
   
})(jQuery);
