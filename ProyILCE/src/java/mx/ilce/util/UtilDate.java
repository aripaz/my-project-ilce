/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.util;

/**
 *
 * @author ccatrilef
 */
public class UtilDate{

    private int dia;
    private int mes;
    private int anio;
    private String separador="/";
    public enum formato {DMA,AMD};

    public UtilDate() {
        java.util.Calendar now = java.util.Calendar.getInstance();
        this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
        this.mes = now.get(java.util.Calendar.MONTH);
        this.anio = now.get(java.util.Calendar.YEAR);
    }

    public UtilDate(int dia, int mes, int anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

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

    public String getFecha(String separador){
        String sld = getFecha();
        sld = sld.replace("/", separador);
        return sld;
    }

    public String getFecha(formato frm, String separador){
        String sld = getFecha(frm);
        sld = sld.replaceAll("/", separador);
        return sld;
    }

}
