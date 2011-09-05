package mx.ilce.report;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.bean.HashCampo;
import mx.ilce.handler.ExceptionHandler;
import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mx.ilce.bean.Campo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Clase administradora de la generacion de Reportes
 * @author ccatrilef
 */
public class AdmReport {

    protected static String foNS = "http://www.w3.org/1999/XSL/Format";
    private FopFactory fopFactory;
    private Document mainDoc;
    private ArrayList lstLayout=new ArrayList();
    private ArrayList lstPageSequence=new ArrayList();
    private ArrayList lstPageMaster=new ArrayList();
    private ArrayList lstRegionBody=new ArrayList();
    private ArrayList lstFlow=new ArrayList();
    private ArrayList lstTitle=new ArrayList();
    private ArrayList lstSecuence=new ArrayList();
    private ArrayList lstPageTitle=new ArrayList();
    private String pageTitle="";
    private Report report;

    private static String BLOCK = "BLOCK";
    private static String TABLE = "TABLE";
    private static String TITLE = "TITLE";
    private static String IMG = "IMG";

    /**
     * Entrega la configuracion del PAGETITLE
     * @return
     */
    public ArrayList getLstPageTitle() {
        return lstPageTitle;
    }

    /**
     * Asigna la configuracion del PAGETITLE
     * @param lstPageTitle
     */
    public void setLstPageTitle(ArrayList lstPageTitle) {
        this.lstPageTitle = lstPageTitle;
    }

    /**
     * Entrega la configuracion del LAYOUT
     * @return
     */
    private ArrayList getLstLayout() {
        return lstLayout;
    }

    /**
     * Asigna la configuracion del LAYOUT
     * @param lstLayout
     */
    private void setLstLayout(ArrayList lstLayout) {
        this.lstLayout = lstLayout;
    }

    /**
     * Entrega la configuracion del PAGEMASTER
     * @return
     */
    private ArrayList getLstPageMaster() {
        return lstPageMaster;
    }

    /**
     * Asigna la configuracion del PAGEMASTER
     * @param lstPageMaster
     */
    private void setLstPageMaster(ArrayList lstPageMaster) {
        this.lstPageMaster = lstPageMaster;
    }

    /**
     * Entrega la configuracion del PAGESEQUENCE
     * @return
     */
    private ArrayList getLstPageSequence() {
        return lstPageSequence;
    }

    /**
     * Asigna la configuracion del PAGESEQUENCE
     * @param lstPageSequence
     */
    private void setLstPageSequence(ArrayList lstPageSequence) {
        this.lstPageSequence = lstPageSequence;
    }

    /**
     * Entrega la configuracion de la REGIONBODY
     * @return
     */
    private ArrayList getLstRegionBody() {
        return lstRegionBody;
    }

    /**
     * Asigna la configuracion de la REGIONBODY
     * @param lstRegionBody
     */
    private void setLstRegionBody(ArrayList lstRegionBody) {
        this.lstRegionBody = lstRegionBody;
    }

    /**
     * Entrega la configuracion de la SECUENCE
     * @return
     */
    private ArrayList getLstSecuence() {
        return lstSecuence;
    }

    /**
     * Asigna la configuracion de la SECUENCE
     * @param lstSecuence
     */
    private void setLstSecuence(ArrayList lstSecuence) {
        this.lstSecuence = lstSecuence;
    }

    /**
     * Entrega la configuracion del TITLE
     * @return
     */
    private ArrayList getLstTitle() {
        return lstTitle;
    }

    /**
     * Asigna la configuracion del TITLE
     * @param lstTitle
     */
    private void setLstTitle(ArrayList lstTitle) {
        this.lstTitle = lstTitle;
    }

    /**
     * Entrega la configuracion del PAGETITLE
     * @return
     */
    private String getPageTitle() {
        return pageTitle;
    }

    /**
     * Asigna la configuracion del PAGETITLE
     * @param pageTitle
     */
    private void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * Entrega la configuracion del FLOW
     * @return
     */
    private ArrayList getLstFlow() {
        return lstFlow;
    }

    /**
     * Asigna la configuracion del FLOW
     * @param lstFlow
     */
    private void setLstFlow(ArrayList lstFlow) {
        this.lstFlow = lstFlow;
    }

    /**
     * Entrega el objeto REPORT
     * @return
     */
    public Report getReport() {
        return report;
    }

