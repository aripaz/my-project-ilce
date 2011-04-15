/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.controller;

import mx.ilce.component.AdminXML;
import mx.ilce.handler.ExecutionHandler;

/**
 *  Clase para la implementacion de los metodos asociados a Forma
 *
 * @author ccatrilef
 */
public class Forma extends Entidad{ 

    private String aliasTab; 
    private Integer ordenTab; 
    private Integer claveAplicacion;
    private Integer claveForma;
    private Integer claveFormaPadre;

/************** GETTER Y SETTER ***************/

    public String getAliasTab() {
        return aliasTab;
    }

    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public Integer getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    public Integer getClaveFormaPadre() {
        return claveFormaPadre;
    }

    public void setClaveFormaPadre(Integer claveFormaPadre) {
        this.claveFormaPadre = claveFormaPadre;
    }

    public Integer getOrdenTab() {
        return ordenTab;
    }

    public void setOrdenTab(Integer ordenTab) {
        this.ordenTab = ordenTab;
    }

/************** METODOS ENTIDAD ****************/
    /**
     * Se ingresa una entidad del tipo Forma, para que quede disponible
     * para la aplicacion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data
     * @return
     */
    public ExecutionHandler ingresarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Se toma un Objeto del tipo forma para su edicion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param data
     * @return
     */
    public ExecutionHandler editarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Se toma un Objeto del tipo Forma para su eliminacion.
     * Se le entrega un dato Object, el cual debe ser casteado al tipo
     * Forma para su procesamiento.
     * @param idForma
     * @return
     */
    public ExecutionHandler eliminarEntidad(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param idForma
     * @return
     */
    public Forma mostrarForma() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
            StringBuffer str = new StringBuffer("<?xml version='1.0' encoding='ISO-8859-1'?>");
            str.append("<rows>");
            str.append("<page>1</page>");
            str.append("<total>1</total>");
            str.append("<records>5</records>");
            str.append("<row id='1'>");
            str.append("<cell>1</cell>");
            str.append("<cell>Back End</cell>");
            str.append("<cell><![CDATA[En desarrollo]]></cell>");
            str.append("</row>");
            str.append("<row id='2'>");
            str.append("<cell>2</cell>");
            str.append("<cell>CRM</cell>");
            str.append("<cell><![CDATA[En desarrollo]]></cell>");
            str.append("</row>	");
            str.append("<row id='3'>");
            str.append("<cell>3</cell>");
            str.append("<cell>Proyectos</cell>");
            str.append("<cell><![CDATA[En desarrollo]]></cell>");
            str.append("</row>	");
            str.append("<row id='4'>");
            str.append("<cell>4</cell>");
            str.append("<cell>SRM</cell>");
            str.append("<cell><![CDATA[En desarrollo]]></cell>");
            str.append("</row>	");
            str.append("<row id='5'>");
            str.append("<cell>5</cell>");
            str.append("<cell>Control presupuestal</cell>");
            str.append("<cell><![CDATA[En desarrollo]]></cell>");
            str.append("</row>");
            str.append("</rows>");
            
            this.setXmlEntidad(str);
            AdminXML admXml = new AdminXML();
            //this.setData(admXml.getDataByXML(this.getXmlEntidad()));

        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public Forma mostrarResultado() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public Forma ingresarBusquedaSencilla() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public Forma ingresarBusquedaAvanzada() {
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }

    public boolean validarCampos(){
        boolean boolSld = true;
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return boolSld;
    }

    public Forma mostrarPanel(){
        Forma frm = new Forma();
        try{
            //procesar el xml como se necesite y asignarlo
        }catch(Exception e){
            e.printStackTrace();
            //throw new UnsupportedOperationException("Not supported yet.");
        }finally{
            //colocar instrucciones que siempre se deben hacer
        }
        return frm;
    }
}
