package mx.ilce.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  Clase implementada para contener los datos del User
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

//------------ CONSTRUCTOR -----------------
    /**
     * Constructor basico del User, inicializa las variables basicas
     */
    public User() {
        this.login = "";
        this.nombre = "";
        this.password = "";
        this.isLogged = false;
    }

//------------ GETTER Y SETTER --------------
    /**
     * Obtiene la clave de area del User
     * @return
     */
    public Integer getClaveArea() {
        return claveArea;
    }

    /**
     * Asigna la clave de area del User
     * @param claveArea
     */
    public void setClaveArea(Integer claveArea) {
        this.claveArea = claveArea;
    }

    /**
     * Obtiene la clave de empleado del user
     * @return
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Asigna la clave de empleado del user
     * @param claveEmpleado
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Obtiene la clave de perfil del user
     * @return
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clave de perfil del user
     * @param clavePerfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene el email del user
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el email del user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la palabra de login del user
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     * Asigna la palabra de login del user
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Obtiene el ID del usuario
     * @return
     */
    public BigDecimal getIDUser() {
        return IDUser;
    }

    /**
     * Asigna el ID USer
     * @param IDUser
     */
    public void setIDUser(BigDecimal IDUser) {
        this.IDUser = IDUser;
    }

    /**
     * Entrega la variable boolena (True o False) que indica si esta conectado
     * @return
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Asigna a la valiable booleana un true o false segun si esta conectado
     * @param isLogin
     */
    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    /**
     * Obtiene el nombre de usuario asignado
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de usuario
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Entrega la password de usuario asignada
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna la password de usuario
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el apellido materno asignado del usuario
     * @return
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Asigna el apellido materno del usuario
     * @param apellidoMaterno
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Obtiene el apellido paterno asignado del usuario
     * @return
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Asigna el apellido paterno del usuario
     * @param apellidoPaterno
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Obtiene la url asignada del avatar del usuario
     * @return
     */
    public String getUrlAvatar() {
        return urlAvatar;
    }

    /**
     * Asigna la url del avatar del usuario
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
     * Obtiene el mensaje de operacion asignado
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Asigna el mensaje de operacion
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Entrega en formato String el contenido del objeto
     * @return
     */
    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre + "IDUser=" + IDUser + "urlAvatar=" + urlAvatar + '}';
    }

}
