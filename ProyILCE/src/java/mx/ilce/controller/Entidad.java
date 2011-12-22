/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
package mx.ilce.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;

/**
 * Clase abstracta que define las características que deben poseer las
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
     * @return  Objeto      Objeto del tipo Object(acepta cualquier objeto)
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
     * @param data  Objeto del tipo Object(acepta cualquier objeto)
     */
    public abstract ExecutionHandler ingresarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para el ingreso de una entidad
     * @param data  Objeto del tipo Object(acepta cualquier objeto)
     */
    public abstract ExecutionHandler editarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para la eliminación de entidad
     * @param data      Data para la ubicacion de la Entidad
     * @return  ExecutionHandler    Resultado de la operación
     * @throws ExceptionHandler
     */
    public abstract ExecutionHandler eliminarEntidad(Object data) throws ExceptionHandler;

    /**
     * Método Abstracto para el despliegue de las formas
     * @return  Entidad     Forma obtenida
     * @throws ExceptionHandler
     */
    public abstract Entidad mostrarForma() throws ExceptionHandler;

    /**
     * Método Abstracto para mostrar el resultado
     * @return  Entidad     Resultado obtenido
     * @throws ExceptionHandler
     */
    public abstract Entidad mostrarResultado() throws ExceptionHandler;

    /**
     * Método Abstracto para la realización de búsquedas sencillas
     * @return  Entidad     Resultado de la búsqueda
     * @throws ExceptionHandler
     */
    public abstract Entidad ingresarBusquedaSencilla() throws ExceptionHandler;

    /**
     * Método Abstracto para el ingreso de búsquedas avanzadas
     * @return  Entidad     Resultado de la búsqueda
     * @throws ExceptionHandler
     */
    public abstract Entidad ingresarBusquedaAvanzada() throws ExceptionHandler;
}
