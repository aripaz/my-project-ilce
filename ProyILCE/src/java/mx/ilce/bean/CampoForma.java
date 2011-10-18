package mx.ilce.bean;

import java.io.Serializable;

/**
 * Clase implementada para contener los datos del CampoForma
 * Esta es una de las clases PILARES de la aplicacion y debe estar en
 * correspondencia con la tabla Campo_Forma
 * @author ccatrilef
 */
public class CampoForma implements Serializable  {

    private Integer claveCampo;
    private Integer claveForma;
    private Integer clavePerfil;
    private String campo;
    private String aliasCampo;
    private Integer obligatorio;
    private String tipoControl;
    private String evento;
    private Integer claveFormaForanea;
    private String filtroForaneo;
    private Integer editaFormaForanea;//BIT
    private String ayuda;
    private Integer datoSensible;//BIT
    private Integer activo;//BIT
    private Integer tamano;
    private Integer claveAplicacion;

    private String tabla;
    private String foraneo;
    private String typeData;

    private Integer visible;//BIT
    private String valorPredeterminado;
    private Integer justificarCambio;//BIT
    private String aliasTab;
    private Integer usadoParaAgrupar;//BIT
    private Integer noPermitirValorForaneoNulo;//BIT

    /**
     * Obtiene el valor del Validador de si debe permitir foraneo o no
     * @return
     */
    public Integer getNoPermitirValorForaneoNulo() {
        return noPermitirValorForaneoNulo;
    }

    /**
     * Asigna el valor del Validador de si debe permitir foraneo o no
     * @param noPermitirValorForaneoNulo
     */
    public void setNoPermitirValorForaneoNulo(Integer noPermitirValorForaneoNulo) {
        this.noPermitirValorForaneoNulo = noPermitirValorForaneoNulo;
    }

    /**
     * Obtiene el valor del Validador de si se debe agrupar o no
     * @return
     */
    public Integer getUsadoParaAgrupar() {
        return usadoParaAgrupar;
    }

    /**
     * Asigna el valor del Validador de si se debe agrupar o no
     * @param usadoParaAgrupar
     */
    public void setUsadoParaAgrupar(Integer usadoParaAgrupar) {
        this.usadoParaAgrupar = usadoParaAgrupar;
    }

    /**
     * Obtiene el valor del AliasTab
     * @return
     */
    public String getAliasTab() {
        return aliasTab;
    }

    /**
     * Asigna el valor del AliasTab
     * @param aliasTab
     */
    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    /**
     * Obtiene el valor del Validador de si debe Justificar cambio o no
     * @return
     */
    public Integer getJustificarCambio() {
        return justificarCambio;
    }

    /**
     * Asigna el valor del Validador de si debe Justificar cambio o no
     * @param justificarCambio
     */
    public void setJustificarCambio(Integer justificarCambio) {
        this.justificarCambio = justificarCambio;
    }

    /**
     * Obtiene el valor predeterminado
     * @return
     */
    public String getValorPredeterminado() {
        return valorPredeterminado;
    }

    /**
     * Asigna el valor predeterminado
     * @param valorPredeterminado
     */
    public void setValorPredeterminado(String valorPredeterminado) {
        this.valorPredeterminado = valorPredeterminado;
    }

    /**
     * Obtiene el valor del Validador de si es visible o no el campo
     * @return
     */
    public Integer getVisible() {
        return visible;
    }