    /**
     * Asigna el objeto REPORT
     * @param report
     */
    public void setReport(Report report) {
        this.report = report;
    }

    /**
     * Metodo para la generacion de un documento a partir del Reporte
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
            System.out.println("FOP ExampleDOM2PDF\n");
            //Setup directories
            File baseDir = new File("c:/log/");
            File outDir = new File(baseDir, "out");
            outDir.mkdirs();
            //Setup output file
            pdffile = new File(outDir, "ResultPDF.pdf");
            System.out.println("PDF Output File: " + pdffile);
            System.out.println();

            getSeccionData();
            //RESULTADO
            foDoc = getDocummentGral();
            convertDOM2PDF(foDoc, pdffile);
            System.out.println("Success!");
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
     * Entrega las configuraciones de las distintas secciones que componen
     * el documento
     * @return
     * @throws ExceptionHandler
     */
    private ArrayList getSeccionData() throws ExceptionHandler{
        ArrayList sld = new ArrayList();
        SectionReport section = new SectionReport();
        //Obtenemos la configuracion general
        HashMap hmConfig = llenaConfigTabla();
        section.setConfigReport(hmConfig);
        //Obtener las secciones asociadas a la configuracion
        HashMap hmSection = getConfigSection();
        //Obtiene la data
        getData(hmSection);

        this.setLstLayout(section.getLayout());
        this.setLstPageMaster(section.getPageMaster());
        this.setLstRegionBody(section.getRegionBody());
        this.setLstPageSequence(section.getPageSecuence());
        this.setLstFlow(section.getFlow());
        this.setLstTitle(section.getTitle());
        this.setLstPageTitle(section.getPageTitle());
        this.setPageTitle(section.getStrPageTitle(hmSection));

        if (!hmSection.isEmpty()){
            for (int i=1;i<=hmSection.size();i++){
                Section sec = (Section) hmSection.get(Integer.valueOf(i));
                List arrDato = new ArrayList();
                String strType = sec.getSequenceType();
                if ((!"LAYOUT".equals(strType)) &&
                    (!"PAGEMASTER".equals(strType)) &&
                    (!"PAGETITLE".equals(strType)) &&
                    (!"REGIONBODY".equals(strType)) &&
                    (!"PAGESEQUENCE".equals(strType)) &&
                    (!"FLOW".equals(strType)) )
                {
                    if ("TITLE".equals(strType)){
                        String strTitle = sec.getTextValue();
                        Element elTitle = this.createTitle(arrDato,strTitle);
                        this.addSeccion(AdmReport.TITLE, elTitle);
                    }else if ("TABLE".equals(strType)){
                        Element table = this.createTable(sec);
                        this.addSeccion(AdmReport.TABLE, table);
                    }else if ("IMG".equals(strType)){
                        //this.addSeccion(AdmReport.IMG, table);
                    }else if ("BLOCK".equals(strType)){
                        String strTexto = sec.getTextValue();
                        Element elBlock = createBlock(arrDato, strTexto);
                        this.addSeccion(AdmReport.BLOCK, elBlock);
                    }
                }
            }
        }
        return sld;
    }

    /**
     * Obtiene la data que contiene una seccion. Esta data se obtiene a partir
     * de las queries que poseen las secciones. SI una seccon no posee una query
     * no realiza ninguna accion
     * @param hsSection
     * @return
     * @throws ExceptionHandler
     */
    private HashMap getData(HashMap hsSection) throws ExceptionHandler{
        HashMap hsMap = new HashMap();

        if (!hsSection.isEmpty()){
            ConReport con = new ConReport();
            for (int i=1;i<=hsSection.size();i++){
                Section sec = (Section) hsSection.get(i);
                String query = sec.getStrQuery();
                if ((query!=null)&&(!"".equals(query))){
                    HashCampo hsCampo = con.getDataWithWhereAndData(query,
                                                        this.getReport().getStrWhere(),
                                                        this.getReport().getArrData(),
                                                        this.getReport().getArrVariables());
                    sec.setHsCampoData(hsCampo);
                }
                hsMap.put(i,sec);
            }
        }
        return hsMap;
    }

