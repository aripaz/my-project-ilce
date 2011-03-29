/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function validaForma(forma){
    var lgn = forma.lgn;
    if( lgn.value == "" || lgn.value == "Escribir usuario"){
        alert("Debe proporcionar su correo electronico");
        lgn.focus();
        lgn.select();
        return false;
    }

    var psw = forma.psw;
    if( psw.value == "" ){
        alert("Debe proporcionar una contraseña");
        psw.focus();
        psw.select();
        return false;
    }
}