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

/**
 * Clase implementada para obtener datos de tipo horario
 * @author ccatrilef
 */
 public class UtilTime {

   /**
    * Obtiene la hora y minuto del instante en que es invocada
    * @return
    */
    public String getHourMinute(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);

        String str = Integer.toString(h) + ":" + Integer.toString(m);
        return str;
    }

    /**
     * Obtiene la hora, minuto y segundo del instante en que es invocada
     * @return
     */
    public String getHourMinuteSecond(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);
        int s = now.get(java.util.Calendar.SECOND);

        String str = Integer.toString(h) + ":" + Integer.toString(m) + ":" +Integer.toString(s);
        return str;
    }

    /**
     * Obtiene la hora del instante en que es invocada
     * @return
     */
    public String getHour(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);

        String str = Integer.toString(h);
        return str;
    }

    /**
     * Obtiene el minuto del instante en que es invocada
     * @return
     */
    public String getMinute(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int m = now.get(java.util.Calendar.MINUTE);

        String str = Integer.toString(m);
        return str;
    }
}
