/*
 * jQuery app
 *
 * Depends:
 *      jquery.ui.button.js
 *
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *      jquery.ui.button.js
 *	jquery.ui.draggable.js
 *	jquery.ui.mouse.js
 *	jquery.ui.position.js
 *	jquery.ui.resizable.js
 */

(function($){

var methods = {
    init : function( options ) { // Llena los options desde XML
        //Crea páneles
     	$("#splitterContainer").splitter({minAsize:100,maxAsize:300,splitVertical:true,A:$('#leftPane'),B:$('#rightPane'),slave:$("#rightSplitterContainer"),closeableto:0});
	$("#rightSplitterContainer").splitter({splitHorizontal:true,A:$('#rightTopPane'),B:$('#rightBottomPane'),closeableto:100});
        //Carga estructura JSON a través de ajax, tomando el webservice con el perfil como parámetro
        options.accordion.oDomElement = "#app_menu";
        options.accordion.xml= "xml_tests/accordion.xml?perfil="+ options.perfil.clave;
        this._handleAccordion();
    },
    showGrid : function( ) { // IS
    },
    showInsertEntityDialog : function( ) { // GOOD
    },
    showUpdateEntityDialog : function( ) { // GOOD
    },
    _ajax: function(sXMLUrl) {
        $.ajax(
            {
            url: sXMLUrl,
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

                return xml;
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });

    },
    _handleAccordion: function  () {
        var xml=this._ajax(options.accordion.xml);
        var i=0;
        $(xml).find("registro").each(function(){
            options.accordion.menu[i].aplicacion=$(this).find("aplicacion").text();

            if ($(this).find("insertar").text()="1") {
                options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_nueva_entidad").text(),entidad:$(this).find("alias_menu_nueva_entidad").text(),funcion:"insertar"}];
            }

            if ($(this).find("mostrar").text()="1") {
                options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_mostrar_entidad").text(),funcion:"mostrar"}];
            }
            
            i++;
        })
        
        //Construye menu de acuerdo a configuración recuperada
        for (i=0;i<options.accordion.menu.length;i++) {
            sHtml="<h3><a href='#' >" + options.accordion.menu[i].aplicacion + "</a></h3>" +
                "<div>";
            for (var k=0;k<options.accordion.menu[i].elementos_menu.length;k++) {
                if (options.accordion.menu[i].elementos_menu[k].funcion=="insertar") {
                    tipoliga="nuevaEntidad";
                }
                else {
                    tipoliga="mostrarEntidad";
                }

                sHtml+="<div><a href='#' id='" + tipoliga+options.accordion.menu[i].elementos_menu[k].entidad + "'>"+options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
            }
            sHtml+="</div>"
        }

       $(options.accordion.oDomElement).append(sHtml);

       $(options.accordion.oDomElement).accordion({
                active: false,
                autoHeight: false,
                collapsible: true,
                change: function() {
                  $(this).find('h3').blur();
                }
       });

       /* Establece los eventos para los links del menú
       $('#nuevaEntidad' + sClaveForma).click(function() {
            alert('Por implementar');
            //handleDialog('configuracion.xml', [2,'Configuraci&oacute;n', 'grid']);
        });

       //Establece los eventos para los links del menú
       $('#muestraEntidad' + sClaveForma).click(function() {
            alert('Por implementar');
            //handleDialog('configuracion.xml', [2,'Configuraci&oacute;n', 'grid']);
        }); */
    },
    update : function( content ) { // !!!
    }
 };

 
  $.app = function(options) {
        var settings ={
            aplicacion: {clave:1,
                        nombre:"BackEnd",
                        entidad:1},
            dialogo:    {titulo:  "Aplicaciones",
                         oDomElement:""},
            grid:       {titulo:  "Aplicaciones",
                         xml:     "xml_tests/grid.app.xlm",
                         colNames: ["Clave","Aplicación", "Estatus"],
                         colModel: [{name:'clave_aplicacion',index:'clave_aplicacion', width:5},
                                  {name:'aplicacion',index:'aplicacion', width:15},
                                  {name:'estatus',index:'estatus', width:80}],
                         sortname:"",
                         oDomElement:""
                            },
            form:       {xml: "xml_tests/form.app.xml",
                         oDomElement:""},
            tab:        {xml: "xml_tests/widget.tabs.xml",
                         oDomElement:""},
            perfil:     {clave:"1" ,
                         nombre:"Daniel Martínez",
                         avatar:"img/user penny.jpg",
                         oDomElement:""
                        },
            accordion:  {xml: "widget.accordion.xml",
                         menu: [{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}],
                         oDomElement:""},
            estado:     ""
        };

        function _connect (sXMLUrl, cmd) {
             return $.ajax(
                    {
                    url: sXMLUrl,
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
                        //
                        //alert("por configurar options")
                        //$("#xmlRepository").html(xml);

                        //Modifica la variable global settings con los resultados
                        if (cmd=="accordion") {
                            alert("entré al if")
                                var i=0;
                                $(xml).find("registro").each(function(){
                                    options.accordion.menu[i].aplicacion=$(this).find("aplicacion").text();

                                    if ($(this).find("insertar").text()="1") {
                                        options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_nueva_entidad").text(),entidad:$(this).find("alias_menu_nueva_entidad").text(),funcion:"insertar"}];
                                    }
                                    alert(options.accordion.menu[i].elementos_menu)
                                    if ($(this).find("mostrar").text()="1") {
                                        options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_mostrar_entidad").text(),funcion:"mostrar"}];
                                    }

                                    i++;
                                })
                        }
                        return xml;
                    },
                    error:function(xhr,err){
                        alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                        alert("responseText: "+xhr.responseText);}
                    });

          }
          var __init= function( options ) { // Llena los options desde XML
                //Crea páneles
                $("#splitterContainer").splitter({minAsize:100,maxAsize:300,splitVertical:true,A:$('#leftPane'),B:$('#rightPane'),slave:$("#rightSplitterContainer"),closeableto:0});
                $("#rightSplitterContainer").splitter({splitHorizontal:true,A:$('#rightTopPane'),B:$('#rightBottomPane'),closeableto:100});
                //Carga estructura JSON a través de ajax, tomando el webservice con el perfil como parámetro
                options.accordion.oDomElement = "#app_menu";
                options.accordion.xml= "xml_tests/widget.accordion.xml?perfil="+ options.perfil.clave;
                __handleAccordion(options);
          }

          var __handleAccordion=function  (options) {
                var xml=_connect(options.accordion.xml,"accordion");
                
               /* var i=0;
                $(xml).find("registro").each(function(){
                    options.accordion.menu[i].aplicacion=$(this).find("aplicacion").text();

                    if ($(this).find("insertar").text()="1") {
                        options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_nueva_entidad").text(),entidad:$(this).find("alias_menu_nueva_entidad").text(),funcion:"insertar"}];
                    }

                    if ($(this).find("mostrar").text()="1") {
                        options.accordion.menu[i].elementos_menu[options.accordion.menu[i].elementos_menu.length]=[{etiqueta:$(this).find("alias_menu_mostrar_entidad").text(),funcion:"mostrar"}];
                    }

                    i++;
                })
                */
                //Construye menu de acuerdo a configuración recuperada
                for (i=0;i<options.accordion.menu.length;i++) {
                    sHtml="<h3><a href='#' >" + options.accordion.menu[i].aplicacion + "</a></h3>" +
                        "<div>";
                    for (var k=0;k<options.accordion.menu[i].elementos_menu.length;k++) {
                        if (options.accordion.menu[i].elementos_menu[k].funcion=="insertar") {
                            tipoliga="nuevaEntidad";
                        }
                        else {
                            tipoliga="mostrarEntidad";
                        }

                        sHtml+="<div><a href='#' id='" + tipoliga+options.accordion.menu[i].elementos_menu[k].entidad + "'>"+options.menu[i].elementos_menu[k].etiqueta+"</a></div>";
                    }
                    sHtml+="</div>"
                }

               $(options.accordion.oDomElement).append(sHtml);

               $(options.accordion.oDomElement).accordion({
                        active: false,
                        autoHeight: false,
                        collapsible: true,
                        change: function() {
                          $(this).find('h3').blur();
                        }
               });

               /* Establece los eventos para los links del menú
               $('#nuevaEntidad' + sClaveForma).click(function() {
                    alert('Por implementar');
                    //handleDialog('configuracion.xml', [2,'Configuraci&oacute;n', 'grid']);
                });

               //Establece los eventos para los links del menú
               $('#muestraEntidad' + sClaveForma).click(function() {
                    alert('Por implementar');
                    //handleDialog('configuracion.xml', [2,'Configuraci&oacute;n', 'grid']);
                }); */
          }
     if ( options ) {
        $.extend( settings, options );
      }

     __init(settings);

    return this.each(function() {
      // If options exist, lets merge them
      // with our default settings
      if ( options ) {
        $.extend( settings, options );
      }
    });

 // Method calling logic
/*   if ( methods[method] ) {
      return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
    } else if ( typeof method === 'object' || ! method ) {
      return methods.init.apply( this, arguments );
    } else {
      $.error( 'Method ' +  method + ' does not exist on jquery.apps' );
    } */

    $.app = function() {
    }
  };
})( jQuery );
