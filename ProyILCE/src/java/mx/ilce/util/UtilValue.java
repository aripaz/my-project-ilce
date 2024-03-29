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
package mx.ilce.util;

import java.math.BigDecimal;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase implementada para manejar los valores de los datos
 * @author ccatrilef
 */
public class UtilValue {

    /**
     * Entrega "" en caso que un String sea NULL
     * @param strData
     * @return
     */
    public String NVL(String strData){
        return (strData==null)?"":strData.trim();
    }

    /**
     * Convierte un object a alguno de los tipos soportados. Los tipos soportados
     * son varchar, number y date
     * @param obj   Objeto que debe ser convertido
     * @param type  Tipo al que se desea conenvertir
     * @return
     */
    public static String castObject(Object obj, String type){
        String strSld = "";
        if (obj != null){
            if (type.equals("varchar")){
                strSld = String.valueOf(obj);
            }else if(type.equals("number")){
                BigDecimal bg = new BigDecimal(String.valueOf(obj));
                strSld = bg.toString();
            }else if (type.equals("date")){
                strSld = String.valueOf(obj);
            }
        }
        return strSld.trim();
    }

    /**
     * Método que reemplaza en un texto los caracteres especiales de HTML al
     * que les corresponde
     * @param data      Texto a revisar
     * @return  String  Texto transformado
     * @throws ExceptionHandler
     */
    public static String replaceHtml(String data) throws ExceptionHandler{
        String str = "";
        if (data!=null){
            str=data;
            if (str.contains("&aacute;")){
                str = str.replaceAll("&aacute;","á");
            }
            if (str.contains("&Aacute;")){
                str = str.replaceAll("&Aacute;","Á");
            }
            if(str.contains("&eacute;")){
                str = str.replaceAll("&eacute;","é" );
            }
            if(str.contains("&Eacute;")){
                str = str.replaceAll("&Eacute;","É" );
            }
            if(str.contains("&iacute;")){
                str = str.replaceAll("&iacute;","í");
            }
            if(str.contains("&Iacute;")){
                str = str.replaceAll("&Iacute;","Í");
            }
            if(str.contains("&oacute;")){
                str = str.replaceAll("&oacute;","ó");
            }
            if(str.contains("&Oacute;")){
                str = str.replaceAll("&Oacute;","Ó");
            }
            if(str.contains("&uacute;")){
                str = str.replaceAll("&uacute;","ú");
            }
            if(str.contains("&Uacute;")){
                str = str.replaceAll("&Uacute;","Ú");
            }
            if(str.contains("&ntilde;")){
                str = str.replaceAll("&ntilde;","ñ");
            }
            if(str.contains("&Ntilde;")){
                str = str.replaceAll("&Ntilde;","Ñ");
            }
            str = str.trim();
        }
        return str;
    }

    /**
     * Método para convertir un texto de una query, para que los caracteres sean
     * aceptados por la Base de Datos
     * @param data      Texto a revisar
     * @return  StringBuffer    Texto transformado
     * @throws ExceptionHandler
     */
    public static String castAcent(String data) throws ExceptionHandler {
        String str = "";
        if (data!=null){
            String paso=data;
            if (paso.contains("Á")){
                paso=paso.replaceAll("Á","'+char(193)+'");
            }
            if (paso.contains("É")){
                paso=paso.replaceAll("É","'+char(201)+'");
            }
            if (paso.contains("Í")){
                paso=paso.replaceAll("Í","'+char(205)+'");
            }
            if (paso.contains("Ñ")){
                paso=paso.replaceAll("Ñ","'+char(209)+'");
            }
            if (paso.contains("Ó")){
                paso=paso.replaceAll("Ó","'+char(211)+'");
            }
            if (paso.contains("Ú")){
                paso=paso.replaceAll("Ú","'+char(218)+'");
            }
            if (paso.contains("Ü")){
                paso=paso.replaceAll("Ü","'+char(220)+'");
            }
            if (paso.contains("á")){
                paso=paso.replaceAll("á","'+char(225)+'");
            }
            if (paso.contains("é")){
                paso=paso.replaceAll("é","'+char(233)+'");
            }
            if (paso.contains("í")){
                paso=paso.replaceAll("í","'+char(237)+'");
            }
            if (paso.contains("ñ")){
                paso=paso.replaceAll("ñ","'+char(241)+'");
            }
            if (paso.contains("ó")){
                paso=paso.replaceAll("ó","'+char(243)+'");
            }
            if (paso.contains("ú")){
                paso=paso.replaceAll("ú","'+char(250)+'");
            }
            if (paso.contains("ü")){
                paso=paso.replaceAll("ú","'+char(252)+'");
            }
            str=paso;
        }else{
            str=data;
        }
        str = replaceHtml(str);
        str = str.replaceAll("\\%2B", "+");
        return str;
    }
}
