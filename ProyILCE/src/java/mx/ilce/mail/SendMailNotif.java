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
package mx.ilce.mail;

import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase encargada de activar el envío de mail de notificación
 * según el estatus de una tarea
 * @author ccatrilef
 */
public class SendMailNotif {

    private Bitacora bitacora;
    private DataMail dataMail;

    /**
     * Obtiene el objeto DataMail, que contiene los datos del mail
     * @return  DataMail    Objeto con los datos del mail
     */
    public DataMail getDataMail() {
        return dataMail;
    }

    /**
     * Asigna el objeto DataMail, que contiene los datos del mail
     * @param dataMail  Objeto con los datos del mail
     */
    public void setDataMail(DataMail dataMail) {
        this.dataMail = dataMail;
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
     * Método para envío de mail asociado a un evento y el estatus de la
     * actividad a la que viene asociado.
     * @return  Boolean     Valor de validación del resultado de la operación
     * @throws ExceptionHandler
     */
    public boolean admSendMail() throws ExceptionHandler{
        boolean sld = false;
        String queryRegitro = "";
        try{
            queryRegitro = "select clave_registro , "
                    + " fecha_bitacora , clave_empleado , clave_forma, clave_aplicacion"
                    + " from bitacora_aplicacion "
                    + " where clave_bitacora = " + this.getBitacora().getIdBitacora();

            ConEntidad con = new ConEntidad();

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQuery(queryRegitro);
            HashCampo hsCmp = con.getDataByQuery(dataTransfer);

            HashMap hsMap = hsCmp.getListData();

            List lstCmp = null;
            String idRegistro = null;
            String fechaBitacora = null;
            String claveUser = null;
            String claveForma = null;
            String claveAplicacion = null;
            if (!hsMap.isEmpty()){
                lstCmp = (List) hsMap.get(0);
                if (!lstCmp.isEmpty()){
                    Campo cmp = (Campo) lstCmp.get(0);
                    idRegistro = cmp.getValor();
                    cmp = (Campo) lstCmp.get(1);
                    fechaBitacora = cmp.getValor();
                    cmp = (Campo) lstCmp.get(2);
                    claveUser = cmp.getValor();
                    cmp = (Campo) lstCmp.get(3);
                    claveForma = cmp.getValor();
                    cmp = (Campo) lstCmp.get(4);
                    claveAplicacion = cmp.getValor();
                }
            }
            String idStatus = null;
            if ((idRegistro!=null) && (!"".equals(idRegistro))){
                String queryTarea = "select clave_estatus "
                        + " from TAREA where clave_tarea =" + idRegistro;
                dataTransfer.setQuery(queryTarea);
                hsCmp = con.getDataByQuery(dataTransfer);
                hsMap = hsCmp.getListData();
                if (!hsMap.isEmpty()){
                    lstCmp = (List) hsMap.get(0);
                    if (!lstCmp.isEmpty()){
                        Campo cmp = (Campo) lstCmp.get(0);
                        idStatus = cmp.getValor();
                    }
                }
            }
            String idFlujo = null;
            String enviarNotificacion = null;
            String notificacion = null;
            String subject = null;
            String fecha = null;
            String proyecto = null;
            if ((idStatus!=null)&&(!"".equals(idStatus))){
                String queryFlujo = "select clave_flujo "
                        + ", enviar_notificacion, notificacion "
                        + ", ct.categoria, tr.fecha_inicial, py.proyecto "
                        + " from flujo_datos_forma fdf , categoria_tarea ct "
                        + ", tarea tr , proyecto py "
                        + " where fdf.clave_forma = " + claveForma
                        + " and   fdf.clave_aplicacion = " + claveAplicacion
                        + " and   fdf.secuencia = " + idStatus
                        + " and   tr.clave_tarea = " + idRegistro
                        + " and   tr.clave_categoria = ct.clave_categoria "
                        + " and   tr.clave_proyecto = py.clave_proyecto";
                dataTransfer.setQuery(queryFlujo);
                hsCmp = con.getDataByQuery(dataTransfer);
                hsMap = hsCmp.getListData();
                if (!hsMap.isEmpty()){
                    lstCmp = (List) hsMap.get(0);
                    if (!lstCmp.isEmpty()){
                        Campo cmp = (Campo) lstCmp.get(0);
                        idFlujo = cmp.getValor();
                        cmp = (Campo) lstCmp.get(1);
                        enviarNotificacion = cmp.getValor();
                        cmp = (Campo) lstCmp.get(2);
                        notificacion = cmp.getValor();
                        cmp = (Campo) lstCmp.get(3);
                        subject = cmp.getValor();
                        cmp = (Campo) lstCmp.get(4);
                        fecha = cmp.getValor();
                        cmp = (Campo) lstCmp.get(5);
                        proyecto = cmp.getValor();
                    }
                }
            }
            if ( (idFlujo!=null)&&(!"".equals(idFlujo)) &&
                 (enviarNotificacion!=null)&&(!"".equals(enviarNotificacion)) &&
                 (notificacion!=null)&&(!"".equals(notificacion))){

                String queryUser = "select em.email, em.nombre , "
                        + " em.apellido_paterno , em.apellido_materno"
                        + " from empleado em "
                        + " where em.clave_empleado =" + claveUser;
                dataTransfer.setQuery(queryUser);
                hsCmp = con.getDataByQuery(dataTransfer);
                hsMap = hsCmp.getListData();
                String[] strUser = null;
                if (!hsMap.isEmpty()){
                    strUser = getDataUser(hsMap);
                }

                notificacion = notificacion.replace("%CATEGORIA", subject);
                notificacion = notificacion.replace("%FECHA", fecha);
                notificacion = notificacion.replace("%PROYECTO", proyecto);
                notificacion = notificacion.replace("%DIABITACORA", fechaBitacora);
                if (strUser!=null){
                    notificacion = notificacion.replace("%USER", strUser[1]);
                }

                String queryDest = "select em.email, em.nombre "
                        + ", em.apellido_paterno , em.apellido_materno"
                        + " from empleado em , flujo_datos_empleado fde"
                        + " where em.clave_empleado = fde.clave_empleado_asignado"
                        + " and fde.clave_flujo = " + idFlujo;

                dataTransfer.setQuery(queryDest);
                hsCmp = con.getDataByQuery(dataTransfer);
                hsMap = hsCmp.getListData();
                String strTo = "";
                if (!hsMap.isEmpty()){
                    strTo = getDataDest(hsMap);
                }

                this.setDataMail(new DataMail());
                this.getDataMail().setSubJect(subject);
                this.getDataMail().setTexto(notificacion);
                this.getDataMail().setStrTo(strTo);
                this.getDataMail().setStrCopy(strUser[0]);
                sld = true;
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para activar el envio de Mail de notificación");
            eh.setDataToXML("QUERY REGISTRO",queryRegitro);
            eh.setDataToXML(this.getBitacora());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }

    /**
     * Obtiene los nombres contenidos en el HashMap. Este requiere que venga
     * en un orden específico, el orden se lo da la query con que se obtuvo los
     * datos. La salida entrega los nombres tras recorrer el HashMap. Usado para
     * obtener los nombres de los destinatarios de mail.
     * @param hsMap         HashMap con los datos de los nombres
     * @return  String[]    Arreglo con el listado de los nombres
     */
    private String[] getDataUser(HashMap hsMap){
        String[] strData = new String[2];

        if ((hsMap!=null)&&(!hsMap.isEmpty())) {
            List lstCmp = (List) hsMap.get(0);
            String strNombre = "";
            if (!lstCmp.isEmpty()){
                Campo cmp = (Campo) lstCmp.get(0);
                strData[0] = cmp.getValor();
                cmp = (Campo) lstCmp.get(1);
                if((cmp.getValor()!=null)&&(!"".equals(cmp.getValor()))){
                    strNombre = cmp.getValor();
                }
                cmp = (Campo) lstCmp.get(2);
                if((cmp.getValor()!=null)&&(!"".equals(cmp.getValor()))){
                    strNombre = strNombre +  " " + cmp.getValor();
                }
                cmp = (Campo) lstCmp.get(3);
                if((cmp.getValor()!=null)&&(!"".equals(cmp.getValor()))){
                    strNombre = strNombre +  " " + cmp.getValor();
                }
                strData[1] = strNombre;
            }
        }
        return strData;
    }

    /**
     * Obtiene un Texto que incluye los datos leídos en el HashMap. Usado para
     * obtener los mail de los destinatarios en un solo texto.
     * @param hsMap     Datos a recorrer
     * @return  String  Texto conteniendo los datos leidos
     */
    private String getDataDest(HashMap hsMap){
        String strData = "";

        if ((hsMap!=null)&&(!hsMap.isEmpty())) {
            for (int i=0;i<hsMap.size();i++){
                List lstCmp = (List) hsMap.get(i);
                if (!lstCmp.isEmpty()){
                    Campo cmp = (Campo) lstCmp.get(0);
                    if ((cmp.getValor()!=null)&&(!"".equals(cmp.getValor()))){
                        if ("".equals(strData)){
                            strData = cmp.getValor();
                        }else{
                            strData = strData + ";" + cmp.getValor();
                        }
                    }
                }
            }
        }
        return strData;
    }
}
