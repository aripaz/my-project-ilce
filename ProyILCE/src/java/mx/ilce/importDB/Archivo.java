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
