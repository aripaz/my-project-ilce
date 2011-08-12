package mx.ilce.bitacora;

import java.io.Serializable;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *  Objeto creado para ser utilizado en la administracion de los datos de Bitacora
 * @author ccatrilef
 */
public final class Bitacora implements Serializable {

    private Integer claveBitacora;
    private Integer claveBitacoraProyecto;
    private Integer claveEmpleado;
    private Integer claveProyecto;
    private Integer claveTipoEvento;
    private String evento;
    private Date fecha;
    private String ip;
    private String navegador;
    private String error;
    private Date fechaBitacora;
    private String bitacora;
    private String consulta;
    private boolean enable;

    private static Integer cero = Integer.valueOf(0);

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Bitacora(HttpServletRequest request){
        this.setIp(request.getRemoteAddr());
        this.setNavegador(request.getHeader("USER-AGENT"));
        this.setEnable(false);
    }

    public String getBitacora() {
        return ((bitacora==null)?"":bitacora);
    }

    public void setBitacora(String bitacora) {
        this.bitacora = bitacora;
    }

    public Integer getClaveBitacora() {
        return ((claveBitacora==null)?cero:claveBitacora);
    }

    public void setClaveBitacora(Integer clave_bitacora) {
        this.claveBitacora = clave_bitacora;
    }

    public Integer getClaveBitacoraProyecto() {
        return ((claveBitacoraProyecto==null)?cero:claveBitacoraProyecto);
    }

    public void setClaveBitacoraProyecto(Integer clave_bitacora_proyecto) {
        this.claveBitacoraProyecto = clave_bitacora_proyecto;
    }

    public Integer getClaveEmpleado() {
        return ((claveEmpleado==null)?cero:claveEmpleado);
    }

    public void setClaveEmpleado(Integer clave_empleado) {
        this.claveEmpleado = clave_empleado;
    }

    public Integer getClaveProyecto() {
        return ((claveProyecto==null)?cero:claveProyecto);
    }

    public void setClaveProyecto(Integer clave_proyecto) {
        this.claveProyecto = clave_proyecto;
    }

    public Integer getClaveTipoEvento() {
        return ((claveTipoEvento==null)?cero:claveTipoEvento);
    }

    public void setClaveTipoEvento(Integer clave_tipo_evento) {
        this.claveTipoEvento = clave_tipo_evento;
    }

    public String getConsulta() {
        return ((consulta==null)?"":consulta);
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String getError() {
        return ((error==null)?"":error);
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaBitacora() {
        return fechaBitacora;
    }

    public void setFechaBitacora(Date fecha_bitacora) {
        this.fechaBitacora = fecha_bitacora;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "Bitacora{" + "claveBitacora=" + claveBitacora
                + " || claveBitacoraProyecto=" + claveBitacoraProyecto
                + " || claveEmpleado=" + claveEmpleado
                + " || claveProyecto=" + claveProyecto
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
