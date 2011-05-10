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

/**
 * Clase utilizada para realizar las consultas a la base de datos desde
 * la aplicacion
 * @author ccatrilef
 */
public class ConEntidad {

    private Properties prop = null;
    private AdminFile adm = new AdminFile();
    private String query = "";
    private HashCampo hashCampo = new HashCampo();
    private CampoForma campoForma = new CampoForma();

    public ConEntidad(){
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Integer getIdQuery(String key) throws Exception{
        if (prop == null){
            prop = AdminFile.leerIdQuery();
        }
        return adm.getIdQuery(prop,key);
    }


    public void ingresaEntidad() throws Exception{
        ConQuery con = new ConQuery();
        HashCampo hs = con.executeInsert(this.campoForma,this.query);
        this.hashCampo = hs;
    }

    public void eliminaEntidad() throws Exception{
        ConQuery con = new ConQuery();
        HashCampo hs = con.executeDelete(this.campoForma,this.query);
        this.hashCampo = hs;
    }

    public void editarEntidad() throws Exception{
        ConQuery con = new ConQuery();
        HashCampo hs = con.executeUpdate(this.campoForma,this.query);
        this.hashCampo = hs;
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
    public List getListFormaByIdAndCampos(String[] strData){
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
            ex.printStackTrace();
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
    public List getListFormaById(String[] strData){
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
            ex.printStackTrace();
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
    public HashCampo getDataByQuery(String strQuery, String[] strData){
        HashCampo hsCmp = new HashCampo();
        ConQuery con = new ConQuery();
        try{
            hsCmp = con.getDataByQuery(strQuery, strData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return hsCmp;
    }

    public String getCampoPK(String tabla){
        String campo = "";
        ConQuery con = new ConQuery();
        try{
            campo = con.getCampoPK(tabla);
        }catch(Exception e){
            e.printStackTrace();
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
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData ){
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getData(IdQuery, strData);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{

        }
        return hsCmp;
    }

     /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData   Arreglo con la data de entrada que se debe usar en la
     * Query
     * @return
     */
    public HashCampo getDataByIdQueryAndWhere(Integer IdQuery, String strData ){
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getDataWithWhere(IdQuery, strData);
        }catch(Exception ex){
            ex.printStackTrace();
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
