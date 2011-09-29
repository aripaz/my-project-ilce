package mx.ilce.bean;

/**
 * Clase implementada para la tranmision de datos entre clases, principalmente 
 * enfocada a llegar a la parte Modelo y la ejecucion de queries
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
    private String rutaFile;

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene un objeto del tipo Object, para soportar cualquier tipo de
     * data que se necesite mantener o enviar y no esta catalogada en los otros
     * metodos
     * @return
     */
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * Asigna un objeto del tipo Object, para soportar cualquier tipo de
     * data que se necesite mantener o enviar y no esta catalogada en los otros
     * metodos
     * @param dataObject
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * Constructor basico de la clase
     */
    public DataTransfer() {
        
    }

    /**
     * Obtiene un arreglo con los datos de entrada, enfocado a los datos de la
     * forma %1, %2, %3, etc
     * @return
     */
    public String[] getArrData() {
        return ((arrData==null)?new String[0]:arrData);
    }

    /**
     * Asigna un arreglo con los datos de entrada, enfocado a los datos de la
     * forma %1, %2, %3, etc
     * @param arrData
     */
    public void setArrData(String[] arrData) {
        this.arrData = arrData;
    }

    /**
     * Obtiene un arreglo bidimensional, el cual contendra las variables
     * predefinidas del Usuario, como clave_empleado, clave_perfil, etc
     * @return
     */
    public String[][] getArrVariables() {
        return ((arrVariables==null)?new String[0][0]:arrVariables);
    }

    /**
     * Asigna un arreglo bidimensional, el cual contendra las variables
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
     * Obtiene la tabla para realizar la operacion, usada para validar la
     * estructura de una query del tipo INSERT, UPDATE y DELETE
     * @return
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * Asigna la tabla para realizar la operacion, usada para validar la
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

}
