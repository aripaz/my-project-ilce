/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import java.math.BigDecimal;
import mx.ilce.handler.ExecutionHandler;

/**
 *
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

    public abstract ExecutionHandler ingresarEntidad(Object data);
    public abstract ExecutionHandler editarEntidad(Object data);
    public abstract ExecutionHandler eliminarEntidad(Object data);
    public abstract Entidad mostrarForma();
    public abstract Entidad mostrarResultado();
    public abstract Entidad ingresarBusquedaSencilla();
    public abstract Entidad ingresarBusquedaAvanzada();
}
