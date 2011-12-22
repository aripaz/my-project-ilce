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
package mx.ilce.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.conection.ConEntidad;
import mx.ilce.util.UtilDate;

/**
 * Clase para agregar seguimiento a la entrada y salida de datos desde la capa
 * vista
 * @author ccatrilef
 */
public class SpyHandler {

    private String timeIni;
    private String dateFile;
    private String servlet;
    private HashMap hsForm;
    private StringBuffer xmlSld;
    private String rutaFile;

    /**
     * Obtiene el Servlet de la operación
     * @return  String  Nombre del Servlet
     */
    private String getServlet() {
        return servlet;
    }

    /**
     * Asigna el Servlet de la operación
     * @param servlet   Nombre del Servlet
     */
    private void setServlet(String servlet) {
        this.servlet = servlet;
    }

    /**
     * Constructor de la clase. Inicializa la fecha y hora de activación, además
     * de otras variables
     */
    public SpyHandler() {
        UtilDate utDate = new UtilDate();
        this.timeIni = utDate.getFechaHMS();
        this.dateFile = utDate.getFechaHMS(UtilDate.formato.AMD, "","");
        this.hsForm = new HashMap();
        this.xmlSld = new StringBuffer("");
        this.rutaFile = "c:/log/";
    }

    /**
     * Obtiene la fecha para el archivo de salida
     * @return  String  Fecha del archivo
     */
    private String getDateFile() {
        return dateFile;
    }

    /**
     * Asigna la fecha para el archivo de salida
     * @param dateFile  Fecha del archivo
     */
    private void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    /**
     * Obtiene la ruta del archivo de salida
     * @return  String  Ruta del archivo
     */
    private String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta del archivo de salida
     * @param rutaFile  Ruta del archivo
     */
    private void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene el HashMap con las variables capturadas
     * @return  HashMap     Variables capturadas
     */
    private HashMap getHsForm() {
        return hsForm;
    }

    /**
     * Asigna el HashMap con las variables capturadas
     * @param hsForm    Variables capturadas
     */
    public void setHsForm(HashMap hsForm) {
        this.hsForm = hsForm;
    }

    /**
     * Obtiene la hora de inicio de la operación
     * @return  String      Texto con la hora
     */
    private String getTimeIni() {
        return timeIni;
    }

    /**
     * Asigna la hora de inicio de la operación
     * @param timeIni   Texto con la hora
     */
    private void setTimeIni(String timeIni) {
        this.timeIni = timeIni;
    }

    /**
     * Obtiene el XML de salida
     * @return  StringBuffer    XML de salida
     */
    private StringBuffer getXmlSld() {
        return xmlSld;
    }

    /**
     * Asigna el XML de salida
     * @param xmlSld    XML de salida
     */
    public void setXmlSld(StringBuffer xmlSld) {
        this.xmlSld = xmlSld;
    }

    /**
     * Método que convierte el contenido de un HashMap en un Texto, donde se
     * indica el nombre del dato y su valor
     * @return  String  Texto con el contenido del HashMap
     */
    private String convertHashToString(){
        StringBuilder str = new StringBuilder("");
        HashMap hsMap = this.getHsForm();

        for (int i=0;i<hsMap.size();i++){
            String strKey = (String) hsMap.keySet().toArray()[i];
            String strDat = (String) hsMap.get(strKey);
            str.append("Dato[").append(i).append("]:").append(strKey).append(" ");
            str.append("\tValor[").append(i).append("]:").append(strDat).append("\n");
        }

        return str.toString();
    }

    /**
     * Método que activa la escritura en archivo de la data capturada. El nombre
     * entregado se usa para darle nombre al archivo.
     * @param nameServlet
     */
    public void SpyData(String nameServlet){
        this.setServlet(nameServlet);
        if (aplySpy()){
            writeToFile();
        }
    }

    /**
     * Método que valida si esta activo o no la operación de captura de data
     * @return  boolean     Resultado de la validación
     */
    private boolean aplySpy(){
        boolean sld = false;

        String query = "select count(*) as valor "
                + "from rpf_iniData where nombre = 'SPY'";
        try {
            ConEntidad con = new ConEntidad();
            mx.ilce.bitacora.Bitacora bitacora = new mx.ilce.bitacora.Bitacora();
            con.setBitacora(bitacora);
            mx.ilce.bean.DataTransfer dataTransfer = new mx.ilce.bean.DataTransfer();
            dataTransfer.setQuery(query);
            mx.ilce.bean.HashCampo hsCmp = con.getDataByQuery(dataTransfer);
            HashMap hsMap = hsCmp.getListData();
            if (!hsMap.isEmpty()){
                ArrayList lst = (ArrayList) hsMap.get(0);
                mx.ilce.bean.Campo cmp = (mx.ilce.bean.Campo) lst.get(0);
                if (cmp.getValor().equals("1")){
                    sld = true;
                }
            }
        } catch (ExceptionHandler ex) {
            Logger.getLogger(SpyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sld;
    }

    /**
     * Método que realiza la escritura a un archivo de la ruta capturada
     * @return  boolean     Resultado de la operación de escritura
     */
    private boolean writeToFile(){
        boolean sld = true;
        String strNameFile = "";
        try{
            File directorio = new File(this.getRutaFile());
            if (!directorio.exists()){
                sld = false;
            }
            if (!directorio.isDirectory()){
                sld = false;
            }
            UtilDate utDate = new UtilDate();
            strNameFile = strNameFile.concat("SPY_"+getDateFile());
            strNameFile = strNameFile.concat(".log");
            StringBuffer strTexto = new StringBuffer();
            strTexto.append("\nSERVLET:").append(this.getServlet());
            strTexto.append("\nFECHA-HORA INICIO: ").append(this.getTimeIni());
            strTexto.append("\nFECHA-HORA TERMINO: ").append(utDate.getFechaHMS());
            strTexto.append("\nDATA FORMULARIO:\n").append(this.convertHashToString());
            strTexto.append("\nDATA XML:\n").append(this.getXmlSld());
            strTexto.append("\n****************\n");
            if (sld){
                sld = guardarArchivo(strTexto, strNameFile);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sld;
    }

    /**
     * Método que escribe en un archivo la data entregada
     * @param strEntrada    Data entregada
     * @param nameFile      Nombre del archivo
     * @return  boolean     Resultado de la operación de escritura
     * @throws IOException
     */
    private boolean guardarArchivo(StringBuffer strEntrada, String nameFile) throws IOException{
        boolean sld = true;
        FileWriter w = null;
        try{
            w = new FileWriter(rutaFile + "/" + nameFile, true);
            w.append(strEntrada.toString());
        }catch (IOException eFile){
            sld = false;
        }catch(Exception e){
            sld = false;
        }finally{
            if (w!=null){
                w.flush();
                w.close();
            }
        }
        return sld;
    }
}
