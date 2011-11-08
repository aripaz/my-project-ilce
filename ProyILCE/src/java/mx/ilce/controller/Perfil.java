package mx.ilce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.bitacora.AdmBitacora;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
     
/**
 *  Clase para la implementación de los métodos usados para el manejo de Perfil
 * @author ccatrilef
 */
public class Perfil extends Entidad{

    private Integer clavePerfil;
    private String perfil;
    private List lstAplicacion;
    private String[][] arrVariables;
    private Bitacora bitacora;
    private User user;

    /**
     * Obtiene el objeto User
     * @return  User    Objeto User
     */
    public User getUser() {
        return user;
    }

    /**
     * Asigna el objeto User
     * @param user  Objeto User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora  Objeto Bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el arreglo de variables
     * @return  String[][]  Arreglo con variables
     */
    public String[][] getArrVariables() {
        return arrVariables;
    }

    /**
     * Asigna el arreglo de variables
     * @param arrVariables  Arreglo con variables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene la clave del perfil
     * @return  Integer     Clave del perfil
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
     * @return  List    Listado de aplicaciones del Perfil
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
     * @return  String  Nombre del perfil
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
     * Constructor básico
     */
    public Perfil() {
    }

    /**
     * Asigna un Object al campo Data. Usado para situaciones en que no esta
     * definidos los metodos para un tipo de dato
     * @param data  Objeto que se va a asignar
     */
    public Perfil(Object data) {
        this.setData(data);
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Entidad mostrarForma() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Entidad mostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Entidad ingresarBusquedaSencilla() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Entidad ingresarBusquedaAvanzada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Método para obtener los datos del usuario a partir de los datos de
     * conexión Usuario y Password. En caso de existir problemas se obtiene
     * el texto con la causa
     * @param user      Texto de identificación del usuario
     * @param password  Texto con la password del usuario
     * @return  LoginHandler    Resultado de la operación
     * @throws ExceptionHandler
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
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para efectuar el Login");
            eh.setDataToXML("User", user);
            eh.setDataToXML("Password", password);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lg;
    }

