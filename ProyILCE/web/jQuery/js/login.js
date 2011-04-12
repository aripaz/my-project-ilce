/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    $("#divMsgLogin").hide();
    $("#divMsjRecuperaPW").hide();

    $("#divLostPw").hide();

    $("#lnkRecuperaPw,#lnkIniciarSesion").click(function(){
        $("#divLogin").toggle();
        $("#divLostPw").toggle();
    });

    $("#iniciarsesion").click(function(){
        s=$("#texto_mensaje").html;
        if ($("#u").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere clave de usuario, verifique</strong>");
            $("#divMsgLogin").show();
            return;
        }
        else {
            $("#divMsgLogin").hide();
        }

        if ($("#c").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere contrase&ntilde;a, verifique</strong>");
            $("#divMsgLogin").show();
            return;
        }

        $.post("controlador.jsp",{
            cmd: "login",
            email: $("#u").val(),
            "password":  $("#c").val(),
            "clave_aplicacion": $("#clave_aplicacion").val()
            },
        function(response){
            var sError=($(response).find('error').text());
            if (sError!="")  {
                $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>" + sError + "</strong>");
                $("#divMsgLogin").show();
            }
        }
        );

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
