/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ccatrilef
 */
public class Campo implements Serializable {

    private String nombre;
    private String valor;
    private Integer codigo;
    private String alias;
    private String typeDataDB;
    private String typeDataAPL;
    private String typeControl;
    private String event;
    private String help;

    /**
     * Constructor de la clase donde se entregan los distintos elementos que
     * componen la clase
     * @param nombre    nombre del campo, generalmente asociado a los campos
     * obtenidos mediante una query
     * @param valor     valor que posee el campo
     * @param codigo    codigo que posee el campo
     * @param alias     alias que pee el campo
     * @param typeDataDB    tipo de dato equivalente usado por el campo en la
     * Base de Datos
     * @param typeControl   Tipo de dato equivalente usado por el campo en el
     * codigo Java
     * @param event     Evento asociado a este campo, generalmente correspondera
     * a una funcion javascript
     * @param help      Texto de ayuda que va asociado al campo
     */
    public Campo(String nombre, String valor, Integer codigo, String alias, String typeDataDB, String typeControl, String event, String help) {
        this.nombre = nombre;
        this.valor = valor;
        this.codigo = codigo;
        this.alias = alias;
        this.typeDataDB = typeDataDB;
        this.typeControl = typeControl;
        this.event = event;
        this.help = help;
    }

    /**
     * Constructor de la clase donde se entregan los distintos elementos que
     * componen la clase
     * @param nombre    nombre del campo, generalmente asociado a los campos
     * obtenidos mediante una query
     * @param codigo    codigo que posee el campo
     * @param typeDataDB    tipo de dato que posee el campo en la base de datos
     */
    public Campo(String nombre, Integer codigo, String typeDataDB) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.typeDataDB = typeDataDB;
    }

    /**
     * Constructor de la clase, para inicializar sin datos el objeto
     */
    public Campo(){
    }

    /**
     * obtiene el alias asociado a un campo
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Asigna el alias asociado a un campo
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Obtiene el codigo asociado a un campo
     * @return
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * Asigna el codigo asociado a un campo
     * @param codigo
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el evento asociado a un codigo, generalmente correspondera
     * a un texto o una funcion javascript
     * @return
     */
    public String getEvent() {
        return event;
    }

    /**
     * Asigna el evento asociado a un codigo, generalmente correspondera
     * a un texto o una funcion javascript
     * @param event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Obtiene el texto de ayuda asociado a un campo
     * @return
     */
    public String getHelp() {
        return help;
    }

    /**
     * Asigna el texto de ayuda a un campo
     * @param help
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Obtiene el nombre del campo, este se debe obtener desde el campo
     * asociado en la query
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del campo, este se obtiene del campo asociado a la
     * query
     * Asigna el
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo de Control que le corresponde al campo
     * @return
     */
    public String getTypeControl() {
        return typeControl;
    }

    /**
     * Asigna el tipo de Control que le corresponde al campo
     * @param typeControl
     */
    public void setTypeControl(String typeControl) {
        this.typeControl = typeControl;
    }

    /**
     * Obtiene el tipo de dato que le corresponde en la Base de Datos al campo
     * @return
     */
    public String getTypeDataDB() {
        return typeDataDB;
    }

    /**
     * Asigna el tipo de dato que le corresponde en la Base de Datos al campo
     * @return
     * @param typeDataDB
     */
    public void setTypeDataDB(String typeDataDB) {
        this.typeDataDB = typeDataDB;
    }

    /**
     * Obtiene el tipo de dato que le corresponde en Java al campo
     * @return
     */
    public String getTypeDataAPL() {
        return typeDataAPL;
    }

    /**
     * Asigna el tipo de dato que le corresponde en la Base de datos al campo
     * @param typeDataAPL
     */
    public void setTypeDataAPL(String typeDataAPL) {
        this.typeDataAPL = typeDataAPL;
    }

    /**
     * Obtiene el valor que posee el campo
     * @return
     */
    public String getValor() {
        return valor;
    }

    /**
     * Asigna el valor que debe poseer el campo
     * @param valor
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    /**
     * Entrega un string con los datos que contiene el campo
     */
    public String toString() {
        return "Campo{" + "nombre=" + nombre + "valor=" + valor + "codigo=" + codigo + "alias=" + alias + "typeData=" + typeDataDB + "typeControl=" + typeControl + "event=" + event + "help=" + help + '}';
    }
    
}
