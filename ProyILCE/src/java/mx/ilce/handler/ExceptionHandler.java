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
package mx.ilce.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.mail.DataMail;
import mx.ilce.util.UtilDate;

/**
 * Manejador de Exception general, que permite unificar las respuestas generales
 * de las Exception ocurridas en la aplicación. Mediante algunas configuraciones
 * y entradas adicionales, permite dar informes más generales, además de tomar
 * acciones adicionales como la generación de archivos con el registro de las
 * Exception, junto con su traza, ubicación, día y hora de su activación.
 * @author ccatrilef
 */
public final class ExceptionHandler extends Throwable {

    private StringBuffer textError = new StringBuffer("");
    private StringBuffer textMessage = new StringBuffer("");
    private StringBuffer xmlError = new StringBuffer("");
    private StringBuffer secuenceError = new StringBuffer("");
    private StringBuffer typeError = new StringBuffer("");
    private ArrayList arrClass = new ArrayList();
    private String packageProy = "mx.ilce.";
    private String time;
    private UtilDate utiDt;
    private ArrayList arrayData;
    private String stringData;
    private boolean seeStringData;
    private boolean logFile;
    private String rutaFile;
    private String strQuery;
    private String dataToXML;
    private String headerDataXML;
    private String dataInterna;

    /**
     * Constructor básico
     */
    public ExceptionHandler() {
    }

    /**
     * Constructor utilizado para situaciones donde se deba forzar un error
     * @param typeError     Texto con el tipo de error producido
     * @param clase         Clase donde se produjo el error
     * @param message       Mensaje de error producido
     * @param dataError     Data informativa del error producido
     */
    public ExceptionHandler(String typeError, Class clase, String message, String dataError){
        cleanData();
        utiDt = new UtilDate();
        setTime(utiDt.getFechaHMS());
        setTypeError(typeError);
        setTextMessage(message);
        setTextError(dataError);
    }

    /**
     * Método para la limpieza del objeto antes de la generación de datos
     */
    private void cleanData(){
        this.textError = new StringBuffer("");
        this.textMessage = new StringBuffer("");
        this.xmlError = new StringBuffer("");
        this.secuenceError = new StringBuffer("");
        this.typeError = new StringBuffer("");
        this.arrClass = new ArrayList();
        this.time="";
        this.arrayData=new ArrayList();
        this.stringData=null;
        this.logFile=false;
        this.rutaFile="";
    }

