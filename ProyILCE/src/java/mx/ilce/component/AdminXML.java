package mx.ilce.component;

import java.util.Iterator;
import java.util.List;
import mx.ilce.bean.Campo;

import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *  Clase para la implementacion de los metodos que se encargaran de transformar
 * los datos en el contenido de un archivo XML, segun la configuracion o la
 * estructura de los datos
 * @author ccatrilef
 */
public class AdminXML {
    private static File WORKING_DIRECTORY;
    private int numRow=0;
    private boolean deleteIncrement=false;
    private boolean includeForaneo=true;
    private HashCampo hashPermisoForma;


    public HashCampo getHashPermisoForma() {
        return hashPermisoForma;
    }

    public void setHashPermisoForma(HashCampo hashPermisoForma) {
        this.hashPermisoForma = hashPermisoForma;
    }

    /**
     * Indica con TRUE o FALSE si al ir formando el XML se debe incluir o no el 
     * listado del foraneo
     * @return
     */
    public boolean isIncludeForaneo() {
        return includeForaneo;
    }

    /**
     * Asigna con TRUE o FALSE si al ir formando el XML se debe incluir o no el
     * listado del foraneo
     * @param includeForaneo
     */
    public void setIncludeForaneo(boolean includeForaneo) {
        this.includeForaneo = includeForaneo;
    }

    /**
     * Indica con TRUE o FALSE si al ir formando el XML se debe ignorar o no
     * los datos de tipo Increement
     * @return
     */
    public boolean isDeleteIncrement() {
        return deleteIncrement;
    }

    /**
     * Asigna con TRUE o FALSE si al ir formando el XML se deben ignorar o no
     * los datos del tipo increment
     * @param deleteIncreement  Estado a aplicar a la variable
     */
    public void setDeleteIncrement(boolean deleteIncrement) {
        this.deleteIncrement = deleteIncrement;
    }

