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
package mx.ilce.importDB;

import java.io.Serializable;

/**
 * Bean implementado para contener los datos de la carga de archivos
 * @author ccatrilef
 */
class CargaArchivo implements Serializable{

    private Integer idCampoTabla;
    private String nombreCampo;
    private Integer idTipoCampo;
    private String tipoCampo;
    private String aliasCampo;
    private boolean obligatorio;
    private String formato;
    private String tabla;
    private boolean sumable;
    private Integer posicionInicio;
    private Integer largo;
    private Integer posicionInicioTotal;
    private Integer largoTotal;
    private Integer nroFilaHeader;
    private String tagTotales;
    private String tagNroRegistros;
    private String separador;
    private Integer posicionSeparador;
    private boolean pivote;
    private Integer idTipoArchivoCarga;
    private Integer idCampoHeader;
    private Integer fila;
    private Integer columna;
    private String tagCampoHeader;
    private boolean ignoreIncomplete;
    private Integer posicionHeader;

    /**
     * Obtiene la posición del header dentro del archivo
     * @return  Integer     Posición del Header
     */
    public Integer getPosicionHeader() {
        return posicionHeader;
    }

    /**
     * Asigna la posición del header dentro del archivo
     * @param posicionHeader    Posición del Header
     */
    public void setPosicionHeader(Integer posicionHeader) {
        this.posicionHeader = posicionHeader;
    }

    /**
     * Obtiene el número de columna
     * @return  Integer     Número de columna
     */
    public Integer getColumna() {
        return columna;
    }

    /**
     * Asigna el número de columna
     * @param columna   Número de columna
     */
    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    /**
     * Obtiene el validador de si se deben ignorar o no los datos incompletos
     * @return  boolean     Valor del validador
     */
    public boolean isIgnoreIncomplete() {
        return ignoreIncomplete;
    }

    /**
     * Asigna el validador de si se deben ignorar o no los datos incompletos
     * @param ignoreIncomplete  Valor del validador
     */
    public void setIgnoreIncomplete(boolean ignoreIncomplete) {
        this.ignoreIncomplete = ignoreIncomplete;
    }

    /**
     * Obtiene la posición donde se encuentra el separador dentro del archivo
     * @return  Integer     Posición del separador
     */
    public Integer getPosicionSeparador() {
        return posicionSeparador;
    }

    /**
     * Asigna la posición donde se encuentra el separador dentro del archivo
     * @param posicionSeparador     Posición del separador
     */
    public void setPosicionSeparador(Integer posicionSeparador) {
        this.posicionSeparador = posicionSeparador;
    }

    /**
     * Obtiene el número de fila
     * @return  Integer     Número de fila
     */
    public Integer getFila() {
        return fila;
    }

    /**
     * Asigna el número de fila
     * @param fila  Número de fila
     */
    public void setFila(Integer fila) {
        this.fila = fila;
    }

    /**
     * Obtiene el ID del CampoHeader
     * @return  Integer     ID del CampoHeader
     */
    public Integer getIdCampoHeader() {
        return idCampoHeader;
    }

    /**
     * Asigna el ID del CampoHeader
     * @param idCampoHeader     ID del CampoHeader
     */
    public void setIdCampoHeader(Integer idCampoHeader) {
        this.idCampoHeader = idCampoHeader;
    }

    /**
     * Obtiene el TAG del CampoHeader
     * @return  String      TAG del CampoHeader
     */
    public String getTagCampoHeader() {
        return tagCampoHeader;
    }

    /**
     * Asigna el TAG del CampoHeader
     * @param tagCampoHeader    TAG del CampoHeader
     */
    public void setTagCampoHeader(String tagCampoHeader) {
        this.tagCampoHeader = tagCampoHeader;
    }

    /**
     * Obtiene el ID del tipo de archivo de carga
     * @return  Integer     ID tipo de archivo de carga
     */
    public Integer getIdTipoArchivoCarga() {
        return idTipoArchivoCarga;
    }

    /**
     * Asigna el ID del tipo de archivo de carga
     * @param idTipoArchivoCarga    ID tipo de archivo de carga
     */
    public void setIdTipoArchivoCarga(Integer idTipoArchivoCarga) {
        this.idTipoArchivoCarga = idTipoArchivoCarga;
    }

    /**
     * Obtiene el validador de si un campo es pivote o no
     * @return  boolean     Valor del validador
     */
    public boolean isPivote() {
        return pivote;
    }

    /**
     * Asigna el validador de si un campo es pivote o no
     * @param pivote    Valor del validador
     */
    public void setPivote(boolean pivote) {
        this.pivote = pivote;
    }

    /**
     * Obtiene el número de fila donde esta ubicado el header
     * @return  Integer     Número de fila del header
     */
    public Integer getNroFilaHeader() {
        return nroFilaHeader;
    }

    /**
     * Asigna el número de fila donde esta ubicado el header
     * @param nroFilaHeader     Número de fila del header
     */
    public void setNroFilaHeader(Integer nroFilaHeader) {
        this.nroFilaHeader = nroFilaHeader;
    }

    /**
     * Obtiene el texto separador asignado
     * @return  String      Texto separador
     */
    public String getSeparador() {
        return separador;
    }

    /**
     * Asigna el texto separador
     * @param separador     Texto separador
     */
    public void setSeparador(String separador) {
        this.separador = separador;
    }

    /**
     * Obtiene el TAG del número de registros
     * @return  String  
     */
    public String getTagNroRegistros() {
        return tagNroRegistros;
    }

    /**
     * Asigna el TAG del número de registros
     * @param tagNroRegistros
     */
    public void setTagNroRegistros(String tagNroRegistros) {
        this.tagNroRegistros = tagNroRegistros;
    }

    public String getTagTotales() {
        return tagTotales;
    }

    public void setTagTotales(String tagTotales) {
        this.tagTotales = tagTotales;
    }

    public Integer getLargo() {
        return largo;
    }

    public void setLargo(Integer largo) {
        this.largo = largo;
    }

    public Integer getLargoTotal() {
        return largoTotal;
    }

    public void setLargoTotal(Integer largoTotal) {
        this.largoTotal = largoTotal;
    }

    public Integer getPosicionInicio() {
        return posicionInicio;
    }

    public void setPosicionInicio(Integer posicionInicio) {
        this.posicionInicio = posicionInicio;
    }

    public Integer getPosicionInicioTotal() {
        return posicionInicioTotal;
    }

    public void setPosicionInicioTotal(Integer posicionInicioTotal) {
        this.posicionInicioTotal = posicionInicioTotal;
    }

    public boolean isSumable() {
        return sumable;
    }

    public void setSumable(boolean sumable) {
        this.sumable = sumable;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getAliasCampo() {
        return aliasCampo;
    }

    public void setAliasCampo(String aliasCampo) {
        this.aliasCampo = aliasCampo;
    }

    public Integer getIdCampoTabla() {
        return idCampoTabla;
    }

    public void setIdCampoTabla(Integer idCampoTabla) {
        this.idCampoTabla = idCampoTabla;
    }

    public Integer getIdTipoCampo() {
        return idTipoCampo;
    }

    public void setIdTipoCampo(Integer idTipoCampo) {
        this.idTipoCampo = idTipoCampo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
    }
}
