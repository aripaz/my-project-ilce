package mx.ilce.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.conection.ConEntidad;
import mx.ilce.util.UtilDate;

/**
 * Clase para agregar seguimiento a la entrada y salida de datos desde la capa
 * vista
 * @author ccatrilef
 */
public class SpyHandler {

    private String timeIni;
    private String dateFile;
    private String servlet;
    private HashMap hsForm;
    private StringBuffer xmlSld;
    private String rutaFile;

    private String getServlet() {
        return servlet;
    }

    private void setServlet(String servlet) {
        this.servlet = servlet;
    }

    public SpyHandler() {
        UtilDate utDate = new UtilDate();
        this.timeIni = utDate.getFechaHMS();
        this.dateFile = utDate.getFechaHMS(UtilDate.formato.AMD, "","");
        this.hsForm = new HashMap();
        this.xmlSld = new StringBuffer("");
        this.rutaFile = "c:/log/";
    }

    private String getDateFile() {
        return dateFile;
    }

    private void setDateFile(String dateFile) {
        this.dateFile = dateFile;
    }

    private String getRutaFile() {
        return rutaFile;
    }

    private void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    private HashMap getHsForm() {
        return hsForm;
    }

    public void setHsForm(HashMap hsForm) {
        this.hsForm = hsForm;
    }

    private String getTimeIni() {
        return timeIni;
    }

    private void setTimeIni(String timeIni) {
        this.timeIni = timeIni;
    }

    private StringBuffer getXmlSld() {
        return xmlSld;
    }

    public void setXmlSld(StringBuffer xmlSld) {
        this.xmlSld = xmlSld;
    }
    
    private String convertHashToString(){
        StringBuilder str = new StringBuilder("");
        HashMap hsMap = this.getHsForm();

        for (int i=0;i<hsMap.size();i++){
            String strKey = (String) hsMap.keySet().toArray()[i];
            String strDat = (String) hsMap.get(strKey);
            str.append("Dato[").append(i).append("]:").append(strKey).append(" ");
            str.append("\tValor[").append(i).append("]:").append(strDat).append("\n");
        }

        return str.toString();
    }

    public void SpyData(String nameServlet){
        this.setServlet(nameServlet);
        if (aplySpy()){
            writeToFile();
        }
    }

    private boolean aplySpy(){
        boolean sld = false;

        String query = "select count(*) as valor "
                + "from rpf_iniData where nombre = 'SPY'";
        try {
            ConEntidad con = new ConEntidad();
            mx.ilce.bitacora.Bitacora bitacora = new mx.ilce.bitacora.Bitacora();
            con.setBitacora(bitacora);
            mx.ilce.bean.DataTransfer dataTransfer = new mx.ilce.bean.DataTransfer();
            dataTransfer.setQuery(query);
            mx.ilce.bean.HashCampo hsCmp = con.getDataByQuery(dataTransfer);
            HashMap hsMap = hsCmp.getListData();
            if (!hsMap.isEmpty()){
                ArrayList lst = (ArrayList) hsMap.get(0);
                mx.ilce.bean.Campo cmp = (mx.ilce.bean.Campo) lst.get(0);
                if (cmp.getValor().equals("1")){
                    sld = true;
                }
            }
        } catch (ExceptionHandler ex) {
            Logger.getLogger(SpyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sld;
    }


    private boolean writeToFile(){
        boolean sld = true;
        String strNameFile = "";
        try{
            File directorio = new File(this.getRutaFile());
            if (!directorio.exists()){
                sld = false;
            }
            if (!directorio.isDirectory()){
                sld = false;
            }
            UtilDate utDate = new UtilDate();
            strNameFile = strNameFile.concat("SPY_"+getDateFile());
            strNameFile = strNameFile.concat(".log");
            StringBuffer strTexto = new StringBuffer();
            strTexto.append("\nSERVLET:").append(this.getServlet());
            strTexto.append("\nFECHA-HORA INICIO: ").append(this.getTimeIni());
            strTexto.append("\nFECHA-HORA TERMINO: ").append(utDate.getFechaHMS());
            strTexto.append("\nDATA FORMULARIO:\n").append(this.convertHashToString());
            strTexto.append("\nDATA XML:\n").append(this.getXmlSld());
            strTexto.append("\n****************\n");
            if (sld){
                sld = guardarArchivo(strTexto, strNameFile);
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
        return sld;
    }

    private boolean guardarArchivo(StringBuffer strEntrada, String nameFile) throws IOException{
        boolean sld = true;
        FileWriter w = null;
        try{
            w = new FileWriter(rutaFile + "/" + nameFile, true);
            w.append(strEntrada.toString());
        }catch (IOException eFile){
            //eFile.printStackTrace();
            sld = false;
        }catch(Exception e){
            //e.printStackTrace();
            sld = false;
        }finally{
            if (w!=null){
                w.flush();
                w.close();
            }
        }
        return sld;
    }
}
