package mx.ilce.component;

import java.sql.SQLException;
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
    private boolean deleteIncreement=false;

    
    public boolean isDeleteIncreement() {
        return deleteIncreement;
    }

    public void setDeleteIncreement(boolean deleteIncreement) {
        this.deleteIncreement = deleteIncreement;
    }



    /**
     * Metodo que permite obtener la data del usuario en el formato del
     * archivo XML definido para la Session y debe completarse con los datos
     * obtenidos desde la Base de Datos
     * @param user  Bean con los datos del usuario que se va a buscar, debe
     * contener la clave de empleado del usuario
     * @return
     * @throws SQLException
     */
    public StringBuffer getSessionXML(User user) throws SQLException{
        ConSession con = new ConSession();

        StringBuffer str = new StringBuffer("");
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

        HashCampo hsCmp = con.getUserXML(user);
        Document document = getDocumentXML("widget.session.xml");
        str.append(listNode(document,0,hsCmp));

        return str;
    }

    /**
     * Obtiene el menu en formato XML que le corresponde al usuario conectado
     * segun su perfil
     * @param user
     * @return
     */
    public StringBuffer getMenuXML(User user){
        ConSession con = new ConSession();

        StringBuffer str = new StringBuffer("");
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

        HashCampo hsCmp = con.getMenuXML(user);

        Document document = getDocumentXML("widget.accordion.xml");
        str.append("<qry>");
        for (int i=0;i<hsCmp.getLengthData();i++){
            str.append(listNode(document,0,hsCmp,i));
        }
        str.append("</qry>");

        return str;
    }

    /**
     * Entrega los tab en formato XML que le corresponden segun el perfil entregado
     * @param perfil
     * @return
     */
    public StringBuffer getTabXML(Perfil perfil){
        StringBuffer str = new StringBuffer("");
        ConSession con = new ConSession();
        
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        HashCampo hsCmp = con.getTabForma(perfil);

        Document document = getDocumentXML("widget.tabs.xml");
        str.append("<qry>");
        for (int i=0;i<hsCmp.getLengthData();i++){
            str.append(listNode(document,0,hsCmp,i));
        }
        str.append("</qry>");

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
     */
    public StringBuffer getGridByData(HashCampo hsData, List lstCampos, int page, int regByPage){
        StringBuffer str = new StringBuffer();

        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
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
            if (this.isDeleteIncreement()){
                if (cmp.getIsIncrement()){
                    seguir = false;
                }
            }
            if (seguir){
                str.append(("<"+cmp.getNombreDB()+">\n"));
                if (cmp.getNombreDB()!=null){
                    CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                    if (cmpAux!=null){
                        if (cmpAux.getAliasCampo()!=null){
                            str.append(("\t<alias_campo><![CDATA["
                                    + replaceAccent(castNULL(cmpAux.getAliasCampo().trim()))
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
        for(int i=regIni;i<hsDat.size();i++){
            ArrayList arr = (ArrayList) hsDat.get(Integer.valueOf(i));
            str.append(("<row id='"+String.valueOf(i+1)+"'>\n"));
            for (int j=0; j<lstCmp.size();j++){
                cmp = (Campo) arr.get(j) ;
                if (!this.isDeleteIncreement() && !cmp.getIsIncrement()){
                    str.append("\t<cell>");
                    str.append(castNULL(String.valueOf(cmp.getValor()).trim()));
                    str.append("</cell>\n");
                }
            }
            str.append("</row>\n");
        }
        str.append("</rows>");
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
     */
    public StringBuffer getGridColumByData(HashCampo hsData, List lstCampos){
        StringBuffer str = new StringBuffer();

        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        Campo cmp = new Campo();
        List lstCmp = hsData.getListCampos();
        HashMap hsDat = hsData.getListData();

        int reg = hsDat.size();
        str.append("<rows>\n");
        str.append("<column_definition>\n");
        for(int i=0; i<lstCmp.size();i++){
            cmp = (Campo) lstCmp.get(i) ;
            str.append(("<"+cmp.getNombreDB()+">\n"));
            if (cmp.getNombreDB()!=null){
                CampoForma cmpAux = getCampoForma(lstCampos,cmp.getNombreDB());
                if (cmpAux!=null){
                    if (cmpAux.getAliasCampo()!=null){
                        str.append(("\t<alias_campo><![CDATA["
                                +replaceAccent(castNULL(cmpAux.getAliasCampo().trim()))
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
        str.append("</rows>");
        return str;
    }

    /**
     * Entrega un XML en base a la Forma indicada y con los datos que se le
     * entregan
     * @param hsData    Data entregada
     * @param lstCampos Listado de campos de la Forma
     * @param idForma   ID de la Forma entregada
     * @return
     */
    public StringBuffer getFormaByData(HashCampo hsData, List lstCampos, Integer idForma, String tipoAccion){
        StringBuffer str = new StringBuffer();

        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
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
                if (this.isDeleteIncreement()){
                    if (cmp.getIsIncrement()){
                        seguir = false;
                    }
                }
                if (seguir){
                    str.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                     + castTypeJavaToXML(cmp.getTypeDataAPL()) +"\">"));
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
                                if ("SELECT".equals(tipoAccion.toUpperCase())){
                                    str.append(" agrega_registro=\"false\"");
                                }else{
                                    str.append(" agrega_registro=\"true\"");
                                }
                                str.append((" clave_forma=\""+idForma+"\">\n"));
                                String[] strData = new String[2];
                                strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                strData[1] = String.valueOf(cmpAux.getFiltroForaneo());
                                StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB());
                                if (!"".equals(strForaneo.toString())){
                                    str.append(("\t\t\t<qry_"+cmp.getNombreDB()));
                                    str.append((" source=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    str.append(strForaneo);
                                    str.append(("\t\t\t</qry_"+cmp.getNombreDB()+">\n"));
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
        return str;
    }

    /**
     * Entrega un XML en base a la Forma indicada y con la estructura de los 
     * datos, pero sin datos
     * @param hsData
     * @param lstCampos
     * @param idForma
     * @return
     */
    public StringBuffer getFormaWithoutData(HashCampo hsData, List lstCampos, Integer idForma, String tipoAccion){
        StringBuffer str = new StringBuffer();

        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
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
                if (this.isDeleteIncreement()){
                    if (cmp.getIsIncrement()){
                        seguir = false;
                    }
                }
                if (seguir){
                    str.append(("\t<"+ cmp.getNombreDB() + " tipo_dato=\""
                                     + castTypeJavaToXML(cmp.getTypeDataAPL()) +"\">"));
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
                                if ("SELECT".equals(tipoAccion.toUpperCase())){
                                    str.append(" agrega_registro=\"false\"");
                                }else{
                                    str.append(" agrega_registro=\"true\"");
                                }
                                str.append((" clave_forma=\""+idForma+"\">\n"));
                                String[] strData = new String[2];
                                strData[0] = String.valueOf(cmpAux.getClaveFormaForanea());
                                //strData[1] = String.valueOf(cmpAux.getFiltroForaneo());
                                StringBuffer strForaneo = getXmlByIdForma(strData, cmp.getNombreDB());
                                if (!"".equals(strForaneo.toString())){
                                    str.append(("\t\t\t<qry_"+cmp.getNombreDB()));
                                    str.append((" source=\""+String.valueOf(cmpAux.getClaveFormaForanea()).trim()+"\">\n"));
                                    str.append(strForaneo);
                                    str.append(("\t\t\t</qry_"+cmp.getNombreDB()+">\n"));
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
     */
    private StringBuffer getXmlByQueryAndData(String query, String[] strData, String strRegistro){
        ConEntidad con = new ConEntidad();
        HashCampo hsData = con.getDataByQuery(query, strData);
        List lstCmp = hsData.getListCampos();

        HashMap hsDat = hsData.getListData();

        StringBuffer str = new StringBuffer("");
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
        return str;
    }

    /**
     * Metodo que permite crear la seccion de un XML a partir de la forma que
     * se esta entregando en la data, con ello se obtiene el XML respectivo
     * @param strData   Data de entrada que se usara en la query
     * @param strRegistro   Nombre del registro desde donde se invoco el metodo
     * @return
     */
    private StringBuffer getXmlByIdForma(String[] strData, String strRegistro){
        ConEntidad con = new ConEntidad();
        HashCampo hsData = new HashCampo();
        try{
            //obtenemos la query de la forma entregada
            String[] strDataQ = new String[2];
            strDataQ[0] =strData[0];
            strDataQ[1] ="select";
            HashCampo hsCmpQ = con.getDataByIdQuery(con.getIdQuery(AdminFile.FORMAQUERY), strDataQ);
            Campo cmp = hsCmpQ.getCampoByName("claveconsulta");
            HashMap dq = hsCmpQ.getListData();
                if (!dq.isEmpty()){
                    ArrayList arr = (ArrayList)dq.get(0);
                    Campo cmpAux = (Campo)arr.get(cmp.getCodigo()-1);
                    String[] strDataFiltro = new String[1];
                    strDataFiltro[0] = strData[1];
                    //ejecutamos la query, con el filtro entregado
                    hsData = con.getDataByIdQuery(Integer.valueOf(cmpAux.getValor()), strDataFiltro);
                }
        }catch(Exception e){

        }
        List lstCmp = hsData.getListCampos();
        HashMap hsDat = hsData.getListData();

        StringBuffer str = new StringBuffer("");
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
        return str;
    }

    /**
     * Metodo para obtener la data ordenada para ser utilizada en la query.
     * Se asume que la query esta bien estructurada, es decir, se requiere
     * que los parametros esten en forma secuencial (Ej: %1 %2 %3 ... ) para
     * ubicar en forma correcta los datos
     * @param query
     * @param lst
     * @return
     */
    private String[] getStringData(String query, ArrayList lst){
        String[] strData = null;
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
        return strData;
    }

    /**
     * Obtiene los datos de un campo, obtenidos desde la forma que se le entrega
     * @param lstData   Listado con la configuracion de la forma
     * @param nombreCampo   Campo que se esta buscando desde la forma
     * @return
     */
    private CampoForma getCampoForma(List lstData, String nombreCampo ){
        CampoForma cmp = null;

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
        return cmp;
    }

    /**
     * Obtiene un Documento XML, a partir del nombre solicitado. Se asume que
     * los archivos XML estan en la ruta resource/xml/[nombre archivo]
     * @param fileName
     * @return
     */
    private Document getDocumentXML(String fileName){
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
        }catch(Exception e1){
            e1.printStackTrace();
        }
        return documento;
    }

    /**
     * Recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde segun el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando es un registro unico.
     * @param e
     * @param level
     * @param hsCmp
     * @return
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp){
        StringBuffer str = new StringBuffer("");
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
        return str;
    }

    /**
     * Recorre un archivo XML y va reemplazando los datos del XML por el que le
     * corresponde segun el resultado de la query entregada en el objeto hsCmp.
     * Se utiliza cuando son mas de un registro
     * @param e
     * @param level
     * @param hsCmp
     * @return
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp, int register){
        StringBuffer str = new StringBuffer("");
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
        return str;
    }

    /**
     * Reemplaza los caracteres con acento y ñ, por sus codificacion HTML respectiva
     * @param data  String de datos donde se buscaran los caracteres especiales
     * @return
     */
    private String replaceAccent(String data){
        String str = "";
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
        return str;
    }

    /**
     * Transforma el texto del tipo en Java a el tipo que le corresponde en
     * el XML
     * @param strData
     * @return
     */
    private String castTypeJavaToXML(String strData){
        String strSld = "";
        if ("java.lang.String".equals(strData)){
            strSld = "string";
        }
        if ("java.lang.Integer".equals(strData)){
            strSld = "integer";
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

    private static void print(String s, int level){
        String str ="";
        for (int i=level; i>0; i--){
            str = str + "\t";
        }
    }

/******************** METODOS DE LA ENTIDAD ******************************/

    private StringBuffer getXMLByList(List lst){
        StringBuffer str = new StringBuffer("");
        str.append("<?xml version='1.0' encoding='ISO-8859-1'?>"+
                   "<qry source='SELECT * FROM aplicacion' columnas='1' >"+
                   "<registro id='1'>");

        if (lst!=null){
            Iterator it = lst.iterator();
            while (it!=null && it.hasNext()){
                Campo cmp = (Campo) it.next();
                str.append("<campo>");
                str.append(("<nombre>"+cmp.getNombre()+"</nombre>"));
                str.append(("<alias_campo>"+cmp.getAlias()+"</alias_campo"));
                str.append(("<valor>"+cmp.getValor()+"</valor>"));
                str.append(("<tipo_dato>"+cmp.getTypeDataDB()+"</tipo_dato>"));
                str.append("<tipo_control></tipo_control>");
                str.append("<evento>onClick=alert('Ejecuta evento on blur');");
                str.append("<ayuda></ayuda>");
                str.append("</campo>");
            }
        }
        str.append("</registro>"+
                   "</qry>");
        return str;
    }

    private StringBuffer getDataByXML(StringBuffer str){
        StringBuffer strSld = new StringBuffer("");
        strSld.append("[");
        strSld.append("{clave_aplicacion:\"1\",aplicacion:\"Back End\",estatus:\"En desarrollo\"},");
        strSld.append("{clave_aplicacion:\"2\",aplicacion:\"CRM\",estatus:\"En desarrollo\"},");
        strSld.append("{clave_aplicacion:\"3\",aplicacion:\"Proyectos\",estatus:\"En desarrollo\"},");
        strSld.append("{clave_aplicacion:\"4\",aplicacion:\"SRM\",estatus:\"En desarrollo\"},");
        strSld.append("{clave_aplicacion:\"5\",aplicacion:\"Control Presupuestal\",estatus:\"En desarrollo\"}");
        strSld.append("]");
        return strSld;
    }

    private boolean validateXML(StringBuffer xml){
        boolean bln=true;

        return bln;
    }

    private List getListByXML(StringBuffer xml, List lst){
        return lst;
    }


}
