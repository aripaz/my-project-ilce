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
