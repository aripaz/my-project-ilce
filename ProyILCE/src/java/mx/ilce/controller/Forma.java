package mx.ilce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.component.ListHash;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LogHandler;
import mx.ilce.mail.DataMail;
import mx.ilce.mail.SendMailNotif;
import mx.ilce.util.Validation;

/**
 * Clase para la implementación de los métodos asociados a Forma.
 * Se prevé dejar en memoria este objeto para que mantenga las formas
 * obtenidas a partir de las aplicaciones asociadas al Perfil del usuario
 * @author ccatrilef
 */
public class Forma extends Entidad{

    private String aliasTab;
    private Integer ordenTab;
    private Integer claveAplicacion;
    private Integer claveForma;
    private Integer claveFormaPadre;
    private Integer clavePerfil;
    private HashMap hsForma;
    private String pk;
    private String tipoAccion;
    private String campoPK;
    private boolean cleanIncrement;
    private String strWhereQuery;
    private String[] arrayData;
    private HashMap formData;
    private ArrayList formName;
    private boolean includeForaneo=true;
    private Integer claveEmpleado;
    private String[][] arrVariables;
    private Bitacora bitacora;
    private boolean includeHour=false;
    private String orderBY;
    private DataMail dataMail;

    /**
     * Obtiene la clave del perfil
     * @return  Integer CLave del Perfil
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clave del perfil
     * @param clavePerfil   Clave del perfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene un objeto DataMail
     * @return  DataMail    Objeto DataMail
     */
    public DataMail getDataMail() {
        return dataMail;
    }

    /**
     * Asigna un objeto DataMail
     * @param dataMail
     */
    public void setDataMail(DataMail dataMail) {
        this.dataMail = dataMail;
    }

    /**
     * Entrega un texto con el Order By Asignado
     * @return  String  Texto con el Order by
     */
    public String getOrderBY() {
        return orderBY;
    }

    /**
     * Asigna un texto con el Order By
     * @param orderBY   Texto con el Order by
     */
    public void setOrderBY(String orderBY) {
        this.orderBY = orderBY;
    }

    /**
     * Entrega la validación de si se debe incluir las horas en la fecha
     * @return      Boolean
     */
    public boolean isIncludeHour() {
        return includeHour;
    }

    /**
     * Asigna la validación para ver si se debe incluir las horas en la fecha
     * @param includeHour   Boolean
     */
    public void setIncludeHour(boolean includeHour) {
        this.includeHour = includeHour;
    }

    /**
     * Obtiene el arreglo de variables
     * @return  String[][]  Arreglo con las variables
     */
    public String[][] getArrVariables() {
        return arrVariables;
    }

    /**
     * Asigna el arreglo de variables
     * @param arrVariables  Arreglo con las variables
     */
    public void setArrVariables(String[][] arrVariables) {
        this.arrVariables = arrVariables;
    }

    /**
     * Obtiene la clave del empleado
     * @return  Integer Clave del empleado
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Asigna la clave del empleado
     * @param claveEmpleado Clave del empleado
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Indica con TRUE o FALSE si al ir formando el XML se debe incluir o no el
     * listado del foraneo
     * @return  Boolean
     */
    public boolean isIncludeForaneo() {
        return includeForaneo;
    }

    /**
     * Asigna con TRUE o FALSE si al ir formando el XML se debe incluir o no el
     * listado del foraneo
     * @param includeForaneo    Boolean
     */
    public void setIncludeForaneo(boolean includeForaneo) {
        this.includeForaneo = includeForaneo;
    }

    /**
     * Obtiene el FormData
     * @return  HashMap     Con la data de la forma
     */
    public HashMap getFormData() {
        return formData;
    }

    /**
     * Asigna el FormData
     * @param formData  FormData a ingresar
     */
    public void setFormData(HashMap formData) {
        this.formData = formData;
    }

    /**
     * Obtiene el FormName
     * @return  ArrayList   Nombre de la forma
     */
    public ArrayList getFormName() {
        return formName;
    }

    /**
     * Asigna el nombre del Form
     * @param formName  Nombre de la forma
     */
    public void setFormName(ArrayList formName) {
        this.formName = formName;
    }

    /**
     * Obtiene el ArrayData
     * @return  String[]    Array con la data
     */
    public String[] getArrayData() {
        return arrayData;
    }

    /**
     * Asigna el ArrayData
     * @param arrayData     Array con la data
     */
    public void setArrayData(String[] arrayData) {
        this.arrayData = arrayData;
    }

    /**
     * Obtiene el texto adicional que se incluira en la query
     * @return  String  Texto con el WHERE para una Query
     */
    public String getStrWhereQuery() {
        return (strWhereQuery==null)?"":strWhereQuery;
    }

    /**
     * Asigna el texto adicional que se incluira en la query
     * @param strWhereQuery     Texto con el WHERE para una query
     */
    public void setStrWhereQuery(String strWhereQuery) {
        this.strWhereQuery = (strWhereQuery==null)?"":strWhereQuery;
    }

    /**
     * Indica mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @return  Boolean     Entrada TRUE o FALSE sobre estado del increment
     */
    public boolean isCleanIncrement() {
        return cleanIncrement;
    }

    /**
     * Asigna mediante TRUE o FALSE si se deben ignorar los campos con increment
     * @param cleanIncrement    Entrada TRUE o FALSE sobre estado del increment
     */
    public void setCleanIncrement(boolean cleanIncrement) {
        this.cleanIncrement = cleanIncrement;
    }

    /**
     * Obtiene el nombre del campo PK de la forma
     * @return  String  Nombre del campo PK
     */
    public String getCampoPK() {
        return campoPK;
    }

    /**
     * Asigna el nombre del campo PK de la forma
     * @param campoPK   Nombre del campo PK
     */
    public void setCampoPK(String campoPK) {
        this.campoPK = campoPK;
    }

    /**
     * Obtiene el tipo de acción que se esta efectuando con la Forma
     * @return  String  Texto con la acción a efectuar
     */
    public String getTipoAccion() {
        return ((tipoAccion==null)?"":tipoAccion);
    }

