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
 *  Bean que contiene los datos de un Elemento de Estructura
 * @author ccatrilef
 */
public class ElementStruct implements Serializable {

    private Integer idElementStruct;
    private Integer idConfigValue;
    private Integer idStructure;
    private Integer idMeasure;
    private Integer idConfigElement;
    private Integer idTypeElement;
    private Integer idTypeConfig;
    private Integer idTypeValue;
    private Integer orden;
    private String valueElement;
    private String configValue;
    private String measure;
    private String typeElement;
    private String typeConfig;
    private String typeValue;
    private String structure;
    private Integer idTypeStructure;
    private String typeStructure;

    public String getTypeStructure() {
        return typeStructure;
    }

    public void setTypeStructure(String typeStructure) {
        this.typeStructure = typeStructure;
    }

    public Integer getIdTypeStructure() {
        return idTypeStructure;
    }

    public void setIdTypeStructure(Integer idTypeStructure) {
        this.idTypeStructure = idTypeStructure;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getTypeConfig() {
        return typeConfig;
    }

    public void setTypeConfig(String typeConfig) {
        this.typeConfig = typeConfig;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Integer getIdMeasure() {
        return idMeasure;
    }

    public void setIdMeasure(Integer idMeasure) {
        this.idMeasure = idMeasure;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Integer getIdConfigValue() {
        return idConfigValue;
    }

    public void setIdConfigValue(Integer idConfigValue) {
        this.idConfigValue = idConfigValue;
    }

    public Integer getIdTypeValue() {
        return idTypeValue;
    }

    public void setIdTypeValue(Integer idTypeValue) {
        this.idTypeValue = idTypeValue;
    }

    public Integer getIdTypeConfig() {
        return idTypeConfig;
    }

    public void setIdTypeConfig(Integer idTypeConfig) {
        this.idTypeConfig = idTypeConfig;
    }

    public Integer getIdConfigElement() {
        return idConfigElement;
    }

    public void setIdConfigElement(Integer idConfigElement) {
        this.idConfigElement = idConfigElement;
    }

    public Integer getIdElementStruct() {
        return idElementStruct;
    }

    public void setIdElementStruct(Integer idElementStruct) {
        this.idElementStruct = idElementStruct;
    }

    public Integer getIdStructure() {
        return idStructure;
    }

    public void setIdStructure(Integer idStructure) {
        this.idStructure = idStructure;
    }

    public Integer getIdTypeElement() {
        return idTypeElement;
    }

    public void setIdTypeElement(Integer idTypeElement) {
        this.idTypeElement = idTypeElement;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(String typeElement) {
        this.typeElement = typeElement;
    }

    public String getValueElement() {
        return valueElement;
    }

    public void setValueElement(String valueElement) {
        this.valueElement = valueElement;
    }

}
