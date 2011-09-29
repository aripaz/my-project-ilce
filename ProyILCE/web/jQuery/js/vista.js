/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    
    $('#switcher').themeswitcher(); 
    //Crea menú de aplicaciones de acuerdo al perfil
    $("#app_menu").appmenu({ xmlUrl : "/ProyILCE/resource/jsp/xmlMenu.jsp"});
    $("#session_menu").sessionmenu();


}); //close $(