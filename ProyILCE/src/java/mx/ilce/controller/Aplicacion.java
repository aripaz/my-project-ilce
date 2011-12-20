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
package mx.ilce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 * Clase para la implementación de los métodos asociados a la aplicación
 * @author ccatrilef
 */
public class Aplicacion extends Entidad {

    private Integer claveEmpleado;
    private Integer claveAplicacion;
    private Integer clavePerfil;
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
    private String[][] arrVariables;
    private Bitacora bitacora;
    private String orderBY;

    /**
     * Obtiene la clave del Perfil
     * @return
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clave del Perfil
     * @param clavePerfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene el texto "Order By" que se usara en la query
     * @return  String  Texto del "Order By"
     */
    public String getOrderBY() {
        return orderBY;
    }

    /**
     * Asigna el texto "Order BY" que se usara en la query
     * @param orderBY   Texto del "Order By"
     */
    public void setOrderBY(String orderBY) {
        this.orderBY = orderBY;
    }

    /**
     * Constructor básico de la clase, inicializa las variables de la clase
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

    /**
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora  Objeto Bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el arreglo de variables
     * @return  String[][]  Matriz con variables
     */
    public String[][] getArrVariables() {
        return arrVariables;
    }

    /**
     * Asigna el arreglo de variables
     * @param arrVariables  Matriz con variables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene la clave del empleado
     * @return  Integer     Clave del empleado
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Asigna la clave del empleado
     * @param claveEmpleado     Clave del empleado
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Indica mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @return  boolean     Estado de la validación
     */
    public boolean isCleanIncrement() {
        return cleanIncrement;
    }

    /**
     * Asigna mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @param cleanIncrement    Estado de la validación
     */
    public void setCleanIncrement(boolean cleanIncrement) {
        this.cleanIncrement = cleanIncrement;
    }

    /**
     * Obtiene el Array de data ingresado al objeto
     * @return  String[]    data de entrada
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
     * Obtiene el número de página que se debe visualizar en la aplicación
     * @return  Integer     Número de la página
     */
    public Integer getNumPage() {
        return numPage;
    }

    /**
     * Asigna el número de página que se debe visualizar en la aplicación
     * @param numPage   Número de página, por defecto es 1
     */
    public void setNumPage(String numPage) {
        if ((numPage==null)||("".equals(numPage))) {
            numPage = "1";
        }
        this.numPage = Integer.valueOf(numPage);
    }

    /**
     * Obtiene el número de filas que se deben desplegar
     * @return      Integer     Número de filas
     */
    public Integer getNumRows() {
        return numRows;
    }

    /**
     * Asigna el número de filas que se deben desplegar
     * @param numRows   Número de filas, por defecto es 10
     */
    public void setNumRows(String numRows) {
        if ((numRows==null)||("".equals(numRows))){
            numRows = "10";
        }
        this.numRows = Integer.valueOf(numRows);
    }

    /**
     * Obtiene el texto adicional que se incluirá en la query
     * @return  String  Texto con el "where" para ser usado en la query
     */
    public String getStrWhereQuery() {
        return strWhereQuery;
    }

    /**
     * Asigna el texto adicional que se incluirá en la query
     * @param strWhereQuery     Texto con el "where" para ser usado en la query
     */
    public void setStrWhereQuery(String strWhereQuery) {
        this.strWhereQuery = strWhereQuery;
    }

    /**
     * Obtiene la clave de la forma
     * @return  Integer     Clave de la forma
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
     * @return  String  Valor del Display a utilizar
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Asigna el display de la forma (HEADER o BODY) que se debe utilizar
     * @param display   Valor del Display a utilizar
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Obtiene el tipo de acción que se está efectuando con la Forma
     * @return  String  Tipo de acción declarada
     */
    public String getTipoAccion() {
        return tipoAccion;
    }

    /**
     * Asigna el tipo de acción que se está efectuando con la Forma
     * @param tipoAccion    Tipo de acción declarada
     */
    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    /**
     * Obtiene el Alias del MenuMostrarEntidad
     * @return  String  Alias utilizado
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
     * @return  String  Alias utilizado
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
     * Obtiene el nombre de la aplicación
     * @return  String  Nombre de la aplicación
     */
    public String getAplicacion() {
        return aplicacion;
    }