    /**
     * Entrega la configuracion general que posee el reporte
     * @return
     * @throws ExceptionHandler
     */
    private HashMap llenaConfigTabla() throws ExceptionHandler{

        HashMap hsMap = new HashMap();
        String query = "select cr.id_configReport as idconfig, "
                + " ct.id_configType as idconfigtype, "
                + " cr.id_sequence as idsequence, "
                + " ct.name"
                + " from RP_ConfigReport cr, RP_ConfigType ct"
                + " where cr.id_configType = ct.id_configType"
                + " and   cr.id_reporte = " + this.getReport().getIdReport() 
                + " order by cr.id_order";

        ConReport con = new ConReport();
        List listData = con.getConfigReport(query);

        String queryConf = "select cr.id_configReport , ctd.name , cd.config"
                + " from RP_ConfigReport cr"
                + " , RP_ConfigType ct"
                + " , RP_ConfigTypeData ctd"
                + " , RP_ConfigData cd"
                + " where cr.id_configType   = ct.id_configType"
                + " and   cr.id_configReport = cd.id_configReport"
                + " and   cd.id_configTypeData = ctd.id_configTypeData"
                + " and   cr.id_reporte = " + this.getReport().getIdReport()
                + " and   cr.id_configReport = ";

        if (!listData.isEmpty()){
            Iterator it = listData.iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
                String queryPas = queryConf + conf.getIdConfig();
                List listConf = con.getListConfig(queryPas);
                conf.setListConfig(listConf);
                hsMap.put(conf.getName(), conf);
            }
        }
        return hsMap;
    }

    /**
     * Obtiene las distintas configuraciones que poseen las secciones que
     * integran el reporte
     * @return
     * @throws ExceptionHandler
     */
    private HashMap getConfigSection() throws ExceptionHandler{
        HashMap hsMap = new HashMap();
        //obtenemos las secciones
        String query = "select distinct "
                + " sec.id_sequence, "
                + " sec.id_order, "
                + " sec.id_query, "
                + " sect.sequenceType, "
                + " sec.textValue "
                + " from RP_ConfigReport cr, RP_Sequence sec"
                + " , RP_SequenceType sect, RP_ConfigData cd"
                + " where cr.id_reporte = " + this.getReport().getIdReport()
                + " and cr.id_sequence = sec.id_sequence"
                + " and sec.id_sequenceType = sect.id_sequenceType"
                + " and cr.id_configReport = cd.id_configReport"
                + " order by sec.id_order";
        ConReport con = new ConReport();
        List listData = con.getListSection(query);
        if (!listData.isEmpty()){
            Iterator it = listData.iterator();
            int pos = 1;
            while (it.hasNext()){
                Section sec = (Section) it.next();
                //obtenemos las queries
                if (!sec.getIdQuery().equals(Integer.valueOf(0))){
                    String queryPas = "select qr.id_query, qr.query"
                            + " from RP_query qr"
                            + " where qr.id_query = " + sec.getIdQuery();
                    String strSld = con.getQueryByID(queryPas);
                    sec.setStrQuery(strSld);
                }
                //obtenemos los header de las columnas
                String queryPas = "select sh.id_sequence , sh.textValue "
                        + " from RP_SequenceHeader sh"
                        + " where sh.id_sequence = " + sec.getIdSequence()
                        + " order by sh.id_order";
                List lstPas = con.getListHeader(queryPas);
                sec.setListHeader(lstPas);
                //obtenemos la existencia de la configuracion de las secciones
                queryPas = "select (select COUNT(*) from RP_SequenceConfigAdic as SCA "
                        + "    where sca.id_sequenceConfig = sc.id_sequenceConfig) as adic, "
                        + " (select COUNT(SCA.id_configReportFirst) from RP_SequenceConfigAdic as SCA "
                        + "    where sca.id_sequenceConfig = sc.id_sequenceConfig) as adicF, "
                        + " (select COUNT(SCA.id_configReportLast) from RP_SequenceConfigAdic as SCA "
                        + "    where sca.id_sequenceConfig = sc.id_sequenceConfig) as adicL, "
                        + " (select count(*) from RP_ConfigData as cd "
                        + "    where cd.id_configReport = cr.id_configReport) as conf,"
                        + " ct.name, cr.id_configReport, sc.id_sequenceConfig "
                        + " from RP_SequenceConfig sc, RP_ConfigReport cr, RP_ConfigType ct"
                        + " where sc.id_configReport = cr.id_configReport"
                        + " and cr.id_configType = ct.id_configType"
                        + " and sc.id_sequence = " + sec.getIdSequence()
                        + " order by sc.id_sequence ";
                List lstConf = con.getListConfigSection(queryPas);
                if (!lstConf.isEmpty()) {
                    Iterator itConf = lstConf.iterator();
                    while (itConf.hasNext()){
                        Config conf = (Config) itConf.next();
                        if (conf.getNumAdic()>0){
                            if (conf.getNumAdicFirst()>0){
                                String queryF = " select cd.id_configReport as idConfig "
                                        + ", ctd.name , cd.config "
                                        + " from RP_SequenceConfigAdic as sca "
                                        + ", RP_ConfigData cd , RP_ConfigTypeData ctd "
                                        + " where sca.id_sequenceConfig = " + conf.getIdSequenceConfig()
                                        + " and sca.id_configReportFirst = cd.id_configReport "
                                        + " and   cd.id_configTypeData = ctd.id_configTypeData";

                                List lstF = con.getListConfig(queryF);
                                if (conf.getName().toUpperCase().equals("COLUMN")){
                                    sec.setListConfigFirstColumn(lstF);
                                }else if (conf.getName().toUpperCase().equals("ROWTABLE")){
                                    sec.setListConfigFirstRow(lstF);
                                }else if (conf.getName().toUpperCase().equals("CELLTABLE")){
                                    sec.setListConfigFirstCell(lstF);
                                }
                            }
                            if (conf.getNumAdicLast()>0){
                                String queryF = " select cd.id_configReport as idConfig "
                                        + ", ctd.name , cd.config "
                                        + " from RP_SequenceConfigAdic as sca "
                                        + ", RP_ConfigData cd , RP_ConfigTypeData ctd "
                                        + " where sca.id_sequenceConfig = " + conf.getIdSequenceConfig()
                                        + " and sca.id_configReportLast = cd.id_configReport "
                                        + " and   cd.id_configTypeData = ctd.id_configTypeData";

                                List lstF = con.getListConfig(queryF);
                                if (conf.getName().toUpperCase().equals("COLUMN")){
                                    sec.setListConfigLastColumn(lstF);
                                }else if (conf.getName().toUpperCase().equals("ROWTABLE")){
                                    sec.setListConfigLastRow(lstF);
                                }else if (conf.getName().toUpperCase().equals("CELLTABLE")){
                                    sec.setListConfigLastCell(lstF);
                                }
                            }
                        }
                        if (conf.getNumConf()>0){
                            String queryC = "select cd.id_configReport as idConfig"
                                    + ", ctd.name , cd.config from RP_ConfigData as cd "
                                    + ", RP_ConfigTypeData ctd "
                                    + " where cd.id_configReport = " + conf.getIdConfig()
                                    + " and   cd.id_configTypeData = ctd.id_configTypeData";

                            List lstC = con.getListConfig(queryC);
                            if (conf.getName().toUpperCase().equals("BLOCK")){
                                sec.setListConfigBlock(lstC);
                            }else if (conf.getName().toUpperCase().equals("TABLE")){
                                sec.setListConfigTable(lstC);
                            }else if (conf.getName().toUpperCase().equals("COLUMN")){
                                sec.setListConfigColumn(lstC);
                            }else if (conf.getName().toUpperCase().equals("BODYTABLE")){
                                sec.setListConfigBodyTable(lstC);
                            }else if (conf.getName().toUpperCase().equals("ROWTABLE")){
                                sec.setListConfigRow(lstC);
                            }else if (conf.getName().toUpperCase().equals("CELLTABLE")){
                                sec.setListConfigCell(lstC);
                            }
                        }
                    }
                }
                hsMap.put(pos++,sec);
            }
        }
        return hsMap;
    }

    /**
     * Metodo encargado de agregar una seccion a la configuracion del documento
     * @param typeData
     * @param obj
     */
    private void addSeccion(String typeData, Object obj){
        Seccion seccion = new Seccion();
        seccion.setTipo(typeData);
        seccion.setData(obj);
        this.getLstSecuence().add(seccion);
    }

    /**
     * Metodo que se encarga de generar el documento a partir de las secciones
     * configuradas
     * @return
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
        if (lstLayout!=null){
            Element elLayout = createLayout(this.getLstLayout());
            root.appendChild(elLayout);
            //PAGEMASTER
            if (lstPageMaster!=null){
                Element pageMaster = createPageMaster(this.getLstPageMaster());
                elLayout.appendChild(pageMaster);
                //REGION BODY
                if (lstRegionBody!=null){
                    Element regionBody = createRegionBody(this.getLstRegionBody());
                    pageMaster.appendChild(regionBody);
                }
            }
        }
        if (lstPageSequence!=null){
            //PAGESEQUENCE
            Element pageSequence = createPageSequence(this.getLstPageSequence());
            root.appendChild(pageSequence);
            //FLOW
            if (lstFlow!=null){
                Element flow = createFlow(this.getLstFlow());
                pageSequence.appendChild(flow);
                if (lstTitle!=null){
                    //PAGE TITLE
                    String texto = getPageTitle();
                    Element title = createTitle(this.getLstTitle(), texto);
                    flow.appendChild(title);
                }
                //listado con datos
                //BLOCK
                if (lstSecuence!=null){
                    Iterator it = lstSecuence.iterator();
                    while (it.hasNext()){
                        Seccion sec = (Seccion) it.next();
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
     * Generador de un archivo PDF a partir de la configuracion de un documento
     * @param xslfoDoc  Configuracion del documento
     * @param pdf   Archivo PDF que debe generarse
     */
    private void convertDOM2PDF(Document xslfoDoc, File pdf) {
        try {
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Setup output
            OutputStream out = new java.io.FileOutputStream(pdf);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format and output stream
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                // Setup Identity Transformer
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(); // identity transformer
                // Setup input for XSLT transformation
                Source src = new DOMSource(xslfoDoc);
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } finally {
            out.close();
            }
            }catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    /**
     * Metodo para la construccion de la Layout de la pagina (bordes)
     * @param lst   Listado con la configuracion del layout
     * @return
     */
    private Element createLayout(ArrayList lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:layout-master-set");
        if (lst!=null){
            Iterator it = lst.iterator();
            sld.setAttributeNS(null, "master-name", "letter");
            while (it.hasNext()){
                String[] data = (String[]) it.next();
                sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        return sld;
    }

    /**
     * Metodo para la construccion de la pagina
     * @param lst   Listado con la configuracion de la pagina
     * @return
     */
    private Element createPageMaster(ArrayList lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:simple-page-master");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                String[] data = (String[]) it.next();
                sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        return sld;
    }

    /**
     * Metodo para la construccion del body de una region
     * @param lst Listado con la configuracion de la region
     * @return
     */
    private Element createRegionBody(ArrayList lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:region-body");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                String[] data = (String[]) it.next();
                sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        return sld;
    }

    /**
     * Metodo para la construccion del secuenciador de página
     * @param lst   Listado con la configuracion del secuenciador
     * @return
     */
    private Element createPageSequence(ArrayList lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:page-sequence");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                String[] data = (String[]) it.next();
                sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        return sld;
    }

    /**
     * Metodo para contruir un Flujo. Este puede contener textos, tablas,
     * imagenes, etc
     * @param lst   Listado con la configuracion del flujo
     * @return
     */
    private Element createFlow(ArrayList lst){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:flow");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                String[] data = (String[]) it.next();
                sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        return sld;
    }

    /**
     * Metodo para construir un Titulo
     * @param lst   Listado con la configuracion del titulo
     * @param texto Texto que contendra el titulo
     * @return
     */
    private Element createTitle(List lst, String texto){
        Element sld = null;
        Text elementText = null;
        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (lst!=null){
            if (!lst.isEmpty()){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    String[] data = (String[]) it.next();
                    sld.setAttributeNS(null, data[0] , data[1]);
                }
            }
        }
        elementText = mainDoc.createTextNode(texto);
        sld.appendChild(elementText);
        return sld;
    }

    private Element createBlock(List lst, String texto){
        Element sld = null;
        Text elementText = null;
        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (lst!=null){
            if (!lst.isEmpty()){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    String[] data = (String[]) it.next();
                    sld.setAttributeNS(null, data[0] , data[1]);
                }
            }
        }
        elementText = mainDoc.createTextNode(texto);
        sld.appendChild(elementText);
        return sld;
    }

    /**
     * Metodo para construir una tabla
     * @return
     */
    private Element createTable(Section section){
        Element sld = null;

        Element elTable = null;
        Element tableBody = null;
        Element tableColum = null;
        Element row = null;

        sld = mainDoc.createElementNS(foNS, "fo:block");
        if (section.getListConfigBlock()!=null){
            Iterator it = section.getListConfigBlock().iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
                sld.setAttributeNS(null, conf.getName(), conf.getConfig());
            }
        }
        //TABLE
        elTable=mainDoc.createElementNS(foNS, "fo:table");
        sld.appendChild(elTable);
        if (section.getListConfigTable()!=null){
            Iterator it = section.getListConfigTable().iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
                elTable.setAttributeNS(null, conf.getName(), conf.getConfig());
            }
        }
        //AGREGAR LAS COLUMNAS
        //contamos cuantas columnas tenemos
        int numCol = section.getHsCampoData().getLengthCampo();
        //Agregamos la configuracion de la primera columna
        if (section.getListConfigFirstColumn()!=null){
            Iterator it = section.getListConfigFirstColumn().iterator();
            if (it.hasNext()){
                Config conf = (Config) it.next();
                tableColum = createColumn(conf);
                elTable.appendChild(tableColum);
                numCol = numCol-1;
            }
        }
        //si existe configuracion de la ultima columna se quita una columna normal
        if (section.getListConfigLastColumn()!=null){
            Iterator it = section.getListConfigLastColumn().iterator();
            if (it.hasNext()){
                numCol = numCol-1;
            }
        }
        //agregar las configuraciones de las columnas normales
        if (section.getListConfigColumn()!=null){
            Iterator it = section.getListConfigColumn().iterator();
            if (it.hasNext()){
                Config conf = (Config) it.next();
                for (int i=0;i<numCol;i++){
                    tableColum = createColumn(conf);
                    elTable.appendChild(tableColum);
                }
            }
        }
        //agregar la configuracion de la ultima columna
        if (section.getListConfigLastColumn()!=null){
            Iterator it = section.getListConfigLastColumn().iterator();
            if (it.hasNext()){
                Config conf = (Config)it.next();
                tableColum = createColumn(conf);
                elTable.appendChild(tableColum);
            }
        }
        //agregar Body
        tableBody = createBodyTable(section.getListConfigBodyTable());
        elTable.appendChild(tableBody);
        //agregar filas
        boolean firstR=false;
        boolean lastR=false;
        //agregamos la configuracion de la primera fila
        if (section.getListConfigFirstRow()!=null){
            Iterator it = section.getListConfigFirstRow().iterator();
            if (it.hasNext()){
                firstR=true;
            }
        }
        //marcamos si existe la configuracion de la ultima fila
        if (section.getListConfigLastRow()!=null){
            Iterator it = section.getListConfigLastRow().iterator();
            if (it.hasNext()){
                lastR=true;
            }
        }
        //Agregamos los header
        if (section.getListHeader()!=null){
            ArrayList lst = new ArrayList();
            Iterator it = section.getListHeader().iterator();
            while (it.hasNext()){
                Section sec = (Section) it.next();
                Campo cmp = new Campo();
                cmp.setValor(sec.getTextValue());
                lst.add(cmp);
            }
            if (section.getListConfigFirstRow()!=null){
                if (section.getListConfigFirstCell()!=null){
                    row = createRow(section.getListConfigFirstRow(),
                                    section.getListConfigFirstCell(),
                                    lst);
                    firstR=false;
                    tableBody.appendChild(row);
                }else{
                    row = createRow(section.getListConfigFirstRow(),
                                    section.getListConfigCell(),
                                    lst);
                    firstR=false;
                    tableBody.appendChild(row);
                }
            }else{
                if (section.getListConfigFirstCell()!=null){
                    row = createRow(section.getListConfigRow(),
                                    section.getListConfigFirstCell(),
                                    lst);
                    firstR=false;
                    tableBody.appendChild(row);
                }else{
                    row = createRow(section.getListConfigRow(),
                                    section.getListConfigCell(),
                                    lst);
                    firstR=false;
                    tableBody.appendChild(row);
                }
            }
        }
        int numFil = 0;
        //agregamos las filas
        if (section.getHsCampoData().getListData()!=null){
            HashMap hsData = section.getHsCampoData().getListData();
            for (int i=0; i<hsData.size();i++){
                ArrayList lst = (ArrayList) hsData.get(Integer.valueOf(i));
                //estamos en la primera fila
                if ((firstR)&&(numFil==0)){
                    row = createRow(section.getListConfigFirstRow(),
                                    section.getListConfigCell(),
                                    lst);
                    firstR=false;
                //estamos en la ultima fila
                }else if((lastR)&&(numFil==(lst.size()-1))){
                    row = createRow(section.getListConfigLastRow(),
                                    section.getListConfigCell(),
                                    lst);
                    lastR=false;
                //estamos en una fila normal
                }else{
                    row = createRow(section.getListConfigRow(),
                                    section.getListConfigCell(),
                                    lst);
                }
                numFil++;
                tableBody.appendChild(row);
            }
        }
        return sld;
    }

     /**
     * Metodo para construir una columna
     * @param lst Listado con las configuraciones de la columna
     * @return
     */
    private Element createColumn(Config data){
        Element sld = null;
        sld = mainDoc.createElementNS(foNS, "fo:table-column");
        sld.setAttributeNS(null, data.getName(), data.getConfig());
        return sld;
    }

    /**
     * Metodo para construir el cuerpo de una tabla
     * @param lst   Listado con las configuraciones del cuerpo de la tabla
     * @return
     */
    private Element createBodyTable(List lst){
        Element sld = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-body");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Config conf = (Config) it.next();
                sld.setAttributeNS(null, conf.getName(), conf.getConfig());
            }
        }
        return sld;
    }

    /**
     * Metodo para construir una fila
     * @param lst   Listado con la configuracion de la fila
     * @param lstCell   Listado con la configuracion de las celdas
     * @param datos     Datos que contendran las celdas que componen la fila
     * @return
     */
    private Element createRow(List lst, List lstCell, ArrayList datos){
        Element sld = null;
        Element cell = null;
        sld=mainDoc.createElementNS(foNS, "fo:table-row");
        if (lst!=null){
            Iterator it = lst.iterator();
            while (it.hasNext()){
                Campo data = (Campo) it.next();
                sld.setAttributeNS(null, data.getNombre(), data.getValor());
            }
        }
        //agregar las celdas
        if (datos!=null){
            Iterator it = datos.iterator();
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
     * Metodo para construir un elemento del tipo Celda
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
                sld.setAttributeNS(null,conf.getName(), conf.getConfig());
                //String[] data = (String[]) it.next();
                //sld.setAttributeNS(null, data[0], data[1]);
            }
        }
        elBlock=mainDoc.createElementNS(foNS, "fo:block");
        sld.appendChild(elBlock);
        elementText=mainDoc.createTextNode(dato);
        elBlock.appendChild(elementText);
        return sld;
    }

