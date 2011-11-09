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
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.conection.ConEntidad;
import mx.ilce.conection.ConSession;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * Clase para la implementación de los métodos que se encargaran de transformar
 * los datos en el contenido de un archivo XML, según la configuración o la
 * estructura de los datos
 * @author ccatrilef
 */
public class AdminXML {
    private static File WORKING_DIRECTORY;
    private int numRow=0;
    private boolean deleteIncrement=false;
    private boolean includeForaneo=true;
    private HashCampo hashPermisoForma;
    private HashMap hsForm;
    private Bitacora bitacora;
    private boolean includeHour=true;
    private boolean includeQuery=false;

    /**
     * Obtiene el valor del Validador que indica si se debe incluir la query
     * dentro del conjunto de datos del XML
     * @return  Boolean     Valor validador
     */
    public boolean isIncludeQuery() {
        return includeQuery;
    }

    /**
     * Asigna un valor al Validador que indica si se debe incluir la query
     * dentro del conjunto de datos del XML
     * @param includeQuery  Valor validador
     */
    public void setIncludeQuery(boolean includeQuery) {
        this.includeQuery = includeQuery;
    }

    /**
     * Obtiene el valor del Validador que indica si se debe incluir la hora con
     * los campos de tipo fecha
     * @return  Boolean     Valor validador
     */
    public boolean isIncludeHour() {
        return includeHour;
    }

