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
package mx.ilce.bitacora;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Objeto creado para ser utilizado en la administración de los datos de la
 * bitácora del User y la Aplicación
 * @author ccatrilef
 */
public final class Bitacora implements Serializable {

    private Integer claveBitacora;
    private Integer claveBitacoraProyecto;
    private Integer claveEmpleado;
    private Integer claveAplicacion;
    private Integer claveForma;
    private Integer claveTipoEvento;
    private Integer claveRegistro;
    private String evento;
    private Date fecha;
    private String ip;
    private String navegador;
    private String error;
    private Date fechaBitacora;
    private String bitacora;
    private String consulta;
    private boolean enable;
    private List lstVariables;
    private Integer idBitacora;

    private static Integer cero = Integer.valueOf(0);

    /**
     * Obtiene el ID asignado de la bitácora
     * @return  Integer     ID de la bitácora
     */
    public Integer getIdBitacora() {
        return ((idBitacora==null)?Integer.valueOf(0):idBitacora);
    }

    /**
     * Asigna el ID de la bitácora
     * @param idBitacora    ID de la bitácora
     */
    public void setIdBitacora(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    /**
     * Constructor básico del objeto Bitacora
     */
    public Bitacora() {
    }

    /**
     * Constructor a partir de los datos del request
     * @param request   Objeto del tipo HttpServletRequest
     */
    public Bitacora(HttpServletRequest request){
        this.setIp(request.getRemoteAddr());
        this.setNavegador(request.getHeader("USER-AGENT"));
        this.setEnable(false);
    }

    /**
     * Método para reiniciar la data que deba ser limpiada para ser usada
     * en las queries
     */
    public void cleanDataQuery(){
        this.setClaveAplicacion(null);
        this.setClaveBitacora(null);
        this.setClaveForma(null);
        this.setClaveTipoEvento(null);
        this.setConsulta(null);
        this.setEnable(false);
        this.setClaveRegistro(null);
    }

    /**
     * Obtiene el listado de variables de la bitácora
     * @return  List    Listado con las variables
     */
    public List getLstVariables() {
        return lstVariables;
    }

    /**
     * Agrega el listado de variables de la bitácora
     * @param lstVariables  Listado con las variables
     */
    public void setLstVariables(List lstVariables) {
        this.lstVariables = lstVariables;
    }

    /**
     * Indica si debe efectuarse la escritura en la bitácora o no
     * @return  boolean     Valor de la validación
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Asigna si debe efectuarse la escritura en la bitácora o no
     * @param enable    Valor de la validación
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Obtiene el texto asignado a la bitácora
     * @return  String      Texto con la operación de la bitácora
     */
    public String getBitacora() {
        return ((bitacora==null)?"":bitacora);
    }

    /**
     * Asigna el texto que debe poseer la bitácora
     * @param bitacora  Texto con la operación de la bitácora
     */
    public void setBitacora(String bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el código numérico de la bitácora
     * @return  Integer     Clave de la bitácora
     */
    public Integer getClaveBitacora() {
        return ((claveBitacora==null)?cero:claveBitacora);
    }

    /**
     * Asigna el código numérico de la bitácora
     * @param clave_bitacora    Clave de la bitácora
     */
    public void setClaveBitacora(Integer clave_bitacora) {
        this.claveBitacora = clave_bitacora;
    }

    /**
     * Obtiene la clave de la bitácora de proyecto
     * @return  Integer     Clave de la bitacora del proyecto
     */
    public Integer getClaveBitacoraProyecto() {
        return ((claveBitacoraProyecto==null)?cero:claveBitacoraProyecto);
    }

    /**
     * Asigna la clave de la bitácora de proyecto
     * @param clave_bitacora_proyecto   Clave de la bitácora del proyecto
     */
    public void setClaveBitacoraProyecto(Integer clave_bitacora_proyecto) {
        this.claveBitacoraProyecto = clave_bitacora_proyecto;
    }

    /**
     * Obtiene la clave del empleado
     * @return  Integer     Clave del empleado
     */
    public Integer getClaveEmpleado() {
        return ((claveEmpleado==null)?cero:claveEmpleado);
    }

    /**
     * Asigna la clave del empleado
     * @param clave_empleado    Clave del empleado
     */
    public void setClaveEmpleado(Integer clave_empleado) {
        this.claveEmpleado = clave_empleado;
    }

    /**
     * Obtiene la clave de la aplicación
     * @return  Integer     Clave de la aplicación
     */
    public Integer getClaveAplicacion() {
        return ((claveAplicacion==null)?cero:claveAplicacion);
    }

    /**
     * Asigna la clave de la aplicación
     * @param clave_aplicacion  Clave de la aplicación
     */
    public void setClaveAplicacion(Integer clave_aplicacion) {
        this.claveAplicacion = clave_aplicacion;
    }

    /**
     * Obtiene la clave de la Forma
     * @return  Integer     Clave de la forma
     */
    public Integer getClaveForma() {
        return ((claveForma==null)?cero:claveForma);
    }

    /**
     * Asigna la clave de la Forma
     * @param claveForma    Clave de la forma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene la clave del tipo de evento
     * @return  Integer     Clave del tipo de evento
     */
    public Integer getClaveTipoEvento() {
        return ((claveTipoEvento==null)?cero:claveTipoEvento);
    }

    /**
     * Asigna la clave del tipo de evento
     * @param clave_tipo_evento Clave del tipo de evento
     */
    public void setClaveTipoEvento(Integer clave_tipo_evento) {
        this.claveTipoEvento = clave_tipo_evento;
    }

    /**
     * Obtiene la claveRegistro
     * @return  Integer Clave del registro
     */
    public Integer getClaveRegistro() {
        return claveRegistro;
    }

    /**
     * Asigna la claveRegistro
     * @param claveRegistro Clave del registro
     */
    public void setClaveRegistro(Integer claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    /**
     * Obtiene la consulta de la bitácora
     * @return  String  Texto con la consulta
     */
    public String getConsulta() {
        return ((consulta==null)?"":consulta);
    }

    /**
     * Asigna la consulta de la bitácora
     * @param consulta  Texto con la consulta
     */
    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    /**
     * Obtiene el texto de error
     * @return  String  Texto con el error a catalogar
     */
    public String getError() {
        return ((error==null)?"":error);
    }

    /**
     * Asigna el texto de error
     * @param error Texto con el error a catalogar
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Obtiene la fecha
     * @return  Date    Fecha asignada
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha
     * @param fecha     Fecha asignada
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la fecha de la bitácora
     * @return  Date    Fecha asignada
     */
    public Date getFechaBitacora() {
        return fechaBitacora;
    }

    /**
     * Asigna la fecha de la bitácora
     * @param fecha_bitacora    Fecha asignada
     */
    public void setFechaBitacora(Date fecha_bitacora) {
        this.fechaBitacora = fecha_bitacora;
    }

    /**
     * Obtiene la IP
     * @return  String  codificación IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * Asigna la IP
     * @param ip    String  codificación IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Obtiene el Navegador utilizado
     * @return  String  Texto con datos del Navegador
     */
    public String getNavegador() {
        return navegador;
    }

    /**
     * Asigna el navegador utilizado
     * @param navegador Texto con datos del Navegador
     */
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    /**
     * Obtiene el texto del evento
     * @return  String  Evento asignado
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Asigna el texto del evento
     * @param evento    Evento asignado
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * Método encargado de agregar las variables utilizadas en las operaciones
     * de INSERT u UPDATE
     * @param campo     Datos del Campo
     * @param alias     Alias del campo
     * @param valor     Valor del campo
     * @param tipo      Tipo de campo
     */
    public void addToListVariables(String campo, String alias,
                                String valor, String tipo){
        if (this.getLstVariables()==null){
            List listVariables = new ArrayList();
            this.setLstVariables(listVariables);
        }
        String[] arrVariables = new String[4];
        arrVariables[0]=campo;
        arrVariables[1]=alias;
        arrVariables[2]=valor;
        arrVariables[3]=tipo;
        this.getLstVariables().add(arrVariables);
    }

    /**
     * Método para convertir a String el contenido del Objeto. Los datos que
     * estén con valor NULL son ignorados
     * @return  String  Texto con los datos del objeto
     */
    @Override
    public String toString() {
        return "Bitacora{" 
                + ((claveBitacora!=null)?"\n\tclaveBitacora=" + claveBitacora:"")
                + ((claveBitacoraProyecto!=null)?"\n\tclaveBitacoraProyecto=" + claveBitacoraProyecto:"")
                + ((claveEmpleado!=null)?"\n\tclaveEmpleado=" + claveEmpleado:"")
                + ((claveAplicacion!=null)?"\n\tclaveAplicacion=" + claveAplicacion:"")
                + ((claveForma!=null)?"\n\tclaveForma=" + claveForma:"")
                + ((claveTipoEvento!=null)?"\n\tclaveTipoEvento=" + claveTipoEvento:"")
                + ((evento!=null)?"\n\tevento=" + evento:"")
                + ((fecha!=null)?"\n\tfecha=" + fecha.toString():"")
                + ((ip!=null)?"\n\tip=" + ip:"")
                + ((navegador!=null)?"\n\tnavegador=" + navegador:"")
                + ((error!=null)?"\n\terror=" + error:"")
                + ((fechaBitacora!=null)?"\n\tfechaBitacora=" + fechaBitacora.toString():"")
                + ((bitacora!=null)?"\n\tbitacora=" + bitacora:"")
                + ((consulta!=null)?"\n\tconsulta=" + consulta:"")
                + "\n\tenable=" + enable
                + "\n}";
    }
}
