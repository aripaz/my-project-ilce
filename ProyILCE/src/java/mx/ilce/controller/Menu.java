/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.util.List;
import mx.ilce.component.AdminXML;
import mx.ilce.handler.ExecutionHandler;

/**
 *
 * @author ccatrilef
 */
public class Menu extends Entidad {

    private StringBuffer xmlMenu;

    private StringBuffer getXmlMenu() {

        StringBuffer str = new StringBuffer("");
        str.append("<h3><a href=\"#\">BackEnd</a></h3>");
        str.append("<div>");
        str.append("<div><a href=\"#\">Nueva aplicación</a></div>");
        str.append("<div><a href=\"#\">Aplicaciones</a></div>");
        str.append("</div>");
        str.append("<h3><a href=\"#\">CRM</a></h3>");
        str.append("<div>");
        str.append("<div><a href=\"#\">Nuevo grupo de interés</a></div>");
        str.append("<div><a href=\"#\">Grupos de interés</a></div>");
        str.append("</div>");
        str.append("<h3><a href=\"#\">Proyectos</a></h3>");
        str.append("<div>");
        str.append("<div><a href=\"#\">Nuevo proyecto</a></div>");
        str.append("<div><a href=\"#\">Proyectos</a></div>");
        str.append("</div>");
        str.append("<h3><a href=\"#\">SRM</a></h3>");
        str.append("<div>");
        str.append("<div><a href=\"#\">Nuevo proveedor</a></div>");
        str.append("<div><a href=\"#\">Proveedores</a></div>");
        str.append("</div>");
        
        return str;
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
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Menu mostrarResultado() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Menu ingresarBusquedaSencilla() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Menu ingresarBusquedaAvanzada() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Menu crearMenu(){
        try{
            ConEntidad con = new ConEntidad();
            List lst = con.obtieneMenu();
            AdminXML admXml = new AdminXML();
            StringBuffer str = admXml.getXMLByList(lst);
            setXmlEntidad(str);
            this.setData(getXmlMenu());

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }
}
