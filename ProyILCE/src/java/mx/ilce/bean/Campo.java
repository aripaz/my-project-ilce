package mx.ilce.bean;

import java.io.Serializable;

/**
 * Clase implementada para el manejo de los campos que se obtienen al ejecutar
 * una query en la base de datos
 * @author ccatrilef
 */
public class Campo implements Serializable {

    private String nombre;
    private String nombreDB;
    private String valor;
    private Integer codigo;
    private String alias;
    private String typeDataDB;
    private String typeDataAPL;
    private String typeControl;
    private String event;
    private String help;
    private Integer tamano;
    private boolean isIncrement;
    private String hourMinSec;

    /**
     * Obtiene la fecha capturada, incluyendo además los datos de
     * hora, minuto y segundo
     * @return
     */
    public String getHourMinSec() {
        return hourMinSec;
    }

    /**
     * Asigna la fecha capturada, incluyendo además los datos de
     * hora, minuto y segundo
     * @param hourMinSec    Valor de la fecha con hora, minuto y segundo
     */
    public void setHourMinSec(String hourMinSec) {
        this.hourMinSec = hourMinSec;
    }

    /**
     * Constructor de la clase donde se inicializan los distintos elementos que
     * componen la clase. Se asignan los datos: nombre, valor, codigo, alias,
     * typeDataDB, typeControl, event, help y tamano
     * @param nombre    Nombre del campo, generalmente asociado a los campos
     * obtenidos mediante una query
     * @param valor     Valor que posee el campo
     * @param codigo    Codigo que posee el campo
     * @param alias     Alias que posee el campo
     * @param typeDataDB    Tipo de dato equivalente usado por el campo en la
     * Base de Datos
     * @param typeControl   Tipo de dato equivalente usado por el campo en el
     * codigo Java
     * @param event     Evento asociado a este campo, generalmente correspondera
     * a una función javascript
     * @param help      Texto de ayuda que va asociado al campo
     * @param tamano    Valor para contener un número que indique algún tamaño
     */
    public Campo(String nombre, String valor, Integer codigo, String alias,
                 String typeDataDB, String typeControl, String event, 
                 String help, Integer tamano) {
        this.nombre = nombre.replaceAll("_","").toUpperCase();
        this.nombreDB = nombre;
        this.valor = valor;
        this.codigo = codigo;
        this.alias = alias;
        this.typeDataDB = typeDataDB.toUpperCase();
        this.typeControl = typeControl;
        this.event = event;
        this.help = help;
        this.tamano = tamano;
    }

    /**
     * Constructor de la clase donde se inicializan los distintos elementos que
     * componen la clase. Se asignan los datos nombre, codigo y typeDataDB
     * @param nombre    Nombre del campo, generalmente asociado a los campos
     * obtenidos mediante una query
     * @param codigo    Codigo que posee el campo
     * @param typeDataDB    Tipo de dato que posee el campo en la base de datos
     */
    public Campo(String nombre, Integer codigo, String typeDataDB) {
        this.nombre = nombre.replaceAll("_","").toUpperCase();
        this.nombreDB = nombre;
        this.codigo = codigo;
        this.typeDataDB = typeDataDB.toUpperCase();
    }

    /**
     * Constructor de la clase donde se inicializan los distintos elementos que
     * componen la clase. Se asignan los campos: nombre, nombreDB, codigo,
     * typeDataDB, typeAPL y valor
     * @param nombre    Nombre del campo, generalmente asociado a los campos
     * obtenidos mediante una query
     * @param nombreDB  Nombre del campo en la Base de datos
     * @param codigo    Código que posee el campo
     * @param typeDataDB    Tipo de dato que posee el campo en la base de datos
     * @param typeAPL       Tipo de dato que le corresponde en la aplicación
     * @param valor     Valor que posee el campo
     */
    public Campo(String nombre, String nombreDB, Integer codigo, String typeDataDB,
            String typeAPL, String valor) {
        this.nombre = nombre.replaceAll("_","").toUpperCase();
        this.nombreDB = nombreDB;
        this.codigo = codigo;
        this.typeDataDB = typeDataDB.toUpperCase();
        this.typeDataAPL = typeAPL;
        this.valor=valor;
    }

