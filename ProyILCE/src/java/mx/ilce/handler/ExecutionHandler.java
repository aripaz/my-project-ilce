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
package mx.ilce.handler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Clase implementada para manejar la ejecución de la aplicación. Posee diversos
 * métodos que facilitan el traslado de mensajes y datos
 * @author ccatrilef
 */
public class ExecutionHandler implements Serializable{

    private BigDecimal idExecution;
    private String titleExecution;
    private String textExecution;
    private boolean executionOK;
    private List listData;
    private Object objectData;

    /**
     * Constructor básico, se inicializan las variables del objeto
     */
    public ExecutionHandler() {
        this.idExecution = new BigDecimal(0);
        this.titleExecution = "";
        this.textExecution = "";
        this.executionOK = true;
    }

    /**
     * Obtiene el Object ingresado al Objeto
     * @return  Object  Objeto ingresado
     */
    public Object getObjectData() {
        return objectData;
    }

    /**
     * Asigna el Object a ingresar al Objeto
     * @param objectData     Objeto ingresado
     */
    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }

    /**
     * Obtiene el ListaData ingresado al Objeto
     * @return  List    Listado del objeto
     */
    public List getListData() {
        return listData;
    }

    /**
     * Asigna el ListData a ingresar al Objeto
     * @param listData  Listado del objeto
     */
    public void setListData(List listData) {
        this.listData = listData;
    }

    /**
     * Indica si la ejecución se realizo de manera correcta (TRUE) o no (FALSE)
     * @return  boolean     Valor TRUE O FALSE de la validación
     */
    public boolean isExecutionOK() {
        return executionOK;
    }

    /**
     * Asigna si la ejecución se realizo de manera correcta (TRUE) o no (FALSE)
     * @param executionOK   Valor TRUE O FALSE de la validación
     */
    public void setExecutionOK(boolean executionOK) {
        this.executionOK = executionOK;
    }

    /**
     * Obtiene el ID de la ejecución
     * @return  BigDecimal  ID de la ejecución
     */
    public BigDecimal getIdExecution() {
        return idExecution;
    }

    /**
     * Asigna el ID de la ejecución
     * @param idExecution   ID de la Ejecución
     */
    public void setIdExecution(BigDecimal idExecution) {
        this.idExecution = idExecution;
    }

    /**
     * Obtiene el texto de respuesta de la ejecución
     * @return  String  Texto de la ejecución
     */
    public String getTextExecution() {
        return textExecution;
    }

    /**
     * Asigna el texto de respuesta de la ejecución
     * @param textExecution     Texto de la ejecución
     */
    public void setTextExecution(String textExecution) {
        this.textExecution = textExecution;
    }

    /**
     * Obtiene el título dado a la ejecución
     * @return  String      Título de la ejecución
     */
    public String getTitleExecution() {
        return titleExecution;
    }

    /**
     * Asigna el título dado a la ejecución
     * @param titleExecution    Título de la ejecución
     */
    public void setTitleExecution(String titleExecution) {
        this.titleExecution = titleExecution;
    }

    /**
     * Método que lleva a un formato String el contenido del objeto
     */
    @Override
    public String toString() {
        return "ExecutionHandler{" 
                + ((idExecution!=null)?"\nidExecution=" + idExecution:"")
                + ((titleExecution!=null)?"\ntitleExecution=" + titleExecution :"")
                + ((textExecution!=null)?"\ntextExecution=" + textExecution:"")
                + "\nexecutionOK=" + executionOK
                + "\n}\n";
    }    
}
