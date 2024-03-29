/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
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

    private String strTo;
    private String strCopy;
    private String strCopyO;

    /**
     * Obtiene el listado de los receptores de copia
     * @return  String  Texto con los receptores
     */
    public String getStrCopy() {
        return strCopy;
    }

    /**
     * Asigna a los receptores de copia
     * @param strCopy   Texto con los receptores
     */
    public void setStrCopy(String strCopy) {
        this.strCopy = strCopy;
    }

    /**
     * Obtiene el listado con los receptores de copia oculta
     * @return  String  Texto con los receptores
     */
    public String getStrCopyO() {
        return strCopyO;
    }

    /**
     * Asigna el listado con los receptores de copia oculta
     * @param strCopyO  Texto con los receptores
     */
    public void setStrCopyO(String strCopyO) {
        this.strCopyO = strCopyO;
    }

    /**
     * Obtiene el listado con los destinatarios del mail
     * @return  String  Texto con los destinatarios
     */
    public String getStrTo() {
        return strTo;
    }

    /**
     * Asigna el listado con los destinatarios del mail
     * @param strTo     Texto con los destinatarios
     */
    public void setStrTo(String strTo) {
        this.strTo = strTo;
    }
    
    /**
     * Obtiene la clave de la forma
     * @return  Integer     Clave de la forma
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Asigna la clave de la forma
     * @param claveForma    Clave de la forma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene el password
     * @return  String  Texto con el password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna el password
     * @param password  Texto con el password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el Mail
     * @return  String  Mail asignado
     */
    public String getMail() {
        return mail;
    }

    /**
     * Asigna el Mail
     * @param mail  Mail asignado
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Obtiene el tipo de query
     * @return  String  Tipo query
     */
    public String getTipoQuery() {
        return tipoQuery;
    }

    /**
     * Asigna el tipo de query
     * @param tipoQuery String  Tipo query
     */
    public void setTipoQuery(String tipoQuery) {
        this.tipoQuery = tipoQuery;
    }

    /**
     * Obtiene el listado de mail para copia oculta
     * @return  List    Listado de mail
     */
    public List getListO() {
        return ListO;
    }

    /**
     * Asigna el listado de mail para copia oculta
     * @param ListO Listado de mail
     */
    public void setListO(List ListO) {
        this.ListO = ListO;
    }

    /**
     * Obtiene el listado de mail para copia
     * @return  List    Listado de mail
     */
    public List getListCopy() {
        return listCopy;
    }

    /**
     * Asigna el listado de mail para copia
     * @param listCopy  Listado de mail
     */
    public void setListCopy(List listCopy) {
        this.listCopy = listCopy;
    }

    /**
     * Obtiene el listado de mail que lo están enviando
     * @return  List    Listado de mail
     */
    public List getListaFrom() {
        return listaFrom;
    }

    /**
     * Asigna el listado de mail que lo están enviando
     * @param listaFrom     Listado de mail
     */
    public void setListaFrom(List listaFrom) {
        this.listaFrom = listaFrom;
    }

    /**
     * Obtiene el listado de mail de los receptores
     * @return  List    Listado de mail
     */
    public List getListaTo() {
        return listaTo;
    }

    /**
     * Asigna el listado de mail de los receptores
     * @param listaTo   Listado de mail
     */
    public void setListaTo(List listaTo) {
        this.listaTo = listaTo;
    }

    /**
     * Obtiene el Apellido Materno
     * @return  String Apellido Materno
     */
    public String getAppMaterno() {
        return appMaterno;
    }

    /**
     * Asigna el Apellido Materno
     * @param appMaterno    Apellido Materno
     */
    public void setAppMaterno(String appMaterno) {
        this.appMaterno = appMaterno;
    }

    /**
     * Obtiene el Apellido Paterno
     * @return  String  Apellido Paterno
     */
    public String getAppPaterno() {
        return appPaterno;
    }

    /**
     * Asigna el Apellido Paterno
     * @param appPaterno    Apellido Paterno
     */
    public void setAppPaterno(String appPaterno) {
        this.appPaterno = appPaterno;
    }

    /**
     * Obtiene el ID del Mail
     * @return  Integer     ID del mail
     */
    public Integer getIdMail() {
        return idMail;
    }

    /**
     * Asigna el ID del Mail
     * @param idMail    ID del mail
     */
    public void setIdMail(Integer idMail) {
        this.idMail = idMail;
    }

    /**
     * Obtiene el ID de la Query
     * @return  Integer     ID de la query
     */
    public Integer getIdQuery() {
        return idQuery;
    }

    /**
     * Asigna el ID de la Query
     * @param idQuery   ID de la query
     */
    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    /**
     * Obtiene el ID del tipo de Mail
     * @return  Integer     ID tipo mail
     */
    public Integer getIdTipoMail() {
        return idTipoMail;
    }

    /**
     * Asigna el ID del tipo de Mail
     * @param idTipoMail    ID tipo mail
     */
    public void setIdTipoMail(Integer idTipoMail) {
        this.idTipoMail = idTipoMail;
    }

    /**
     * Obtiene el Nombre
     * @return  String  Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el Nombre
     * @param nombre    Nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el Orden de posición
     * @return  Integer     Orden de posición
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Asigna el Orden de posición
     * @param orden     Orden de posición
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * Obtiene la query
     * @return  String  Texto de la query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna la query
     * @param query     Texto de la query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene el Subject
     * @return  String  Texto del Subject
     */
    public String getSubJect() {
        return subJect;
    }

    /**
     * Asigna el Subject
     * @param subJect   Texto del Subject
     */
    public void setSubJect(String subJect) {
        this.subJect = subJect;
    }

    /**
     * Obtiene el Texto
     * @return  String  Texto del mail
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna el Texto
     * @param texto Texto del mail
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtiene el Texto Impersonal
     * @return  String  Texto impersonal del mail
     */
    public String getTextoImpersonal() {
        return textoImpersonal;
    }

    /**
     * Asigna el Texto Impersonal
     * @param textoImpersonal   Texto impersonal del mail
     */
    public void setTextoImpersonal(String textoImpersonal) {
        this.textoImpersonal = textoImpersonal;
    }

    /**
     * Obtiene el Tipo
     * @return  String  Tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna el Tipo
     * @param tipo  Iipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el Tipo de mail
     * @return  String  Tipo de mail
     */
    public String getTipoMail() {
        return tipoMail;
    }

    /**
     * Asigna el Tipo de mail
     * @param tipoMail  Tipo de mail
     */
    public void setTipoMail(String tipoMail) {
        this.tipoMail = tipoMail;
    }

    /**
     * Método que convierte a String el contenido del objeto DataMail
     * @return
     */
    @Override
    public String toString() {
        return "DataMail{"
                + ((claveForma!=null)?"\n\tclaveForma=" + claveForma:"")
                + ((idQuery!=null)?"\n\tidQuery=" + idQuery :"")
                + ((query!=null)?"\n\tquery=" + query :"")
                + ((tipoQuery!=null)?"\n\ttipoQuery=" + tipoQuery :"")
                + ((idTipoMail!=null)?"\n\tidTipoMail=" + idTipoMail:"")
                + ((tipoMail!=null)?"\n\ttipoMail=" + tipoMail :"")
                + ((idMail!=null)?"\n\tidMail=" + idMail :"")
                + ((mail!=null)?"\n\tmail=" + mail :"")
                + ((nombre!=null)?"\n\tnombre=" + nombre :"")
                + ((appPaterno!=null)?"\n\tappPaterno=" + appPaterno :"")
                + ((appMaterno!=null)?"\n\tappMaterno=" + appMaterno :"")
                + ((tipo!=null)?"\n\ttipo=" + tipo :"")
                + ((subJect!=null)?"\n\tsubJect=" + subJect :"")
                + ((texto!=null)?"\n\ttexto=" + texto :"")
                + ((textoImpersonal!=null)?"\n\ttextoImpersonal=" + textoImpersonal :"")
                + ((orden!=null)?"\n\torden=" + orden :"")
                + ((password!=null)?"\n\tpassword=" + password :"")
                + ((listaFrom!=null)?"\n\tlistaFrom=" + listaFrom.toString() :"")
                + ((listaTo!=null)?"\n\tlistaTo=" + listaTo.toString() :"")
                + ((listCopy!=null)?"\n\tlistCopy=" + listCopy :"")
                + ((ListO!=null)?"\n\tListO=" + ListO.toString() :"")
                + ((strTo!=null)?"\n\tstrTo=" + strTo :"")
                + ((strCopy!=null)?"\n\tstrCopy=" + strCopy :"")
                + ((strCopyO!=null)?"\n\tstrCopyO=" + strCopyO :"")
                + "\n}";
    }
}
