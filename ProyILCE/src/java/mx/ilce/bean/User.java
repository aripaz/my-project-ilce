package mx.ilce.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import mx.ilce.bitacora.Bitacora;

/**
 * Clase implementada para contener los datos del User
 * @author ccatrilef
 */
public class User implements Serializable {

    private String login;
    private String nombre;
    private String password;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private StringBuffer xmlPermiso;
    private boolean isLogged;
    private BigDecimal IDUser;
    private String urlAvatar;
    private String message;
    private Integer claveEmpleado;
    private Integer clavePerfil;
    private Integer claveArea;
    private String email;
    private Bitacora bitacora;

    /**
     * Constructor básico del User, inicializa las variables básicas
     */
    public User() {
        this.login = "";
        this.nombre = "";
        this.password = "";
        this.isLogged = false;
    }

    /**
     * Obtiene la clave de área del User
     * @return
     */
    public Integer getClaveArea() {
        return claveArea;
    }

    /**
     * Asigna la clave de área del User
     * @param claveArea
     */
    public void setClaveArea(Integer claveArea) {
        this.claveArea = claveArea;
    }

    /**
     * Obtiene la clave de empleado del User
     * @return
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Asigna la clave de empleado del User
     * @param claveEmpleado
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Obtiene la clave de perfil del User
     * @return
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clave de perfil del User
     * @param clavePerfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene el email del User
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el email del User
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la palabra de login del User
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     * Asigna la palabra de login del User
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Obtiene el ID del User
     * @return
     */
    public BigDecimal getIDUser() {
        return IDUser;
    }

    /**
     * Asigna el ID del User
     * @param IDUser
     */
    public void setIDUser(BigDecimal IDUser) {
        this.IDUser = IDUser;
    }

    /**
     * Obtiene la variable boolena (TRUE o FALSE) que indica si esta conectado
     * @return
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Asigna a la valiable booleana un TRUE o FALSE según si esta conectado o no
     * @param isLogin
     */
    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    /**
     * Obtiene el nombre de User asignado
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de User
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la password de User asignada
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna la password de User
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el apellido materno asignado del User
     * @return
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Asigna el apellido materno del User
     * @param apellidoMaterno
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Obtiene el apellido paterno asignado del User
     * @return
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Asigna el apellido paterno del User
     * @param apellidoPaterno
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Obtiene la url asignada del avatar del User
     * @return
     */
    public String getUrlAvatar() {
        return urlAvatar;
    }

    /**
     * Asigna la url del avatar del User
     * @param urlAvatar
     */
    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    /**
     * Obtiene el XML de permiso asignado
     * @return
     */
    public StringBuffer getXmlPermiso() {
        return xmlPermiso;
    }

    /**
     * Asigna el XML de permiso
     * @param xmlPermiso
     */
    public void setXmlPermiso(StringBuffer xmlPermiso) {
        this.xmlPermiso = xmlPermiso;
    }

    /**
     * Obtiene el mensaje de operación asignado
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Asigna el mensaje de operación
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene el objeto bitácora
     * @return
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto bitácora
     * @param bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Método para convertir a String el contenido del Objeto. Los datos que
     * estén con valor NULL son ignorados
     * @return  String  Texto con los datos del objeto
     */
    @Override
    public String toString() {
        return "\nUser{"
                + ((login!=null)?"\n\tlogin=" + login:"")
                + ((nombre!=null)?"\n\tnombre=" + nombre:"")
                + ((password!=null)?"\n\tpassword=" + password:"")
                + ((apellidoPaterno!=null)?"\n\tapellidoPaterno=" + apellidoPaterno:"")
                + ((apellidoMaterno!=null)?"\n\tapellidoMaterno=" + apellidoMaterno:"")
                + "\n\tisLogged=" + isLogged
                + ((IDUser!=null)?"\n\tIDUser=" + IDUser:"")
                + ((urlAvatar!=null)?"\n\turlAvatar=" + urlAvatar:"")
                + ((message!=null)?"\n\tmessage=" + message:"")
                + ((claveEmpleado!=null)?"\n\tclaveEmpleado=" + claveEmpleado:"")
                + ((clavePerfil!=null)?"\n\tclavePerfil=" + clavePerfil:"")
                + ((claveArea!=null)?"\n\tclaveArea=" + claveArea:"")
                + ((email!=null)?"\n\temail=" + email:"")
                + ((bitacora!=null)?"\n\tbitacora=" + bitacora.toString():"")
                + ((xmlPermiso!=null)?"\n\txmlPermiso=" + xmlPermiso:"")
                + '}';
    }

}