    /**
     * Asigna el tipo de acción que se esta efectuando con la Forma
     * @param tipoAccion    Texto con la acción a efectuar
     */
    public void setTipoAccion(String tipoAccion) {
        if (tipoAccion==null){
            tipoAccion="";
        }
        this.tipoAccion = tipoAccion;
    }

    /**
     * Obtiene el código PK ingresado
     * @return  String      Código PK de la Forma
     */
    public String getPk() {
        return pk;
    }

    /**
     * Asigna el código PK ingresado
     * @param pk    Código PK de la Forma
     */
    public void setPk(String pk) {
        if (pk==null){
            pk="0";
        }
        this.pk = pk;
    }

    /**
     * Obtiene el Alias del TAB
     * @return  String      Alias del TAB
     */
    public String getAliasTab() {
        return aliasTab;
    }

    /**
     * Asigna el Alias del TAB
     * @param aliasTab      Alias del TAB
     */
    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    /**
     * Obtiene la clave de la aplicación
     * @return  Integer     Clave de la Aplicación
     */
    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Asigna la clave de la aplicación
     * @param claveAplicacion   Clave de la Aplicación
     */
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
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
     * @param claveForma    Clave de la Forma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene la clave de la forma padre
     * @return      Integer     Clave de la forma Padre
     */
    public Integer getClaveFormaPadre() {
        return claveFormaPadre;
    }

    /**
     * Asigna la clave de la forma padre
     * @param claveFormaPadre   Clave de la Forma Padre
     */
    public void setClaveFormaPadre(Integer claveFormaPadre) {
        this.claveFormaPadre = claveFormaPadre;
    }

    /**
     * Obtiene el orden del Tab
     * @return  Integer     Código con el orden
     */
    public Integer getOrdenTab() {
        return ordenTab;
    }

    /**
     * Asigna el orden del Tab
     * @param ordenTab      Código con el orden
     */
    public void setOrdenTab(Integer ordenTab) {
        this.ordenTab = ordenTab;
    }

    /**
     * Obtiene el hash con la configuración de la forma
     * @return  HashMap     Hash con la configuración
     */
    public HashMap getHsForma() {
        return hsForma;
    }

    /**
     * Asigna el hash con la configuración de la forma
     * @param hsForma   Hash con la configuración
     */
    public void setHsForma(HashMap hsForma) {
        this.hsForma = hsForma;
    }

    /**
     * Método que agrega una forma (con formato List) al Hash de formas, asignandole como
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
     * que se asociaron cuando se obtuvo el perfil del Usuario. El resultado es
     * un listado con los campos de la forma.
     * @param key   ID de la forma a solicitar
     * @return  List    Listado con la forma
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
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora      Objeto Bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Constructor básico de la forma, inicializa las variables
     */
    public Forma() {
        this.aliasTab = "";
        this.pk ="0";
        this.tipoAccion ="";
        this.hsForma = new HashMap();
    }

    /**
     * Método alternativo implementado para borrar los permisos anteriores
     * e ingresar los nuevos permisos al mismo tiempo.
     * NO PROBADA
     * @param data  Data de entrada
     * @return  ExecutionHandler    Resultado de la ejecución
     * @throws ExceptionHandler
     */
    public ExecutionHandler ingresarDataPermisos(Object data) throws ExceptionHandler{
        ExecutionHandler ex = null;
        try{
            List lstData = (List) data;
            HashMap hsForm = (HashMap) lstData.get(0);
            Forma forma = (Forma) lstData.get(1);
            Integer claveFormaInsert = forma.getClaveForma();

            List lstForma = forma.getForma(Integer.valueOf(claveFormaInsert));
            CampoForma cmpF = (CampoForma) lstForma.get(0);
            String tabla = cmpF.getTabla();

            String pkInsert = forma.getPk();

            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());

            //Obtenemos la estructura de la tabla incluida en la forma
            String query = "select * from " + tabla;
            String[] strData = new String[0];

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(query);
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(this.getArrVariables());

            HashCampo hsCmp = conE.getDataByQuery(dataTransfer);

            StringBuilder strQuery = new StringBuilder();
            StringBuffer strCampos = new StringBuffer("(");
            StringBuffer strValues = new StringBuffer("(");
            //recorremos los campos de la forma y los constrastamos contra los de la tabla
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                // si es autoIncremental, no se necesita enviar
                if (!cmpHS.getIsIncrement()){
                    if (valor!=null){
                        boolean isString = false;
                        if (("java.lang.String".equals(cmpHS.getTypeDataAPL())) ||
                            ("mx.ilce.bean.Text".equals(cmpHS.getTypeDataAPL())) ||
                            ("java.sql.Date".equals(cmpHS.getTypeDataAPL()))
                           ){
                            isString = true;
                        }
                        strCampos.append(cmpFL.getCampo()).append(",");
                        if (isString){
                            if("".equals(valor)){
                                valor = "null";
                                strValues.append(valor).append(",");
                            }else{
                                strValues.append("'");
                                strValues.append((valor==null)?"":valor);
                                strValues.append("',");
                            }
                        }else{
                            if("".equals(valor)){
                                valor = "null";
                            }
                            strValues.append(valor).append(",");
                        }
                    }
                }
            }
            strCampos.delete(strCampos.length()-1 ,strCampos.length());
            strCampos.append(")");
            strValues.delete(strValues.length()-1 ,strValues.length());
            strValues.append(")");
            strQuery.append("insert into ").append(tabla);
            strQuery.append(" ").append(strCampos);
            strQuery.append(" values ");
            strQuery.append(strValues);
            strQuery.append("");

            conE.setQuery(strQuery.toString());
            conE.setCampoForma(cmpF);

