/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.component;

import java.util.Iterator;
import java.util.List;
import mx.ilce.bean.Campo;

/**
 *
 * @author ccatrilef
 */
public class AdminXML {

    public boolean validateXML(StringBuffer xml){
        boolean bln=true;

        return bln;
    }

    public List getListByXML(StringBuffer xml, List lst){
        return lst;
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
