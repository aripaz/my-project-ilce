package mx.ilce.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Clase encargada de calcular la fecha, incluyendo las horas, minutos y
 * segundos, en distintos formatos y con distintos caracteres de separación
 * @author ccatrilef
 */
public class UtilDate{

    private int dia;
    private int mes;
    private int anio;
    private int hour;
    private int min;
    private int sec;
    private String separador="/";
    /**
     * Definición de los formatos de Fecha aceptados.
     * (-) DMA= Día Mes Año
     * (-) AMD= Año Mes Día
     */
    public enum formato {DMA,AMD,AMDhms,DMAhms,AMDhmspp,DMAhmspp};

    /**
     * Constructor donde se inicializan los parámetros con los datos del cálculo
     * del momento (dia y hora) en que se recibe la solicitud
     */
    public UtilDate() {
        java.util.Calendar now = java.util.Calendar.getInstance();
        this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
        this.mes = now.get(java.util.Calendar.MONTH)+1;
        this.anio = now.get(java.util.Calendar.YEAR);
        this.hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        this.min = now.get(java.util.Calendar.MINUTE);
        this.sec = now.get(java.util.Calendar.SECOND);
    }

    public UtilDate(String fecha, String tokenFormato){
        String strAnio = "";
        String strMes = "";
        String strDia = "";
        String strHora = "";
        String strMinuto = "";
        String strSegundo = "";
        String strPeriodo = "";

        String fechaTrab = fecha;
        if (tokenFormato==null){
            tokenFormato = "";
        }
        int posMesAux = tokenFormato.indexOf("ME");
        int posAnio = tokenFormato.indexOf("AAAA");
        int posMes = tokenFormato.indexOf("MM");
        int posDia = tokenFormato.indexOf("DD");
        int posHora = tokenFormato.indexOf("hh");
        int posMin = tokenFormato.indexOf("mm");
        int posSeg = tokenFormato.indexOf("ss");
        int posPer = tokenFormato.indexOf("PP");
        int largo = fecha.length();

        try{
            if (posMesAux>=0){
                fechaTrab = traduceMonthToString(fechaTrab);
                largo = fechaTrab.length();
            }
            if ((posMes>=0) && (largo>=posMes+2)){
                strMes = fechaTrab.substring(posMes,posMes+2);
            }else{
                if (posMesAux>0){
                    strMes = fechaTrab.substring(posMesAux,posMesAux+2);
                }
            }
            if ((posAnio>=0) && (largo>=posAnio+4)){
                strAnio = fechaTrab.substring(posAnio,posAnio+4);
            }else{
                posAnio = tokenFormato.indexOf("AA");
                if ((posAnio>=0) && (largo>=posAnio+2)){
                    strAnio = fechaTrab.substring(posAnio,posAnio+2);
                }
            }
            if ((posDia>=0) && (largo>=posDia+2)){
                strDia = fechaTrab.substring(posDia,posDia+2);
            }
            if ((posHora>=0) && (largo>=posHora+2)){
                strHora = fechaTrab.substring(posHora,posHora+2);
            }
            if ((posMin>=0) && (largo>=posMin+2)){
                strMinuto = fechaTrab.substring(posMin,posMin+2);
            }
            if ((posSeg>=0) && (largo>=posSeg+2)){
                strSegundo = fechaTrab.substring(posSeg,posSeg+2);
            }
            if ((posPer>=0) && (largo>=posPer+2)){
                strPeriodo = fechaTrab.substring(posPer,posPer+2);
            }
        }catch (Exception e){

        }
        String str = strDia+"/"+strMes+"/"+strAnio;
        if (isFechaValida(str)){
            this.anio = Integer.parseInt(strAnio);
            this.mes = Integer.parseInt(strMes);
            this.dia = Integer.parseInt(strDia);

            if (strPeriodo.equals("PM")){
                if (!strHora.equals("")){
                    this.hour = Integer.parseInt(strHora)+12;
                    if (this.hour == 24){
                        this.hour = 0;
                    }
                }
            }else{
                if (!strHora.equals("")){
                    this.hour = Integer.parseInt(strHora);
                }
            }
            if (!strMinuto.equals("")){
                this.min = Integer.parseInt(strMinuto);
            }
            if (!strSegundo.equals("")){
                this.sec = Integer.parseInt(strSegundo);
            }
        }else{
            strDia="00";
            strMes="00";
            strAnio="0000";
            this.anio = Integer.parseInt(strAnio);
            this.mes = Integer.parseInt(strMes);
            this.dia = Integer.parseInt(strDia);
        }

    }

