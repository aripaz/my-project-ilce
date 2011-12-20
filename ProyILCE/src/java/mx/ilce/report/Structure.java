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
 * Clase implementada para contener los datos de una Structure
 * @author ccatrilef
 */
public class Structure implements Serializable {

    private Integer idReport;
    private Integer idStructure;
    private Integer idTypeStructure;
    private String typeStructure;
    private String structure;
    private String query;
    private int mainFig;
    private Integer idOrder;
    private Integer idQuery;
    private int isExtern;

    public int getIsExtern() {
        return isExtern;
    }

    public void setIsExtern(int isExtern) {
        this.isExtern = isExtern;
    }

    public Integer getIdQuery() {
        return idQuery;
    }

    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public int getMainFig() {
        return mainFig;
    }

    public void setMainFig(int mainFig) {
        this.mainFig = mainFig;
    }

    public Integer getIdStructure() {
        return idStructure;
    }

    public void setIdStructure(Integer idStructure) {
        this.idStructure = idStructure;
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

    public String getTypeStructure() {
        return typeStructure;
    }

    public void setTypeStructure(String typeStructure) {
        this.typeStructure = typeStructure;
    }

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }


}
