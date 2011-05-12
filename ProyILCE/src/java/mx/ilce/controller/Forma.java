package mx.ilce.controller;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.handler.ExecutionHandler;

/**   
 * Clase para la implementacion de los metodos asociados a Forma
 * Se prevee dejar en memoria esta objeto para que mantenga las formas
 * obtenidas a partir de las aplicaciones asociadas al Perfil del usuario
 *
 * @author ccatrilef
 */
public class Forma extends Entidad{ 

    private String aliasTab; 
    private Integer ordenTab; 
    private Integer claveAplicacion;
    private Integer claveForma;
    private Integer claveFormaPadre;
    private HashMap hsForma;
    private String pk;
    private String tipoAccion;
    private String campoPK;

    /************** GETTER Y SETTER ***************/

    public String getCampoPK() {
        return campoPK;
    }

    public void setCampoPK(String campoPK) {
        this.campoPK = campoPK;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        if (tipoAccion==null){
            tipoAccion="";
        }
        this.tipoAccion = tipoAccion;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        if (pk==null){
            pk="0";
        }
        this.pk = pk;
    }

    public String getAliasTab() {
        return aliasTab;
    }

    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public Integer getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    public Integer getClaveFormaPadre() {
        return claveFormaPadre;
    }

    public void setClaveFormaPadre(Integer claveFormaPadre) {
        this.claveFormaPadre = claveFormaPadre;
    }

    public Integer getOrdenTab() {
        return ordenTab;
    }

    public void setOrdenTab(Integer ordenTab) {
        this.ordenTab = ordenTab;
    }

    public HashMap getHsForma() {
        return hsForma;
    }