//----------------------------------------------------------------------

    /**
     * Clase interna implementada para contener secciones que componen
     * un documento
     */
    class Seccion implements Serializable{
        private String tipo;
        private Object data;

        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
        public String getTipo() {
            return tipo;
        }
        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    /**
     * Clase implementada para contener las configuraciones de las secciones
     * del documento
     */
    class TableConfig{
        ArrayList lstBlock=new ArrayList();
        ArrayList lstTable=new ArrayList();
        ArrayList lstColum=new ArrayList();
        ArrayList lstBody=new ArrayList();
        ArrayList lstRow=new ArrayList();
        ArrayList lstCell=new ArrayList();
        ArrayList lstDatos=null;

        public TableConfig() {

        }
        public ArrayList getLstBlock() {
            return lstBlock;
        }
        public void setLstBlock(ArrayList lstBlock) {
            this.lstBlock = lstBlock;
        }
        public ArrayList getLstTable() {
            return lstTable;
        }
        public void setLstTable(ArrayList lstTable) {
            this.lstTable = lstTable;
        }
        public ArrayList getLstColum() {
            return lstColum;
        }
        public void setLstColum(ArrayList lstColum) {
            this.lstColum = lstColum;
        }
        public ArrayList getLstBody() {
            return lstBody;
        }
        public void setLstBody(ArrayList lstBody) {
            this.lstBody = lstBody;
        }
        public ArrayList getLstRow() {
            return lstRow;
        }
        public void setLstRow(ArrayList lstRow) {
            this.lstRow = lstRow;
        }
        public ArrayList getLstCell() {
            return lstCell;
        }
        public void setLstCell(ArrayList lstCell) {
            this.lstCell = lstCell;
        }
        public ArrayList getLstDatos() {
            return lstDatos;
        }
        public void setLstDatos(ArrayList lstDatos) {
            this.lstDatos = lstDatos;
        }
    }
}
