/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ccatrilef
 */
public class HashCampo implements Serializable  {

    private Integer lengthCampo;
    private Integer lengthData;
    private HashMap listData;
    private List listCampos;
    private HashMap listCampoByCod;
    private HashMap listCampoByName;
    private HashMap listAlias;

    /**
     * Agregamos un registro (linea) al listado de Data
     * @param lst
     * @param codigo
     */
    public void addListData(List lst, Integer codigo){
        this.listData.put(codigo, lst);
        setLengthData(this.listData.size());
    }

    /**
     * Obtenemos un campo por medio de su codigo
     * @param codigo
     * @return
     */
    public Campo getCampoByCod(Integer codigo){
        HashMap hm = this.listCampoByCod;
        return (Campo) hm.get(codigo);
    }

    /**
     * Agregamos un Campo, ordenandolo por su codigo
     * @param cmp
     */
    private void addCampoByCod(Campo cmp){
        this.listCampoByCod.put(cmp.getCodigo(), cmp);
    }

    /**
     * Obtenemos un Campo por medio de su alias
     * @param alias
     * @return
     */
    public Campo getCampoByAlias(String alias){
        HashMap hm = this.listAlias;
        Integer codigo = (Integer) hm.get(alias);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guardamos el codigo de un campo, ordenandolo por el alias
     * @param cmp
     */
    private void addCampoByAlias(Campo cmp){
        this.listAlias.put(cmp.getAlias(), cmp.getCodigo());
    }

    /**
     * Obtenemos un campo por su nombre
     * @param name
     * @return
     */
    public Campo getCampoByName(String name){
        HashMap hm = this.listCampoByName;
        Integer codigo = (Integer) hm.get(name);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guardamos el codigo de un campo ordenandolo por su nombre
     * @param cmp
     */
    private void addCampoByName(Campo cmp){
        this.listCampoByName.put(cmp.getNombre(), cmp.getCodigo());
    }

    /**
     * guardamos un campo ordenado por Alias, Nombre y Codigo.
     * @param cmp
     */
    public void addCampo(Campo cmp){
        this.listCampos.add(cmp);
        addCampoByCod(cmp);
        addCampoByName(cmp);
        if (cmp.getAlias()!=null){
            addCampoByAlias(cmp);
        }
        setLengthCampo(this.listCampos.size());
    }

//----------------------------------------------
//  GETTER Y SETTER

    /**
     * Obtiene el numero de campos que posee cada registro
     * @return
     */
    public Integer getLengthCampo() {
        return lengthCampo;
    }

    /**
     * Asigna el numero de campos que posee cada registro
     * @param lengthCampo
     */
    public void setLengthCampo(Integer lengthCampo) {
        this.lengthCampo = lengthCampo;
    }

    /**
     * Obtiene el numero de registros de un listado
     * @return
     */
    public Integer getLengthData() {
        return lengthData;
    }

    /**
     * Asigna el numero de registros que posee un listado
     * @param lengthData
     */
    public void setLengthData(Integer lengthData) {
        this.lengthData = lengthData;
    }

    /**
     * Obtiene el listado con los registros de la data recuperada en una query
     * @return
     */
    public HashMap getListData() {
        return listData;
    }

    /**
     * Asigna un listado con los registros recuperados por medio de una query
     * @param listData
     */
    public void setListData(HashMap listData) {
        this.listData = listData;
    }

    /**
     * Entrega el listado de nombres de campo que se usaron para obtener un
     * listado de registros de una query
     * @return
     */
    public List getListCampos() {
        return listCampos;
    }

    /**
     * Asigna un listado con los nombres de campo que se usaron para obtener un
     * listado de registros mediante una query
     * @param listCampos
     */
    public void setListCampos(List listCampos) {
        this.listCampos = listCampos;
    }

    /**
     * Constructor de la clase, donde se le entregan los distintos elementos
     * que contiene la clase, su contenido depende de la query que se ejecuto
     * para su llenado
     * @param lengthCampo   Cantidad de campos que posee la query
     * @param lengthData    Cantidad de registros obtenidos por la query
     * @param listData      Registros obtenidos mediante la query
     * @param listCampos    Campos que posee la query
     * @param listCampoByCod    Campos que posee la query, ordenados por codigo
     * @param listCampoByName   Campos que posee la query, ordenados por nombre
     * @param listAlias     Alias de los campos obtenidos por la query,
     * generalmente se usaran para mostrarlo como titulo o encabezado en las
     * paginas
     */
    public HashCampo(Integer lengthCampo, Integer lengthData, HashMap listData, List listCampos, HashMap listCampoByCod, HashMap listCampoByName, HashMap listAlias) {
        this.lengthCampo = lengthCampo;
        this.lengthData = lengthData;
        this.listData = listData;
        this.listCampos = listCampos;
        this.listCampoByCod = listCampoByCod;
        this.listCampoByName = listCampoByName;
        this.listAlias = listAlias;
    }

    /**
     * Constructor de la clase el cual inicializa los valores de los campos
     * utilizados, para indicar que no se poseen datos.
     */
    public HashCampo() {
        this.lengthCampo = Integer.valueOf(0);
        this.lengthData = Integer.valueOf(0);
        this.listData = new HashMap();
        this.listCampos = new ArrayList();
        this.listCampoByCod = new HashMap();
        this.listCampoByName = new HashMap();
        this.listAlias = new HashMap();
    }

}
