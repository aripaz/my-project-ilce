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

    public DataTransfer() {
        
    }

    public String[] getArrData() {
        return arrData;
    }

    public void setArrData(String[] arrData) {
        this.arrData = arrData;
    }

    public String[][] getArrVariables() {
        return arrVariables;
    }

    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public CampoForma getCampoForma() {
        return campoForma;
    }

    public void setCampoForma(CampoForma campoForma) {
        this.campoForma = campoForma;
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

    public String getQueryDelete() {
        return queryDelete;
    }

    public void setQueryDelete(String queryDelete) {
        this.queryDelete = queryDelete;
    }

    public String getQueryInsert() {
        return queryInsert;
    }

    public void setQueryInsert(String queryInsert) {
        this.queryInsert = queryInsert;
    }

    public String getQueryUpdate() {
        return queryUpdate;
    }

    public void setQueryUpdate(String queryUpdate) {
        this.queryUpdate = queryUpdate;
    }

    public String getStrWhere() {
        return strWhere;
    }

    public void setStrWhere(String strWhere) {
        this.strWhere = strWhere;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }


}