    /**
     * Constructor de la Exception donde se entrega un objeto con la clase de la
     * Exception ocurrida, la clase donde ocurrió el problema y un mensaje que se
     * desea desplegar, junto con los mensajes obtenidos desde la exception y la
     * data adicional entregada.
     * @param obj       Clase del tipo de Error (Exception, NullPointerException, etc)
     * @param clase     Clase donde se produjo el error
     * @param message   Mensaje que se quiere agregar al error además de los
     * contenidos en la exception
     */
    public ExceptionHandler(Object obj, Class clase, String message){
        cleanData();
        utiDt = new UtilDate();
        setTime(utiDt.getFechaHMS());
        setTypeError(obj.getClass().getSimpleName());
        setTextMessage(message);

        if (getTypeError().toString().equals(AddressException.class.getSimpleName())){
            AddressException e = (AddressException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(MessagingException.class.getSimpleName())){
            MessagingException e = (MessagingException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(InvocationTargetException.class.getSimpleName())){
            InvocationTargetException e = (InvocationTargetException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(IllegalArgumentException.class.getSimpleName())){
            IllegalArgumentException e = (IllegalArgumentException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(IllegalAccessException.class.getSimpleName())){
            IllegalAccessException e = (IllegalAccessException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(InstantiationException.class.getSimpleName())){
            InstantiationException e = (InstantiationException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(NoSuchMethodException.class.getSimpleName())){
            NoSuchMethodException e = (NoSuchMethodException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(URISyntaxException.class.getSimpleName())){
            URISyntaxException e = (URISyntaxException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(NullPointerException.class.getSimpleName())) {
            NullPointerException e = (NullPointerException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(ClassNotFoundException.class.getSimpleName())) {
            ClassNotFoundException e = (ClassNotFoundException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(ClassCastException.class.getSimpleName())) {
            ClassCastException e = (ClassCastException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(IOException.class.getSimpleName())) {
            IOException e = (IOException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(SQLException.class.getSimpleName())) {
            SQLException e = (SQLException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else if (getTypeError().toString().equals(FileNotFoundException.class.getSimpleName())) {
            FileNotFoundException e = (FileNotFoundException)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }else {  
            Exception e = (Exception)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }
    }

    /**
     * Método que manda la instrucción de escribir en un archivo los datos de error capturados
     * en la Exception. Requiere que previamente se haya entregado la ruta donde
     * se depositará el archivo de error y haber marcado como TRUE que debe generarse
     * el archivo
     * @return  boolean     Resultado de la operación de escritura
     */
    public boolean writeToFile(){
        boolean sld = true;
        writeLogError wf = new writeLogError();
        String strRutaFile = this.getRutaFile();
        String strNameFile = "";
        if ((this.logFile) && (this.rutaFile!=null)){
            try{
                StringBuffer strAdicional = new StringBuffer();
                File directorio = new File(this.rutaFile);
                if (!directorio.exists()){
                    strAdicional.append("No existe el Directorio para Log\n");
                    sld = false;
                }
                if (!directorio.isDirectory()){
                    strAdicional.append("El Directorio configurado para Log no es un directorio\n");
                    sld = false;
                }
                if ((strAdicional!=null) && (!"".equals(strAdicional.toString()))){
                    this.setStringData(strAdicional.toString());
                }
                UtilDate ut = new UtilDate();
                strNameFile = strNameFile.concat(ut.getFecha(UtilDate.formato.AMD,""));
                strNameFile = strNameFile.concat(this.getTypeError().toString());
                strNameFile = strNameFile.concat(".log");
                wf.setNameFile(strNameFile);
                wf.setRutaFile(strRutaFile);
                StringBuffer strTexto = new StringBuffer();
                strTexto.append("\nFECHA: ").append(this.getTime());
                strTexto.append("\nTIPO: ").append(this.getTypeError());
                strTexto.append("\nMENSAJE: ").append(this.getTextMessage());
                strTexto.append("\nDESCRIPCION: ").append(this.getTextError());
                if ((this.strQuery!=null)&&(!"".equals(this.strQuery))){
                    strTexto.append("\nQUERY:\n").append(this.getStrQuery());
                }
                if ((this.stringData!=null)&&(!"".equals(this.stringData))){
                    strTexto.append("\nDATA:\n").append(this.getStringData());
                }
                strTexto.append("\nTRAZA\n-------------\n").append(this.getSecuenceError());
                strTexto.append("\n****************\n");
                if (sld){
                    sld = wf.guardarArchivo(strTexto);
                }else{
                    this.setStringData(strAdicional.toString());
                }
            }catch(IOException e){}
        }
        return sld;
    }

    /**
     * Método que en base a los datos contenidos en la Exception genera un XML
     * con dichos datos
     */
    private void setTextToXmlError(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<error>\n");
        str.append("<general>");
        str.append(this.getTextMessage());
        str.append("</general>\n");
        str.append("<fecha>");
        str.append(this.getTime("-"));
        str.append("</fecha>\n");
        str.append("<descripcion>");
        str.append(((this.getTextError()==null)||
                ("".equals(this.getTextError().toString())))?this.getTextMessage():this.getTextError());
        str.append("</descripcion>\n");
        str.append("<tipo>");
        str.append(this.getTypeError());
        str.append("</tipo>\n");
        if (this.seeStringData()){
            if ((this.strQuery!=null) && (!"".equals(this.strQuery))) {
                str.append("<query><![CDATA[\n");
                str.append(this.getStrQuery());
                str.append("]]>\n</query>\n");
            }
            if ((this.stringData!=null) && (!"".equals(this.stringData))) {
                str.append("<datos_adicionales>");
                str.append(this.getStringData());
                str.append("\n</datos_adicionales>\n");
            }
        }
        str.append("</error>");
        this.setXmlError(str);
    }

    /**
     * Método que captura el StackTrace completo de la exception generada
     * @param stack     Stack que contiene la secuencia de errores capturadas en
     * el Exception
     * @param clase     Clase desde donde debe consultarse los datos de la traza.
     * Esta clase debe ser desde donde se capturo el error.
     */
    private void getStackTrace(StackTraceElement[] stack, Class clase) {
        boolean buscarPadre = true;
        boolean buscarHijo = false;
        boolean seguir = true;

        if (stack !=null){
            String strClase = clase.getCanonicalName();
            for (int i=0;i<stack.length && seguir;i++){
                StackTraceElement st = stack[i];
                if (buscarPadre){
                    if (st.getClassName().equals(strClase)){
                        buscarPadre = false;
                        buscarHijo = true;
                        Dato dato = new Dato();
                        dato.setNameClass(st.getClassName());
                        dato.setMethodClass(st.getMethodName());
                        dato.setFileClass(st.getFileName());
                        dato.setLineNumber(st.getLineNumber());
                        arrClass.add(dato);
                    }
                }else{
                    if (buscarHijo){
                        if (st.getClassName().contains(packageProy)){
                            Dato dato = new Dato();
                            dato.setNameClass(st.getClassName());
                            dato.setMethodClass(st.getMethodName());
                            dato.setFileClass(st.getFileName());
                            dato.setLineNumber(st.getLineNumber());
                            arrClass.add(dato);
                        }else{
                            seguir = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que construye un texto con la secuencia de ejecución del exception
     * con el objeto de identificar en que clase se produjo, en que método y en
     * que línea se produjo el error
     * @return  StringBuffer    Texto con la secuencia de error
     */
    private StringBuffer getStringSecuenceError(){
        StringBuffer sld = new StringBuffer();
        sld.append("ARCHIVO\t,LINEA\t,METODO\t\t,CLASE\n");
        if ((arrClass!=null) && (!arrClass.isEmpty())){
            for (int i=0;i<arrClass.size();i++){
                Dato dto = (Dato) arrClass.get(i);
                sld.append(dto.getFileClass()).append("\t,");
                sld.append(dto.getLineNumber()).append("\t,");
                sld.append(dto.getMethodClass()).append("\t,");
                sld.append(dto.getNameClass()).append("\n");
            }
        }
        return sld;
    }

    /**
     * Obtiene el texto de la query asignada
     * @return  Texto con la query
     */
    public String getStrQuery() {
        return strQuery;
    }

    /**
     * Asigna el texto de la query
     * @param strQuery  Texto con la query
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Método que entrega un valor booleano para considerar o no la inclusión
     * de la data contenida en el campo stringData
     * @return  boolean     Valor TRUE o FALSE de la validación
     */
    public boolean seeStringData() {
        return seeStringData;
    }

    /**
     * Método que asigna un valor booleano para considerar o no la inclusión
     * de la data contenida en el campo stringData
     * @param seeStringData     Valor TRUE o FALSE de la validación
     */
    public void setSeeStringData(boolean seeStringData) {
        this.seeStringData = seeStringData;
    }

    /**
     * Obtiene la ruta asignada para la generación del archivo de Log de la
     * Exception generada
     * @return  String  Texto con la ruta
     */
    public String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta para la generación del archivo de Log de la exception
     * @param rutaFile      Texto con la ruta
     */
    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene la respuesta de si se debe generar o no el archivo de Log
     * @return  boolean     Valor TRUE o FALSE de la validación
     */
    public boolean isLogFile() {
        return logFile;
    }

    /**
     * Asigna la respuesta de si se debe generar o no el archivo de Log
     * @param logFile       Valor TRUE o FALSE de la validación
     */
    public void setLogFile(boolean logFile) {
        this.logFile = logFile;
    }

    /**
     * Obtiene el arreglo de datos entregados a la Exception
     * @return  ArrayList   Arreglo con los datos
     */
    public ArrayList getArrayData() {
        return arrayData;
    }

    /**
     * Asigna el arreglo de datos entregados a la Exception
     * @param arrayData     Arreglo con los datos
     */
    public void setArrayData(ArrayList arrayData) {
        this.arrayData = arrayData;
    }

    /**
     * Obtiene los datos contenidos en el campo stringData
     * @return  String  Texto con los valores asignados
     */
    public String getStringData() {
        return stringData;
    }

    /**
     * Asigna los datos que debe contener en el campo stringData
     * @param stringData    Texto con los valores asignados
     */
    public void setStringData(String stringData) {
        String str = this.stringData;
        if (str!=null){
            if (stringData!=null){
                str = str + "\n" + stringData;
            }
        }else{
            if (stringData!=null){
                str = stringData;
            }
        }
        this.stringData = str;
    }

    /**
     * Obtiene el tipo de error que se produjo
     * @return  StringBuffer    Texto con el tipo de error producido
     */
    public StringBuffer getTypeError() {
        return typeError;
    }

    /**
     * Asigna el tipo de error que se produjo
     * @param typeError     Texto con el tipo de error producido
     */
    private void setTypeError(String typeError) {
        this.typeError.append(typeError);
    }

    /**
     * Obtiene la fecha y hora en que se generó a la Exception
     * @return  String  Texto con la fecha y hora
     */
    public String getTime() {
        return time;
    }

    /**
     * Obtiene la fecha y hora en que se generó la Exception. Se asigna
     * el caracter separador entregado para representar la fecha
     * @param separador     Caracter separador que debe utilizarce
     * @return  String  Texto con la fecha y hora
     */
    public String getTime(String separador) {
        String strTime = time.replaceAll("/", separador);
        return strTime;
    }

    /**
     * Asigna la fecha y hora en que se generó la Exception.
     * @param time  Texto con la fecha y hora
     */
    private void setTime(String time) {
        this.time = time;
    }

    /**
     * Obtiene la secuencia de clases y métodos invocados en la exception
     * @return  StringBuffer    Texto con la secuencia de invocación
     */
    public StringBuffer getSecuenceError() {
        return secuenceError;
    }

    /**
     * Asigna la secuencia de clases y métodos invocados en la exception
     * @param secuenceError     Texto con la secuencia de invocación
     */
    private void setSecuenceError(String secuenceError) {
        this.secuenceError.append(secuenceError);
    }

    /**
     * Obtiene el texto de error ingresado al campo textError
     * @return  StringBuffer    Texto con la descripción del error
     */
    public StringBuffer getTextError() {
        return textError;
    }

    /**
     * Asigna el texto de error al campo textError
     * @param textError     Texto con la descripción del error
     */
    public void setTextError(String textError) {
        this.textError.append((textError==null)?"":textError);
    }

    /**
     * Obtiene el texto del mensaje ingresado al campo textMessage
     * @return  StringBuffer    Texto con el mensaje de error
     */
    public StringBuffer getTextMessage() {
        return textMessage;
    }

    /**
     * Asigna el texto del mensaje al campo textMessage
     * @param textMessage   Texto con el mensaje de error
     */
    private void setTextMessage(String textMessage) {
        this.textMessage.append((textMessage==null)?"":textMessage);
    }

    /**
     * Obtiene el xml generado con los datos de la Exception
     * @return  StringBuffer    Texto en formato XML del error
     */
    public StringBuffer getXmlError() {
        setTextToXmlError();
        return xmlError;
    }

    /**
     * Asigna el xml generado con los datos de la Exception al campo xmlError
     * @param xmlError      Texto en formato XML del error
     */
    private void setXmlError(StringBuffer xmlError) {
        this.xmlError = xmlError;
    }

    /**
     * Clase definida para manejar la escritura de archivos de Log con los datos
     * contenidos en la Exception
     */
    private class writeLogError{

        private String rutaFile;
        private String nameFile;

        /**
         * Obtiene el nombre que poseerá el archivo de Log
         * @return  String  Nombre del archivo
         */
        public String getNameFile() {
            return nameFile;
        }

        /**
         * Asigna el nombre que poseerá el archivo de Log
         * @param nameFile  Nombre del archivo
         */
        public void setNameFile(String nameFile) {
            this.nameFile = nameFile;
        }

        /**
         * Obtiene la ruta donde se colocará el archivo de Log
         * @return  String  Ruta del archivo
         */
        public String getRutaFile() {
            return rutaFile;
        }

        /**
         * Asigna la ruta donde se colocará el archivo de Log
         * @param rutaFile  Ruta del archivo
         */
        public void setRutaFile(String rutaFile) {
            this.rutaFile = rutaFile;
        }

        /**
         * Método que escribe la data entregada al archivo de Log. Si el archivo
         * existe, el texto entregado se adjunta al final del archivo
         * @param strEntrada    Texto con lo que debe anexarse al archivo de error
         * @return  boolean     Valor de la validación del resultado de la operación
         * @throws IOException
         */
        public boolean guardarArchivo(StringBuffer strEntrada) throws IOException{
            boolean sld = true;
            FileWriter w = null;
            try{
                w = new FileWriter(rutaFile + "/" + nameFile, true);
                w.append(strEntrada.toString());
            }catch (IOException eFile){
                sld = false;
            }catch(Exception e){
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

    /**
     * Clase implementada para sea utilizada en un arreglo de datos del tipo List.
     * Su uso principal es contener los datos de la traza del error
     */
    private class Dato{
        String nameClass = "";
        String methodClass = "";
        String fileClass = "";
        int lineNumber = 0;

        public Dato() {
        }

        /**
         * Obtiene el nombre del archivo de la clase donde se produjo el error
         * @return  String  Texto con el nombre del archivo de clase
         */
        public String getFileClass() {
            return fileClass;
        }

        /**
         * Asigna el nombre del archivo de la clase donde se produjo el error
         * @param fileClass     Texto con el nombre del archivo de clase
         */
        public void setFileClass(String fileClass) {
            this.fileClass = fileClass;
        }

        /**
         * Obtiene el número de línea de la clase donde se produjo el error
         * @return  int Número de la línea del error
         */
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * Asigna el número de línea de la clase donde se produjo el error
         * @param lineNumber    Número de la línea del error
         */
        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        /**
         * Obtiene el método de la clase donde se produjo el error
         * @return  String  Método donde se hizo la invocación
         */
        public String getMethodClass() {
            return methodClass;
        }

        /**
         * Asigna el método de la clase donde se produjo el error
         * @param methodClass   Método donde se hizo la invocación
         */
        public void setMethodClass(String methodClass) {
            this.methodClass = methodClass;
        }

        /**
         * Obtiene el nombre de la clase donde se produjo el error
         * @return  String  Nombre de la clase donde se produjo el error
         */
        public String getNameClass() {
            return nameClass;
        }

        /**
         * Asigna el nombre de la clase donde se produjo el error
         * @param nameClass     Nombre de la clase donde se produjo el error
         */
        public void setNameClass(String nameClass) {
            this.nameClass = nameClass;
        }
    }

    /**
     * Obtiene la estructura XML de los datos ingresados hasta el momento
     * de la invocación
     * @return  String  Texto con el XML
     */
    public String getDataToXML(){
        String sld = "";
        if (dataToXML!=null){
            sld = ("\n\t<reglas_reemplazo>") + dataToXML + ("\n\t</reglas_reemplazo>");
        }
        if (headerDataXML!=null){
            sld = headerDataXML + sld;
        }
        if (dataInterna!=null){
            sld = sld + dataInterna;
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto DataTransfer entregado, a la sección
     * de parámetros de reemplazo de la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a continuación.
     * @param dataTransfer
     */
    public void setDataToXML(DataTransfer dataTransfer){
        if (dataTransfer!=null){
            setHeaderDataTransferXML(dataTransfer);
            if (dataTransfer.getArrData()!=null){
                setDataToXML(dataTransfer.getArrData());
            }
            if (dataTransfer.getArrVariables()!=null){
                setDataToXML(dataTransfer.getArrVariables());
            }
            if (dataTransfer.getCampo()!=null){
                setDataToXML(dataTransfer.getCampo());
            }
            if (dataTransfer.getCampoForma()!=null){
                setDataToXML(dataTransfer.getCampoForma());
            }
        }
    }

    /**
     * Asigna los datos contenidos en el objeto DataTransfer entregado, a la sección
     * previa a los parámetros de reemplazo de la estructura XML que registra un
     * error. Si existían datos previos estos se mantienen y la
     * nueva data se anexa a continuación.
     * @param dataTransfer
     */
    private void setHeaderDataTransferXML(DataTransfer dataTransfer){
        StringBuilder sld= new StringBuilder();
        if (dataTransfer!=null){
            if (dataTransfer.getIdQuery()!=null){
                sld.append("\n\t<clave_consulta>")
                   .append(dataTransfer.getIdQuery())
                   .append("</clave_consulta>");
            }
            if (dataTransfer.getStrWhere()!=null){
                sld.append("\n\t<w><![CDATA[")
                   .append(dataTransfer.getStrWhere())
                   .append("]]></w>");
            }
            if (dataTransfer.getOrderBY()!=null){
                sld.append("\n\t<order_by>\n<![CDATA[")
                   .append(dataTransfer.getOrderBY())
                   .append("]]>\n</order_by>");
            }
            if (dataTransfer.getQueryDelete()!=null){
                sld.append("\n\t<query_delete>\n<![CDATA[")
                   .append(dataTransfer.getQueryDelete())
                   .append("]]>\n</query_delete>");
            }
            if (dataTransfer.getQueryInsert()!=null){
                sld.append("\n\t<query_insert>\n<![CDATA[")
                   .append(dataTransfer.getQueryInsert())
                   .append("]]>\n</query_insert>");
            }
            if (dataTransfer.getQueryUpdate()!=null){
                sld.append("\n\t<query_update>\n<![CDATA[")
                   .append(dataTransfer.getQueryUpdate())
                   .append("]]>\n</query_update>\n");
            }
        }
        if (headerDataXML!=null){
            headerDataXML = headerDataXML + sld.toString();
        }else{
            headerDataXML = sld.toString();
        }
    }

    /**
     * Asigna los datos entregados a la sección de parámetros de reemplazo de
     * la estructura XML que registra un error. Si existían datos previos estos
     * se mantienen y la nueva data se coloca a continuación.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo String
     */
    public void setDataToXML(String nombre, String valor){
        StringBuilder sld= new StringBuilder();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append((valor==null)?"":valor)
           .append("]]></valor>");
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo String
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String nombre, String valor){
        StringBuffer sld= new StringBuffer();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append((valor==null)?"":valor)
           .append("]]></valor>");
        return sld;
    }

    /**
     * Asigna los datos entregados a la sección de parámetros de reemplazo de
     * la estructura XML que registra un error. Si existían datos previos estos
     * se mantienen y la nueva data se coloca a continuación.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo Integer
     */
    public void setDataToXML(String nombre, Integer valor){
        StringBuilder sld= new StringBuilder();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append((valor==null)?"":valor)
           .append("]]></valor>");
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo Integer
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String nombre, Integer valor){
        StringBuffer sld= new StringBuffer();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append((valor==null)?"":valor)
           .append("]]></valor>");
        return sld;
    }

    /**
     * Asigna los datos entregados a la sección de parámetros de reemplazo de
     * la estructura XML que registra un error. Si existían datos previos estos
     * se mantienen y la nueva data se coloca a continuación.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo boolean
     */
    public void setDataToXML(String nombre, boolean valor){
        StringBuilder sld= new StringBuilder();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append(valor)
           .append("]]></valor>");
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo boolean
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String nombre, boolean valor){
        StringBuffer sld= new StringBuffer();
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append(valor)
           .append("]]></valor>");
        return sld;
    }

    /**
     * Asigna los datos entregados a la sección de parámetros de reemplazo de
     * la estructura XML que registra un error. Si existían datos previos estos
     * se mantienen y la nueva data se coloca a continuación.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo Date
     */
    public void setDataToXML(String nombre, Date valor){
        StringBuilder sld= new StringBuilder();
        UtilDate ut = new UtilDate(valor);
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append(ut.getFechaHMS())
           .append("]]></valor>");
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo Date
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String nombre, Date valor){
        StringBuffer sld= new StringBuffer();
        UtilDate ut = new UtilDate(valor);
        sld.append("\n\t\t<parametro><![CDATA[")
           .append(nombre)
           .append("]]></parametro>")
           .append("\n\t\t<valor><![CDATA[")
           .append(ut.getFechaHMS())
           .append("]]></valor>");
        return sld;
    }

    /**
     * Asigna los datos contenidos en la matriz de datos entregada, a la sección
     * de parámetros de reemplazo de la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a continuación.
     * @param strData   Matriz con los datos
     */
    public void setDataToXML(String[][] strData){
        StringBuilder sld= new StringBuilder();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\t<parametro><![CDATA[")
                   .append((strData[i][0]==null)?"":strData[i][0])
                   .append("]]></parametro>")
                   .append("\n\t\t<valor><![CDATA[")
                   .append((strData[i][1]==null)?"":strData[i][1])
                   .append("]]></valor>");
            }
        }
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo String[][]
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String[][] strData){
        StringBuffer sld= new StringBuffer();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\t<parametro><![CDATA[")
                   .append((strData[i][0]==null)?"":strData[i][0])
                   .append("]]></parametro>")
                   .append("\n\t\t<valor><![CDATA[")
                   .append((strData[i][1]==null)?"":strData[i][1])
                   .append("]]></valor>");
            }
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el arreglo de datos entregado, a la sección
     * de parámetros de reemplazo de la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a continuación.
     * @param strData   Arreglo con los datos
     */
    public void setDataToXML(String[] strData){
        StringBuilder sld= new StringBuilder();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\t<parametro><![CDATA[")
                   .append(i+1)
                   .append("]]></parametro>")
                   .append("\n\t\t<valor><![CDATA[")
                   .append((strData[i]==null)?"":strData[i])
                   .append("]]></valor>");
            }
        }
        if (dataToXML!=null){
            dataToXML = dataToXML + sld.toString();
        }else{
            dataToXML = sld.toString();
        }
    }

    /**
     * Obtiene una sección del XML de error con la data entregada, correspondiente
     * a los parámetros de algún campo. Usado en las llamadas recursivas para la
     * generación del XML.
     * @param nombre    Nombre del parámetro
     * @param valor     Valor del parámetro, tipo String[]
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(String[] strData){
        StringBuffer sld= new StringBuffer();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("\n\t\t<parametro><![CDATA[")
                   .append(i+1)
                   .append("]]></parametro>")
                   .append("\n\t\t<valor><![CDATA[")
                   .append((strData[i]==null)?"":strData[i])
                   .append("]]></valor>");
            }
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto User entregado, a la sección
     * de registro_User en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param user  Objeto User
     */
    public void setDataToXML(User user){
        StringBuilder sld = new StringBuilder();
        if (user!=null){
            sld.append("\n<registro_User>");
            if (user.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",user.getClaveEmpleado()));
            }
            if (user.getIDUser()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",user.getIDUser().toString()));
            }
            if (user.getNombre()!=null){
                sld.append(setDataToXMLInt("NOMBRE",user.getNombre()));
            }
            if (user.getApellidoPaterno()!=null){
                sld.append(setDataToXMLInt("APELLIDO PATERNO",user.getApellidoPaterno()));
            }
            if (user.getApellidoMaterno()!=null){
                sld.append(setDataToXMLInt("APELLIDO MATERNO",user.getApellidoMaterno()));
            }
            if (user.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",user.getClavePerfil()));
            }
            if (user.getEmail()!=null){
                sld.append(setDataToXMLInt("EMAIL",user.getEmail()));
            }
            if (user.getClaveArea()!=null){
                sld.append(setDataToXMLInt("CLAVE AREA",user.getClaveArea()));
            }
            if (user.getLogin()!=null){
                sld.append(setDataToXMLInt("LOGIN",user.getLogin()));
            }
            if (user.getPassword()!=null){
                sld.append(setDataToXMLInt("PSW",user.getPassword()));
            }
            sld.append(setDataToXMLInt("LOGGED",user.isLogged()));
            if (user.getBitacora()!=null){
                sld.append(setDataToXMLInt(user.getBitacora()));
            }
            sld.append("\n</registro_User>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto User, la
     * que corresponderá a una sección registro_User. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param user    Objeto User
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(User user){
        StringBuffer sld = new StringBuffer();
        if (user!=null){
            sld.append("\n\t<registro_User>");
            if (user.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",user.getClaveEmpleado()));
            }
            if (user.getIDUser()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",user.getIDUser().toString()));
            }
            if (user.getNombre()!=null){
                sld.append(setDataToXMLInt("NOMBRE",user.getNombre()));
            }
            if (user.getApellidoPaterno()!=null){
                sld.append(setDataToXMLInt("APELLIDO PATERNO",user.getApellidoPaterno()));
            }
            if (user.getApellidoMaterno()!=null){
                sld.append(setDataToXMLInt("APELLIDO MATERNO",user.getApellidoMaterno()));
            }
            if (user.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",user.getClavePerfil()));
            }
            if (user.getEmail()!=null){
                sld.append(setDataToXMLInt("EMAIL",user.getEmail()));
            }
            if (user.getClaveArea()!=null){
                sld.append(setDataToXMLInt("CLAVE AREA",user.getClaveArea()));
            }
            if (user.getLogin()!=null){
                sld.append(setDataToXMLInt("LOGIN",user.getLogin()));
            }
            if (user.getPassword()!=null){
                sld.append(setDataToXMLInt("PSW",user.getPassword()));
            }
            sld.append(setDataToXMLInt("LOGGED",user.isLogged()));
            if (user.getBitacora()!=null){
                sld.append(setDataToXMLInt(user.getBitacora()));
            }
            sld.append("\n\t</registro_User>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Perfil entregado, a la sección
     * de registro_Perfil en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param perfil  Objeto Perfil
     */
    public void setDataToXML(Perfil perfil) throws ExceptionHandler{
        StringBuilder sld = new StringBuilder();
        if (perfil!=null){
            sld.append("\n<registro_Perfil>");
            if (perfil.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",perfil.getClavePerfil()));
            }
            if (perfil.getPerfil()!=null){
                sld.append(setDataToXMLInt("PERFIL",perfil.getPerfil()));
            }
            if (perfil.getArrVariables()!=null){
                sld.append(setDataToXMLInt(perfil.getArrVariables()));
            }
            if (perfil.getBitacora()!=null){
                sld.append(setDataToXMLInt(perfil.getBitacora()));
            }
            if (perfil.getUser()!=null){
                sld.append(setDataToXMLInt(perfil.getUser()));
            }
            if (perfil.getListArea()!=null){
                sld.append(setDataToXMLInt(perfil.getListArea()));
            }
            if (perfil.getLstAplicacion()!=null){
                sld.append(setDataToXMLInt(perfil.getLstAplicacion()));
            }
            sld.append("\n</registro_Perfil>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto Perfil, la
     * que corresponderá a una sección registro_Perfil. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param perfil    Objeto Perfil
     * @return  StringBuffer    Sección de XML obtenida
     * @throws ExceptionHandler
     */
    private StringBuffer setDataToXMLInt(Perfil perfil) throws ExceptionHandler{
        StringBuffer sld = new StringBuffer();
        if (perfil!=null){
            sld.append("\n\t<registro_Perfil>");
            if (perfil.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",perfil.getClavePerfil()));
            }
            if (perfil.getPerfil()!=null){
                sld.append(setDataToXMLInt("PERFIL",perfil.getPerfil()));
            }
            if (perfil.getArrVariables()!=null){
                sld.append(setDataToXMLInt(perfil.getArrVariables()));
            }
            if (perfil.getBitacora()!=null){
                sld.append(setDataToXMLInt(perfil.getBitacora()));
            }
            if (perfil.getUser()!=null){
                sld.append(setDataToXMLInt(perfil.getUser()));
            }
            if (perfil.getListArea()!=null){
                sld.append(setDataToXMLInt(perfil.getListArea()));
            }
            if (perfil.getLstAplicacion()!=null){
                sld.append(setDataToXMLInt(perfil.getLstAplicacion()));
            }
            sld.append("\n\t</registro_Perfil>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Bitacora entregado, a la sección
     * de registro_Bitacora en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param perfil  Objeto Perfil
     */
    public void setDataToXML(Bitacora bitacora){
        StringBuilder sld = new StringBuilder();
        if (bitacora!=null){
            sld.append("\n<registro_Bitacora>");
            if (bitacora.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACION",bitacora.getClaveAplicacion()));
            }
            if (bitacora.getClaveBitacora()!=null){
                sld.append(setDataToXMLInt("CLAVE BITACORA",bitacora.getClaveBitacora()));
            }
            if (bitacora.getClaveBitacoraProyecto()!=null){
                sld.append(setDataToXMLInt("CLAVE BITACORA PROYECTO",bitacora.getClaveBitacoraProyecto()));
            }
            if (bitacora.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",bitacora.getClaveEmpleado()));
            }
            if (bitacora.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",bitacora.getClaveForma()));
            }
            if (bitacora.getClaveRegistro()!=null){
                sld.append(setDataToXMLInt("CLAVE REGISTRO",bitacora.getClaveRegistro()));
            }
            if (bitacora.getClaveTipoEvento()!=null){
                sld.append(setDataToXMLInt("CLAVE TIPO EVENTO",bitacora.getClaveTipoEvento()));
            }
            if (bitacora.getConsulta()!=null){
                sld.append(setDataToXMLInt("CONSULTA",bitacora.getConsulta()));
            }
            if (bitacora.getError()!=null){
                sld.append(setDataToXMLInt("ERROR",bitacora.getError()));
            }
            if (bitacora.getEvento()!=null){
                sld.append(setDataToXMLInt("EVENTO",bitacora.getEvento()));
            }
            if (bitacora.getFecha()!=null){
                sld.append(setDataToXMLInt("FECHA",bitacora.getFecha()));
            }
            if (bitacora.getFechaBitacora()!=null){
                sld.append(setDataToXMLInt("FECHA BITACORA",bitacora.getFechaBitacora()));
            }
            if (bitacora.getIdBitacora()!=null){
                sld.append(setDataToXMLInt("IP BITACORA",bitacora.getIdBitacora()));
            }
            if (bitacora.getIp()!=null){
                sld.append(setDataToXMLInt("IP",bitacora.getIp()));
            }
            if (bitacora.getNavegador()!=null){
                sld.append(setDataToXMLInt("NAVEGADOR",bitacora.getNavegador()));
            }
            sld.append("\n</registro_Bitacora>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto Bitacora, la
     * que corresponderá a una sección registro_Bitacora. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param bitacora    Objeto Bitacora
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(Bitacora bitacora){
        StringBuffer sld = new StringBuffer();
        if (bitacora!=null){
            sld.append("\n\t<registro_Bitacora>");
            if (bitacora !=null){
                if (bitacora.getClaveAplicacion()!=null){
                    sld.append(setDataToXMLInt("CLAVE APLICACION",bitacora.getClaveAplicacion()));
                }
                if (bitacora.getClaveBitacora()!=null){
                    sld.append(setDataToXMLInt("CLAVE BITACORA",bitacora.getClaveBitacora()));
                }
                if (bitacora.getClaveBitacoraProyecto()!=null){
                    sld.append(setDataToXMLInt("CLAVE BITACORA PROYECTO",bitacora.getClaveBitacoraProyecto()));
                }
                if (bitacora.getClaveEmpleado()!=null){
                    sld.append(setDataToXMLInt("CLAVE EMPLEADO",bitacora.getClaveEmpleado()));
                }
                if (bitacora.getClaveForma()!=null){
                    sld.append(setDataToXMLInt("CLAVE FORMA",bitacora.getClaveForma()));
                }
                if (bitacora.getClaveRegistro()!=null){
                    sld.append(setDataToXMLInt("CLAVE REGISTRO",bitacora.getClaveRegistro()));
                }
                if (bitacora.getClaveTipoEvento()!=null){
                    sld.append(setDataToXMLInt("CLAVE TIPO EVENTO",bitacora.getClaveTipoEvento()));
                }
                if (bitacora.getConsulta()!=null){
                    sld.append(setDataToXMLInt("CONSULTA",bitacora.getConsulta()));
                }
                if (bitacora.getError()!=null){
                    sld.append(setDataToXMLInt("ERROR",bitacora.getError()));
                }
                if (bitacora.getEvento()!=null){
                    sld.append(setDataToXMLInt("EVENTO",bitacora.getEvento()));
                }
                if (bitacora.getFecha()!=null){
                    sld.append(setDataToXMLInt("FECHA",bitacora.getFecha()));
                }
                if (bitacora.getFechaBitacora()!=null){
                    sld.append(setDataToXMLInt("FECHA BITACORA",bitacora.getFechaBitacora()));
                }
                if (bitacora.getIdBitacora()!=null){
                    sld.append(setDataToXMLInt("IP BITACORA",bitacora.getIdBitacora()));
                }
                if (bitacora.getIp()!=null){
                    sld.append(setDataToXMLInt("IP",bitacora.getIp()));
                }
                if (bitacora.getNavegador()!=null){
                    sld.append(setDataToXMLInt("NAVEGADOR",bitacora.getNavegador()));
                }
            }
            sld.append("\n\t</registro_Bitacora>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Aplicacion entregado, a la sección
     * de registro_Aplicacion en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param aplicacion    Objeto Aplicacion
     */
    public void setDataToXML(Aplicacion aplicacion){
        StringBuilder sld = new StringBuilder();
        if (aplicacion!=null){
            sld.append("\n<registro_Aplicacion>");
            if (aplicacion.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",aplicacion.getClaveAplicacion()));
            }
            if (aplicacion.getAplicacion()!=null){
                sld.append(setDataToXMLInt("APLICACIÓN",aplicacion.getAplicacion()));
            }
            if (aplicacion.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",aplicacion.getClaveEmpleado()));
            }
            if (aplicacion.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",aplicacion.getClaveForma()));
            }
            if (aplicacion.getClaveFormaPrincipal()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA PRINCIPAL",aplicacion.getClaveFormaPrincipal()));
            }
            if (aplicacion.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",aplicacion.getClavePerfil()));
            }
            if (aplicacion.getDescripcion()!=null){
                sld.append(setDataToXMLInt("DESCRIPCIÓN",aplicacion.getDescripcion()));
            }
            if (aplicacion.getDisplay()!=null){
                sld.append(setDataToXMLInt("DISPLAY",aplicacion.getDisplay()));
            }
            if (aplicacion.getNumPage()!=null){
                sld.append(setDataToXMLInt("NÚMERO PÁGINA",aplicacion.getNumPage()));
            }
            if (aplicacion.getNumRows()!=null){
                sld.append(setDataToXMLInt("NÚMERO DE FILAS",aplicacion.getNumRows()));
            }
            if (aplicacion.getOrderBY()!=null){
                sld.append(setDataToXMLInt("ORDER BY",aplicacion.getOrderBY()));
            }
            if (aplicacion.getStrWhereQuery()!=null){
                sld.append(setDataToXMLInt("WHERE",aplicacion.getStrWhereQuery()));
            }
            if (aplicacion.getTipoAccion()!=null){
                sld.append(setDataToXMLInt("TIPO ACCIÓN",aplicacion.getTipoAccion()));
            }
            if (aplicacion.getAliasMenuMostrarEntidad()!=null){
                sld.append(setDataToXMLInt("ALIAS MENU MOSTRAR ENTIDAD",aplicacion.getAliasMenuMostrarEntidad()));
            }
            if (aplicacion.getAliasMenuNuevaEntidad()!=null){
                sld.append(setDataToXMLInt("ALIAS MENU NUEVA ENTIDAD",aplicacion.getAliasMenuNuevaEntidad()));
            }
            if (aplicacion.getArrayData()!=null){
                sld.append(setDataToXMLInt(aplicacion.getArrayData()));
            }
            if (aplicacion.getArrVariables()!=null){
                sld.append(setDataToXMLInt(aplicacion.getArrVariables()));
            }
            sld.append("\n</registro_Aplicacion>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto Aplicacion, la
     * que corresponderá a una sección registro_Aplicacion. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param aplicacion    Objeto Aplicacion
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(Aplicacion aplicacion){
        StringBuffer sld = new StringBuffer();
        if (aplicacion!=null){
            sld.append("\n\t<registro_Aplicacion>");
            if (aplicacion.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",aplicacion.getClaveAplicacion()));
            }
            if (aplicacion.getAplicacion()!=null){
                sld.append(setDataToXMLInt("APLICACIÓN",aplicacion.getAplicacion()));
            }
            if (aplicacion.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",aplicacion.getClaveEmpleado()));
            }
            if (aplicacion.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",aplicacion.getClaveForma()));
            }
            if (aplicacion.getClaveFormaPrincipal()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA PRINCIPAL",aplicacion.getClaveFormaPrincipal()));
            }
            if (aplicacion.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",aplicacion.getClavePerfil()));
            }
            if (aplicacion.getDescripcion()!=null){
                sld.append(setDataToXMLInt("DESCRIPCIÓN",aplicacion.getDescripcion()));
            }
            if (aplicacion.getDisplay()!=null){
                sld.append(setDataToXMLInt("DISPLAY",aplicacion.getDisplay()));
            }
            if (aplicacion.getNumPage()!=null){
                sld.append(setDataToXMLInt("NÚMERO PÁGINA",aplicacion.getNumPage()));
            }
            if (aplicacion.getNumRows()!=null){
                sld.append(setDataToXMLInt("NÚMERO DE FILAS",aplicacion.getNumRows()));
            }
            if (aplicacion.getOrderBY()!=null){
                sld.append(setDataToXMLInt("ORDER BY",aplicacion.getOrderBY()));
            }
            if (aplicacion.getStrWhereQuery()!=null){
                sld.append(setDataToXMLInt("WHERE",aplicacion.getStrWhereQuery()));
            }
            if (aplicacion.getTipoAccion()!=null){
                sld.append(setDataToXMLInt("TIPO ACCIÓN",aplicacion.getTipoAccion()));
            }
            if (aplicacion.getAliasMenuMostrarEntidad()!=null){
                sld.append(setDataToXMLInt("ALIAS MENU MOSTRAR ENTIDAD",aplicacion.getAliasMenuMostrarEntidad()));
            }
            if (aplicacion.getAliasMenuNuevaEntidad()!=null){
                sld.append(setDataToXMLInt("ALIAS MENU NUEVA ENTIDAD",aplicacion.getAliasMenuNuevaEntidad()));
            }
            if (aplicacion.getArrayData()!=null){
                sld.append(setDataToXMLInt(aplicacion.getArrayData()));
            }
            if (aplicacion.getArrVariables()!=null){
                sld.append(setDataToXMLInt(aplicacion.getArrVariables()));
            }
            sld.append("\n\t</registro_Aplicacion>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Campo entregado, a la sección
     * de registro_Campo en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param campo     Objeto Campo
     */
    public void setDataToXML(Campo campo){
        StringBuilder sld = new StringBuilder();
        if (campo!=null){
            sld.append("\n<registro_Campo>");
            if (campo.getCodigo()!=null){
                sld.append(setDataToXMLInt("CÓDIGO CAMPO",campo.getCodigo()));
            }
            if (campo.getNombre()!=null){
                sld.append(setDataToXMLInt("NOMBRE CAMPO EN APLICACIÓN",campo.getNombre()));
            }
            if (campo.getNombreDB()!=null){
                sld.append(setDataToXMLInt("NOMBRE CAMPO EN BASE DE DATOS",campo.getNombreDB()));
            }
            if (campo.getAlias()!=null){
                sld.append(setDataToXMLInt("ALIAS CAMPO",campo.getAlias()));
            }
            if (campo.getEvent()!=null){
                sld.append(setDataToXMLInt("EVENT",campo.getEvent()));
            }
            if (campo.getHelp()!=null){
                sld.append(setDataToXMLInt("HELP",campo.getHelp()));
            }
            if (campo.getTamano()!=null){
                sld.append(setDataToXMLInt("TAMAÑO",campo.getTamano()));
            }
            if (campo.getTypeControl()!=null){
                sld.append(setDataToXMLInt("TIPO CONTROL",campo.getTypeControl()));
            }
            if (campo.getTypeDataAPL()!=null){
                sld.append(setDataToXMLInt("TIPO DATO PARA APLICACIÓN",campo.getTypeDataAPL()));
            }
            if (campo.getTypeDataDB()!=null){
                sld.append(setDataToXMLInt("TIPO DE DATO PARA BASE DE DATOS",campo.getTypeDataDB()));
            }
            if (campo.getValor()!=null){
                sld.append(setDataToXMLInt("VALOR",campo.getValor()));
            }
            if (campo.getHourMinSec()!=null){
                sld.append(setDataToXMLInt("FECHA CON HORA-MINUTO-SEGUNDO",campo.getHourMinSec()));
            }
            sld.append(setDataToXMLInt("ES AUTOINCREMENTAL",campo.getIsIncrement()));
            sld.append("\n</registro_Campo>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto Campo, la
     * que corresponderá a una sección registro_Campo. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param campo    Objeto Campo
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(Campo campo){
        StringBuffer sld = new StringBuffer();
        if (campo!=null){
            sld.append("\n\t<registro_Campo>");
            if (campo.getCodigo()!=null){
                sld.append(setDataToXMLInt("CÓDIGO CAMPO",campo.getCodigo()));
            }
            if (campo.getNombre()!=null){
                sld.append(setDataToXMLInt("NOMBRE CAMPO EN APLICACIÓN",campo.getNombre()));
            }
            if (campo.getNombreDB()!=null){
                sld.append(setDataToXMLInt("NOMBRE CAMPO EN BASE DE DATOS",campo.getNombreDB()));
            }
            if (campo.getAlias()!=null){
                sld.append(setDataToXMLInt("ALIAS CAMPO",campo.getAlias()));
            }
            if (campo.getEvent()!=null){
                sld.append(setDataToXMLInt("EVENT",campo.getEvent()));
            }
            if (campo.getHelp()!=null){
                sld.append(setDataToXMLInt("HELP",campo.getHelp()));
            }
            if (campo.getTamano()!=null){
                sld.append(setDataToXMLInt("TAMAÑO",campo.getTamano()));
            }
            if (campo.getTypeControl()!=null){
                sld.append(setDataToXMLInt("TIPO CONTROL",campo.getTypeControl()));
            }
            if (campo.getTypeDataAPL()!=null){
                sld.append(setDataToXMLInt("TIPO DATO PARA APLICACIÓN",campo.getTypeDataAPL()));
            }
            if (campo.getTypeDataDB()!=null){
                sld.append(setDataToXMLInt("TIPO DE DATO PARA BASE DE DATOS",campo.getTypeDataDB()));
            }
            if (campo.getValor()!=null){
                sld.append(setDataToXMLInt("VALOR",campo.getValor()));
            }
            if (campo.getHourMinSec()!=null){
                sld.append(setDataToXMLInt("FECHA CON HORA-MINUTO-SEGUNDO",campo.getHourMinSec()));
            }
            sld.append(setDataToXMLInt("ES AUTOINCREMENTAL",campo.getIsIncrement()));
            sld.append("\n\t</registro_Campo>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto CampoForma entregado, a la sección
     * de registro_CampoForma en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param campoForma    Objeto CampoForma
     */
    public void setDataToXML(CampoForma campoForma){
        StringBuilder sld = new StringBuilder();
        if (campoForma!=null){
            sld.append("\n<registro_CampoForma>");
            if (campoForma.getActivo()!=null){
                sld.append(setDataToXMLInt("ACTIVO",campoForma.getActivo()));
            }
            if (campoForma.getAliasCampo()!=null){
                sld.append(setDataToXMLInt("ALIAS CAMPO",campoForma.getAliasCampo()));
            }
            if (campoForma.getAliasTab()!=null){
                sld.append(setDataToXMLInt("ALIAS TAB",campoForma.getAliasTab()));
            }
            if (campoForma.getAyuda()!=null){
                sld.append(setDataToXMLInt("AYUDA",campoForma.getAyuda()));
            }
            if (campoForma.getCampo()!=null){
                sld.append(setDataToXMLInt("CAMPO",campoForma.getCampo()));
            }
            if (campoForma.getCargaDatoForaneosRetrasada()!=null){
                sld.append(setDataToXMLInt("CARGA DATO FORANEOS RETRASADA",campoForma.getCargaDatoForaneosRetrasada()));
            }
            if (campoForma.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",campoForma.getClaveAplicacion()));
            }
            if (campoForma.getClaveCampo()!=null){
                sld.append(setDataToXMLInt("CLAVE CAMPO",campoForma.getClaveCampo()));
            }
            if (campoForma.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",campoForma.getClaveForma()));
            }
            if (campoForma.getClaveFormaForanea()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA FORANEA",campoForma.getClaveFormaForanea()));
            }
            if (campoForma.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",campoForma.getClavePerfil()));
            }
            if (campoForma.getDatoSensible()!=null){
                sld.append(setDataToXMLInt("DATO SENSIBLE",campoForma.getDatoSensible()));
            }
            if (campoForma.getEditaFormaForanea()!=null){
                sld.append(setDataToXMLInt("EDITA FORMA FORANEA PERFIL",campoForma.getEditaFormaForanea()));
            }
            if (campoForma.getEvento()!=null){
                sld.append(setDataToXMLInt("EVENTO",campoForma.getEvento()));
            }
            if (campoForma.getFiltroForaneo()!=null){
                sld.append(setDataToXMLInt("FILTRO FORANEO",campoForma.getFiltroForaneo()));
            }
            if (campoForma.getForaneo()!=null){
                sld.append(setDataToXMLInt("FORANEO",campoForma.getForaneo()));
            }
            if (campoForma.getJustificarCambio()!=null){
                sld.append(setDataToXMLInt("JUSTIFICAR CAMBIO",campoForma.getJustificarCambio()));
            }
            if (campoForma.getNoPermitirValorForaneoNulo()!=null){
                sld.append(setDataToXMLInt("NO PERMITIR VALOR FORANEO NULO",campoForma.getNoPermitirValorForaneoNulo()));
            }
            if (campoForma.getObligatorio()!=null){
                sld.append(setDataToXMLInt("OBLIGATORIO",campoForma.getObligatorio()));
            }
            if (campoForma.getTabla()!=null){
                sld.append(setDataToXMLInt("TABLA",campoForma.getTabla()));
            }
            if (campoForma.getTamano()!=null){
                sld.append(setDataToXMLInt("TAMAÑO",campoForma.getTamano()));
            }
            if (campoForma.getTipoControl()!=null){
                sld.append(setDataToXMLInt("TIPO CONTROL",campoForma.getTipoControl()));
            }
            if (campoForma.getTypeData()!=null){
                sld.append(setDataToXMLInt("TYPE DATA",campoForma.getTypeData()));
            }
            if (campoForma.getUsadoParaAgrupar()!=null){
                sld.append(setDataToXMLInt("USADO PARA AGRUPAR",campoForma.getUsadoParaAgrupar()));
            }
            if (campoForma.getValorPredeterminado()!=null){
                sld.append(setDataToXMLInt("VALOR PREDERMINADO",campoForma.getValorPredeterminado()));
            }
            if (campoForma.getVisible()!=null){
                sld.append(setDataToXMLInt("VISIBLE",campoForma.getVisible()));
            }
            sld.append("\n</registro_CampoForma>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto CampoForma, la
     * que corresponderá a una sección registro_CampoForma. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param campoForma    Objeto CampoForma
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(CampoForma campoForma){
        StringBuffer sld = new StringBuffer();
        if (campoForma!=null){
            sld.append("\n\t<registro_CampoForma>");
            if (campoForma.getActivo()!=null){
                sld.append(setDataToXMLInt("ACTIVO",campoForma.getActivo()));
            }
            if (campoForma.getAliasCampo()!=null){
                sld.append(setDataToXMLInt("ALIAS CAMPO",campoForma.getAliasCampo()));
            }
            if (campoForma.getAliasTab()!=null){
                sld.append(setDataToXMLInt("ALIAS TAB",campoForma.getAliasTab()));
            }
            if (campoForma.getAyuda()!=null){
                sld.append(setDataToXMLInt("AYUDA",campoForma.getAyuda()));
            }
            if (campoForma.getCampo()!=null){
                sld.append(setDataToXMLInt("CAMPO",campoForma.getCampo()));
            }
            if (campoForma.getCargaDatoForaneosRetrasada()!=null){
                sld.append(setDataToXMLInt("CARGA DATO FORANEOS RETRASADA",campoForma.getCargaDatoForaneosRetrasada()));
            }
            if (campoForma.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",campoForma.getClaveAplicacion()));
            }
            if (campoForma.getClaveCampo()!=null){
                sld.append(setDataToXMLInt("CLAVE CAMPO",campoForma.getClaveCampo()));
            }
            if (campoForma.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",campoForma.getClaveForma()));
            }
            if (campoForma.getClaveFormaForanea()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA FORANEA",campoForma.getClaveFormaForanea()));
            }
            if (campoForma.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",campoForma.getClavePerfil()));
            }
            if (campoForma.getDatoSensible()!=null){
                sld.append(setDataToXMLInt("DATO SENSIBLE",campoForma.getDatoSensible()));
            }
            if (campoForma.getEditaFormaForanea()!=null){
                sld.append(setDataToXMLInt("EDITA FORMA FORANEA PERFIL",campoForma.getEditaFormaForanea()));
            }
            if (campoForma.getEvento()!=null){
                sld.append(setDataToXMLInt("EVENTO",campoForma.getEvento()));
            }
            if (campoForma.getFiltroForaneo()!=null){
                sld.append(setDataToXMLInt("FILTRO FORANEO",campoForma.getFiltroForaneo()));
            }
            if (campoForma.getForaneo()!=null){
                sld.append(setDataToXMLInt("FORANEO",campoForma.getForaneo()));
            }
            if (campoForma.getJustificarCambio()!=null){
                sld.append(setDataToXMLInt("JUSTIFICAR CAMBIO",campoForma.getJustificarCambio()));
            }
            if (campoForma.getNoPermitirValorForaneoNulo()!=null){
                sld.append(setDataToXMLInt("NO PERMITIR VALOR FORANEO NULO",campoForma.getNoPermitirValorForaneoNulo()));
            }
            if (campoForma.getObligatorio()!=null){
                sld.append(setDataToXMLInt("OBLIGATORIO",campoForma.getObligatorio()));
            }
            if (campoForma.getTabla()!=null){
                sld.append(setDataToXMLInt("TABLA",campoForma.getTabla()));
            }
            if (campoForma.getTamano()!=null){
                sld.append(setDataToXMLInt("TAMAÑO",campoForma.getTamano()));
            }
            if (campoForma.getTipoControl()!=null){
                sld.append(setDataToXMLInt("TIPO CONTROL",campoForma.getTipoControl()));
            }
            if (campoForma.getTypeData()!=null){
                sld.append(setDataToXMLInt("TYPE DATA",campoForma.getTypeData()));
            }
            if (campoForma.getUsadoParaAgrupar()!=null){
                sld.append(setDataToXMLInt("USADO PARA AGRUPAR",campoForma.getUsadoParaAgrupar()));
            }
            if (campoForma.getValorPredeterminado()!=null){
                sld.append(setDataToXMLInt("VALOR PREDERMINADO",campoForma.getValorPredeterminado()));
            }
            if (campoForma.getVisible()!=null){
                sld.append(setDataToXMLInt("VISIBLE",campoForma.getVisible()));
            }
            sld.append("\n\t</registro_CampoForma>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Forma entregado, a la sección
     * de registro_Forma en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param forma     Objeto Forma
     */
    public void setDataToXML(Forma forma){
        StringBuilder sld = new StringBuilder();
        if (forma!=null){
            sld.append("\n<registro_Forma>");
            if (forma.getAliasTab()!=null){
                sld.append(setDataToXMLInt("ALIAS TAB",forma.getAliasTab()));
            }
            if (forma.getCampoPK()!=null){
                sld.append(setDataToXMLInt("CAMPO PK",forma.getCampoPK()));
            }
            if (forma.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",forma.getClaveAplicacion()));
            }
            if (forma.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",forma.getClaveEmpleado()));
            }
            if (forma.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",forma.getClaveForma()));
            }
            if (forma.getClaveFormaPadre()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA PADRE",forma.getClaveFormaPadre()));
            }
            if (forma.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",forma.getClavePerfil()));
            }
            if (forma.getArrVariables()!=null){
                sld.append(setDataToXMLInt(forma.getArrVariables()));
            }
            if (forma.getArrayData()!=null){
                sld.append(setDataToXMLInt(forma.getArrayData()));
            }
            if (forma.getBitacora()!=null){
                sld.append(setDataToXMLInt(forma.getBitacora()));
            }
            if (forma.getDataMail()!=null){
                sld.append(setDataToXMLInt(forma.getDataMail()));
            }
            if (forma.getFormData()!=null){
                sld.append(setDataToXMLInt(forma.getFormData()));
            }
            if (forma.getFormName()!=null){
                sld.append(setDataToXMLInt(forma.getFormName()));
            }
            sld.append("\n</registro_Forma>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto Forma, la
     * que corresponderá a una sección registro_Forma. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param forma    Objeto Forma
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(Forma forma){
        StringBuffer sld = new StringBuffer();
        if (forma!=null){
            sld.append("\n\t<registro_Forma>");
            if (forma.getAliasTab()!=null){
                sld.append(setDataToXMLInt("ALIAS TAB",forma.getAliasTab()));
            }
            if (forma.getCampoPK()!=null){
                sld.append(setDataToXMLInt("CAMPO PK",forma.getCampoPK()));
            }
            if (forma.getClaveAplicacion()!=null){
                sld.append(setDataToXMLInt("CLAVE APLICACIÓN",forma.getClaveAplicacion()));
            }
            if (forma.getClaveEmpleado()!=null){
                sld.append(setDataToXMLInt("CLAVE EMPLEADO",forma.getClaveEmpleado()));
            }
            if (forma.getClaveForma()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA",forma.getClaveForma()));
            }
            if (forma.getClaveFormaPadre()!=null){
                sld.append(setDataToXMLInt("CLAVE FORMA PADRE",forma.getClaveFormaPadre()));
            }
            if (forma.getClavePerfil()!=null){
                sld.append(setDataToXMLInt("CLAVE PERFIL",forma.getClavePerfil()));
            }
            if (forma.getArrVariables()!=null){
                sld.append(setDataToXMLInt(forma.getArrVariables()));
            }
            if (forma.getArrayData()!=null){
                sld.append(setDataToXMLInt(forma.getArrayData()));
            }
            if (forma.getBitacora()!=null){
                sld.append(setDataToXMLInt(forma.getBitacora()));
            }
            if (forma.getDataMail()!=null){
                sld.append(setDataToXMLInt(forma.getDataMail()));
            }
            if (forma.getFormData()!=null){
                sld.append(setDataToXMLInt(forma.getFormData()));
            }
            sld.append("\n\t</registro_Forma>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto DataMail entregado, a la sección
     * de registro_DataMail en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param dataMail  Objeto DataMail
     */
    public void setDataToXML(DataMail dataMail){
        StringBuilder sld = new StringBuilder();
        try{
            if (dataMail!=null){
                sld.append("\n<registro_DataMail>");
                if (dataMail.getNombre()!=null){
                    sld.append(setDataToXMLInt("NOMBRE",dataMail.getNombre()));
                }
                if (dataMail.getAppPaterno()!=null){
                    sld.append(setDataToXMLInt("APELLIDO PATERNO",dataMail.getAppPaterno()));
                }
                if (dataMail.getAppMaterno()!=null){
                    sld.append(setDataToXMLInt("APELLIDO MATERNO",dataMail.getAppMaterno()));
                }
                if (dataMail.getClaveForma()!=null){
                    sld.append(setDataToXMLInt("CLAVE FORMA",dataMail.getClaveForma()));
                }
                if (dataMail.getIdMail()!=null){
                    sld.append(setDataToXMLInt("ID MAIL",dataMail.getIdMail()));
                }
                if (dataMail.getIdQuery()!=null){
                    sld.append(setDataToXMLInt("ID QUERY",dataMail.getIdQuery()));
                }
                if (dataMail.getIdTipoMail()!=null){
                    sld.append(setDataToXMLInt("ID TIPO MAIL",dataMail.getIdTipoMail()));
                }
                if (dataMail.getMail()!=null){
                    sld.append(setDataToXMLInt("MAIL",dataMail.getMail()));
                }
                if (dataMail.getOrden()!=null){
                    sld.append(setDataToXMLInt("ORDEN",dataMail.getOrden()));
                }
                if (dataMail.getPassword()!=null){
                    sld.append(setDataToXMLInt("PASSWORD",dataMail.getPassword()));
                }
                if (dataMail.getQuery()!=null){
                    sld.append(setDataToXMLInt("QUERY",dataMail.getQuery()));
                }
                if (dataMail.getStrCopy()!=null){
                    sld.append(setDataToXMLInt("STRING COPY",dataMail.getStrCopy()));
                }
                if (dataMail.getStrCopyO()!=null){
                    sld.append(setDataToXMLInt("STRING COPY OCULTO",dataMail.getStrCopyO()));
                }
                if (dataMail.getStrTo()!=null){
                    sld.append(setDataToXMLInt("STRING TO",dataMail.getStrTo()));
                }
                if (dataMail.getSubJect()!=null){
                    sld.append(setDataToXMLInt("SUBJECT",dataMail.getSubJect()));
                }
                if (dataMail.getTexto()!=null){
                    sld.append(setDataToXMLInt("TEXTO",dataMail.getTexto()));
                }
                if (dataMail.getTextoImpersonal()!=null){
                    sld.append(setDataToXMLInt("TEXTO IMPERSONAL",dataMail.getTextoImpersonal()));
                }
                if (dataMail.getTipo()!=null){
                    sld.append(setDataToXMLInt("TIPO",dataMail.getTipo()));
                }
                if (dataMail.getTipoMail()!=null){
                    sld.append(setDataToXMLInt("TIPO MAIL",dataMail.getTipoMail()));
                }
                if (dataMail.getTipoQuery()!=null){
                    sld.append(setDataToXMLInt("TIPO QUERY",dataMail.getTipoQuery()));
                }
                if (dataMail.getListCopy()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListCopy()));
                }
                if (dataMail.getListO()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListO()));
                }
                if (dataMail.getListaFrom()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListaFrom()));
                }
                if (dataMail.getListaTo()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListaTo()));
                }
                sld.append("\n</registro_DataMail>");
                if (dataInterna!=null){
                    dataInterna = dataInterna + sld.toString();
                }else{
                    dataInterna = sld.toString();
                }
            }
        }catch(Exception e){}
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto DataMail, la
     * que corresponderá a una sección registro_DataMail. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param dataMail    Objeto DataMail
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(DataMail dataMail){
        StringBuffer sld = new StringBuffer();
        try{
            if (dataMail!=null){
                sld.append("\n\t<registro_DataMail>");
                if (dataMail.getNombre()!=null){
                    sld.append(setDataToXMLInt("NOMBRE",dataMail.getNombre()));
                }
                if (dataMail.getAppPaterno()!=null){
                    sld.append(setDataToXMLInt("APELLIDO PATERNO",dataMail.getAppPaterno()));
                }
                if (dataMail.getAppMaterno()!=null){
                    sld.append(setDataToXMLInt("APELLIDO MATERNO",dataMail.getAppMaterno()));
                }
                if (dataMail.getClaveForma()!=null){
                    sld.append(setDataToXMLInt("CLAVE FORMA",dataMail.getClaveForma()));
                }
                if (dataMail.getIdMail()!=null){
                    sld.append(setDataToXMLInt("ID MAIL",dataMail.getIdMail()));
                }
                if (dataMail.getIdQuery()!=null){
                    sld.append(setDataToXMLInt("ID QUERY",dataMail.getIdQuery()));
                }
                if (dataMail.getIdTipoMail()!=null){
                    sld.append(setDataToXMLInt("ID TIPO MAIL",dataMail.getIdTipoMail()));
                }
                if (dataMail.getMail()!=null){
                    sld.append(setDataToXMLInt("MAIL",dataMail.getMail()));
                }
                if (dataMail.getOrden()!=null){
                    sld.append(setDataToXMLInt("ORDEN",dataMail.getOrden()));
                }
                if (dataMail.getPassword()!=null){
                    sld.append(setDataToXMLInt("PASSWORD",dataMail.getPassword()));
                }
                if (dataMail.getQuery()!=null){
                    sld.append(setDataToXMLInt("QUERY",dataMail.getQuery()));
                }
                if (dataMail.getStrCopy()!=null){
                    sld.append(setDataToXMLInt("STRING COPY",dataMail.getStrCopy()));
                }
                if (dataMail.getStrCopyO()!=null){
                    sld.append(setDataToXMLInt("STRING COPY OCULTO",dataMail.getStrCopyO()));
                }
                if (dataMail.getStrTo()!=null){
                    sld.append(setDataToXMLInt("STRING TO",dataMail.getStrTo()));
                }
                if (dataMail.getSubJect()!=null){
                    sld.append(setDataToXMLInt("SUBJECT",dataMail.getSubJect()));
                }
                if (dataMail.getTexto()!=null){
                    sld.append(setDataToXMLInt("TEXTO",dataMail.getTexto()));
                }
                if (dataMail.getTextoImpersonal()!=null){
                    sld.append(setDataToXMLInt("TEXTO IMPERSONAL",dataMail.getTextoImpersonal()));
                }
                if (dataMail.getTipo()!=null){
                    sld.append(setDataToXMLInt("TIPO",dataMail.getTipo()));
                }
                if (dataMail.getTipoMail()!=null){
                    sld.append(setDataToXMLInt("TIPO MAIL",dataMail.getTipoMail()));
                }
                if (dataMail.getTipoQuery()!=null){
                    sld.append(setDataToXMLInt("TIPO QUERY",dataMail.getTipoQuery()));
                }
                if (dataMail.getListCopy()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListCopy()));
                }
                if (dataMail.getListO()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListO()));
                }
                if (dataMail.getListaFrom()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListaFrom()));
                }
                if (dataMail.getListaTo()!=null){
                    sld.append(setDataToXMLInt(dataMail.getListaTo()));
                }
                sld.append("\n\t</registro_DataMail>");
            }
        }catch(Exception e){}
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Listado entregado, a la sección
     * de registro_Listado en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * Como el contenido de la lista puede ser un objeto cualquiera, se esta
     * dando soporte los distintos tipos manejados por la aplicación:
     * (-) Aplicacion
     * (-) Campo
     * (-) CampoForma
     * (-) HashMap
     * (-) Forma
     * (-) String
     * (-) String[]
     * (-) String[][]
     * @param listado
     */
    public void setDataToXML(List listado)throws ExceptionHandler{
        StringBuilder sld = new StringBuilder();
        if (listado!=null){
            Iterator it = listado.iterator();
            sld.append("\n<registro_Listado>");
            while (it.hasNext()){
                Object obj = it.next();
                if (obj!=null){
                    String strObj = obj.getClass().getName();
                    if ("mx.ilce.controller.Aplicacion".equals(strObj)){
                        Aplicacion apl = (Aplicacion) obj;
                        if (apl!=null){
                            sld.append(setDataToXMLInt(apl));
                        }
                    }else if ("mx.ilce.bean.Campo".equals(strObj)){
                        Campo cmp = (Campo) obj;
                        if (cmp!=null){
                            sld.append(setDataToXMLInt(cmp));
                        }
                    }else if ("mx.ilce.bean.CampoForma".equals(strObj)){
                        CampoForma cmpF = (CampoForma) obj;
                        if (cmpF!=null){
                            sld.append(setDataToXMLInt(cmpF));
                        }
                    }else if ("java.util.HashMap".equals(strObj)){
                        HashMap hsMap = (HashMap) obj;
                        if (hsMap!=null){
                            sld.append(setDataToXMLInt(hsMap));
                        }
                    }else if ("mx.ilce.controller.Forma".equals(strObj)){
                        Forma frm = (Forma) obj;
                        if (frm!=null){
                            sld.append(setDataToXMLInt(frm));
                        }
                    }else if ("mx.ilce.controller.Perfil".equals(strObj)){
                        Perfil perfil = (Perfil) obj;
                        if (perfil!=null){
                            sld.append(setDataToXMLInt(perfil));
                        }
                    }else if ("java.lang.String".equals(strObj)){
                        String str = (String) obj;
                        if (str!=null){
                            sld.append(setDataToXMLInt(str,""));
                        }
                    }else if ("java.lang.String[]".equals(strObj)){
                        String[] strArr = (String[]) obj;
                        if (strArr!=null){
                            sld.append(setDataToXMLInt(strArr));
                        }
                    }else if ("java.lang.String[][]".equals(strObj)){
                        String[][] strArr = (String[][]) obj;
                        if (strArr!=null){
                            sld.append(setDataToXMLInt(strArr));
                        }
                    }
                }
            }
            sld.append("\n</registro_Listado>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos de un objeto List o
     * ArrayList, la que corresponderá a una sección registro_Listado. Usado en
     * las llamadas recursivas para la generación del XML.
     * @param listado    Objeto List o ArrayList
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(List listado){
        StringBuffer sld = new StringBuffer();
        if (listado!=null){
            Iterator it = listado.iterator();
            sld.append("\n\t<registro_Listado>");
            while (it.hasNext()){
                Object obj = it.next();
                if (obj!=null){
                    String strObj = obj.getClass().getName();
                    if ("mx.ilce.controller.Aplicacion".equals(strObj)){
                        Aplicacion apl = (Aplicacion) obj;
                        if (apl!=null){
                            sld.append(setDataToXMLInt(apl));
                        }
                    }else if ("mx.ilce.bean.Campo".equals(strObj)){
                        Campo cmp = (Campo) obj;
                        if (cmp!=null){
                            sld.append(setDataToXMLInt(cmp));
                        }
                    }else if ("mx.ilce.bean.CampoForma".equals(strObj)){
                        CampoForma cmpF = (CampoForma) obj;
                        if (cmpF!=null){
                            sld.append(setDataToXMLInt(cmpF));
                        }
                    }else if ("java.util.HashMap".equals(strObj)){
                        HashMap hsMap = (HashMap) obj;
                        if (hsMap!=null){
                            sld.append(setDataToXMLInt(hsMap));
                        }
                    }else if ("mx.ilce.controller.Forma".equals(strObj)){
                        Forma frm = (Forma) obj;
                        if (frm!=null){
                            sld.append(setDataToXMLInt(frm));
                        }
                    }else if ("java.lang.String".equals(strObj)){
                        String str = (String) obj;
                        if (str!=null){
                            sld.append(setDataToXMLInt(str,""));
                        }
                    }else if ("java.lang.String[]".equals(strObj)){
                        String[] strArr = (String[]) obj;
                        if (strArr!=null){
                            sld.append(setDataToXMLInt(strArr));
                        }
                    }else if ("java.lang.String[][]".equals(strObj)){
                        String[][] strArr = (String[][]) obj;
                        if (strArr!=null){
                            sld.append(setDataToXMLInt(strArr));
                        }
                    }
                }
            }
            sld.append("\n\t</registro_Listado>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto HashMap entregado, a la sección
     * de registro_HashMap en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * Como el contenido del HashMap puede ser un objeto cualquiera, se esta
     * dando soporte los distintos tipos manejados por la aplicación:
     * (-) String
     * (-) HashMap
     * (-) ArrayList
     * (-) List
     * (-) Campo
     * (-) StringBuilder
     * (-) StringBuffer
     * @param hash
     */
    public void setDataToXML(HashMap hash){
        StringBuilder sld = new StringBuilder();
        if (hash!=null){
            sld.append("\n\t<registro_HashMap>");
            Object[] arrKey = hash.keySet().toArray();
            for (int i=0;i<arrKey.length;i++){
                if (hash.containsKey(arrKey[i])){
                    Object obj = hash.get(arrKey[i]);
                    Object objKey = arrKey[i];
                    if (obj!=null){
                        String strObj = obj.getClass().getName();
                        if ("java.lang.String".equals(strObj)){
                            String str = (String) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str));
                            }
                        }else if ("java.util.HashMap".equals(strObj)){
                            HashMap hsMap = (HashMap) obj;
                            if (hsMap!=null){
                                sld.append(setDataToXMLInt(hsMap));
                            }
                        }else if ("java.util.ArrayList".equals(strObj)){
                            ArrayList arrLst = (ArrayList) obj;
                            if (arrLst!=null){
                                sld.append(setDataToXMLInt(arrLst));
                            }
                        }else if ("java.util.List".equals(strObj)){
                            List lst = (List) obj;
                            if (lst!=null){
                                sld.append(setDataToXMLInt(lst));
                            }
                        }else if ("mx.ilce.bean.Campo".equals(strObj)){
                            Campo cmp = (Campo) obj;
                            if (cmp!=null){
                                sld.append(setDataToXMLInt(cmp));
                            }
                        }else if ("java.lang.StringBuilder".equals(strObj)){
                            StringBuilder str = (StringBuilder) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str.toString()));
                            }
                        }else if ("java.lang.StringBuffer".equals(strObj)){
                            StringBuffer str = (StringBuffer) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str.toString()));
                            }
                        }
                    }
                }
            }
            sld.append("\n</registro_HashMap>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }

    /**
     * Obtiene una sección del XML de error con los datos del objeto HashMap, la
     * que corresponderá a una sección registro_DataMail. Usado en las llamadas
     * recursivas para la generación del XML.
     * @param hash    Objeto HashMap
     * @return  StringBuffer    Sección de XML obtenida
     */
    private StringBuffer setDataToXMLInt(HashMap hash){
        StringBuffer sld = new StringBuffer();
        if (hash!=null){
            sld.append("\n\t<registro_HashMap>");
            Object[] arrKey = hash.keySet().toArray();
            for (int i=0;i<arrKey.length;i++){
                if (hash.containsKey(arrKey[i])){
                    Object obj = hash.get(arrKey[i]);
                    Object objKey = arrKey[i];
                    if (obj!=null){
                        String strObj = obj.getClass().getName();
                        if ("java.lang.String".equals(strObj)){
                            String str = (String) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str));
                            }
                        }else if ("java.util.HashMap".equals(strObj)){
                            HashMap hsMap = (HashMap) obj;
                            if (hsMap!=null){
                                sld.append(setDataToXMLInt(hsMap));
                            }
                        }else if ("java.util.ArrayList".equals(strObj)){
                            ArrayList arrLst = (ArrayList) obj;
                            if (arrLst!=null){
                                sld.append(setDataToXMLInt(arrLst));
                            }
                        }else if ("java.util.List".equals(strObj)){
                            List lst = (List) obj;
                            if (lst!=null){
                                sld.append(setDataToXMLInt(lst));
                            }
                        }else if ("mx.ilce.bean.Campo".equals(strObj)){
                            Campo cmp = (Campo) obj;
                            if (cmp!=null){
                                sld.append(setDataToXMLInt(cmp));
                            }
                        }else if ("java.lang.StringBuilder".equals(strObj)){
                            StringBuilder str = (StringBuilder) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str.toString()));
                            }
                        }else if ("java.lang.StringBuffer".equals(strObj)){
                            StringBuffer str = (StringBuffer) obj;
                            if (str!=null){
                                sld.append(setDataToXMLInt(String.valueOf(objKey),str.toString()));
                            }
                        }
                    }
                }
            }
            sld.append("\n\t</registro_HashMap>");
        }
        return sld;
    }

    /**
     * Asigna los datos contenidos en el objeto Enumeration entregado, a la sección
     * de registro_Enumeration en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param enumeration   Objeto Enumeration
     */
    public void setDataToXML(Enumeration enumeration){
        StringBuilder sld = new StringBuilder();
        try{
            if (enumeration!=null){
                sld.append("\n<registro_Enumeration>");
                while (enumeration.hasMoreElements()){
                    String strEnum = (String) enumeration.nextElement();
                    sld.append(setDataToXMLInt("PARAMETRO ENUMERATION",strEnum));
                }
                sld.append("\n</registro_Enumeration>");
                if (dataInterna!=null){
                    dataInterna = dataInterna + sld.toString();
                }else{
                    dataInterna = sld.toString();
                }
            }
        }catch(Exception e){}
    }

    /**
     * Asigna los datos contenidos en el objeto HashCampo entregado, a la sección
     * de registro_HashCampo en la estructura XML que registra un error. Si
     * existían datos previos estos se mantienen y la nueva data se anexa a
     * continuación.
     * @param hashCampo     Objeto HashCampo
     */
    public void setDataToXML(HashCampo hashCampo){
        StringBuilder sld = new StringBuilder();
        if (hashCampo!=null){
            sld.append("\n<registro_HashCampo>");
            if (hashCampo.getLengthCampo()!=null){
                sld.append(setDataToXMLInt("LARGO DE CAMPOS",hashCampo.getLengthCampo()));
            }
            if (hashCampo.getLengthData()!=null){
                sld.append(setDataToXMLInt("LARGO DE LA DATA",hashCampo.getLengthData()));
            }
            if (hashCampo.getPkData()!=null){
                sld.append(setDataToXMLInt("VALOR PK",hashCampo.getPkData()));
            }
            if (hashCampo.getStrQuery()!=null){
                sld.append(setDataToXMLInt("QUERY",hashCampo.getStrQuery()));
            }
            if (hashCampo.getListCampos()!=null){
                sld.append(setDataToXMLInt(hashCampo.getListCampos()));
            }
            if (hashCampo.getListData()!=null){
                sld.append(setDataToXMLInt(hashCampo.getListData()));
            }
            sld.append("\n</registro_HashCampo>");
            if (dataInterna!=null){
                dataInterna = dataInterna + sld.toString();
            }else{
                dataInterna = sld.toString();
            }
        }
    }
}