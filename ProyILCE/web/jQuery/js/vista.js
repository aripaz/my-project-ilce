/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {


    //Crea menú de aplicaciones de acuerdo al perfil
    $("#app_menu").sessionmenu().menu({
             xmlUrl : "/ProyILCE/resource/jsp/xmlMenu.jsp"});
    
    
    //Inicializa el escritorio
    $("#tabUser").desktop();

}); //close $(