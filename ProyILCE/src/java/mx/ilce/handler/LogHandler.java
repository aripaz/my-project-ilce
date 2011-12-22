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
import mx.ilce.util.UtilDate;


/**
 * Clase implementada para manejar el control de Log de la aplicación
 * @author ccatrilef
 */
public class LogHandler {

    private String rutaFile;
    private String dateFile;
    private String time;
    private StringBuffer textMessage = new StringBuffer("");
    private StringBuffer textData = new StringBuffer("");
    private String strQuery;
    private boolean boolSelect=true;

    /**
     * Obtiene el texto de la query asignada
     * @return  String  Texto de la query
     */
    public String getStrQuery() {
        return strQuery;
    }

    /**
     * Asigna el texto de la query
     * @param strQuery  Texto de la query
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Entrega el valor de validación de si se debe o no registrar el Select
     * @return  boolean     Valor TRUE O FALSE de validación
     */
    public boolean isBoolSelect() {
        return boolSelect;
    }

    /**
     * Asigna el valor de validación de si se debe o no registrar el Select
     * @param boolSelect    Valor TRUE O FALSE de validacion
     */
    public void setBoolSel(boolean boolSelect) {
        this.boolSelect = boolSelect;
    }

    /**
     * Constructor básico de la clase, inicializa las variables Time y DateFile
     */
    public LogHandler() {
        UtilDate ut = new UtilDate();
        setTime(ut.getFechaHMS());
        setDateFile(ut.getFecha(UtilDate.formato.AMD,""));
    }

    /**
     * Constructor de la clase, el cual asigna los mensajes que se deben escribir
     * en el log
     * @param rutaFile      Ruta donde se dejara el archivo
     * @param textMessage   Texto del mensaje
     * @param textData      Data adicional para el mensaje
     * @return boolean      Valor con el resultado de la operación
     */
    public boolean logData(String rutaFile,StringBuffer textMessage, StringBuffer textData){
        boolean sld =false;
        setRutaFile(rutaFile);
        setTextData(textData);
        setTextMessage(textMessage);
        if (this.rutaFile!=null){
            if (this.isBoolSelect()){
                sld = writeToFile();
            }else{
                sld = writeToFile("OPER");
            }
        }
        return sld;
    }

    /**
     * Constructor de la clase, el cual asigna los mensajes que se deben escribir
     * en el log
     * @param rutaFile      Ruta donde se dejara el archivo
     * @param textMessage   Texto del mensaje
     * @param textData      Data adicional para el mensaje
     * @return  boolean     Resultado de la operación de escritura
     */
    public boolean logData(String rutaFile,StringBuffer textMessage,
            StringBuffer textData, String HeaderFile){
        boolean sld =false;
        setRutaFile(rutaFile);
        setTextData(textData);
        setTextMessage(textMessage);
        if (this.rutaFile!=null){
            sld = writeToFile(HeaderFile);
        }
        return sld;
    }

    /**
     * Método para guardar los warning
     * @param rutaFile      Ruta del archivo
     * @param textMessage   Texto del mensaje
     * @param textData      Datos obtenidos
     * @return  boolean     Resultado de la operación de escritura
     */
    public boolean logWarning(String rutaFile,StringBuffer textMessage, StringBuffer textData){
        boolean sld =false;
        setRutaFile(rutaFile);
        setTextData(textData);
        setTextMessage(textMessage);
        if (this.rutaFile!=null){
           sld = writeToFile("WARNING");
        }
        return sld;
    }

    /**
     * Obtiene el campo DateFile que se usara para el nombre del archivo de Log
     * @return  String  Texto con el valor
     */
    private String getDateFile() {
        return dateFile;
    }

    /**
     * Asigna el campo DateFile que se usara para el nombre del archivo de Log
     * @param dateFile      Texto con la fecha que se asignara
     */
    private void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    /**
     * Obtiene el texto contenido en textData
     * @return  StringBuffer    Texto con la data obtenida
     */
    private StringBuffer getTextData() {
        return textData;
    }

    /**
     * Asigna el texto a textData
     * @param textData  Texto con la Data que se asignara
     */
    private void setTextData(StringBuffer textData) {
        this.textData = textData;
    }

    /**
     * Obtiene el texto del mensaje contenido en textMessage
     * @return  StringBuffer    Texto con el mensaje obtenido
     */
    private StringBuffer getTextMessage() {
        return textMessage;
    }

    /**
     * Asigna el texto del mensaje en textMessage
     * @param textMessage   Texto con el Mensaje que se asignara
     */
    private void setTextMessage(StringBuffer textMessage) {
        this.textMessage = textMessage;
    }

    /**
     * Obtiene el texto de la fecha y hora asignada al objeto
     * @return  String  Texto con la fecha y hora obtenida
     */
    private String getTime() {
        return time;
    }

    /**
     * Asigna el texto de la fecha y hora al objeto
     * @param time      Texto con la fecha y hora
     */
    private void setTime(String time) {
        this.time = time;
    }

    /**
     * Obtiene la ruta donde se depositara el archivo de Log
     * @return  String  Ruta del archivo
     */
    private String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta donde se depositara el archivo de Log
     * @param rutaFile  Ruta donde ubicar el archivo
     */
    private void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Ejecuta la escritura del archivo de Log con los datos contenidos en el Objeto
     * @return  boolean     Valor con la validación del resultado de la operación
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
            strNameFile = strNameFile.concat(getDateFile());
            strNameFile = strNameFile.concat(".log");
            StringBuffer strTexto = new StringBuffer();
            strTexto.append("\nFECHA: ").append(this.getTime());
            strTexto.append("\nMENSAJE: ").append(this.getTextMessage());
            strTexto.append("\nQUERY:\n").append(this.strQuery);
            strTexto.append("\n\nDATA:\n").append(this.getTextData());
            strTexto.append("\n****************\n");
            if (sld){
                sld = guardarArchivo(strTexto, strNameFile);
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
        return sld;
    }

    /**
     * Método que escribe en un archivo el contenido del LOG, agregando un texto
     * adicional al nombre del archivo para diferenciarlo. De ser exitosa la
     * operación entrega un TRUE, sino un FALSE
     * @param oper          Texto adicional para colocar al nombre de archivo
     * @return  boolean     Valor de validación con el resultado de la operación
     */
    private boolean writeToFile(String oper){
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
            strNameFile = strNameFile.concat(getDateFile()+oper);
            strNameFile = strNameFile.concat(".log");
            StringBuffer strTexto = new StringBuffer();
            strTexto.append("\nFECHA: ").append(this.getTime());
            strTexto.append("\nMENSAJE: ").append(this.getTextMessage());
            strTexto.append("\nQUERY:\n").append(this.strQuery);
            strTexto.append("\n\nDATA:\n").append(this.getTextData());
            strTexto.append("\n****************\n");
            if (sld){
                sld = guardarArchivo(strTexto, strNameFile);
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
        return sld;
    }


    /**
     * Escribe el archivo de Log con la data entregada y el nombre señalado.
     * De ser exitosa la operación entrega un TRUE, sino un FALSE
     * @param strEntrada    Data a escribir en el archvio
     * @param nameFile      Nombre que debera poseer el archivo
     * @return  boolean     Valor de validación con el resultado de la operación
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
