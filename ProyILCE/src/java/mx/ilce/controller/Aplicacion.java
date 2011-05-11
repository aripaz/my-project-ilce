/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExecutionHandler;

/**
 *  Clase pra la implementacion de los metodos asociados a la aplicacion
 * @author ccatrilef
 */
public class Aplicacion extends Entidad {

    private Integer claveAplicacion;
    private String aplicacion;
    private Integer claveFormaPrincipal;
    private String descripcion;
    private String aliasMenuNuevaEntidad;
    private String aliasMenuMostrarEntidad;
    private Integer claveForma;
    private String tipoAccion;
    private String display;
    private String strWhereQuery;
    private HashMap hsForma;
    private Integer numPage;
    private Integer numRows;
    private String[] arrayData;

    public Aplicacion() {
        this.claveAplicacion = 0;
        this.aplicacion = "";
        this.claveFormaPrincipal = 0;
        this.descripcion = "";
        this.aliasMenuNuevaEntidad = "";
        this.aliasMenuMostrarEntidad = "";
        this.claveForma = 0;
        this.tipoAccion = "";
        this.display = "";
        this.strWhereQuery = "";
        this.hsForma = new HashMap();
        this.numPage = 1;
        this.numRows = 10;
    }

/********* GETTER Y SETTER *********/
    public String[] getArrayData() {
        return arrayData;
    }

    public void setArrayData(String[] arrayData) {
        this.arrayData = arrayData;
    }

    public Integer getNumPage() {
        return numPage;
    }

    public void setNumPage(String numPage) {
        if ((numPage==null)||("".equals(numPage))) {
            numPage = "1";
        }
        this.numPage = Integer.valueOf(numPage);
    }

    public Integer getNumRows() {
        return numRows;
    }

    public void setNumRows(String numRows) {
        if ((numRows==null)||("".equals(numRows))){
            numRows = "10";
        }
        this.numRows = Integer.valueOf(numRows);
    }

    public String getStrWhereQuery() {
        return strWhereQuery;
    }

    public void setStrWhereQuery(String strWhereQuery) {
        this.strWhereQuery = strWhereQuery;
    }

    public Integer getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getAliasMenuMostrarEntidad() {
        return aliasMenuMostrarEntidad;
    }

    public void setAliasMenuMostrarEntidad(String aliasMenuMostrarEntidad) {
        this.aliasMenuMostrarEntidad = aliasMenuMostrarEntidad;
    }

    public String getAliasMenuNuevaEntidad() {
        return aliasMenuNuevaEntidad;
    }

    public void setAliasMenuNuevaEntidad(String aliasMenuNuevaEntidad) {
        this.aliasMenuNuevaEntidad = aliasMenuNuevaEntidad;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public Integer getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    public void setClaveFormaPrincipal(Integer claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

/******* OPERACIONES DE ENTIDAD ******/

    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Agrega una forma (con formato List) al Hash de formas, asignandole como
     * Key, el ID de la Forma. Si existia una forma con la misma Key, esta es
     * reemplazada
     * @param key   ID de la Forma
     * @param obj   Listado de la forma a agregar
     */
    public void addForma(Integer key, List obj){
        this.hsForma.put(key, obj);
    }

    /**
     * Obtiene, mediante el ID introducido, una forma desde el listado de formas,
     * que se asociaron cuando se obtuvo el prfil del Usuario.  El resultado es
     * un listado con los campos de la forma.
     * @param key   ID de la forma a solicitar
     * @return
     */
    public List getForma(Integer key){
        List hs = (List) this.hsForma.get(key);
        return hs;
    }

    /**
     * Asigna el XML obtenido con referencia a los datos introducidos al
     * controlador
     * @return
     */
    public Aplicacion mostrarForma() {
        try{
            StringBuffer xmlForma = new StringBuffer("");
            if (this.getDisplay().toUpperCase().equals("HEADER")){
                xmlForma = this.getHeaderGrid();
            }else if (this.getDisplay().toUpperCase().equals("BODY")){
                xmlForma = this.getHeaderAndBodyGrid();
            }else{
                xmlForma = this.getHeaderGrid();
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    private StringBuffer getHeaderGrid(){
        StringBuffer strSld = null;
        ConEntidad con = new ConEntidad();
        AdminXML adm = new AdminXML();
        try{
            String[] strData = new String[2];
            HashCampo hsCmp = new HashCampo();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());
                HashCampo hsCmpQ = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMAQUERY), strData);
                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);

                    if ((this.getStrWhereQuery()!=null)&&(this.getArrayData()==null)){
                        strData = new String[1];
                        strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());
                        hsCmp = con.getDataByIdQueryAndWhere(Integer.valueOf(cmpAux.getValor()), strData[0]);
                    }else if ((this.getStrWhereQuery()==null)&&(this.getArrayData()!=null)){
                        hsCmp = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()), this.getArrayData());
                    }else if ((this.getStrWhereQuery()!=null)&&(this.getArrayData()!=null)){
                        strData = new String[1];
                        strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());
                         hsCmp = con.getDataByIdQueryAndWhereAndData(Integer.valueOf(cmpAux.getValor()),strData[0],this.getArrayData());
                    }else{
                        strData = new String[0];
                        hsCmp = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()),strData);
                    }
                }
            }
             List lstF = (List) this.getForma(this.getClaveForma());
            strSld = adm.getGridColumByData(hsCmp,lstF);

        }catch(Exception e){

        }
        return strSld;
    }

    private StringBuffer getHeaderAndBodyGrid(){
        StringBuffer strSld = null;
        ConEntidad con = new ConEntidad();
        AdminXML adm = new AdminXML();
        try{
            String[] strData = new String[2];
            HashCampo hsCmp = new HashCampo();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());
                HashCampo hsCmpQ = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMAQUERY), strData);
                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    strData = new String[1];
                    strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());
                    hsCmp = con.getDataByIdQueryAndWhere(Integer.valueOf(cmpAux.getValor()), strData[0]);
                }
            }
            List lstF = (List) this.getForma(this.getClaveForma());
            strSld = adm.getGridByData(hsCmp,lstF,this.getNumPage(),this.getNumRows());
        }catch(Exception e){

        }
        return strSld;
    }

    public Aplicacion mostrarResultado() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }
    
    public Aplicacion ingresarBusquedaSencilla() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion ingresarBusquedaAvanzada() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion mostrarPanel(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion guardarBusqueda(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion rescatarBusqueda(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion eliminarBusqueda() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    
}
