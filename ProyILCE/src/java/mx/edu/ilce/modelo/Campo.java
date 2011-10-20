/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.modelo;
import java.util.ArrayList;

/**
 *
 * @author danielm
 */
public class Campo {
    
    String nombre;
    String tipoDato;
    boolean autoIncrement;
    /*Del diccionario de datos*/
    String alias;
    byte obligatorio;
    String tipoControl;
    String evento;
    int claveFormaForanea;
    String filtroForaneo;
    byte editaFormaForanea;
    byte noPermitirValorForaneoNulo;
    String ayuda;
    byte datoSensible;
    byte activo;
    int tamano;
    byte visible;
    String valorPredeterminado;
    byte justificarCambio;
    byte usadoParaAgrupar;
    Consulta FormaForanea;

    
    public Consulta getFormaForanea() {
        return FormaForanea;
    }

    public void setFormaForanea(Consulta FormaForanea) {
        this.FormaForanea =FormaForanea ;
    }
    
    public void setFormaForanea(int claveForma,String w, ArrayList<String> reglasDeReemplazo ) {
        this.FormaForanea = new Consulta(claveForma,"foreign",0,w,reglasDeReemplazo); 
    }
     
    public byte getActivo() {
        return activo;
    }

    public void setActivo(byte activo) {
        this.activo = activo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    public int getClaveFormaForanea() {
        return claveFormaForanea;
    }

    public void setClaveFormaForanea(int claveFormaForanea) {
        this.claveFormaForanea =claveFormaForanea;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public byte getDatoSensible() {
        return datoSensible;
    }

    public void setDatoSensible(byte datoSensible) {
        this.datoSensible = datoSensible;
    }

    public byte getEditaFormaForanea() {
        return editaFormaForanea;
    }

    public void setEditaFormaForanea(byte editaFormaForanea) {
        this.editaFormaForanea = editaFormaForanea;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFiltroForaneo() {
        return filtroForaneo;
    }

    public void setFiltroForaneo(String filtroForaneo) {
        this.filtroForaneo = filtroForaneo;
    }

    public byte getJustificarCambio() {
        return justificarCambio;
    }

    public void setJustificarCambio(byte justificarCambio) {
        this.justificarCambio = justificarCambio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte getNoPermitirValorForaneoNulo() {
        return noPermitirValorForaneoNulo;
    }

    public void setNoPermitirValorForaneoNulo(byte noPermitirValorForaneoNulo) {
        this.noPermitirValorForaneoNulo = noPermitirValorForaneoNulo;
    }

    public byte getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(byte obligatorio) {
        this.obligatorio = obligatorio;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getTipoControl() {
        return tipoControl;
    }

    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    public byte getUsadoParaAgrupar() {
        return usadoParaAgrupar;
    }

    public void setUsadoParaAgrupar(byte usadoParaAgrupar) {
        this.usadoParaAgrupar = usadoParaAgrupar;
    }

    public String getValorPredeterminado() {
        return valorPredeterminado;
    }

    public void setValorPredeterminado(String valorPredeterminado) {
        this.valorPredeterminado = valorPredeterminado;
    }

    public byte getVisible() {
        return visible;
    }

    public void setVisible(byte visible) {
        this.visible = visible;
    }
    

    public Campo(String nombre, String tipoDato, boolean autoIncrement) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.autoIncrement = autoIncrement;
    }


    @Override
    public String toString() {
        return "FieldDef{" + "nombre=" + nombre + ", tipoDato=" + tipoDato + ", autoIncrement=" + autoIncrement + ", alias=" + alias + ", obligatorio=" + obligatorio + ", tipoControl=" + tipoControl + ", evento=" + evento + ",claveFormaForanea=" + claveFormaForanea + ", filtroForaneo=" + filtroForaneo + ", editaFormaForanea=" + editaFormaForanea + ", noPermitirValorForaneoNulo=" + noPermitirValorForaneoNulo + ", ayuda=" + ayuda + ", datoSensible=" + datoSensible + ", activo=" + activo + ", tamano=" + tamano + ", visible=" + visible + ", valorPredeterminado=" + valorPredeterminado + ", justificarCambio=" + justificarCambio + ", usadoParaAgrupar=" + usadoParaAgrupar + '}';
    }
 
    
    public StringBuffer toXMLDiccionario(Object valor) {
        return new StringBuffer()
                .append("<alias>").append(alias).append("</alias>")
                .append("<obligatorio>").append(obligatorio).append("</obligatorio>")
                .append("<tipo_control>").append(tipoControl).append("</tipo_control>")
                .append("<evento>").append(evento).append("</evento>")
                .append("<clave_forma_foranea>").append(claveFormaForanea).append("</clave_forma_foranea>")
                .append("<filtro_foraneo>").append(filtroForaneo).append("</filtro_foraneo>")
                .append("<edita_forma_foranea>").append(editaFormaForanea).append("</edita_forma_foranea>")
                .append("<no_permitir_valor_foraneo_nulo>").append(noPermitirValorForaneoNulo).append("</no_permitir_valor_foraneo_nulo>")
                .append("<ayuda>").append(ayuda).append("</ayuda>")
                .append("<dato_sensible>").append(datoSensible).append("</dato_sensible>")
                .append("<activo>").append(activo).append("</activo>")
                .append("<tamano>").append(tamano).append("</tamano>")
                .append("<visible>").append(visible).append("</visible>")
                .append("<valor_predeterminado>").append(valorPredeterminado).append("</valor_predeterminado>")
                .append("<justificar_cambio>").append(justificarCambio ).append("</justificar_cambio>")
                .append("<usado_para_agrupar>").append(usadoParaAgrupar).append("</usado_para_agrupar>");
    }
    
    public StringBuffer toXMLDatosDeEntidad(Object valor) {
            return new StringBuffer()
                .append("<").append(nombre).append(" ").append(" tipo_dato='").append(tipoDato).append("'>")
                        .append("<![CDATA[").append(valor).append("]]>")
                .append("</").append(nombre).append(">");
           
    }
    
     public StringBuffer toXMLDatosDeEntidadConDiccionario(Object valor) {
            return new StringBuffer()
                .append("<").append(nombre).append(" ").append(" tipo_dato='").append(tipoDato).append("'>")
                        .append("<![CDATA[").append(valor).append("]]>")
                .append("<alias>").append(alias).append("</alias>")
                .append("<obligatorio>").append(obligatorio).append("</obligatorio>")
                .append("<tipo_control>").append(tipoControl).append("</tipo_control>")
                .append("<evento>").append(evento).append("</evento>")
                .append("<clave_forma_foranea>").append(claveFormaForanea).append("</clave_forma_foranea>")
                .append("<filtro_foraneo>").append(filtroForaneo).append("</filtro_foraneo>")
                .append("<edita_forma_foranea>").append(editaFormaForanea).append("</edita_forma_foranea>")
                .append("<no_permitir_valor_foraneo_nulo>").append(noPermitirValorForaneoNulo).append("</no_permitir_valor_foraneo_nulo>")
                .append("<ayuda>").append(ayuda).append("</ayuda>")
                .append("<dato_sensible>").append(datoSensible).append("</dato_sensible>")
                .append("<activo>").append(activo).append("</activo>")
                .append("<tamano>").append(tamano).append("</tamano>")
                .append("<visible>").append(visible).append("</visible>")
                .append("<valor_predeterminado>").append(valorPredeterminado).append("</valor_predeterminado>")
                .append("<justificar_cambio>").append(justificarCambio ).append("</justificar_cambio>")
                .append("<usado_para_agrupar>").append(usadoParaAgrupar).append("</usado_para_agrupar>")
                .append("</").append(nombre).append(">");
    }
     
    public StringBuffer toXMLDatosDeEntidadConDiccionarioYForaneos(Object valor) {
        StringBuffer xml = new StringBuffer();
        StringBuffer xmlForaneo= new StringBuffer("<foraneo agrega_registro='");
        
        if (this.editaFormaForanea!=1)
            xmlForaneo.append("true");
        else
            xmlForaneo.append("false");

        xmlForaneo.append("' clave_forma='").append(this.claveFormaForanea).append("'>");

        if (this.getFormaForanea()!=null) {
           for (int m=0;m<this.getFormaForanea().getCampos().size();m++) {
               Campo campoForaneo=this.getFormaForanea().getCampos().get(m);    
               xmlForaneo.append(campoForaneo.toXMLDatosDeEntidad(valor));
           } 
           xmlForaneo.append("</foraneo>");
        }
        else 
            xmlForaneo.delete(0,xmlForaneo.length());

        xml
        .append("<").append(nombre).append(" ").append(" tipo_dato='").append(tipoDato).append("'>")
                .append("<![CDATA[").append(valor).append("]]>")
        .append("<alias>").append(alias).append("</alias>")
        .append("<obligatorio>").append(obligatorio).append("</obligatorio>")
        .append("<tipo_control>").append(tipoControl).append("</tipo_control>")
        .append("<evento>").append(evento).append("</evento>")
        .append("<clave_forma_foranea>").append(claveFormaForanea).append("</clave_forma_foranea>")
        .append("<filtro_foraneo>").append(filtroForaneo).append("</filtro_foraneo>")
        .append("<edita_forma_foranea>").append(editaFormaForanea).append("</edita_forma_foranea>")
        .append("<no_permitir_valor_foraneo_nulo>").append(noPermitirValorForaneoNulo).append("</no_permitir_valor_foraneo_nulo>")
        .append("<ayuda>").append(ayuda).append("</ayuda>")
        .append("<dato_sensible>").append(datoSensible).append("</dato_sensible>")
        .append("<activo>").append(activo).append("</activo>")
        .append("<tamano>").append(tamano).append("</tamano>")
        .append("<visible>").append(visible).append("</visible>")
        .append("<valor_predeterminado>").append(valorPredeterminado).append("</valor_predeterminado>")
        .append("<justificar_cambio>").append(justificarCambio ).append("</justificar_cambio>")
        .append("<usado_para_agrupar>").append(usadoParaAgrupar).append("</usado_para_agrupar>")
        .append(xmlForaneo)
        .append("</").append(nombre).append(">");               
                
        return xml;
    }

}
