/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.gridMenus = function(opc){
        $.fn.gridMenus.settings = {
            xmlUrl : "srvGrid" , // "srvControl", "srvGrid" "xml_tests/widget.grid.xml"
            app:"",
            form:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.gridMenus.options = $.extend($.fn.gridMenus.settings, opc);
            obj = $(this);
            $.fn.appgrid.getFilters();
        });
        
        
        
        $.fn.appgrid.getFilters = function(){
            obj.html("");
            $.ajax({
            url: "srvFormaSearch?$cf=93&$ta=select&$w=" + escape("clave_empleado=" +$("#_ce_").val()+ " AND clave_forma="+nForma),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlGs = new ActiveXObject("Microsoft.XMLDOM");
                    xmlGs.async = false;
                    xmlGs.validateOnParse="true";
                    xmlGs.loadXML(data);
                    if (xmlGs.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                }                
                else 
                    xmlGs = data;
                
                var sBusquedas="<br>";
                var nAplicacion="";
                
                $(xmlGs).find("registro").each( function(){
                    nClave=$(this).find("clave_filtro")[0].firstChild.data;
                    if (nClave=="") return false
                    sFiltro=$(this).find("filtro")[0].firstChild.data
                    nForma=$(this).find("clave_forma")[0].firstChild.data;
                    nAplicacion =$(this).find("clave_aplicacion")[0].firstChild.data;
                    sW=escape($(this).find("consulta")[0].firstChild.data);
                    sSuffix =nAplicacion + "_" + nForma + "_" + nClave;
                    sBusquedas="<div class='link_toolbar'>"+
                    "<div class='linkSearch'><a class='linkSearch' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sW+ "' forma='" + nForma + "' pk='" + nClave + "' >" + sFiltro + "</a></div>"+
                    "<div style='float:right'><div title='Eliminar filtro' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + nForma + "'></div></div>" +
                    "</div>";

                    obj.append(sBusquedas);

                    //Oculta botones
                    $(".ui-icon-close", "#filtros_"+sDivSuffix).hide();
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
                        /*var newE = $.Event('click');
                        newE.gridFilter=$(this).attr("data");*/
                        data=$(this).attr("data");
                        if ($("#showEntity_" + nAplicacion + "_" + nForma).length>0)
                            $("#showEntity_" + nAplicacion + "_" + nForma).trigger("click",data);
                        else {
                            //hace la búsqueda del grid de acuerdo a la posicion relativa del objeto actual
                            aGridIdSuffix=$(this).parent().parent().parent().parent().parent().next().next().children()[0].children[0].id.split("_");
                            $.fn.appmenu.setGridFilter(aGridIdSuffix[2]+"_"+aGridIdSuffix[3]+"_"+aGridIdSuffix[4],nAplicacion,nForma,data);
                        }
                    });                    
                });

                //Hace bind con los divs padres del link en el evento hover
                $(".link_toolbar").hover(
                    function () {
                        //$(this).addClass('active_filter');
                        $(".closeLnkFiltro",this).show();
                    },
                    function () {
                        //$(this).removeClass('active_filter');
                        $(".closeLnkFiltro",this).hide();
                    }
                    );
                
                //Hace bind con los botones de cerrar en el evento hover
                $(".closeLnkFiltro").hover(
                    function () {
                        $(this).parent().addClass('ui-state-default');
                        $(this).parent().addClass('ui-corner-all');
                    },
                    function () {
                        $(this).parent().removeClass('ui-state-default');
                        $(this).parent().removeClass('ui-corner-all');
                    }
                    );

                //Hace bind del botón de búsqueda
                $(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;
                    $.post("srvFormaDelete","$cf=93&$pk=" + $(this).attr("pk"));
                    $(this).parent().parent().remove();
                });
                
                if (bGetLog==1)
                    $.fn.appmenu.getLog(sDivSuffix,nApp,nForma,bGetLog);

            },
            error:function(xhr,err){
                alert("Error al recuperar filtros: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);            }            
        });
     }
   };      

})(jQuery);