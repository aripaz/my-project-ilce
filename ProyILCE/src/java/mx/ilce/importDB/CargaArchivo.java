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

    public Integer getPosicionHeader() {
        return posicionHeader;
    }

    public void setPosicionHeader(Integer posicionHeader) {
        this.posicionHeader = posicionHeader;
    }
    
    public Integer getColumna() {
        return columna;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public boolean isIgnoreIncomplete() {
        return ignoreIncomplete;
    }

    public void setIgnoreIncomplete(boolean ignoreIncomplete) {
        this.ignoreIncomplete = ignoreIncomplete;
    }
    
    public Integer getPosicionSeparador() {
        return posicionSeparador;
    }

    public void setPosicionSeparador(Integer posicionSeparador) {
        this.posicionSeparador = posicionSeparador;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getIdCampoHeader() {
        return idCampoHeader;
    }

    public void setIdCampoHeader(Integer idCampoHeader) {
        this.idCampoHeader = idCampoHeader;
    }

    public String getTagCampoHeader() {
        return tagCampoHeader;
    }

    public void setTagCampoHeader(String tagCampoHeader) {
        this.tagCampoHeader = tagCampoHeader;
    }

    public Integer getIdTipoArchivoCarga() {
        return idTipoArchivoCarga;
    }

    public void setIdTipoArchivoCarga(Integer idTipoArchivoCarga) {
        this.idTipoArchivoCarga = idTipoArchivoCarga;
    }
    
    public boolean isPivote() {
        return pivote;
    }

    public void setPivote(boolean pivote) {
        this.pivote = pivote;
    }

    public Integer getNroFilaHeader() {
        return nroFilaHeader;
    }

    public void setNroFilaHeader(Integer nroFilaHeader) {
        this.nroFilaHeader = nroFilaHeader;
    }

    public String getSeparador() {
        return separador;
    }

    public void setSeparador(String separador) {
        this.separador = separador;
    }

    public String getTagNroRegistros() {
        return tagNroRegistros;
    }

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
