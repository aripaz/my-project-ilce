/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.util;

import java.math.BigDecimal;

/**
 *
 * @author vaio
 */
public class UtilValue {

    public String NVL(String strData){
        return (strData==null)?"":strData.trim();
    }

    public String castObject(Object obj, String type){
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
}
