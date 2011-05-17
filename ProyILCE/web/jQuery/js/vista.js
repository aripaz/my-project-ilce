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
    
    //Debe recuperarse el valor de perfil a partir de una cookie o un bean
    nPerfil=1;
    //Crea menú de aplicaciones de acuerdo al perfil  

    $("#apps_menu").appmenu({
        perfil:nPerfil
    });

    //Crea el menú de sesión
    $("#session_menu").sessionmenu();



}); //close $(