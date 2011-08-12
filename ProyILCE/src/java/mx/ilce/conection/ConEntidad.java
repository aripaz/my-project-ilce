package mx.ilce.conection;

import java.util.List;
import java.util.Properties;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.component.ListHash;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase utilizada para realizar las consultas a la base de datos desde
 * la aplicacion
 * @author ccatrilef
 */
public class ConEntidad {

    private Properties prop = null;
    private String query = "";
    private String queryDel = "";
    private HashCampo hashCampo = new HashCampo();
    private CampoForma campoForma = new CampoForma();
    private Bitacora bitacora;

    /**
     * Constructor basico de la clase, al crearse se cargan los datos del
     * properties
     * @throws ExceptionHandler
     */
    public ConEntidad() throws ExceptionHandler{
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para abrir Conexion ConSession");
        }
    }

    /**
     * Obtiene el IDQUERY desde el properties de queries
     * @param key   clave que se esta buscando
     * @return
     * @throws ExceptionHandler
     */
    public Integer getIdQuery(String key) throws ExceptionHandler{
        Integer intSld = new Integer(0);
        try{
            if (prop == null){
                prop = AdminFile.leerIdQuery();
            }
            intSld = AdminFile.getIdQuery(prop,key);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener ID QUERY desde properties");
        }
        return intSld;
    }

    /**
     * Se ejecuta la insercion de los datos configurados en el objeto
     * (CampoForma y Query)
     * @throws ExceptionHandler
     */
    public void ingresaEntidad() throws ExceptionHandler{
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeInsert(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Ingresar la Entidad");
        }
    }

    /**
     * Se ejecuta la eliminacion de los datos configurados en el objeto
     * (CampoForma y Query)
     * @throws ExceptionHandler
     */
    public void eliminaEntidad() throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeDelete(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Eliminar la Entidad");
        }
    }

    /**
     * Se ejecuta la edicion de los datos configurados en el objeto
     * (CampoForma y Query)
     * @throws ExceptionHandler
     */
    public void editarEntidad() throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeUpdate(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Editar la Entidad");
        }
    }

    public void ingresarDataPermisos() throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeDeleteInsert(this.campoForma,this.queryDel,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Borrar y Agregar Permisos de la Entidad");
        }
    }

    /**
     * NO IMPLEMENTADO
     */
    public void obtieneEntidad(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public List obtieneMenu(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Obtiene la configuracion de la formas a partir del ID de la misma  y los
     * campos que se estan buscando
     * @param strData   Debe contener dos parametros, el ID de la Forma y un
     * listado de String con los nombres de los campos que se quieren obtener.
     * Con esto se evita traer la forma completa.
     * @return
     * @throws ExceptionHandler
     */
    public List getListFormaByIdAndCampos(String[] strData, String[][] arrVariables) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.FORMACAMPOS), strData, arrVariables);
            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                lstSld = lst.getListBean(CampoForma.class, hsCmp);
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el listado de Formas por ID y Campos");
        }finally{

        }
        return lstSld;
    }

    /**
     * Obtiene la configuracion de una forma a partir del ID de la misma,
     * entregando un Listado de Bean del tipo CampoForma
     * @param strData   Debe contener el ID de la forma a buscar
     * @return
     * @throws ExceptionHandler
     */
    public List getListFormaById(String[] strData, String[][] arrVariables) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.FORMA), strData, arrVariables);
            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                lstSld = lst.getListBean(CampoForma.class, hsCmp);
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el listado de Formas por ID");
        }finally{

        }
        return lstSld;
    }

    /**
     * Obtiene la data generada a partir de la Query entregada y los parametros
     * de entrada que deben utilizarce con la Query
     * @param strQuery  Query que se desea ejecutar
     * @param strData   Datos de entrada para la query
     * @return
     * @throws ExceptionHandler
     */
    public HashCampo getDataByQuery(String strQuery, String[] strData, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            hsCmp = con.getDataByQuery(strQuery, strData, arrVariables);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener Datos por una QUERY");
        }
        return hsCmp;
    }

    /**
     * Entrega el campo PK de una Tabla
     * @param tabla     Nombre de la tabla donde se buscara el PK
     * @return
     * @throws ExceptionHandler
     */
    public String getCampoPK(String tabla) throws ExceptionHandler{
        String campo = "";
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            campo = con.getCampoPK(tabla);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el Campo PK de una tabla");
        }
        return campo;
    }

     /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData   Arreglo con la data de entrada que se debe usar en la
     * Query
     * @return
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData, String[][] arrVariables ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            hsCmp = connQ.getData(IdQuery, strData, arrVariables);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener datos por el ID QUERY");
        }finally{

        }
        return hsCmp;
    }

     /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData   Condicionador a aplicar a la query
     * Query
     * @return
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQueryAndWhere(Integer IdQuery, String strData, String[][] arrVariables ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            hsCmp = connQ.getDataWithWhere(IdQuery, strData, arrVariables);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener datos por el ID QUERY y WHERE");
        }finally{

        }
        return hsCmp;
    }

    /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strWhere  Condicionador a aplicar a la query
     * @param strData   Arreglo con la data de entrada que se debe usar en la
     * query
     * @return
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQueryAndWhereAndData(Integer IdQuery, String strWhere, String[] strData, String[][] arrVariables )
            throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            
            hsCmp = connQ.getDataWithWhereAndData(IdQuery,strWhere,strData, arrVariables);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener datos por el ID QUERY, WHERE y DATA");
        }finally{

        }
        return hsCmp;
    }


    /**
     * Obtiene al query de borrado
     * @return
     */
    public String getQueryDel() {
        return queryDel;
    }

    /**
     * Asigna la query de borrado
     * @param queryDel
     */
    public void setQueryDel(String queryDel) {
        this.queryDel = queryDel;
    }

    /**
     * Obtiene la query contenida en el objeto
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna la query contenida en el objeto
     * @param query     Texto de la query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene el objeto de tipo HashCampo
     * @return
     */
    public HashCampo getHashCampo() {
        return hashCampo;
    }

    /**
     * Asigna el objeto de tipo HashCampo
     * @param hashCampo     HashCampo a asignar
     */
    public void setHashCampo(HashCampo hashCampo) {
        this.hashCampo = hashCampo;
    }

    /**
     * Obtiene el campoForma
     * @return
     */
    public CampoForma getCampoForma() {
        return campoForma;
    }

    /**
     * Asigna el campoForma
     * @param campoForma    CampoForma a asignar
     */
    public void setCampoForma(CampoForma campoForma) {
        this.campoForma = campoForma;
    }

    /**
     * Obtiene el objeto bitacora
     * @return
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto bitacora
     * @param bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

}