    /**
     * Método para obtener los datos del usuario a partir de los datos de
     * conexión contenidos en el Bean User. En caso de existir problemas se
     * obtiene el texto con la causa
     * @param usuario   Objeto User con los datos del usuario
     * @return  LoginHandler    Resultado de la operación
     * @throws ExceptionHandler
     */
    public LoginHandler login(User usuario) throws ExceptionHandler {
        LoginHandler lg = new LoginHandler();
        try{
            String userData = usuario.getLogin();
            String password = usuario.getPassword();
            ConSession con = new ConSession();
            Bitacora bitacoraI = this.getBitacora();
            boolean enableBit = this.getBitacora().isEnable();
            con.setBitacora(bitacoraI);

            con.getBitacora().setEnable(enableBit);
            User usr = con.getUser(userData, password, this.getArrVariables());
            if (usr.isLogged()){
                bitacoraI.setEnable(false);
                usr.setBitacora(bitacoraI);
                lg.setIsLogin(true);
                //completar los datos del perfil
                con.getBitacora().setEnable(false);
                Perfil perf = con.getPerfil(usr, this.getArrVariables());

                this.clavePerfil = ((perf.getClavePerfil()==null)?Integer.valueOf(0):perf.getClavePerfil());
                this.lstAplicacion = ((perf.getLstAplicacion()==null)?new ArrayList():perf.getLstAplicacion());
                this.perfil = ((perf.getPerfil()==null)?"":perf.getPerfil());

                lg.setObjectData(usr);
                this.setData(usr);
            }else{
                lg.setIsLogin(false);
                lg.setTitleExecution("Error al conectarse");
                lg.setTextExecution(usr.getMessage());
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para efectuar el Login");
            eh.setDataToXML(usuario);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lg;
    }

    /**
     * Método utilizado para el registro de un nuevo usuario
     * @return  ExecutionHandler    Resultado de la operación
     * @throws ExceptionHandler
     */
    public ExecutionHandler registrarUsuario() throws ExceptionHandler{
        ExecutionHandler sld = new ExecutionHandler();
        try{
            ConSession con = new ConSession();
            con.setBitacora(this.getBitacora());

            User newUser = con.insertUser(this.getUser());
            sld.setTextExecution(newUser.getMessage());

            if (newUser.getClaveEmpleado()==null){
                sld.setExecutionOK(false);
            }else{
                sld.setExecutionOK(true);
            }
            sld.setObjectData(newUser);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para registrar al usuario");
            eh.setDataToXML(this.getUser());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }

    /**
     * Método utilizado para la recuperación de la password del usuario
     * @return  ExecutionHandler    Resultado de la operación
     * @throws ExceptionHandler
     */
    public ExecutionHandler enviarPasswordPerdido() throws ExceptionHandler {
        ExecutionHandler sld = new ExecutionHandler();
        try{
            ConSession con = new ConSession();
            con.setBitacora(this.getBitacora());

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setDataObject(this.getUser());

            User newUser = con.getUserByMail(dataTransfer);
            sld.setTextExecution(newUser.getMessage());

            if (newUser.getClaveEmpleado()==null){
                sld.setExecutionOK(false);
            }else{
                sld.setExecutionOK(true);
            }
            sld.setObjectData(newUser);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para enviar la password al usuario");
            eh.setDataToXML(this.getUser());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }


    /**
     * Método para registrar en base de datos el logout de un usuario
     * @throws ExceptionHandler
     */
    public void cerrarSession() throws ExceptionHandler{
        try{
            boolean enableBit = this.getBitacora().isEnable();

            this.getBitacora().setClaveEmpleado(this.getUser().getClaveEmpleado());
            this.getUser().setBitacora(this.getBitacora());
            if (enableBit){
                AdmBitacora admBit = new AdmBitacora();
                admBit.setBitacora(this.getBitacora());
                admBit.logout();
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para efectuar el Logout");
            eh.setDataToXML(this.getUser());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public List obtenerVisitas() {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Obtiene el Listado de áreas de la aplicación
     * @return  List    Listado con las áreas
     * @throws ExceptionHandler
     */
    public List getListArea() throws ExceptionHandler{
        List lst = null;
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());

            String strQuery = "select clave_area, area from area";
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(strQuery);
            dataTransfer.setArrData(new String[0]);

            HashCampo hsCmp = con.getDataByQuery(dataTransfer);
            HashMap hs = hsCmp.getListData();
            if (!hs.isEmpty()){
                lst = new ArrayList();
                for (int i=0; i<hs.size();i++){
                    ArrayList arrLst = (ArrayList) hs.get(Integer.valueOf(i));
                    Campo cmp1 = (Campo) arrLst.get(0);
                    Campo cmp2 = (Campo) arrLst.get(1);
                    String[] str = new String[2];
                    str[0]= cmp1.getValor();
                    str[1]= cmp2.getValor();
                    lst.add(str);
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener las áreas");
        }finally{

        }
        return lst;
    }

    /**
     * Método que indica si un mail ya esta registrado o no
     * @return  Boolean     Resultado de la validación
     * @throws ExceptionHandler
     */
    public boolean existUser() throws ExceptionHandler{
        boolean sld = false;
        try {
            ConEntidad con = new ConEntidad();

            con.setBitacora(this.getBitacora());

            String strQuery = "select * from empleado "
                    + "where email = '" + this.getUser().getEmail().trim() + "'";
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(strQuery);
            dataTransfer.setArrData(new String[0]);

            HashCampo hsCmp = con.getDataByQuery(dataTransfer);
            HashMap hs = hsCmp.getListData();
            if (!hs.isEmpty()){
                sld = true;
            }
        } catch (ExceptionHandler ex) {
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para buscar si existe el usuario");
            eh.setDataToXML(this.getUser());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }

    /**
     * Método que convierte a String el contenido del objeto nPerfil
     * @return
     */
    @Override
    public String toString() {
        return "\nPerfil{"
                + ((clavePerfil!=null)?"\n\tclavePerfil=" + clavePerfil:"")
                + ((perfil!=null)?"\n\tperfil=" + perfil:"")
                + ((lstAplicacion!=null)?"\n\tlstAplicacion=" + lstAplicacion.toString() :"")
                + ((arrVariables!=null)?"\n\tarrVariables=" + arrVariables:"")
                + ((bitacora!=null)?"\n\tbitacora=" + bitacora.toString():"")
                + ((user!=null)?"\n\tuser=" + user.toString():"")
                + "\n}";
    }
}
