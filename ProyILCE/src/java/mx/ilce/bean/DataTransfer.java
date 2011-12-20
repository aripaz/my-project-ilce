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
package mx.ilce.bean;

/**
 * Clase implementada para la transmisión de datos entre clases, principalmente
 * enfocada a llegar a la parte Modelo y la ejecución de queries
 * @author ccatrilef
 */
public class DataTransfer {

    private Integer idQuery;
    private String query;
    private String queryInsert;
    private String queryDelete;
    private String queryUpdate;
    private String strWhere;
    private String tabla;
    private CampoForma  campoForma;
    private Campo campo;
    private String[] arrData;
    private String[][] arrVariables;
    private String orderBY;
    private Object dataObject;

    /**
     * Obtiene un objeto del tipo Object, para soportar cualquier tipo de
     * data que se necesite mantener o enviar y no está catalogada en los otros
     * métodos
     * @return
     */
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * Asigna un objeto del tipo Object, para soportar cualquier tipo de
     * data que se necesite mantener o enviar y no está catalogada en los otros
     * métodos
     * @param dataObject
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * Constructor básico de la clase sin datos
     */
    public DataTransfer() {
        
    }

    /**
     * Obtiene un arreglo con los datos de entrada, enfocado a los datos de la
     * forma %1, %2, %3, etc.
     * @return
     */
    public String[] getArrData() {
        return ((arrData==null)?new String[0]:arrData);
    }

    /**
     * Asigna un arreglo con los datos de entrada, enfocado a los datos de la
     * forma %1, %2, %3, etc.
     * @param arrData
     */
    public void setArrData(String[] arrData) {
        this.arrData = arrData;
    }

    /**
     * Obtiene un arreglo bidimensional, el cual contendrá las variables
     * predefinidas del Usuario, como clave_empleado, clave_perfil, etc
     * @return
     */
    public String[][] getArrVariables() {
        return ((arrVariables==null)?new String[0][0]:arrVariables);
    }

    /**
     * Asigna un arreglo bidimensional, el cual contendrá las variables
     * predefinidas del Usuario, como clave_empleado, clave_perfil, etc
     * @param arrVariables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene un dato del tipo Campo
     * @return
     */
    public Campo getCampo() {
        return campo;
    }

    /**
     * Asigna un dato del tipo Campo
     * @param campo
     */
    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    /**
     * Obtiene un dato del tipo CampoForma
     * @return
     */
    public CampoForma getCampoForma() {
        return campoForma;
    }

    /**
     * Asigna un dato del tipo CampoForma
     * @param campoForma
     */
    public void setCampoForma(CampoForma campoForma) {
        this.campoForma = campoForma;
    }

    /**
     * Obtiene el ID de la query a ejecutar
     * @return
     */
    public Integer getIdQuery() {
        return idQuery;
    }

    /**
     * Asigna el ID de la query a ejecutar
     * @param idQuery
     */
    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    /**
     * Obtiene la query a ejecutar
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna la query a ejecutar
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene una query del tipo DELETE para ejecutar
     * @return
     */
    public String getQueryDelete() {
        return queryDelete;
    }

    /**
     * Asigna una query del tipo DELETE para ejecutar
     * @param queryDelete
     */
    public void setQueryDelete(String queryDelete) {
        this.queryDelete = queryDelete;
    }

    /**
     * Obtiene una query del tipo INSERT para ejecutar
     * @return
     */
    public String getQueryInsert() {
        return queryInsert;
    }

    /**
     * Asigna una query del tipo INSERT para ejecutar
     * @param queryInsert
     */
    public void setQueryInsert(String queryInsert) {
        this.queryInsert = queryInsert;
    }

    /**
     * Obtiene una query del tipo UPDATE para ejecutar
     * @return
     */
    public String getQueryUpdate() {
        return queryUpdate;
    }

    /**
     * Asigna una query del tipo UPDATE para ejecutar
     * @param queryUpdate
     */
    public void setQueryUpdate(String queryUpdate) {
        this.queryUpdate = queryUpdate;
    }

    /**
     * Obtiene el texto WHERE que se debe agregar a una query
     * @return
     */
    public String getStrWhere() {
        return strWhere;
    }

    /**
     * Asigna el texto WHERE que se debe agregar a una query
     * @param strWhere
     */
    public void setStrWhere(String strWhere) {
        this.strWhere = strWhere;
    }

