/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    $("#splitterContainer").splitter({
        minAsize:100,
        maxAsize:250,
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
    
    //Crea el menú de sesión
    $("#session_menu").sessionmenu();

    //Crea menú de aplicaciones de acuerdo al perfil
    $("#apps_menu").appmenu({
             xmlUrl : "/ProyILCE/resource/jsp/xmlMenu.jsp"});

    //Inicializa el escritorio
    $("#tabUser").desktop();

}); //close $(