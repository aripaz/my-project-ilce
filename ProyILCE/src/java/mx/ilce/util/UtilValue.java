package mx.ilce.util;

import java.math.BigDecimal;

/**
 *  Clase implementada para manejar los valores de los datos
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
     * COnvierte un object a alguno de los tipos soportados. Los tipos soportados
     * son varchar, number y date
     * @param obj   Objeto que debe ser convertido
     * @param type  Typo al que se desea conenvertir
     * @return
     */
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
