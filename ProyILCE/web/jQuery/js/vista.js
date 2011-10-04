/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    //Despliega dialogo modal para evitar acciones del usuario mientras se cargan primeros grids
    //puesto que causa conflictos
    $("#divwait").dialog({
            height: 140,
            modal: true,
            autoOpen: true,
            closeOnEscape:false
    });
    
    $('#switcher').themeswitcher(); 
    //Crea menú de aplicaciones de acuerdo al perfil
    $("#app_menu").appmenu({xmlUrl : "/ProyILCE/resource/jsp/xmlMenu.jsp"});
    $("#session_menu").sessionmenu();   
    
}); //close $(