    /**
     * Constructor donde se inicializa con los parámetros de entrada, los datos
     * de la clase
     * @param dia    Dato con el dia a asignar
     * @param mes    Dato con el mes a asignar
     * @param anio   Dato con el año a asignar
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
     * Constructor donde se inicializan los parámetros con los datos de la fecha
     * contenida en el objeto Date, que se recibe en la solicitud
     * @param date
     */
    public UtilDate(Date date){
        java.util.Calendar now = java.util.Calendar.getInstance();
        now.setTime(date);
        this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
        this.mes = now.get(java.util.Calendar.MONTH)+1;
        this.anio = now.get(java.util.Calendar.YEAR);
        this.hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        this.min = now.get(java.util.Calendar.MINUTE);
        this.sec = now.get(java.util.Calendar.SECOND);
    }

    /**
     * Constructor donde se inicializa con los parámetros de entrada los datos
     * de la clase
     * @param dia    Dato con el dia a asignar
     * @param mes    Dato con el mes a asignar
     * @param anio   Dato con el año a asignar
     * @param hour   Dato con la hora a asignar
     * @param min    Dato con el minuto a asignar
     * @param sec    Dato con el segundo a asignar
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
     * Método que recarga los datos de la clase con el cálculo del momento
     */
    public void recargaFecha(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
        this.mes = now.get(java.util.Calendar.MONTH)+1;
        this.anio = now.get(java.util.Calendar.YEAR);
        this.hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        this.min = now.get(java.util.Calendar.MINUTE);
        this.sec = now.get(java.util.Calendar.SECOND);
    }

    /**
     * Método que entrega la fecha que contiene la clase en formato DD/MM/AAAA
     * @return
     */
    public String getFecha(){
        String strDia = String.valueOf(this.dia);
        String strMes = String.valueOf(this.mes);
        String strAnio = String.valueOf(this.anio);
        String str = strDia+"/"+strMes+"/"+strAnio;
        if (isFechaValida(str)){
            if (this.dia<10){
                strDia = "0"+strDia;
            }
            if (this.mes<10){
                strMes = "0"+strMes;
            }
        }else{
            strDia="00";
            strMes="00";
            strAnio="0000";
        }
        return strDia+separador+strMes+separador+strAnio;
    }

    /**
     * Método que entrega la fecha que contiene la clase en el formato DD/MM/AAAA hh:mm:ss
     * @return
     */
    public String getFechaHMS(){
        String strDia = String.valueOf(this.dia);
        String strMes = String.valueOf(this.mes);
        String strAnio = String.valueOf(this.anio);
        String strHour = String.valueOf(this.hour);
        String strMin = String.valueOf(this.min);
        String strSec = String.valueOf(this.sec);
        String str = strDia+"/"+strMes+"/"+strAnio;
        if (isFechaValida(str)){
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
        }else{
            strDia="00";
            strMes="00";
            strAnio="0000";
            strHour="00";
            strMin="00";
            strSec="00";
        }
        return strDia+separador+strMes+separador+strAnio+
                " "+strHour+":"+strMin+":"+strSec;
    }

    /**
     * Método que entrega la fecha que contiene la clase en el formato solicitado
     * AMD=AAAA/MM/DD, DMA=DD/MM/AAAA
     * @param frm    Formato que se desea para la fecha AMD, DMA
     * @return
     */
    public String getFecha(formato frm){
        String sld = "";
        String strDia = String.valueOf(this.dia);
        String strMes = String.valueOf(this.mes);
        String strAnio = String.valueOf(this.anio);
        String str = strDia+"/"+strMes+"/"+strAnio;
        if (isFechaValida(str)){
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
        }else{
            strDia="00";
            strMes="00";
            strAnio="0000";
        }
        return sld;
    }

