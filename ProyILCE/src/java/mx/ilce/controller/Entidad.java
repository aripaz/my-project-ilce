package mx.ilce.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 * Clase abstracta que define las caracteristicas que deben poseer las
 * clases que heredaran de ella
 * @author ccatrilef
 */
public abstract class Entidad implements Serializable{

    private BigDecimal idEntidad;
    private StringBuffer xmlEntidad;
    private Object data;

    /**
     * Obtiene el ID de la Entidad
     * @return
     */
    public BigDecimal getIdEntidad() {
        return idEntidad;
    }

    /**
     * Asigna el ID de la entidad
     * @param idEntidad     ID de la entidad
     */
    public void setIdEntidad(BigDecimal idEntidad) {
        this.idEntidad = idEntidad;
    }

    /**
     * Obtiene el XMLEntidad
     * @return
     */
    public StringBuffer getXmlEntidad() {
        return xmlEntidad;
    }

    /**
     * Asigna el XML de la Entidad
     * @param xmlEntidad    Texto XML a signar
     */
    public void setXmlEntidad(StringBuffer xmlEntidad) {
        this.xmlEntidad = xmlEntidad;
    }

    /**
     * Obtiene la data de la Entidad
     * @return
     */
    public Object getData() {
        return data;
    }

    /***
     * Asigna la data a la Entidad
     * @param data  Data a asignar
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Método Abstracto para el ingreso de una entidad
     * @param data
     */
    public abstract ExecutionHandler ingresarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para el ingreso de una entidad
     * @param data
     */
    public abstract ExecutionHandler editarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para la eliminación de entidad
     * @param data      Data para la ubicacion de la Entidad
     * @return
     * @throws ExceptionHandler
     */
    public abstract ExecutionHandler eliminarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para el despliegue de las formas
     * @return
     * @throws ExceptionHandler
     */
    public abstract Entidad mostrarForma() throws ExceptionHandler;

    /**
     * Método Abstracto para mostrar el resultado
     * @return
     * @throws ExceptionHandler
     */
    public abstract Entidad mostrarResultado() throws ExceptionHandler;

    /**
     * Método Abstracto para la realizacién de busquedas sencillas
     * @return
     * @throws ExceptionHandler
     */
    public abstract Entidad ingresarBusquedaSencilla() throws ExceptionHandler;

    /**
     * Método Abstracto para el ingreso de busquedas avanzadas
     * @return
     * @throws ExceptionHandler
     */
    public abstract Entidad ingresarBusquedaAvanzada() throws ExceptionHandler;
}