    /**
     * Obtiene la tabla para realizar la operación, usada para validar la
     * estructura de una query del tipo INSERT, UPDATE y DELETE
     * @return
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * Asigna la tabla para realizar la operación, usada para validar la
     * estructura de una query del tipo INSERT, UPDATE y DELETE
     * @param tabla
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Obtiene el texto con el ORDER BY que se debe utilizar en la query
     * @return
     */
    public String getOrderBY() {
        return orderBY;
    }

    /**
     * Asigna el texto con el ORDER BY que se debe utilizar en la query
     * @param orderBY
     */
    public void setOrderBY(String orderBY) {
        this.orderBY = orderBY;
    }

    /**
     * Convierte un arreglo de String[] a un String
     * @param strData   Arreglo que se llevara a String
     * @return  String  Texto con la conversión de los datos
     */
    private String arrayToString(String[] strData){
        StringBuilder sld= new StringBuilder();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\tNro ").append(i+1).append(": ").append(strData[i]);
            }
        }
        return sld.toString();
    }

    /**
     * Convierte un arreglo de String[][] a un String
     * @param strData   Arreglo que se llevara a String
     * @return  String  Texto con la conversión de los datos
     */
    private String arrayDoubleToString(String[][] strData){
        StringBuilder sld= new StringBuilder();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\tNro ").append(i+1).append(":\n\t\t");
                for (int j=0;j<strData[i].length;j++){
                    sld.append("dato[").append(j).append("]: ").append(strData[i][j]).append(" || ");
                }
            }
        }
        return sld.toString();
    }    

    /**
     * Método para convertir a String el contenido del Objeto. Los datos que 
     * estén con valor NULL son ignorados
     * @return  String  Texto con los datos del objeto
     */
    @Override
    public String toString() {
        String str = "";
        str = "DataTransfer{"
                + ((idQuery!=null)?"\n\tIdQuery=" + idQuery:"")
                + ((query!=null)?"\n\tQuery=" + query:"")
                + ((queryInsert!=null)?"\n\tQueryInsert=" + queryInsert:"")
                + ((queryDelete!=null)?"\n\tQueryDelete=" + queryDelete:"")
                + ((queryUpdate!=null)?"\n\tQueryUpdate=" + queryUpdate:"")
                + ((strWhere!=null)?"\n\tStrWhere=" + strWhere:"")
                + ((tabla!=null)?"\n\tTabla=" + tabla:"")
                + ((orderBY!=null)?"\n\tOrderBY=" + orderBY:"")
                + ((dataObject!=null)?"\n\tDataObject=" + dataObject:"")
                + ((campo!=null)?"\n\tCampo=" + campo.toString():"")
                + ((campoForma!=null)?"\n\tCampoForma=" + campoForma.toString():"")
                + ((arrData!=null)?"\n\tArrData=" + arrayToString(arrData):"")
                + ((arrVariables!=null)?"\n\tArrVariables=" + arrayDoubleToString(arrVariables):"")
                + "\n}";
        return str;
    }

    /**
     * Método similar al toString(), para convertir a String el contenido del Objeto.
     * Los datos que estén con valor NULL y los objetos Campo y CampoForma
     * son ignorados
     * @return  String  Texto con los datos del objeto
     */
    public String toStringSimple() {
        String str = "";
        str = "DataTransfer{"
                + ((idQuery!=null)?"\n\tIdQuery=" + idQuery:"")
                + ((query!=null)?"\n\tQuery=" + query:"")
                + ((queryInsert!=null)?"\n\tQueryInsert=" + queryInsert:"")
                + ((queryDelete!=null)?"\n\tQueryDelete=" + queryDelete:"")
                + ((queryUpdate!=null)?"\n\tQueryUpdate=" + queryUpdate:"")
                + ((strWhere!=null)?"\n\tStrWhere=" + strWhere:"")
                + ((tabla!=null)?"\n\tTabla=" + tabla:"")
                + ((orderBY!=null)?"\n\tOrderBY=" + orderBY:"")
                + ((dataObject!=null)?"\n\tDataObject=" + dataObject:"")
                + ((arrData!=null)?"\n\tArrData=" + arrayToString(arrData):"")
                + ((arrVariables!=null)?"\n\tArrVariables=" + arrayDoubleToString(arrVariables):"")
                + "\n}";
        return str;
    }
}
