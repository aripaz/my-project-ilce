/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.handler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ccatrilef
 */
public class ExecutionHandler implements Serializable{

    private BigDecimal idExecution;
    private String titleExecution;
    private String textExecution;
    private boolean executionOK;
    private List listData;
    private Object objectData;

    public ExecutionHandler() {
        this.idExecution = new BigDecimal(0);
        this.titleExecution = "Title";
        this.textExecution = "Text";
        this.executionOK = true;
    }

    public Object getObjectData() {
        return objectData;
    }

    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }

    public List getListData() {
        return listData;
    }

    public void setListData(List listData) {
        this.listData = listData;
    }

    public boolean isExecutionOK() {
        return executionOK;
    }

    public void setExecutionOK(boolean executionOK) {
        this.executionOK = executionOK;
    }

    public BigDecimal getIdExecution() {
        return idExecution;
    }

    public void setIdExecution(BigDecimal idExecution) {
        this.idExecution = idExecution;
    }

    public String getTextExecution() {
        return textExecution;
    }

    public void setTextExecution(String textExecution) {
        this.textExecution = textExecution;
    }

    public String getTitleExecution() {
        return titleExecution;
    }

    public void setTitleExecution(String titleExecution) {
        this.titleExecution = titleExecution;
    }

    @Override
    public String toString() {
        return "ExecutionHandler{" + "idExecution=" + idExecution + "titleExecution=" + titleExecution + "textExecution=" + textExecution + "executionOK=" + executionOK + '}';
    }


    
}
