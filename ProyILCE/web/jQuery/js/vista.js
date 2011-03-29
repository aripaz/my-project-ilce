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

    //Carga estructura JSON a través de ajax, tomando el webservice con el perfil como parámetro
    $("#apps_menu").appmenu({
        xmlUrl:"xml_tests/widget.accordion.xml?perfil="+nPerfil,
        perfil:nPerfil
    });

}); //close $(