    /**
     * Metodo que permite obtener la data del usuario en el formato del
     * archivo XML definido para la Session y debe completarse con los datos
     * obtenidos desde la Base de Datos
     * @param user  Bean con los datos del usuario que se va a buscar, debe
     * contener la clave de empleado del usuario
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getSessionXML(User user) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try{
            ConSession con = new ConSession();
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            HashCampo hsCmp = con.getUserXML(user);
            Document document = getDocumentXML("widget.session.xml");
            str.append(listNode(document,0,hsCmp));
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de Session");
        }
        return str;
    }

    /**
     * Obtiene el menu en formato XML que le corresponde al usuario conectado
     * segun su perfil
     * @param user  Objeto User con los datos del usuariuo conectado
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getMenuXML(User user) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try{
            ConSession con = new ConSession();
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            HashCampo hsCmp = con.getMenuXML(user);

            Document document = getDocumentXML("widget.accordion.xml");
            str.append("<qry>");
            for (int i=0;i<hsCmp.getLengthData();i++){
                str.append(listNode(document,0,hsCmp,i));
            }
            str.append("</qry>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de Menu");
        }
        return str;
    }

    /**
     * Entrega los tab en formato XML que le corresponden segun el perfil entregado
     * @param perfil    Objeto Perfil con los datos del perfil del usuariuo conectado
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getTabXML(Perfil perfil) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try {
            ConSession con = new ConSession();
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            HashCampo hsCmp = con.getTabForma(perfil);
            Document document = getDocumentXML("widget.tabs.xml");
            str.append("<qry>");
            for (int i=0;i<hsCmp.getLengthData();i++){
                str.append(listNode(document,0,hsCmp,i));
            }
            str.append("</qry>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de TABs");
        }
        return str;
    }

    /**
     * Obtiene una grilla XML a partir de los datos entregados, se debe indicar
     * la pagina que se desea mostrar, junto con el maximo de registros que se
     * deben mostrar por pagina
     * @param hsData   Data obtenida desde una query previa, debe contener los
     * datos y los campos que le corresponden
     * @param lstCampos Contiene el listado de campos de la forma utilizada, se
     * utiliza para completar los datos equivalentes que le corresponden a cada
     * campo de la columna
     * @param page   Número de pagina que se desea mostrar del total de datos
     * @param regByPage Numero de registros por pagina que se deben mostrar
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getGridByData(HashCampo hsData, List lstCampos, int page, int regByPage)
                throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try {
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();

            int total = hsDat.size()/regByPage;
            if ((hsDat.size()%regByPage)>0){
                total++;
            }
            int reg = hsDat.size();
            if (reg>regByPage){
                reg = regByPage;
            }
            if (page==total){
                reg = hsDat.size()-((total-1)*regByPage);
            }
            int regIni = (regByPage*(page-1));
            str.append("<rows>\n");
            str.append(("<page>"+page+"</page>\n"));
            str.append(("<total>"+total+"</total>\n"));
            str.append(("<records>"+reg+"</records>\n"));
            str.append("<column_definition>\n");
            for(int i=0; i<lstCmp.size();i++){
                cmp = (Campo) lstCmp.get(i) ;
                boolean seguir = true;
                if (seguir){
                    str.append(("<"+cmp.getNombreDB()));
                    if (cmp.getIsIncrement()){
                        str.append((" autoincrement=\"TRUE\" "));
                    }
                    str.append((">\n"));
                    if (cmp.getNombreDB()!=null){
                        CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                        if (cmpAux!=null){
                            if (cmpAux.getAliasCampo()!=null){
                                str.append(("\t<alias_campo><![CDATA["
                                        //+ replaceAccent(castNULL(cmpAux.getAliasCampo().trim()))
                                        + castNULL(cmpAux.getAliasCampo().trim())
                                        + "]]></alias_campo>\n"));
                            }
                            if (cmpAux.getTamano()!=null){
                                str.append(("\t<tamano>"+cmpAux.getTamano()+"</tamano>\n"));
                            }
                        }
                    }
                    str.append(("</"+cmp.getNombreDB()+">\n"));
                }
            }
            str.append("</column_definition>\n");
            if (this.getHashPermisoForma()!=null){
                HashCampo hsPerm = this.getHashPermisoForma();
                HashMap hsDatPerm = hsPerm.getListData();
                List lstCamp = hsPerm.getListCampos();
                str.append("<permisos>\n");
                if (!hsDatPerm.isEmpty()){
                    for (int i=0;i<hsDatPerm.size();i++){
                        str.append("\t<row>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append(">![CDATA[");
                            str.append(cmpAux.getValor()).append("]]</");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</row>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            for(int i=regIni;i<hsDat.size();i++){
                ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                str.append(("<row id='"+String.valueOf(i+1)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) arr.get(j) ;
                    str.append("\t<cell>");
                    str.append(replaceHtml(castNULL(String.valueOf(cmp.getValor()).trim())));
                    str.append("</cell>\n");
                }
                str.append("</row>\n");
            }
            str.append("</rows>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de Grid");
        }
        return str;
    }

    /**
     * Obtiene las columnas de una grilla XML a partir de los datos entregados
     * @param hsData   Data obtenida desde una query previa, debe contener los
     * datos y los campos que le corresponden
     * @param lstCampos Contiene el listado de campos de la forma utilizada, se
     * utiliza para completar los datos equivalentes que le corresponden a cada
     * campo de la columna
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getGridColumByData(HashCampo hsData, List lstCampos)
                throws ExceptionHandler {
        StringBuffer str = new StringBuffer();
        try{
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();

            int reg = hsDat.size();
            str.append("<rows>\n");
            str.append("<column_definition>\n");
            for(int i=0; i<lstCmp.size();i++){
                cmp = (Campo) lstCmp.get(i) ;
                str.append(("<"+cmp.getNombreDB()));
                if (cmp.getIsIncrement()){
                    str.append((" autoincrement=\"TRUE\" "));
                }
                str.append((">\n"));
                if (cmp.getNombreDB()!=null){
                    CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                    if (cmpAux!=null){
                        if (cmpAux.getAliasCampo()!=null){
                            str.append(("\t<alias_campo><![CDATA["
                                    //+replaceAccent(castNULL(cmpAux.getAliasCampo().trim()))
                                    +castNULL(cmpAux.getAliasCampo().trim())
                                    +"]]></alias_campo>\n"));
                        }
                        if (cmpAux.getTamano()!=null){
                            str.append(("\t<tamano>"+cmpAux.getTamano()+"</tamano>\n"));
                        }
                    }
                }
                str.append(("</"+cmp.getNombreDB()+">\n"));
            }
            str.append("</column_definition>\n");
            if (this.getHashPermisoForma()!=null){
                HashCampo hsPerm = this.getHashPermisoForma();
                HashMap hsDatPerm = hsPerm.getListData();
                List lstCamp = hsPerm.getListCampos();
                str.append("<permisos>\n");
                if (!hsDatPerm.isEmpty()){
                    for (int i=0;i<hsDatPerm.size();i++){
                        str.append("\t<row>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append(">![CDATA[");
                            str.append(cmpAux.getValor()).append("]]</");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</row>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            str.append("</rows>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML con las columnas de Grid");
        }
        return str;
    }

    /**
     * Entrega un XML en base a la Forma indicada y con los datos que se le
     * entregan
     * @param hsData    Data entregada
     * @param lstCampos Listado de campos de la Forma
     * @param idForma   ID de la Forma entregada
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getFormaByData(HashCampo hsData, List lstCampos, 
            Integer idForma, String tipoAccion) throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try {
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();

            str.append("<qry>\n");
            for(int i=0;i<hsDat.size();i++){
                ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                str.append(("<registro id='"+String.valueOf(i+1)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) arr.get(j) ;
                    boolean seguir = true;
                    if (this.isDeleteIncrement()){
                        if (cmp.getIsIncrement()){
                            seguir = false;
                        }
                    }
                    if (seguir){
                        str.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                         + castTypeJavaToXML(cmp.getTypeDataAPL())
                                         + "\""));
                        if (cmp.getIsIncrement()){
                            str.append((" autoincrement=\"TRUE\" "));
                        }
                        str.append((">"));
                        str.append(("<![CDATA["));
                        str.append(replaceAccent(castNULL(String.valueOf(cmp.getValor()).trim())));
                        str.append("]]>\n");
                        if (cmp.getNombreDB()!=null){
                            CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                            if (cmpAux!=null){
                                if (cmpAux.getAliasCampo()!=null){
                                    str.append(("\t\t<alias_campo><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAliasCampo()).trim()))
                                            + "]]></alias_campo>\n"));
                                }
                                if (cmpAux.getObligatorio()!=null){
                                    str.append(("\t\t<obligatorio><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getObligatorio()).trim())
                                            + "]]></obligatorio>\n"));
                                }
                                if (cmpAux.getTipoControl()!=null){
                                    str.append(("\t\t<tipo_control><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getTipoControl()).trim())
                                            + "]]></tipo_control>\n"));
                                }
                                if (cmpAux.getEvento()!=null){
                                    str.append(("\t\t<evento><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getEvento()).trim())
                                            + "]]></evento>\n"));
                                }
                                if (cmpAux.getClaveFormaForanea()!=null){
                                    str.append("\t\t<foraneo");
                                    if ((cmpAux.getEditaFormaForanea()!=null)&&
                                            ("1".equals(String.valueOf(cmpAux.getEditaFormaForanea())))){
                                        str.append(" agrega_registro=\"true\"");
                                    }else{
                                        str.append(" agrega_registro=\"false\"");
                                    }
                                    str.append((" clave_forma=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    if (this.isIncludeForaneo()){
                                        String[] strData = new String[2];
                                        strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                        strData[1] = String.valueOf(cmpAux.getFiltroForaneo());
                                        if ((cmpAux.getFiltroForaneo()==null)&&(hsData.getPkData()!=null)){
                                            strData[1] = hsData.getPkData();
                                        }
                                        StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB());
                                        if (!"".equals(strForaneo.toString())){
                                            str.append(("\t\t\t<qry_"+cmp.getNombreDB()));
                                            str.append((" source=\"\">\n"));
                                            str.append(strForaneo);
                                            str.append(("\t\t\t</qry_"+cmp.getNombreDB()+">\n"));
                                        }
                                    }
                                    str.append("\t\t</foraneo>\n");
                                }
                                if (cmpAux.getAyuda()!=null){
                                    str.append(("\t\t<ayuda><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAyuda()).trim()))
                                            + "]]></ayuda>\n"));
                                }
                                if (cmpAux.getDatoSensible()!=null){
                                    str.append(("\t\t<dato_sensible><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getDatoSensible()).trim())
                                            + "]]></dato_sensible>\n"));
                                }
                                if (cmpAux.getActivo()!=null){
                                    str.append(("\t\t<activo><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getActivo()).trim())
                                            + "]]></activo>\n"));
                                }
                                if (cmpAux.getTamano()!=null){
                                    str.append(("\t\t<tamano>"
                                            + castNULL(String.valueOf(cmpAux.getTamano()).trim())
                                            + "</tamano>\n"));
                                }
                            }
                        }
                        str.append(("\t</"+cmp.getNombreDB()+">\n"));
                    }
                }
                str.append("</registro>\n");
            }
            str.append("</qry>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de la Forma con datos");
        }
        return str;
    }

    private String getCampoIncrement(List lstCmp){
        String sld = "";
        if ((lstCmp!=null)&&(lstCmp.isEmpty())){
            Iterator it = lstCmp.iterator();
            while (it.hasNext()){

            }
        }
        return sld;
    }

    /**
     * Entrega un XML en base a la Forma indicada y con la estructura de los 
     * datos, pero sin datos
     * @param hsData    Data para la generacion del XML
     * @param lstCampos     Listado de Campos a considerar desde la Data
     * @param idForma   ID de la Forma utilizada
     * @return
     * @throws ExceptionHandler
     */
    public StringBuffer getFormaWithoutData(HashCampo hsData, List lstCampos, 
            Integer idForma, String tipoAccion) throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try {
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();
            //HashMap hsDat = hsData.getListData();

            str.append("<qry>\n");
            for(int i=0;i<1;i++){
                //ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                str.append(("<registro id='"+String.valueOf(i)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) lstCmp.get(j) ;
                    boolean seguir = true;
                    if (this.isDeleteIncrement()){
                        if (cmp.getIsIncrement()){
                            seguir = false;
                        }
                    }
                    if (seguir){
                        str.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                         + castTypeJavaToXML(cmp.getTypeDataAPL())
                                         + "\""));
                        if (cmp.getIsIncrement()){
                            str.append((" autoincrement=\"TRUE\" "));
                        }
                        str.append((">"));
                        str.append("<![CDATA[]]>\n");
                        if (cmp.getNombreDB()!=null){
                            CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                            if (cmpAux!=null){
                                if (cmpAux.getAliasCampo()!=null){
                                    str.append(("\t\t<alias_campo><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAliasCampo()).trim()))
                                            + "]]></alias_campo>\n"));
                                }
                                if (cmpAux.getObligatorio()!=null){
                                    str.append(("\t\t<obligatorio><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getObligatorio()).trim())
                                            + "]]></obligatorio>\n"));
                                }
                                if (cmpAux.getTipoControl()!=null){
                                    str.append(("\t\t<tipo_control><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getTipoControl()).trim())
                                            + "]]></tipo_control>\n"));
                                }
                                if (cmpAux.getEvento()!=null){
                                    str.append(("\t\t<evento><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getEvento()).trim())
                                            + "]]></evento>\n"));
                                }
                                if (cmpAux.getClaveFormaForanea()!=null){
                                    str.append("\t\t<foraneo");
                                    if ((cmpAux.getEditaFormaForanea()!=null)&&
                                            ("1".equals(String.valueOf(cmpAux.getEditaFormaForanea())))){
                                        str.append(" agrega_registro=\"true\"");
                                    }else{
                                        str.append(" agrega_registro=\"false\"");
                                    }
                                    str.append((" clave_forma=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    String[] strData = new String[2];
                                    strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                    //strData[1] = String.valueOf(cmpAux.getFiltroForaneo());
                                    if (hsData.getPkData()!=null){
                                        strData[1]=hsData.getPkData();
                                    }
                                    if (this.isIncludeForaneo()){
                                        StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB());
                                        if (!"".equals(strForaneo.toString())){
                                            str.append(("\t\t\t<qry_"+cmp.getNombreDB()));
                                            str.append((" source=\"\">\n"));
                                            str.append(strForaneo);
                                            str.append(("\t\t\t</qry_"+cmp.getNombreDB()+">\n"));
                                        }
                                    }
                                    str.append("\t\t</foraneo>\n");
                                }
                                if (cmpAux.getAyuda()!=null){
                                    str.append(("\t\t<ayuda><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAyuda()).trim()))
                                            + "]]></ayuda>\n"));
                                }
                                if (cmpAux.getDatoSensible()!=null){
                                    str.append(("\t\t<dato_sensible><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getDatoSensible()).trim())
                                            + "]]></dato_sensible>\n"));
                                }
                                if (cmpAux.getActivo()!=null){
                                    str.append(("\t\t<activo><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getActivo()).trim())
                                            + "]]></activo>\n"));
                                }
                                if (cmpAux.getTamano()!=null){
                                    str.append(("\t\t<tamano>"
                                            + castNULL(String.valueOf(cmpAux.getTamano()).trim())
                                            + "</tamano>\n"));
                                }
                            }
                        }
                        str.append(("\t</"+cmp.getNombreDB()+">\n"));
                    }
                }
                str.append("</registro>\n");
            }
            str.append("</qry>");
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de la Forma sin datos");
        }
        return str;
    }


    /**
     * Metodo que permite crear la seccion de un XML a partir de la query que
     * se utilizara para completar los datos, la data que se debe utilizar como
     * entrada para la query y el nombre del registro que esta solicitando esta
     * seccion de XML
     * @param query     Query usada para completar los datos
     * @param strData   Data de entrada que se usara en la query
     * @param strRegistro   Nombre del registro desde donde se invoco el metodo
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer getXmlByQueryAndData(String query, String[] strData, 
            String strRegistro) throws ExceptionHandler {
        StringBuffer str = new StringBuffer("");
        try {
            ConEntidad con = new ConEntidad();
            HashCampo hsData = con.getDataByQuery(query, strData);
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();
            if (!hsDat.isEmpty()){
                for(int i=0;i<hsDat.size();i++){
                    ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                    str.append(("\t\t\t\t<registro_"+ strRegistro+" "));
                    str.append(("id='"+String.valueOf(i+1)+"'>\n"));
                    for (int j=0; j<lstCmp.size();j++){
                        Campo cmp = (Campo) arr.get(j) ;
                        str.append(("\t\t\t\t\t<"+ cmp.getNombreDB()));
                        str.append((" tipo_dato=\"" + castTypeJavaToXML(cmp.getTypeDataAPL()) + "\">"));
                        str.append(("<![CDATA["));
                        str.append(replaceAccent(castNULL(String.valueOf(cmp.getValor()).trim())));
                        str.append(("]]>"));
                        str.append(("</"+ cmp.getNombreDB() + ">\n"));
                    }
                    str.append(("\t\t\t\t</registro_"+ strRegistro+">\n"));
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML de una QUERY y DATA");
        }
        return str;
    }

    /**
     * Metodo que permite crear la seccion de un XML a partir de la forma que
     * se esta entregando en la data
     * @param strData   Data de entrada que se usara en la query
     * @param strRegistro   Nombre del registro desde donde se invoco el metodo
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer getXmlByIdForma(String[] strData, String strRegistro)
            throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try{
            ConEntidad con = new ConEntidad();
            HashCampo hsData = new HashCampo();
            //obtenemos la query de la forma entregada
            String[] strDataQ = new String[2];
            strDataQ[0] =strData[0];
            strDataQ[1] ="foreign";
            HashCampo hsCmpQ = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMAQUERY), strDataQ);
            Campo cmpQ = hsCmpQ.getCampoByName("claveconsulta");
            HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmpQ.getCodigo()-1);
                    String[] strDataFiltro = new String[1];
                    strDataFiltro[0] = strData[1];
                    //ejecutamos la query, con el filtro entregado
                    hsData = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()), strDataFiltro);
                }
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();
            if (!hsDat.isEmpty()){
                for(int i=0;i<hsDat.size();i++){
                    ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                    str.append(("\t\t\t\t<registro_"+ strRegistro+" "));
                    str.append(("id='"+String.valueOf(i+1)+"'>\n"));
                    for (int j=0; j<lstCmp.size();j++){
                        Campo cmp = (Campo) arr.get(j);
                        str.append(("\t\t\t\t\t<"+ cmp.getNombreDB()));
                        str.append((" tipo_dato=\"" + castTypeJavaToXML(cmp.getTypeDataAPL()) + "\">"));
                        str.append(("<![CDATA["));
                        str.append(replaceAccent(castNULL(String.valueOf(cmp.getValor()).trim())));
                        str.append(("]]>"));
                        str.append(("</"+ cmp.getNombreDB() + ">\n"));
                    }
                    str.append(("\t\t\t\t</registro_"+ strRegistro+">\n"));
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML por el ID de la forma");
        }
        return str;
    }

    /**
     * Metodo para obtener la data ordenada para ser utilizada en la query.
     * Se asume que la query esta bien estructurada, es decir, se requiere
     * que los parametros esten en forma secuencial (Ej: %1 %2 %3 ... ) para
     * ubicar en forma correcta los datos
     * @param query     Query donde se colocara la Data
     * @param lst   Listado con la Data
     * @return
     * @throws ExceptionHandler
     */
    private String[] getStringData (String query, ArrayList lst) throws ExceptionHandler{
        String[] strData = null;
        try {
            String[] splitPorc = query.split("%");

            if (splitPorc.length>1){
                strData = new String[splitPorc.length-1];
                for (int i=0; i<lst.size();i++){
                    Campo cmp = (Campo) lst.get(i);
                    String word1 = " "+cmp.getNombreDB()+"=";
                    String word2 = " "+cmp.getNombreDB()+" =";
                    if (cmp.getNombreDB()!=null){
                        if (query.contains(word1)||query.contains(word2)){
                            boolean seguir = true;
                            for (int j=0;j<splitPorc.length&&seguir;j++){
                                String str = splitPorc[j];
                                if (str.contains(word1)||str.contains(word2)){
                                    strData[j]=String.valueOf(cmp.getValor()).trim();
                                    seguir=false;
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el Array de data de una query");
        }
        return strData;
    }

    /**
     * Obtiene los datos de un campo, obtenidos desde la forma que se le entrega
     * @param lstData   Listado con la configuracion de la forma
     * @param nombreCampo   Campo que se esta buscando desde la forma
     * @return
     * @throws ExceptionHandler
     */
    private CampoForma getCampoForma(List lstData, String nombreCampo ) throws ExceptionHandler{
        CampoForma cmp = null;
        try {
            if ((lstData!=null)&&(!lstData.isEmpty())){
                Iterator it = lstData.iterator();
                boolean seguir=true;
                while(it.hasNext()&&seguir){
                    CampoForma cmpAux = (CampoForma) it.next();
                    if (cmpAux.getCampo().equals(nombreCampo)){
                        cmp = cmpAux;
                        seguir=false;
                    }
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el campo desde una forma");
        }
        return cmp;
    }

    /**
     * Obtiene un Documento XML, a partir del nombre solicitado. Se asume que
     * los archivos XML estan en la ruta resource/xml/[nombre archivo]
     * @param fileName  Nombre del archivo
     * @return
     * @throws ExceptionHandler
     */
    private Document getDocumentXML(String fileName) throws ExceptionHandler{
        Document documento = null;
        try {
            File f = null;

            String separador = String.valueOf(File.separator);
            URL url = AdminFile.class.getResource("AdminFile.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
                f = f.getParentFile().getParentFile();
                f = f.getParentFile().getParentFile();
                f = f.getParentFile();
                WORKING_DIRECTORY = f.getParentFile();
            }
            File fichero = new File(WORKING_DIRECTORY +
                                        separador + "resource" +
                                        separador + "xml" +
                                        separador + fileName);
            if (fichero.exists()){
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                documento = builder.parse(fichero);
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el documento XML desde /resource");
        }
        return documento;
    }

    /**
     * Recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde segun el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando es un registro unico.
     * @param e     Nodo de inicio de la lectura de datos
     * @param level     Nivel en que se encuentra la lectura
     * @param hsCmp     Conjunto de datos que seran analizados
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try {
            ArrayList lst = (ArrayList) hsCmp.getListCampos();
            Campo cmp = null;
            String strPadre = "";
            boolean endRow = false;

            if (!(e instanceof Element && ((Element) e).getLocalName()!= null)){
                if ((e.getNodeName() != null)
                        &&(!"#text".equals(e.getNodeName()))
                        &&(!"#cdata-section".equals(e.getNodeName()))
                        &&(!"#document".equals(e.getNodeName()))
                        &&(!"qry".equals(e.getNodeName()))) {
                    str.append(("\n<"+e.getNodeName()+" "));
                    strPadre = e.getNodeName();
                    cmp = hsCmp.getCampoByNameDB(e.getNodeName());
                }
            }
            if (cmp != null){
                HashMap hs = hsCmp.getListData();
                ArrayList arr = (ArrayList) hs.get(0);
                Campo cmpR = (Campo) arr.get(cmp.getCodigo()-1);
                level++;
                if (e.hasAttributes()){
                    NamedNodeMap attributes = e.getAttributes();
                    int length = attributes.getLength();
                    Attr attr = null;
                    for (int i=0; i<length; i++){
                        attr = (Attr)attributes.item(i);
                        str.append((attr.getNodeName()+"=\""));
                        str.append((attr.getNodeValue()+"\">"));
                        str.append(("<![CDATA["+replaceAccent(cmpR.getValor())+"]]>"));
                        str.append(("</"+strPadre+">"));
                    }
                }
            }else{
                if (e.hasAttributes()){
                    NamedNodeMap attributes = e.getAttributes();
                    int length = attributes.getLength();
                    Attr attr = null;
                    for (int i=0; i<length; i++){
                        attr = (Attr)attributes.item(i);
                        str.append((attr.getNodeName()+"=\""));
                        str.append((numRow+"\">"));
                        endRow = true;
                    }
                }
            }
            for (Node node = e.getFirstChild();node != null; node = node.getNextSibling()){
                str.append(listNode(node, level,hsCmp));
            }
            if (endRow){
                str.append(("\n</"+strPadre+">"));
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para completar el XML con los datos");
        }
        return str;
    }

    /**
     * Recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde segun el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando son mas de un registro
     * @param e     Nodo de inicio de la lectura de datos
     * @param level     Nivel en que se encuentra la lectura
     * @param hsCmp     Conjunto de datos que seran analizados
     * @param register  Numero del registor en que se vera la lectura
     * @return
     * @throws ExceptionHandler
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp, int register)
            throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try {
            Campo cmp = null;
            String strPadre = "";
            boolean endRow = false;

            if (!(e instanceof Element && ((Element) e).getLocalName()!= null)){
                if ((e.getNodeName() != null)
                        &&(!"#text".equals(e.getNodeName()))
                        &&(!"#cdata-section".equals(e.getNodeName()))
                        &&(!"#document".equals(e.getNodeName()))
                        &&(!"qry".equals(e.getNodeName()))) {
                    str.append(("\n<"+e.getNodeName()+" "));
                    strPadre = e.getNodeName();
                    cmp = hsCmp.getCampoByNameDB(e.getNodeName());
                }
            }
            if (cmp != null){
                HashMap hs = hsCmp.getListData();
                ArrayList arr = (ArrayList) hs.get(register);
                Campo cmpR = (Campo) arr.get(cmp.getCodigo()-1);
                level++;
                if (e.hasAttributes()){
                    NamedNodeMap attributes = e.getAttributes();
                    int length = attributes.getLength();
                    Attr attr = null;
                    for (int i=0; i<length; i++){
                        attr = (Attr)attributes.item(i);
                        str.append((attr.getNodeName()+"=\""));
                        str.append((attr.getNodeValue()+"\">"));
                        str.append(("<![CDATA["+replaceAccent(cmpR.getValor())+"]]>"));
                        str.append(("</"+strPadre+">"));
                    }
                }
            }else{
                if (e.hasAttributes()){
                    NamedNodeMap attributes = e.getAttributes();
                    int length = attributes.getLength();
                    Attr attr = null;
                    for (int i=0; i<length; i++){
                        attr = (Attr)attributes.item(i);
                        str.append((attr.getNodeName()+"=\""));
                        str.append(((register+1)+"\">"));
                        endRow = true;
                    }
                }
            }
            for (Node node = e.getFirstChild();node != null; node = node.getNextSibling()){
                str.append(listNode(node, level,hsCmp,register));
            }
            if (endRow){
                str.append(("\n</"+strPadre+">"));
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para completar el XML con los datos");
        }
        return str;
    }

    public String salidaXML(String data){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        str.append("<qry>\n");
        str.append("<resultado>").append(data).append("</resultado>\n");
        str.append("<qry>\n");

        return str.toString();
    }

    /**
     * Reemplaza los caracteres con acento y ñ, por sus codificacion HTML respectiva
     * @param data  String de datos donde se buscaran los caracteres especiales
     * @return
     * @throws ExceptionHandler
     */
    private String replaceAccent(String data) throws ExceptionHandler{
        String str = "";
        try{
            if (data!=null){
                str=data;
                if (str.contains("á")){
                    str = str.replaceAll("á", "&aacute;");
                }
                if(str.contains("é")){
                    str = str.replaceAll("é", "&eacute;");
                }
                if(str.contains("í")){
                    str = str.replaceAll("í", "&iacute;");
                }
                if(str.contains("ó")){
                    str = str.replaceAll("ó", "&oacute;");
                }
                if(str.contains("ú")){
                    str = str.replaceAll("ú", "&uacute;");
                }
                if(str.contains("ñ")){
                    str = str.replaceAll("ñ", "&ntilde;");
                }
                str = str.trim();
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para reemplazar los acentos por su formato HTML");
        }
        return str;
    }

    /**
     * Metodo para reemplazar los acentos que viene en configuracion HTML por
     * el texto correspondiente
     * @param data  String que sera recorrido para la revison de loa acentos
     * @return
     * @throws ExceptionHandler
     */
    private String replaceHtml(String data) throws ExceptionHandler{
        String str = "";
        try{
            if (data!=null){
                str=data;
                if (str.contains("&aacute;")){
                    str = str.replaceAll("&aacute;","á");
                }
                if(str.contains("&eacute;")){
                    str = str.replaceAll("&eacute;","é" );
                }
                if(str.contains("&iacute;")){
                    str = str.replaceAll("&iacute;","í");
                }
                if(str.contains("&oacute;")){
                    str = str.replaceAll("&oacute;","ó");
                }
                if(str.contains("&uacute;")){
                    str = str.replaceAll("&uacute;","ú");
                }
                if(str.contains("&ntilde;")){
                    str = str.replaceAll("&ntilde;","ñ");
                }
                str = str.trim();
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para reemplazar los acentos por su formato HTML");
        }
        return str;
    }

    /**
     * Transforma el texto del tipo en Java a el tipo que le corresponde en
     * el XML
     * @param strData   String que sera analizado
     * @return
     */
    private String castTypeJavaToXML(String strData){
        String strSld = "";
        if ("java.lang.String".equals(strData)){
            strSld = "string";
        }else
        if ("java.lang.Integer".equals(strData)){
            strSld = "integer";
        }else
        if ("mx.ilce.bean.Text".equals(strData)){
            strSld = "text";
        }
        return strSld;
    }

     /**
     * Al entregarse un texto con la palabra NULL, entrega un ""
     * @param strData   Texto a analizar
     * @return
     */
    private String castNULL(String strData){
        String sld = "";

        if(!"NULL".equals(strData.toUpperCase()))
            sld = strData;
        return sld;
    }

/******************** METODOS DE LA ENTIDAD ******************************/

    /**
     * NO IMPLEMENTADA
     */
    private StringBuffer getXMLByList(List lst){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA
     */
    private StringBuffer getDataByXML(StringBuffer str){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA
     */
    private boolean validateXML(StringBuffer xml){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA
     */
    private List getListByXML(StringBuffer xml, List lst){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
