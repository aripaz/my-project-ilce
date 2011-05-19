/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ccatrilef
 */
public class LogHandler {

    private String rutaFile;
    private enum formato {DMA,AMD};
    private String dateFile;
    private String time;
    private StringBuffer textMessage = new StringBuffer("");
    private StringBuffer textData = new StringBuffer("");

    public LogHandler() {
        UtilDate ut = new UtilDate();
        setTime(ut.getFechaHMS());
        setDateFile(ut.getFecha(formato.AMD,""));
    }

    public boolean logData(String rutaFile,StringBuffer textMessage, StringBuffer textData){
        boolean sld =false;
        setRutaFile(rutaFile);
        setTextData(textData);
        setTextMessage(textMessage);
        if (this.rutaFile!=null){
            sld = writeToFile();
        }
        return sld;
    }

    private String getDateFile() {
        return dateFile;
    }

    private void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    private StringBuffer getTextData() {
        return textData;
    }

    private void setTextData(StringBuffer textData) {
        this.textData = textData;
    }

    private StringBuffer getTextMessage() {
        return textMessage;
    }

    private void setTextMessage(StringBuffer textMessage) {
        this.textMessage = textMessage;
    }

    private String getTime() {
        return time;
    }

    private void setTime(String time) {
        this.time = time;
    }

    private String getRutaFile() {
        return rutaFile;
    }

    private void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    private boolean writeToFile(){
        boolean sld = true;
        String strNameFile = "";
        try{
            File directorio = new File(this.rutaFile);
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
            strTexto.append("\nLOG DATA:\n").append(this.getTextData());
            strTexto.append("\n****************\n");
            if (sld){
                sld = guardarArchivo(strTexto, strNameFile);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sld;
    }

    private boolean guardarArchivo(StringBuffer strEntrada, String nameFile) throws IOException{
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
