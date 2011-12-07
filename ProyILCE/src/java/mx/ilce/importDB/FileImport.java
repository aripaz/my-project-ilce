package mx.ilce.importDB;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import mx.ilce.util.UtilDate;

/**
 * Clase implementada para el manejo de Archivos de Importación de Datos a Tablas
 * @author ccatrilef
 */
public class FileImport {

    private String fileProcess;
    private String fileHeader;
    private String fileBody;
    private String fileTotales;
    private HashMap hsTotales;
    private String textError;
    private HashMap camposValidos;
    private HashMap camposInvalidos;
    private String headerValido;
    private String strHeader;
    private boolean useTotal;
    private List listHeaders;

    DecimalFormat formateador = new DecimalFormat("###,###,###,###.##");

    private static Integer PIVOTECUENTA = 1;

    public List getListHeaders() {
        return listHeaders;
    }

    private void setListHeaders(List listHeaders) {
        this.listHeaders = listHeaders;
    }

    public boolean isUseTotal() {
        return useTotal;
    }

    public void setUseTotal(boolean useTotal) {
        this.useTotal = useTotal;
    }

    public String getStrHeader() {
        return ((strHeader==null)?"":strHeader);
    }

    public void setStrHeader(String strHeader) {
        this.strHeader = strHeader;
    }

    public HashMap getCamposInvalidos() {
        return camposInvalidos;
    }

    public void setCamposInvalidos(HashMap camposInvalidos) {
        this.camposInvalidos = camposInvalidos;
    }

    public String getHeaderValido() {
        return headerValido;
    }

    public void setHeaderValido(String headerValido) {
        this.headerValido = headerValido;
    }

    public String getFileTotales() {
        return fileTotales;
    }

    public void setFileTotales(String fileTotales) {
        this.fileTotales = fileTotales;
    }

    public HashMap getCamposValidos() {
        return camposValidos;
    }

    public void setCamposValidos(HashMap camposValidos) {
        this.camposValidos = camposValidos;
    }

    public String getTextError() {
        return ((textError==null)?"":textError);
    }

    public void setTextError(String textError) {
        this.textError = textError;
    }

    public FileImport() {
        hsTotales = new HashMap();
    }

    public HashMap getHsTotales() {
        return hsTotales;
    }

    public void setHsTotales(HashMap hsTotales) {
        this.hsTotales = hsTotales;
    }


    public String getFileBody() {
        return fileBody;
    }

    public void setFileBody(String fileBody) {
        this.fileBody = fileBody;
    }

    public String getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(String fileHeader) {
        this.fileHeader = fileHeader;
    }

    public String getFileProcess() {
        return fileProcess;
    }

    public void setFileProcess(String fileProcess) {
        this.fileProcess = fileProcess;
    }

    /**
     * Método para obtener el encabezado de un archivo, hasta la fila anterior
     * indicada en el número entregado.
     * El archivo que se lee es el indicado en la variable global fileProcess.
     * @param filaHeader
     * @return
     */
    public boolean putFileHeader(Integer filaHeader, List lstHeader){
        boolean sld = false;

        if (lstHeader!=null){
            List lstCampos = (List) lstHeader.get(0);
            if (lstCampos!=null){
                Iterator it = lstCampos.iterator();
                while (it.hasNext()){
                    CargaArchivo ca = (CargaArchivo) it.next();
                    String strFila = getRowsFromFile(ca.getFila());
                    String dato = "";
                    if (ca.getLargo()!=null){
                        Integer posFin = 0;
                        if (ca.getLargo()+ca.getPosicionInicio()>=strFila.length()-1){
                            posFin = strFila.length()-1;
                        }else{
                            posFin = ca.getLargo()+ca.getPosicionInicio()-1;
                        }
                        dato = strFila.substring(ca.getPosicionInicio()-1,posFin);
                    }else{
                        dato = strFila.substring(ca.getPosicionInicio()-1);
                    }
                    if (Date.class.getName().equals(getTypeDataApp(ca.getTipoCampo()))){
                        UtilDate ut = new UtilDate(dato, ca.getFormato());
                        dato = ut.getFecha();
                    }
                    String nombreCampo = ca.getNombreCampo();
                    List lstData = new ArrayList();
                    lstData.add(nombreCampo);
                    lstData.add(dato);
                    addToListHeader(lstData);
                }
                sld = true;
            }
        }
        return sld;
    }

    /**
     * Método que agrega una lista de datos al listado global de los header
     * obtenidos desde el archivo
     * @param lst   Listado que debe ser agregado
     */
    private void addToListHeader(List lst){
        List lstHeader = this.getListHeaders();

        if (lstHeader==null){
            lstHeader = new ArrayList();
        }
        lstHeader.add(lst);
        this.setListHeaders(lstHeader);
    }

