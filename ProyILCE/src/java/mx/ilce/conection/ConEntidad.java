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

    public ConEntidad(){
        try{
            this.prop = adm.leerIdQuery();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Integer getIdQuery(String key) throws Exception{
        if (prop == null){
            prop = adm.leerIdQuery();
        }
        return adm.getIdQuery(prop,key);
    }


    public void ingresaEntidad(){

    }

    public void eliminaEntidad(){

    }

    public void actualizaEntidad(){

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
            //HashCampo hsCmp = connQ.getData(11, strData);
            HashCampo hsCmp = connQ.getData(getIdQuery(adm.FORMACAMPOS), strData);
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
            //HashCampo hsCmp = connQ.getData(12, strData);
            HashCampo hsCmp = connQ.getData(getIdQuery(adm.FORMA), strData);
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
}
