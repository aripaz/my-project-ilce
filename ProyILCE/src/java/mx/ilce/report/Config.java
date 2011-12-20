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
package mx.ilce.report;

import java.io.Serializable;

/**
 * Clase para contener las configuraciones
 * @author ccatrilef
 */
public class Config implements Serializable{

    private Integer idConfigStructure;
    private Integer idConfigElement;
    private Integer idConfigValue;
    private Integer idStructure;
    private Integer idOrder;
    private Integer idQuery;
    private Integer idMeasure;
    private Integer isExtern;
    private Integer idTypeConfig;
    private Integer idTypeStructure;
    private Integer idTypeValue;
    private String configValue;
    private String query;
    private String typeStructure;
    private String typeConfig;
    private String typeValue;
    private String measure;
    private int mainFig;

    public Integer getIdConfigElement() {
        return idConfigElement;
    }

    public void setIdConfigElement(Integer idConfigElement) {
        this.idConfigElement = idConfigElement;
    }

    /**
     * Obtiene el ID de la query asociado al reporte
     * @return Integer  ID de la query
     */
    public Integer getIdQuery() {
        return idQuery;
    }

    /**
     * Asigna el ID de la query del reporte
     * @param idQueryReport     ID de la query
     */
    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    /**
     * Obtiene el valor de validacion de si es externa o no la query
     * @return Integer      Valor de validacion
     */
    public Integer getIsExtern() {
        return isExtern;
    }

    /**
     * Asigna el valor de validacion de si es externa o no la query
     * @param isExtern      Valor de validacion
     */
    public void setIsExtern(Integer isExtern) {
        this.isExtern = isExtern;
    }

    /**
     * Obtiene el ID de la medida
     * @return Integer  ID de la medida
     */
    public Integer getIdMeasure() {
        return idMeasure;
    }

    /**
     * Asigna el ID de la medida
     * @param idMeasure     ID de la medida
     */
    public void setIdMeasure(Integer idMeasure) {
        this.idMeasure = idMeasure;
    }

    /**
     * Obtiene el nombre de la medida
     * @return String   Nombre de la medida
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * Asigna el nombre de la medida
     * @param measure   Nombre de la medida
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    /**
     * Obtiene el valor de la configuracion
     * @return  String  Valor de la configuracion
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * Asigna el valor de la configuracion
     * @param configValue   Valor de la configuracion
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    /**
     * Obtiene el ID del valor de configuracion
     * @return  Integer     ID del valor de configuracion
     */
    public Integer getIdConfigValue() {
        return idConfigValue;
    }

    /**
     * Asigna el ID del valor de configuracion
     * @param idConfigValue     ID del valor de configuracion
     */
    public void setIdConfigValue(Integer idConfigValue) {
        this.idConfigValue = idConfigValue;
    }

    /**
     * Obtiene el ID del tipo de valor de configuracion
     * @return Integer  ID del tipo de valor de configuracion
     */
    public Integer getIdTypeValue() {
        return idTypeValue;
    }

    /***
     * Asigna el ID del tipo de valor de configuracion
     * @param idTypeConfigValue     ID del tipo de valor de configuracion
     */
    public void setIdTypeValue(Integer idTypeValue) {
        this.idTypeValue = idTypeValue;
    }

    /**
     * Obtiene el tipo de valor de configuracion
     * @return String   Tipo de valor de configuracion
     */
    public String getTypeValue() {
        return typeValue;
    }

    /**
     * Asigna el tipo de valor de configuracion
     * @param typeConfigValue   Tipo de valor de configuracion
     */
    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * Obtiene el ID del tipo de configuracion
     * @return  Integer     ID del tipo de configuracion
     */
    public Integer getIdTypeConfig() {
        return idTypeConfig;
    }

    /**
     * Asigna el ID del tipo de configuracion
     * @param idTypeConfig      ID del tipo de configuracion
     */
    public void setIdTypeConfig(Integer idTypeConfig) {
        this.idTypeConfig = idTypeConfig;
    }

    /**
     * Obtiene el tipo de configuracion
     * @return  String  Tipo de configuracion
     */
    public String getTypeConfig() {
        return typeConfig;
    }

    /**
     * Asigna el tipo de configuracion
     * @param typeConfig    Tipo de configuracion
     */
    public void setTypeConfig(String typeConfig) {
        this.typeConfig = typeConfig;
    }

    /**
     * Obtiene el ID de configuracion de estructura
     * @return Integer ID de configuracion de estructura
     */
    public Integer getIdConfigStructure() {
        return ((idConfigStructure==null)?0:idConfigStructure);
    }

    /**
     * Asigna el ID de configuracion de estructura
     * @param idConfigStructure     ID de configuracion de estructura
     */
    public void setIdConfigStructure(Integer idConfigStructure) {
        this.idConfigStructure = idConfigStructure;
    }

    /**
     * Obtiene el ID de la estructura
     * @return  Integer ID de la estructura
     */
    public Integer getIdStructure() {
        return ((idStructure==null)?0:idStructure);
    }

    /**
     * Asigna el ID de la estructura
     * @param idStructure   ID de la estructura
     */
    public void setIdStructure(Integer idStructure) {
        this.idStructure = idStructure;
    }

    /**
     * Obtiene el nombre de la estructura
     * @return String   Nombre de la estructura
     */
    /*
    public String getStructure() {
        return structure;
    }*/

    /**
     * Asigna el nombre de la estructura
     * @param structure     Nombre de la estructura
     */
    /*
    public void setStructure(String structure) {
        this.structure = structure;
    }*/

    /**
     * Obtiene el ID del tipo de estructura
     * @return  Integer     ID del tipo de estructura
     */
    public Integer getIdTypeStructure() {
        return ((idTypeStructure==null)?0:idTypeStructure);
    }

    /**
     * Asigna el ID del tipo de estructura
     * @param idTypeStructure   ID del tipo de estructura
     */
    public void setIdTypeStructure(Integer idTypeStructure) {
        this.idTypeStructure = idTypeStructure;
    }

    /**
     * Obtiene el nombre del tipo de estructura
     * @return  String  Nombre del tipo de estructura
     */
    public String getTypeStructure() {
        return typeStructure;
    }

    /**
     * Asigna el Nombre del tipo de estructura
     * @param typeStructure     Nombre del tipo de estructura
     */
    public void setTypeStructure(String typeStructure) {
        this.typeStructure = typeStructure;
    }

    /**
     * Obtiene el texto de la query
     * @return  String  Texto de la query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna el texto de la query
     * @param query     Texto de la query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene el ID del orden de posicion
     * @return  Integer     ID del orden de posicion
     */
    public Integer getIdOrder() {
        return idOrder;
    }

    /**
     * Asigna el ID del orden de posicion
     * @param idOrder   ID del orden de posicion
     */
    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * Obtiene el valor de validacion de si es principal o no la estructura
     * @return  int    Valor de validacion
     */
    public int getMainFig() {
        return mainFig;
    }

    /**
     * Asigna el valor de validacion de si es principal o no la estructura
     * @param mainFig   Valor de validacion
     */
    public void setMainFig(int mainFig) {
        this.mainFig = mainFig;
    }
}
