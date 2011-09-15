package mx.ilce.mail;

import java.io.Serializable;
import java.util.List;

/**
 * Bean implementado para contener la data utilizada por el servicio de mail
 * @author ccatrilef
 */
public class DataMail implements Serializable{

    private Integer claveForma;
    private Integer idQuery;
    private String query;
    private String tipoQuery;
    private Integer idTipoMail;
    private String tipoMail;
    private Integer idMail;
    private String mail;
    private String nombre;
    private String appPaterno;
    private String appMaterno;
    private String tipo;
    private String subJect;
    private String texto;
    private String textoImpersonal;
    private Integer orden;
    private String password;
    private List listaFrom;
    private List listaTo;
    private List listCopy;
    private List ListO;

    /**
     * Obtiene la clave de la forma
     * @return
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Asigna la clave de la forma
     * @param claveForma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene el password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna el password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el Mail
     * @return
     */
    public String getMail() {
        return mail;
    }

    /**
     * Asigna el Mail
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Obtiene el tipo de query
     * @return
     */
    public String getTipoQuery() {
        return tipoQuery;
    }

    /**
     * Asigna el tipo de query
     * @param tipoQuery
     */
    public void setTipoQuery(String tipoQuery) {
        this.tipoQuery = tipoQuery;
    }

    /**
     * Obtiene el listado de mail para copia oculta
     * @return
     */
    public List getListO() {
        return ListO;
    }

    /**
     * Asigna el listado de mail para copia oculta
     * @param ListO
     */
    public void setListO(List ListO) {
        this.ListO = ListO;
    }

    /**
     * Obtiene el listado de mail para copia
     * @return
     */
    public List getListCopy() {
        return listCopy;
    }

    /**
     * Asigna el listado de mail para copia
     * @param listCopy
     */
    public void setListCopy(List listCopy) {
        this.listCopy = listCopy;
    }

    /**
     * Obtiene el listado de mail que lo estan enviando
     * @return
     */
    public List getListaFrom() {
        return listaFrom;
    }

    /**
     * Asigna el listado de mail que lo estan enviando
     * @param listaFrom
     */
    public void setListaFrom(List listaFrom) {
        this.listaFrom = listaFrom;
    }

    /**
     * Obtiene el listado de mail de los receptores
     * @return
     */
    public List getListaTo() {
        return listaTo;
    }

    /**
     * Asigna el listado de mail de los receptores
     * @param listaTo
     */
    public void setListaTo(List listaTo) {
        this.listaTo = listaTo;
    }

    /**
     * Obtiene el Apellido Materno
     * @return
     */
    public String getAppMaterno() {
        return appMaterno;
    }

    /**
     * Asigna el Apellido Materno
     * @param appMaterno
     */
    public void setAppMaterno(String appMaterno) {
        this.appMaterno = appMaterno;
    }

    /**
     * Obtiene el Apellido Paterno
     * @return
     */
    public String getAppPaterno() {
        return appPaterno;
    }

    /**
     * Asigna el Apellido Paterno
     * @param appPaterno
     */
    public void setAppPaterno(String appPaterno) {
        this.appPaterno = appPaterno;
    }

    /**
     * Obtiene el ID del Mail
     * @return
     */
    public Integer getIdMail() {
        return idMail;
    }

    /**
     * Asigna el ID del Mail
     * @param idMail
     */
    public void setIdMail(Integer idMail) {
        this.idMail = idMail;
    }

    /**
     * Obtiene el ID de la Query
     * @return
     */
    public Integer getIdQuery() {
        return idQuery;
    }

    /**
     * Asigna el ID de la Query
     * @param idQuery
     */
    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    /**
     * Obtiene el ID del tipo de Mail
     * @return
     */
    public Integer getIdTipoMail() {
        return idTipoMail;
    }

    /**
     * Asigna el ID del tipo de Mail
     * @param idTipoMail
     */
    public void setIdTipoMail(Integer idTipoMail) {
        this.idTipoMail = idTipoMail;
    }

    /**
     * Obtiene el Nombre
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el Nombre
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el Orden
     * @return
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Asigna el Orden
     * @param orden
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * Obtiene la query
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna la query
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene el Subject
     * @return
     */
    public String getSubJect() {
        return subJect;
    }

    /**
     * Asigna el Subject
     * @param subJect
     */
    public void setSubJect(String subJect) {
        this.subJect = subJect;
    }

    /**
     * Obtiene el Texto
     * @return
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna el Texto
     * @param texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtiene el Texto Impersonal
     * @return
     */
    public String getTextoImpersonal() {
        return textoImpersonal;
    }

    /**
     * Asigna el Texto Impersonal
     * @param textoImpersonal
     */
    public void setTextoImpersonal(String textoImpersonal) {
        this.textoImpersonal = textoImpersonal;
    }

    /**
     * Obtiene el Tipo
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna el Tipo
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el Tipo de mail
     * @return
     */
    public String getTipoMail() {
        return tipoMail;
    }

    /**
     * Asigna el Tipo de mail
     * @param tipoMail
     */
    public void setTipoMail(String tipoMail) {
        this.tipoMail = tipoMail;
    }


}
