/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
package mx.ilce.importDB;

/**
 * Bean implementado para contener los datos de una tabla, usada para manejar
 * esquemas donde las tablas no están necesariamente en la misma base de datos
 * de operación
 * @author ccatrilef
 */
class DataTable {

    private String nameTable;
    private String dominioTable;
    private String baseTable;
    private String userTable;
    private String passTable;

    /**
     * Obtiene el nombre de la tabla
     * @return  String  Nombre de la tabla
     */
    public String getNameTable() {
        return nameTable;
    }

    /**
     * Asigna el nombre de la tabla
     * @param nameTable     Nombre de la tabla
     */
    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    /**
     * Obtiene el nombre de la base de datos donde esta ubicada la tabla
     * @return  String  Nombre de la base de datos
     */
    public String getBaseTable() {
        return baseTable;
    }

    /**
     * Asigna el nombre de la base de datos donde esta ubicada la tabla
     * @param baseTable     Nombre de la base de datos
     */
    public void setBaseTable(String baseTable) {
        this.baseTable = baseTable;
    }

    /**
     * Obtiene el nombre del dominio donde se encuentra la tabla
     * @return  String  Nombre del dominio
     */
    public String getDominioTable() {
        return dominioTable;
    }

    /**
     * Asigna el nombre del dominio donde se encuentra la tabla
     * @param dominioTable  Nombre del dominio
     */
    public void setDominioTable(String dominioTable) {
        this.dominioTable = dominioTable;
    }

    /**
     * Obtiene el password asignado al usuario para poder operar con la tabla
     * @return  String  Texto con el password
     */
    public String getPassTable() {
        return passTable;
    }

    /**
     * Asigna el password utilizado por el usuario para poder operar con la tabla
     * @param passTable     Texto con el password
     */
    public void setPassTable(String passTable) {
        this.passTable = passTable;
    }

    /**
     * Obtiene el usuario asignado para operar con la tabla
     * @return  String  Texto con el usuario
     */
    public String getUserTable() {
        return userTable;
    }

    /**
     * Asigna el usuario utilizado para operar con la tabla
     * @param userTable     Texto con el usuario
     */
    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

}
