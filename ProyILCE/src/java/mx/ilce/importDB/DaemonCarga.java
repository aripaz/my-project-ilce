package mx.ilce.importDB;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para ser utilizada como Daemon
 * @author Administrador
 */
public class DaemonCarga extends Thread {

    private AdmImportDB admImport;

    public AdmImportDB getAdmImport() {
        return admImport;
    }

    public void setAdmImport(AdmImportDB admImport) {
        this.admImport = admImport;
    }

    public void run(){

        try {
            for (int i=0;i<20;i++){
                System.out.println("PRUEBA " + i);
                sleep(100);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DaemonCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
