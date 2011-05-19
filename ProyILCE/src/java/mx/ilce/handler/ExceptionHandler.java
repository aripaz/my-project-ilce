/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.handler;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Manejador de Exception general, que permite unificar las respuestas generales
 * de las Exception ocurridas en la aplicacion. Mediante algunas configuraciones
 * y entradas adicionales, permite dar informes mas generales, ademas de tomar
 * acciones adicionales como la generacion de archivos con el registro de las
 * Exception, junto con su traza, ubicacion, dia y hora de su activacion.
 * @author ccatrilef
 */
public class ExceptionHandler extends Throwable {

    private enum formato {DMA,AMD};
    private StringBuffer textError = new StringBuffer("");
    private StringBuffer textMessage = new StringBuffer("");
    private StringBuffer xmlError = new StringBuffer("");
    private StringBuffer secuenceError = new StringBuffer("");
    private StringBuffer typeError = new StringBuffer("");
    private ArrayList arrClass = new ArrayList();
    private String packageProy = "mx.ilce.";
    private String time;
    private UtilDate utiDt;
    private ArrayList arrayData;
    private String stringData;
    private boolean seeStringData;
    private boolean logFile;
    private String rutaFile;

    public ExceptionHandler() {
    }

    private void cleanData(){
        this.textError = new StringBuffer("");
        this.textMessage = new StringBuffer("");
        this.xmlError = new StringBuffer("");
        this.secuenceError = new StringBuffer("");
        this.typeError = new StringBuffer("");
        this.arrClass = new ArrayList();
        this.time="";
        this.arrayData=new ArrayList();
        this.stringData=null;
        this.logFile=false;
        this.rutaFile="";
    }