    /**
     * Método que entrega la fecha que contiene la clase en el formato solicitado
     * AMD=AAAA/MM/DD hh:mm:ss, DMA=DD/MM/AAAA hh:mm:ss
     * @param frm    Formato que se desea para la fecha AMD, DMA
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
        String str = strDia+"/"+strMes+"/"+strAnio;
        if (isFechaValida(str)){
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
        }else{
            strDia="00";
            strMes="00";
            strAnio="0000";
            strHour="00";
            strMin="00";
            strSec="00";
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
     * Método que entrega la fecha existente en la clase, en formato DD/MM/AAAA,
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
     * Método que entrega la fecha existente en la clase, en formato DD/MM/AAAA hh:mm:ss,
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
     * Método que entrega la fecha existente en la clase, en formato DD/MM/AAAA hh:mm:ss,
     * reemplazando el caracter / por separadorFecha y el caracter : por el
     * separadorHora
     * @param separadorFecha Caracter separador que debe poseer la fecha en vez de /
     * @param separadorHora Caracter separador que debe poseer la hora en vez de :
     * @return
     */
    public String getFechaHMS(String separadorFecha, String separadorHora){
        String sld = getFechaHMS();
        sld = sld.replace("/", separadorFecha);
        sld = sld.replace(":", separadorHora);
        return sld;
    }

    /**
     * Método que entrega la fecha existente en la clase, en formato solicitado,
     * reemplazando el caracter / por separadorFecha y el caracter : por el
     * separadorHora
     * @param separadorFecha Caracter separador que debe poseer la fecha en vez de /
     * @param separadorHora Caracter separador que debe poseer la hora en vez de :
     * @return
     */
    public String getFechaHMS(formato frm, String separadorFecha, String separadorHora){
        String sld = getFechaHMS(frm);
        sld = sld.replace("/", separadorFecha);
        sld = sld.replace(":", separadorHora);
        return sld;
    }

