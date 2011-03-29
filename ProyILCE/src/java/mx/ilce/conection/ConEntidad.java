/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.util.ArrayList;
import java.util.List;
import mx.ilce.bean.Campo;

/**
 *
 * @author ccatrilef
 */
public class ConEntidad {

    public void ingresaEntidad(){

    }

    public void eliminaEntidad(){

    }

    public void actualizaEntidad(){

    }

    public void obtieneEntidad(){

    }

    public List obtieneMenu(){
        List lst = new ArrayList();

        for (int i=0;i<10;i++){
            Campo cmp = new Campo();
            cmp.setNombre("Nombre"+i);
            cmp.setAlias("Alias"+i);
            cmp.setValor(String.valueOf(i));
            lst.add(cmp);
        }
        return lst;
    }
}
