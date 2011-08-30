package mx.ilce.handler;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * Manejador de Exception general, que permite unificar las respuestas generales
 * de las Exception ocurridas en la aplicacion. Mediante algunas configuraciones
 * y entradas adicionales, permite dar informes mas generales, ademas de tomar
 * acciones adicionales como la generacion de archivos con el registro de las
 * Exception, junto con su traza, ubicacion, dia y hora de su activacion.
 * @author ccatrilef
 */
public final class ExceptionHandler extends Throwable {

    private enum formato {DMA,AMD};
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

    /**
     * Constructor Basico
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
     * Metodo para la limpieza del objeto antes de la generacion de datos
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
     * exception ocurrida, la clase donde ocurrio el problema y un mensaje que se
     * desea desplegar, junto con los mensajes obtenidos desde la exception y la
     * data adicional entregada.
     * @param obj       Clase del tipo de Error (Exception, NullPointerException, etc)
     * @param clase     Clase donde se produjo el error
     * @param message   Mensaje que se quiere agregar al error ademas de los
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
        }else {  //
            Exception e = (Exception)obj;
            setTextError(e.getMessage());
            getStackTrace(e.getStackTrace(),clase);
            setSecuenceError(getStringSecuenceError().toString());
        }
    }

    /**
     * Manda la instruccion de escribir en un archivo los datos de error capturados
     * en la exception. Requiere que previamente se haya entregado la ruta donde
     * se depositara el archivo de error y haber marcado como TRUE que debe generarse
     * el archivo
     * @return
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
                    strAdicional.append("No Existe el Directorio para Log\n");
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
                strNameFile = strNameFile.concat(ut.getFecha(formato.AMD,""));
                strNameFile = strNameFile.concat(this.getTypeError().toString());
                strNameFile = strNameFile.concat(".log");
                wf.setNameFile(strNameFile);
                wf.setRutaFile(strRutaFile);
                StringBuffer strTexto = new StringBuffer();
                strTexto.append("\nFECHA: ").append(this.getTime());
                strTexto.append("\nTIPO: ").append(this.getTypeError());
                strTexto.append("\nMENSAGE: ").append(this.getTextMessage());
                strTexto.append("\nDESCRIPCION: ").append(this.getTextError());
                if ((this.stringData!=null)&&(!"".equals(this.stringData))){
                    strTexto.append("\nDATA ADICIONAL:\n").append(this.getStringData());
                }
                if ((this.strQuery!=null)&&(!"".equals(this.strQuery))){
                    strTexto.append("\nQUERY:\n").append(this.getStrQuery());
                }
                strTexto.append("\nTRAZA\n-------------\n").append(this.getSecuenceError());
                strTexto.append("\n****************\n");
                if (sld){
                    sld = wf.guardarArchivo(strTexto);
                }else{
                    this.setStringData(strAdicional.toString());
                }
            }catch(IOException e){
                //e.printStackTrace();
            }
        }
        return sld;
    }

    /**
     * Metodo que en base a los datos contenidos en la Exception genera un XML
     * con dichos datos
     */
    private void setTextToXmlError(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        str.append("<error>\n");
        str.append("<general>");
        str.append(this.getTextMessage());
        str.append("</general>").append("\n");
        str.append("<fecha>");
        str.append(this.getTime("-"));
        str.append("</fecha>").append("\n");
        str.append("<descripcion>");
        str.append(this.getTextError());
        str.append("</descripcion>").append("\n");
        str.append("<tipo>");
        str.append(this.getTypeError());
        str.append("</tipo>").append("\n");
        if (this.seeStringData()){
            if ((this.strQuery!=null) && (!"".equals(this.strQuery))) {
                str.append("<query>");
                str.append(this.getStrQuery());
                str.append("<query>").append("\n");
            }
            if ((this.stringData!=null) && (!"".equals(this.stringData))) {
                str.append("<dataAdicional>");
                str.append(this.getStringData());
                str.append("</dataAdicional>").append("\n");
            }
        }
        str.append("</error>");
        this.setXmlError(str);
    }