    /**
     * Método que entrega la fecha existente en la clase, con el formato solicitado y
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
     * Método que entrega la fecha existente en la clase, incluyendo la hora, minutos y
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
    
    /**
     * Método para validar si una fecha es correcta
     * @param fechax    Fecha que se va a validar
     * @return
     */
    public final boolean isFechaValida(String fechax) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechax);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Método que traduce los meses de un formato de nombre a uno numérico,
     * tanto en ingles como en español. Los nombres de los meses deben ser
     * nombres completos (Ej:ENERO o JANUARY).
     * @param strFecha
     * @return
     */
    public final String traduceMonthToString(String strFecha){
        String strTrab = "";
        String sld = strFecha;

        strTrab = strFecha.toUpperCase();
        if (strTrab.contains("ENERO")){
            String[] spl = strTrab.split("ENERO");
            if (spl.length==2){
                sld = spl[0] + "01" + spl[1];
            }else{
                sld = spl[0] + "01";
            }
        }else if (strTrab.contains("JANUARY")){
            String[] spl = strTrab.split("JANUARY");
            if (spl.length==2){
                sld = spl[0] + "01" + spl[1];
            }else{
                sld = spl[0] + "01";
            }
        }else if (strTrab.contains("FEBRERO")){
            String[] spl = strTrab.split("FEBRERO");
            if (spl.length==2){
                sld = spl[0] + "02" + spl[1];
            }else{
                sld = spl[0] + "02";
            }
        }else if (strTrab.contains("FEBRUARY")){
            String[] spl = strTrab.split("FEBRUARY");
            if (spl.length==2){
                sld = spl[0] + "02" + spl[1];
            }else{
                sld = spl[0] + "02";
            }
        }else if (strTrab.contains("MARZO")){
            String[] spl = strTrab.split("MARZO");
            if (spl.length==2){
                sld = spl[0] + "03" + spl[1];
            }else{
                sld = spl[0] + "03";
            }
        }else if (strTrab.contains("MARCH")){
            String[] spl = strTrab.split("MARCH");
            if (spl.length==2){
                sld = spl[0] + "03" + spl[1];
            }else{
                sld = spl[0] + "03";
            }
        }else if (strTrab.contains("ABRIL")){
            String[] spl = strTrab.split("ABRIL");
            if (spl.length==2){
                sld = spl[0] + "04" + spl[1];
            }else{
                sld = spl[0] + "04";
            }
        }else if (strTrab.contains("APRIL")){
            String[] spl = strTrab.split("APRIL");
            if (spl.length==2){
                sld = spl[0] + "04" + spl[1];
            }else{
                sld = spl[0] + "04";
            }
        }else if (strTrab.contains("MAYO")){
            String[] spl = strTrab.split("MAYO");
            if (spl.length==2){
                sld = spl[0] + "05" + spl[1];
            }else{
                sld = spl[0] + "05";
            }
        }else if (strTrab.contains("MAY")){
            String[] spl = strTrab.split("MAY");
            if (spl.length==2){
                sld = spl[0] + "05" + spl[1];
            }else{
                sld = spl[0] + "05";
            }
        }else if (strTrab.contains("JUNIO")){
            String[] spl = strTrab.split("JUNIO");
            if (spl.length==2){
                sld = spl[0] + "06" + spl[1];
            }else{
                sld = spl[0] + "06";
            }
        }else if (strTrab.contains("JUNY")){
            String[] spl = strTrab.split("JUNY");
            if (spl.length==2){
                sld = spl[0] + "06" + spl[1];
            }else{
                sld = spl[0] + "06";
            }
        }else if (strTrab.contains("JULIO")){
            String[] spl = strTrab.split("JULIO");
            if (spl.length==2){
                sld = spl[0] + "07" + spl[1];
            }else{
                sld = spl[0] + "07";
            }
        }else if (strTrab.contains("JULY")){
            String[] spl = strTrab.split("JULY");
            if (spl.length==2){
                sld = spl[0] + "07" + spl[1];
            }else{
                sld = spl[0] + "07";
            }
        }else if (strTrab.contains("AGOSTO")){
            String[] spl = strTrab.split("AGOSTO");
            if (spl.length==2){
                sld = spl[0] + "08" + spl[1];
            }else{
                sld = spl[0] + "08";
            }
        }else if (strTrab.contains("AUGUST")){
            String[] spl = strTrab.split("AUGUST");
            if (spl.length==2){
                sld = spl[0] + "08" + spl[1];
            }else{
                sld = spl[0] + "08";
            }
        }else if (strTrab.contains("SEPTIEMBRE")){
            String[] spl = strTrab.split("SEPTIEMBRE");
            if (spl.length==2){
                sld = spl[0] + "09" + spl[1];
            }else{
                sld = spl[0] + "09";
            }
        }else if (strTrab.contains("SEPTEMBER")){
            String[] spl = strTrab.split("SEPTEMBER");
            if (spl.length==2){
                sld = spl[0] + "09" + spl[1];
            }else{
                sld = spl[0] + "09";
            }
        }else if (strTrab.contains("OCTUBRE")){
            String[] spl = strTrab.split("OCTUBRE");
            if (spl.length==2){
                sld = spl[0] + "10" + spl[1];
            }else{
                sld = spl[0] + "10";
            }
        }else if (strTrab.contains("OCTOBER")){
            String[] spl = strTrab.split("OCTOBER");
            if (spl.length==2){
                sld = spl[0] + "10" + spl[1];
            }else{
                sld = spl[0] + "10";
            }
        }else if (strTrab.contains("NOVIEMBRE")){
            String[] spl = strTrab.split("NOVIEMBRE");
            if (spl.length==2){
                sld = spl[0] + "11" + spl[1];
            }else{
                sld = spl[0] + "11";
            }
        }else if (strTrab.contains("NOVEMBER")){
            String[] spl = strTrab.split("NOVEMBER");
            if (spl.length==2){
                sld = spl[0] + "11" + spl[1];
            }else{
                sld = spl[0] + "11";
            }
        }else if (strTrab.contains("DICIEMBRE")){
            String[] spl = strTrab.split("DICIEMBRE");
            if (spl.length==2){
                sld = spl[0] + "12" + spl[1];
            }else{
                sld = spl[0] + "12";
            }
        }else if (strTrab.contains("DECEMBER")){
            String[] spl = strTrab.split("DECEMBER");
            if (spl.length==2){
                sld = spl[0] + "12" + spl[1];
            }else{
                sld = spl[0] + "12";
            }
        }
        return sld;
    }
}
