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
package mx.ilce.bean;

/**
 * Clase implementada para dar equivalencia al tipo de data TEXT desde
 * la base de datos
 * @author ccatrilef
 */
public class Text{

    private String str = "";

    /**
     * Método que asigna un texto al objeto
     * @param str
     */
    public Text(String str) {
        this.str = str;
    }

    /**
     * Obtiene el texto asignado al objeto
     * @return  String  Valor del texto
     */
    public String getStr() {
        return str;
    }

    /**
     * Asigna el texto al objeto
     * @param str   Valor del texto
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     * Método que limpia el contenido del objeto
     */
    public void clean(){
        setStr("");
    }

    /**
     * Método que concatena el nuevo texto al existente en el objeto
     * @param data  Texto a concatenar
     */
    public void concat(String data){
        if (data!=null){
            str = str.concat(data);
        }
    }

    /**
     * Método que compara el texto evaluado con el texto existente en el objeto
     * @param data  Texto a comparar
     * @return  boolean     Valor de la comparación
     */
    public boolean equals(String data){
        return ((data==null)?false:str.equals(data));
    }

    /**
     * Método que entrega la posición de la primera ocurrencia del texto
     * entregado, comparándolo con el texto existente en el objeto
     * @param data  Texto a buscar
     * @return  boolean     Valor de la posición
     */
    public int indexOf(String data){
        return str.indexOf(data);
    }

    /**
     * Método que aplica una eliminación de los espacios en blanco a la izquierda 
     * y derecha del texto contenido en el objeto
     */
    public void trim(){
        str = str.trim();
    }

    /**
     * Método que entrega un substring del texto contenido en el objeto, desde
     * la posición de inicio, hasta la de término entregada
     * @param beginIndex    Posición de inicio
     * @param endIndex      Posición de término
     * @return  String      Substring obtenido
     */
    public String subString(int beginIndex, int endIndex){
        return str.substring(beginIndex, endIndex);
    }

    /**
     * Método que entrega en mayúsculas el texto asignado al objeto
     * @return  String  Texto en mayúsculas
     */
    public String toUpperCase(){
        return str.toUpperCase();
    }

    /**
     * Método que entrega en minúsculas el texto asignado al objeto
     * @return  String  Texto en minúsculas
     */
    public String toLowerCase(){
        return str.toLowerCase();
    }

    /**
     * Método que entrega el largo del texto
     * @return  int     Valor del largo del texto
     */
    public int length(){
        return str.length();
    }

    /**
     * Método que indica si esta vacio o no el texto del objeto
     * @return  boolean     Valor con la evaluación
     */
    public boolean isEmpty(){
        return ((str.length()==0)?true:str.isEmpty());
    }

    /**
     * Método que entrega un arreglo dividiendo el texto contenido en el objeto
     * en las posiciones que se coinciden con el texto entregado
     * @param data  Texto de validación
     * @return  String[]    Arreglo obtenido
     */
    public String[] split(String data){
        return str.split(data);
    }

    /**
     * Método que reemplaza un texto por otro, dentro del texto contenido en el
     * objeto, se hace en la primera ocurrencia
     * @param oldData   Texto a buscar
     * @param newData   Texto por el que se debe reemplazar
     */
    public void replaceFirst(String oldData, String newData){
        str = str.replaceFirst(oldData, newData);
    }

    /**
     * Método que reemplaza un texto por otro, dentro del texto contenido en el
     * objeto, se hace en todas las ocurrencias
     * @param oldData   Texto a buscar
     * @param newData   Texto por el que se debe reemplazar
     */
    public void replaceAll(String oldData, String newData){
        str = str.replaceAll(oldData, newData);
    }
}
