package mx.ilce.controller;
  
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    /************** GETTER Y SETTER ***************/

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
     * Obtiene una forma desde el listado de formas, mediante el ID introducido
     * como Key
     * @param key   ID de la forma a solicitar
     * @return
     */
    public List getForma(Integer key){
        List hs = (List) this.hsForma.get(key);
        return hs;
    }

    public Forma() {
        this.aliasTab = "";
        //this.lstFormas = new ArrayList();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Se toma un Objeto del tipo forma para su edicion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data
     * @return
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Se toma un Objeto del tipo Forma para su eliminacion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param idForma
     * @return
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param idForma
     * @return
     */
    public Forma mostrarForma() {
        try{
            ConSession con = new ConSession();
            String[] strData = new String[1];
            StringBuffer xmlForma = new StringBuffer("");
            if (this.claveForma !=null){
                strData[0] = String.valueOf(this.claveForma);
                HashCampo hsCmp = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMA), strData);

                if (!this.hsForma.isEmpty()){
                    AdminXML admXML = new AdminXML();

                    List lstF = (List)this.getForma(Integer.valueOf(claveForma));
                    xmlForma = admXML.getFormaByData(hsCmp, lstF, con.getIdQuery(AdminFile.FORMA));
                }
            }
            this.setXmlEntidad(xmlForma);
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
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
                Integer idForma = apl.getClaveFormaPrincipal();
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
    public Integer getIdFormaByIdAplic(Integer idAplication, List lstAplications){
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
