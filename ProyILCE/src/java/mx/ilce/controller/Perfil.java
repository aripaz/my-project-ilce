package mx.ilce.controller;

import java.util.List;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
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
    private String[][] arrVariables;
    private Bitacora bitacora;

    /**
     * Obtiene el objeto bitacora
     * @return
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto bitacora
     * @param bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el arreglo de variables
     * @return
     */
    public String[][] getArrVariables() {
        return arrVariables;
    }

    /**
     * Asigna el arreglo de variables
     * @param arrVariables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene la clave del perfil
     * @return
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clave del perfil
     * @param clavePerfil   Clave del Perfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene el listado de aplicaciones del perfil
     * @return
     */
    public List getLstAplicacion() {
        return lstAplicacion;
    }

    /**
     * Asigna el listado de aplicaciones del perfil
     * @param lstAplicacion     Listado de aplicaciones del Perfil
     */
    public void setLstAplicacion(List lstAplicacion) {
        this.lstAplicacion = lstAplicacion;
    }

    /**
     * Obtiene el nombre del perfil
     * @return
     */
    public String getPerfil() {
        return perfil;
    }

    /**
     * Asigna el nombre del perfil
     * @param perfil    Nombre del Perfil
     */
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    /**
     * Constructor Basico
     */
    public Perfil() {
    }

    /**
     * Asigna un Object
     * @param data  Objeto que se va a asignar
     */
    public Perfil(Object data) {
        this.setData(data);
    }

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Entidad mostrarForma() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Entidad mostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Entidad ingresarBusquedaSencilla() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
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
            con.setBitacora(this.getBitacora());

            User usr = con.getUser(user, password, this.getArrVariables());
            usr.setBitacora(this.getBitacora());

            if (usr.isLogged()){
                lg.setIsLogin(true);
                //completar los datos del perfil
                Perfil perf = con.getPerfil(usr, this.getArrVariables());
                perf.setBitacora(this.getBitacora());

                this.clavePerfil = perf.getClavePerfil();
                this.lstAplicacion = perf.getLstAplicacion();
                this.perfil = perf.getPerfil();
                
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
     * @param usuario   Objeto User con los datos del usuario
     * @return
     */
    public LoginHandler login(User usuario) throws ExceptionHandler {
        LoginHandler lg = new LoginHandler();
        try{
            String user = usuario.getLogin();
            String password = usuario.getPassword();
            ConSession con = new ConSession();
            Bitacora bitacora = this.getBitacora();
            con.setBitacora(bitacora);

            User usr = con.getUser(user, password, this.getArrVariables());

            if (usr.isLogged()){
                bitacora.setEnable(false);
                usr.setBitacora(bitacora);
                lg.setIsLogin(true);
                //completar los datos del perfil
                con.setBitacora(null);
                Perfil perf = con.getPerfil(usr, this.getArrVariables());

                this.clavePerfil = perf.getClavePerfil();
                this.lstAplicacion = perf.getLstAplicacion();
                this.perfil = perf.getPerfil();

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
     * NO IMPLEMENTADO
     */
    public ExecutionHandler registrarUsuario(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler enviarPasswordPerdido() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public void cerrarSession(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public List obtenerVisitas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
