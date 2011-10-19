/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ilce.modelo;
import java.util.ArrayList;


/**
 *
 * @author Daniel
 */
public class Aplicacion {
    private int claveAplicacion;
    private String aplicacion;
    private String aliasMenuNuevaEntidad;
    private String aliasMenuMuestraEntidad;
    private int claveFormaPrincipal;
    
    private ArrayList<Forma> formas = new  ArrayList<Forma>();

    public String getAliasMenuMuestraEntidad() {
        return aliasMenuMuestraEntidad;
    }

    public int getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    public void setClaveFormaPrincipal(int claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    public void setAliasMenuMuestraEntidad(String aliasMenuMuestraEntidad) {
        this.aliasMenuMuestraEntidad = aliasMenuMuestraEntidad;
    }

    public String getAliasMenuNuevaEntidad() {
        return aliasMenuNuevaEntidad;
    }

    public void setAliasMenuNuevaEntidad(String aliasMenuNuevaEntidad) {
        this.aliasMenuNuevaEntidad = aliasMenuNuevaEntidad;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public int getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(int claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }
        public ArrayList<Forma> getFormas() {
        return formas;
    }

    public void setFormas(ArrayList<Forma> formas) {
        this.formas = formas;
    }

    public void pushForma(Forma forma) {
        this.formas.add(forma);
    }
    
    public void popForma(int i) {
        this.formas.remove(i);
    }
}
