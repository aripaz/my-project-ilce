/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import mx.ilce.handler.ExecutionHandler;

/**
 *  Clase pra la implementacion de los metodos asociados a la aplicacion
 * @author ccatrilef
 */
public class Aplicacion extends Entidad {

    private Integer claveAplicacion;
    private String aplicacion;
    private Integer claveFormaPrincipal;
    private String descripcion;
    private String aliasMenuNuevaEntidad;
    private String aliasMenuMostrarEntidad;

/********* GETTER Y SETTER *********/

    public String getAliasMenuMostrarEntidad() {
        return aliasMenuMostrarEntidad;
    }

    public void setAliasMenuMostrarEntidad(String aliasMenuMostrarEntidad) {
        this.aliasMenuMostrarEntidad = aliasMenuMostrarEntidad;
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

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public Integer getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    public void setClaveFormaPrincipal(Integer claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

/******* OPERACIONES DE ENTIDAD ******/

    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Aplicacion mostrarForma() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion mostrarResultado() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }
    
    public Aplicacion ingresarBusquedaSencilla() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion ingresarBusquedaAvanzada() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion mostrarPanel(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion guardarBusqueda(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion rescatarBusqueda(){
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    public Aplicacion eliminarBusqueda() {
        try{

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return this;
    }

    
}
