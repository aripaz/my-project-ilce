package mx.ilce.bitacora;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *  Objeto creado para ser utilizado en la administracion de los datos de Bitacora
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

    private static Integer cero = Integer.valueOf(0);

    /**
     * Constructor a partir de los datos del request
     * @param request
     */
    public Bitacora(HttpServletRequest request){
        this.setIp(request.getRemoteAddr());
        this.setNavegador(request.getHeader("USER-AGENT"));
        this.setEnable(false);
    }

    /**
     * Metodo para reiniciar la data que deba ser limpiada usada en las queries
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
     * Obtiene el listado de variables de la bitacora
     * @return
     */
    public List getLstVariables() {
        return lstVariables;
    }

    /**
     * Agrega el listado de variables de la bitacora
     * @param lstVariables
     */
    public void setLstVariables(List lstVariables) {
        this.lstVariables = lstVariables;
    }

    /**
     * Indica si debe efectuarse la escritura en la bitacora o no
     * @return
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Asigna si debe efectuarse la escritura en la bitacora o no
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Obtiene el texto asignado a la bitacora
     * @return
     */
    public String getBitacora() {
        return ((bitacora==null)?"":bitacora);
    }

    /**
     * Asigna el texto que debe poseer la bitacora
     * @param bitacora
     */
    public void setBitacora(String bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el codigo numerico de la bitacora
     * @return
     */
    public Integer getClaveBitacora() {
        return ((claveBitacora==null)?cero:claveBitacora);
    }

    /**
     * Asigna el codigo numerico de la bitacora
     * @param clave_bitacora
     */
    public void setClaveBitacora(Integer clave_bitacora) {
        this.claveBitacora = clave_bitacora;
    }

    /**
     * Obtiene la clave de la bitacora de proyecto
     * @return
     */
    public Integer getClaveBitacoraProyecto() {
        return ((claveBitacoraProyecto==null)?cero:claveBitacoraProyecto);
    }

    /**
     * Asigna la clave de la bitacora de proyecto
     * @param clave_bitacora_proyecto
     */
    public void setClaveBitacoraProyecto(Integer clave_bitacora_proyecto) {
        this.claveBitacoraProyecto = clave_bitacora_proyecto;
    }

    /**
     * Obtiene la clave del empleado
     * @return
     */
    public Integer getClaveEmpleado() {
        return ((claveEmpleado==null)?cero:claveEmpleado);
    }

    /**
     * Asigna la clave del empleado
     * @param clave_empleado
     */
    public void setClaveEmpleado(Integer clave_empleado) {
        this.claveEmpleado = clave_empleado;
    }

    /**
     * Obtiene la clave de la aplicacion
     * @return
     */
    public Integer getClaveAplicacion() {
        return ((claveAplicacion==null)?cero:claveAplicacion);
    }

    /**
     * Asigna la clave de la aplicacion
     * @param clave_aplicacion
     */
    public void setClaveAplicacion(Integer clave_aplicacion) {
        this.claveAplicacion = clave_aplicacion;
    }

    /**
     * Onbtiene la clave de la Forma
     * @return
     */
    public Integer getClaveForma() {
        return ((claveForma==null)?cero:claveForma);
    }

    /**
     * Asigna la clave de la Forma
     * @param claveForma
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Obtiene la clave del tipo de evento
     * @return
     */
    public Integer getClaveTipoEvento() {
        return ((claveTipoEvento==null)?cero:claveTipoEvento);
    }

    /**
     * Asigna la clave del tipo de evento
     * @param clave_tipo_evento
     */
    public void setClaveTipoEvento(Integer clave_tipo_evento) {
        this.claveTipoEvento = clave_tipo_evento;
    }

    /**
     * Obtiene la claveRegistro
     * @return
     */
    public Integer getClaveRegistro() {
        return claveRegistro;
    }

    /**
     * Asigna la claveRegistro
     * @param claveRegistro
     */
    public void setClaveRegistro(Integer claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    /**
     * Obtiene la consulta de la bitacora
     * @return
     */
    public String getConsulta() {
        return ((consulta==null)?"":consulta);
    }

    /**
     * Asigna la consulta de la bitacora
     * @param consulta
     */
    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    /**
     * Obtiene el texto de error
     * @return
     */
    public String getError() {
        return ((error==null)?"":error);
    }

    /**
     * Asigna el texto de error
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Obtiene la fecha
     * @return
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la fecha de la bitacora
     * @return
     */
    public Date getFechaBitacora() {
        return fechaBitacora;
    }

    /**
     * Asigna la fecha de la bitacora
     * @param fecha_bitacora
     */
    public void setFechaBitacora(Date fecha_bitacora) {
        this.fechaBitacora = fecha_bitacora;
    }

    /**
     * Obtiene la IP
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     * Asigna la IP
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Obtiene el Navegador utilizado
     * @return
     */
    public String getNavegador() {
        return navegador;
    }

    /**
     * Asigna el navegador utilizado
     * @param navegador
     */
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    /**
     * Obtiene el texto del evento
     * @return
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Asigna el texto del evento
     * @param evento
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * Metodo encargado de agregar las variables utilizadas en las operaciones
     * de INSERT u UPDATE
     * @param campo
     * @param alias
     * @param valor
     * @param tipo
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
     * Convierte a String el contenido de la bitacora
     * @return
     */
    @Override
    public String toString() {
        return "Bitacora{" + "claveBitacora=" + claveBitacora
                + " || claveBitacoraProyecto=" + claveBitacoraProyecto
                + " || claveEmpleado=" + claveEmpleado
                + " || claveAplicacion=" + claveAplicacion
                + " || claveForma=" + claveForma
                + " || claveTipoEvento=" + claveTipoEvento
                + " || evento=" + evento
                + " || fecha=" + fecha
                + " || ip=" + ip
                + " || navegador=" + navegador
                + " || error=" + error
                + " || fechaBitacora=" + fechaBitacora
                + " || bitacora=" + bitacora
                + " || consulta=" + consulta
                + " || enable=" + enable
                + '}';
    }
}