    /**
     * Constructor básico de la clase, para inicializar sin datos el objeto
     */
    public Campo(){
    }

    /**
     * Obtiene el alias asociado a un campo
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Asigna el alias asociado a un campo
     * @param alias     Alias del Campo
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Obtiene el código asociado a un campo
     * @return
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * Asigna el código asociado a un campo
     * @param codigo    Codigo del Campo
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el evento asociado a un código, generalmente correspondera
     * a un texto o una función javascript
     * @return
     */
    public String getEvent() {
        return event;
    }

    /**
     * Asigna el evento asociado a un código, generalmente correspondera
     * a un texto o una función javascript
     * @param event     Texto del evento del Campo
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
     * @param help      Texto de ayuda del Campo
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Obtiene el nombre del campo sin el caracter _, este se debe obtener
     * desde el campo asociado en la query
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del campo sin el caracter _, este se obtiene del campo
     * asociado a la query
     * @param nombre    Nombre del Campo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del campo con el caracter _, este se debe obtener 
     * desde el campo asociado en la query
     * @return
     */
    public String getNombreDB() {
        return nombreDB;
    }

    /**
     * Asigna el nombre del campo con el caracter _, este se obtiene del campo
     * asociado a la query
     * @param nombre    Nombre proveniente de la Base de Datos del Campo
     */
    public void setNombreDB(String nombreDB) {
        this.nombreDB = nombreDB;
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
     * @param typeControl   Tipo de control asignado al Campo
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
     * @param typeDataDB    Tipo de dato proveniente de la Base de Datos
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
     * Asigna el tipo de dato que le corresponde en Java al campo
     * @param typeDataAPL   Typo de dato usado en la aplicacion para el Campo
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
     * @param valor     Valor del Campo
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Obtiene el tamaño de un campo
     * @return
     */
    public Integer getTamano() {
        return tamano;
    }

    /**
     * Asigna el tamaño que debe poseer un campo
     * @param tamano    Tamaño del Campo
     */
    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    /**
     * Retorna si es Autoincremet el campo. Se debe interpretar así: TRUE es la clave
     * de la tabla, FALSE no es la clave de la tabla
     * @return
     */
    public boolean getIsIncrement() {
        return isIncrement;
    }

    /**
     * Asigna si es Autoincrement el campo. Se debe interpretar así: TRUE es la clave
     * de la tabla, FALSE no es la clave de la tabla
     * @param isIncrement   Validación que indica si el campo es Autoincrement o no
     */
    public void setIsIncrement(boolean isIncrement) {
        this.isIncrement = isIncrement;
    }

    /**
     * Método para convertir a String el contenido del Objeto. Los datos que
     * estén con valor NULL son ignorados
     * @return  String  Texto con los datos del objeto
     */
    @Override
    public String toString() {
        String str = "";
        str = "Campo{\n"
                + ((nombre!=null)?"\n\tnombre=" + nombre:"")
                + ((nombreDB!=null)?"\n\tnombreDB=" + nombreDB:"")
                + ((alias!=null)?"\n\talias=" + alias:"")
                + ((valor!=null)?"\n\tvalor=" + valor:"")
                + ((codigo!=null)?"\n\tcodigo=" + codigo:"")
                + ((typeDataDB!=null)?"\n\ttypeDataDB=" + typeDataDB:"")
                + ((typeDataAPL!=null)?"\n\ttypeDataAPL=" + typeDataAPL:"")
                + ((typeControl!=null)?"\n\ttypeControl=" + typeControl:"")
                + ((event!=null)?"\n\tevent=" + event:"")
                + ((help!=null)?"\n\thelp=" + help:"")
                + ((tamano!=null)?"\n\ttamano=" + tamano:"")
                + ((hourMinSec!=null)?"\n\thourMinSec=" + hourMinSec:"")
                + "\n\tisIncrement=" + isIncrement
                + "}\n";
        return str;
    }
}