    /**
     * Constructor de la Exception donde se entrega un objeto con la clase de la
     * exception ocurrida, la clase donde ocurrio el problema y un mensaje que se
     * desea desplegar, junto con los mensajes obtenidos desde la exception y la
     * data adicional entregada.
     * @param obj
     * @param clase
     * @param message
     */
    public ExceptionHandler(Object obj, Class clase, String message){
        cleanData();
        utiDt = new UtilDate();
        setTime(utiDt.getFechaHMS());
        setTypeError(obj.getClass().getSimpleName());
        setTextMessage(message);

        if (getTypeError().equals(NullPointerException.class.getSimpleName())) {
            NullPointerException e = (NullPointerException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());

        }else if (getTypeError().equals(ClassNotFoundException.class.getSimpleName())) {
            ClassNotFoundException e = (ClassNotFoundException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());

        }else if (getTypeError().equals(ClassCastException.class.getSimpleName())) {
            ClassCastException e = (ClassCastException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());

        }else if (getTypeError().equals(IOException.class.getSimpleName())) {
            IOException e = (IOException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());

        }else if (getTypeError().equals(SQLException.class.getSimpleName())) {
            SQLException e = (SQLException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());

        }else if (getTypeError().equals(FileNotFoundException.class.getSimpleName())) {
            FileNotFoundException e = (FileNotFoundException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else {  //
            Exception e = (Exception)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }
    }

    public boolean writeToFile(){
        boolean sld = true;
        writeLogError wf = new writeLogError();
        String strRutaFile = this.getRutaFile();
        String strNameFile = "";
        if ((this.logFile) && (this.rutaFile!=null)){
            try{
                StringBuffer strAdicional = new StringBuffer();
                File directorio = new File(this.rutaFile);
                if (!directorio.exists()){
                    strAdicional.append("No Existe el Directorio para Log\n");
                    sld = false;
                }
                if (!directorio.isDirectory()){
                    strAdicional.append("El Directorio configurado para Log no es un directorio\n");
                    sld = false;
                }
                if ((strAdicional!=null) && (!"".equals(strAdicional))){
                    this.setStringData(strAdicional.toString());
                }
                UtilDate ut = new UtilDate();
                strNameFile = strNameFile.concat(ut.getFecha(formato.AMD,""));
                strNameFile = strNameFile.concat(this.getTypeError().toString());
                strNameFile = strNameFile.concat(".log");
                wf.setNameFile(strNameFile);
                wf.setRutaFile(strRutaFile);
                StringBuffer strTexto = new StringBuffer();
                strTexto.append("\nFECHA: ").append(this.getTime());
                strTexto.append("\nTIPO: ").append(this.getTypeError());
                strTexto.append("\nMENSAGE: ").append(this.getTextMessage());
                strTexto.append("\nDESCRIPCION: ").append(this.getTextError());
                if ((this.stringData!=null)&&(!"".equals(this.stringData))){
                    strTexto.append("\nDATA ADICIONAL:\n").append(this.getStringData());
                }
                strTexto.append("\nTRAZA\n-------------\n").append(this.getSecuenceError());
                strTexto.append("\n****************\n");
                if (sld){
                    sld = wf.guardarArchivo(strTexto);
                }else{
                    this.setStringData(strAdicional.toString());
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return sld;
    }

    private void setArrayData(){
        ArrayList arr = this.getArrayData();

        if ((arr!=null)&& (!arr.isEmpty())){
            StringBuffer str = new StringBuffer();
            for (int i=0; i<arr.size(); i++){
                str.append(("Dato "+i+": ")).append(arr.get(i).toString());
                str.append("\n");
            }
            if (this.textError==null){
                setTextError("");
            }
            this.textError.append("\n").append(str);
        }
    }

    private void setTextToXmlError(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<error>\n");
        str.append("<row>");
        str.append("ERROR:");
        str.append(this.getTextMessage());
        str.append("</row>").append("\n");
        str.append("<row>");
        str.append("FECHA:");
        str.append(this.getTime("-"));
        str.append("</row>").append("\n");
        str.append("<row>");
        str.append("DESCRIPCION:");
        str.append(this.getTextError());
        str.append("</row>").append("\n");
        str.append("<row>");
        str.append("TIPO:");
        str.append(this.getTypeError());
        str.append("</row>").append("\n");
        if (this.seeStringData()){
            if ((this.stringData!=null) && (!"".equals(this.stringData))) {
                str.append("<row>");
                str.append("DATA ADICIONAL:");
                str.append(this.getStringData());
                str.append("</row>").append("\n");
            }
        }
        str.append("</error>");
        this.setXmlError(str);
    }

    private void getStackTrace(StackTraceElement[] stack, Class clase) {
        boolean buscarPadre = true;
        boolean buscarHijo = false;
        boolean seguir = true;

        if (stack !=null){
            String strClase = clase.getCanonicalName();
            for (int i=0;i<stack.length && seguir;i++){
                StackTraceElement st = stack[i];
                if (buscarPadre){
                    if (st.getClassName().equals(strClase)){
                        buscarPadre = false;
                        buscarHijo = true;
                        Dato dato = new Dato();
                        dato.setNameClass(st.getClassName());
                        dato.setMethodClass(st.getMethodName());
                        dato.setFileClass(st.getFileName());
                        dato.setLineNumber(st.getLineNumber());
                        arrClass.add(dato);
                    }
                }else{
                    if (buscarHijo){
                        if (st.getClassName().contains(packageProy)){
                            Dato dato = new Dato();
                            dato.setNameClass(st.getClassName());
                            dato.setMethodClass(st.getMethodName());
                            dato.setFileClass(st.getFileName());
                            dato.setLineNumber(st.getLineNumber());
                            arrClass.add(dato);
                        }else{
                            seguir = false;
                        }
                    }
                }
            }
        }
    }

    private StringBuffer getStringSecuenceError(){
        StringBuffer sld = new StringBuffer();
        sld.append("CLASE\t,METODO\t,LINEA\t,ARCHIVO\n");
        if ((arrClass!=null) && (!arrClass.isEmpty())){

            for (int i=0;i<arrClass.size();i++){
                Dato dto = (Dato) arrClass.get(i);
                sld.append(dto.getFileClass()).append("\t,");
                sld.append(dto.getLineNumber()).append("\t,");
                sld.append(dto.getMethodClass()).append("\t,");
                sld.append(dto.getNameClass()).append("\n");
            }
        }
        return sld;
    }

    /******** GETTER AND SETTER ***********/

    public boolean seeStringData() {
        return seeStringData;
    }

    public void setSeeStringData(boolean seeStringData) {
        this.seeStringData = seeStringData;
    }

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    public boolean isLogFile() {
        return logFile;
    }

    public void setLogFile(boolean logFile) {
        this.logFile = logFile;
    }

    public ArrayList getArrayData() {
        return arrayData;
    }

    public void setArrayData(ArrayList arrayData) {
        this.arrayData = arrayData;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        String str = this.stringData;
        if (str!=null){
            if (stringData!=null){
                str = str + "\n" + stringData;
            }
        }else{
            if (stringData!=null){
                str = stringData;
            }
        }
        this.stringData = str;
    }

    public StringBuffer getTypeError() {
        return typeError;
    }

    private void setTypeError(String typeError) {
        this.typeError.append(typeError);
    }

    public String getTime() {
        return time;
    }

    public String getTime(String separador) {
        String strTime = time.replaceAll("/", separador);
        return strTime;
    }

    private void setTime(String time) {
        this.time = time;
    }

    public StringBuffer getSecuenceError() {
        return secuenceError;
    }

    private void setSecuenceError(String secuenceError) {
        this.secuenceError.append(secuenceError);
    }

    public StringBuffer getTextError() {
        return textError;
    }

    private void setTextError(String textError) {
        this.textError.append(textError);
    }

    public StringBuffer getTextMessage() {
        return textMessage;
    }

    private void setTextMessage(String textMessage) {
        this.textMessage.append(textMessage);
    }

    public StringBuffer getXmlError() {
        setTextToXmlError();
        return xmlError;
    }

    private void setXmlError(StringBuffer xmlError) {
        this.xmlError = xmlError;
    }

    private class writeLogError{

        private String rutaFile;
        private String nameFile;

        public String getNameFile() {
            return nameFile;
        }

        public void setNameFile(String nameFile) {
            this.nameFile = nameFile;
        }

        public String getRutaFile() {
            return rutaFile;
        }

        public void setRutaFile(String rutaFile) {
            this.rutaFile = rutaFile;
        }

        public boolean guardarArchivo(StringBuffer strEntrada) throws IOException{
            boolean sld = true;
            FileWriter w = null;
            try{
                w = new FileWriter(rutaFile + "/" + nameFile, true);
                w.append(strEntrada.toString());
            }catch (IOException eFile){
                eFile.printStackTrace();
                sld = false;
            }catch(Exception e){
                e.printStackTrace();
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


    private class Dato{
        String nameClass = "";
        String methodClass = "";
        String fileClass = "";
        int lineNumber = 0;

        public Dato() {
        }

        public String getFileClass() {
            return fileClass;
        }

        public void setFileClass(String fileClass) {
            this.fileClass = fileClass;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String getMethodClass() {
            return methodClass;
        }

        public void setMethodClass(String methodClass) {
            this.methodClass = methodClass;
        }

        public String getNameClass() {
            return nameClass;
        }

        public void setNameClass(String nameClass) {
            this.nameClass = nameClass;
        }
    }

    private class UtilDate{

        private int dia;
        private int mes;
        private int anio;
        private int hour;
        private int min;
        private int sec;
        private String separador="/";

        /**
        * Constructor donde se inicializa con el calculo del momento los datos de
        * la clase
        */
        public UtilDate() {
            java.util.Calendar now = java.util.Calendar.getInstance();
            this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
            this.mes = now.get(java.util.Calendar.MONTH);
            this.anio = now.get(java.util.Calendar.YEAR);
            this.hour = now.get(java.util.Calendar.HOUR);
            this.min = now.get(java.util.Calendar.MINUTE);
            this.sec = now.get(java.util.Calendar.SECOND);
        }

        /**
        * Constructor donde se inicializa con los parametros de entrada, los datos
        * de la clase
        * @param dia
        * @param mes
        * @param anio
        */
        public UtilDate(int dia, int mes, int anio) {
            this.dia = dia;
            this.mes = mes;
            this.anio = anio;
            this.hour = 0;
            this.min = 0;
            this.sec = 0;
        }

        /**
        * Constructor donde se inicializa con los parametros de entreda los datos
        * de la clase
        * @param dia
        * @param mes
        * @param anio
        * @param hour
        * @param min
        * @param sec
        */
        public UtilDate(int dia, int mes, int anio, int hour, int min, int sec) {
            this.dia = dia;
            this.mes = mes;
            this.anio = anio;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
        }

        /**
        * Metodo que recarga los datos de la clase con el calculo del momento
        */
        public void recargaFecha(){
            java.util.Calendar now = java.util.Calendar.getInstance();
            this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
            this.mes = now.get(java.util.Calendar.MONTH);
            this.anio = now.get(java.util.Calendar.YEAR);
            this.hour = now.get(java.util.Calendar.HOUR);
            this.min = now.get(java.util.Calendar.MINUTE);
            this.sec = now.get(java.util.Calendar.SECOND);
        }

        /**
        * Entrega la fecha que contiene la clase en formato DD/MM/AAAA
        * @return
        */
        public String getFecha(){
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);

            if (this.dia<10){
            strDia = "0"+strDia;
            }
            if (this.mes<10){
            strMes = "0"+strMes;
            }
            return strDia+separador+strMes+separador+strAnio;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato DD/MM/AAAA hh:mm:ss
        * @return
        */
        public String getFechaHMS(){
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String strHour = String.valueOf(this.hour);
            String strMin = String.valueOf(this.min);
            String strSec = String.valueOf(this.sec);

            if (this.dia<10){
            strDia = "0"+strDia;
            }
            if (this.mes<10){
            strMes = "0"+strMes;
            }
            if (this.hour<10){
            strHour = "0"+strHour;
            }
            if (this.min<10){
            strMin = "0"+strMin;
            }
            if (this.sec<10){
            strSec = "0"+strSec;
            }
            return strDia+separador+strMes+separador+strAnio+separador+
            " "+strHour+":"+strMin+":"+strSec;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato solicitado
        * AMD=AAAA/MM/DD, DMA=DD/MM/AAAA
        * @param frm
        * @return
        */
        public String getFecha(formato frm){
            String sld = "";
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);

            if (this.dia<10){
            strDia = "0"+strDia;
            }
            if (this.mes<10){
            strMes = "0"+strMes;
            }
            if (frm.equals(frm.AMD)){
            sld = strAnio+separador+strMes+separador+strDia;
            }else{
            sld = strDia+separador+strMes+separador+strAnio;
            }
            return sld;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato solicitado
        * AMD=AAAA/MM/DD hh:mm:ss, DMA=DD/MM/AAAA hh:mm:ss
        * @param frm
        * @return
        */
        public String getFechaHMS(formato frm){
            String sld = "";
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String strHour = String.valueOf(this.hour);
            String strMin = String.valueOf(this.min);
            String strSec = String.valueOf(this.sec);

            if (this.dia<10){
            strDia = "0"+strDia;
            }
            if (this.mes<10){
            strMes = "0"+strMes;
            }
            if (this.hour<10){
            strHour="0"+strHour;
            }
            if (this.min<10){
            strMin="0"+strMin;
            }
            if (this.sec<10){
            strSec = "0"+strSec;
            }
            if (frm.equals(frm.AMD)){
            sld = strAnio+separador+strMes+separador+strDia;
            }else{
            sld = strDia+separador+strMes+separador+strAnio;
            }
            sld = sld+" "+strHour+":"+strMin+":"+strSec;
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, en formato DD/MM/AAAA,
        * reemplazando el caracter / por el solicitado
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFecha(String separador){
            String sld = getFecha();
            sld = sld.replace("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, en formato DD/MM/AAAA hh:mm:ss,
        * reemplazando el caracter / por el solicitado
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFechaHMS(String separador){
            String sld = getFechaHMS();
            sld = sld.replace("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, con el formato solicitado y
        * reemplazando el caracter / por el entregado
        * @param frm   Formato que debe poseer la fecha
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFecha(formato frm, String separador){
            String sld = getFecha(frm);
            sld = sld.replaceAll("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, incluyendo la hora, minutos y
        * segundos, con el formato solicitado y reemplazando el caracter / por el
        * entregado
        * @param frm   Formato que debe poseer la fecha
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFechaHMS(formato frm, String separador){
            String sld = getFechaHMS(frm);
            sld = sld.replaceAll("/", separador);
            return sld;
        }
    }
}