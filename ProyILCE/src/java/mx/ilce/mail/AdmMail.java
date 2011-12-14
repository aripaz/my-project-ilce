package mx.ilce.mail;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.Campo;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase implementada para la administración y obtención de datos para envio de Mail
 * @author ccatrilef
 */
public class AdmMail {

    private Properties prop;
    private Connection conn;
    private String strData[];
    private DataMail dataMail;
    private Bitacora bitacora;

    /**
     * Query usada para obtener las queries del tipo MAILIST
     */
    private static String MAILLIST = "MAILLIST";

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
     * Obtiene el objeto DataMail
     * @return  DataMail    Objeto DataMail
     */
    public DataMail getDataMail() {
        return dataMail;
    }

    /**
     * Asigna el objeto DataMail
     * @param dataMail  Objeto DataMail
     */
    public void setDataMail(DataMail dataMail) {
        this.dataMail = dataMail;
    }

    /**
     * Obtiene el arreglo con los datos de entrada
     * @return  String[]    Arreglo con datos
     */
    public String[] getStrData() {
        return strData;
    }

    /**
     * Asigna el arreglo con los datos de entrada
     * @param strData   Arreglo con datos
     */
    public void setStrData(String[] strData) {
        this.strData = strData;
    }

    /**
     * Obtiene los datos de tipo Properties
     * @return  Properties  Objeto Properties
     */
    public Properties getProp() {
        return prop;
    }

    /**
     * Asigna los datos de tipo Properties
     * @param prop  Objeto Properties
     */
    public void setProp(Properties prop) {
        this.prop = prop;
    }

    /**
     * Obtiene el valor de una palabra clave (key), desde un arreglo de
     * properties
     * @param prop      Listado de properties obtenido desde el archivo de configuración
     * @param strKey    Palabra usada como Key para la búsqueda dentro del propertie
     * @return String   Valor de la Key solicitada
     */
    private static String getKey(Properties prop, String key) throws ExceptionHandler{
        String sld = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                sld = prop.getProperty(key);
            }
	}catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,AdmMail.class,
                             "Problemas para obtener la llave desde el properties");
            eh.setDataToXML("KEY",key);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	}
        return sld;
    }

    /**
     * Obtiene los receptores de mail asociados al evento y la forma
     * @param dataMail
     * @return  HashMap     Hash con los receptores de mail
     * @throws ExceptionHandler
     */
    public HashMap getReceptorMail(DataMail dataMail) throws ExceptionHandler{
        HashMap hm = new HashMap();
        try{
            this.setDataMail(dataMail);
            this.setProp(UtilMail.leerConfig());

            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());

            DataTransfer dataTransfer = new DataTransfer();

            String idQuery = getKey(this.getProp(),MAILLIST);
            dataTransfer.setIdQuery(Integer.valueOf(idQuery));
            String[] arrData = new String[1];
            arrData[0]= dataMail.getClaveForma().toString();
            dataTransfer.setArrData(arrData);

            HashCampo hscmp = conE.getDataByIdQuery(dataTransfer);
            Campo cmp = (hscmp.getCampoByAlias("CONSULTA")==null)?new Campo():hscmp.getCampoByAlias("CONSULTA");

            String queryPersonas = cmp.getValor();
            //TODO:ESTA ES UNA QUERY POR DEFECTO EN CASO QUE FALLE LA QUERY REAL Y SE ACTIVE EL MAIL
            if ((queryPersonas==null)||("".equals(queryPersonas))){
                queryPersonas = "select e.nombre, e.apellido_paterno, e.apellido_materno, e.email "
                        + " from empleado e where e.apellido_paterno = 'x@x.com'";
            }
            StringBuilder strLstNombre = new StringBuilder();
            StringBuilder strLstMail = new StringBuilder();

            if ((queryPersonas!=null)&&(!"".equals(queryPersonas))) {
                dataTransfer = new DataTransfer();
                dataTransfer.setQuery(queryPersonas);
                HashCampo hsCmp = conE.getDataByQuery(dataTransfer);
                HashMap hsMp = hsCmp.getListData();
                for (int i=0;i<hsMp.size();i++){
                    List lst = (List) hsMp.get(Integer.valueOf(i));
                    Iterator it = lst.iterator();
                    StringBuilder strNombre = new StringBuilder();
                    StringBuilder strMail = new StringBuilder();
                    boolean blNombre = false;
                    boolean blApPat = false;
                    boolean blApMat = false;
                    boolean blMail = false;
                    while (it.hasNext()){
                        Campo cmpPer = (Campo) it.next();
                        if ("NOMBRE".equals(cmpPer.getNombre())){
                            strNombre.append(cmpPer.getValor());
                            blNombre = true;
                        }
                        if ("APELLIDOPATERNO".equals(cmpPer.getNombre())){
                            strNombre.append(" ");
                            strNombre.append(cmpPer.getValor());
                            blApPat = true;
                        }
                        if ("APELLIDOMATERNO".equals(cmpPer.getNombre())){
                            strNombre.append(" ");
                            strNombre.append(cmpPer.getValor());
                            blApMat = true;
                        }
                        if ("EMAIL".equals(cmpPer.getNombre())){
                            strMail.append(cmpPer.getValor());
                            blMail = true;
                        }
                        if (blNombre && blApPat && blApMat && blMail){
                            strLstNombre.append(strNombre).append(";");
                            strLstMail.append(strMail).append(";");
                            strNombre = new StringBuilder();
                            strMail = new StringBuilder();
                            blNombre = false;
                            blApPat = false;
                            blApMat = false;
                            blMail = false;
                        }
                    }
                }
            }
            hm.put("TO",strLstMail.toString());
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                      "Problemas para procesar los datos del Mail");
            eh.setDataToXML(dataMail);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hm;
    }
}
