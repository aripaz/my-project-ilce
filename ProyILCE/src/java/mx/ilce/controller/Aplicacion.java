package mx.ilce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 *  Clase para la implementacion de los metodos asociados a la aplicacion
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
    private boolean cleanIncrement;

    /**
     * Constructor Basico de la clase, inicializa las variables de la clase
     */
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
        this.cleanIncrement=false;
    }

/********* GETTER Y SETTER *********/

    /**
     * Indica mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @return
     */
    public boolean isCleanIncrement() {
        return cleanIncrement;
    }

    /**
     * Asigna mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @param cleanIncrement    Estado de la validacion
     */
    public void setCleanIncrement(boolean cleanIncrement) {
        this.cleanIncrement = cleanIncrement;
    }

    /**
     * Obtiene el Array de data ingresado al objeto
     * @return
     */
    public String[] getArrayData() {
        return arrayData;
    }

    /**
     * Asigna el Array de data a ingresar al objeto
     * @param arrayData     Data a asignar
     */
    public void setArrayData(String[] arrayData) {
        this.arrayData = arrayData;
    }

    /**
     * Obtiene el numero de pagina que se debe visualizar en la aplicacion
     * @return
     */
    public Integer getNumPage() {
        return numPage;
    }

    /**
     * Asigna el numero de pagina que se debe visualizar en la aplicacion
     * @param numPage   Numero de pagina, por defecto es 1
     */
    public void setNumPage(String numPage) {
        if ((numPage==null)||("".equals(numPage))) {
            numPage = "1";
        }
        this.numPage = Integer.valueOf(numPage);
    }

    /**
     * Obtiene el numero de filas que se deben desplegar
     * @return
     */
    public Integer getNumRows() {
        return numRows;
    }

    /**
     * Asigna el numero de filas que se deben desplegar
     * @param numRows   Numero de filas, por defecto es 10
     */
    public void setNumRows(String numRows) {
        if ((numRows==null)||("".equals(numRows))){
            numRows = "10";
        }
        this.numRows = Integer.valueOf(numRows);
    }

    /**
     * Obtiene el texto adicional que se incluira en la query
     * @return
     */
    public String getStrWhereQuery() {
        return strWhereQuery;
    }

    /**
     * Asigna el texto adicional que se incluira en la query
     * @param strWhereQuery     Estructura de query adicional
     */
    public void setStrWhereQuery(String strWhereQuery) {
        this.strWhereQuery = strWhereQuery;
    }

    /**
     * Obtiene la clave de la forma
     * @return
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Asigna la clave de la forma
     * @param claveForma    Clave de la forma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene el display de la forma (HEADER o BODY) que se debe utilizar
     * @return
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Asigna el display de la forma (HEADER o BODY) que se debe utilizar
     * @param display   Display a utilizar
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Obtiene el tipo de accion que se esta efectuando con la Forma
     * @return
     */
    public String getTipoAccion() {
        return tipoAccion;
    }

    /**
     * Asigna el tipo de accion que se esta efectuando con la Forma
     * @param tipoAccion    Tipo de accion declarada
     */
    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    /**
     * Obtiene el Alias del MenuMostrarEntidad
     * @return
     */
    public String getAliasMenuMostrarEntidad() {
        return aliasMenuMostrarEntidad;
    }

    /**
     * Asigna el Alias del MenuMostrarEntidad
     * @param aliasMenuMostrarEntidad   Alias utilizado
     */
    public void setAliasMenuMostrarEntidad(String aliasMenuMostrarEntidad) {
        this.aliasMenuMostrarEntidad = aliasMenuMostrarEntidad;
    }

    /**
     * Obtiene el Alias del MenuNuevaEntidad
     * @return
     */
    public String getAliasMenuNuevaEntidad() {
        return aliasMenuNuevaEntidad;
    }

    /**
     * Asigna el Alias del MenuNuevaEntidad
     * @param aliasMenuNuevaEntidad     Alias utilizado
     */
    public void setAliasMenuNuevaEntidad(String aliasMenuNuevaEntidad) {
        this.aliasMenuNuevaEntidad = aliasMenuNuevaEntidad;
    }

    /**
     * Obtiene el nombre de la aplicacion
     * @return
     */
    public String getAplicacion() {
        return aplicacion;
    }

    /**
     * Asigna el nombre de la aplicacion
     * @param aplicacion    Nombre de la aplicacion
     */
    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    /**
     * Obtiene la clave de la aplicacion
     * @return
     */
    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Asigna la clave de la aplicacion
     * @param claveAplicacion   Clave de la aplicacion
     */
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    /**
     * Obtiene la clave de la Forma principal
     * @return
     */
    public Integer getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    /**
     * Asigna la clave de la Forma principal
     * @param claveFormaPrincipal   Clave de la forma principal
     */
    public void setClaveFormaPrincipal(Integer claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    /**
     * Obtiene la descripcion
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripcion
     * @param descripcion   Descripcion asignada
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//************** OPERACIONES DE ENTIDAD **********

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * NO IMPLEMENTADO
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion mostrarResultado(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion ingresarBusquedaSencilla(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion ingresarBusquedaAvanzada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion mostrarPanel(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion guardarBusqueda(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion rescatarBusqueda(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public Aplicacion eliminarBusqueda() {
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
     * @throws ExceptionHandler
     */
    public Aplicacion mostrarForma() throws ExceptionHandler{
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
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para Mostrar la Forma");
        }finally{

        }
        return this;
    }

    /**
     * Obtiene las columnas de una grilla XML a partir de los datos asignados a
     * los campos ClaveForma, TipoAccion, ArrayData, strWhereQuery y Forma
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer getHeaderGrid() throws ExceptionHandler{
        StringBuffer strSld = null;
        try{
            AdminXML adm = new AdminXML();
            ConEntidad con = new ConEntidad();
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
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener Header Grid");
        }
        return strSld;
    }

    /**
     * Obtiene una grilla XML a partir de los datos asignados: ClaveForma,
     * TipoAccion, strWhereQuery, NumPage y NumRows
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer getHeaderAndBodyGrid() throws ExceptionHandler{
        StringBuffer strSld = null;
        try{
            ConEntidad con = new ConEntidad();
            AdminXML adm = new AdminXML();
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
            if (this.isCleanIncrement()){
                adm.setDeleteIncrement(cleanIncrement);
            }
            List lstF = (List) this.getForma(this.getClaveForma());
            strSld = adm.getGridByData(hsCmp,lstF,this.getNumPage(),this.getNumRows());
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener Header y Body Grid");
        }
        return strSld;
    }
}