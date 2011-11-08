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
    public enum formato {DMA,AMD};

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
        return strDia+separador+strMes+separador+strAnio+separador+
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
    public boolean isFechaValida(String fechax) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechax);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