    /**
     * Asigna el nombre de la aplicación
     * @param aplicacion    Nombre de la aplicación
     */
    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    /**
     * Obtiene la clave de la aplicación
     * @return  Integer     Clave de la aplicación
     */
    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Asigna la clave de la aplicación
     * @param claveAplicacion   Clave de la aplicación
     */
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    /**
     * Obtiene la clave de la Forma principal
     * @return  Integer CLave de la forma principal
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
     * Obtiene la descripción
     * @return  String  Descripcion asignada
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripción
     * @param descripcion   Descripción asignada
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion mostrarResultado(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion ingresarBusquedaSencilla(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion ingresarBusquedaAvanzada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion mostrarPanel(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion guardarBusqueda(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion rescatarBusqueda(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public Aplicacion eliminarBusqueda() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    /**
     * Método que agrega una forma (con formato List) al Hash de formas, 
     * asignándole como Key, el ID de la Forma. Si existía una forma con la
     * misma Key, esta es reemplazada
     * @param key   ID de la Forma
     * @param obj   Listado de la forma a agregar
     */
    public void addForma(Integer key, List obj){
        this.hsForma.put(key, obj);
    }

    /**
     * Obtiene, mediante el ID introducido, una forma desde el listado de formas,
     * que se asociaron cuando se obtuvo el perfil del Usuario. El resultado es
     * un listado con los campos de la forma.
     * @param key       ID de la forma a solicitar
     * @return  List    Listado con los campos de la Forma
     */
    public List getForma(Integer key){
        List hs = null;
        if (this.hsForma.containsKey(key)){
            hs = (List) this.hsForma.get(key);
        }else{
            if (hsForma.containsKey(key)){
                hs = (List) hsForma.get(key);
            }
        }
        return hs;
    }

