/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.math.BigDecimal;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 * Clase abstracta que define las caracteristicas que deben poseer las
 * clases que heredaran de ella
 * @author ccatrilef
 */
public abstract class Entidad {

    private BigDecimal idEntidad;
    private StringBuffer xmlEntidad;
    private Object data;

    public BigDecimal getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(BigDecimal idEntidad) {
        this.idEntidad = idEntidad;
    }

    public StringBuffer getXmlEntidad() {
        return xmlEntidad;
    }

    public void setXmlEntidad(StringBuffer xmlEntidad) {
        this.xmlEntidad = xmlEntidad;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public abstract ExecutionHandler ingresarEntidad(Object data) throws ExceptionHandler;
    public abstract ExecutionHandler editarEntidad(Object data) throws ExceptionHandler;
    public abstract ExecutionHandler eliminarEntidad(Object data) throws ExceptionHandler;
    public abstract Entidad mostrarForma() throws ExceptionHandler;
    public abstract Entidad mostrarResultado() throws ExceptionHandler;
    public abstract Entidad ingresarBusquedaSencilla() throws ExceptionHandler;
    public abstract Entidad ingresarBusquedaAvanzada() throws ExceptionHandler;
}
