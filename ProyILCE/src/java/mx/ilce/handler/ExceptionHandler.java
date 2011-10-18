package mx.ilce.handler;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import mx.ilce.util.UtilDate;

/**
 * Manejador de Exception general, que permite unificar las respuestas generales
 * de las Exception ocurridas en la aplicacion. Mediante algunas configuraciones
 * y entradas adicionales, permite dar informes mas generales, ademas de tomar
 * acciones adicionales como la generacion de archivos con el registro de las
 * Exception, junto con su traza, ubicacion, dia y hora de su activacion.
 * @author ccatrilef
 */
public final class ExceptionHandler extends Throwable {

    //private enum formato {DMA,AMD};
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
        }else {  
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
                str.append("</query>").append("\n");
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

    /******** GETTER AND SETTER ***********/

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
     * Metodo que entrega un valor booleano para considerar o no la inclusion
     * de la data contenida en el campo stringData
     * @return  Boolean     Valor TRUE o FALSE de la validacion
     */
    public boolean seeStringData() {
        return seeStringData;
    }

    /**
     * Metodo que asigna un valor booleano para considerar o no la inclusion
     * de la data contenida en el campo stringData
     * @param seeStringData     Valor TRUE o FALSE de la validacion
     */
    public void setSeeStringData(boolean seeStringData) {
        this.seeStringData = seeStringData;
    }

    /**
     * Obtiene la ruta asignada para la generacion del archivo de Log de la
     * exception
     * @return  String  Texto con la ruta
     */
    public String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta para la generacion del archivo de Log de la exception
     * @param rutaFile      Texto con la ruta
     */
    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene la respuesta de si se debe generar o no el archivo de Log
     * @return  Boolean     Valor TRUE o FALSE de la validacion
     */
    public boolean isLogFile() {
        return logFile;
    }

    /**
     * Asigna la respuesta de si se debe generar o no el archivo de Log
     * @param logFile       Valor TRUE o FALSE de la validacion
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
     * Obtiene la fecha y hora en que se genero a la exception
     * @return  String  Texto con la fecha y hora
     */
    public String getTime() {
        return time;
    }

    /**
     * Obtiene la fecha y hora en que se genero a la exception. Se entrega cual
     * debe ser el caracter separador para representar la fecha
     * @param separador     Caracter separador que debe utilizarce al obtener
     * un dato del tipo fecha
     * @return  String  Texto con la fecha y hora
     */
    public String getTime(String separador) {
        String strTime = time.replaceAll("/", separador);
        return strTime;
    }

    /**
     * Asigna la fecha y hora en que se genero a la exception.
     * @param time  Texto con la fecha y hora
     */
    private void setTime(String time) {
        this.time = time;
    }

    /**
     * Obtiene la secuencia de clases y metodos invocados en la exception
     * @return  StringBuffer    Texto con la secuencia de invocacion
     */
    public StringBuffer getSecuenceError() {
        return secuenceError;
    }

    /**
     * Asigna la secuencia de clases y metodos invocados en la exception
     * @param secuenceError     Texto con la secuencia de invocacion
     */
    private void setSecuenceError(String secuenceError) {
        this.secuenceError.append(secuenceError);
    }

    /**
     * Obtiene el texto de error ingresado al campo textError
     * @return  StringBuffer    Texto con la descripcion del error
     */
    public StringBuffer getTextError() {
        return textError;
    }

    /**
     * Asigna el texto de error al campo textError
     * @param textError     Texto con la descripcion del error
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
     * clase definida para manejar la escritura de archivos de Log con los datos
     * contenidos en la exception
     */
    private class writeLogError{

        private String rutaFile;
        private String nameFile;

        /**
         * Obtiene el nombre que poseera el archivo de Log
         * @return  String  Nombre del archivo
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
         * @return  String  Ruta del archivo
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
         * @return  Boolean     Valor de la validacion del resultado de la operacion
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
         * Obtiene el numero de linea de la clase donde se produjo el error
         * @return  int Numero de la linea del error
         */
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * Asigna el numero de linea de la clase donde se produjo el error
         * @param lineNumber    Numero de la linea del error
         */
        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        /**
         * Obtiene el metodo de la clase donde se produjo el error
         * @return  String  Metodo donde se hizo la invocacion
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
}