            StringBuilder strQueryDel = new StringBuilder();
            strQueryDel.append("delete from ").append(tabla).append(" where ");
            String strCampoPK = "";
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                if (cmpHS!=null){
                    // si es autoIncremental, no se necesita enviar
                    if (cmpHS.getIsIncrement()){
                        strCampoPK = cmpFL.getCampo();
                    }
                }
            }
            if (pkInsert!=null){
                if ((pkInsert.trim().length()>0)&&(!pkInsert.trim().equals("0"))){
                    if (strCampoPK !=null){
                        strQueryDel.append(strCampoPK);
                        strQueryDel.append(" = ").append(pkInsert);
                    }
                }
            }
            if (forma.getStrWhereQuery()!=null){
                String strWhere = forma.getStrWhereQuery().trim();
                if (strWhere.length()>0){
                    if (pkInsert!=null){
                        if ((pkInsert.trim().length()>0)&&(!pkInsert.trim().equals("0"))){
                            if (strCampoPK !=null){
                                strQueryDel.append(" AND ").append(strWhere);
                            }else{
                                strQueryDel.append(strWhere);
                            }
                        }else{
                            strQueryDel.append(strWhere);
                        }
                    }else{
                        strQueryDel.append(strWhere);
                    }
                }
            }
            strQueryDel.append("");
            conE.setQueryDel(strQueryDel.toString());

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (!intHs.equals(0)){
                ex.setExecutionOK(true);
                ex.setObjectData(intHs);
            }else{
                ex.setExecutionOK(false);
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Ingresar Data y Permisos");
            eh.setDataToXML((List)data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return ex;
    }

    /**
     * Método que ngresa una entidad del tipo Forma, para que quede disponible
     * para la aplicación.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data  Forma que se ingresara
     * @return  ExecutionHandler    Resultado de la ejecución
     */
    public ExecutionHandler ingresarEntidad(Object data) throws ExceptionHandler{
        ExecutionHandler ex = null;
        try{
            List lstData = (List) data;

            HashMap hsForm = (HashMap) lstData.get(0);
            Forma forma = (Forma) lstData.get(1);
            Integer claveFormaInsert = forma.getClaveForma();

            List lstForma = forma.getForma(Integer.valueOf(claveFormaInsert));
            CampoForma cmpF = (CampoForma) lstForma.get(0);
            String tabla = cmpF.getTabla();

            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());

            //obtenemos la estructura de la tabla
            String query = "select * from " + tabla;
            String[] strData = new String[0];
            conE.getBitacora().setEnable(false);

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(query);
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(this.getArrVariables());

            HashCampo hsCmp = conE.getDataByQuery(dataTransfer);

            StringBuffer strQuery = new StringBuffer();
            StringBuffer strCampos = new StringBuffer("(");
            StringBuffer strValues = new StringBuffer("(");
            HashMap hsCmpProcesados = new HashMap();
            HashMap hsCmpRepetidos = new HashMap();
            Integer intRep = 0;
            List listVariables = new ArrayList();

            //recorremos los campos de la forma y constratamos contra los de la tabla
            //para solo agregar los campos que nos enviaron
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                // si es autoIncremental, no se necesita enviar
                if (!cmpHS.getIsIncrement()){
                    String[] arrVariablesI = new String[4];
                    AdminForm admForm = new AdminForm();
                    valor = admForm.getDefaultValueByClass(cmpHS.getTypeDataAPL(), valor);
                    if (valor!=null){
                        if (!hsCmpProcesados.containsKey(cmpFL.getCampo())){
                            hsCmpProcesados.put(cmpFL.getCampo(),cmpFL.getCampo());
                            boolean isString = false;
                            if (("java.lang.String".equals(cmpHS.getTypeDataAPL())) ||
                                ("mx.ilce.bean.Text".equals(cmpHS.getTypeDataAPL())) ||
                                ("java.sql.Date".equals(cmpHS.getTypeDataAPL()))
                               ){
                                isString = true;
                            }
                            strCampos.append(cmpFL.getCampo()).append(",");
                            if (isString){
                                if("".equals(valor)){
                                    valor = "null";
                                    strValues.append(valor).append(",");
                                }else{
                                    strValues.append("'");
                                    strValues.append((valor==null)?"":valor);
                                    strValues.append("',");
                                }
                            }else{
                                if("".equals(valor)){
                                    valor = "null";
                                }
                                strValues.append(valor).append(",");
                            }
                            arrVariablesI[0]=cmpFL.getCampo();
                            arrVariablesI[1]=((cmpFL.getAliasCampo()==null)?cmpFL.getCampo():cmpFL.getAliasCampo());
                            arrVariablesI[2]=valor;
                            arrVariablesI[3]=cmpHS.getTypeDataAPL();
                            listVariables.add(arrVariablesI);
                        }else{
                            hsCmpRepetidos.put(intRep++,cmpFL.getCampo());
                        }
                    }
                }
            }
            strCampos.delete(strCampos.length()-1 ,strCampos.length());
            strCampos.append(")");
            strValues.delete(strValues.length()-1 ,strValues.length());
            strValues.append(")");
            strQuery.append("insert into ").append(tabla);
            strQuery.append(" ").append(strCampos);
            strQuery.append(" values ");
            strQuery.append(strValues);
            strQuery.append("");

            dataTransfer = new DataTransfer();
            dataTransfer.setQueryInsert(strQuery.toString());
            dataTransfer.setCampoForma(cmpF);
            conE.getBitacora().setEnable(true);
            conE.getBitacora().setLstVariables(listVariables);
            conE.ingresaEntidad(dataTransfer);

            SendMailNotif sen = new SendMailNotif();
            sen.setBitacora(conE.getBitacora());
            /*
            if (sen.admSendMail()){
                this.setDataMail(sen.getDataMail());
            }*/

            conE.getBitacora().setEnable(false);
            conE.getBitacora().setLstVariables(null);

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (!intHs.equals(0)){
                ex.setExecutionOK(true);
                ex.setObjectData(intHs);
                this.getBitacora().setIdBitacora(conE.getBitacora().getIdBitacora());
            }else{
                ex.setExecutionOK(false);
            }
            if (!hsCmpRepetidos.isEmpty()){
                StringBuffer campos = new StringBuffer();
                for (int i=0;i<intRep;i++){
                    campos.append((String) hsCmpRepetidos.get(Integer.valueOf(i)));
                    campos.append(" || ");
                }
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                textData.append("ALERTA, EXISTEN CAMPOS REPETIDOS EN LA FORMA\n");
                textData.append(("FORMA: "+claveFormaInsert)).append("\n");
                textData.append(("QUERY: "+strQuery)).append("\n");
                textData.append(("CAMPOS: "+campos));
                log.logWarning(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("editarEntidad"),textData);
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para Ingresar el INSERT de la Forma");
            eh.setDataToXML((List)data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return ex;
    }

    /**
     * Método que toma un Objeto del tipo forma para su edición.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data  Forma que se editara
     * @return  ExecutionHandler    Resultado de la ejecución
     */
    public ExecutionHandler editarEntidad(Object data) throws ExceptionHandler{
        ExecutionHandler ex = null;
        try{
            List lstData = (List) data;

            HashMap hsForm = (HashMap) lstData.get(0);
            Forma forma = (Forma) lstData.get(1);
            
            String pkInsert = forma.getPk();
            Integer claveFormaInsert = forma.getClaveForma();

            List lstForma = forma.getForma(Integer.valueOf(claveFormaInsert));
            CampoForma cmpF = (CampoForma) lstForma.get(0);
            String tabla = cmpF.getTabla();

            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());

            // obtenemos la estructura de la tabla
            String query = "select * from " + tabla;
            String[] strData = new String[0];
            conE.getBitacora().setEnable(false);

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(query);
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(this.getArrVariables());

            HashCampo hsCmp = conE.getDataByQuery(dataTransfer);

            StringBuffer strQuery = new StringBuffer();
            strQuery.append("update ").append(tabla).append(" set ");
            String strCampoPK = "";
            HashMap hsCmpProcesados = new HashMap();
            HashMap hsCmpRepetidos = new HashMap();
            Integer intRep = 0;
            List listVariables = new ArrayList();

            //recorremos los campos de la forma y constratamos contra los de la tabla
            //para solo modificar los campos que nos enviaron
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                if (cmpHS!=null){
                    // si es autoIncremental, no se necesita enviar
                    if (!cmpHS.getIsIncrement()){
                        String[] arrVariablesI = new String[4];
                        AdminForm admForm = new AdminForm();
                        valor = admForm.getDefaultValueByClass(cmpHS.getTypeDataAPL(), valor);
                        if (valor!=null){
                            if (!hsCmpProcesados.containsKey(cmpFL.getCampo())){
                                hsCmpProcesados.put(cmpFL.getCampo(),cmpFL.getCampo());
                                boolean isString = false;
                                if (("java.lang.String".equals(cmpHS.getTypeDataAPL())) ||
                                    ("mx.ilce.bean.Text".equals(cmpHS.getTypeDataAPL())) ||
                                    ("java.sql.Date".equals(cmpHS.getTypeDataAPL()))
                                   ){
                                    isString = true;
                                }
                                strQuery.append(cmpFL.getCampo()).append("=");
                                if (isString){
                                    if("".equals(valor)){
                                        valor = "null";
                                        strQuery.append(valor).append(",");
                                    }else{
                                        strQuery.append("'");
                                        strQuery.append((valor==null)?"":valor);
                                        strQuery.append("',");
                                    }
                                }else{
                                    if("".equals(valor)){
                                        valor = "null";
                                    }
                                    strQuery.append(valor).append(",");
                                }
                            }else{
                                hsCmpRepetidos.put(intRep++,cmpFL.getCampo());
                            }
                            arrVariablesI[0]=cmpFL.getCampo();
                            arrVariablesI[1]=((cmpFL.getAliasCampo()==null)?cmpFL.getCampo():cmpFL.getAliasCampo());
                            arrVariablesI[2]=valor;
                            arrVariablesI[3]=cmpHS.getTypeDataAPL();
                            listVariables.add(arrVariablesI);
                        }
                    }else{
                        strCampoPK = cmpFL.getCampo();
                    }
                }
            }
            if ((strCampoPK ==null)||("".equals(strCampoPK))){
                List lst = hsCmp.getListCampos();
                boolean seguir = true;
               for (int i=0;i<lst.size()&&seguir;i++){
                   Campo cmp = (Campo) lst.get(i);
                   if (cmp.getIsIncrement()){
                       strCampoPK = cmp.getNombreDB();
                       seguir=false;
                   }
               }
            }
            strQuery.delete(strQuery.length()-1,strQuery.length());
            strQuery.append(" where ").append(strCampoPK);
            strQuery.append(" = ").append(pkInsert);
            strQuery.append("");

            dataTransfer = new DataTransfer();
            dataTransfer.setQueryUpdate(strQuery.toString());
            dataTransfer.setCampoForma(cmpF);
            conE.getBitacora().setEnable(true);
            conE.getBitacora().setLstVariables(listVariables);
            conE.editarEntidad(dataTransfer);

            SendMailNotif sen = new SendMailNotif();
            sen.setBitacora(conE.getBitacora());
            /*
            if (sen.admSendMail()){
                this.setDataMail(sen.getDataMail());
            }
            */
            conE.getBitacora().setEnable(false);
            conE.getBitacora().setLstVariables(null);

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (intHs.equals(1)){
                ex.setExecutionOK(true);
                this.getBitacora().setIdBitacora(conE.getBitacora().getIdBitacora());
            }else{
                ex.setExecutionOK(false);
            }
            if (!hsCmpRepetidos.isEmpty()){
                StringBuffer campos = new StringBuffer();
                for (int i=0;i<intRep;i++){
                    campos.append((String) hsCmpRepetidos.get(Integer.valueOf(i)));
                    campos.append(" || ");
                }
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                textData.append("ALERTA, EXISTEN CAMPOS REPETIDOS EN LA FORMA\n");
                textData.append(("FORMA: "+claveFormaInsert)).append("\n");
                textData.append(("QUERY: "+strQuery)).append("\n");
                textData.append(("CAMPOS: "+campos));
                log.logWarning(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("editarEntidad"),textData);
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para Ingresar el UPDATE de la Forma");
            eh.setDataToXML((List)data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return ex;
    }

    /**
     * Método que toma un Objeto del tipo Forma para su eliminación.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param idForma   ID de la Forma que se eliminara
     * @return  ExecutionHandler    Resultado de la ejecución
     */
    public ExecutionHandler eliminarEntidad(Object data) throws ExceptionHandler {
        ExecutionHandler ex = null;
        try{
            List lstData = (List) data;
            Forma forma = (Forma) lstData.get(1);

            String pkInsert = forma.getPk();
            Integer claveFormaInsert = forma.getClaveForma();

            List listVariables = new ArrayList();

            String[] arrayDataI = new String[2];
            arrayDataI[0]=String.valueOf(claveFormaInsert);
            arrayDataI[1]="insert";
            List lstForma = forma.getFormaForDeleteById(arrayDataI);
            if (lstForma!=null){
                CampoForma cmpF = (CampoForma) lstForma.get(0);
                String tabla = cmpF.getTabla();

                ConEntidad conE = new ConEntidad();
                conE.setBitacora(this.getBitacora());

                // obtenemos la estructura de la tabla
                String query = "select * from " + tabla;
                String[] strData = new String[0];

                DataTransfer dataTransfer = new DataTransfer();
                dataTransfer.setQuery(query);
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmp = conE.getDataByQuery(dataTransfer);

                StringBuilder strQuery = new StringBuilder();
                strQuery.append("delete from ").append(tabla).append(" where ");
                String strCampoPK = "";

                //recorremos los campos de la forma y contrastamos contra el de la tabla
                //para ubicar el campo clave de la tabla
                boolean seguir = true;
                for (int i=0;i<lstForma.size()&&seguir;i++){
                    CampoForma cmpFL = (CampoForma) lstForma.get(i);
                    Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                    if (cmpHS!=null){
                        // si es autoIncremental, no se necesita enviar
                        if (cmpHS.getIsIncrement()){
                            strCampoPK = cmpFL.getCampo();
                            seguir = false;
                        }
                    }
                }
                if (pkInsert!=null){
                    String[] arrVariablesI = new String[4];
                    if ((pkInsert.trim().length()>0)&&(!pkInsert.trim().equals("0"))){
                        if (strCampoPK !=null){
                            strQuery.append(strCampoPK);
                            strQuery.append(" = ").append(pkInsert);
                            arrVariablesI[0]=strCampoPK;
                            arrVariablesI[1]=strCampoPK;
                            arrVariablesI[2]=pkInsert;
                            arrVariablesI[3]=Integer.class.getName();
                            listVariables.add(arrVariablesI);
                        }
                    }
                }
                if (forma.getStrWhereQuery()!=null){
                    String strWhere = forma.getStrWhereQuery().trim();
                    if (strWhere.length()>0){
                        if (pkInsert!=null){
                            if ((pkInsert.trim().length()>0)&&(!pkInsert.trim().equals("0"))){
                                if (strCampoPK !=null){
                                    strQuery.append(" AND ").append(strWhere);
                                }else{
                                    strQuery.append(strWhere);
                                }
                            }else{
                                strQuery.append(strWhere);
                            }
                        }else{
                            strQuery.append(strWhere);
                        }
                    }
                }
                strQuery.append("");

                dataTransfer = new DataTransfer();
                dataTransfer.setQueryDelete(strQuery.toString());
                dataTransfer.setCampoForma(cmpF);
                conE.getBitacora().setEnable(true);
                conE.getBitacora().setLstVariables(listVariables);
                conE.eliminaEntidad(dataTransfer);

                conE.getBitacora().setEnable(false);
                conE.getBitacora().setLstVariables(null);

                HashCampo hs = conE.getHashCampo();
                Integer intHs = (Integer) hs.getObjData();
                ex = new ExecutionHandler();
                if (intHs.equals(1)){
                    ex.setExecutionOK(true);
                    ex.setObjectData(intHs);
                    this.getBitacora().setIdBitacora(conE.getBitacora().getIdBitacora());
                }else{
                    ex.setExecutionOK(false);
                    ex.setObjectData(intHs);
                }
            }else{
                ex = new ExecutionHandler();
                ex.setExecutionOK(false);
                ex.setObjectData(Integer.valueOf(0));
                ex.setTextExecution("No se encuentra el listado de la Forma");
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para Ingresar el DELETE de la Forma");
            eh.setDataToXML((List)data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return ex;
    }

    /**
     * Genera el XML de la forma y lo asigna al campo XmlEntidad
     * @return  Forma   Objeto Forma
     * @throws ExceptionHandler
     */
    public Forma mostrarForma() throws ExceptionHandler {
        StringBuffer xmlForma = new StringBuffer("");
        try{
            ConSession con = new ConSession();
            con.setBitacora(this.getBitacora());
            boolean enableBitac = this.getBitacora().isEnable();

            String[] strData = new String[2];
            Validation val = new Validation();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());

                //Obtenemos el IDQUERY de la forma entregada
                con.getBitacora().setEnable(false);
                Integer idQuery = con.getIdQuery(AdminFile.FORMAQUERY);

                //obtenemos los datos de la forma
                con.getBitacora().setEnable(false);
                HashCampo hsCmpQ = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMAQUERY),
                                                strData, this.getArrVariables());

                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();

                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    strData = new String[1];
                    strData[0]= this.getPk();

                    //ejecutamos la QUERY que se obtuvo al buscar la forma
                    con.getBitacora().setEnable(enableBitac);
                    HashCampo hsCmp = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()),
                                                strData, this.getArrVariables());
                    hsCmp.setPkData(this.getPk());

                    if (!this.hsForma.isEmpty()){
                        AdminXML admXML = new AdminXML();
                        admXML.setBitacora(this.getBitacora());

                        if (this.getClaveEmpleado()!=null){
                            HashCampo hsCmpPerm = new HashCampo();
                            strData = new String[2];
                            strData[0] = String.valueOf(this.getClaveEmpleado());
                            strData[1] = String.valueOf(this.getClaveForma());
                            con.getBitacora().setEnable(false);
                            hsCmpPerm = con.getDataByIdQuery(con.getIdQuery(AdminFile.PERMISOS),
                                                        strData, this.getArrVariables());
                            admXML.setHashPermisoForma(hsCmpPerm);
                        }
                        admXML.setHsForm(this.getFormData());
                        List lstF = (List)this.getForma(Integer.valueOf(claveForma));
                        if (this.isCleanIncrement()){
                            admXML.setDeleteIncrement(true);
                        }else{
                            admXML.setDeleteIncrement(false);
                        }
                        try{
                            Forma fmrForma = new Forma();
                            fmrForma.setBitacora(this.getBitacora());
                            lstF = fmrForma.getFormaByIdFormaIdPerfil(this.getClaveForma(),this.getClavePerfil(),lstF);
                        }catch(Exception e){}

                        if (hsCmp.getLengthData()==0){
                            xmlForma = admXML.getFormaWithoutData(hsCmp,lstF,this.getClaveForma(),
                                    this.getTipoAccion(), this.getArrVariables());
                        }else{
                            admXML.setIncludeHour(true);
                            xmlForma = admXML.getFormaByData(hsCmp,lstF,this.getClaveForma(),
                                    this.getTipoAccion(),this.getArrVariables());
                        }
                    }else{
                        StringBuilder strEmpty = new StringBuilder();
                        strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                        strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                        strEmpty.append("PK: ").append(this.getPk()).append(", ");
                        strEmpty.append("ID QUERY: ").append(String.valueOf(cmpAux.getValor()));
                        val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                                "No se encontro datos para la forma, accion y PK solicitada");
                    }
                }else{
                    StringBuilder strEmpty = new StringBuilder();
                    strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                    strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                    strEmpty.append("ID QUERY: ").append(idQuery);
                    val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                            "No se encontró consulta de la forma y acción solicitadas");
                }
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener el XML la Forma a mostrar");
            eh.setDataToXML("CLAVE FORMA", this.getClaveForma());
            eh.setDataToXML("TIPO ACCION", this.getTipoAccion());
            eh.setDataToXML("PK", this.getPk());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return this;
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    public Forma mostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Método para ingresar una busqueda sencilla desde un formulario
     * @return  Forma   Objeto Forma
     * @throws ExceptionHandler
     */
    public Forma ingresarBusquedaSencilla() throws ExceptionHandler {
        StringBuffer xmlForma = new StringBuffer("");
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            boolean enableBitac = this.getBitacora().isEnable();

            String[] strData = new String[2];
            Validation val = new Validation();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());

                //obtenemos el IDQUERY asociado a la forma
                Integer idQuery = con.getIdQuery(AdminFile.FORMAQUERY);
                //obtenemos los datos de la forma
                DataTransfer dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(idQuery);
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpQ = con.getDataByIdQuery(dataTransfer);

                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    String strWhere = this.getStrWhereQuery();

                    //obtenemos la data buscada
                    con.getBitacora().setEnable(enableBitac);
                    dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                    dataTransfer.setStrWhere(strWhere);
                    dataTransfer.setArrVariables(this.getArrVariables());

                    HashCampo hsCmp = con.getDataByIdQueryAndWhere(dataTransfer);
                    if (!this.hsForma.isEmpty()){
                        AdminXML admXML = new AdminXML();
                        admXML.setBitacora(this.getBitacora());
                        admXML.getBitacora().setEnable(false);

                        List lstF = (List)this.getForma(Integer.valueOf(claveForma));
                        if (this.isCleanIncrement()){
                            admXML.setDeleteIncrement(cleanIncrement);
                        }
                        if (hsCmp.getLengthData()==0){
                            xmlForma = admXML.getFormaWithoutData(hsCmp, lstF,this.getClaveForma(),
                                    this.getTipoAccion(), this.getArrVariables());
                        }else{
                            xmlForma = admXML.getFormaByData(hsCmp, lstF,this.getClaveForma(),
                                    this.getTipoAccion(), this.getArrVariables());
                        }
                    }else{
                        StringBuilder strEmpty = new StringBuilder();
                        strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                        strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                        strEmpty.append("ID QUERY: ").append(String.valueOf(cmpAux.getValor()));
                        val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                                "No se encontro datos para la forma y accion solicitada");
                    }
                }else{
                    StringBuilder strEmpty = new StringBuilder();
                    strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                    strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                    strEmpty.append("ID QUERY: ").append(idQuery);
                    val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                            "No se encontro datos para la forma y accion solicitada");
                }
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener el XML la Forma en una busqueda");
            eh.setDataToXML("CLAVE FORMA", this.getClaveForma());
            eh.setDataToXML("TIPO ACCION", this.getTipoAccion());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return this;
    }

    /**
     * Método para ingresar una busqueda avanzada desde un formulario
     * @return  Forma   Objeto Forma
     * @throws ExceptionHandler
     */
    public Forma ingresarBusquedaAvanzada() throws ExceptionHandler{
        StringBuffer xmlForma = new StringBuffer("");
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            boolean enableBitac = this.getBitacora().isEnable();

            String[] strData = new String[2];
            Validation val = new Validation();
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.getClaveForma());
                strData[1] = String.valueOf(this.getTipoAccion());
                con.getBitacora().setEnable(false);

                //obtenemos el IDQUERY de la forma
                Integer idQuery = con.getIdQuery(AdminFile.FORMAQUERY);
                con.getBitacora().setEnable(false);
                //obtenemos los datos de la forma
                DataTransfer dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(idQuery);
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpQ = con.getDataByIdQuery(dataTransfer);

                Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
                HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    String strWhere = this.getStrWhereQuery();
                    strData = this.getArrayData();

                    //obtenemos los datos de la consulta
                    con.getBitacora().setEnable(enableBitac);
                    dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                    dataTransfer.setStrWhere(strWhere);
                    dataTransfer.setArrData(strData);
                    dataTransfer.setArrVariables(this.getArrVariables());

                    HashCampo hsCmp = con.getDataByIdQueryAndWhereAndData(dataTransfer);

                    if (!this.hsForma.isEmpty()){
                        AdminXML admXML = new AdminXML();
                        admXML.setBitacora(this.getBitacora());

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

                            admXML.setHashPermisoForma(hsCmpPerm);
                        }
                        admXML.setHsForm(this.getFormData());
                        List lstF = (List)this.getForma(Integer.valueOf(claveForma));
                        try{
                            Forma fmrForma = new Forma();
                            fmrForma.setBitacora(this.getBitacora());
                            lstF = fmrForma.getFormaByIdFormaIdPerfil(this.getClaveForma(),this.getClavePerfil(),lstF);
                        }catch(Exception e){}
                        if (this.isCleanIncrement()){
                            admXML.setDeleteIncrement(true);
                        }else{
                            admXML.setDeleteIncrement(false);
                        }
                        if (!this.isIncludeForaneo()){
                            admXML.setIncludeForaneo(false);
                        }else{
                            admXML.setIncludeForaneo(true);
                        }
                        admXML.getBitacora().setEnable(false);
                        if (hsCmp.getLengthData()==0){
                            xmlForma = admXML.getFormaWithoutData(hsCmp, lstF, this.getClaveForma(),
                                    this.getTipoAccion(),this.getArrVariables());
                        }else{
                            admXML.setIncludeHour(true);
                            xmlForma = admXML.getFormaByData(hsCmp, lstF, this.getClaveForma(),
                                    this.getTipoAccion(),this.getArrVariables());
                        }
                    }else{
                        StringBuilder strEmpty = new StringBuilder();
                        strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                        strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                        strEmpty.append("ID QUERY: ").append(String.valueOf(cmpAux.getValor()));
                        val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                                "No se encontró consulta de la forma y acción solicitadas");
                    }
                }else{
                    StringBuilder strEmpty = new StringBuilder();
                    strEmpty.append("Clave Forma: ").append(this.getClaveForma()).append(", ");
                    strEmpty.append("Accion: ").append(this.getTipoAccion()).append(", ");
                    strEmpty.append("ID QUERY: ").append(String.valueOf(idQuery));
                    val.executeErrorEmptyData(strEmpty.toString(), this.getClass(),
                            "No se encontró consulta de la forma y acción solicitadas");
                }
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener el XML la Forma en una busqueda");
            eh.setDataToXML("CLAVE FORMA", this.getClaveForma());
            eh.setDataToXML("TIPO ACCION", this.getTipoAccion());
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return this;
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    public boolean validarCampos(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    public Forma mostrarPanel(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Completa el listado de formas según el listado de aplicaciones que se le
     * entrega. Cada aplicación posee una forma y por cada una de ellas, se trae
     * su configuración, introduciendolo en el listado, asociandole el ID de la
     * forma que le corresponde a la configuración. Cada configuración (Forma)
     * esta compuesto por un listado de CampoForma.
     * @param lstAplications    Listado con las aplicaciones del User
     * @throws ExceptionHandler
     */
    public void getFormasByAplications(List lstAplications ) throws ExceptionHandler{
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            if ((lstAplications!=null)&&(!lstAplications.isEmpty())){
                Iterator it = lstAplications.iterator();
                while (it.hasNext()){
                    Aplicacion apl = (Aplicacion) it.next();
                    Integer idForma = apl.getClaveForma();
                    String[] strData = new String[1];
                    strData[0]=String.valueOf(idForma);

                    if (con.getBitacora()!=null){
                        con.getBitacora().setEnable(false);
                    }
                    DataTransfer dataTransfer = new DataTransfer();
                    dataTransfer.setArrData(strData);
                    dataTransfer.setArrVariables(this.getArrVariables());
                    dataTransfer.setStrWhere(this.getStrWhereQuery());

                    List lstE = con.getListFormaById(dataTransfer);
                    addForma(idForma, lstE);
                }
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener las formas de un listado de Aplicaciones");
            eh.setDataToXML(lstAplications);
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * Método que entrega el ID de la forma asociada a la aplicación, rescatado
     * desde el listado de aplicaciones que le entrega el Perfil
     * @param idAplication  ID de la aplicación donde se va a buscar la forma
     * @param lstAplications    Listado de aplicaciones, estas aplicaciones se
     * deben obtener a travez del Perfil
     * @return  Integer     ID de la Forma
     * @throws ExceptionHandler
     */
    private Integer getIdFormaByIdAplic(Integer idAplication, List lstAplications)
                throws ExceptionHandler{
        Integer idForma=Integer.valueOf(0);
        try{
            if ((lstAplications!=null)&&(!lstAplications.isEmpty())){
                Iterator it = lstAplications.iterator();
                while (it.hasNext()){
                    Aplicacion apl = (Aplicacion) it.next();
                    if (idAplication.equals(apl.getClaveAplicacion())){
                        idForma = apl.getClaveFormaPrincipal();
                    }
                }
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener el ID de una forma de una Aplicación");
            eh.setDataToXML("ID APLICACION",idAplication);
            eh.setDataToXML(lstAplications);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return idForma;
    }

    /**
     * Obtiene un List con la configuración de la forma entregada. El List esta
     * formado por Bean del tipo CampoForma
     * @param arrayData     Arreglo de datos con la configuración de la forma
     * @return  List    Listado con los datos de la Forma
     * @throws ExceptionHandler
     */
    public List getListFormaById(String[] arrayData) throws ExceptionHandler{
        List lst = null;
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());

            con.getBitacora().setEnable(false);

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setArrData(arrayData);
            dataTransfer.setArrVariables(this.getArrVariables());
            lst = con.getListFormaById(dataTransfer);

        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener el List con la configuración de la Forma");
            eh.setDataToXML(arrayData);
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lst;
    }

    /**
     * Obtiene un arrayList con el formato de una forma. Esta se utiliza cuando
     * no esta configurada la forma, pero se tiene la query con la consulta para
     * completarla. Obviamente no estan disponibles todos los datos, pero si los
     * básicos para construir el XML.
     * @param arrayData     Arreglo de datos con los datos de la forma que se
     * quiere obtener
     * @return  ArrayList   Arreglo con los datos de la Forma
     * @throws ExceptionHandler
     */
    public ArrayList getNewFormaById(String[] arrayData) throws ExceptionHandler {
        ArrayList lst = new ArrayList();
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            con.getBitacora().setEnable(false);

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

                con.getBitacora().setEnable(false);
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
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener la Forma, mediante el ID de la query");
            eh.setDataToXML(arrayData);
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lst;
    }

    /**
     * Método para traer los datos básicos de una forma para poder eliminar un dato.
     * @param arrayData
     * @return  ArrayList   Listado con la configuración de la forma
     * @throws ExceptionHandler
     */
    public ArrayList getFormaForDeleteById(String[] arrayData) throws ExceptionHandler {
        ArrayList lst = new ArrayList();
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            con.getBitacora().setEnable(false);

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
                //con esta query garantisamos que solo se obtengann los campos y no la data
                String strQuery = "select * from " + tabla + " where 1 = 2";

                con.getBitacora().setEnable(false);
                dataTransfer = new DataTransfer();
                dataTransfer.setQuery(strQuery);
                dataTransfer.setArrData(arrayData);
                dataTransfer.setArrVariables(this.getArrVariables());

                HashCampo hsCmpList = con.getDataByQuery(dataTransfer);

                List lstCmp = (List) hsCmpList.getListCampos();
                for (int i=0;i<lstCmp.size();i++){
                    Campo cmpArr = (Campo) lstCmp.get(i);
                    CampoForma cmpF = new CampoForma();
                    cmpF.setTipoControl("text");
                    cmpF.setCampo(cmpArr.getNombreDB());
                    cmpF.setTabla(tabla);
                    cmpF.setTypeData(cmpArr.getTypeDataAPL());
                    lst.add(cmpF);
                }
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener la Forma, mediante el ID de la query");
            eh.setDataToXML(arrayData);
            eh.setDataToXML(this.getArrVariables());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lst;
    }

    /**
     * Método para la obtención de los datos de una forma, asociada a la clave
     * de la forma y el perfil del usuario.
     * @param claveForma    Clave de la forma
     * @param clavePerfil   Clave del perfil
     * @param lst           Listado de parámetros
     * @return List         Listado con la configuración de la forma
     * @throws ExceptionHandler
     */
    public List getFormaByIdFormaIdPerfil(Integer claveForma, Integer clavePerfil, List lst) throws ExceptionHandler{
        List lstF = new ArrayList();
        try{
            ConEntidad conE = new ConEntidad();
            DataTransfer dataTransfer = new DataTransfer();

            String query = "select * from campo_forma "
                    + " where clave_forma = " + claveForma;
            if (clavePerfil!=null){
                query = query + " and clave_perfil = " + clavePerfil;
            }
            dataTransfer.setQuery(query);
            conE.setBitacora(this.getBitacora());
            HashCampo hsCampo = conE.getDataByQuery(dataTransfer);
            ListHash lstHash = new ListHash();
            ArrayList arrLst = lstHash.getListBean(CampoForma.class, hsCampo);
            if ((arrLst!=null)&&(!arrLst.isEmpty())){
                lstF = arrLst;
            }else{
                lstF = lst;
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                             "Problemas para obtener la Forma, mediante el ID de la forma y el ID del perfil");
            eh.setDataToXML("CLAVE FORMA",claveForma);
            eh.setDataToXML("CLAVE PERFIL",clavePerfil);
            eh.setDataToXML(lst);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lstF;
    }

    /**
     * Método que convierte a String el contenido del objeto Forma
     * @return
     */
    @Override
    public String toString() {
        return "Forma{"
                + ((aliasTab!=null)?"\n\taliasTab=" + aliasTab:"")
                + ((ordenTab!=null)?"\n\tordenTab=" + ordenTab:"")
                + ((claveAplicacion!=null)?"\n\tclaveAplicacion=" + claveAplicacion:"")
                + ((claveForma!=null)?"\n\tclaveForma=" + claveForma:"")
                + ((claveFormaPadre!=null)?"\n\tclaveFormaPadre=" + claveFormaPadre:"")
                + ((clavePerfil!=null)?"\n\tclavePerfil=" + clavePerfil:"")
                + ((hsForma!=null)?"\n\thsForma=" + hsForma:"")
                + ((pk!=null)?"\n\tpk=" + pk:"")
                + ((tipoAccion!=null)?"\n\ttipoAccion=" + tipoAccion:"")
                + ((campoPK!=null)?"\n\tcampoPK=" + campoPK:"")
                + "\n\tcleanIncrement=" + cleanIncrement
                + ((strWhereQuery!=null)?"\n\tstrWhereQuery=" + strWhereQuery:"")
                + ((arrayData!=null)?"\n\tarrayData=" + arrayData:"")
                + ((formData!=null)?"\n\tformData=" + formData:"")
                + ((formName!=null)?"\n\tformName=" + formName:"")
                + "\n\tincludeForaneo=" + includeForaneo
                + ((claveEmpleado!=null)?"\n\tclaveEmpleado=" + claveEmpleado:"")
                + ((arrVariables!=null)?"\n\tarrVariables=" + arrVariables:"")
                + ((bitacora!=null)?"\n\tbitacora=" + bitacora:"")
                + "\n\tincludeHour=" + includeHour
                + ((orderBY!=null)?"\n\torderBY=" + orderBY:"")
                + ((dataMail!=null)?"\n\tdataMail=" + dataMail:"")
                + "\n}";
    }

}
