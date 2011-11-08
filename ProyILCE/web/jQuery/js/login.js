/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {

    if (($("#msjLogin").html().toLowerCase()=='<span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span>')||
        ($("#msjLogin").html().toLowerCase()=='<span style="float: left; margin-right: 0.3em" class="ui-icon ui-icon-alert"></span>'))   {
    $("#divMsgLogin").hide();
    }
    
    $("#divMsjRecuperaPW").hide();
    
    $("#divCarousel").agile_carousel({
                carousel_data: [{
                                "content": $("#divLogin").html(),
                                "content_button": ""
                                }, {
                                "content": $("#divLostPw").html(),
                                "content_button": ""
                                }],
                carousel_outer_height: 228,
                carousel_height: 228,
                slide_height: 230,
                carousel_outer_width: 480,
                slide_width: 480,
                transition_time: 300,
                continuous_scrolling: true,
                control_set_1: "numbered_buttons",
                no_control_set: "hover_previous_button,hover_next_button"
    });
    
    //Hack a carrousel
    $(".slide_number_1").html("Inicio de sesi&oacute;n");
    $(".slide_number_2").html("Olvid&eacute; mi contrase&ntilde;a");
    //$("#divLostPw").hide();

    $("#lnkRecuperaPw,#lnkIniciarSesion").click(function(){
        $("#divLogin").toggle();
        $("#divLostPw").toggle();
    });

    $("#iniciarsesion").click(function(){
        s=$("#texto_mensaje").html;
        if ($("#lgn").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere clave de usuario, verifique</strong>");
            $("#divMsgLogin").show();
            return false;
        }
        else {
            $("#divMsgLogin").hide();
        }

        if ($("#psw").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere contrase&ntilde;a, verifique</strong>");
            $("#divMsgLogin").show();
            return false;
        }

        $("#frmLogin").submit();
        return true;
    });

    $("#btnRecuperarPw").click(function(){
        if ($("#rc").val()=="") {
            $("#msjRecuperaPW").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere clave de usuario, verifique</strong>");
            $("#divMsjRecuperaPW").show();
            return;
        }
        else {
            $("#divMsjRecuperaPW").hide();
        }

    });
});
