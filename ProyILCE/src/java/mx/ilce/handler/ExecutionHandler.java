/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.handler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *  Clase implementada para manejar la ejecucion de la aplicacion. POsee diversos
 * metodos que facilitan el traslado de mensajes y datos
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
     * Constructor Basico, se inicializan las variables del objeto
     */
    public ExecutionHandler() {
        this.idExecution = new BigDecimal(0);
        this.titleExecution = "";
        this.textExecution = "";
        this.executionOK = true;
    }

    /**
     * Obtiene el Object ingresado al Objeto
     * @return
     */
    public Object getObjectData() {
        return objectData;
    }

    /**
     * Asigna el Object a ingresar al Objeto
     * @param objectData
     */
    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }

    /**
     * Obtiene el ListaData ingresado al Objeto
     * @return
     */
    public List getListData() {
        return listData;
    }

    /**
     * Asigna el Listada a ingresar al Objeto
     * @param listData
     */
    public void setListData(List listData) {
        this.listData = listData;
    }

    /**
     * Indica si la ejecucion se realizo de manera correcta(TRUE) o no (FALSE)
     * @return
     */
    public boolean isExecutionOK() {
        return executionOK;
    }

    /**
     * Asigna si la ejecucion se realizo de manera correcta(TRUE) o no (FALSE)
     * @param executionOK
     */
    public void setExecutionOK(boolean executionOK) {
        this.executionOK = executionOK;
    }

    /**
     * Obtiene el ID de la execucion
     * @return
     */
    public BigDecimal getIdExecution() {
        return idExecution;
    }

    /**
     * Asigna el ID de la execucion
     * @param idExecution
     */
    public void setIdExecution(BigDecimal idExecution) {
        this.idExecution = idExecution;
    }

    /**
     * Obtiene el texto de respuesta de la ejecucion
     * @return
     */
    public String getTextExecution() {
        return textExecution;
    }

    /**
     * asigna el texto de respuesta de la ejecucion
     * @param textExecution
     */
    public void setTextExecution(String textExecution) {
        this.textExecution = textExecution;
    }

    /**
     * Obtiene el titulo dado a la ejecucion
     * @return
     */
    public String getTitleExecution() {
        return titleExecution;
    }

    /**
     * Asigna el titulo dado a la ejecucion
     * @param titleExecution
     */
    public void setTitleExecution(String titleExecution) {
        this.titleExecution = titleExecution;
    }

    /**
     * Convierte a un String el contenido del Objeto
     * @return
     */
    @Override
    public String toString() {
        return "ExecutionHandler{" + "idExecution=" + idExecution + "titleExecution=" + titleExecution + "textExecution=" + textExecution + "executionOK=" + executionOK + '}';
    }


    
}