    /**
     * Asigna el valor del Validador de si es visible o no el campo
     * @param visible
     */
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    /**
     * Obtiene la claveAplicacion del objeto
     * @return
     */
    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Asigna la claveAplicacion del objeto
     * @param claveAplicacion
     */
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    /**
     * Obtiene la clavePerfil del objeto
     * @return
     */
    public Integer getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Asigna la clavePerfil del objeto
     * @param clavePerfil
     */
    public void setClavePerfil(Integer clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Obtiene un entero que indica si se edita (1) o no (0) la forma Foranea
     * @return
     */
    public Integer getEditaFormaForanea() {
        return editaFormaForanea;
    }

    /**
     * Asigna un entero que indica si se edita (1) o no (0) la forma Foranea
     * @param editaFormaForanea
     */
    public void setEditaFormaForanea(Integer editaFormaForanea) {
        this.editaFormaForanea = editaFormaForanea;
    }

    /**
     * Obtiene el nombre del tipo de dato del CampoForma
     * @return
     */
    public String getTypeData() {
        return typeData;
    }

    /**
     * Asigna el nombre del tipo de dato del CampoForma
     * @param typeData      Tipo de Dato del CampoForma
     */
    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    /**
     * Obtiene la clave de la forma foranea del CampoForma
     * @return
     */
    public Integer getClaveFormaForanea() {
        return claveFormaForanea;
    }

    /**
     * Asigna la clave de la forma foranea del CampoForma
     * @param claveFormaForanea     Clave de la forma foranea
     */
    public void setClaveFormaForanea(Integer claveFormaForanea) {
        this.claveFormaForanea = claveFormaForanea;
    }

    /**
     * Obtiene el validador de la condicion de activo o no del CampoForma
     * @return
     */
    public Integer getActivo() {
        return activo;
    }

    /**
     * Asigna el validador de la condicion de activo o no del CampoForma
     * @param activo    Validador del estado activo o no del CampoForma
     */
    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el texto con el Alias de un campoForma
     * @return
     */
    public String getAliasCampo() {
        return aliasCampo;
    }

    /**
     * Asigna el texto con el Alias de un CampoForma
     * @param aliasCampo    Alias del CampoForma
     */
    public void setAliasCampo(String aliasCampo) {
        this.aliasCampo = aliasCampo;
    }

    /**
     * Obtiene el texto de ayuda que esta asociado a un CampoForma
     * @return
     */
    public String getAyuda() {
        return ayuda;
    }

    /**
     * Asigna el texto de ayuda que esta asociado a un CampoForma
     * @param ayuda     Texto de ayuda del CampoForma
     */
    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    /**
     * Obtiene el nombre del CampoForma
     * @return
     */
    public String getCampo() {
        return campo;
    }

    /**
     * Asigna el nombre del CampoForma
     * @param campo     Nombre del CampoForma
     */
    public void setCampo(String campo) {
        this.campo = campo;
    }

    /**
     * Obtiene la clave del CampoForma
     * @return
     */
    public Integer getClaveCampo() {
        return claveCampo;
    }

    /**
     * Asigna la clave del CampoForma
     * @param claveCampo    Clave del CampoForma
     */
    public void setClaveCampo(Integer claveCampo) {
        this.claveCampo = claveCampo;
    }

    /**
     * Obtiene la clave de la forma a la que esta asociado el CampoForma
     * @return
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Asigna la clave de la forma a la que esta asociado el campoForma
     * @param claveForma    Clave de la Forma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene el codigo que indica si un dato es sensible o no
     * @return
     */
    public Integer getDatoSensible() {
        return datoSensible;
    }

    /**
     * Asigna el codigo que indica si un dato es sensible o no
     * @param datoSensible  Validador que indica si el dato es sensible o no
     */
    public void setDatoSensible(Integer datoSensible) {
        this.datoSensible = datoSensible;
    }

    /**
     * Obtiene un texto (generalmente la invocacion de un JavaScript) que estara
     * asociado a un evento con el campoForma
     * @return
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Asigna el texto (generalmente la invocacion de un JavaScript) que estara
     * asociado a un evento con el CampoForma
     * @param evento    Texto con el evento del CampoForma
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * Obtiene el texto con el dato foraneo que le corresponde al CampoForma
     * @return
     */
    public String getFiltroForaneo() {
        return filtroForaneo;
    }

    /**
     * Asigna el texto con el dato foraneo que le corresponde al CampoForma
     * @param filtroForaneo     Texto del filtro foraneo
     */
    public void setFiltroForaneo(String filtroForaneo) {
        this.filtroForaneo = filtroForaneo;
    }

    /**
     * Obtiene el dato foraneo que se asigno al CampoForma
     * @return
     */
    public String getForaneo() {
        return foraneo;
    }

    /**
     * Asigna el dato foraneo que se asigno al CampoForma
     * @param foraneo   Nombre del dato foraneo
     */
    public void setForaneo(String foraneo) {
        this.foraneo = foraneo;
    }

    /**
     * Obtiene el codigo que indica si es obligatorio o no un CampoForma
     * @return
     */
    public Integer getObligatorio() {
        return obligatorio;
    }

    /**
     * Asigna el codigo que indica si es obligatorio o no un CampoForma
     * @param obligatorio   Validador de si es obligatorio o no el CampoForma
     */
    public void setObligatorio(Integer obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * Obtiene el nombre de la Tabla a la que esta asociado el CampoForma
     * @return
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * Asigna el nombre de la Tabla a la que esta asociado el CampoForma
     * @param tabla     Nombre de la Tabla
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Obtiene el tamaño que tendra el CampoForma
     * @return
     */
    public Integer getTamano() {
        return tamano;
    }

    /**
     * Asigna el tamaño que tendra el campoForma
     * @param tamano    Tamaño del CampoForma
     */
    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    /**
     * Obtiene un texto con el tipo de control que debe utilizarce para el
     * CampoForma. Por defecto (con valor null) es un campo del tipo Text
     * @return
     */
    public String getTipoControl() {
        return tipoControl;
    }

    /**
     * Asigna un texto con el tipo de control que debe utilizarce para el
     * campóForma. Por defecto (con valor null) es un campo del tipo Text
     * @param tipoControl   Tipo de Control asociado al CampoForma
     */
    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    /**
     * Metodo que lleva formato String el contenido del objeto
     * @return
     */
    @Override
    public String toString() {
        String str = "";
        str = "CampoForma{"
                + ((claveCampo!=null)?"\n|| claveCampo=" + claveCampo:"")
                + ((claveForma!=null)?"\n|| claveForma=" + claveForma:"")
                + ((tabla!=null)?"\n|| tabla=" + tabla:"")
                + ((campo!=null)?"\n|| campo=" + campo:"")
                + ((aliasCampo!=null)?"\n|| aliasCampo=" + aliasCampo:"")
                + ((obligatorio!=null)?"\n|| obligatorio=" + obligatorio:"")
                + ((tipoControl!=null)?"\n|| tipoControl=" + tipoControl:"")
                + ((evento!=null)?"\n|| evento=" + evento:"")
                + ((foraneo!=null)?"\n|| foraneo=" + foraneo:"")
                + ((filtroForaneo!=null)?"\n|| filtroForaneo=" + filtroForaneo:"")
                + ((ayuda!=null)?"\n|| ayuda=" + ayuda:"")
                + ((datoSensible!=null)?"\n|| datoSensible=" + datoSensible:"")
                + ((activo!=null)?"\n|| activo=" + activo:"")
                + ((tamano!=null)?"\n|| tamano=" + tamano:"")
                + ((claveFormaForanea!=null)?"\n|| claveFormaForanea=" + claveFormaForanea:"")
                + ((typeData!=null)?"\n|| typeData=" + typeData:"")
                + "\n}";
        return str;
    }
}
