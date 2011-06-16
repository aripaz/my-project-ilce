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

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.treeMenu.options = $.extend($.fn.treeMenu.settings, opc);
            $.fn.treeMenu.getTreeDefinition(this);
        });
    }

    $.fn.treeMenu.getTreeDefinition=function(o) {
        $.ajax(
        {
            url: $.fn.treeMenu.options.xmlUrl + "?$cf=" + $.fn.treeMenu.options.entidad + "&$pk=" + $.fn.treeMenu.options.pk + "&$ta=children",
            dataType: ($.browser.msie) ? "text" : "xml",
            contentType: "application/x-www-form-urlencoded",
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
                //Se define la estructura del árbol de acuerdo a la aplicación
                var sXML="";

                //Se reemplaza el parámetro de la aplicación
                
                oRegistros.each( function() {
                   sTypes+= '"'+$.trim($(this).find('rel').text().replace('\n','')) + '":{"icon":{"image":"' + $.trim($(this).find('icono').text().replace('\n','')) + '"}},';
                   nClaveNodo= $.trim($(this).find('clave_nodo').text().replace('\n',''));
                   sRel=$.trim($(this).find('rel').text().replace('\n',''));
                   sTextoNodo=$.trim($(this).find('texto_nodo').text().replace('\n','')).replace("&aacute;","á").replace("&eacute;","é").replace("&iacute;","í").replace("&oacute;","ó").replace("&uacute;","ú");
                   nClaveNodoPadre=$.trim($(this).find('clave_nodo_padre').text().replace('\n',''));
                   if (sRel=='aplicacion')
                       sState='open'
                   else
                       sState='closed'

                   sXML+="<item id='" + nClaveNodo + "' parent_id='" + nClaveNodoPadre + "' rel='" + sRel +"' state='" + sState + "'><content><name><![CDATA[" + sTextoNodo + "]]></name></content></item>";
                });

                sXML="<root>" + sXML + "</root>";
                sTypes="{"+sTypes.substring(0,sTypes.length-1)+"}";
                oTypes.types = $.parseJSON(sTypes);

                if ($(o).attr("behaviour")=='kardex')
                    aPlugins="themes,contextmenu,xml_data,types,ui".split(",");
                else
                    aPlugins="themes,contextmenu,xml_data,types,ui,checkbox".split(",");
                
                $(o).jstree({
                    "plugins" : aPlugins,
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

                if ($(o).attr("behaviour")=='profile' && $.fn.treeMenu.options.pk>0)
                   $.fn.treeMenu.getAppProfiles(o);
                else if ($(o).attr("behaviour")=='profile')
                    $(o).jstree('check_node', $('#perfil-1'));
                    
               
                $("#" + o.id+ " a").live("click", function(e) {
                   if ($(o).attr("behaviour")=='kardex') {
                      var sNodeId=this.parentNode.id;
                      var sTitulo=$.trim(this.text);
                      var nApp=sNodeId.split("-")[1];
                      var nForma=sNodeId.split("-")[2];
                      var sW=sNodeId.split("-").length>3?sNodeId.split("-")[3]:"";

                      //Llama grids
                      if (nForma==undefined) return false;

                       $(o.nextSibling).appgrid({app: nApp,
                          entidad: nForma,
                          wsParameters: sW,
                          titulo:sTitulo,
                          showFilterLink:false,
                          inQueue:false,
                          leyendas:["Nuevo registro", "Edición de registro"],
                          height:"75%"
                       });
                    }

                    if (($(o).attr("behaviour")=='profile')) {
                        if (this.parentNode.id=='perfil-1'){
                            if(!$.jstree._reference('#' + o.id).is_checked ('#perfil-1')) {
                                alert ('No es posible quitar acceso al Administrador');
                                $.jstree._reference('#' + o.id).check_node('#perfil-1');
                            }
                        }
                    }
                 });

            },
            error:function(xhr,err){
                alert("Error al recuperar definición de arbol\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
 };

$.fn.treeMenu.getAppProfiles=function(o) {
        if ($(o).attr("originalForm")=="2")
            nCF=10;
        else
            nCF=14;

        $.ajax(
        {
            url: $.fn.treeMenu.options.xmlUrl + "?$cf="+nCF+"&$pk=" + $.fn.treeMenu.options.pk + "&$ta=children",
            dataType: ($.browser.msie) ? "text" : "xml",
            contentType: "application/x-www-form-urlencoded",
            success:  function(data){
                if (typeof data == "string") {
                    xmlAP = new ActiveXObject("Microsoft.XMLDOM");
                    xmlAP.async = false;
                    xmlAP.validateOnParse="true";
                    xmlAP.loadXML(data);
                    if (xmlAP.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlAP.parseError.errorCode +"\nParse reason:" + xmlAP.parseError.reason + "\nLinea:" + xmlAP.parseError.line);
                }
                else
                    xmlAP = data;

                // Recorre el arbol y va marcando los registros que encuentra
                $(o).find('li.jstree-unchecked').each(function(){
                   sClaveNodo=this.id;
                   nClaveRegistro=this.id.split("-")[1];
                   sTipoNodo=$(this).attr("rel");
                   sClaveNodoHijo=this.id.split("-")[2];
                   
                   if ($(o).attr("originalForm")=="2"){   // Forma aplicacion
                        oRegistros=$(xmlAP).find('registro');
                        oRegistros.each( function() {
                           nClaveP=oRegistros.find('clave_perfil').text().split("\n")[0];
                            if (nClaveRegistro==nClaveP && sTipoNodo=='Perfiles') {
                                $.jstree._reference('#' + o.id).check_node('#' + sClaveNodo);
                                return false;
                            }

                        });

                   }
                   else {
                        if (sClaveNodoHijo==undefined)
                            return true;

                        oRegistros=$(xmlAP).find('registro');
                        oRegistros.each( function() {
                            nPerfil=$(this).find('clave_perfil').text().split("\n")[0]; //clave_permiso
                            nPermiso=$(this).find('clave_permiso').text().split("\n")[0]; //clave_permiso
                            
                            if (nClaveRegistro==nPerfil && nPermiso==sClaveNodoHijo && sTipoNodo=='permiso') {
                                $.jstree._reference('#' + o.id).check_node('#' + sClaveNodo);
                                return false;
                            }
                        });


                   }
                });
            },
            error:function(xhr,err){
                alert("Error al recuperar definición de arbol\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
 };
})(jQuery);
