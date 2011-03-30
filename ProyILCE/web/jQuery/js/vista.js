/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    $("#splitterContainer").splitter({
        minAsize:100,
        maxAsize:300,
        splitVertical:true,
        A:$('#leftPane'),
        B:$('#rightPane'),
        slave:$("#rightSplitterContainer"),
        closeableto:0
    });

    $("#rightSplitterContainer").splitter({
        splitHorizontal:true,
        A:$('#rightTopPane'),
        B:$('#rightBottomPane'),
        closeableto:100
    });
    
    //Debe recuperarse el valor de perfil a partir de una cookie o un bean
    nPerfil=1;
    //Crea menú de aplicaciones de acuerdo al perfil  

    $("#apps_menu").appmenu({
        perfil:nPerfil
    });

    //Crea el menú de sesión
    $("#session_menu").sessionmenu();

    //Crea el formulario de búsqueda avanzada de la última aplicación abierta
    nUltimaApp=1;
    $("#advanced_search").hide();
    $("#advanced_search").form({
        forma:nUltimaApp,
        modo:"busqueda_avanzada"
    });

    //Habilita mecanismo para expandir / colapsar el formulario de búsqueda avanzada
    $("#lnkBusqueda").click(function(){
        $("#simple_search").slideToggle();
        $("#advanced_search").slideToggle();
    });


}); //close $(