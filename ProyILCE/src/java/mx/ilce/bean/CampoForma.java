/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.bean;

import java.io.Serializable;

/**
 *
 * @author ccatrilef
 */
public class CampoForma implements Serializable  {

    private Integer claveCampo;
    private String campo;
    private String aliasCampo;
    private Integer obligatorio;
    private String tipoControl;
    private String evento;
    private String foraneo;
    private Integer filtroForaneo;
    private String ayuda;
    private Integer datoSensible;
    private Integer activo;

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getAliasCampo() {
        return aliasCampo;
    }

    public void setAliasCampo(String aliasCampo) {
        this.aliasCampo = aliasCampo;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Integer getClaveCampo() {
        return claveCampo;
    }

    public void setClaveCampo(Integer claveCampo) {
        this.claveCampo = claveCampo;
    }

    public Integer getDatoSensible() {
        return datoSensible;
    }

    public void setDatoSensible(Integer datoSensible) {
        this.datoSensible = datoSensible;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Integer getFiltroForaneo() {
        return filtroForaneo;
    }

    public void setFiltroForaneo(Integer filtroForaneo) {
        this.filtroForaneo = filtroForaneo;
    }

    public String getForaneo() {
        return foraneo;
    }

    public void setForaneo(String foraneo) {
        this.foraneo = foraneo;
    }

    public Integer getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Integer obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getTipoControl() {
        return tipoControl;
    }

    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }
    
}
