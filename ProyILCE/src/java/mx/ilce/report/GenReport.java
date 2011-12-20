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
package mx.ilce.report;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.UtilDate;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Clase generadora de los reportes, tras la obtencion de los datos para su
 * configuracion y generacion.
 * @author ccatrilef
 */
public class GenReport  implements Serializable{

    protected static String foNS = "http://www.w3.org/1999/XSL/Format";
    private FopFactory fopFactory;
    private Document mainDoc;
    private Report report;

    private List lstLayoutConf;
    private List lstPageMasterConf;
    private List lstRegionBodyConf;
    private List lstPageSequenceConf;
    private List lstFlowConf;
    private List lstPageTitleConf;
    private List lstTitleConf;
    private List lstSecuenceConf;
    //
    private static String BLOCK = "BLOCK";
    private static String TABLE = "TABLE";
    private static String TITLE = "TITLE";

    private List listStruct;
    private HashMap hsConfigStruct;
    private HashMap hsElementStruct;
    private HashMap hsConfigElement;
    private HashMap hsQuery;

    private String rutaReport;
    private Integer idUser;

    private List lstElementReport;

    public List getLstElementReport() {
        return lstElementReport;
    }

    public void setLstElementReport(List lstElementReport) {
        this.lstElementReport = lstElementReport;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getRutaReport() {
        return rutaReport;
    }

    public void setRutaReport(String rutaReport) {
        this.rutaReport = rutaReport;
    }

    public List getLstSecuenceConf(){
        return lstSecuenceConf;
    }

    public void setLstSecuenceConf(List lstSecuenceConf) {
        this.lstSecuenceConf = lstSecuenceConf;
    }

    public List getLstTitleConf() {
        return lstTitleConf;
    }

    public void setLstTitleConf(List lstTitleConf) {
        this.lstTitleConf = lstTitleConf;
    }

    public HashMap getHsQuery() {
        return hsQuery;
    }

    public void setHsQuery(HashMap hsQuery) {
        this.hsQuery = hsQuery;
    }

    public List getLstFlowConf() {
        return lstFlowConf;
    }

    public void setLstFlowConf(List lstFlowConf) {
        this.lstFlowConf = lstFlowConf;
    }

    public List getLstLayoutConf() {
        return lstLayoutConf;
    }

    public void setLstLayoutConf(List lstLayoutConf) {
        this.lstLayoutConf = lstLayoutConf;
    }

    public List getLstPageMasterConf() {
        return lstPageMasterConf;
    }

    public void setLstPageMasterConf(List lstPageMasterConf) {
        this.lstPageMasterConf = lstPageMasterConf;
    }

    public List getLstPageSequenceConf() {
        return lstPageSequenceConf;
    }

    public void setLstPageSequenceConf(List lstPageSequenceConf) {
        this.lstPageSequenceConf = lstPageSequenceConf;
    }

    public List getLstRegionBodyConf() {
        return lstRegionBodyConf;
    }

    public void setLstRegionBodyConf(List lstRegionBodyConf) {
        this.lstRegionBodyConf = lstRegionBodyConf;
    }

    public List getLstPageTitleConf() {
        return lstPageTitleConf;
    }

    public void setLstPageTitleConf(List lstPageTitleConf) {
        this.lstPageTitleConf = lstPageTitleConf;
    }

    public HashMap getHsConfigElement() {
        return hsConfigElement;
    }

    public void setHsConfigElement(HashMap hsConfigElement) {
        this.hsConfigElement = hsConfigElement;
    }

    public HashMap getHsConfigStruct() {
        return hsConfigStruct;
    }

    public void setHsConfigStruct(HashMap hsConfigStruct) {
        this.hsConfigStruct = hsConfigStruct;
    }

    public HashMap getHsElementStruct() {
        return hsElementStruct;
    }

    public void setHsElementStruct(HashMap hsElementStruct) {
        this.hsElementStruct = hsElementStruct;
    }

    public List getListStruct() {
        return listStruct;
    }

    public void setListStruct(List listStruct) {
        this.listStruct = listStruct;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    /**
     * Método que inicializa los listados de configuración antes de ser procesados.
     */
    private void inicializeList(){
        setLstLayoutConf(new ArrayList());
        setLstPageMasterConf(new ArrayList());
        setLstRegionBodyConf(new ArrayList());
        setLstPageSequenceConf(new ArrayList());
        setLstFlowConf(new ArrayList());
        setLstPageTitleConf(new ArrayList());
        setLstTitleConf(new ArrayList());
        setLstSecuenceConf(new ArrayList());
    }

    /**
     * Método que se encarga de generar el documento a partir de las secciones
     * configuradas
     * @return  Document    Documento generado
     * @throws ExceptionHandler
     * @throws ParserConfigurationException
     */
    private Document getDocummentGral() throws ExceptionHandler, ParserConfigurationException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        mainDoc = db.newDocument();
        //ROOT
        Element root = mainDoc.createElementNS(foNS, "fo:root");
        mainDoc.appendChild(root);
        //LAYOUT
        if (this.getLstLayoutConf()!=null){
            Element elLayout = createLayoutConf(this.getLstLayoutConf());
            root.appendChild(elLayout);
            //PAGEMASTER
            if (this.getLstPageMasterConf()!=null){
                Element pageMaster = createPageMasterConf(this.getLstPageMasterConf());
                elLayout.appendChild(pageMaster);
                //REGION BODY
                if (this.getLstRegionBodyConf()!=null){
                    Element regionBody = createRegionBodyConf(this.getLstRegionBodyConf());
                    pageMaster.appendChild(regionBody);
                }
            }
        }
        if (this.getLstPageSequenceConf()!=null){
            //PAGESEQUENCE
            Element pageSequence = createPageSequenceConf(this.getLstPageSequenceConf());
            root.appendChild(pageSequence);
            //FLOW
            if (this.getLstFlowConf()!=null){
                Element flow = createFlowConf(this.getLstFlowConf());
                pageSequence.appendChild(flow);
                if (this.getLstTitleConf()!=null){
                    //PAGE TITLE
                    List lstPage = this.getLstTitleConf();
                    Element title = createTitleConf(this.getLstPageTitleConf(), lstPage);
                    flow.appendChild(title);
                }
                //listado con datos
                //BLOCK
                if (this.getLstSecuenceConf()!=null){
                    Iterator it = this.getLstSecuenceConf().iterator();
                    while (it.hasNext()){
                        Section sec = (Section) it.next();
                        String strTipo = sec.getTipo();
                        if ("TITLE".equals(strTipo)){
                            Element title = (Element) sec.getData();
                            mainDoc.adoptNode(title);
                            flow.appendChild(title);
                        }else if("TABLE".equals(strTipo)){
                            Element tableElem = (Element) sec.getData();
                            mainDoc.adoptNode(tableElem);
                            flow.appendChild(tableElem);
                        }else if("IMAGE".equals(strTipo)){

                        }else if ("BLOCK".equals(strTipo)){
                            Element block = (Element) sec.getData();
                            mainDoc.adoptNode(block);
                            flow.appendChild(block);
                        }
                    }
                }
            }
        }
        return mainDoc;
    }

    /**
     * Generador de un archivo PDF a partir de la configuración de un documento
     * @param xslfoDoc  Configuración del documento
     * @param pdf   Archivo PDF que debe generarse
     */
    private void convertDOM2PDF(Document xslfoDoc, File pdf) {
        try {
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            OutputStream out = new java.io.FileOutputStream(pdf);

            out = new java.io.BufferedOutputStream(out);
            try {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                // Configuración XSLT para la transformación
                Source src = new DOMSource(xslfoDoc);
                // Resultado del SAX
                Result res = new SAXResult(fop.getDefaultHandler());

                // transformación
                transformer.transform(src, res);
            } finally {
                out.close();
            }
        }catch (Exception e) {
                e.printStackTrace(System.err);
        }
    }

    /**
     * Método para la generación de un documento a partir del Reporte
     * asignado
     * @throws ExceptionHandler
     */
    public void getDocument() throws ExceptionHandler{
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        File pdffile = null;
        Document foDoc = null;
        try {
            fopFactory = FopFactory.newInstance();
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            db = dbf.newDocumentBuilder();
            db.reset();
            mainDoc = db.newDocument();

            //directorios
            File baseDir = new File(getRutaFilePDF());
            File outDir = new File(baseDir, this.getIdUser().toString()
                                            + baseDir.separator + "report" );
            if (!outDir.exists()){
                outDir.mkdirs();
            }
            UtilDate ut = new UtilDate();
            String nameFileReport = "REP_"
                    + this.getReport().getIdReport()
                    + "_"
                    //+ ut.getFechaHMS(UtilDate.formato.AMD,"","")
                    + ut.getFecha(UtilDate.formato.AMD,"")
                    + ".pdf";
            pdffile = new File(outDir, nameFileReport );

            inicializeList();
            getSeccionData();
            //RESULTADO
            foDoc = getDocummentGral();
            convertDOM2PDF(foDoc, pdffile);
            this.setRutaReport(pdffile.getPath());
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AdmReport.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (db!=null){
                db.reset();
            }
            if (foDoc!=null){
                foDoc = null;
            }
        }
    }

    /**
     * Método que obtiene la ruta del directorio donde sera colocado el archivo
     * de reporte
     * @return  String  Ruta obtenida para el archivo
     * @throws ExceptionHandler
     */
    public String getRutaFilePDF() throws ExceptionHandler{
        String sld = "";

        Properties prop = AdminFile.leerConfig();
        String rutaBibl = AdminFile.getKey(prop, AdminFile.BIBLIOTECA);
        String Proyecto = AdminFile.getKey(prop, AdminFile.PROYECTO);
        String[] data = rutaBibl.split("/");

        URL url = AdminFile.class.getResource("AdminFile.class");
        String[] urlDir = url.getPath().split("/");

        boolean seguir = true;
        int pos=0;
        for (int i=0;i<urlDir.length&&seguir;i++){
            if (urlDir[i].equals(Proyecto)){
               pos = urlDir.length - i;
               seguir= false;
            }
        }
        File fWork = null;
        if(url.getProtocol().equals("file")) {
            try {
                fWork = new File(url.toURI());
                for (int j=0;j<pos;j++){
                    fWork = fWork.getParentFile();
                }
            } catch (URISyntaxException ex) {}
        }
        File srcDir = new File(fWork.getPath());
        for (int i=1;i<data.length;i++){
            srcDir = new File(srcDir.getPath() + fWork.separator + data[i]);
            if (!srcDir.exists()) {
                srcDir.mkdir();
            }
        }
        rutaBibl = srcDir.getPath() + srcDir.separator;

        if (rutaBibl!=null){
            sld = rutaBibl;
        }
        return sld;
    }

    /**
     * Método que entrega las configuraciones de las distintas secciones que
     * componen el documento
     * @return  ArrayList   Listado con las configuraciones
     * @throws ExceptionHandler
     */
    private ArrayList getSeccionData() throws ExceptionHandler{
        ArrayList sld = new ArrayList();

        //Obtenemos la configuracion general
        llenaConfigTabla();

        List lstStruct = this.getListStruct();
        if (lstStruct!=null){
            Iterator itST = lstStruct.iterator();
            while (itST.hasNext()){
                Structure struct = (Structure) itST.next();
                Integer idST = struct.getIdStructure();
                String strType = struct.getTypeStructure();
                boolean isMain = ((struct.getMainFig()==1)?true:false);

                HashMap hsCNFST = this.getHsConfigStruct();
                List lstCNFST = (List) hsCNFST.get(idST);

                if ((strType.equals("layout")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstLayoutConf(lstCNFST);
                    }
                }else if ((strType.equals("pageMaster")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstPageMasterConf(lstCNFST);
                    }
                }else if ((strType.equals("regionBody")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstRegionBodyConf(lstCNFST);
                    }
                }else if ((strType.equals("pageSequence")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstPageSequenceConf(lstCNFST);
                    }
                }else if ((strType.equals("flow")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstFlowConf(lstCNFST);
                    }
                }else if ((strType.equals("pageTitle")) &&(isMain)) {
                    if (lstCNFST!=null){
                        this.setLstPageTitleConf(lstCNFST);
                    }
                    List lstElem = (List) this.getHsElementStruct().get(idST);
                    if (lstElem!=null){
                        this.setLstTitleConf(lstElem);
                    }
                }else if ((strType.equals("title")) &&(!isMain)) {
                        List lstElem = (List) this.getHsElementStruct().get(idST);
                        List lstCNFEL = (List) this.getHsConfigElement().get(idST);
                        Element elTitle = this.createTitle(lstElem,lstCNFEL);
                        this.addSeccion(TITLE, elTitle);
                }else if ((strType.equals("table")) &&(!isMain)) {
                        List lstElem = (List) this.getHsElementStruct().get(idST);
                        List lstQuery = (List) this.getHsQuery().get(idST);
                        HashMap hsData = new HashMap();
                        if (lstQuery!=null){
                            QueryStruct qryRep = (QueryStruct) lstQuery.get(0);
                            if (qryRep.getIsExtern()==0){
                                hsData = getData(qryRep.getStrQuery());
                            }
                        }
                        Element table = this.createTable(lstCNFST,lstElem,hsData);
                        this.addSeccion(TABLE, table);
                }else if ((strType.equals("block")) &&(!isMain)){
                        List lstElem = (List) this.getHsElementStruct().get(idST);
                        Element elBlock = createBlock(lstCNFST, lstElem);
                        this.addSeccion(BLOCK, elBlock);
                }else if ((strType.equals("img")) &&(!isMain)){

                }
            }
        }
        return sld;
    }


    /**
     * Método que obtiene la configuración general que posee el reporte y
     * deposita los resultados en los listados de las distintas secciones
     * @throws ExceptionHandler
     */
    private void llenaConfigTabla() throws ExceptionHandler{

        String query = "select st.id_structure , st.structure "
                + " , ts.typeStructure , ts.main, st.orden "
                + " from RPF_Structure st, RPF_TypeStructure ts "
                + " where st.id_report = " + this.getReport().getIdReport()
                + " and st.id_typeStructure = ts.id_typeStructure "
                + " order by st.orden";

        ConReport conR = new ConReport();
        conR.setQuery(query);
        List lstStruct = conR.getListStructure();
        this.setListStruct(lstStruct);

        query = "select * from ("
                + " select cs.id_configStructure, cs.id_structure , tc.typeConfig "
                + " , cv.ConfigValue , ts.typeStructure , st.orden "
                + " from RPF_ConfigStructure cs, RPF_Structure st"
                + " , RPF_TypeConfig tc , RPF_ConfigValue cv, RPF_TypeStructure ts"
                + " where st.id_report = " + this.getReport().getIdReport()
                + " and st.id_structure = cs.id_structure"
                + " and cs.id_typeConfig = tc.id_typeConfig"
                + " and cs.id_configValue = cv.id_configValue"
                + " and st.id_typeStructure = ts.id_typeStructure"
                + " union"
                + " select cs.id_configStructure, cs.id_structure, tc.typeConfig , "
                + " cs.configValue + me.measure , ts.typeStructure , st.orden"
                + " from RPF_ConfigStructure cs, RPF_Structure st"
                + " , RPF_TypeConfig tc , RPF_Measure me, RPF_TypeStructure ts"
                + " where st.id_report = " + this.getReport().getIdReport()
                + " and st.id_structure = cs.id_structure"
                + " and cs.id_typeConfig = tc.id_typeConfig"
                + " and cs.id_measure = me.id_measure"
                + " and st.id_typeStructure = ts.id_typeStructure"
                + " and cs.id_configValue is null"
                + ") as T order by id_structure, orden";

        conR.setQuery(query);
        HashMap hsMapConfigStruct = conR.getListConfigStruct();
        this.setHsConfigStruct(hsMapConfigStruct);

        query = "select es.id_elementStruct , es.id_structure, es.valueElement"
                + " , es.orden , te.typeElement"
                + " from RPF_ElementStruct es, RPF_Structure st"
                + " , RPF_TypeElement te"
                + " where st.id_structure = es.id_structure"
                + " and st.id_report = " + this.getReport().getIdReport()
                + " and es.id_typeElement = te.id_typeElement "
                + " order by es.id_structure, es.orden";

        conR.setQuery(query);
        HashMap hsMapElementStruct = conR.getListElementStruct();
        this.setHsElementStruct(hsMapElementStruct);

        query = "select * from ("
                + " select ce.id_configElement , es.id_elementStruct "
                + " , tc.typeConfig, cv.ConfigValue "
                + " , te.typeElement, es.orden"
                + " from RPF_ConfigElement ce , RPF_ElementStruct es "
                + " , RPF_Structure st , RPF_TypeConfig tc "
                + " , RPF_ConfigValue cv, RPF_TypeElement te"
                + " where ce.id_elementStruct = es.id_elementStruct"
                + " and es.id_structure = st.id_structure"
                + " and st.id_report = " + this.getReport().getIdReport()
                + " and ce.id_typeConfig = tc.id_typeConfig"
                + " and ce.id_configValue = cv.id_configValue"
                + " and es.id_typeElement = te.id_typeElement"
                + " union "
                + " select ce.id_configElement , es.id_elementStruct"
                + " , tc.typeConfig, ce.configValue + me.measure "
                + " , te.typeElement, es.orden"
                + " from RPF_ConfigElement ce , RPF_ElementStruct es "
                + " , RPF_Structure st , RPF_TypeConfig tc "
                + " , RPF_TypeElement te , RPF_Measure me "
                + " where ce.id_elementStruct = es.id_elementStruct"
                + " and es.id_structure = st.id_structure "
                + " and st.id_report = " + this.getReport().getIdReport()
                + " and ce.id_typeConfig = tc.id_typeConfig "
                + " and ce.id_configValue is null "
                + " and es.id_typeElement = te.id_typeElement "
                + " and ce.id_measure = me.id_measure"
                + ") as T order by id_elementStruct , orden";

        conR.setQuery(query);
        HashMap hsMapConfigElement = conR.getListConfigElementStruct();
        this.setHsConfigElement(hsMapConfigElement);

        query = "select qr.id_queryReport, qr.id_structure, qr.query, qr.isExtern "
                + " from RPF_QueryReport qr , RPF_Structure st "
                + " where qr.id_structure = st.id_structure "
                + " and st.id_report = " + this.getReport().getIdReport()
                + " order by st.orden";

        conR.setQuery(query);
        HashMap hsMapQuery = conR.getListQueries();
        this.setHsQuery(hsMapQuery);
    }

    /**
     * Método que obtiene la data que contiene una sección. Esta data se obtiene
     * a partir de la query que posee la sección. Si una sección no posee una
     * query no realiza ninguna acción
     * @param strQuery  Query a ejecutar
     * @return  HashMap Data obtenida mediante la query
     * @throws ExceptionHandler
     */
    private HashMap getData(String strQuery) throws ExceptionHandler{
        HashMap hsMap = new HashMap();

        if (strQuery!=null){
            ConReport con = new ConReport();
            HashCampo hsCampo = con.getDataWithWhereAndData(strQuery,
                                                this.getReport().getStrWhere(),
                                                this.getReport().getArrData(),
                                                this.getReport().getArrVariables());
            hsMap.put(1,hsCampo);
        }
        return hsMap;
    }

    /**
     * Método para construir un Título
     * @param lst   Listado con la configuración del título
     * @param texto Texto que contendra el título
     * @return  Element Elemento con el resultado
     */
    private Element createTitle(List lstText, List lstConfigElement){
        Element sld = null;
        Text elementText = null;
        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (lstConfigElement!=null){
            if (!lstConfigElement.isEmpty()){
                Iterator it = lstConfigElement.iterator();
                while (it.hasNext()){
                    ElementStruct elem = (ElementStruct) it.next();
                    sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
                }
            }
        }
        if (lstText!=null){
            ElementStruct elem = (ElementStruct) lstText.get(0);
            elementText = mainDoc.createTextNode(elem.getValueElement());
        }else{
            elementText = mainDoc.createTextNode("");
        }
        sld.appendChild(elementText);
        return sld;
    }

    /**
     * Método que crea un Titulo para un elemento
     * @param lstConfigElement
     * @param lstText
     * @return
     */
    private Element createTitleConf(List lstConfigElement, List lstText){
        Element sld = null;
        Text elementText = null;
        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (lstConfigElement!=null){
            if (!lstConfigElement.isEmpty()){
                Iterator it = lstConfigElement.iterator();
                while (it.hasNext()){
                    Config elem = (Config) it.next();
                    sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
                }
            }
        }
        if ((lstText!=null)&&(!lstText.isEmpty())){
            ElementStruct elem = (ElementStruct) lstText.get(0);
            elementText = mainDoc.createTextNode(elem.getValueElement());
            sld.appendChild(elementText);
        }
        return sld;
    }

    /**
     * Método encargado de agregar una sección a la configuración del documento
     * @param typeData
     * @param obj
     */
    private void addSeccion(String typeData, Object obj){
        Section seccion = new Section();
        seccion.setTipo(typeData);
        seccion.setData(obj);
        this.getLstSecuenceConf().add(seccion);
    }

    /**
     * Método que contruye un bloque
     * @param lst
     * @param lstElem
     * @return
     */
    private Element createBlock(List lst, List lstElem){
        Element sld = null;
        Text elementText = null;
        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (lst!=null){
            if (!lst.isEmpty()){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config elem = (Config) it.next();
                    sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
                }
            }
        }
        String texto = "";
        if (lstElem!=null){
            ElementStruct elem = (ElementStruct) lstElem.get(0);
            texto = elem.getValueElement();
        }
        elementText = mainDoc.createTextNode(texto);
        sld.appendChild(elementText);
        return sld;
    }

