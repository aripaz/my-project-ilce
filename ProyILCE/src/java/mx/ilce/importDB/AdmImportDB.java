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

import com.csvreader.CsvReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.UtilDate;
import mx.ilce.util.Validation;

/**
 * Clase implementada para administrar la importación de datos desde archivos
 * @author ccatrilef
 */
class AdmImportDB {

    private String archivoCarga;
    private String rutaFile;
    private String rutaFileError;
    private String rutaFileCSV;
    private String rutaFileCSVTotal;
    private Integer idArchivoCarga;
    private Integer idTabla;
    private boolean stopError=false;
    private boolean existError=false;
    private String strQuery;
    private String strError;
    private Integer idEstadoCarga;
    private boolean totalCSV;

    private static String VALIDANDO = "1";
    private static String CARGANDO = "2";
    private static String FINALIZADO = "3";
    private static String ERROR_VALIDACION = "4";
    private static String ERROR_CARGA = "5";

    public static String processCARGA = "SP_UPL_CargaDatos";
    public static String processERROR = "SP_UPL_ERROR";
    private String storeProcedure;
    private List dataStoreProcedure;
    private List listHeader;
    private boolean includeHeader;
    private DataTable tablaProceso;

    public DataTable getTablaProceso() {
        return tablaProceso;
    }

    public void setTablaProceso(DataTable tablaProceso) {
        this.tablaProceso = tablaProceso;
    }

    private boolean isIncludeHeader() {
        return includeHeader;
    }

    private void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public List getListHeader() {
        return listHeader;
    }

    public void setListHeader(List listHeader) {
        this.listHeader = listHeader;
    }

    private List getDataStoreProcedure() {
        return dataStoreProcedure;
    }

    private void setDataStoreProcedure(List dataStoreProcedure) {
        this.dataStoreProcedure = dataStoreProcedure;
    }

    public String getStoreProcedure() {
        return storeProcedure;
    }

    public void setStoreProcedure(String storeProcedure) {
        this.storeProcedure = storeProcedure;
    }

    public String getRutaFileError() {
        return rutaFileError;
    }

    public void setRutaFileError(String rutaFileError) {
        this.rutaFileError = rutaFileError;
    }

    public String getRutaFileCSVTotal() {
        return rutaFileCSVTotal;
    }

    public void setRutaFileCSVTotal(String rutaFileCSVTotal) {
        this.rutaFileCSVTotal = rutaFileCSVTotal;
    }

    public boolean isTotalCSV() {
        return totalCSV;
    }

    public void setTotalCSV(boolean totalCSV) {
        this.totalCSV = totalCSV;
    }

    public Integer getIdEstadoCarga() {
        return idEstadoCarga;
    }

    public void setIdEstadoCarga(Integer idEstadoCarga) {
        this.idEstadoCarga = idEstadoCarga;
    }

    public String getRutaFileCSV() {
        return rutaFileCSV;
    }

    public void setRutaFileCSV(String rutaFileCSV) {
        this.rutaFileCSV = rutaFileCSV;
    }

    public boolean isExistError() {
        return existError;
    }

    public void setExistError(boolean existError) {
        this.existError = existError;
    }

    public boolean isStopError() {
        return stopError;
    }