    /**
     * Método que obtiene el texto ubicado en la fila que corresponde
     * al número entregado.
     * El archivo que se lee es el indicado en la variable global fileProcess.
     * @param filaHeader
     * @return  String Texto con el contenido leido en la fila indicada
     */
    public String getRowsFromFile(Integer filaHeader){
        String sld = "";
        FileReader fr = null;
        try {
            //TODO: ver manera de optimizar esta lectura para continuar en la
            //linea que se quedo en una lectura anterior
            File archivo = new File(this.getFileProcess());
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            for (int i=0;i<filaHeader;i++){
                linea = br.readLine();
            }
            sld = linea;
        } catch (EOFException eof){
            sld = "EOF";
        } catch (IOException ex) {
            //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sld;
    }

    /**
     * Método que obtiene el header que le corresponde a un archivo
     * @param filaHeader    Numero donde se encuentra la fila del Header
     * @param listCampos    Listado con la configuración de campos
     * @return  boolean     Resultado de la operación (TRUE:exitoso FALSE: con errores
     */
    private boolean getRowHeader(Integer filaHeader, List listCampos, boolean compareTotal){
        boolean sld = true;
        boolean compararTotal = false;
        String strFileHeaders = "";
        if (filaHeader==0){
            try{
                if (!listCampos.isEmpty()){
                    Iterator it = listCampos.iterator();
                    //leer la lista de campos
                    while (it.hasNext()){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        if (compareTotal){
                            if (caG.isSumable()){
                                compararTotal = true;
                            }
                        }
                        strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                    }
                }
            }catch (Exception e){
                sld = false;
                String stError = getTextError();
                stError = stError + "\nProblemas generar el Header con la configuración de campos."
                                  + "\nDATOS LEIDOS:" + strFileHeaders;
                setTextError(stError);
            }
        }else{
            try{
                strFileHeaders = getRowsFromFile(filaHeader);
                if (compareTotal){
                    //buscaremos si existe un campo sumable
                    try{
                        if (!listCampos.isEmpty()){
                            Iterator it = listCampos.iterator();
                            //leer la lista de campos
                            boolean seguir = true;
                            while (it.hasNext() && seguir){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                if (caG.isSumable()){
                                    compararTotal = true;
                                    seguir = false;
                                }
                            }
                        }
                    }catch (Exception e){
                        sld = false;
                        String stError = getTextError();
                        stError = stError + "\nProblemas generar el Header con la configuración de campos."
                                          + "\nDATOS LEIDOS:" + strFileHeaders;
                        setTextError(stError);
                    }
                }
            } catch (Exception e) {
                sld = false;
                String stError = getTextError();
                stError = stError + "\nProblemas obtener el Header del archivo indicado en la fila:" + filaHeader +".";
                setTextError(stError);
            }
        }
        this.setUseTotal(compararTotal);
        if (sld){
            this.setStrHeader(strFileHeaders);
        }
        return sld;
    }

    /**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos. El archivo leido es del tipo CSV y no posee 
     * totales que validar. Si el valor filaHeader que se indica es 0, se entiende
     * que no hay header y se construye uno a partir de la configuración de campos
     * del archivo, que viene el listCampos
     * (-) El archivo que se lee es el indicado en la variable global fileProcess.
     * (-) El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyCSV_ST(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        boolean existCampos = false;
        String strFileHeaders = "";

        sld = getRowHeader(filaHeader, listCampos, false);
        if (sld){
            strFileHeaders = this.getStrHeader();
            existCampos = existCampoInHeader(strFileHeaders,listCampos);
            //TRABAJAR EXTRAYENDO LOS REGISTROS
            if (existCampos){
                try{
                    //Escribir header en archivo
                    sld = writeToFileCSV(strFileHeaders);
                    //leer a partir de la fila a continuacion de filaHeader
                    String strFila = "";
                    Integer posFila = filaHeader+1;
                    strFila = getRowsFromFile(posFila);
                    while ((strFila!=null)&& sld){
                        String strPaso = strFila.trim();
                        //ignorar si es una fila vacia (trim().lenght==0)
                        if (!"".equals(strPaso)) {
                            sld = writeToFileCSV(strPaso);
                        }
                        posFila++;
                        strFila = getRowsFromFile(posFila);
                    }
                } catch (Exception e){
                    sld = false;
                    String stError = getTextError();
                    stError = stError + "\nProblemas al escribir archivo de transferencia.";
                    setTextError(stError);
                }
            }else{
                String stError = getTextError();
                stError = "\nProblemas al validar archivo CSV sin Totales."
                        + "\nNo se encuentran los campos en el Header." + stError;
                setTextError(stError);
            }
        }else{
            String stError = getTextError();
            stError = "\nProblemas al validar archivo CSV sin Totales." + stError;
            setTextError(stError);
        }
        return sld;
    }

    /**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos. El archivo leido es del tipo CSV y posee
     * totales que validar. Si el valor filaHeader que se indica es 0, se entiende
     * que no hay header y se construye uno a partir de la configuración de campos
     * del archivo, que viene el listCampos
     * (-) El archivo que se lee es el indicado en la variable global fileProcess.
     * (-) El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyCSV_CT(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        boolean compararTotal = false;
        boolean existCampos = false;
        String strFileHeaders = "";
        String strTotales = "";

        sld = getRowHeader(filaHeader, listCampos, true);
        if (sld){
            strFileHeaders = this.getStrHeader();
            compararTotal = this.isUseTotal();
            existCampos = existCampoInHeader(strFileHeaders,listCampos);
            //TRABAJAR EXTRAYENDO LOS REGISTROS
            if (existCampos){
                try{
                    //Escribir header en archivo
                    sld = writeToFileCSV(strFileHeaders);
                    //leer a partir de la fila a continuacion de filaHeader
                    String strFila = "";
                    Integer posFila = filaHeader+1;
                    strFila = getRowsFromFile(posFila);
                    while ((strFila!=null) && sld ){
                        String strPaso = strFila.trim();
                        //ignorar si es una fila vacia (trim().lenght==0)
                        if (!"".equals(strPaso)) {
                            strFila = getRowsFromFile(posFila+1);
                            //escribimos en archivo si no es la ultima fila
                            if ((strFila!=null)&&(!"EOF".equals(strFila))){
                                sld = writeToFileCSV(strPaso);
                            }else{
                                strTotales = strPaso;
                            }
                        }
                        posFila++;
                        strFila = getRowsFromFile(posFila);
                    }
                } catch (Exception e){
                    sld = false;
                    String stError = getTextError();
                    stError = stError + "\nProblemas al escribir archivo de transferencia.";
                    setTextError(stError);
                }
            }else{
                String stError = getTextError();
                stError = "\nProblemas al validar archivo CSV con Totales."
                        + "\nNo se encuentran los campos en el Header." + stError;
                setTextError(stError);
            }
        }else{
            String stError = getTextError();
            stError = "\nProblemas al validar archivo CSV con Totales." + stError;
            setTextError(stError);
        }
        //guardamos los totales si se solicita
        if (sld && compararTotal){
            sld = writeToFileTotalesCSV(strFileHeaders,strTotales);
        }
        return sld;
    }

    /**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos. El archivo leido es del tipo PLANO y no posee
     * totales que validar. Si el valor filaHeader que se indica es 0, se entiende
     * que no hay header y se construye uno a partir de la configuración de campos
     * del archivo, que viene el listCampos
     * (-) El archivo que se lee es el indicado en la variable global fileProcess.
     * (-) El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyPLANO_ST(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        boolean seguir = true;
        boolean existCampos = false;
        boolean compararRegistros = false;
        String strFileHeaders = "";
        String strNumReg = "";
        Integer numRegistros = 0;

        try{
            //TRABAJAR CON LOS HEADERS DE LOS REGISTROS
            if (filaHeader==0){
                if (!listCampos.isEmpty()){
                    Iterator it = listCampos.iterator();
                    //leer la lista de campos
                    while (it.hasNext()){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                    }
                }
                existCampos = true;
            }else{
                strFileHeaders = getRowsFromFile(filaHeader);
                existCampos = existCampoInHeader(strFileHeaders,listCampos);
                strFileHeaders = this.getHeaderValido();
            }
        }catch (Exception e){
            sld = false;
            String stError = getTextError();
            stError = stError + "\nProblemas en la obtención del Header del Archivo."
                    + "\nHEADER LEIDO:" + strFileHeaders;
            setTextError(stError);
        }

        if (existCampos){
            //TRABAJAR EXTRAYENDO LOS REGISTROS
            if (sld){
                //Escribir header en archivo
                sld = writeToFileCSV(strFileHeaders);
                //leer a partir de la fila a continuacion de filaHeader
                String strFila = "";
                Integer posFila = filaHeader+1;
                strFila = getRowsFromFile(posFila);
                while ((strFila!=null) && sld){
                    String strPaso = strFila.trim();
                    //ignorar si es una fila vacia (trim().lenght==0)
                    if (!"".equals(strPaso)) {
                        //revisar si la fila posee el tagNroRegistros
                        //SI tiene el tagNroRegistros
                        if ((strFila.contains(ca.getTagNroRegistros()))
                                &&(!"".equals(ca.getTagNroRegistros()))) {
                            //extraer el numero de registros, es un numero despues del Tag
                            //Terminar con la lectura
                            if (ca.getTagNroRegistros()!=null){
                                int posNumReg = strFila.indexOf(ca.getTagNroRegistros());
                                strNumReg = strFila.substring(posNumReg+ca.getTagNroRegistros().length());
                                strNumReg = strNumReg.trim();
                                compararRegistros = true;
                            }
                        //NO tiene el tagNroRegistros
                        }else{
                            //recorrer los campos y recortar según indica el campo
                            //limpiar de espacios en blanco a la derecha e izquierda
                            //a los tipos String colocarles ""
                            //escribir fila en archivo
                            //Si hay error detener y devolver FALSE: no corresponde la estructura
                            String filaCSV = "";
                            try{
                                Iterator it = listCampos.iterator();
                                while (it.hasNext()&&seguir){
                                    CargaArchivo caG = (CargaArchivo) it.next();
                                    int pos = caG.getPosicionInicio()-1;
                                    int len = caG.getLargo();
                                    String dato = "";
                                    if (pos+len-1<strFila.length()){
                                        dato = strFila.substring(pos,pos+len);
                                    }
                                    dato = dato.trim();
                                    dato = getDatoClean(dato,caG);
                                    filaCSV = filaCSV + dato + ",";
                                }
                            }catch(Exception e){
                                seguir=false;
                                sld=false;
                                setTextError("Problemas al extraer un registro."
                                            + ((strFila!=null)?"\nFILA: "+strFila:"")
                                            + ((filaCSV!=null)?"\nDATOS LEIDOS: "+filaCSV:"") );
                            }
                            if (sld){
                                sld = writeToFileCSV(filaCSV);
                            }
                            numRegistros++;
                        }
                    }
                    posFila++;
                    strFila = getRowsFromFile(posFila);
                }
            }
            //validar número de registros
            if (sld && compararRegistros){
                Integer intNumReg = Integer.valueOf(strNumReg);
                if (!numRegistros.equals(intNumReg)){
                    sld = false;
                    String stError = getTextError();
                    stError = stError + "\nEl número de Registros leidos no coincide con el declarado en el archivo."
                            + ((numRegistros!=null)?"\nREGISTROS LEIDOS: "+numRegistros:"")
                            + ((strNumReg!=null)?"\nREGISTROS ARCHIVO: "+strNumReg:"");
                    setTextError(stError);
                }
            }
        }else{
            sld = false;
            String stError = getTextError();
            stError = stError + "\nExisten campos obligatorios no declarados en el header."
                    + "\nCAMPOS: " + getStringCamposInvalidos();
            setTextError(stError);
        }
        return sld;
    }

    /**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos.
     * El archivo que se lee es el indicado en la variable global fileProcess.
     * El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyPLANO_CT(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        boolean existCampos = false;
        boolean compararTotal = false;
        boolean compararRegistros = false;
        String strFileHeaders = "";
        HashMap hsMontosTotales = new HashMap();
        HashMap hsSumas = new HashMap();
        String strNumReg = "";
        Integer numRegistros = 0;

        try{
            //TRABAJAR CON LOS HEADERS DE LOS REGISTROS
            if (filaHeader==0){
                if (!listCampos.isEmpty()){
                    Iterator it = listCampos.iterator();
                    //leer la lista de campos
                    while (it.hasNext()){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        //inicializamos los datos de suma
                        if (caG.isSumable()){
                            hsSumas.put(caG.getNombreCampo(), Double.valueOf(0.0d));
                            compararTotal = true;
                        }
                        strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                    }
                }
                existCampos = true;
            }else{
                strFileHeaders = getRowsFromFile(filaHeader);
                existCampos = existCampoInHeader(strFileHeaders,listCampos);
                strFileHeaders = this.getHeaderValido();
            }
        }catch (Exception e){
            sld = false;
            String stError = getTextError();
            stError = stError + "\nProblemas en la obtención del Header del Archivo."
                    + "\nHEADER LEIDO:" + strFileHeaders;
            setTextError(stError);
        }
        if (existCampos){
            //TRABAJAR EXTRAYENDO LOS REGISTROS
            if (sld){
                //Escribir header en archivo
                sld = writeToFileCSV(strFileHeaders);
                //leer a partir de la fila a continuacion de filaHeader
                String strFila = "";
                Integer posFila = filaHeader+1;
                strFila = getRowsFromFile(posFila);
                String strPivote = "";
                String strCuenta = "";
                while ((strFila!=null) && sld){
                    String strPaso = strFila.trim();
                    //ignorar si es una fila vacia (trim().lenght==0)
                    if (!"".equals(strPaso)) {
                        //revisar si la fila posee el tagTotales
                        //SI tiene el tagTotales
                        if ((strFila.contains(ca.getTagTotales()))
                                && (!"".equals(ca.getTagTotales()))) {
                            // recorrer los campos, extrayendo el total de cada campo, si corresponde
                            // colocarlo en un hashmap, usando el nombreCampo como Key
                            Iterator it = listCampos.iterator();
                            while (it.hasNext()){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                if ((caG.getPosicionInicioTotal()!=null)&&(caG.getLargoTotal()!=null)){
                                    int posTot = caG.getPosicionInicioTotal()-1;
                                    int lenTot = caG.getLargoTotal();
                                    String total = "";
                                    if (posTot+lenTot-1<strFila.length()){
                                        total = strFila.substring(posTot,posTot+lenTot);
                                    }else{
                                        total = strFila.substring(posTot);
                                    }
                                    total = total.trim();
                                    total = reduceFormatDouble(total,caG.getFormato());
                                    hsMontosTotales.put(caG.getNombreCampo(), total);
                                }
                            }
                        //NO tiene el tagTotales
                        }else{
                            //revisar si la fila posee el tagNroRegistros
                            //SI tiene el tagNroRegistros
                            if ((strFila.contains(ca.getTagNroRegistros()))
                                    && (!"".equals(ca.getTagNroRegistros()))) {
                                //extraer el numero de registros, es un numero despues del Tag
                                //Terminar con la lectura
                                if (ca.getTagNroRegistros()!=null){
                                    int posNumReg = strFila.indexOf(ca.getTagNroRegistros());
                                    strNumReg = strFila.substring(posNumReg+ca.getTagNroRegistros().length());
                                    strNumReg = strNumReg.trim();
                                    compararRegistros = true;
                                }
                            //NO tiene el tagNroRegistros
                            }else{
                                //recorrer los campos y recortar según indica el campo
                                //limpiar de espacios en blanco a la derecha e izquierda
                                //a los tipos String colocarles ""
                                //escribir fila en archivo
                                //Si hay error detener y devolver FALSE: no corresponde la estructura
                                String filaCSV = "";
                                try{
                                    Iterator it = listCampos.iterator();
                                    while (it.hasNext() && sld){
                                        CargaArchivo caG = (CargaArchivo) it.next();
                                        int pos = caG.getPosicionInicio()-1;
                                        int len = caG.getLargo();
                                        String dato = "";
                                        if (pos+len-1<strFila.length()){
                                            dato = strFila.substring(pos,pos+len);
                                        }
                                        dato = dato.trim();
                                        if (caG.isPivote()){
                                            strCuenta = dato;
                                        }
                                        dato = getDatoClean(dato,caG);
                                        filaCSV = filaCSV + dato + ",";
                                        if ((caG.isSumable())&&(isPivoteAcumulable(strCuenta, strPivote,PIVOTECUENTA))) {
                                            Double datoSuma = (Double)hsSumas.get(caG.getNombreCampo());
                                            if (datoSuma==null){
                                                datoSuma = Double.valueOf(0.0);
                                            }
                                            datoSuma = datoSuma + Double.valueOf(dato);
                                            hsSumas.put(caG.getNombreCampo(), datoSuma);
                                            strPivote = strCuenta;
                                        }
                                    }
                                }catch(Exception e){
                                    sld=false;
                                    setTextError("Problemas al extraer un registro."
                                                + ((strFila!=null)?"\nFILA: "+strFila:"")
                                                + ((filaCSV!=null)?"\nDATOS LEIDOS: "+filaCSV:"") );
                                }
                                if (sld){
                                    sld = writeToFileCSV(filaCSV);
                                }
                                numRegistros++;
                            }
                        }
                    }
                    posFila++;
                    strFila = getRowsFromFile(posFila);
                }
            }
            if (sld && compararRegistros){
                Integer intNumReg = Integer.valueOf(strNumReg);
                if (!numRegistros.equals(intNumReg)){
                    sld = false;
                    String stError = getTextError();
                    stError = stError + "\nEl número de Registros leidos no coincide con el declarado en el archivo."
                            + ((numRegistros!=null)?"\nREGISTROS LEIDOS: "+numRegistros:"")
                            + ((strNumReg!=null)?"\nREGISTROS ARCHIVO: "+strNumReg:"");
                    setTextError(stError);
                }
            }
            //validar los totales y numero de registros
            if (sld && compararTotal){
                Iterator it = listCampos.iterator();
                while (it.hasNext() && sld){
                    CargaArchivo caG = (CargaArchivo) it.next();
                    if (caG.isSumable()){
                        String strTotal = (String) hsMontosTotales.get(caG.getNombreCampo());
                        Double datoSuma = (Double) hsSumas.get(caG.getNombreCampo());
                        if ((strTotal!=null) && (datoSuma!=null)){
                            Double total = Double.valueOf(strTotal);
                            if (total.compareTo(datoSuma)!=0 ){
                                String d1 = formateador.format(total);
                                String d2 = formateador.format(datoSuma);
                                sld = false;
                                String stError = getTextError();
                                stError = stError + "\nLa suma de datos no coincide con el Monto Total del archivo."
                                        + ((total!=null)?"\nMONTO TOTAL ARCHIVO: "+d1:"")
                                        + ((datoSuma!=null)?"\nSUMA CAMPOS: "+d2:"");
                                setTextError(stError);
                            }
                        }
                    }
                }
            }
        }else{
            sld = false;
            String stError = getTextError();
            stError = stError + "\nExisten campos obligatorios no declarados en el header."
                    + "\nCAMPOS: " + getStringCamposInvalidos();
            setTextError(stError);
        }
        return sld;
    }

    /**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos.
     * El archivo que se lee es el indicado en la variable global fileProcess.
     * El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyXLSSimple(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        HashMap hsSumas = new HashMap();
        String strFileHeaders = "";
        boolean validateTotal = false;
        int rowTotal = -1;
        Sheet hoja = null;
        Sheet[] hojas = null;
        int filas = 0;
        Workbook workbook = null;
        try{
            File xlsData = new File(this.getFileProcess());
            workbook = Workbook.getWorkbook(xlsData);
            hojas = workbook.getSheets();
            int largoHojas = hojas.length;
            if (largoHojas>0){
                hoja = workbook.getSheet(0);
                filas = hoja.getRows();
            }
            if (ca.getPosicionInicioTotal()!=null){
                validateTotal = true;
                rowTotal = ca.getPosicionInicioTotal();
                if (rowTotal==0){
                    rowTotal = filas-1;
                }
            }

            //VEMOS SI TENEMOS DATOS
            if ((hoja!=null) && (filas>0)){
                //obtener fila de header si existe
                Cell[] rowHeader = null;
                int fila = 0;
                if (ca.getNroFilaHeader()!=null){
                    rowHeader = hoja.getRow(ca.getNroFilaHeader()-1);
                    fila = ca.getNroFilaHeader();
                }
                //tiene header
                if (rowHeader!=null){
                    Iterator it = listCampos.iterator();
                    while (it.hasNext() && sld){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        String datoCell = rowHeader[caG.getPosicionInicio()-1].getContents();
                        if ((datoCell!=null) && ( (caG.getNombreCampo().equals(datoCell))
                                                ||(caG.getAliasCampo().equals(datoCell))  ))
                        {
                            if (caG.isSumable() && validateTotal) {
                                hsSumas.put(caG.getNombreCampo(),Double.valueOf(0.0));
                            }
                            strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                        }else{
                            //No se encuentra el dato
                            sld = false;
                        }
                    }
                }else{
                    //no tiene header, asi que se construye a partir de la configuración
                    Iterator it = listCampos.iterator();
                    while (it.hasNext()){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                    }
                }
                if (sld){
                    sld = writeToFileCSV(strFileHeaders);
                    while ((fila<filas)&&(rowTotal!=fila)){
                        Cell[] row = hoja.getRow(fila);
                        if (row!=null) {
                            String filaCSV = "";
                            Iterator it = listCampos.iterator();
                            while (it.hasNext() && sld){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                String dato = row[caG.getPosicionInicio()-1].getContents();
                                if (dato==null){
                                    dato = "";
                                }
                                if (("".equals(dato))&&(caG.isObligatorio())){
                                    sld = false;
                                }else{
                                    dato = getDatoClean(dato, caG);
                                    if (caG.isSumable() && validateTotal){
                                        Double dbl = (Double) hsSumas.get(caG.getNombreCampo());
                                        if (dbl==null){
                                            dbl = Double.valueOf(0.0);
                                        }
                                        dbl = dbl + Double.valueOf(dato);
                                        hsSumas.put(caG.getNombreCampo(), dbl);
                                    }
                                    filaCSV = filaCSV + dato + ",";
                                }
                            }
                            if (sld){
                                sld = writeToFileCSV(filaCSV);
                            }
                            fila++;
                        }
                    }
                    if (fila==rowTotal){
                        Cell[] row = hoja.getRow(fila);
                        if (row!=null) {
                            Iterator it = listCampos.iterator();
                            while (it.hasNext()){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                if (caG.isSumable()){
                                    String dato = row[caG.getPosicionInicio()-1].getContents();
                                    Double dbl = Double.valueOf(dato);
                                    Double total = (Double) hsSumas.get(caG.getNombreCampo());
                                    if (dbl.compareTo(total)!=0){
                                        sld = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (IOException ie ){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ie);
        }catch (BiffException be){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, be);
        }catch (Exception e){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, e);
        }
        return sld;
    }

/**
     * Método que genera un archivo con el body y los header, procesando el
     * archivo de entrada y transformandolo a uno CSV que pueda ser leido para ser
     * ingresado a la base de datos.
     * El archivo que se lee es el indicado en la variable global fileProcess.
     * El archivo que se genera es el indicado en la variable global fileBody.
     * @param filaHeader    Número de la fila donde se ubica el header
     * @param ca            Objeto con la configuración general del archivo
     * @param listCampos    Listado con las configuraciones de los campos
     * @return  boolean     Resultado de la operacion (TRUE:Exitoso, FALSE: Existen errores)
     */
    public boolean getFileBodyXLSSeparador(Integer filaHeader, CargaArchivo ca, List listCampos){
        boolean sld = true;
        HashMap hsSumas = new HashMap();
        String strFileHeaders = "";
        boolean validateTotal = false;
        int rowTotal = -1;
        Sheet hoja = null;
        Sheet[] hojas = null;
        int filas = 0;
        Workbook workbook = null;
        try{
            File xlsData = new File(this.getFileProcess());
            workbook = Workbook.getWorkbook(xlsData);
            hojas = workbook.getSheets();
            int largoHojas = hojas.length;
            if (largoHojas>0){
                hoja = workbook.getSheet(0);
                filas = hoja.getRows();
            }
            if (ca.getPosicionInicioTotal()!=null){
                validateTotal = true;
                rowTotal = ca.getPosicionInicioTotal();
                if (rowTotal==0){
                    rowTotal = filas-1;
                }
            }

            //VEMOS SI TENEMOS DATOS
            if ((hoja!=null) && (filas>0)){
                //obtener fila de header si existe
                Cell[] rowHeader = null;
                int fila = 0;
                if (ca.getNroFilaHeader()!=null){
                    rowHeader = hoja.getRow(ca.getNroFilaHeader()-1);
                    fila = ca.getNroFilaHeader();
                }
                //tiene header
                if (rowHeader!=null){
                    Iterator it = listCampos.iterator();
                    while (it.hasNext() && sld){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        String datoCell = rowHeader[caG.getPosicionInicio()-1].getContents();
                        if ((datoCell!=null) && ( (caG.getNombreCampo().equals(datoCell))
                                                ||(caG.getAliasCampo().equals(datoCell))  ))
                        {
                            if (caG.isSumable() && validateTotal) {
                                hsSumas.put(caG.getNombreCampo(),Double.valueOf(0.0));
                            }
                            strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                        }else{
                            //No se encuentra el dato
                            sld = false;
                        }
                    }
                }else{
                    //no tiene header, asi que se construye a partir de la configuración
                    Iterator it = listCampos.iterator();
                    while (it.hasNext()){
                        CargaArchivo caG = (CargaArchivo) it.next();
                        strFileHeaders = strFileHeaders + caG.getNombreCampo() + ",";
                    }
                }
                if (sld){
                    sld = writeToFileCSV(strFileHeaders);
                    while ((fila<filas)&&(rowTotal!=fila)){
                        Cell[] row = hoja.getRow(fila);
                        if (row!=null) {
                            String filaCSV = "";
                            Iterator it = listCampos.iterator();
                            while (it.hasNext() && sld){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                String dato = row[caG.getPosicionInicio()-1].getContents();
                                if (dato==null){
                                    dato = "";
                                }
                                if (("".equals(dato))&&(caG.isObligatorio())){
                                    sld = false;
                                }else{
                                    dato = getDatoClean(dato, caG);
                                    if (caG.isSumable() && validateTotal){
                                        Double dbl = (Double) hsSumas.get(caG.getNombreCampo());
                                        if (dbl==null){
                                            dbl = Double.valueOf(0.0);
                                        }
                                        dbl = dbl + Double.valueOf(dato);
                                        hsSumas.put(caG.getNombreCampo(), dbl);
                                    }
                                    filaCSV = filaCSV + dato + ",";
                                }
                            }
                            if (sld){
                                sld = writeToFileCSV(filaCSV);
                            }
                            fila++;
                        }
                    }
                    if (fila==rowTotal){
                        Cell[] row = hoja.getRow(fila);
                        if (row!=null) {
                            Iterator it = listCampos.iterator();
                            while (it.hasNext()){
                                CargaArchivo caG = (CargaArchivo) it.next();
                                if (caG.isSumable()){
                                    String dato = row[caG.getPosicionInicio()-1].getContents();
                                    Double dbl = Double.valueOf(dato);
                                    Double total = (Double) hsSumas.get(caG.getNombreCampo());
                                    if (dbl.compareTo(total)!=0){
                                        sld = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (IOException ie ){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ie);
        }catch (BiffException be){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, be);
        }catch (Exception e){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, e);
        }
        return sld;
    }

/*
    private HSSFWorkbook readFile(String filename) throws IOException {
        FileInputStream xlsData = new FileInputStream(this.getFileProcess());

        return new HSSFWorkbook(xlsData);
    }

    public boolean getFileBodyXLS(){
        boolean sld = true;
        try {
            FileInputStream xlsData = new FileInputStream(this.getFileProcess());
            if (xlsData !=null){
                HSSFWorkbook wb = new HSSFWorkbook(xlsData);
                HSSFSheet hoja = wb.getSheetAt(0);
                int lastRow = hoja.getLastRowNum();
                //short row = (short) 2;
                for (int i=0;i<lastRow;i++){
                    HSSFRow row = hoja.getRow(i);
                    Iterator it = row.iterator();
                    while (it.hasNext()){
                        HSSFCell cell = (HSSFCell) it.next();
                        String dato = cell.getStringCellValue();
                    }
                }
            }
        } catch (IOException ex){
            Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sld;
    }
*/
    /**
     * Método que entrega en un String, el listado de campos invalidos encontrados
     * en la revisión del Header
     * @return
     */
    private String getStringCamposInvalidos(){
        String sld = "";
        HashMap hsMap = this.getCamposInvalidos();

        if (!hsMap.isEmpty()){
            Collection col = hsMap.values();
            Iterator it = col.iterator();
            while (it.hasNext()){
                String str = (String) it.next();
                sld = sld + str + ",";
            }
            if (sld.endsWith(",")){
                sld = sld.substring(0,sld.length()-1);
            }
        }
        return sld;
    }

    /**
     * Método que entrega en un String un dato limpio de caracteres en blanco
     * y en el caso de los numericos, con un formato capas de ser procesado por
     * la aplicación
     * @param dato  String con el dato a validar
     * @param caG   Objeto con la configuración del campo
     * @return  String  Texto con el dato limpio
     */
    private String getDatoClean(String dato, CargaArchivo caG){
        String sld = "";
        sld = dato.trim();
        if (String.class.getName().equals(getTypeDataApp(caG.getTipoCampo()))){
            if (sld.length()>=2){
                if (sld.startsWith("\"")){
                    sld = sld.substring(1);
                }
                if (sld.endsWith("\"")){
                    sld = sld.substring(0,sld.length()-1);
                }
            }
            sld = "\"" + sld + "\"";
        }
        if (Double.class.getName().equals(getTypeDataApp(caG.getTipoCampo()))){
            sld = reduceFormatDouble(sld,caG.getFormato());
        }
        return sld;
    }

    /**
     * Método que valida si todos los campos obligatorios estan en el header
     * declarado. Si el header hace referencia a los campos, al menos a todos los
     * obligatorios, estos se agregan a la variable global de campos validos
     * para despues permitir ubicar su posicion , según la posicion del campo en el
     * Header del archivo, ademas de la variable de Header Valido . Si llega a
     * faltar algún campo obligatorio, se rechaza la estructura completa.
     * @param header    Texto con el header de campos del archivo
     * @param listado   Listado con la configuración de campos para el archivo
     * @return  boolean Resultado obtenido de la operación (TRUE:exitoso, FALSE:Con errores)
     */
    private boolean existCampoInHeader(String header, List listado){
        boolean sld = true;
        HashMap hsValidCampos = new HashMap();
        HashMap hsValidAlias = new HashMap();
        HashMap hsInvalid = new HashMap();
        String headerList = "";

        if (listado!=null){
            Iterator it = listado.iterator();
            while (it.hasNext()){
                CargaArchivo ca = (CargaArchivo) it.next();
                if (header.contains(ca.getAliasCampo())){
                    //los guardamos para buscar su posicion
                    hsValidAlias.put(ca.getAliasCampo(), Integer.valueOf(0));
                }else if (header.contains(ca.getNombreCampo())){
                    //los guardamos para buscar su posicion
                    hsValidCampos.put(ca.getNombreCampo(), Integer.valueOf(0));
                }else{
                    //vemos si es obligatorio
                    if (ca.isObligatorio()){
                        //los guardamos para saber cuales faltaron
                        hsInvalid.put(ca.getNombreCampo(), Integer.valueOf(0));
                    }
                }
                headerList = headerList + ca.getNombreCampo() + ",";
            }
            if (!hsInvalid.isEmpty()){
                sld = false;
                this.setCamposInvalidos(hsInvalid);
            }else{
                CargaArchivo ca = (CargaArchivo) listado.get(0);
                if ((ca.getSeparador()!=null)&&(!"".equals(ca.getSeparador()))){
                    String[] spl = header.split(ca.getSeparador());
                    for (int i=0;i<spl.length;i++){
                        if (hsValidCampos.containsKey(spl[i])){
                            hsValidCampos.put(spl[i],Integer.valueOf(i));
                        }
                        if (hsValidAlias.containsKey(spl[i])){
                            hsValidCampos.put(spl[i],Integer.valueOf(i));
                        }
                    }
                    this.setCamposValidos(hsValidCampos);
                }else{
                    this.setHeaderValido(headerList);
                }
            }
        }
        return sld;
    }

    /**
     * Método que entrega el tipo de dato que le corresponde utilizar en la
     * aplicación según el tipo de dato entregado por la Base de Datos
     * @param strData   Tipo de dato entregado por la Base de Datos
     * @return  String  Texto con el nombre del tipo de Dato
     */
    private String getTypeDataApp(String strData){
        String sld = "";
        if ("INTEGER".equals(strData)){
            sld = Integer.class.getName();
        }else if ("INT".equals(strData)){
            sld = Integer.class.getName();
        }else if ("VARCHAR".equals(strData)){
            sld = String.class.getName();
        }else if ("NVARCHAR".equals(strData)){
            sld = String.class.getName();
        }else if ("CHAR".equals(strData)){
            sld = String.class.getName();
        }else if ("TEXT".equals(strData)){
            sld = String.class.getName();
        }else if ("SMALLDATETIME".equals(strData)){
            sld = Date.class.getName();
        }else if ("DATETIME".equals(strData)){
            sld = Date.class.getName();
        }else if ("FLOAT".equals(strData)){
            sld = Double.class.getName();
        }
        return sld;
    }

    /**
     * Método que entrega el número de veces que se repite un caracter en un texto.
     * Si entrega -1 es que hubo algun error en la operación.
     * @param strData   Texto en que se buscara
     * @param strChar   Texto o caracter a buscar
     * @return  String  Número con el conteo de ocurrencias encontradas
     */
    private int countChar(String strData, String strChar){
        int num = 0;
        try{
            String[] spl = strData.split(strChar);
            num = spl.length-1;
        }catch(Exception e){
            num = -1;
        }
        return num;
    }

    /**
     * Método para transformar de un formato numerico cualquiera a uno que puede
     * manejar la aplicacion. En Java los numeros decimales se manejan con una
     * estructura #.#, donde el punto es la separación del decimal. Si en la
     * parte entera existen comas u otro simbolo, este es eliminado para poder
     * dejarlo en una estructura que pueden reconocer los constructores numéricos
     * de Java.
     * @param strData   Número a transformar
     * @param strFormat Formato con el cual viene el número
     * @return  String  Número ya transformado
     */
    private String reduceFormatDouble(String strData, String strFormat){
        String sld = "";

        int nComa = countChar(strFormat,",");
        int nPunto = countChar(strFormat,"\\.");

        if ((nComa>=nPunto)&&(nPunto==1)){
            sld = strData.replaceAll(",","");
        }else if ((nComa<=nPunto)&&(nComa==1)){
            String[] spl = strData.split(",");
            for (int i=0;i<spl.length;i++){
                sld = sld + spl[i];
            }
        }else{
            sld = strData;
        }
        return sld;
    }

    /**
     * Método que determina si un dato debe ser considerado en la sumatoria
     * para validar el monto de los datos sumables y el monto total declarado
     * en el archivo, validandolo medisnte el calculo del pivote correspondiente
     * @param pivote            Texto con el pivote a analizar
     * @param pivoteAnterior    Texto con el pivote utilizado anteriormente
     * @return  boolean     Resultado con la operación
     */
    public boolean isPivoteAcumulable(String pivote, String pivoteAnterior, Integer idTipoPivote){
        boolean sld = false;
        
        if (idTipoPivote!=null){
            switch (idTipoPivote){
                case 1:
                    sld = pivoteCuenta(pivote,pivoteAnterior);
                    break;
                default:
                    break;
            }
        }
        return sld;
    }

    /**
     * Método que determina si una cuenta debe ser considerada en la sumatoria
     * para validar el monto de las cuentas y el monto declarado en el archivo
     * @param cuenta        Texto con la cuenta
     * @param cuentaPivote  Texto pivote, con la anterior cuenta utilizada en la suma
     * @return  boolean     Resultado con la operación
     */
    private boolean pivoteCuenta(String cuenta, String cuentaPivote){
        boolean sld = false;
        String[] splCuenta = cuenta.split("-");
        String[] splPivote = cuentaPivote.split("-");

        if (("".equals(cuentaPivote))||(cuenta.equals(cuentaPivote))) {
            sld = true;
        }else{
            if (splCuenta.length == splPivote.length){
                if (splPivote[0].equals(splCuenta[0])){
                    if ((!splPivote[1].equals(splCuenta[1]))&&(!splPivote[1].equals("000"))) {
                        sld = true;
                    }
                }else{
                    sld = true;
                }
            }
        }
        return sld;
    }

    /**
     * Método que escribe en un archivo estilo CSV, una fila con el texto entregado
     * @param dato      Texto a ingresar al archivo
     * @return  boolean Resultado de la operación (TRUE:Exitoso, FALSE:Con errores)
     */
    private boolean writeToFileCSV(String dato){
        boolean sld = true;
        FileWriter archivo = null;
        try {
            if (dato!=null){
                //Limpieza último caracter
                String ultChar = dato.substring(dato.length() - 1);
                if (",".equals(ultChar)) {
                    dato = dato.substring(0, dato.length() - 1);
                }
                //Obtener ruta archivo CSV
                String rutaFile = getFileBody();

                //Escritura del dato en archivo CSV
                archivo = new FileWriter(rutaFile, true);
                PrintWriter pw = null;
                if (archivo!=null){
                    pw = new PrintWriter(archivo);
                    pw.println(dato);
                }else{
                    archivo = new FileWriter(rutaFile,true);
                    pw = new PrintWriter(archivo);
                    pw.println(dato);
                }
            }
        } catch (IOException ex) {
            sld = false;
            //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (archivo!=null){
                    archivo.close();
                }
            } catch (IOException ex) {
                //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
                sld = false;
            }
        }
        return sld;
    }

    /**
     * Método que escribe en un archivo los totales de un CSV, este archivo sera
     * usado para validar el total de los registros
     * @param strHeader     String con el Header de campos del archivo
     * @param strTotales    String con los totales del archivo
     * @return  boolean     Resultado de la operiación (TRUE:existoso FALSE: Con errores)
     */
    private boolean writeToFileTotalesCSV(String strHeader, String strTotales){
        boolean sld = true;
        FileWriter archivo = null;
        try {
            if (strTotales!=null){
                //Limpieza último caracter
                String ultChar = strTotales.substring(strTotales.length() - 1);
                if (",".equals(ultChar)) {
                    strTotales = strTotales.substring(0, strTotales.length() - 1);
                }
                //Obtener ruta archivo CSV de totales
                String rutaFile = getFileTotales();

                //Escritura del dato en archivo CSV
                archivo = new FileWriter(rutaFile, true);
                PrintWriter pw = null;
                if (archivo!=null){
                    pw = new PrintWriter(archivo);
                    pw.println(strHeader);
                    pw.println(strTotales);
                }else{
                    archivo = new FileWriter(rutaFile,true);
                    pw = new PrintWriter(archivo);
                    pw.println(strHeader);
                    pw.println(strTotales);
                }
            }
        } catch (IOException ex) {
            sld = false;
            //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (archivo!=null){
                    archivo.close();
                }
            } catch (IOException ex) {
                //Logger.getLogger(FileImport.class.getName()).log(Level.SEVERE, null, ex);
                sld = false;
            }
        }
        return sld;
    }
}
