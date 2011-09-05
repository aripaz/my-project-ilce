package mx.ilce.report;

import java.io.Serializable;
import java.util.List;

/**
 * Clase para contener las configuraciones
 * @author ccatrilef
 */
class Config implements Serializable{

    private Integer idConfig;
    private Integer idConfigType;
    private Integer idSequence;
    private Integer idSequenceConfig;
    private String name;
    private String config;
    private String sequenceType;
    private List listConfig;

    private int numAdic;
    private int numAdicFirst;
    private int numAdicLast;
    private int numConf;


    /**
     * Obtiene el numero adicional
     * @return
     */
    public int getNumAdic() {
        return numAdic;
    }

    /**
     * Asigna el numero adicional
     * @param numAdic
     */
    public void setNumAdic(int numAdic) {
        this.numAdic = numAdic;
    }

    /**
     * Obtiene el numero adicional FIRST
     * @return
     */
    public int getNumAdicFirst() {
        return numAdicFirst;
    }

    /**
     * Asigna el numero adicional FIRST
     * @param numAdicFirst
     */
    public void setNumAdicFirst(int numAdicFirst) {
        this.numAdicFirst = numAdicFirst;
    }

    /**
     * Obtiene el numero adicional LAST
     * @return
     */
    public int getNumAdicLast() {
        return numAdicLast;
    }

    /**
     * Asigna el numero adicional LAST
     * @param numAdicLast
     */
    public void setNumAdicLast(int numAdicLast) {
        this.numAdicLast = numAdicLast;
    }

    /**
     * Obtiene el numero de configuraciones
     * @return
     */
    public int getNumConf() {
        return numConf;
    }

    /**
     * Asigna el numero de configuraciones
     * @param numConf
     */
    public void setNumConf(int numConf) {
        this.numConf = numConf;
    }

    /**
     * Obtiene el ID de la secuencia de la configuracion
     * @return
     */
    public Integer getIdSequenceConfig() {
        return idSequenceConfig;
    }

    /**
     * Asigna el ID de la secuencia de la configuracion
     * @param idSequenceConfig
     */
    public void setIdSequenceConfig(Integer idSequenceConfig) {
        this.idSequenceConfig = idSequenceConfig;
    }

    /**
     * Obtiene el listado de configuraciones
     * @return
     */
    public List getListConfig() {
        return listConfig;
    }

    /**
     * Asigna el listado de configuraciones
     * @param listConfig
     */
    public void setListConfig(List listConfig) {
        this.listConfig = listConfig;
    }

    /**
     * Obtiene la configuracion
     * @return
     */
    public String getConfig() {
        return config;
    }

    /**
     * Asigna la configuracion
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * Obtiene el ID de la configuracion
     * @return
     */
    public Integer getIdConfig() {
        return idConfig;
    }

    /**
     * Asigna el ID de la configuracion
     * @param idConfig
     */
    public void setIdConfig(Integer idConfig) {
        this.idConfig = idConfig;
    }

    /**
     * Obtiene el ID del Tipo de la configuracion
     * @return
     */
    public Integer getIdConfigType() {
        return idConfigType;
    }

    /**
     * Asigna el ID del Tipo de la configuracion
     * @param idConfigType
     */
    public void setIdConfigType(Integer idConfigType) {
        this.idConfigType = idConfigType;
    }

    /**
     * Obtiene el ID de la secuencia
     * @return
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Asigna el ID de la secuencia
     * @param idSequence
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Obtiene el nombre de la configuracion
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna el nombre de la configuracion
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el tipo de la configuracion
     * @return
     */
    public String getSequenceType() {
        return sequenceType;
    }

    /**
     * Asigna el tipo de la configuracion
     * @param sequenceType
     */
    public void setSequenceType(String sequenceType) {
        this.sequenceType = sequenceType;
    }

    @Override
    public String toString() {
        return "Config{" + "idConfig=" + idConfig + " || " + "name=" + name + " || " + "config=" + config + '}';
    }

}
