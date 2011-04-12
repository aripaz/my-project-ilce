/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.conection.ConSession;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author ccatrilef
 */
public class AdminXML {
    private static File WORKING_DIRECTORY;
    private int numRow=0;

    public boolean validateXML(StringBuffer xml){
        boolean bln=true;

        return bln;
    }

    public List getListByXML(StringBuffer xml, List lst){
        return lst;
    }

    private static void print(String s, int level){
        String str ="";
        for (int i=level; i>0; i--){
            str = str + "\t";
        }
        System.out.print(str);
        System.out.println(s+", LEVEL:"+level);
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
        System.out.println("====== XML ======");
        System.out.println(str);

        return str;
    }

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
        System.out.println("====== XML LARGO ======");
        System.out.println(str);

        return str;
    }

    /**
     * Obtiene un DOcumento XML, a partir del nombre solicitado. Se asume que
     * los archivos XML entan en la ruta resource/xml/[nombre archivo]
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
                                                   //separador + "widget.session.xml");
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
                //print("NODO:"+ e.getNodeName(),level);
                str.append(("\n<"+e.getNodeName()+" "));
                strPadre = e.getNodeName();
                cmp = hsCmp.getCampoByName(e.getNodeName().toUpperCase());
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
                    //print("ATRIBUTO:"+attr.getNodeName(),level);
                    //print("VALOR:\""+attr.getNodeValue()+"\"",level);
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
                    //print("ATRIBUTO:"+attr.getNodeName(),level);
                    //print("VALOR:\""+(++numRow)+"\"",level);
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
     * @param e
     * @param level
     * @param hsCmp
     * @return
     */
    private StringBuffer listNode(Node e, int level, HashCampo hsCmp, int register){
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
                //print("NODO:"+ e.getNodeName(),level);
                str.append(("\n<"+e.getNodeName()+" "));
                strPadre = e.getNodeName();
                cmp = hsCmp.getCampoByName(e.getNodeName().toUpperCase());
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
                    //print("ATRIBUTO:"+attr.getNodeName(),level);
                    //print("VALOR:\""+attr.getNodeValue()+"\"",level);
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
                    //print("ATRIBUTO:"+attr.getNodeName(),level);
                    //print("VALOR:\""+(++numRow)+"\"",level);
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

    public StringBuffer getXMLByList(List lst){
        StringBuffer str = new StringBuffer("");
        str.append("<?xml version='1.0' encoding='ISO-8859-1'?>"+
                   "<qry source='SELECT * FROM aplicacion' columnas='1' >"+
                   "<registro id='1'>");

        if (lst!=null){
            Iterator it = lst.iterator();
            while (it!=null && it.hasNext()){
                Campo cmp = (Campo) it.next();
                str.append("<campo>");
                str.append("<nombre>"+cmp.getNombre()+"</nombre>");
                str.append("<alias_campo>"+cmp.getAlias()+"</alias_campo");
                str.append("<valor>"+cmp.getValor()+"</valor>");
                str.append("<tipo_dato>"+cmp.getTypeDataDB()+"</tipo_dato>");
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

    public StringBuffer getDataByXML(StringBuffer str){
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

}