    /**
     * Método que construye una tabla
     * @param lstConfig
     * @param elemStruct
     * @param hsDataTable
     * @return
     */
    private Element createTable(List lstConfig, List elemStruct, HashMap hsDataTable){
        Element sld = null;
        Element elTable = null;
        Element tableBody = null;
        Element tableColum = null;
        Element row = null;

        HashMap hsTitle = new HashMap();
        Integer intTitle = 1;
        HashMap hsHeaderColumn = new HashMap();
        Integer intHeaderColumn = 1;
        HashMap hsHeaderRow = new HashMap();
        Integer intHeaderRow = 1;
        HashMap hsFooter = new HashMap();
        Integer intFooter = 1;
        HashMap hsBlock = new HashMap();
        Integer intBlock = 1;
        HashMap hsFirstColumn = new HashMap();
        Integer intFirstColumn = 1;
        HashMap hsLastColumn = new HashMap();
        Integer intLastColumn = 1;
        HashMap hsColumn = new HashMap();
        Integer intColumn = 1;
        HashMap hsFirstRow = new HashMap();
        Integer intFirstRow = 1;
        HashMap hsLastRow = new HashMap();
        Integer intLastRow = 1;
        HashMap hsRow = new HashMap();
        Integer intRow = 1;
        HashMap hsBodyTable = new HashMap();
        Integer intBodyTable = 1;
        HashMap hsCell = new HashMap();
        HashMap hsFirstCell = new HashMap();

        if (elemStruct!=null){
            Iterator it = elemStruct.iterator();
            while (it.hasNext()){
                ElementStruct elem = (ElementStruct) it.next();
                if (elem.getTypeElement().equals("Title")){
                    hsTitle.put(intTitle++, elem);                      //UNO
                }else if (elem.getTypeElement().equals("HeaderColumn")){
                    hsHeaderColumn.put(intHeaderColumn++, elem);        //VARIOS
                }else if (elem.getTypeElement().equals("HeaderRow")){
                    hsHeaderRow.put(intHeaderRow++, elem);              //VARIOS
                }else if (elem.getTypeElement().equals("Footer")){
                    hsFooter.put(intFooter++, elem);                    //VARIOS
                }else if (elem.getTypeElement().equals("Block")){
                    hsBlock.put(intBlock++, elem);                      //UNO
                }else if (elem.getTypeElement().equals("FirstColumn")){
                    hsFirstColumn.put(intFirstColumn++, elem);          //UNO
                }else if (elem.getTypeElement().equals("LastColumn")){
                    hsLastColumn.put(intLastColumn++, elem);            //UNO
                }else if (elem.getTypeElement().equals("Column")){
                    hsColumn.put(intColumn++, elem);                    //UNO
                }else if (elem.getTypeElement().equals("FirstRow")){
                    hsFirstRow.put(intFirstRow++, elem);                //UNO
                }else if (elem.getTypeElement().equals("LastRow")){
                    hsLastRow.put(intLastRow++, elem);                  //UNO
                }else if (elem.getTypeElement().equals("Row")){
                    hsRow.put(intRow++, elem);                          //UNO
                }else if (elem.getTypeElement().equals("BodyTable")){
                    hsBodyTable.put(intBodyTable++, elem);              //UNO
                }
            }
        }

        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (!hsBlock.isEmpty()){
            ElementStruct elem = (ElementStruct) hsBlock.get(Integer.valueOf(1));
            List lst = (List)this.getHsConfigElement().get(elem.getIdElementStruct());
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    ElementStruct elBlock = (ElementStruct) it.next();
                    sld.setAttributeNS(null, elBlock.getTypeConfig(), elBlock.getConfigValue());
                }
            }
        }
        //TABLE
        elTable=mainDoc.createElementNS(foNS, "fo:table");
        sld.appendChild(elTable);
        if (lstConfig!=null){
            Iterator it = lstConfig.iterator();
            while (it.hasNext()){
                Config conf =  (Config) it.next();
                elTable.setAttributeNS(null, conf.getTypeConfig(), conf.getConfigValue());
            }
        }
        //AGREGAR LAS COLUMNAS
        //contamos cuantas columnas tenemos
        int numCol = hsHeaderColumn.size();
        if (!hsFirstColumn.isEmpty()){
            ElementStruct elem = (ElementStruct) hsFirstColumn.get(Integer.valueOf(1));
            List lstConfElem = (List) this.getHsElementStruct().get(elem.getIdElementStruct());
            tableColum = createColumn(lstConfElem);
            elTable.appendChild(tableColum);
            numCol = numCol-1;
        }
        //si existe configuracion de la ultima columna se quita una columna normal
        if (!hsLastColumn.isEmpty()){
            numCol = numCol-1;
        }
        //agregar las configuraciones de las columnas normales
        if (!hsColumn.isEmpty()){
            ElementStruct elem = (ElementStruct) hsColumn.get(Integer.valueOf(1));
            List lstConfElem = (List) this.getHsElementStruct().get(elem.getIdElementStruct());
            if (lstConfElem!=null){
                for (int i=0;i<numCol;i++){
                    tableColum = createColumn(lstConfElem);
                    elTable.appendChild(tableColum);
                }
            }
        }
        //agregar la configuracion de la ultima columna
        if (!hsLastColumn.isEmpty()){
            ElementStruct elem = (ElementStruct) hsLastColumn.get(Integer.valueOf(1));
            List lstConfElem = (List) this.getHsElementStruct().get(elem.getIdElementStruct());
            if (lstConfElem!=null){
                for (int i=0;i<numCol;i++){
                    tableColum = createColumn(lstConfElem);
                    elTable.appendChild(tableColum);
                }
            }
        }
        //agregar Body
        tableBody = createBodyTable(hsBodyTable);
        elTable.appendChild(tableBody);
        //agregar filas
        boolean firstR=false;
        boolean lastR=false;
        //agregamos la configuracion de la primera fila
        if (!hsFirstRow.isEmpty()){
            firstR=true;
        }
        if (!hsLastRow.isEmpty()){
            lastR=true;
        }
        //Agregamos los header
        if (!hsHeaderColumn.isEmpty()){
            if (!hsFirstRow.isEmpty()){
                if (!hsFirstCell.isEmpty()){
                    row = createRow(hsFirstRow,
                                    hsFirstCell,
                                    hsHeaderColumn);
                    firstR=false;
                    tableBody.appendChild(row);
                }else{
                    row = createRow(hsFirstRow,
                                    hsCell,
                                    hsHeaderColumn);
                    firstR=false;
                    tableBody.appendChild(row);
                }
            }else{
                if (!hsFirstCell.isEmpty()){
                    row = createRow(hsRow,
                                    hsFirstCell,
                                    hsHeaderColumn);
                    firstR=false;
                    tableBody.appendChild(row);
                }else{
                    row = createRow(hsRow,
                                    hsCell,
                                    hsHeaderColumn);
                    firstR=false;
                    tableBody.appendChild(row);
                }
            }
        }
        int numFil = 0;
        //agregamos las filas
        if (!hsDataTable.isEmpty()){
            HashCampo hsCmpo = (HashCampo) hsDataTable.get(1);
            for (int i=0;i<hsCmpo.getListData().size();i++){
                List lst = (List) hsCmpo.getListData().get(i);
                //estamos en la primera fila
                if ((firstR)&&(numFil==0)){
                    row = createRow(hsFirstRow,
                                    hsCell,
                                    lst);
                    firstR=false;
                //estamos en la ultima fila
                }else if((lastR)&&(numFil==(lst.size()-1))){
                    row = createRow(hsLastRow,
                                    hsCell,
                                    lst);
                    lastR=false;
                //estamos en una fila normal
                }else{
                    row = createRow(hsRow,
                                    hsCell,
                                    lst);
                }
                numFil++;
                tableBody.appendChild(row);
            }
        }
        return sld;
    }

     /**
     * Método que construye una columna
     * @param lstConfig Listado con las configuraciones de la columna
     * @return
     */
    private Element createColumn(List lstConfig){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:table-column");
        if (lstConfig!=null){
            Iterator it = lstConfig.iterator();
            while (it.hasNext()){
                ElementStruct elem = (ElementStruct) it.next();
                sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que construye el cuerpo de una tabla
     * @param lst   Listado con las configuraciones del cuerpo de la tabla
     * @return
     */
    private Element createBodyTable(HashMap hsBodyTable){
        List lst = (List) hsBodyTable.get(Integer.valueOf(1));
        Element sld = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-body");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                ElementStruct elem = (ElementStruct) it.next();
                sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que construye una fila
     * @param lst   Listado con la configuracion de la fila
     * @param lstCell   Listado con la configuracion de las celdas
     * @param datos     Datos que contendran las celdas que componen la fila
     * @return
     */
    private Element createRow(HashMap hsRow, HashMap hsCell, List lstDatos){
        Element sld = null;
        Element cell = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-row");
        //List lstRow = (List) hsRow.get(Integer.valueOf(1));
        Collection lstRow = hsRow.values();
        //List lstCell = (List) hsCell.get(Integer.valueOf(1));
        Collection lstCell =hsCell.values();

        if (lstRow!=null){
            Iterator it = lstRow.iterator();
            while (it.hasNext()){
                ElementStruct elem = (ElementStruct) it.next();
                if ((elem.getTypeConfig()!=null)&&( elem.getConfigValue()!=null)){
                    sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
                }
            }
        }
        //agregar las celdas
        if (lstDatos!=null){
            Iterator it = lstDatos.iterator();
            while (it.hasNext()){
                Campo cmp = (Campo) it.next();
                String texto = cmp.getValor();
                cell = createCell(lstCell, texto);
                sld.appendChild(cell);
            }
        }
        return sld;
    }

    /**
     * Método que construye una fila
     * @param hsRow
     * @param hsCell
     * @param hsDatos
     * @return
     */
    private Element createRow(HashMap hsRow, HashMap hsCell, HashMap hsDatos){
        Element sld = null;
        Element cell = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-row");
        //List lstRow = (List) hsRow.get(Integer.valueOf(1));
        Collection lstRow = hsRow.values();
        //List lstCell = (List) hsCell.get(Integer.valueOf(1));
        Collection lstCell = hsCell.values();

        if (lstRow!=null){
            Iterator it = lstRow.iterator();
            while (it.hasNext()){
                ElementStruct elem = (ElementStruct) it.next();
                if ((elem.getTypeConfig()!=null)&&( elem.getConfigValue()!=null)){
                    sld.setAttributeNS(null, elem.getTypeConfig(), elem.getConfigValue());
                }
            }
        }
        //agregar las celdas
        if (!hsDatos.isEmpty()){
            for (int i=1;i<=hsDatos.size();i++){
                ElementStruct elem = (ElementStruct) hsDatos.get(i);
                cell = createCell(lstCell, elem.getValueElement());
                sld.appendChild(cell);
            }
        }
        return sld;
    }

    /**
     * Método que construye un elemento del tipo Celda
     * @param lst   Listado de configuraciones de la celda
     * @param dato  Dato que contendra la celda
     * @return
     */
    private Element createCell(List lst, String dato){
        Element sld = null;
        Element elBlock = null;
        Text elementText = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-cell");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
//                sld.setAttributeNS(null,conf.getName(), conf.getConfig());
                sld.setAttributeNS(null,conf.getTypeConfig(), conf.getConfigValue());

            }
        }
        elBlock=mainDoc.createElementNS(foNS, "fo:block");
        sld.appendChild(elBlock);
        elementText=mainDoc.createTextNode(dato);
        elBlock.appendChild(elementText);
        return sld;
    }

    private Element createCell(Collection lst, String dato){
        Element sld = null;
        Element elBlock = null;
        Text elementText = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-cell");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
//                sld.setAttributeNS(null,conf.getName(), conf.getConfig());
                if ((conf.getTypeConfig()!=null)&&(conf.getConfigValue()!=null)){
                    sld.setAttributeNS(null,conf.getTypeConfig(), conf.getConfigValue());
                }

            }
        }
        elBlock=mainDoc.createElementNS(foNS, "fo:block");
        sld.appendChild(elBlock);
        elementText=mainDoc.createTextNode(dato);
        elBlock.appendChild(elementText);
        return sld;
    }

     /**
     * Método que construye el Layout de la página (bordes)
     * @param lst   Listado con la configuración del layout
     * @return
     */
    private Element createLayoutConf(List lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:layout-master-set");
        if (lst!=null){
            Iterator it = lst.iterator();
            sld.setAttributeNS(null, "master-name", "letter");
            while (it.hasNext()){
                Config elem = (Config) it.next();
                sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que construye la página
     * @param lst   Listado con la configuración de la página
     * @return
     */
    private Element createPageMasterConf(List lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:simple-page-master");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config elem = (Config) it.next();
                sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que construye el body de una región
     * @param lst Listado con la configuración de la región
     * @return
     */
    private Element createRegionBodyConf(List lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:region-body");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config elem = (Config) it.next();
                sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que construye el secuenciador de página
     * @param lst   Listado con la configuración del secuenciador
     * @return
     */
    private Element createPageSequenceConf(List lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:page-sequence");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config elem = (Config) it.next();
                sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que contruye un Flujo. Este puede contener textos, tablas,
     * imagenes, etc
     * @param lst   Listado con la configuración del flujo
     * @return
     */
    private Element createFlowConf(List lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:flow");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config elem = (Config) it.next();
                sld.setAttributeNS(null,elem.getTypeConfig(), elem.getConfigValue());
            }
        }
        return sld;
    }

    /**
     * Método que crea y genera un reporte a partir de los elementos entregados
     */
    public void genReportWithElement() throws ExceptionHandler{
        List lst = getLstElementReport();
        AdmReport adm = new AdmReport();
        adm.setReport(this.getReport());
//-- Crear el reporte de manera normal, obteniendo el XREP
        Integer idRep = adm.addReport();
//-- Obtener el MAX orden de las estructuras
        Integer maxOrden = adm.getMaxOrderReport(idRep);
//-- Por cada estructura seleccionada STold
//  -- crear la nueva estructura STnew, el orden es igual MAX=MAX+1
//  -- Guardar la relacion STnew-STold
        List lstRelStruct = new ArrayList();
        if (lst!=null){
            Iterator it = lst.iterator();
            maxOrden++;
            while (it.hasNext()){
                Integer STold = (Integer) it.next();
                Integer STnew = adm.copyStructure(idRep,STold,maxOrden++);
                Integer[] rel = new Integer[2];
                rel[0] = STnew;
                rel[1] = STold;
                lstRelStruct.add(rel);
            }
        }
//-- Por cada una de las nuevas estructuras STnew, buscar la estructura antigua STold
//-- y copiar su configuracion de estructura
        if (!lstRelStruct.isEmpty()){
            Iterator it = lstRelStruct.iterator();
            while (it.hasNext()){
                Integer[] rel = (Integer[]) it.next();
                adm.copyConfigStructure(rel[0], rel[1]);
            }
        }
//-- Por cada una de las estructuras STnew crear los mismos elementos
//-- que posee la estructura STold
//  -- buscar los elementos de la estructura antigua
        List lstRelElem = new ArrayList();
        if (!lstRelStruct.isEmpty()){
            Iterator it = lstRelStruct.iterator();
            while (it.hasNext()){
                Integer[] rel = (Integer[]) it.next();
                List lstElem = adm.getListElementByIdStruct(rel[1]);
//  -- por cada uno de los elementos ELold, Insertar un nuevo elemento ELnew
//  -- Guardar la relación ELnew-ELold
                if (lstElem!=null){
                    Iterator itEl = lstElem.iterator();
                    while (itEl.hasNext()){
                        ElementStruct elem = (ElementStruct) itEl.next();
                        Integer ELnew = adm.copyElementStructure(rel[0], elem.getIdElementStruct());
                        Integer[] relElem = new Integer[2];
                        relElem[0]= ELnew;
                        relElem[1]= elem.getIdElementStruct();
                        lstRelElem.add(relElem);
                    }
                }
            }
        }
//-- Por cada uno de los nuevos elementos ELnew, buscar el elemento antiguo ELold
//-- y copiar su configuración
        if (!lstRelElem.isEmpty()){
            Iterator it = lstRelElem.iterator();
            while (it.hasNext()){
                Integer[] relElem = (Integer[]) it.next();
                adm.copyConfigElementStructure(relElem[0],relElem[1]);
            }
        }
        Report reportNew = new Report();
        reportNew.setIdReport(idRep);
        this.setReport(reportNew);
        this.getDocument();
    }
}
