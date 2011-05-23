/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.modelo;

/**
 *
 * @author danielm
 */
public class FieldDef {

    String name;
    String dataType;
    boolean autoIncrement;
    /*Del diccionario de datos*/
    String alias;
    byte obligatorio;
    String tipo_control;
    String evento;
    int clave_foranea;
    String filtro_foraneo;
    String ayuda;
    byte dato_sensible;
    byte activo;
    int tamano;

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

    public int getClave_foranea() {
        return clave_foranea;
    }

    public void setClave_foranea(int clave_foranea) {
        this.clave_foranea = clave_foranea;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public byte getDato_sensible() {
        return dato_sensible;
    }

    public void setDato_sensible(byte dato_sensible) {
        this.dato_sensible = dato_sensible;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFiltro_foraneo() {
        return filtro_foraneo;
    }

    public void setFiltro_foraneo(String filtro_foraneo) {
        this.filtro_foraneo = filtro_foraneo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTipo_control() {
        return tipo_control;
    }

    public void setTipo_control(String tipo_control) {
        this.tipo_control = tipo_control;
    }

    public FieldDef(String name, String dataType, boolean autoIncrement) {
        this.name = name;
        this.dataType = dataType;
        this.autoIncrement = autoIncrement;
    }

    public FieldDef(String name, String dataType, boolean autoIncrement, int clave_forma) {
        this.name = name;
        this.dataType = dataType;
        this.autoIncrement = autoIncrement;
    }

    @Override
    public String toString() {
        return   "FieldDef {\n" +
                "name:" + this.name + "\n" +
                "dataType: " + this.dataType  + "\n" +
                "autoIncrement: " + this.autoIncrement  + "\n" +
                "alias: " +this.alias  + "\n" +
                "obligatorio: " + this.obligatorio  + "\n" +
                "tipo_control: " + this.tipo_control + "\n" +
                "evento: " + this.evento  + "\n" +
                "clave_foranea: " + this.clave_foranea  + "\n" +
                "filtro_foraneo: " + this.filtro_foraneo  + "\n" +
                "ayuda: " + this.ayuda  + "\n" +
                "dato_sensible: " + this.dato_sensible  + "\n" +
                "activo: " + this.activo + "\n" +
                "tamano: " + this.tamano + "}\n";
    }


}
