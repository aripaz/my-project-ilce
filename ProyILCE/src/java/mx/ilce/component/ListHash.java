/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 *
 * @author ccatrilef
 */
public class ListHash {

    private HashMap lista;

    public boolean insertListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        this.lista.put(clave, dato);
        return bln;
    }

    public boolean deleteListHash(BigDecimal clave){
        boolean bln=true;
        return bln;
    }

    public boolean updateListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        return bln;
    }

    public Object getObjectHash(BigDecimal clave){
        String str="";
        return str;
    }
}
