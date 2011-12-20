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

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.handler.ExceptionHandler;

/** 
 * Clase para ser utilizada como Daemon en la carga de archivos
 * @author ccatrilef
 */
class DaemonCarga extends Thread {

    private AdmImportDB admImport;
    private String display;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public AdmImportDB getAdmImport() {
        return admImport;
    }

    public void setAdmImport(AdmImportDB admImport) {
        this.admImport = admImport;
    }

    public void run(){

        try {
            AdmImportDB admImp = this.getAdmImport();

            admImp.insertEstadoValidando();
            admImp.procesarArchivo();
            admImp.updateEstadoCargando();

            String sldData = "";
            String sldError = "";
            StringBuffer xmlForma = new StringBuffer("");
            if (admImp.isExistError()){
                if ("XML".equals(this.getDisplay())){
                    sldError =  admImp.getXMLError();
                    sldData = admImp.getStrQuery();
                    xmlForma = new StringBuffer(sldError);
                }else{
                    sldError = admImp.getStrError();
                    sldData = admImp.getStrQuery();
                    xmlForma = new StringBuffer(sldError);
                }
                xmlForma = new StringBuffer("NOT OK");
            }else{
                if ("XML".equals(this.getDisplay())){
                    sldData = admImp.getStrQuery();
                    xmlForma = new StringBuffer(sldData);
                }else{
                    sldData = admImp.getStrQuery();
                    xmlForma = new StringBuffer(sldData);
                }
                //ejecución de las queries
                boolean sld = admImp.processQuery();

                if (!sld){
                    xmlForma.append(admImp.getStrError());
                    admImp.setStoreProcedure(AdmImportDB.processERROR);
                }else{
                    admImp.setStoreProcedure(AdmImportDB.processCARGA);
                }

                admImp.addToDataStoreProcedure(admImp.getIdEstadoCarga());
                admImp.processStoreProcedure();

                admImp.updateEstadoFinalizado();
                xmlForma = new StringBuffer("OK");
            }
        } catch (ExceptionHandler ex) {
            Logger.getLogger(DaemonCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
