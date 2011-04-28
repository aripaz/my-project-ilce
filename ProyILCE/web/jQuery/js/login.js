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

    $("#divLostPw").hide();

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
