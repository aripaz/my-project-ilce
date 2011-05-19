/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.util.List;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 *  Clase para la implementacion de los metodos asociados al menu
 *
 * @author ccatrilef
 */
public class Menu extends Entidad {

    private StringBuffer xmlMenu;

    private StringBuffer getXmlMenu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void setXmlMenu(StringBuffer xmlMenu) {
        this.xmlMenu = xmlMenu;
    }

    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Menu mostrarForma() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Menu mostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Menu ingresarBusquedaSencilla() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Menu ingresarBusquedaAvanzada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Menu crearMenuN() throws ExceptionHandler{
        try{
            ConEntidad con = new ConEntidad();
            List lst = con.obtieneMenu();
            AdminXML admXml = new AdminXML();
            this.setData(getXmlMenu());
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para crear el XML de Menu");
        }finally{

        }
        return this;
    }
}
