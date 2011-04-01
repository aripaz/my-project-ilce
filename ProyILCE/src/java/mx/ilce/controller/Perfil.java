/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.util.ArrayList;
import java.util.List;
import mx.ilce.bean.User;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
     
/**
 *
 * @author ccatrilef
 */
public class Perfil extends Entidad{

    public Perfil() {
    }

    public Perfil(Object data) {
        this.setData(data);
    }

    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Entidad mostrarForma() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Entidad mostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Entidad ingresarBusquedaSencilla() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Entidad ingresarBusquedaAvanzada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LoginHandler login(String user, String password) {
        LoginHandler lg = new LoginHandler();
        try{
            ConSession con = new ConSession();
            User usr = con.getUser(user, password);

            if (usr.isLogged()){
                lg.setIsLogin(true);
                //completar los datos del perfil
                //this.setXmlEntidad(usr.getXmlPermiso());
                lg.setObjectData(usr);
                this.setData(usr);
            }else{
                lg.setIsLogin(false);
                lg.setTitleExecution("Error al conectarse");
                lg.setTextExecution(usr.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        return lg;
    }

    public LoginHandler login(User usuario) {
        LoginHandler lg = new LoginHandler();
        try{
            String user = usuario.getLogin();
            String password = usuario.getPassword();
            ConSession con = new ConSession();
            User usr = con.getUser(user, password);

            if (usr.isLogged()){
                lg.setIsLogin(true);
                //completar los datos del perfil
                //this.setXmlEntidad(usr.getXmlPermiso());
                lg.setObjectData(usr);
                this.setData(usr);
            }else{
                lg.setIsLogin(false);
                lg.setTitleExecution("Error al conectarse");
                lg.setTextExecution(usr.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return lg;
    }

    public ExecutionHandler registrarUsuario(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler enviarPasswordPerdido() {
        ExecutionHandler execHand = new ExecutionHandler();
        try{
            List lst = new ArrayList();
            lst.add("uno");
            lst.add("dos");
            execHand.setListData(lst);
        }catch(Exception e){
            e.printStackTrace();
        }
        return execHand;
    }

    public void cerrarSession(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List obtenerVisitas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
