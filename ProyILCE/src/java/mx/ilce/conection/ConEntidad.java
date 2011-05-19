/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
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
    private HashCampo hashCampo = new HashCampo();
    private CampoForma campoForma = new CampoForma();

    public ConEntidad() throws ExceptionHandler{
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para abrir Conexion ConSession");
        }
    }

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


    public void ingresaEntidad() throws ExceptionHandler{
        try{
            ConQuery con = new ConQuery();
            HashCampo hs = con.executeInsert(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Ingresar la Entidad");
        }
    }

    public void eliminaEntidad() throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            HashCampo hs = con.executeDelete(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Eliminar la Entidad");
        }
    }

    public void editarEntidad() throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            HashCampo hs = con.executeUpdate(this.campoForma,this.query);
            this.hashCampo = hs;
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Editar la Entidad");
        }
    }

    public void obtieneEntidad(){

    }

    public List obtieneMenu(){
        List lst = new ArrayList();

        for (int i=0;i<10;i++){
            Campo cmp = new Campo();
            cmp.setNombre("Nombre"+i);
            cmp.setAlias("Alias"+i);
            cmp.setValor(String.valueOf(i));
            lst.add(cmp);
        }
        return lst;
    }

    /**
     * Obtiene la configuracion de la formas a partir del ID de la misma  y los
     * campos que se estan buscando
     * @param strData   Debe contener dos parametros, el ID de la Forma y un
     * listado de String con los nombres de los campos que se quieren obtener.
     * Con esto se evita traer la forma completa.
     * @return
     */
    public List getListFormaByIdAndCampos(String[] strData) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.FORMACAMPOS), strData);
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
     */
    public List getListFormaById(String[] strData) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.FORMA), strData);
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
     * @param strQuery
     * @param strData
     * @return
     */
    public HashCampo getDataByQuery(String strQuery, String[] strData) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery con = new ConQuery();
            hsCmp = con.getDataByQuery(strQuery, strData);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener Datos por una QUERY");
        }
        return hsCmp;
    }

    /**
     * Entrega el campo PK de una Tabla
     * @param tabla
     * @return
     * @throws ExceptionHandler
     */
    public String getCampoPK(String tabla) throws ExceptionHandler{
        String campo = "";
        try{
            ConQuery con = new ConQuery();
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
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getData(IdQuery, strData);
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
     */
    public HashCampo getDataByIdQueryAndWhere(Integer IdQuery, String strData ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getDataWithWhere(IdQuery, strData);
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
     */
    public HashCampo getDataByIdQueryAndWhereAndData(Integer IdQuery, String strWhere, String[] strData ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getDataWithWhereAndData(IdQuery,strWhere,strData);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener datos por el ID QUERY, WHERE y DATA");
        }finally{

        }
        return hsCmp;
    }

/********************* GETTER AND SETTER ******************/

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public HashCampo getHashCampo() {
        return hashCampo;
    }

    public void setHashCampo(HashCampo hashCampo) {
        this.hashCampo = hashCampo;
    }

    public CampoForma getCampoForma() {
        return campoForma;
    }

    public void setCampoForma(CampoForma campoForma) {
        this.campoForma = campoForma;
    }

}
