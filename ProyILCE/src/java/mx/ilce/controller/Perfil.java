/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.util.List;
import mx.ilce.bean.User;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
     
/**
 *  Clase para la implementacion de los metodos usados para el manejo de Perfil
 * @author ccatrilef
 */
public class Perfil extends Entidad{

    private Integer clavePerfil;
    private String perfil;
    private List lstAplicacion;

/******** GETTER Y SETTER ********/

    public Integer getClavePerfil() {
        return clavePerfil;
    }

    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    public List getLstAplicacion() {
        return lstAplicacion;
    }

    public void setLstAplicacion(List lstAplicacion) {
        this.lstAplicacion = lstAplicacion;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

/******* OPERACIONES DE ENTIDAD ******/

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

    /**
     * Metodo para obtener los datos del usuario a partir de los datos de
     * conexion Usuario y Password. En caso de existir problemas se obtiene
     * el texto con la causa
     * @param user      Texto de identificacion del usuario
     * @param password  Texto con la password del usuario
     * @return
     */
    public LoginHandler login(String user, String password) throws ExceptionHandler{
        LoginHandler lg = new LoginHandler();
        try{
            ConSession con = new ConSession();
            User usr = con.getUser(user, password);

            if (usr.isLogged()){
                lg.setIsLogin(true);
                //completar los datos del perfil
                Perfil perfil = con.getPerfil(usr);
                this.clavePerfil = perfil.getClavePerfil();
                this.lstAplicacion = perfil.getLstAplicacion();
                this.perfil = perfil.getPerfil();
                
                lg.setObjectData(usr);
                this.setData(usr);
            }else{
                lg.setIsLogin(false);
                lg.setTitleExecution("Error al conectarse");
                lg.setTextExecution(usr.getMessage());
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para efectuar el Login");
        }finally{
            
        }
        return lg;
    }

    /**
     * Metodo para obtener los datos del usuario a partir de los datos de
     * conexion contenidos en el Bean User. En caso de existir problemas se
     * obtiene el texto con la causa
     * @param usuario
     * @return
     */
    public LoginHandler login(User usuario) throws ExceptionHandler {
        LoginHandler lg = new LoginHandler();
        try{
            String user = usuario.getLogin();
            String password = usuario.getPassword();
            ConSession con = new ConSession();
            User usr = con.getUser(user, password);

            if (usr.isLogged()){
                lg.setIsLogin(true);
                //completar los datos del perfil
                Perfil perfil = con.getPerfil(usr);
                this.clavePerfil = perfil.getClavePerfil();
                this.lstAplicacion = perfil.getLstAplicacion();
                this.perfil = perfil.getPerfil();

                lg.setObjectData(usr);
                this.setData(usr);
            }else{
                lg.setIsLogin(false);
                lg.setTitleExecution("Error al conectarse");
                lg.setTextExecution(usr.getMessage());
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para efectuar el Login");
        }finally{

        }
        return lg;
    }

    public ExecutionHandler registrarUsuario(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler enviarPasswordPerdido() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void cerrarSession(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List obtenerVisitas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