    /**
     * Asigna un valor al Validador que indica si se debe incluir la hora con
     * los campos de tipo fecha
     * @param includeHour   Valor Validador
     */
    public void setIncludeHour(boolean includeHour) {
        this.includeHour = includeHour;
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
     * Obtiene el HashMap que contiene la data capturada desde un formulario
     * @return  HashMap     HashMap con datos del formulario
     */
    public HashMap getHsForm() {
        return hsForm;
    }

    /**
     * Asigna el HashMap que contiene la data capturada desde un formulario
     * @param hsForm    HashMap con datos del formulario
     */
    public void setHsForm(HashMap hsForm) {
        this.hsForm = hsForm;
    }

    /**
     * Obtiene los permisos de la forma
     * @return      HashCampo   Hash con los permisos
     */
    public HashCampo getHashPermisoForma() {
        return hashPermisoForma;
    }

    /**
     * Asigna los permisos de la Forma
     * @param hashPermisoForma  Hash con los permisos
     */
    public void setHashPermisoForma(HashCampo hashPermisoForma) {
        this.hashPermisoForma = hashPermisoForma;
    }

    /**
     * Obtiene el valor del Validador que indica con TRUE o FALSE si al ir
     * formando el XML se debe incluir o no el listado del foraneo
     * @return      Boolean     Valor validador
     */
    public boolean isIncludeForaneo() {
        return includeForaneo;
    }

    /**
     * Asigna con TRUE o FALSE si al ir formando el XML se debe incluir o no el
     * listado del foraneo
     * @param includeForaneo    Valor validador
     */
    public void setIncludeForaneo(boolean includeForaneo) {
        this.includeForaneo = includeForaneo;
    }

    /**
     * Obtiene el valor del Validador que indica con TRUE o FALSE si al ir
     * formando el XML se debe ignorar o no
     * los datos de tipo Increement
     * @return  Boolean     Estado a aplicar a la variable
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
     * Método que permite obtener la data del usuario en el formato del
     * archivo XML definido para la Session y debe completarse con los datos
     * obtenidos desde la Base de Datos
     * @param user  Bean con los datos del usuario que se va a buscar, debe
     * contener la clave de empleado del usuario
     * @return  StringBuffer    XML con datos de Session
     * @throws ExceptionHandler
     */
    public StringBuffer getSessionXML(User user, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try{
            ConSession con = new ConSession();
            con.setBitacora(this.getBitacora());
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            HashCampo hsCmp = con.getUserXML(user, arrVariables);
            Document document = getDocumentXML("widget.session.xml");
            str.append(listNode(document,0,hsCmp));
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                      "Problemas para obtener el XML de Session");
            eh.setDataToXML(arrVariables);
            eh.setDataToXML(user);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que obtiene el menu en formato XML que le corresponde al usuario conectado
     * según su perfil
     * @param user      Objeto User con los datos del usuario conectado
     * @return  StringBuffer    XML con configuración del Menu
     * @throws ExceptionHandler
     */
    public StringBuffer getMenuXML(User user, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try{
            ConSession con = new ConSession();
            con.setBitacora(this.getBitacora());
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            HashCampo hsCmp = con.getMenuXML(user, arrVariables);

            Document document = getDocumentXML("widget.accordion.xml");
            str.append("<qry>\n")
               .append("\t<sql><![CDATA[")
               .append(hsCmp.getStrQuery())
               .append("]]>\n")
               .append("\t</sql>\n");
            for (int i=0;i<hsCmp.getLengthData();i++){
                str.append(listNode(document,0,hsCmp,i));
            }
            str.append("\n</qry>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de Menu");
            eh.setDataToXML(arrVariables);
            eh.setDataToXML(user);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que entrega los tab en formato XML que le corresponden según el perfil entregado
     * @param perfil    Objeto Perfil con los datos del perfil del usuario conectado
     * @return      StringBuffer    XML con los TAB
     * @throws ExceptionHandler
     */
    public StringBuffer getTabXML(Perfil perfil, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try {
            ConSession con = new ConSession();
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            HashCampo hsCmp = con.getTabForma(perfil, arrVariables);
            Document document = getDocumentXML("widget.tabs.xml");
            str.append("<qry>");
            for (int i=0;i<hsCmp.getLengthData();i++){
                str.append(listNode(document,0,hsCmp,i));
            }
            str.append("</qry>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de TABs");
            eh.setDataToXML(arrVariables);
            eh.setDataToXML(perfil);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que obtiene una grilla XML a partir de los datos entregados, se debe indicar
     * la página que se desea mostrar, junto con el máximo de registros que se
     * deben mostrar por página
     * @param hsData   Data obtenida desde una query previa, debe contener los
     * datos y los campos que le corresponden
     * @param lstCampos Contiene el listado de campos de la forma utilizada, se
     * utiliza para completar los datos equivalentes que le corresponden a cada
     * campo de la columna
     * @param page   Número de página que se desea mostrar del total de datos
     * @param regByPage Número de registros por página que se deben mostrar
     * @return  StringBuffer    XML con los datos para la Grilla
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

            if (this.isIncludeQuery()){
                str.append("<qry>\n<![CDATA[");
                str.append(hsData.getStrQuery());
                str.append("]]>\n</qry>\n");
            }

            str.append(("<page>"+page+"</page>\n"));
            str.append(("<total>"+total+"</total>\n"));
            str.append(("<records>"+reg+"</records>\n"));

            CampoForma cmpForma = (CampoForma) lstCampos.get(0);
            Integer idForma = cmpForma.getClaveForma();
            StringBuffer strForma = new StringBuffer();
            strForma.append("<configuracion_grid>\n");
            strForma.append(getEventoForma(idForma));
            strForma.append("</configuracion_grid>\n");
            str.append(strForma);

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
                                        + castNULL(cmpAux.getAliasCampo().trim())
                                        + "]]></alias_campo>\n"));
                            }
                            if (cmpAux.getTamano()!=null){
                                str.append(("\t<tamano>"+cmpAux.getTamano()+"</tamano>\n"));
                            }
                            if (cmpAux.getDatoSensible()!=null){
                                str.append(("\t<dato_sensible>"+cmpAux.getDatoSensible()+"</dato_sensible>\n"));
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
                        str.append("\t<permiso>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append("><![CDATA[");
                            str.append(cmpAux.getValor()).append("]]></");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</permiso>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            for(int i=regIni;i<hsDat.size();i++){
                ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                str.append(("<row id='"+String.valueOf(i+1)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) arr.get(j) ;
                    if (Integer.class.getName().equals(cmp.getTypeDataAPL())){
                        str.append("\t<cell>");
                        str.append(replaceHtml(castNULL(String.valueOf(cmp.getValor()).trim())));
                        str.append("</cell>\n");
                    }else{
                        str.append("\t<cell><![CDATA[");
                        str.append(replaceHtml(castNULL(String.valueOf(cmp.getValor()).trim())));
                        str.append("]]></cell>\n");
                    }
                }
                str.append("</row>\n");
            }
            str.append("</rows>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de Grid");
            eh.setDataToXML("PÁGINA",page);
            eh.setDataToXML("REGISTROS POR PÁGINA",regByPage);
            eh.setDataToXML(hsData);
            eh.setDataToXML(lstCampos);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que obtiene las columnas de una grilla XML a partir de los datos entregados
     * @param hsData   Data obtenida desde una query previa, debe contener los
     * datos y los campos que le corresponden
     * @param lstCampos Contiene el listado de campos de la forma utilizada, se
     * utiliza para completar los datos equivalentes que le corresponden a cada
     * campo de la columna
     * @return  StringBuffer    XML con las columnas que posee la Grilla
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

            if (this.isIncludeQuery()){
                str.append("<qry>\n<![CDATA[");
                str.append(hsData.getStrQuery());
                str.append("]]>\n</qry>\n");
            }
            CampoForma cmpForma = (CampoForma) lstCampos.get(0);
            Integer idForma = cmpForma.getClaveForma();
            StringBuffer strForma = new StringBuffer();
            strForma.append("<configuracion_grid>\n");
            strForma.append(getEventoForma(idForma));
            strForma.append("</configuracion_grid>\n");
            str.append(strForma);
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
                                    +castNULL(cmpAux.getAliasCampo().trim())
                                    +"]]></alias_campo>\n"));
                        }
                        if (cmpAux.getTamano()!=null){
                            str.append(("\t<tamano>"+cmpAux.getTamano()+"</tamano>\n"));
                        }
                        if (cmpAux.getDatoSensible()!=null){
                            str.append(("\t<dato_sensible>"+cmpAux.getDatoSensible()+"</dato_sensible>\n"));
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
                        str.append("\t<permiso>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append("><![CDATA[");
                            str.append(cmpAux.getValor()).append("]]></");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</permiso>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            str.append("</rows>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML con las columnas de Grid");
            eh.setDataToXML(hsData);
            eh.setDataToXML(lstCampos);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que entrega un XML en base a la Forma indicada y con los datos que
     * se le entregan
     * @param hsData    Data entregada
     * @param lstCampos Listado de campos de la Forma
     * @param idForma   ID de la Forma entregada
     * @return  StringBuffer    XML con los datos de la Forma
     * @throws ExceptionHandler
     */
    public StringBuffer getFormaByData(HashCampo hsData, List lstCampos, 
            Integer idForma, String tipoAccion, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try {
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();

            str.append("<qry>\n")
               .append("\t<sql><![CDATA[")
               .append(hsData.getStrQuery())
               .append("]]>\n")
               .append("\t</sql>\n");
            if (this.getHashPermisoForma()!=null){
                HashCampo hsPerm = this.getHashPermisoForma();
                HashMap hsDatPerm = hsPerm.getListData();
                List lstCamp = hsPerm.getListCampos();
                str.append("<permisos>\n");
                if (!hsDatPerm.isEmpty()){
                    for (int i=0;i<hsDatPerm.size();i++){
                        str.append("\t<permiso>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append("><![CDATA[");
                            str.append(cmpAux.getValor()).append("]]></");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</permiso>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            StringBuffer strForma = new StringBuffer();
            strForma.append("<configuracion_forma>\n");
            strForma.append(getEventoForma(idForma));
            strForma.append("</configuracion_forma>\n");
            StringBuffer strCampos = new StringBuffer();
            for(int i=0;i<hsDat.size();i++){
                ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
                strCampos.append(("<registro id='"+String.valueOf(i+1)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) arr.get(j) ;
                    boolean seguir = true;
                    if (this.isDeleteIncrement()){
                        if (cmp.getIsIncrement()){
                            seguir = false;
                        }
                    }
                    if (seguir){
                        strCampos.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                         + castTypeJavaToXML(cmp.getTypeDataAPL())
                                         + "\""));
                        if (cmp.getIsIncrement()){
                            strCampos.append((" autoincrement=\"TRUE\" "));
                        }

                        strCampos.append((">"));
                        strCampos.append(("<![CDATA["));
                        strCampos.append(replaceAccent(castNULL(String.valueOf(cmp.getValor()).trim())));
                        if ((this.isIncludeHour()) && 
                                ("java.sql.Date".equals(cmp.getTypeDataAPL()))){
                            try{
                                String[] strHour = cmp.getHourMinSec().split(" ");
                                if (strHour.length > 1){
                                    strCampos.append(" ").append(strHour[1]);
                                }
                            }catch(Exception e){ }
                        }
                        strCampos.append("]]>\n");
                        if (cmp.getNombreDB()!=null){
                            CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                            if (cmpAux!=null){
                                if (cmpAux.getAliasCampo()!=null){
                                    strCampos.append(("\t\t<alias_campo><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAliasCampo()).trim()))
                                            + "]]></alias_campo>\n"));
                                }
                                if (cmpAux.getObligatorio()!=null){
                                    strCampos.append(("\t\t<obligatorio><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getObligatorio()).trim())
                                            + "]]></obligatorio>\n"));
                                }
                                if (cmpAux.getTipoControl()!=null){
                                    strCampos.append(("\t\t<tipo_control><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getTipoControl()).trim())
                                            + "]]></tipo_control>\n"));
                                }
                                if (cmpAux.getEvento()!=null){
                                    strCampos.append(("\t\t<evento><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getEvento()).trim())
                                            + "]]></evento>\n"));
                                }
                                if (cmpAux.getClaveFormaForanea()!=null){
                                    strCampos.append("\t\t<foraneo");
                                    if ((cmpAux.getEditaFormaForanea()!=null)&&
                                            ("1".equals(String.valueOf(cmpAux.getEditaFormaForanea())))){
                                        strCampos.append(" agrega_registro=\"true\"");
                                    }else{
                                        strCampos.append(" agrega_registro=\"false\"");
                                    }
                                    strCampos.append((" clave_forma=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    if (this.isIncludeForaneo()){
                                        String[] strData = new String[3];
                                        strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                        strData[1] = "";
                                        if (hsData.getPkData()!=null){
                                            strData[2] = hsData.getPkData();
                                        }else{
                                            strData[2] = cmpAux.getFiltroForaneo();
                                        }
                                        HashMap dataForm = this.getHsForm();
                                        if((dataForm!=null)&&(!dataForm.isEmpty())&&(cmpAux.getFiltroForaneo()!=null) ){
                                            String datoForaneo = (String) dataForm.get(cmpAux.getFiltroForaneo());
                                            if ((datoForaneo!=null)&&(!"".equals(datoForaneo))){
                                                strData[1]=datoForaneo;
                                            }
                                        }
                                        StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB()
                                                                        , arrVariables);
                                        strCampos.append(strForaneo);
                                    }
                                    strCampos.append("\t\t</foraneo>\n");
                                }
                                if (cmpAux.getAyuda()!=null){
                                    strCampos.append(("\t\t<ayuda><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAyuda()).trim()))
                                            + "]]></ayuda>\n"));
                                }
                                if (cmpAux.getDatoSensible()!=null){
                                    strCampos.append(("\t\t<dato_sensible><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getDatoSensible()).trim())
                                            + "]]></dato_sensible>\n"));
                                }
                                if (cmpAux.getActivo()!=null){
                                    strCampos.append(("\t\t<activo><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getActivo()).trim())
                                            + "]]></activo>\n"));
                                }
                                if (cmpAux.getTamano()!=null){
                                    strCampos.append(("\t\t<tamano>"
                                            + castNULL(String.valueOf(cmpAux.getTamano()).trim())
                                            + "</tamano>\n"));
                                }
                                if (cmpAux.getVisible()!=null){
                                    strCampos.append(("\t\t<visible>"
                                            + castNULL(String.valueOf(cmpAux.getVisible()).trim())
                                            + "</visible>\n"));
                                }
                                if (cmpAux.getValorPredeterminado()!=null){
                                    strCampos.append(("\t\t<valor_predeterminado>"
                                            + castNULL(String.valueOf(cmpAux.getValorPredeterminado()).trim())
                                            + "</valor_predeterminado>\n"));
                                }
                                if (cmpAux.getJustificarCambio()!=null){
                                    strCampos.append(("\t\t<justificar_cambio>"
                                            + castNULL(String.valueOf(cmpAux.getJustificarCambio()).trim())
                                            + "</justificar_cambio>\n"));
                                }
                                if (cmpAux.getUsadoParaAgrupar()!=null){
                                    strCampos.append(("\t\t<usado_para_agrupar>"
                                            + castNULL(String.valueOf(cmpAux.getUsadoParaAgrupar()).trim())
                                            + "</usado_para_agrupar>\n"));
                                }
                                if (cmpAux.getNoPermitirValorForaneoNulo()!=null){
                                    strCampos.append(("\t\t<no_permitir_valor_foraneo_nulo>"
                                            + castNULL(String.valueOf(cmpAux.getNoPermitirValorForaneoNulo()).trim())
                                            + "</no_permitir_valor_foraneo_nulo>\n"));
                                }
                            }
                        }
                        strCampos.append(("\t</"+cmp.getNombreDB()+">\n"));
                    }
                }
                strCampos.append("</registro>\n");
            }
            //verificamos $ff
            StringBuffer strFormForan = getFormasForaneas(idForma,arrVariables);
            str.append(strForma);
            str.append(strCampos);
            str.append(strFormForan);
            str.append("</qry>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de la Forma con datos");
            eh.setDataToXML("CLAVE FORMA",idForma);
            eh.setDataToXML("TIPO ACCION",tipoAccion);
            eh.setDataToXML(arrVariables);
            eh.setDataToXML(hsData);
            eh.setDataToXML(lstCampos);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que entrega un XML en base a la Forma indicada y con la estructura de los
     * datos, pero sin datos
     * @param hsData    Data para la generación del XML
     * @param lstCampos     Listado de Campos a considerar desde la Data
     * @param idForma   ID de la Forma utilizada
     * @return  StringBuffer    XML de la Forma, pero sin data
     * @throws ExceptionHandler
     */
    public StringBuffer getFormaWithoutData(HashCampo hsData, List lstCampos, 
            Integer idForma, String tipoAccion, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer str = new StringBuffer();
        try {
            str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            Campo cmp = new Campo();
            List lstCmp = hsData.getListCampos();

            str.append("<qry>\n")
               .append("\t<sql><![CDATA[")
               .append(hsData.getStrQuery())
               .append("]]>\n")
               .append("\t</sql>\n");
            if (this.getHashPermisoForma()!=null){
                HashCampo hsPerm = this.getHashPermisoForma();
                HashMap hsDatPerm = hsPerm.getListData();
                List lstCamp = hsPerm.getListCampos();
                str.append("<permisos>\n");
                if (!hsDatPerm.isEmpty()){
                    for (int i=0;i<hsDatPerm.size();i++){
                        str.append("\t<permiso>\n");
                        ArrayList lstData = (ArrayList) hsDatPerm.get(Integer.valueOf(i));
                        for (int j=0;j<lstCamp.size();j++){
                            Campo cmpCol = (Campo) lstCamp.get(j);
                            Campo cmpAux = (Campo) lstData.get(cmpCol.getCodigo()-1);
                            str.append("\t\t<").append(cmpCol.getNombreDB()).append("><![CDATA[");
                            str.append(cmpAux.getValor()).append("]]></");
                            str.append(cmpCol.getNombreDB()).append(">\n");
                        }
                        str.append("\t</permiso>\n");
                    }
                }
                str.append("</permisos>\n");
            }
            StringBuffer strForma = new StringBuffer();
            strForma.append("<configuracion_forma>\n");
            strForma.append(getEventoForma(idForma));
            strForma.append("</configuracion_forma>\n");
            StringBuffer strCampos = new StringBuffer();
            for(int i=0;i<1;i++){
                strCampos.append(("<registro id='"+String.valueOf(i)+"'>\n"));
                for (int j=0; j<lstCmp.size();j++){
                    cmp = (Campo) lstCmp.get(j) ;
                    boolean seguir = true;
                    if (this.isDeleteIncrement()){
                        if (cmp.getIsIncrement()){
                            seguir = false;
                        }
                    }
                    if (seguir){
                        strCampos.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                         + castTypeJavaToXML(cmp.getTypeDataAPL())
                                         + "\""));
                        if (cmp.getIsIncrement()){
                            strCampos.append((" autoincrement=\"TRUE\" "));
                        }
                        strCampos.append((">"));
                        strCampos.append("<![CDATA[]]>\n");
                        if (cmp.getNombreDB()!=null){
                            CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                            if (cmpAux!=null){
                                if (cmpAux.getAliasCampo()!=null){
                                    strCampos.append(("\t\t<alias_campo><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAliasCampo()).trim()))
                                            + "]]></alias_campo>\n"));
                                }
                                if (cmpAux.getObligatorio()!=null){
                                    strCampos.append(("\t\t<obligatorio><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getObligatorio()).trim())
                                            + "]]></obligatorio>\n"));
                                }
                                //carga_retrasada_foraneo
                                if (cmpAux.getCargaDatoForaneosRetrasada()!=null){
                                    strCampos.append(("\t\t<carga_dato_foraneos_retrasada>"
                                            + castNULL(String.valueOf(cmpAux.getCargaDatoForaneosRetrasada()))
                                            + "</carga_dato_foraneos_retrasada>\n"));
                                }
                                if (cmpAux.getTipoControl()!=null){
                                    strCampos.append(("\t\t<tipo_control><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getTipoControl()).trim())
                                            + "]]></tipo_control>\n"));
                                }
                                if (cmpAux.getEvento()!=null){
                                    strCampos.append(("\t\t<evento><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getEvento()).trim())
                                            + "]]></evento>\n"));
                                }
                                if (cmpAux.getClaveFormaForanea()!=null){
                                    strCampos.append("\t\t<foraneo");
                                    if ((cmpAux.getEditaFormaForanea()!=null)&&
                                            ("1".equals(String.valueOf(cmpAux.getEditaFormaForanea())))){
                                        strCampos.append(" agrega_registro=\"true\"");
                                    }else{
                                        strCampos.append(" agrega_registro=\"false\"");
                                    }
                                    strCampos.append((" clave_forma=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    String[] strData = new String[3];
                                    strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                    strData[1] = "";
                                    if (hsData.getPkData()!=null){
                                        strData[2]=hsData.getPkData();
                                    }else{
                                        strData[2] = String.valueOf(cmpAux.getFiltroForaneo());
                                    }
                                    if (this.isIncludeForaneo()){
                                        HashMap dataForm = this.getHsForm();
                                        if((dataForm!=null)&&(!dataForm.isEmpty())&&(cmpAux.getFiltroForaneo()!=null) ){
                                            String datoForaneo = (String) dataForm.get(cmpAux.getFiltroForaneo());
                                            if ((datoForaneo!=null)&&(!"".equals(datoForaneo))){
                                                strData[1]=datoForaneo;
                                            }
                                        }
                                        StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB(), arrVariables);
                                        strCampos.append(strForaneo);
                                    }
                                    strCampos.append("\t\t</foraneo>\n");
                                }
                                if (cmpAux.getAyuda()!=null){
                                    strCampos.append(("\t\t<ayuda><![CDATA["
                                            + replaceAccent(castNULL(String.valueOf(cmpAux.getAyuda()).trim()))
                                            + "]]></ayuda>\n"));
                                }
                                if (cmpAux.getDatoSensible()!=null){
                                    strCampos.append(("\t\t<dato_sensible><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getDatoSensible()).trim())
                                            + "]]></dato_sensible>\n"));
                                }
                                if (cmpAux.getActivo()!=null){
                                    strCampos.append(("\t\t<activo><![CDATA["
                                            + castNULL(String.valueOf(cmpAux.getActivo()).trim())
                                            + "]]></activo>\n"));
                                }
                                if (cmpAux.getTamano()!=null){
                                    strCampos.append(("\t\t<tamano>"
                                            + castNULL(String.valueOf(cmpAux.getTamano()).trim())
                                            + "</tamano>\n"));
                                }
                                if (cmpAux.getVisible()!=null){
                                            strCampos.append(("\t\t<visible>"
                                            + castNULL(String.valueOf(cmpAux.getVisible()).trim())
                                            + "</visible>\n"));
                                }
                                if (cmpAux.getValorPredeterminado()!=null){
                                            strCampos.append(("\t\t<valor_predeterminado>"
                                            + castNULL(String.valueOf(cmpAux.getValorPredeterminado()).trim())
                                            + "</valor_predeterminado>\n"));
                                }
                                if (cmpAux.getJustificarCambio()!=null){
                                            strCampos.append(("\t\t<justificar_cambio>"
                                            + castNULL(String.valueOf(cmpAux.getJustificarCambio()).trim())
                                            + "</justificar_cambio>\n"));
                                }
                                if (cmpAux.getUsadoParaAgrupar()!=null){
                                            strCampos.append(("\t\t<usado_para_agrupar>"
                                            + castNULL(String.valueOf(cmpAux.getUsadoParaAgrupar()).trim())
                                            + "</usado_para_agrupar>\n"));
                                }
                                if (cmpAux.getNoPermitirValorForaneoNulo()!=null){
                                            strCampos.append(("\t\t<no_permitir_valor_foraneo_nulo>"
                                            + castNULL(String.valueOf(cmpAux.getNoPermitirValorForaneoNulo()).trim())
                                            + "</no_permitir_valor_foraneo_nulo>\n"));
                                }
                            }
                        }
                        strCampos.append(("\t</"+cmp.getNombreDB()+">\n"));
                    }
                }
                strCampos.append("</registro>\n");
            }
            StringBuffer strFormForan = getFormasForaneas(idForma,arrVariables);

            str.append(strForma);
            str.append(strCampos);
            str.append(strFormForan);
            str.append("</qry>");
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de la Forma sin datos");
            eh.setDataToXML("CLAVE FORMA",idForma);
            eh.setDataToXML("TIPO ACCION",tipoAccion);
            eh.setDataToXML(arrVariables);
            eh.setDataToXML(hsData);
            eh.setDataToXML(lstCampos);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que obtiene las formas foraneas con una estructura tipo html. Se
     * revisa el arreglo de variables para revisar si existe la instrucción que
     * gatille su inclusión en el XML. El texto tiene una estructura del tipo HTML.
     * @param claveForma    Clave de la forma analizada
     * @param arrVariables  Arreglo con las variables capturadas
     * @return  StringBuffer    Texto con la configuración
     * @throws ExceptionHandler
     */
    private StringBuffer getFormasForaneas(Integer claveForma, String[][] arrVariables) throws ExceptionHandler{
        StringBuffer strSld = new StringBuffer("");
        StringBuffer strLI = new StringBuffer("");
        StringBuffer strDIV = new StringBuffer("");
        try{
            if (arrVariables!=null){
                boolean seguir = true;
                boolean getFormForan = false;
                for (int i=0;i<arrVariables.length&&seguir;i++){
                    if (arrVariables[i][0].equals("$ff")){
                        seguir = false;
                        if ("true".equals(arrVariables[i][1])){
                            getFormForan = true;
                        }
                    }
                }
                if (getFormForan){
                    if (claveForma!=null){
                        ConEntidad con = new ConEntidad();
                        con.setBitacora(this.getBitacora());

                        String[] arrData = new String[1];
                        arrData[0] = claveForma.toString();

                        DataTransfer dataTransfer= new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(AdminFile.getKey(AdminFile.leerIdQuery(),AdminFile.FORMASFORANEAS)));
                        dataTransfer.setArrData(arrData);

                        HashCampo hsCampo = con.getDataByIdQuery(dataTransfer);

                        if (!hsCampo.getListData().isEmpty()){
                            strSld.append("<formas_foraneas>\n");
                            for (int i=0;i<hsCampo.getListData().size();i++){
                                List lstData = (ArrayList) hsCampo.getListData().get(Integer.valueOf(i));
                                String strClaveAplic = "";
                                String strClaveForma = "";
                                String strForma = "";
                                for (int j=0; j<lstData.size();j++){
                                    Campo cmp = (Campo) lstData.get(j);
                                    if (cmp.getNombreDB().equals("clave_aplicacion")){
                                        strClaveAplic = cmp.getValor();
                                    }
                                    if (cmp.getNombreDB().equals("clave_forma")){
                                        strClaveForma = cmp.getValor();
                                    }
                                    if (cmp.getNombreDB().equals("forma")){
                                        strForma = cmp.getValor();
                                    }
                                }
                                strLI.append("\t<li><a href='#formTab_")
                                     .append(strClaveAplic).append("_")
                                     .append(strClaveForma).append("'>")
                                     .append(strForma)
                                     .append("</a></li>\n");
                                strDIV.append("\t<div id='formTab_")
                                      .append(strClaveAplic).append("_")
                                      .append(strClaveForma).append("'>\n")
                                      .append("\t\t<div id='formGrid_")
                                      .append(strClaveAplic).append("_")
                                      .append(strClaveForma).append("' \n")
                                      .append("\t\t app=\"").append(strClaveAplic).append("\" \n")
                                      .append("\t\t forma=\"").append(strClaveForma).append("\" \n")
                                      .append("\t\t titulo=\"").append(strForma).append("\" \n")
                                      .append("\t\t align='center' class='queued_grids'>\n")
                                      .append("\t\t\t<br/><br/>Cargando información...<br/><br/>\n")
                                      .append("\t\t\t<img src='img/loading.gif'/>\n")
                                      .append("\t\t</div>\n\t</div>\n");
                            }
                            strSld.append(strLI);
                            strSld.append(strDIV);
                            strSld.append("</formas_foraneas>\n");
                        }
                    }
                }
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML de las Formas foraneas");
            eh.setDataToXML("CLAVE FORMA",claveForma);
            eh.setDataToXML(arrVariables);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return strSld;
    }

    /**
     * Método que trae la sección de XML asociada al Evento de una Forma
     * @param claveForma    Clave de la forma a buscar
     * @return  StringBuffer    Sección de XML con los datos de Evento
     * @throws ExceptionHandler
     */
    private StringBuffer getEventoForma(Integer claveForma) throws ExceptionHandler{
        StringBuffer strSld = new StringBuffer();
        try{
            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(Integer.valueOf(AdminFile.getKey(AdminFile.leerIdQuery(),AdminFile.EVENTO)));
            String[] arrData = new String[1];
            arrData[0] = claveForma.toString();
            dataTransfer.setArrData(arrData);
            HashCampo hsCmp = conE.getDataByIdQuery(dataTransfer);
            HashMap hsMp = hsCmp.getListData();
            if (!hsMp.isEmpty()){
                List lst = (List) hsMp.get(0);
                String strTipoEvento = "";
                String strEvento = "";
                String strForma = "";
                String strAliasTab = "";
                String strInstruccion = "";
                for (int i=0;i<lst.size();i++){
                    Campo cmp = (Campo) lst.get(i);
                    if ("tipo_evento".equals(cmp.getNombreDB())){
                        strTipoEvento = cmp.getValor();
                    }
                    if ("evento".equals(cmp.getNombreDB())){
                        strEvento = cmp.getValor();
                    }
                    if ("alias_tab".equals(cmp.getNombreDB())){
                        strAliasTab = cmp.getValor();
                    }
                    if ("instrucciones".equals(cmp.getNombreDB())){
                        strInstruccion = cmp.getValor();
                    }
                    if ("forma".equals(cmp.getNombreDB())){
                        strForma = cmp.getValor();
                    }
                }
                strSld.append("\t<alias_tab><![CDATA[");
                strSld.append(castNULL(strAliasTab));
                strSld.append("]]></alias_tab>\n");
                strSld.append("\t<evento tipo=\"");
                strSld.append(castNULL(strTipoEvento));
                strSld.append("\">");
                strSld.append("<![CDATA[");
                strSld.append(castNULL(strEvento));
                strSld.append("]]>");
                strSld.append("</evento>\n");
                strSld.append("\t<instrucciones><![CDATA[");
                strSld.append(castNULL(strInstruccion));
                strSld.append("]]></instrucciones>\n");
                strSld.append("\t<forma><![CDATA[");
                strSld.append(castNULL(strForma));
                strSld.append("]]></forma>\n");
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML del Evento de la Forma");
            eh.setDataToXML("CLAVE FORMA",claveForma);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return strSld;
    }

    /**
     * Método que permite crear la sección de un XML a partir de la forma que
     * se esta entregando en la data
     * @param strData   Data de entrada que se usara en la query
     * @param strRegistro   Nombre del registro desde donde se invoco el método
     * @return  StringBuffer    Sección de XML asociado a la forma, usado en la
     * construcción de los Datos foraneos
     * @throws ExceptionHandler
     */
    private StringBuffer getXmlByIdForma(String[] strData, String strRegistro, String[][] arrVariables)
            throws ExceptionHandler{
        StringBuffer str = new StringBuffer("");
        try{
            ConEntidad con = new ConEntidad();
            con.setBitacora(this.getBitacora());
            HashCampo hsData = new HashCampo();
            //obtenemos la query de la forma entregada
            String[] strDataQ = new String[2];
            strDataQ[0] =strData[0];
            strDataQ[1] ="foreign";
            con.getBitacora().setEnable(false);

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(con.getIdQuery(AdminFile.FORMAQUERY));
            dataTransfer.setArrData(strDataQ);
            dataTransfer.setArrVariables(arrVariables);

            HashCampo hsCmpQ = con.getDataByIdQuery(dataTransfer);

            Campo cmpQ = hsCmpQ.getCampoByName("claveconsulta");
            HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmpQ.getCodigo()-1);
                    String[] strDataFiltro = new String[1];
                    strDataFiltro[0] = strData[2];
                    //ejecutamos la query, con el filtro entregado
                    if ("".equals(strData[1])){
                        con.getBitacora().setEnable(false);
                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setArrData(strDataFiltro);
                        dataTransfer.setArrVariables(arrVariables);

                        hsData = con.getDataByIdQuery(dataTransfer);
                    }else{
                        con.getBitacora().setEnable(false);
                        dataTransfer = new DataTransfer();
                        dataTransfer.setIdQuery(Integer.valueOf(cmpAux.getValor()));
                        dataTransfer.setStrWhere(strData[1]);
                        dataTransfer.setArrData(strDataFiltro);
                        dataTransfer.setArrVariables(arrVariables);

                        hsData = con.getDataByIdQueryAndWhereAndData(dataTransfer);
                    }
                }
            List lstCmp = hsData.getListCampos();
            HashMap hsDat = hsData.getListData();
            if (!hsDat.isEmpty()){
                str.append(("\t\t\t<qry_"+strRegistro)).append(">\n");
                str.append("\t\t\t\t<sql><![CDATA[")
                   .append(hsCmpQ.getStrQuery())
                   .append("]]>\n")
                   .append("\t\t\t\t</sql>\n");
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
                str.append(("\t\t\t</qry_"+strRegistro+">\n"));
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el XML por el ID de la forma");
            eh.setDataToXML("REGISTRO", strRegistro);
            eh.setDataToXML(strData);
            eh.setDataToXML(arrVariables);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Obtiene los datos de un campo, obtenidos desde la forma que se le entrega
     * @param lstData   Listado con la configuración de la forma
     * @param nombreCampo   Campo que se esta buscando desde la forma
     * @return  CampoForma  Datos del CampoForma
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
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el campo desde una forma");
            eh.setDataToXML("NOMBRE DEL CAMPO", nombreCampo);
            eh.setDataToXML(lstData);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return cmp;
    }

    /**
     * Obtiene un Documento XML, a partir del nombre solicitado. Se asume que
     * los archivos XML estan en la ruta resource/xml/[nombre archivo]
     * @param fileName  Nombre del archivo
     * @return  Document    Documento obtenido tras leer un archivo fisico XML
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
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtener el documento XML desde /resource");
            eh.setDataToXML("ARCHIVO", fileName);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return documento;
    }

    /**
     * Método que recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde según el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando es un registro único.
     * @param e     Nodo de inicio de la lectura de datos
     * @param level     Nivel en que se encuentra la lectura
     * @param hsCmp     Conjunto de datos que serán analizados
     * @return  StringBuffer    Sección de XML obtenido
     * @throws ExceptionHandler
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp) throws ExceptionHandler{
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
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para completar el XML con los datos");
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde según el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando son más de un registro, su llamado es recursivo.
     * @param e     Nodo de inicio de la lectura de datos
     * @param level     Nivel en que se encuentra la lectura
     * @param hsCmp     Conjunto de datos que serán analizados
     * @param register  Número del registro en que se vera la lectura
     * @return  StringBuffer    Sección de XML Obtenido
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
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para completar el XML con los datos");
            eh.setDataToXML("REGISTRO", String.valueOf(register));
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método para entregar las respuestas en un formato de XML, asociado a un
     * Tag "pk"
     * @param data  Data de la respuesta
     * @return  String  XML con la respuesta
     */
    public String salidaXML(String data){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<qry>\n");
        str.append("<pk>").append(data).append("</pk>\n");
        str.append("</qry>");

        return str.toString();
    }

    /**
     * Método para entregar una respuesta en un formato de XML, asociado a un
     * Tag "respuesta"
     * @param data  Data de la respuesta
     * @return  String  XML con la respuesta
     */
    public String salidaXMLResponse(String data){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<qry>\n");
        str.append("<respuesta>").append(data).append("</respuesta>\n");
        str.append("</qry>");

        return str.toString();
    }

    /**
     * Método para entregar las respuestas de la operación y la bitácora
     * en un formato XML, asociado a los Tag "pk" y "clave_bitacora"
     * @param data  Data de la respuesta
     * @param idBitacora    ID de la bitácora
     * @return  String  XML con la respuesta
     */
    public String salidaXMLBitacora(String data, String idBitacora){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<qry>\n");
        str.append("<pk>").append(data).append("</pk>\n");
        str.append("<clave_bitacora>").append(idBitacora).append("</clave_bitacora>\n");
        str.append("</qry>");

        return str.toString();
    }

    /**
     * Método que reemplaza los caracteres con acento y ñ, por su codificación HTML respectiva
     * @param data  String de datos donde se buscaran los caracteres especiales
     * @return  String  String con los acentos reemplazados
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
                if (str.contains("Á")){
                    str = str.replaceAll("Á", "&Aacute;");
                }
                if(str.contains("é")){
                    str = str.replaceAll("é", "&eacute;");
                }
                if(str.contains("É")){
                    str = str.replaceAll("É", "&Eacute;");
                }
                if(str.contains("í")){
                    str = str.replaceAll("í", "&iacute;");
                }
                if(str.contains("Í")){
                    str = str.replaceAll("Í", "&Iacute;");
                }
                if(str.contains("ó")){
                    str = str.replaceAll("ó", "&oacute;");
                }
                if(str.contains("Ó")){
                    str = str.replaceAll("Ó", "&Oacute;");
                }
                if(str.contains("ú")){
                    str = str.replaceAll("ú", "&uacute;");
                }
                if(str.contains("Ú")){
                    str = str.replaceAll("Ú", "&Uacute;");
                }
                if(str.contains("ñ")){
                    str = str.replaceAll("ñ", "&ntilde;");
                }
                if(str.contains("Ñ")){
                    str = str.replaceAll("Ñ", "&Ntilde;");
                }
                str = str.trim();
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para reemplazar los caracteres por su formato HTML");
            eh.setDataToXML("TEXTO", data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método para reemplazar los acentos que vienen en la configuracion HTML por
     * el texto correspondiente
     * @param data  String que será recorrido para la revisión de loa acentos
     * @return  String  String con los acentos reemplazados
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
                if (str.contains("&Aacute;")){
                    str = str.replaceAll("&Aacute;","Á");
                }
                if(str.contains("&eacute;")){
                    str = str.replaceAll("&eacute;","é" );
                }
                if(str.contains("&Eacute;")){
                    str = str.replaceAll("&Eacute;","É" );
                }
                if(str.contains("&iacute;")){
                    str = str.replaceAll("&iacute;","í");
                }
                if(str.contains("&Iacute;")){
                    str = str.replaceAll("&Iacute;","Í");
                }
                if(str.contains("&oacute;")){
                    str = str.replaceAll("&oacute;","ó");
                }
                if(str.contains("&Oacute;")){
                    str = str.replaceAll("&Oacute;","Ó");
                }
                if(str.contains("&uacute;")){
                    str = str.replaceAll("&uacute;","ú");
                }
                if(str.contains("&Uacute;")){
                    str = str.replaceAll("&Uacute;","Ú");
                }
                if(str.contains("&ntilde;")){
                    str = str.replaceAll("&ntilde;","ñ");
                }
                if(str.contains("&Ntilde;")){
                    str = str.replaceAll("&Ntilde;","Ñ");
                }
                str = str.trim();
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para reemplazar los Textos HTML por el caracter correspondiente");
            eh.setDataToXML("TEXTO", data);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return str;
    }

    /**
     * Método que transforma el texto del tipo en Java a el tipo que le corresponde en
     * el XML
     * @param strData   String que sera analizado
     * @return  String  String con la conversión del tipo de dato
     */
    private String castTypeJavaToXML(String strData){
        String strSld = "";
        if ("java.lang.String".equals(strData)){
            strSld = "string";
        }else if ("java.lang.Integer".equals(strData)){
            strSld = "integer";
        }else if ("mx.ilce.bean.Text".equals(strData)){
            strSld = "text";
        }else if ("mx.ilce.bean.BIT".equals(strData)){
            strSld = "bit";
        }else if ("mx.ilce.bean.Money".equals(strData)){
            strSld = "money";
        }else if ("java.sql.Date".equals(strData)){
            strSld = "datetime";
        }
        return strSld;
    }

     /**
     * Método que al recibbir un texto con la palabra NULL, o con valor null entrega un ""
     * @param strData   Texto a analizar
     * @return  String  String con el resultado del analisis
     */
    private String castNULL(String strData){
        String sld = "";
        if (strData!=null){
            if(!"NULL".equals(strData.toUpperCase()))
                sld = strData;
        }
        return sld;
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    private StringBuffer getXMLByList(List lst){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    private StringBuffer getDataByXML(StringBuffer str){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    private boolean validateXML(StringBuffer xml){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADA, al no requerirse de momento
     */
    private List getListByXML(StringBuffer xml, List lst){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
