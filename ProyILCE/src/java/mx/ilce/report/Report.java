package mx.ilce.report;

import java.io.Serializable;

/**
 * Clase para contener los datos de entrada de los reportes
 * @author ccatrilef
 */
public class Report implements Serializable{

    private Integer idReport;
    private String report;
    private String strWhere;
    private String[] arrData;
    private String[][] arrVariables;

    /**
     * Obtiene el arreglo de datos
     * @return
     */
    public String[] getArrData() {
        return ((arrData==null)?new String[0]:arrData);
    }

    /**
     * Asigna el arreglo de datos
     * @param arrData
     */
    public void setArrData(String[] arrData) {
        this.arrData = arrData;
    }

    /**
     * Obtiene el Arreglo de variables
     * @return
     */
    public String[][] getArrVariables() {
        return ((arrVariables==null)?new String[0][0]:arrVariables);
    }

    /**
     * Asigna el Arreglo de variables
     * @param arrVariables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene el ID del reporte
     * @return
     */
    public Integer getIdReport() {
        return ((idReport==null)?Integer.valueOf(1):idReport);
    }

    /**
     * Asigna el ID del reporte
     * @param idReport
     */
    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    /**
     * Obtiene el nombre del reporte
     * @return
     */
    public String getReport() {
        return ((report==null)?"":report);
    }

    /**
     * Asigna el nombre del reporte
     * @param report
     */
    public void setReport(String report) {
        this.report = report;
    }

    /**
     * Entrega el texto con el WHERE adicional para la query
     * @return
     */
    public String getStrWhere() {
        return ((strWhere==null)?"":strWhere);
    }

    /**
     * Asigna el texto con el WHERE adicional para la query
     * @param strWhere
     */
    public void setStrWhere(String strWhere) {
        this.strWhere = strWhere;
    }

}
