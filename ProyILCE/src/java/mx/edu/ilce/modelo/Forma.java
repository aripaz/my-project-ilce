/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ilce.modelo;

/**
 *
 * @author Daniel
 */
public class Forma {
    private int claveForma;
    private String forma;
    private boolean select;
    private boolean insert;
    private boolean update;
    private boolean delete;
    private boolean sensitiveData;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(boolean sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
    

    public int getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }
    
}
