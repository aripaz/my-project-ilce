/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    $("#cuadro_mensaje").hide();
    $("#iniciarsesion").click(function(){
        s=$("#texto_mensaje").html;
        if ($("#lgn").val()=="") {
            $("#texto_mensaje").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere la clave de usuario, verifique</strong>");
            $("#cuadro_mensaje").show();
            return;
        }

        if ($("#psw").val()=="") {
            $("#texto_mensaje").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere la contraseña, verifique</strong>");
            $("#cuadro_mensaje").show();
            return;
        }

        $.post("servlet",{
            cmd: "login",
            email: $("#lgn").val(),
            "password":  $("#psw").val(),
            "clave_aplicacion": $("#clave_aplicacion").val()
            },
        function(response){
            var sError=($(response).find('error').text()); //<error>El mensaje de error va aqui</error>
            if (sError!="")  {
                $("#texto_mensaje").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>" + sError + "</strong>");
                $("#cuadro_mensaje").show();
            }
        }
        );

    });
});