    /**
     * Metodo que captura el StackTrace completo de la exception generada
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
     * Metodo que construye un texto con la secuencia de ejecucion del exception
     * con el objeto de identificar en que clase se produjo, en que metodo y en
     * que linea se produjo el error
     * @return
     */
    private StringBuffer getStringSecuenceError(){
        StringBuffer sld = new StringBuffer();
        sld.append("ARCHIVO\t,LINEA\t,METODO\t,CLASE\n");
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

    /******** GETTER AND SETTER ***********/

    public String getStrQuery() {
        return strQuery;
    }

    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Metodo para ver si se debe considerar la data incluida en el campo
     * stringData
     * @return
     */
    public boolean seeStringData() {
        return seeStringData;
    }

    /**
     * Metodo que asigna un valor booleano para considerar o no la inclusion
     * de la data contenida en el campo stringData
     * @param seeStringData     Entrada TRUE o FALSE, para indicar si se debe
     * considerar en la generacion del informe de error la data adicional
     */
    public void setSeeStringData(boolean seeStringData) {
        this.seeStringData = seeStringData;
    }

    /**
     * Obtiene la ruta asignada para la generacion del archivo de Log de la
     * exception
     * @return
     */
    public String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta para la generacion del archivo de Log de la exception
     * @param rutaFile      Ruta donde debe depositarse el archivo de error
     */
    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene la respuesta de si se debe generar o no el archivo de Log
     * @return
     */
    public boolean isLogFile() {
        return logFile;
    }

    /**
     * Asigna la respuesta de si se debe generar o no el archivo de Log
     * @param logFile       Entrada TRUE o FALSE para indicar si se debe generar
     * o no el archivo de Log
     */
    public void setLogFile(boolean logFile) {
        this.logFile = logFile;
    }

    /**
     * Obtiene el arreglo de datos entregados a la Exception
     * @return
     */
    public ArrayList getArrayData() {
        return arrayData;
    }

    /**
     * Asigna el arreglo de datos entregados a la Exception
     * @param arrayData     ArrayList que contiene los datos de error adicionales
     * que se agregaran al informe de error
     */
    public void setArrayData(ArrayList arrayData) {
        this.arrayData = arrayData;
    }

    /**
     * Obtiene los datos contenidos en el campo stringData
     * @return
     */
    public String getStringData() {
        return stringData;
    }

    /**
     * Asigna los datos que debe contener en el campo stringData
     * @param stringData    String que contiene data o mensajes adicionales que
     * deben ser incluidos en el informe de error
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
     * @return
     */
    public StringBuffer getTypeError() {
        return typeError;
    }

    /**
     * Asigna el tipo de error que se produjo
     * @param typeError     Asigna el nombre del tipo de error producido
     */
    private void setTypeError(String typeError) {
        this.typeError.append(typeError);
    }

    /**
     * Obtiene la fecha y hora en que se genero a la exception
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * Obtiene la fecha y hora en que se genero a la exception. Se entrega cual
     * debe ser el caracter separador para representar la fecha
     * @param separador     Caracter separador que debe utilizarce al obtener
     * un dato del tipo fecha
     * @return
     */
    public String getTime(String separador) {
        String strTime = time.replaceAll("/", separador);
        return strTime;
    }

    /**
     * Asigna la fecha y hora en que se genero a la exception.
     * @param time
     */
    private void setTime(String time) {
        this.time = time;
    }

    /**
     * Obtiene la secuencia de clases y metodos invocados en la exception
     * @return
     */
    public StringBuffer getSecuenceError() {
        return secuenceError;
    }

    /**
     * Asigna la secuencia de clases y metodos invocados en la exception
     * @param secuenceError     Texto con la secuencia de errores
     */
    private void setSecuenceError(String secuenceError) {
        this.secuenceError.append(secuenceError);
    }

    /**
     * Obtiene el texto de error ingresado al campo textError
     * @return
     */
    public StringBuffer getTextError() {
        return textError;
    }

    /**
     * Asigna el texto de error al campo textError
     * @param textError     Texto que contiene el Error entregado
     */
    public void setTextError(String textError) {
        this.textError.append((textError==null)?"":textError);
    }

    /**
     * Obtiene el texto del mensaje ingresado al campo textMessage
     * @return
     */
    public StringBuffer getTextMessage() {
        return textMessage;
    }

    /**
     * Asigna el texto del mensaje al campo textMessage
     * @param textMessage   Texto que contiene el Mensaje enregado
     */
    private void setTextMessage(String textMessage) {
        this.textMessage.append((textMessage==null)?"":textMessage);
    }

    /**
     * Obtiene el xml generado con los datos de la Exception
     * @return
     */
    public StringBuffer getXmlError() {
        setTextToXmlError();
        return xmlError;
    }

    /**
     * Asigna el xml generado con los datos de la Exception al campo xmlError
     * @param xmlError      Texto con el XML de error generado
     */
    private void setXmlError(StringBuffer xmlError) {
        this.xmlError = xmlError;
    }

    /**
     * clase definida para manejar la escritura de archivos de Log con los datos
     * contenidos en la exception
     */
    private class writeLogError{

        private String rutaFile;
        private String nameFile;

        /**
         * Obtiene el nombre que poseera el archivo de Log
         * @return
         */
        public String getNameFile() {
            return nameFile;
        }

        /**
         * Asigna el nombre que poseera el archivo de Log
         * @param nameFile  Nombre del archivo
         */
        public void setNameFile(String nameFile) {
            this.nameFile = nameFile;
        }

        /**
         * Obtiene la ruta donde se colocara el archivo de Log
         * @return
         */
        public String getRutaFile() {
            return rutaFile;
        }

        /**
         * Asigna la ruta donde se colocara el archivo de Log
         * @param rutaFile  Ruta del archivo
         */
        public void setRutaFile(String rutaFile) {
            this.rutaFile = rutaFile;
        }

        /**
         * Metodo que escribe la data entregada al archivo de Log. Si el archivo
         * existe, el texto entregado se adjunta al final del archivo
         * @param strEntrada    Texto con lo que debe anexarse al archivo de error
         * @return
         * @throws IOException
         */
        public boolean guardarArchivo(StringBuffer strEntrada) throws IOException{
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
         * @return
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
         * Obtiene el numero de linea de la clase donde se produjo el error
         * @return
         */
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * Asigna el numero de linea de la clase donde se produjo el error
         * @param lineNumber    Numero de linea del error
         */
        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        /**
         * Obtiene el metodo de la clase donde se produjo el error
         * @return
         */
        public String getMethodClass() {
            return methodClass;
        }

        /**
         * Asigna el metodo de la clase donde se produjo el error
         * @param methodClass   Metodo donde se hizo la invocacion
         */
        public void setMethodClass(String methodClass) {
            this.methodClass = methodClass;
        }

        /**
         * Obtiene el nombre de la clase donde se produjo el error
         * @return
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
     * Clase implementada para contener utilidades para la obtencion de fechas
     * en un formato especifico y con mayor o menor contenido de datos
     */
    private class UtilDate{

        private int dia;
        private int mes;
        private int anio;
        private int hour;
        private int min;
        private int sec;
        private String separador="/";

        /**
        * Constructor donde se inicializa con el calculo del momento los datos de
        * la clase
        */
        public UtilDate() {
            java.util.Calendar now = java.util.Calendar.getInstance();
            this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
            this.mes = now.get(java.util.Calendar.MONTH);
            this.anio = now.get(java.util.Calendar.YEAR);
            this.hour = now.get(java.util.Calendar.HOUR);
            this.min = now.get(java.util.Calendar.MINUTE);
            this.sec = now.get(java.util.Calendar.SECOND);
        }

        /**
        * Constructor donde se inicializa con los parametros de entrada, los datos
        * de la clase
        * @param dia    Dato con el dia a asignar
        * @param mes    Dato con el mes a asignar
        * @param anio   Dato con el año a asignar
        */
        public UtilDate(int dia, int mes, int anio) {
            this.dia = dia;
            this.mes = mes;
            this.anio = anio;
            this.hour = 0;
            this.min = 0;
            this.sec = 0;
        }

        /**
        * Constructor donde se inicializa con los parametros de entreda los datos
        * de la clase
        * @param dia    Dato con el dia a asignar
        * @param mes    Dato con el mes a asignar
        * @param anio   Dato con el año a asignar
        * @param hour   Dato con la hora a asignar
        * @param min    Dato con el minuto a asignar
        * @param sec    Dato con el segundo a asignar
        */
        public UtilDate(int dia, int mes, int anio, int hour, int min, int sec) {
            this.dia = dia;
            this.mes = mes;
            this.anio = anio;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
        }

        /**
        * Metodo que recarga los datos de la clase con el calculo del momento
        */
        public void recargaFecha(){
            java.util.Calendar now = java.util.Calendar.getInstance();
            this.dia = now.get(java.util.Calendar.DAY_OF_MONTH);
            this.mes = now.get(java.util.Calendar.MONTH);
            this.anio = now.get(java.util.Calendar.YEAR);
            this.hour = now.get(java.util.Calendar.HOUR);
            this.min = now.get(java.util.Calendar.MINUTE);
            this.sec = now.get(java.util.Calendar.SECOND);
        }

        /**
        * Entrega la fecha que contiene la clase en formato DD/MM/AAAA.
        * En caso que se haya asignado una fecha incorrecta, entregara 00/00/0000
        * @return
        */
        public String getFecha(){
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String str = strDia+"/"+strMes+"/"+strAnio;
            if (isFechaValida(str)){
                if (this.dia<10){
                    strDia = "0"+strDia;
                }
                if (this.mes<10){
                    strMes = "0"+strMes;
                }
            }else{
                strDia="00";
                strMes="00";
                strAnio="0000";
            }
            return strDia+separador+strMes+separador+strAnio;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato DD/MM/AAAA hh:mm:ss
        * En caso que se haya asignado una fecha incorrecta, entregara 00/00/0000 00:00:00
        * @return
        */
        public String getFechaHMS(){
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String strHour = String.valueOf(this.hour);
            String strMin = String.valueOf(this.min);
            String strSec = String.valueOf(this.sec);
            String str = strDia+"/"+strMes+"/"+strAnio;
            if (isFechaValida(str)){
                if (this.dia<10){
                strDia = "0"+strDia;
                }
                if (this.mes<10){
                strMes = "0"+strMes;
                }
                if (this.hour<10){
                strHour = "0"+strHour;
                }
                if (this.min<10){
                strMin = "0"+strMin;
                }
                if (this.sec<10){
                strSec = "0"+strSec;
                }
            }else{
                strDia="00";
                strMes="00";
                strAnio="0000";
                strHour="00";
                strMin="00";
                strSec="00";
            }
            return strDia+separador+strMes+separador+strAnio+" "+strHour+":"+strMin+":"+strSec;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato solicitado
        * AMD=AAAA/MM/DD, DMA=DD/MM/AAAA. En caso que se haya asignado una
        * fecha incorrecta, entregara 00/00/0000
        * @param frm    Formato que se desea para la fecha AMD, DMA
        * @return
        */
        public String getFecha(formato frm){
            String sld = "";
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String str = strDia+"/"+strMes+"/"+strAnio;
            if (isFechaValida(str)){
                if (this.dia<10){
                strDia = "0"+strDia;
                }
                if (this.mes<10){
                strMes = "0"+strMes;
                }
            }else{
                strDia="00";
                strMes="00";
                strAnio="0000";
            }
            if (frm.equals(frm.AMD)){
                sld = strAnio+separador+strMes+separador+strDia;
            }else{
                sld = strDia+separador+strMes+separador+strAnio;
            }
            return sld;
        }

        /**
        * Entrega la fecha que contiene la clase en el formato solicitado
        * AMD=AAAA/MM/DD hh:mm:ss, DMA=DD/MM/AAAA hh:mm:ss. En caso que se haya
        * asignado una fecha incorrecta, entregara 00/00/0000
        * @param frm    Formato que se desea para la fecha AMD, DMA
        * @return
        */
        public String getFechaHMS(formato frm){
            String sld = "";
            String strDia = String.valueOf(this.dia);
            String strMes = String.valueOf(this.mes);
            String strAnio = String.valueOf(this.anio);
            String strHour = String.valueOf(this.hour);
            String strMin = String.valueOf(this.min);
            String strSec = String.valueOf(this.sec);
            String str = strDia+"/"+strMes+"/"+strAnio;
            if (isFechaValida(str)){
                if (this.dia<10){
                strDia = "0"+strDia;
                }
                if (this.mes<10){
                strMes = "0"+strMes;
                }
                if (this.hour<10){
                strHour="0"+strHour;
                }
                if (this.min<10){
                strMin="0"+strMin;
                }
                if (this.sec<10){
                strSec = "0"+strSec;
                }
            }else{
                strDia="00";
                strMes="00";
                strAnio="0000";
                strHour="00";
                strMin="00";
                strSec="00";
            }
            if (frm.equals(frm.AMD)){
            sld = strAnio+separador+strMes+separador+strDia;
            }else{
            sld = strDia+separador+strMes+separador+strAnio;
            }
            sld = sld+" "+strHour+":"+strMin+":"+strSec;
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, en formato DD/MM/AAAA,
        * reemplazando el caracter / por el solicitado
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFecha(String separador){
            String sld = getFecha();
                sld = sld.replace("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, en formato DD/MM/AAAA hh:mm:ss,
        * reemplazando el caracter / por el solicitado
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFechaHMS(String separador){
            String sld = getFechaHMS();
            sld = sld.replace("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, con el formato solicitado y
        * reemplazando el caracter / por el entregado
        * @param frm   Formato que debe poseer la fecha
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFecha(formato frm, String separador){
            String sld = getFecha(frm);
            sld = sld.replaceAll("/", separador);
            return sld;
        }

        /**
        * Entrega la fecha existente en la clase, incluyendo la hora, minutos y
        * segundos, con el formato solicitado y reemplazando el caracter / por el
        * entregado
        * @param frm   Formato que debe poseer la fecha
        * @param separador Caracter separador que debe poseer la fecha en vez de /
        * @return
        */
        public String getFechaHMS(formato frm, String separador){
            String sld = getFechaHMS(frm);
            sld = sld.replaceAll("/", separador);
            return sld;
        }

        /**
         * Metodo para validar si una fecha es correcta
         * @param fechax
         * @return
         */
        private boolean isFechaValida(String fechax) {
            try {
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                formatoFecha.setLenient(false);
                formatoFecha.parse(fechax);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}