    public void setStopError(boolean stopError) {
        this.stopError = stopError;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public String getStrQuery() {
        return ((strQuery==null)?"":strQuery);
    }

    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    public Integer getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(Integer idTabla) {
        this.idTabla = idTabla;
    }

    public Integer getIdArchivoCarga() {
        return ((idArchivoCarga!=null)?idArchivoCarga:0);
    }

    public void setIdArchivoCarga(Integer idArchivoCarga) {
        this.idArchivoCarga = idArchivoCarga;
    }

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    public String getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(String archivoCarga) {
        this.archivoCarga = archivoCarga;
    }

    /**
     * Constructor básico de la clase
     */
    public AdmImportDB() {
    }

    /**
     * Método para recorrer y desglosar datos de un archivo CSV sin el total
     * para las columnas sumables
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     */
    private boolean desglosarCSV_ST(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = false;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeader(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyCSV_ST(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyCSV_ST(0,ca,lstData);
            }
            //Asignamos archivo de trabajo para carga
            setRutaFileCSV(fImport.getFileBody());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Método para recorrer y desglosar datos de un archivo CSV con el total
     * para las columnas sumables
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     */
    private boolean desglosarCSV_CT(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = false;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());
        fImport.setFileTotales(getNameFileCSVTotales());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeader(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyCSV_CT(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyCSV_CT(0,ca,lstData);
            }
            //Asignamos archivo de trabajo para carga
            this.setRutaFileCSV(fImport.getFileBody());
            this.setRutaFileCSVTotal(fImport.getFileTotales());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Método para recorrer y desglosar datos de un archivo PLANO sin el total
     * para las columnas sumables
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     */
    private boolean desglosarPLANO_ST(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = false;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeader(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyPLANO_ST(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyPLANO_ST(0,ca,lstData);
            }
            //Asignamos archivo de trabajo para carga
            setRutaFileCSV(fImport.getFileBody());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Método para recorrer y desglosar datos de un archivo PLANO con el total
     * para las columnas sumables
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     */
    private boolean desglosarPLANO_CT(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = false;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());
        fImport.setFileTotales(getNameFileCSVTotales());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeader(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyPLANO_CT(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyPLANO_CT(0,ca,lstData);
            }
            //Asignamos archivo de trabajo para carga
            setRutaFileCSV(fImport.getFileBody());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Método para recorrer y desglosar datos de un archivo Excel simple el cual
     * puede tener o no totales para validar
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     * @throws ExceptionHandler
     */
    private boolean desglosarXLSSimple(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = true;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeaderXLS(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyXLSSimple(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyXLSSimple(0,ca,lstData);
            }
            //asignamos archivo de trabajo para carga
            setRutaFileCSV(fImport.getFileBody());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Método para recorrer y desglosar datos de un archivo Excel simple el cual
     * puede tener o no totales para validar
     * @param filaHeader    Número de la fila en que se encuentra el header del archivo
     * @param ca            Objeto con la configuración general del archivo
     * @param lstData       Listado de los campos para el tipo de archivo
     * @return  boolean     Resultado de la lectura y validación
     * @throws ExceptionHandler
     */
    private boolean desglosarXLSSeparador(Integer filaHeader, CargaArchivo ca, List lstData) throws ExceptionHandler{
        boolean sld = true;
        FileImport fImport = new FileImport();
        fImport.setFileProcess(this.getRutaFile());
        fImport.setFileBody(getNameFileCSV());

        //Trabajamos con datos que no estan en registros
        List lstHeader = getListCamposHeader();
        if ((lstHeader!=null) && (!lstHeader.isEmpty())) {
            sld = fImport.putFileHeaderXLS(lstHeader);
            if (sld){
                lstHeader = fImport.getListHeaders();
                this.setListHeader(lstHeader);
            }
        }
        if (sld){
            List lstSeparador = getListCamposSeparador();
            fImport.setListSeparador(lstSeparador);
            //Trabajamos con los registros
            if (filaHeader!=null){
                sld = fImport.getFileBodyXLSSeparador(filaHeader,ca,lstData);
            }else{
                sld = fImport.getFileBodyXLSSeparador(0,ca,lstData);
            }
            this.setIncludeHeader(fImport.isIncludeHeader());
            //asignamos archivo de trabajo para carga
            setRutaFileCSV(fImport.getFileBody());
        }
        if (!sld){
            setStrError(fImport.getTextError());
        }
        return sld;
    }

    /**
     * Se obtiene un listado con los siguientes elementos:
     * (-) Las secciones del archivo: ENCABEZADO, HEADERS, BODY, FOOTER.
     * (-) Los totales de cada campo sumable
     * (-) Número de registros que debía poseer el archivo
     * @param listCampos    Listado con la configuración de campos del archivo
     * @return  boolean     Resultado de la operación (TRUE: exitoso, FALSE: con errores)
     */
    private boolean desglosarArchivo(List listCampos) throws ExceptionHandler{
        boolean sld = false;
        Integer filaHeader = null;
        Integer typeFile = null;

        List lstData = (List)listCampos.get(2);
        CargaArchivo ca = null;
        if (!lstData.isEmpty()){
            ca = (CargaArchivo) lstData.get(0);
        }
        if (ca!=null){
            filaHeader = ca.getNroFilaHeader();
            typeFile = ca.getIdTipoArchivoCarga();
        }
        if (typeFile!=null){
            switch (typeFile){
                case 1://CSV_ST:
                    sld = desglosarCSV_ST(filaHeader,ca,lstData);
                    break;
                case 2://CSV_CT:
                    sld = desglosarCSV_CT(filaHeader,ca,lstData);
                    this.setTotalCSV(true);
                    break;
                case 3://PLANO_ST:
                    sld = desglosarPLANO_ST(filaHeader,ca,lstData);
                    break;
                case 4://PLANO_CT:
                    sld = desglosarPLANO_CT(filaHeader,ca,lstData);
                    break;
                case 5://Excel Simple
                    sld = desglosarXLSSimple(filaHeader,ca,lstData);
                    break;
                case 6://Excel con separador
                    sld = desglosarXLSSeparador(filaHeader,ca,lstData);
                    break;
                default:
                   break;
            }
        }
        return sld;
    }

    /**
     * Método que obtiene el objeto HashMap con los totales de los campos que
     * son sumables, inicializados con valor cero (0).
     * @param listCampos    Listado con la configuración de campos
     * @return  HashMap     Objeto HashMap con los totales en cero (0)
     */
    private HashMap getHashTotales(List listCampos){
        HashMap sld = new HashMap();

        if (listCampos!=null){
            Iterator it = listCampos.iterator();
            while (it.hasNext()){
                CargaArchivo car = (CargaArchivo) it.next();
                if (car.isSumable()){
                    sld.put(car.getNombreCampo(), Double.valueOf(0.0));
                }
            }
        }
        return sld;
    }

    /**
     * Método que incluye los campos header en el hashMap de campo, ordenados
     * por nombre
     * @param hsNombre      Objeto Hashmap con los campos ordenados por nombre
     * @return  HashMap     Objeto con los campos agregados
     * @throws ExceptionHandler
     */
    private HashMap includeHeader(HashMap hsNombre) throws ExceptionHandler{
        HashMap hsSld = new HashMap();
        
        hsSld = hsNombre;
        List lstHeader = getListCamposSeparador();

        if (lstHeader!=null){
            List lstCamp = (List) lstHeader.get(0);
            Iterator it = lstCamp.iterator();
            while (it.hasNext()){
                CargaArchivo ca = (CargaArchivo) it.next();
                hsSld.put(ca.getNombreCampo(), ca);
            }
        }
        return hsSld;
    }

    /**
     * Método que procesa la lectura, validación y conversión de un archivo
     * a un conjunto de Insert en la tabla configurada, según el tipo de carga
     */
    public boolean procesarArchivo(){
        Validation val = new Validation();
        CsvReader reader = null;
        CsvReader readerTot = null;
        boolean sld = true;
        try{
            List lst = getListCampos();
            HashMap hsNombre = (HashMap) lst.get(0);
            HashMap hsAlias = (HashMap) lst.get(1);
            HashMap hsTotales = new HashMap();
            DataTable dt = getDataTable();
            this.setTablaProceso(dt);

            if (desglosarArchivo(lst)){
                List lstColum = new ArrayList();
                if (!lst.isEmpty()){
                    if (this.isIncludeHeader()){
                        hsNombre = includeHeader(hsNombre);
                    }
                    reader = new CsvReader(getRutaFileCSV());
                    reader.setDelimiter(',');
                    if (this.isTotalCSV()){
                        hsTotales = getHashTotales((List) lst.get(2));
                    }
                    int fila = 1;
                    if (reader.readHeaders()){
                        int numCols = reader.getHeaderCount();
                        for (int i=0;i<numCols;i++){
                            String nombreCol = reader.getHeader(i);
                            if ((hsNombre.containsKey(nombreCol)) ||
                                (hsAlias.containsKey(nombreCol))){
                                lstColum.add(nombreCol);
                            }
                        }
                    }
                    String tabla = "";
                    if (!lstColum.isEmpty()){
                        CargaArchivo car = (CargaArchivo) hsNombre.get(lstColum.get(0));
                        tabla = car.getTabla();
                        if (this.getTablaProceso()!=null){
                            tabla = this.getTablaProceso().getDominioTable() + "." + tabla;
                        }
                        String dataError = "";
                        String cuenta = "";
                        String cuentaAnterior = "";
                        while (reader.readRecord()) {
                            String strlistCampo = "(";
                            String strListDato = "(";
                            boolean errorOblig = false;
                            boolean obligatorio = false;
                            boolean pivote = false;
                            for (int i=0;i<lstColum.size();i++){
                                CargaArchivo caNombre = (CargaArchivo) hsNombre.get(lstColum.get(i));
                                CargaArchivo caAlias = (CargaArchivo) hsNombre.get(lstColum.get(i));
                                String strData = "";
                                String strTipo = "";
                                String strCampo = "";
                                String strFormato = "";
                                obligatorio = false;
                                pivote = false;
                                if (caNombre!=null){
                                    strData = reader.get(caNombre.getNombreCampo());
                                    strTipo = caNombre.getTipoCampo();
                                    strCampo = caNombre.getNombreCampo();
                                    strFormato = caNombre.getFormato();
                                    obligatorio = caNombre.isObligatorio();
                                    pivote = caNombre.isPivote();
                                }else if (caAlias !=null){
                                    strData = reader.get(caAlias.getAliasCampo());
                                    strTipo = caAlias.getTipoCampo();
                                    strCampo = caAlias.getNombreCampo();
                                    strFormato = caAlias.getFormato();
                                    obligatorio = caAlias.isObligatorio();
                                    pivote = caAlias.isPivote();
                                }
                                if (!"".equals(strData)){
                                    if ((strTipo.equals("INTEGER")) ||
                                        (strTipo.equals("INT")) ){
                                        if (val.isNumberInteger(strData)){
                                            strlistCampo = strlistCampo + strCampo + ",";
                                            strListDato = strListDato + strData + ",";
                                        }else{
                                            dataError = setCampoErrorCSV(dataError,fila,strCampo,strData,"No es numérico");
                                            if (obligatorio){
                                                errorOblig = true;
                                            }
                                        }
                                    }else if ((strTipo.equals("FLOAT"))){
                                        if (val.isNumberDouble(strData)){
                                            strlistCampo = strlistCampo + strCampo + ",";
                                            strListDato = strListDato + strData + ",";
                                            if (this.isTotalCSV()&& isPivoteAcumulable(cuenta, cuentaAnterior)){
                                                Double dbl = Double.valueOf(strData);
                                                Double dblTot = (Double) hsTotales.get(strCampo);
                                                dblTot = dblTot + dbl;
                                                hsTotales.put(strCampo, dblTot);
                                            }
                                        }else{
                                            dataError = setCampoErrorCSV(dataError,fila,strCampo,strData,"No es numérico");
                                            if (obligatorio){
                                                errorOblig = true;
                                            }
                                        }
                                    }else if ((strTipo.equals("SMALLDATETIME")) ||
                                              (strTipo.equals("DATETIME")) ){
                                        UtilDate ut = new UtilDate(strData,strFormato);
                                        if (ut.getFechaHMS().equals("00/00/0000/ 00:00:00")){
                                            dataError = setCampoErrorCSV(dataError,fila,strCampo,strData,"No es fecha valida");
                                            if (obligatorio){
                                                errorOblig = true;
                                            }
                                        }else{
                                            strlistCampo = strlistCampo + strCampo + ",";
                                            strListDato = strListDato + "'" + ut.getFechaHMS() + "',";
                                        }
                                    }else if ((strTipo.equals("VARCHAR")) ||
                                              (strTipo.equals("TEXT")) ){
                                        if (pivote){
                                            cuenta = strData;
                                            if (isPivoteAcumulable(cuenta, cuentaAnterior)){
                                                cuentaAnterior = strData;
                                            }
                                        }
                                        strlistCampo = strlistCampo + strCampo + ",";
                                        strListDato = strListDato + "'" + strData + "',";
                                    }
                                }
                            }
                            if (!errorOblig){
                                addCampoInsert(strlistCampo, strListDato,tabla);
                            }
                            fila++;
                        }
                        if (this.isTotalCSV()){
                            readerTot = new CsvReader(this.getRutaFileCSVTotal());
                            readerTot.setDelimiter(',');
                            if (readerTot.readHeaders()){
                            }
                            if (readerTot.readRecord()) {
                                for (int i=0;i<lstColum.size();i++){
                                    CargaArchivo caNombre = (CargaArchivo) hsNombre.get(lstColum.get(i));
                                    if (caNombre.isSumable()){
                                        String strData = readerTot.get(caNombre.getNombreCampo());
                                        Double dblTot = (Double) hsTotales.get(caNombre.getNombreCampo());
                                        Double dbl = Double.valueOf(strData);
                                        if (dblTot.compareTo(dbl)!=0){
                                            dataError = setCampoErrorCSV(dataError,fila,caNombre.getNombreCampo(),strData,
                                                    "No es igual el Total a la suma de los campos");
                                        }
                                    }
                                }
                            }
                        }
                        //se presentaron errores en algun instante de la operacion
                        if (!dataError.equals("")){
                            sld = false;
                            this.setStrError("\nMessage:Existen errores en la operación."
                                    + "Error:" + dataError
                                    + this.getStrError());
                            this.setExistError(true);
                        }
                    }else{
                        sld = false;
                        this.setStrError("\nMessage:No existen campos configurados que coincidan con el archivo."
                            + "Error:"
                            + this.getStrError());
                        this.setExistError(true);
                    }
                }else{
                    sld = false;
                    this.setStrError("\nMessage:No existe configuración para el tipo de archivo."
                            + "Error:"
                            + this.getStrError());
                    this.setExistError(true);
                }
            }else{
                sld = false;
                this.setStrError("\nMessage:Problemas al generar archivo de carga."
                            + "Error:"
                            + this.getStrError());
                this.setExistError(true);
            }
        }catch (ExceptionHandler eh){
            eh.writeToFile();
            sld = false;
            this.setStrError("\nMessage:" + eh.getTextMessage()
                        + "Error:" + eh.getTextError()
                        + ((eh.getStrQuery()!=null)?"Query:"+eh.getStrQuery():""));
            this.setExistError(true);
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,AdmImportDB.class,"Error al generar archivo de carga");
            sld = false;
            this.setStrError("\nMessage:" + eh.getTextMessage()
                        + "Error:" + eh.getTextError()
                        + ((eh.getStrQuery()!=null)?"Query:"+eh.getStrQuery():""));
            this.setExistError(true);
        }finally{
            if (reader!=null){
                reader.close();
            }
            if (readerTot!=null){
                readerTot.close();
            }
        }
        return sld;
    }

    /**
     * Método que determina si un dato debe ser considerado en la sumatoria
     * para validar el monto de los datos sumables y el monto total declarado
     * en el archivo, validándolo mediante el cálculo del pivote correspondiente
     * @param pivote            Texto con el pivote a analizar
     * @param pivoteAnterior    Texto con el pivote utilizado anteriormente
     * @return  boolean         Resultado de la operación
     */
    private boolean isPivoteAcumulable(String pivote, String pivoteAnterior){
        boolean sld = false;
        FileImport fImport = new FileImport();
        sld = fImport.isPivoteAcumulable(pivote, pivoteAnterior, 1);
        return sld;
    }


    /**
     * Método que genera un mensaje de error genérico, para ser capturado como salida
     * para el resultado de las operaciones
     * @param strError  String con los errores capturados anteriormente
     * @param fila      Número de la fila del archivo donde se produjo el error
     * @param strCampo  Nombre del campo que causo el error
     * @param strData   Dato del campo que causo el error
     * @param texto     Texto con el mensaje de error capturado
     * @return String   Texto con los errores acumulados
     */
    private String setCampoErrorCSV(String strError, int fila, String strCampo, String strData, String texto){
        String dataError = "";

        dataError = ((strError==null)?"":strError)
                  + "\nFila:" + fila
                  + " Campo:" + ((strCampo==null)?"":strCampo)
                  + " Data:" + ((strData==null)?"":strData)
                  + " Error: " + ((texto==null)?"":texto);
        return dataError;
    }

    /**
     * Método que genera la query de Insert con los datos entregados
     * @param strListadoCampos  String con los nombres de campos recuperados
     * @param strListadoDatos   String con los dados de campos recuperados
     * @param tabla             Nombre de la tabla donde se hara el insert
     */
    private void addCampoInsert(String strListadoCampos, String strListadoDatos, String tabla){
        boolean seguir = true;
        String query = "";
        if (!strListadoCampos.equals("(")){
            String strLast = strListadoCampos.substring(strListadoCampos.length()-1);
            if (strLast.equals(",")){
                strListadoCampos = strListadoCampos.substring(0,strListadoCampos.length()-1);
            }
        }else{
            seguir = false;
        }
        if (!strListadoDatos.equals("(")){
            String strLast = strListadoDatos.substring(strListadoDatos.length()-1);
            if (strLast.equals(",")){
                strListadoDatos = strListadoDatos.substring(0,strListadoDatos.length()-1);
            }
        }else{
            seguir = false;
        }
        if (seguir){
            query = "insert into " +  tabla + " "
                    + strListadoCampos + ",id_EstadoCarga"
                    + ") values "
                    + strListadoDatos + "," + this.getIdEstadoCarga()
                    + ")\n";
            this.setStrQuery(this.getStrQuery()+query);
        }
    }

    /**
     * Método que convierte a un XML el resultado de operaciones. El requisito
     * es que en el campo strError, cada fila este separada por un Enter.
     * @return  String  Texto del XML obtenido
     */
    public String getXMLError(){
        String sld = "";

        String[] str = this.getStrError().split("\n");
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n")
           .append("<error>\n");
        for (int i=0;i<str.length;i++){
            String data = str[i];
            if (data.length()>0){
                //Errores con una estructura
                if ((data.contains("Fila:")) &&
                    (data.contains("Campo:")) &&
                    (data.contains("Data:")) &&
                    (data.contains("Error:")) ){
                    int posCampo = data.indexOf("Campo:");
                    int posData = data.indexOf("Data:");
                    int posError = data.indexOf("Error:");
                    xml.append("<numero id=\"").append(i).append("\">\n")
                       .append("\t<descripcion>")
                       .append(data.substring(posError+6))
                       .append("</descripcion>\n")
                       .append("\t<fila>")
                       .append(data.substring(5,posCampo))
                       .append("</fila>\n")
                       .append("\t<campo>")
                       .append(data.substring(posCampo+6,posData))
                       .append("</campo>\n")
                       .append("\t<data>")
                       .append(data.substring(posData+5,posError))
                       .append("</data>\n")
                       .append("</numero>\n");
                //Errores sin una estructura
                }else{
                    if ((data.contains("Message:")) &&
                        (data.contains("Error:"))){
                        int posMessage = data.indexOf("Message:");
                        int posError = data.indexOf("Error:");
                        int posQuery = data.indexOf("Query:");
                        xml.append("<numero id=\"").append(i).append("\">\n")
                           .append("<general>")
                           .append(data.substring(posMessage+8,posError))
                           .append("</general>");
                           if (posQuery>0){
                                xml.append("<descripcion>")
                                   .append(data.substring(posError+6,posQuery))
                                   .append("</descripcion>");
                                xml.append("<query><![CDATA[\n")
                                   .append(data.substring(posQuery+6))
                                   .append("]]>\n</query>\n");
                           }else{
                                xml.append("<descripcion>")
                                   .append(data.substring(posError+6))
                                   .append("</descripcion>");
                           }
                        xml.append("</numero>\n");
                    }else{
                        xml.append("<numero id=\"").append(i).append("\">\n")
                           .append("\t<descripcion>")
                           .append(data)
                           .append("</descripcion>\n")
                           .append("</numero>\n");
                    }
                }
            }
        }
        xml.append("</error>");
        sld = xml.toString();
        return sld;
    }

    /**
     * Método que calcula el nombre del archivo imagen CSV de un archivo de carga
     * @return
     */
    private String getNameFileCSV(){
        String strRutaFile = getRutaFile();
        if ((strRutaFile != null) && (!"".equals(strRutaFile))) {
            String[] spl = strRutaFile.split("\\.");
            String rutaNew = "";
            if (spl.length > 2) {
                for (int i = 0; i < spl.length - 1; i++) {
                    rutaNew = rutaNew + spl[i] + ".";
                }
            } else {
                rutaNew = spl[0];
            }
            rutaNew = rutaNew + "-CSV." + spl[spl.length - 1];
            strRutaFile = rutaNew;
        }
        return strRutaFile;
    }

    /**
     * Método que calcula el nombre del archivo imagen CSVTotales de un archivo de carga
     * @return
     */
    private String getNameFileCSVTotales(){
        String strRutaFile = getRutaFile();
        if ((strRutaFile != null) && (!"".equals(strRutaFile))) {
            String[] spl = strRutaFile.split("\\.");
            String rutaNew = "";
            if (spl.length > 2) {
                for (int i = 0; i < spl.length - 1; i++) {
                    rutaNew = rutaNew + spl[i] + ".";
                }
            } else {
                rutaNew = spl[0];
            }
            rutaNew = rutaNew + "-CSVTOTAL." + spl[spl.length - 1];
            strRutaFile = rutaNew;
        }
        return strRutaFile;
    }

    /**
     * Método que obtiene el listado de tipos de archivos que se pueden cargar
     * @return  List    Listado con los tipos de archivos, cada uno es un objeto Archivo
     * @throws ExceptionHandler
     */
    public List getListArchivos() throws ExceptionHandler{
        List lst = new ArrayList();
        String query = "select id_ArchivoCarga, archivoCarga , id_Tabla , id_TipoArchivoCarga "
                + " from UPL_ArchivoCarga";

        ConImportDB con = getConection();
        con.setStrQuery(query);

        lst = con.getListArchivo();

        return lst;
    }

    /**
     * Método que inserta el estado VALIDANDO en la base de datos, del archivo
     * de carga que se está cargando
     * @return  boolean resultado de la operación
     */
    public boolean insertEstadoValidando(){
        boolean sld = true;

        try{
            this.setRutaFileCSV(this.getNameFileCSV());

            String query = "insert into UPL_EstadoCarga "
                    + "(archivo, archivoImagen, id_EstadoArchivo"
                    + ", id_ArchivoCarga, fechaValidacion) "
                    + " values ("
                    + "'" + ((this.getRutaFile().length()>250)?
                            this.getRutaFile().substring(0,249):this.getRutaFile()) + "',"
                    + "'" + ((this.getRutaFileCSV().length()>250)?
                            this.getRutaFileCSV().substring(0,249):this.getRutaFileCSV()) + "',"
                    + VALIDANDO + ","
                    + this.getIdArchivoCarga()
                    + ",getdate()"
                    + ")";

            ConImportDB con = getConection();
            con.setStrQuery(query);
            Integer idResultado = con.executeInsert();
            this.setIdEstadoCarga(idResultado);
        }catch (Exception ex){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Validando de la carga."
                            + "Error:" + ex.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }catch (ExceptionHandler e){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Validando de la carga."
                            + "Error:" + e.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }
        return sld;
    }

    /**
     * Método que actualiza el estado del archivo de carga a CARGANDO
     * @return  boolean resultado de la operación
     */
    public boolean updateEstadoCargando(){
        boolean sld = true;

        try{
            setRutaFileCSV(getNameFileCSV());

            String query = "update UPL_EstadoCarga "
                    + " set id_EstadoArchivo = " + CARGANDO
                    + " , fechacarga = getdate() "
                    + " where id_EstadoCarga = "
                    + this.getIdEstadoCarga();

            ConImportDB con = getConection();
            con.setStrQuery(query);
            Integer idResultado = con.executeUpdate();
            if (idResultado<0){
                sld = false;
            }
        }catch (Exception ex){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Cagando de la carga."
                            + "Error:" + ex.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }catch (ExceptionHandler e){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Cargando de la carga."
                            + "Error:" + e.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }
        return sld;
    }

    /**
     * Método que actualiza el estado del archivo de carga a CARGANDO
     * @return  boolean resultado de la operación
     */
    public boolean updateEstadoFinalizado(){
        boolean sld = true;

        try{
            setRutaFileCSV(getNameFileCSV());

            String query = "update UPL_EstadoCarga "
                    + " set id_EstadoArchivo = " + FINALIZADO
                    + " , fechaFinalizado = getdate() "
                    + " where id_EstadoCarga = "
                    + this.getIdEstadoCarga();

            ConImportDB con = getConection();
            con.setStrQuery(query);
            Integer idResultado = con.executeUpdate();
            if (idResultado<0){
                sld = false;
            }
        }catch (Exception ex){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Finalizado de la carga."
                            + "Error:" + ex.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }catch (ExceptionHandler e){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado Finalizado de la carga."
                            + "Error:" + e.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }
        return sld;
    }

    /**
     * Método que actualiza el estado del archivo de carga a ERROR_CARGA,
     * además de guardar el archivo con el error
     * @return  boolean resultado de la operación
     */
    public boolean updateEstadoErrorCarga(){
        boolean sld = true;
        try{
            setRutaFileCSV(getNameFileCSV());

            String query = "update UPL_EstadoCarga "
                    + " set id_EstadoArchivo = "
                    + ERROR_CARGA
                    + " , archivoImagen = "
                    + "'" + ((this.getRutaFileError().length()>250)?
                            this.getRutaFileError().substring(0,249):this.getRutaFileError())
                    + " where id_EstadoCarga = "
                    + this.getIdEstadoCarga();

            ConImportDB con = getConection();
            con.setStrQuery(query);
            Integer idResultado = con.executeUpdate();
            if (idResultado<0){
                sld = false;
            }
        }catch (Exception ex){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado de Error de la carga."
                            + "Error:" + ex.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }catch (ExceptionHandler e){
            sld = false;
            this.setStrError("\nMessage:Error al actualizar el estado de Error de la carga."
                            + "Error:" + e.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }
        return sld;
    }

    /**
     * Método que actualiza el estado del archivo de carga a ERROR_VALIDACION,
     * además de guardar el archivo con el error
     * @return  boolean resultado de la operación
     */
    public boolean updateEstadoErrorValidación(){
        boolean sld = true;
        try{
            setRutaFileCSV(getNameFileCSV());

            String query = "update UPL_EstadoCarga "
                    + " set id_EstadoArchivo = "
                    + ERROR_VALIDACION
                    + " , archivoImagen = "
                    + "'" + ((this.getRutaFileError().length()>250)?
                            this.getRutaFileError().substring(0,249):this.getRutaFileError())
                    + " where id_EstadoCarga = "
                    + this.getIdEstadoCarga();

            ConImportDB con = getConection();
            con.setStrQuery(query);
            Integer idResultado = con.executeUpdate();
            if (idResultado<0){
                sld = false;
            }
        }catch (Exception ex){
            sld = false;
            this.setStrError("\nMessage:Error al agregar la data al Store Procedure."
                            + "Error:" + ex.getMessage()
                            + this.getStrError());
            this.setExistError(true);
        }catch (ExceptionHandler e){
            sld = false;
        }
        return sld;
    }

    /**
     * Método que obtiene el listado de campos a manejar en el archivo de carga
     * @return  List    Listado con los campos, cada uno es un objeto CargaArchivo
     * @throws ExceptionHandler
     */
    private List getListCampos() throws ExceptionHandler{
        List lst = new ArrayList();
        String query = "select ct.id_CampoTabla , ct.nombreCampo, ct.id_TipoCampo "
                + " , tc.tipoCampo , ct.aliasCampo , ct.obligatorio "
                + " , tb.nombreTabla , ct.formato , ct.sumable "
                + " , ct.posicionInicio, ct.posicionHeader "
                + " , ct.largo , ct.posicionInicioTotal , ct.largoTotal "
                + " , ac.nroFilaHeader, ac.tagTotales, ac.tagNroRegistros "
                + " , ac.separador , ct.pivote , ac.id_TipoArchivoCarga "
                + " , ac.posicionSeparador, ac.ignoreIncomplete "
                + " from UPL_CampoTabla ct , UPL_TipoCampo tc "
                + ", UPL_ArchivoCarga ac, UPL_Tabla tb "
                + " where ac.id_ArchivoCarga = " + this.getIdArchivoCarga()
                + " and ct.id_ArchivoCarga = ac.id_ArchivoCarga "
                + " and ct.id_TipoCampo = tc.id_TipoCampo "
                + " and ac.id_Tabla = tb.id_Tabla "
                + " order by ct.orden asc ";

        ConImportDB con = getConection();
        con.setStrQuery(query);
        lst = con.getCampos();
        return lst;
    }

    /**
     * Método que obtiene los campos Header que están asociados a la
     * configuración de la carga de un archivo. Se excluyen aquellos que son
     * usados como parte del separador de registros
     * @return  List    Listado con los campos, cada uno es un objeto CargaArchivo
     * @throws ExceptionHandler
     */
    private List getListCamposHeader() throws ExceptionHandler{
        List sld = new ArrayList();
        String query = "select ch.id_CampoHeader, ch.nombreCampo"
                + ", ch.id_TipoCampo, ch.fila, ch.columna, ch.tagCampoHeader "
                + ", ch.posicionInicio, ch.largo, ch.formato, tc.tipoCampo "
                + " from UPL_CampoHeader ch , UPL_TipoCampo tc "
                + " where ch.id_ArchivoCarga = " + this.getIdArchivoCarga()
                + " and ch.id_TipoCampo = tc.id_TipoCampo "
                + " and ch.useInSeparador = 0 ";

        ConImportDB con = getConection();
        con.setStrQuery(query);
        sld = con.getCamposHeader();

        return sld;
    }

    /**
     * Método que obtiene los campos Separadores y que serán usados para ser
     * asociados al grupo de registros que corresponda en el archivo.
     * @return  List    Listado con los campos Separadores, cada uno es un objeto CargaArchivo
     * @throws ExceptionHandler
     */
    private List getListCamposSeparador() throws ExceptionHandler{
        List sld = new ArrayList();
        String query = "select ch.id_CampoHeader, ch.nombreCampo"
                + ", ch.id_TipoCampo, ch.fila, ch.columna, ch.tagCampoHeader "
                + ", ch.posicionInicio, ch.largo, ch.formato, tc.tipoCampo "
                + " from UPL_CampoHeader ch , UPL_TipoCampo tc "
                + " where ch.id_ArchivoCarga = " + this.getIdArchivoCarga()
                + " and ch.id_TipoCampo = tc.id_TipoCampo "
                + " and ch.useInSeparador = 1 "
                + " order by ch.posicionInicio ";

        ConImportDB con = getConection();
        con.setStrQuery(query);
        sld = con.getCamposHeader();

        return sld;
    }

    /**
     * Método que efectúa el procesamiento de las queries obtenidas tras la
     * lectura y validación del archivo de carga
     * @return  boolean     Resultado con la operación
     */
    public boolean processQuery() throws ExceptionHandler{
        boolean sld = true;
        String strData = this.getStrQuery();
        String[] strDataQuery = strData.split("\n");

        if (strDataQuery!=null){
            ConImportDB con = getConection();
            for (int i=0;i<strDataQuery.length;i++){
                String query = strDataQuery[i];
                if (query!=null){
                    query = query.trim();
                    if (query.length()>0){
                        con.setStrQuery(query);
                        Integer idResultado = con.executeInsert();
                        if (idResultado<=0){
                            sld = false;
                            this.setStrError("\nMessage:Error al insertar registro."
                                            + "Error:"
                                            + "Query:" + query + this.getStrError());
                            this.setExistError(true);
                        }
                    }
                }
            }
        }
        if (sld){
            List listaHeader = this.getListHeader();
            if (listaHeader!=null){
                sld = processHeader(listaHeader);
            }
        }
        return sld;
    }

    /**
     * Método que ingresa a la base de datos los header de un proceso de
     * carga de datos
     * @param listaHeader   Listado con los header a ingresar
     * @return
     */
    private boolean processHeader(List listaHeader){
        boolean sld = true;
        if (listaHeader!=null){
            ConImportDB con = getConection();
            Iterator it = listaHeader.iterator();
            while ((it.hasNext()) && sld) {
                List lst = (List) it.next();
                String tipo = (String) lst.get(0);
                String dato = (String) lst.get(1);
                String query = "insert into UPL_DataHeader "
                        + "(id_EstadoCarga,tipoData,dataHeader) values "
                        + "(" + this.getIdEstadoCarga()
                        + ",'" + tipo + "','" + dato + "')";
                con.setStrQuery(query);
                try {
                    con.executeInsert();
                } catch (ExceptionHandler ex) {
                    this.setStrError("\nMessage:Error al insertar registro."
                                    + "Error:" + ex.getTypeError()
                                    + "Query:" + query + this.getStrError());
                    this.setExistError(true);
                    sld = false;
                }
            }
        }
        return sld;
    }

    /**
     * Método que ejecuta el Store Procedure asociado a la configuración de la
     * carga de archivo
     * @return  boolean     Resultado de la operación
     * @throws ExceptionHandler
     */
    public boolean processStoreProcedure() throws ExceptionHandler{
        boolean sld = true;

        String strProcedure = this.getStoreProcedure();
        List lstData = this.getDataStoreProcedure();

        ConImportDB con = getConection();

        con.executeProcedure(strProcedure, lstData);

        return sld;
    }

    /**
     * Método que agrega un objeto al listado de data que se anexara en el
     * llamado a un Store Procedure
     * @param obj   Objeto que se agregara
     * @return  boolean     Resultado de la operación
     */
    public boolean addToDataStoreProcedure(Object obj){
        boolean sld = true;
        try{
            if (obj!=null){
                List lst = new ArrayList();
                lst.add(obj.getClass().getName());
                lst.add(obj);
                if (this.dataStoreProcedure==null){
                    this.dataStoreProcedure = new ArrayList();
                }
                this.dataStoreProcedure.add(lst);
            }
        }catch (Exception e){
            sld = false;
            this.setStrError("\nMessage:Error al agregar la data al Store Procedure. "
                            + "Error:"+e.getMessage()+"\n"
                            + this.getStrError());
            this.setExistError(true);
        }
        return sld;
    }

    /**
     * Método para la obtención de los datos adicionales de la tabla de carga
     * @return  DataTable   Objeto con los datos adicionales
     * @throws ExceptionHandler
     */
    private DataTable getDataTable() throws ExceptionHandler{
        DataTable sld = null;
        String query = "select ac.id_ArchivoCarga, ac.archivoCarga, ta.id_Tabla "
                + ", ta.nombreTabla, ta.dominio , ta.base, ta.usuario, ta.password "
                + " from UPL_ArchivoCarga ac, UPL_Tabla ta"
                + " where ac.id_Tabla = ta.id_Tabla"
                + " and ac.id_ArchivoCarga = " + this.getIdArchivoCarga();

        ConImportDB con = getConection();
        con.setStrQuery(query);
        sld = con.getTable();

        return sld;
    }


    /**
     * Obtiene un objeto de conexión a la base de datos. En caso que existan
     * datos adicionales de la tabla de proceso, se incluyen esos datos.
     * @return  ConImportDB     Objeto de conexión
     */
    private ConImportDB getConection(){
        ConImportDB con = new ConImportDB();

        if (this.getTablaProceso()!=null){
            con.setDataTable(this.getTablaProceso());
        }

        return con;
    }

}
