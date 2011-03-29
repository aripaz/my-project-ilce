/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.util;

/**
 *
 * @author vaio
 */
public class Validation {

    public boolean isPar(String data){
        int i =  Integer.parseInt(data) ;
        int j = i%2;
        return ((j==0)?true:false);
    }
}
