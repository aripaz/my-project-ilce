package mx.ilce.handler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *  Clase implementada para manejar la ejecucion de la aplicacion. Posee diversos
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
     * Indica si la ejecucion se realizo de manera correcta (TRUE) o no (FALSE)
     * @return  Boolean     Valor TRUE O FALSE de la validacion
     */
    public boolean isExecutionOK() {
        return executionOK;
    }

    /**
     * Asigna si la ejecucion se realizo de manera correcta (TRUE) o no (FALSE)
     * @param executionOK   Valor TRUE O FALSE de la validacion
     */
    public void setExecutionOK(boolean executionOK) {
        this.executionOK = executionOK;
    }

    /**
     * Obtiene el ID de la execucion
     * @return  BigDecimal  ID de la ejecucion
     */
    public BigDecimal getIdExecution() {
        return idExecution;
    }

    /**
     * Asigna el ID de la ejecucion
     * @param idExecution   ID de la Ejecucion
     */
    public void setIdExecution(BigDecimal idExecution) {
        this.idExecution = idExecution;
    }

    /**
     * Obtiene el texto de respuesta de la ejecucion
     * @return  String  Texto de la ejecucion
     */
    public String getTextExecution() {
        return textExecution;
    }

    /**
     * Asigna el texto de respuesta de la ejecucion
     * @param textExecution     Texto de la ejecucion
     */
    public void setTextExecution(String textExecution) {
        this.textExecution = textExecution;
    }

    /**
     * Obtiene el titulo dado a la ejecucion
     * @return  String      Titulo de la ejecucion
     */
    public String getTitleExecution() {
        return titleExecution;
    }

    /**
     * Asigna el titulo dado a la ejecucion
     * @param titleExecution    Titulo de la ejecucion
     */
    public void setTitleExecution(String titleExecution) {
        this.titleExecution = titleExecution;
    }

    /**
     * Metodo que lleva a un formato String el contenido del objeto
     */
    @Override
    public String toString() {
        return "ExecutionHandler{" 
                + "idExecution=" + idExecution + " || "
                + "titleExecution=" + titleExecution + " || "
                + "textExecution=" + textExecution + " || "
                + "executionOK=" + executionOK + '}';
    }    
}
