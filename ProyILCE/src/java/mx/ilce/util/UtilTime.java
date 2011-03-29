/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.util;

/**
 *
 * @author ccatrilef
 */
 public class UtilTime {

    public String getHourMinute(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);

        String str = Integer.toString(h) + ":" + Integer.toString(m);
        return str;
    }

    public String getHourMinuteSecond(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);
        int s = now.get(java.util.Calendar.SECOND);

        String str = Integer.toString(h) + ":" + Integer.toString(m) + ":" +Integer.toString(s);
        return str;
    }

    public String getHour(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);

        String str = Integer.toString(h);
        return str;
    }

    public String getMinute(){
        java.util.Calendar now = java.util.Calendar.getInstance();
        int m = now.get(java.util.Calendar.MINUTE);

        String str = Integer.toString(m);
        return str;
    }
}
