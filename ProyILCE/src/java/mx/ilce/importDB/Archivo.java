package mx.ilce.importDB;

import java.io.Serializable;

/**
 * Clase implementada para contener los datos de los archivos manejados
 * @author ccatrilef
 */
public class Archivo implements Serializable{

    private Integer idArchivoCarga;
    private Integer idTabla;
    private String archivoCarga;
    private Integer idTipoArchivoCarga;

    public Integer getIdTipoArchivoCarga() {
        return idTipoArchivoCarga;
    }

    public void setIdTipoArchivoCarga(Integer idTipoArchivoCarga) {
        this.idTipoArchivoCarga = idTipoArchivoCarga;
    }
    
    public Integer getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(Integer idTabla) {
        this.idTabla = idTabla;
    }

    public Integer getIdArchivoCarga() {
        return idArchivoCarga;
    }

    public void setIdArchivoCarga(Integer idArchivoCarga) {
        this.idArchivoCarga = idArchivoCarga;
    }

    public String getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(String archivoCarga) {
        this.archivoCarga = archivoCarga;
    }
    

}