    public void setHsForma(HashMap hsForma) {
        this.hsForma = hsForma;
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

    public Forma() {
        this.aliasTab = "";
        this.pk ="0";
        this.tipoAccion ="";
        this.hsForma = new HashMap();
    }

/************** METODOS ENTIDAD ****************/
    /**
     * Se ingresa una entidad del tipo Forma, para que quede disponible
     * para la aplicacion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data
     * @return
     */
    public ExecutionHandler ingresarEntidad(Object data) {
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
            String query = "select * from " + tabla;
            String[] strData = new String[0];
            HashCampo hsCmp = conE.getDataByQuery(query, strData);
            StringBuffer strQuery = new StringBuffer("");
            StringBuffer strCampos = new StringBuffer("(");
            StringBuffer strValues = new StringBuffer("(");
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                // si es autoIncremental, no se necesita enviar
                if (!cmpHS.getIsIncrement()){
                    if (valor!=null){
                        boolean isString = false;
                        if ("java.lang.String".equals(cmpHS.getTypeDataAPL())){
                            isString = true;
                        }
                        strCampos.append(cmpFL.getCampo()).append(",");
                        if (isString){
                            strValues.append("'");
                            strValues.append(valor);
                            strValues.append("',");
                        }else{
                            strValues.append(valor).append(",");
                        }
                    }
                    if ((valor==null)&&(cmpFL.getObligatorio()==1)){
                        //error
                        System.err.append("Error");
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
            conE.ingresaEntidad();

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (intHs.equals(1)){
                ex.setExecutionOK(true);
            }else{
                ex.setExecutionOK(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ex;
    }

    /**
     * Se toma un Objeto del tipo forma para su edicion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data
     * @return
     */
    public ExecutionHandler editarEntidad(Object data) {
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
            String query = "select * from " + tabla;
            String[] strData = new String[0];
            HashCampo hsCmp = conE.getDataByQuery(query, strData);
            StringBuffer strQuery = new StringBuffer("");
            strQuery.append("update ").append(tabla).append(" set ");
            String strCampoPK = "";
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                // si es autoIncremental, no se necesita enviar
                if (!cmpHS.getIsIncrement()){
                    if (valor!=null){
                        boolean isString = false;
                        if ("java.lang.String".equals(cmpHS.getTypeDataAPL())){
                            isString = true;
                        }
                        strQuery.append(cmpFL.getCampo()).append("=");
                        if (isString){
                            strQuery.append("'");
                            strQuery.append(valor);
                            strQuery.append("',");
                        }else{
                            strQuery.append(valor).append(",");
                        }
                    }
                    if ((valor==null)&&(cmpFL.getObligatorio()==1)){
                        //error
                        System.err.append("Error");
                    }
                }else{
                    strCampoPK = cmpFL.getCampo();
                }
            }
            strQuery.delete(strQuery.length()-1,strQuery.length());
            strQuery.append(" where ").append(strCampoPK);
            strQuery.append(" = ").append(pkInsert);
            strQuery.append("");

            conE.setQuery(strQuery.toString());
            conE.setCampoForma(cmpF);
            conE.editarEntidad();

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (intHs.equals(1)){
                ex.setExecutionOK(true);
            }else{
                ex.setExecutionOK(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ex;
    }

    /**
     * Se toma un Objeto del tipo Forma para su eliminacion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param idForma
     * @return
     */
    public ExecutionHandler eliminarEntidad(Object data) {
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
            String query = "select * from " + tabla;
            String[] strData = new String[0];
            HashCampo hsCmp = conE.getDataByQuery(query, strData);
            StringBuffer strQuery = new StringBuffer("");
            strQuery.append("delete from ").append(tabla).append(" where ");
            String strCampoPK = "";
            for (int i=0;i<lstForma.size();i++){
                CampoForma cmpFL = (CampoForma) lstForma.get(i);
                Campo cmpHS = hsCmp.getCampoByNameDB(cmpFL.getCampo());
                String valor = (String) hsForm.get(cmpFL.getCampo());
                // si es autoIncremental, no se necesita enviar
                if (!cmpHS.getIsIncrement()){
                    if ((valor!=null)&&(!"".equals(valor))){
                        boolean isString = false;
                        if ("java.lang.String".equals(cmpHS.getTypeDataAPL())){
                            isString = true;
                        }
                        strQuery.append(cmpFL.getCampo()).append("=");
                        if (isString){
                            strQuery.append("'");
                            strQuery.append(valor);
                            strQuery.append("' AND ");
                        }else{
                            strQuery.append(valor).append(" AND ");
                        }
                    }
                    if ((valor==null)&&(cmpFL.getObligatorio()==1)){
                        //error
                        System.err.append("Error");
                    }
                }else{
                    strCampoPK = cmpFL.getCampo();
                }
            }
            strQuery.append(strCampoPK);
            strQuery.append(" = ").append(pkInsert);
            strQuery.append("");

            conE.setQuery(strQuery.toString());
            conE.setCampoForma(cmpF);
            conE.eliminaEntidad();

            HashCampo hs = conE.getHashCampo();
            Integer intHs = (Integer) hs.getObjData();
            ex = new ExecutionHandler();
            if (intHs.equals(1)){
                ex.setExecutionOK(true);
            }else{
                ex.setExecutionOK(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ex;
    }

    /**
     *
     * @param idForma
     * @return
     */
    public Forma mostrarForma() {
        try{
            ConSession con = new ConSession();
            String[] strData = new String[2];
            StringBuffer xmlForma = new StringBuffer("");
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
                    strData[0]= this.getPk();
                    HashCampo hsCmp = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()), strData);
                    if (!this.hsForma.isEmpty()){
                        AdminXML admXML = new AdminXML();
                        List lstF = (List)this.getForma(Integer.valueOf(claveForma));
                        if (hsCmp.getLengthData()==0){
                            xmlForma = admXML.getFormaWithoutData(hsCmp, lstF, con.getIdQuery(AdminFile.FORMA));
                        }else{
                            xmlForma = admXML.getFormaByData(hsCmp, lstF, con.getIdQuery(AdminFile.FORMA));
                        }
                    }
                }
            } 
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return this;
    }

    public Forma mostrarResultado() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public Forma ingresarBusquedaSencilla() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public Forma ingresarBusquedaAvanzada() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public boolean validarCampos(){
        boolean boolSld = true;
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return boolSld;
    }

    public Forma mostrarPanel(){
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

/***********************    METODOS ADICIONALES   ***************************/

    /**
     * Completa el listado de formas segun el listado de aplicaciones que se le
     * entrega. Cada aplicacion posee una forma y por cada una de ellas, se trae
     * su configuracion, introduciendolo en el listado, asociandole el ID de la
     * forma que le corresponde a la configuracion. Cada configuracion (Forma)
     * esta compuesto por un listado de CampoForma.
     * @param lstAplications
     */
    public void getFormasByAplications(List lstAplications ){

        ConEntidad con = new ConEntidad();
        if ((lstAplications!=null)&&(!lstAplications.isEmpty())){
            Iterator it = lstAplications.iterator();
            while (it.hasNext()){
                Aplicacion apl = (Aplicacion) it.next();
                Integer idForma = apl.getClaveForma();
                String[] strData = new String[1];
                strData[0]=String.valueOf(idForma);
                List lstE = con.getListFormaById(strData);
                addForma(idForma, lstE);
            }
        }
    }

    /**
     * Entrega el ID de la forma asociada a la aplicacion, rescatado desde el
     * listado de aplicaciones que le entrega el Perfil
     * @param idAplication  ID de la aplicacion donde se va a buscar la forma
     * @param lstAplications    Listado de aplicaciones, estas aplicaciones se
     * deben obtener a travez del Perfil
     * @return
     */
    private Integer getIdFormaByIdAplic(Integer idAplication, List lstAplications){
        Integer idForma=Integer.valueOf(0);

        if ((lstAplications!=null)&&(!lstAplications.isEmpty())){
            Iterator it = lstAplications.iterator();
            while (it.hasNext()){
                Aplicacion apl = (Aplicacion) it.next();
                if (idAplication.equals(apl.getClaveAplicacion())){
                    idForma = apl.getClaveFormaPrincipal();
                }
            }
        }
        return idForma;
    }
}