    /**
     * Asigna el XML obtenido con referencia a los datos introducidos al
     * controlador
     * @return      Aplicacion  Objeto con los datos de entrega
     * @throws ExceptionHandler
     */
    public Aplicacion mostrarForma() throws ExceptionHandler{
        StringBuffer xmlForma = new StringBuffer("");
        try{
            if (this.getDisplay().toUpperCase().equals("HEADER")){
                xmlForma = this.getHeaderGrid();
            }else if (this.getDisplay().toUpperCase().equals("BODY")){
                xmlForma = this.getHeaderAndBodyGrid();
            }else{
                xmlForma = this.getHeaderGrid();
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Mostrar la Forma");
            eh.setDataToXML("DISPLAY",this.getDisplay());
            eh.setDataToXML("CLAVE FORMA",this.getClaveForma());
            eh.setDataToXML("TIPO ACCION",this.getClaveForma());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{

        }
        return this;
    }

    /**
     * Obtiene las columnas de una grilla XML a partir de los datos asignados a
     * los campos ClaveForma, TipoAccion, ArrayData, strWhereQuery y Forma
     * @return  StringBuffer    Texto con el Header del Grid
     * @throws ExceptionHandler
     */
    private StringBuffer getHeaderGrid() throws ExceptionHandler{
        StringBuffer strSld = null;
        try{
            AdminXML adm = new AdminXML();
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            String[] strData = new String[2];
            HashCampo hsCmp = new HashCampo();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());

                //obtiene la query a ejecutar
                con.getBitacora().setEnable(false);
                DataTransfer dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(con.getIdQuery(AdminFile.FORMAQUERY));
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpQ = con.getDataByIdQuery(dataTransfer);

                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();

                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);

                    //agregamos el where para obtener la estructura de los campos y la tabla
                    if ((this.getStrWhereQuery()!=null)&&(this.getArrayData()==null)){
                        strData = new String[1];
                        strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());
                        con.getBitacora().setEnable(false);

                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setStrWhere(strData[0]);
                        dataTransfer.setArrVariables(this.getArrVariables());

                        hsCmp = con.getDataByIdQueryAndWhere(dataTransfer);

                    }else if ((this.getStrWhereQuery()==null)&&(this.getArrayData()!=null)){
                        con.getBitacora().setEnable(false);

                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setArrData(this.getArrayData());
                        dataTransfer.setArrVariables(this.getArrVariables());

                        hsCmp = con.getDataByIdQuery(dataTransfer);

                    }else if ((this.getStrWhereQuery()!=null)&&(this.getArrayData()!=null)){
                        strData = new String[1];
                        strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());
                        con.getBitacora().setEnable(false);

                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setStrWhere(strData[0]);
                        dataTransfer.setArrData(this.getArrayData());
                        dataTransfer.setArrVariables(this.getArrVariables());

                        hsCmp = con.getDataByIdQueryAndWhereAndData(dataTransfer);
                        
                    }else{
                        strData = new String[0];
                        con.getBitacora().setEnable(false);

                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setArrData(strData);
                        dataTransfer.setArrVariables(this.getArrVariables());

                        hsCmp = con.getDataByIdQuery(dataTransfer);
                    }
                }
                if (this.getClaveEmpleado()!=null){
                    HashCampo hsCmpPerm = new HashCampo();
                    strData = new String[2];
                    strData[0] = String.valueOf(this.getClaveEmpleado());
                    strData[1] = String.valueOf(this.getClaveForma());

                    con.getBitacora().setEnable(false);

                    dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(con.getIdQuery(AdminFile.PERMISOS));
                    dataTransfer.setArrData(strData);
                    dataTransfer.setArrVariables(this.getArrVariables());

                    hsCmpPerm = con.getDataByIdQuery(dataTransfer);
                    
                    adm.setHashPermisoForma(hsCmpPerm);
                }
            }
            List lstF = (List) this.getForma(this.getClaveForma());
            try{
                Forma fmrForma = new Forma();
                fmrForma.setBitacora(this.getBitacora());
                lstF = fmrForma.getFormaByIdFormaIdPerfil(this.getClaveForma(),this.getClavePerfil(),lstF);
            }catch(Exception e){}

            //Solo el administrador puede ver la query
            String idAdmin = AdminFile.getKey(AdminFile.leerConfig(),AdminFile.IDADMIN);
            if (idAdmin.equals(String.valueOf(this.getClavePerfil()))){
                adm.setIncludeQuery(true);
            }
            strSld = adm.getGridColumByData(hsCmp,lstF);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener Header Grid");
            eh.setDataToXML("CLAVE FORMA", this.getClaveForma());
            eh.setDataToXML("TIPO ACCION", this.getTipoAccion());
            eh.setDataToXML("CLAVE PERFIL", this.getClavePerfil());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return strSld;
    }

    /**
     * Obtiene una grilla XML a partir de los datos asignados: ClaveForma,
     * TipoAccion, strWhereQuery, NumPage y NumRows
     * @return  StringBuffer    Texto con el Header y data de la forma
     * @throws ExceptionHandler
     */
    private StringBuffer getHeaderAndBodyGrid() throws ExceptionHandler{
        StringBuffer strSld = null;
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            AdminXML adm = new AdminXML();
            String[] strData = new String[2];
            HashCampo hsCmp = new HashCampo();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());
                
                //obtenemos la query a ejecutar
                con.getBitacora().setEnable(false);
                DataTransfer dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(con.getIdQuery(AdminFile.FORMAQUERY));
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpQ = con.getDataByIdQuery(dataTransfer);

                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    strData = new String[1];
                    strData[0]= ((this.getStrWhereQuery()==null)?"":this.getStrWhereQuery());

                    con.getBitacora().setEnable(true);
                    dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                    dataTransfer.setStrWhere(strData[0]);
                    dataTransfer.setArrVariables(this.getArrVariables());
                    if (this.getOrderBY()!=null){
                        dataTransfer.setOrderBY(this.getOrderBY());
                    }
                    //obtenemos los datos
                    hsCmp = con.getDataByIdQueryAndWhere(dataTransfer);
                }
                if (this.getClaveEmpleado()!=null){
                    HashCampo hsCmpPerm = new HashCampo();
                    strData = new String[2];
                    strData[0] = String.valueOf(this.getClaveEmpleado());
                    strData[1] = String.valueOf(this.getClaveForma());

                    //obtenemos los permisos
                    con.getBitacora().setEnable(false);
                    dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(con.getIdQuery(AdminFile.PERMISOS));
                    dataTransfer.setArrData(strData);
                    dataTransfer.setArrVariables(this.getArrVariables());

                    hsCmpPerm = con.getDataByIdQuery(dataTransfer);
                    adm.setHashPermisoForma(hsCmpPerm);
                }
            }
            if (this.isCleanIncrement()){
                adm.setDeleteIncrement(cleanIncrement);
            }
            List lstF = (List) this.getForma(this.getClaveForma());
            try{
                Forma fmrForma = new Forma();
                fmrForma.setBitacora(this.getBitacora());
                lstF = fmrForma.getFormaByIdFormaIdPerfil(this.getClaveForma(),this.getClavePerfil(),lstF);
            }catch(Exception e){}

            //Solo el administrador puede ver la query
            String idAdmin = AdminFile.getKey(AdminFile.leerConfig(),AdminFile.IDADMIN);
            if (idAdmin.equals(String.valueOf(this.getClavePerfil()))){
                adm.setIncludeQuery(true);
            }
            strSld = adm.getGridByData(hsCmp,lstF,this.getNumPage(),this.getNumRows());
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener Header y Body Grid");
            eh.setDataToXML("CLAVE FORMA", this.getClaveForma());
            eh.setDataToXML("TIPO ACCION", this.getTipoAccion());
            eh.setDataToXML("CLAVE PERFIL", this.getClavePerfil());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return strSld;
    }

    /**
     * Obtiene un arrayList con el formato de una forma. Esta se utiliza cuando
     * no está configurada la forma, pero se tiene la query con la consulta para
     * completarla. Obviamente no están disponibles todos los datos, pero si los
     * básicos para construir el XML.
     * @param arrayData     Arreglo de datos con los datos de la forma que se
     * quiere obtener
     * @return              ArrayList   Contiene los datos de una forma nueva
     * @throws ExceptionHandler
     */
    public ArrayList getNewFormaById(String[] arrayData) throws ExceptionHandler {
        ArrayList lst = new ArrayList();
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(con.getIdQuery(AdminFile.FORMAQUERY));
            dataTransfer.setArrData(arrayData);
            dataTransfer.setArrVariables(this.getArrVariables());

            HashCampo hsCmp = con.getDataByIdQuery(dataTransfer);

            Campo cmp = hsCmp.getCampoByName("consulta");
            HashMap dq = hsCmp.getListData();
            if (!dq.isEmpty()){
                ArrayList arr = (ArrayList)dq.get(0);
                Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                String[] strSplit = cmpAux.getValor().toUpperCase().split(" FROM ");
                String[] strSPlit2 = strSplit[1].split(" ");
                String tabla = strSPlit2[0];

                dataTransfer = new DataTransfer();
                dataTransfer.setQuery(cmpAux.getValor());
                dataTransfer.setArrData(arrayData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpList = con.getDataByQuery(dataTransfer);

                List lstCmp = (List) hsCmpList.getListCampos();
                for (int i=0;i<lstCmp.size();i++){
                    Campo cmpArr = (Campo) lstCmp.get(i);
                    if (!cmpArr.getIsIncrement()){
                        CampoForma cmpF = new CampoForma();
                        cmpF.setTipoControl("text");
                        cmpF.setCampo(cmpArr.getNombreDB());
                        cmpF.setTabla(tabla);
                        cmpF.setTypeData(cmpArr.getTypeDataAPL());
                        lst.add(cmpF);
                    }
                }
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener una nueva Forma, mediante el ID de la query");
            eh.setDataToXML(arrayData);
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lst;
    }

    /**
     * Método que convierte a String el contenido del objeto Aplicacion
     * @return
     */
    @Override
    public String toString() {
        return "Aplicacion{"
                + ((claveEmpleado!=null)?"\n\tclaveEmpleado=" + claveEmpleado:"")
                + ((claveAplicacion!=null)?"\n\tclaveAplicacion=" + claveAplicacion:"")
                + ((clavePerfil!=null)?"\n\tclavePerfil=" + clavePerfil:"")
                + ((aplicacion!=null)?"\n\taplicacion=" + aplicacion:"")
                + ((claveFormaPrincipal!=null)?"\n\tclaveFormaPrincipal=" + claveFormaPrincipal:"")
                + ((descripcion!=null)?"\n\tdescripcion=" + descripcion:"")
                + ((aliasMenuNuevaEntidad!=null)?"\n\taliasMenuNuevaEntidad=" + aliasMenuNuevaEntidad:"")
                + ((aliasMenuMostrarEntidad!=null)?"\n\taliasMenuMostrarEntidad=" + aliasMenuMostrarEntidad:"")
                + ((claveForma!=null)?"\n\tclaveForma=" + claveForma:"")
                + ((tipoAccion!=null)?"\n\ttipoAccion=" + tipoAccion:"")
                + ((display!=null)?"\n\tdisplay=" + display:"")
                + ((strWhereQuery!=null)?"\n\tstrWhereQuery=" + strWhereQuery:"")
                + ((hsForma!=null)?"\n\thsForma=" + hsForma:"")
                + ((numPage!=null)?"\n\tnumPage=" + numPage:"")
                + ((numRows!=null)?"\n\tnumRows=" + numRows:"")
                + ((arrayData!=null)?"\n\tarrayData=" + arrayData:"")
                + "\n\tcleanIncrement=" + cleanIncrement
                + ((arrVariables!=null)?"\n\tarrVariables=" + arrVariables:"")
                + ((bitacora!=null)?"\n\tbitacora=" + bitacora:"")
                + ((orderBY!=null)?"\n\torderBY=" + orderBY:"")
                + "\n}";
    }


}