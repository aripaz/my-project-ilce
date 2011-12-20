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
package mx.ilce.component;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.UtilDate;

/**
 * Clase implementada para lograr control de la recuperación de formularios
 * desde la capa Vista
 * @author ccatrilef
 */
public class AdminForm {

    private static File WORKING_DIRECTORY;

    /**
     * Método que entrega un HashMap que contiene los datos de un formulario,
     * separados en dos Hash: Datos (FORM) y Archivos (FILES) y dos
     * ArrayList: con los nombres de los campos del formulario (arrayFORM) y
     * uno con los nombres de los campos de archivo (arrayFILE). Para poder
     * recuperar los Archivos se requiere que el contentType del Form sea
     * "multipart/form-data", sino es de este tipo, no se envía el Hash de
     * archivos y el campo correspondiente al archivo se incluye dentro del
     * Hash de FORM. La estructura de cada hash contiene el nombre del campo
     * como clave y el dato obtenido del formulario, ambos en formato String.
     * Cuando se envía el contentType correcto y existe el archivo, se deja
     * una copia del mismo, con un nombre nuevo en el servidor, asociado al
     * campo archivo, va el nuevo nombre y su ruta con que quedo en el servidor.
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return  HashMap     Contiene los datos de las variables encontradas
     * @throws ExceptionHandler
     */
    public HashMap getFormulario(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        try{
            String contentType = request.getContentType();
            if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)){
                hs = getFormularioMultiPart(request);
            }else if ((contentType != null) && (contentType.indexOf("application/x-www-form-urlencoded") >= 0)){
                hs = getFormularioWwwForm(request);
            }else{
                hs = getFormularioSimple(request);
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el formulario");
            eh.setDataToXML("CONTENT TYPE", request.getContentType());
            eh.setDataToXML(hs);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hs;
    }

    /**
     * Método que entrega un Hash con el contenido de un formulario, en el Hash de
     * datos (FORM) y un arreglo con los nombres de los campos de datos del
     * formulario (arrayFORM). Los datos asociados a archivos solo se entregan
     * como otro dato más, no generándose el hash con los datos de archivo ni
     * realizándose la copia en el servidor
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return  HashMap     Resultado de los datos leidos del Formulario
     * @throws ExceptionHandler
     */
    private HashMap getFormularioSimple(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        HashMap hsForm = null;
        try{ 
            Enumeration enumData = request.getParameterNames();
            ArrayList arrayFORM = new ArrayList();
            if (enumData != null){
                hsForm = new HashMap();
                while (enumData.hasMoreElements()){
                    String strEnum = (String) enumData.nextElement();
                    String[] strData = request.getParameterValues(strEnum);
                    String value = strData[0];
                    value = castCodeEncoded(value);
                    value = castCodeNoUtf8(value);
                    value = castCodeUrl(value);
                    hsForm.put(strEnum, value);
                    arrayFORM.add(strEnum);
                }
                hs = new HashMap();
                hs.put("FORM", hsForm);
                hs.put("arrayFORM", arrayFORM);
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el formulario Simple");
            eh.setDataToXML(request.getParameterNames());
            eh.setDataToXML(hs);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hs;

    }
    
    /**
     * Método que entrega un Hash con el contenido de un formulario, separados en el Hash
     * de datos (FORM), el de archivos (FILE), un arreglo con nombres campos de
     * datos del formulario (arrayFORM) y un arreglo con los nombres de los
     * campos de archivo (arrayFILE). Cuando los archivos que se indican
     * existen, se genera una copia en el servidor y se entrega en el Hash, el
     * nombre y ruta del archivo. Se usa para formularios del tipo multipart/form-data
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return  HashMap     Resultado de los datos leidos del Formulario, incluye los archivos
     * @throws ExceptionHandler
     */
    private HashMap getFormularioMultiPart(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        HashMap hsForm = null;
        HashMap hsFile = null;
        try{
            HashMap hsTypeAcepted = getContentTypeAcepted();

            Properties prop = AdminFile.leerConfig();

            String FileServerPath = AdminFile.getKey(prop, AdminFile.FILESERVER);
            String maxSizeFile = AdminFile.getKey(prop, AdminFile.MAXSIZEFILE);
            int maxPostSize = Integer.valueOf(maxSizeFile);
            ArrayList arrayFORM = new ArrayList();
            ArrayList arrayFILE = new ArrayList();

            MultipartParser mp = new MultipartParser(request, maxPostSize,true,true);
            mp.setEncoding("UTF-8");

            Part part;
            while ((part = mp.readNextPart()) != null) {
                if (hs==null){
                    hs = new HashMap();
                }
                String name = part.getName();
                if (part.isParam()) {
                    ParamPart paramPart = (ParamPart) part;
                    String value = paramPart.getStringValue();
                    if (hsForm==null){
                        hsForm = new HashMap();
                    }
                    value = castCodeEncoded(value);
                    value = castCodeNoUtf8(value);
                    value = castCodeUrl(value);
                    hsForm.put(name, value);
                    arrayFORM.add(name);
                }else if (part.isFile()) {
                    FilePart filePart = (FilePart) part;
                    String fileName = filePart.getFileName();
                    if (fileName!=null){
                        String typeFile = filePart.getContentType();
                        String evalType = (String) hsTypeAcepted.get(typeFile);

                        if ((evalType!=null) &&("OK".equals(evalType))){

                            if ((fileName != null)&&(!"".equals(fileName))) {
                                if (hsFile==null){
                                    hsFile = new HashMap();
                                }
                                UtilDate ud = new UtilDate();
                                String strDia = ud.getFechaHMS(UtilDate.formato.AMD,"");
                                strDia = strDia.replaceAll(":","");
                                strDia = strDia.replaceAll(" ","_");
                                String dirName = FileServerPath + strDia + "." + fileName;
                                File dir = new File(dirName);
                                long size = filePart.writeTo(dir);
                                if (size>0){
                                    hsFile.put(name, dirName);
                                    arrayFILE.add(name);
                                }
                            }
                        }else{
                            throw new ExceptionHandler("Captura de archivo", AdminForm.class,
                                    "Formato de archivo no aceptado", "Tipo de archivo enviado:" + typeFile);
                        }
                    }
                }
            }
            hs.put("FORM", hsForm);
            hs.put("FILE", hsFile);
            hs.put("arrayFORM", arrayFORM);
            hs.put("arrayFILE", arrayFILE);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el formulario multipart");
            eh.setDataToXML(hs);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hs;
    }

    /**
     * Método que entrega un Hash con el contenido de un formulario, en el Hash de
     * datos (FORM) y un arreglo con los nombres de los campos de datos del
     * formulario (arrayFORM). Los datos asociados a archivos solo se entregan
     * como otro dato más, no generándose el hash con los datos de archivo ni
     * realizándose la copia en el servidor. Se usa para formularios del tipo
     * application/x-www-form-urlencoded
     * @param request
     * @return HashMap Resultado de los datos leidos del formulario
     * @throws ExceptionHandler
     */
    private HashMap getFormularioWwwForm(HttpServletRequest request) throws ExceptionHandler{
        ServletInputStream instream = null;
        HashMap hs = null;
        try {
            ArrayList arrayFORM = new ArrayList();
            int inputLen, offset;
            boolean dataRemaining=true;
            boolean existData=false;
            byte[] postedBytes = null;
            String postedBody;
            int length = request.getContentLength();
            instream = request.getInputStream();

            if (length>0){
                postedBytes = new byte[length];
                offset = 0;
                while(dataRemaining) {
                    inputLen = instream.read (postedBytes, offset,length-offset);
                    if (inputLen <= 0) {
                        throw new IOException ("Error de lectura de archivo");
                    }
                    offset += inputLen;
                    if((length-offset) ==0) {
                        dataRemaining=false;
                    }
                }
                postedBody = new String (postedBytes);
                StringTokenizer st = new StringTokenizer(postedBody, "&");
                String key=null;
                String value=null;

                HashMap hsForm = new HashMap();
                while (st.hasMoreTokens()) {
                    String pair = (String)st.nextToken();
                    int pos = pair.indexOf('=');
                    if (pos == -1) {
                        throw new IllegalArgumentException();
                    } 
                    key = java.net.URLDecoder.decode(pair.substring(0, pos),"UTF-8");
                    value = pair.substring(pos+1,pair.length());
                    value = castCodeNoUtf8(value);
                    value = castCodeUrl(value);
                    value = castCodeEncoded(value);
                    value = value.replaceAll("\\+","\\%2B");
                    try{
                        value = java.net.URLDecoder.decode(value,"UTF-8");
                    }catch(IllegalArgumentException e){
                        if (value.contains("%")){
                            String[] strVal = value.split("%");
                            String[] paso = new String[strVal.length];
                            for (int i=0;i<strVal.length;i++){
                                paso[i] = java.net.URLDecoder.decode(strVal[i],"UTF-8");
                            }
                            value = paso[0];
                            for (int i=1;i<strVal.length;i++){
                                value = value + "%" + paso[i];
                            }
                        }
                    }
                    hsForm.put(key, value);
                    arrayFORM.add(key);
                    existData=true;
                }
                if (existData){
                    hs = new HashMap();
                    hs.put("FORM", hsForm);
                    hs.put("arrayFORM", arrayFORM);
                }
            }else{
                hs = getFormularioSimple(request);
            }
        } catch (IOException ex) {
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el formulario x-www-form-urlencoded");
            eh.setDataToXML(hs);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        } finally {
            try {
                instream.close();
            } catch (IOException ex) {
                throw new ExceptionHandler(ex,this.getClass(),
                        "Problemas para obtener el formulario multipart, al cerrar ServletInputStream");
            }
        }
        return hs;
    }

    /**
     * Método que entrega los datos de una variable. Primero los busca como
     * atributo de la Session, si no lo encuentra, lo busca como parámetro de
     * un formulario
     * @param name  Nombre de la variable a buscar
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return
     */
    public String getStringRequest(String name, HttpServletRequest request) throws ExceptionHandler{
        String data = null;
        try{
            data = (String) request.getSession().getAttribute(name);
            if (data==null){
                data = (String) request.getParameter(name);
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener los datos de un campo en el request");
            eh.setDataToXML("CAMPO FORMULARIO", name);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return data;
    }

    /**
     * Método con los ContentType aceptados y que deben rechazarse
     * @return  HashMap     HashMap con contentType
     */
    private HashMap getContentTypeAcepted(){
        HashMap hs = new HashMap();
        //IMAGENES
        hs.put("image/gif", "OK");
        hs.put("image/x-png", "OK");
        hs.put("image/jpeg", "OK");
        hs.put("image/x-ms-bmp", "OK");
        hs.put("image/bmp", "OK");
        //WORD
        hs.put("application/msword", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "OK");
        //EXCEL
        hs.put("application/x-excel", "OK");
        hs.put("application/vnd.ms-excel", "OK");
        hs.put("application/x-msexcel", "OK");
        hs.put("application/ms-excel", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "OK");
        //POWERPOINT
        hs.put("application/vnd.ms-powerpoint", "OK");
        hs.put("application/ms-powerpoint", "OK");
        hs.put("application/mspowerpoint", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "OK");
        hs.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "OK");
        //PROYECT
        hs.put("application/vnd.ms-project", "OK");
        //WORK
        hs.put("application/vnd.ms-works", "OK");
        hs.put("application/vnd.ms-tnef", "OK");
        hs.put("application/vnd.artgalry", "OK");
        //ZIP
        hs.put("application/zip", "OK");
        hs.put("application/x-compressed", "OK");
        //RTF
        hs.put("application/rtf", "OK");
        //PDF
        hs.put("application/pdf", "OK");
        hs.put("application/x-pdf", "OK");
        //HTML
        hs.put("text/html", "OK");
        //VISIO
        hs.put("application/visio", "OK");
        hs.put("application/x-visio", "OK");
        hs.put("application/vnd.visio", "OK");
        hs.put("application/visio.drawing", "OK");
        hs.put("application/vsd", "OK");
        hs.put("application/x-vsd", "OK");
        //TEXTO
        hs.put("text/plain", "OK");
        //NO PERMITIDOS
        hs.put("application/octet-stream", "NOK");
        hs.put("text/asp", "NOK");
        hs.put("text/jsp", "NOK");
        hs.put("text/js", "NOK");
        hs.put("text/php", "NOK");
        hs.put("text/x-script.perl", "NOK");

        return hs;
    }

    /**
     * Método para convertir caracteres obtenidos de un encodeURI de Jquery, 
     * principalmente formulado para no perder caracteres especiales enviados
     * por la capa vista y que no son bien traducidos por el decode de Java
     * @param val   String con los datos
     * @return
     */
    private String castCodeEncoded(String val){
        String str = val;
        str = str.replaceAll("\\%C3\\%A1","á");
        str = str.replaceAll("\\%C3\\%A9","é");
        str = str.replaceAll("\\%C3\\%AD","í");
        str = str.replaceAll("\\%C3\\%B1","ñ");
        str = str.replaceAll("\\%C3\\%B3","ó");
        str = str.replaceAll("\\%C3\\%BA","ú");
        str = str.replaceAll("\\%C3\\%BC","ü");
        str = str.replaceAll("\\%C3\\%81","Á");
        str = str.replaceAll("\\%C3\\%89","É");
        str = str.replaceAll("\\%C3\\%8D","Í");
        str = str.replaceAll("\\%C3\\%93","Ó");
        str = str.replaceAll("\\%C3\\%9A","Ú");
        str = str.replaceAll("\\%C3\\%9C","Ü");
        str = str.replaceAll("\\%C2\\%A1","¡");
        str = str.replaceAll("\\%C2\\%A6","¦");
        str = str.replaceAll("\\%20", " ");
        str = str.replaceAll("\\%21", "!");
        str = str.replaceAll("\\%22", "\"");
        str = str.replaceAll("\\%23", "#");
        str = str.replaceAll("\\%24", "\\$");
        str = str.replaceAll("\\%25", "\\%");
        str = str.replaceAll("\\%26", "\\&");
        str = str.replaceAll("\\%27", "'");
        str = str.replaceAll("\\%28", "(");
        str = str.replaceAll("\\%29", ")");
        str = str.replaceAll("\\%2A", "*");
        //str = str.replaceAll("\\%2B", "+");
        str = str.replaceAll("\\%2C", ",");
        str = str.replaceAll("\\%2D", "-");
        str = str.replaceAll("\\%2E", ".");
        str = str.replaceAll("\\%2F", "/");
        str = str.replaceAll("\\%3A", ":");
        str = str.replaceAll("\\%3B", ";");
        str = str.replaceAll("\\%3C", "<");
        str = str.replaceAll("\\%3D", "=");
        str = str.replaceAll("\\%3E", ">");
        str = str.replaceAll("\\%3F", "?");
        str = str.replaceAll("\\%5B", "[");
        str = str.replaceAll("\\%5C", "\\");
        str = str.replaceAll("\\%5D", "]");
        str = str.replaceAll("\\%5E", "^");
        str = str.replaceAll("\\%5F", "_");
        str = str.replaceAll("\\%7B", "{");
        str = str.replaceAll("\\%7C", "|");
        str = str.replaceAll("\\%7D", "}");
        str = str.replaceAll("\\%7E", "~");
        str = str.replaceAll("\\%40", "@");
        str = str.replaceAll("\\%0A", "\n");
        return str;
    }

    /**
     * Método para convertir caracteres NO UTF8 a uno manejable para que se guarde
     * en la base de datos
     * @param data  String con los datos
     * @return
     */
    private String castCodeNoUtf8(String data){
        String str = "";
        if (data!=null){
            str = data;
            if (str.contains("Ã¡")){
                str = str.replaceAll("Ã¡", "á");
            }
            if (str.contains("Ã€")){
                str = str.replaceAll("Ã€", "Á");
            }
            if (str.contains("Ã©")){
                str = str.replaceAll("Ã©", "é");
            }
            if (str.contains("Ã‰")){
                str = str.replaceAll("Ã‰", "É");
            }
            if (str.contains("Ã­")){
                str = str.replaceAll("Ã­", "í");
            }
            if (str.contains("Ã*")){
                str = str.replaceAll("Ã*", "Í");
            }
            if (str.contains("Ã³")){
                str = str.replaceAll("Ã³", "ó");
            }
            if (str.contains("Ã“")){
                str = str.replaceAll("Ã“", "Ó");
            }
            if (str.contains("Ãº")){
                str = str.replaceAll("Ãº", "ú");
            }
            if (str.contains("Ãš")){
                str = str.replaceAll("Ãš", "Ú");
            }
            if (str.contains("Ã±")){
                str = str.replaceAll("Ã±", "ñ");
            }
            if (str.contains("Ã‘")){
                str = str.replaceAll("Ã‘", "Ñ");
            }
        }else{
            str = data;
        }
        return str;
    }

    /**
     * Método para convertir caracteres obtenidos de un decode de Jquery,
     * principalmente formulado para no perder caracteres especiales enviados
     * por la capa vista y que no son bien traducidos por el decode de Java
     * @param strData   String con los datos
     * @return
     */
    private String castCodeUrl(String strData){
        String str = "";
        if (strData!=null){
            str = strData;
            if (str.contains("%E1")){
                str = str.replaceAll("%E1","á");
            }
            if (str.contains("%E9")){
                str = str.replaceAll("%E9","é");
            }
            if (str.contains("%ED")){
                str = str.replaceAll("%ED","í");
            }
            if (str.contains("%F3")){
                str = str.replaceAll("%F3","ó");
            }
            if (str.contains("%FA")){
                str = str.replaceAll("%FA","ú");
            }
            if (str.contains("%C1")){
                str = str.replaceAll("%C1","Á");
            }
            if (str.contains("%C9")){
                str = str.replaceAll("%C9","É");
            }
            if (str.contains("%CD")){
                str = str.replaceAll("%CD","Í");
            }
            if (str.contains("%D3")){
                str = str.replaceAll("%D3","Ó");
            }
            if (str.contains("%DA")){
                str = str.replaceAll("%DA","Ú");
            }
            if (str.contains("%D1")){
                str = str.replaceAll("%D1","Ñ");
            }
            if (str.contains("%F1")){
                str = str.replaceAll("%F1","ñ");
            }
            if (str.contains("%E4")){
                str = str.replaceAll("%E4","ä");
            }
            if (str.contains("%EB")){
                str = str.replaceAll("%EB","ë");
            }
            if (str.contains("%EF")){
                str = str.replaceAll("%EF","ï");
            }
            if (str.contains("%F6")){
                str = str.replaceAll("%F6","ö");
            }
            if (str.contains("%FC")){
                str = str.replaceAll("%FC","ü");
            }
            if (str.contains("%C4")){
                str = str.replaceAll("%C4","Ä");
            }
            if (str.contains("%CB")){
                str = str.replaceAll("%CB","Ë");
            }
            if (str.contains("%CF")){
                str = str.replaceAll("%CF","Ï");
            }
            if (str.contains("%D6")){
                str = str.replaceAll("%D6","Ö");
            }
            if (str.contains("%DC")){
                str = str.replaceAll("%DC","Ü");
            }
        }
        return str;
    }

    /**
     * Método que lee el archivo variables.properties, para obtener las variables
     * que servirán para la evaluación de queries
     * @return  Properties      Objeto con las variables leidas
     * @throws ExceptionHandler
     */
    private static Properties leerConfigVariables() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
        URL url = null;
	try {
            String separador = String.valueOf(File.separator);
            url = AdminFile.class.getResource("AdminForm.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
		f = f.getParentFile().getParentFile();
		f = f.getParentFile().getParentFile();
		WORKING_DIRECTORY = f.getParentFile();
            }
            fichero = new File(WORKING_DIRECTORY + separador + "variables.properties");
            if (fichero.exists()){
                is=new FileInputStream(fichero.getAbsolutePath());
                prop.load(is);
            }
        } catch(URISyntaxException u){
            ExceptionHandler eh = new ExceptionHandler(u,AdminForm.class,
                             "Problemas para leer el archivo de configuración de Variables");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	} catch(IOException e) {
            ExceptionHandler eh = new ExceptionHandler(e,AdminForm.class,
                             "Problemas para leer el archivo de configuración de Variables");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminForm.class,
                        "Problemas para cerrar el archivo de configuración de Variables");
            }
        }
	return prop;
    }

    /**
     * Método que obtiene un arreglo, construido a partir del archivo variables.properties, el cual
     * contiene los valores que servirán de referencia para buscar en el formulario
     * @param hsForm    Hash que contiene los datos leidos desde el formulario
     * @return  String[][]  Arreglo con los valores
     * @throws ExceptionHandler
     */
    public String[][] getVariablesFromProperties(HashMap hsForm) throws ExceptionHandler{
        String[][] strSld = null;

        Properties prop = leerConfigVariables();
        if (prop.size()>0) {
            strSld = new String[prop.size()][2];
            Enumeration e = prop.keys();
            int i=0;
            while (e.hasMoreElements()){
                String strKey = (String) e.nextElement();
                String strData = prop.getProperty(strKey);
                strSld[i][0] = strData;
                if (hsForm!=null){
                    String strForm = (String)hsForm.get(strData);
                    if ((strForm!=null)&&(!"".equals(strForm))){
                        strSld[i][1] = strForm;
                    }else{
                        strSld[i][1] = strKey;
                    }
                }else{
                    strSld[i][1] = strKey;
                }
                i++;
            }
        }
        return strSld;
    }

    /**
     * Método que toma un Objeto de referencia y busca en los valores que entregan los métodos
     * que correspondan (por su nombre) a las variables, reemplazando los valores
     * @param obj   Objeto de referencia, de donde se sacaran los valores
     * @param arrVariable   Arreglo que contiene las variables que se estan buscando
     * @return  String[][]  Arreglo con el resultado de los valores
     * @throws ExceptionHandler
     */
    public String[][] getVariableByObject(Object obj, String[][] arrVariable) throws ExceptionHandler{
        String[][] strSld = null;
        try{
            Class clase = Class.forName(obj.getClass().getName());
            Method[] met = clase.getDeclaredMethods();
            if (met.length>0){
                Object[] paramDato = null;
                for (int j=0;j<arrVariable.length;j++){
                    for(int i=0;i<met.length;i++){
                        String strIni = met[i].getName().substring(0,3);
                        if ("get".equals(strIni)){
                            String nameCampo = "GET"+((arrVariable[j][1]==null)?"":arrVariable[j][1]).toUpperCase();
                            String nameMetodo = met[i].getName().toUpperCase();
                            if (nameMetodo.equals(nameCampo)){
                                Object objRet =  met[i].getReturnType();
                                objRet = met[i].invoke(obj, paramDato);
                                arrVariable[j][1]= String.valueOf(objRet);
                            }
                        }
                    }
                }
                strSld = arrVariable;
            }
        }catch(NullPointerException e0){
            ExceptionHandler eh = new ExceptionHandler(e0,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML(arrVariable);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(ClassNotFoundException e1){
            ExceptionHandler eh = new ExceptionHandler(e1,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML(arrVariable);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalAccessException e2){
            ExceptionHandler eh = new ExceptionHandler(e2,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML(arrVariable);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalArgumentException e3){
            ExceptionHandler eh = new ExceptionHandler(e3,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML(arrVariable);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(InvocationTargetException e4){
            ExceptionHandler eh = new ExceptionHandler(e4,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML(arrVariable);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return strSld;
    }

    /**
     * Método encargado de limpiar las variables que no hayan sido encontradas
     * en el formulario y/o en la clases de referencia, dejándolas con un valor
     * null, para que no sean consideradas en el análisis de las queries.
     * @param arrVariable   Arreglo que contiene las variables que deben ser 
     * consideradas en las evaluaciones de las queries
     * @return String[][]   Arreglo con las variables sin data anuladas
     * @throws ExceptionHandler
     */
    public String[][] cleanVariables(String[][] arrVariable) throws ExceptionHandler{
        String[][] strSld = arrVariable;

        Properties prop = leerConfigVariables();
        if (!prop.isEmpty()){
            for (int i=0;i<strSld.length;i++){
                String strData = strSld[i][1];
                String strKey = prop.getProperty(strData);
                if ((strKey!=null)&&(!"".equals(strKey))){
                    if (strKey.equals(strSld[i][0])){
                        strSld[i][1]=null;
                    }
                }
            }
        }
        return strSld;
    }

    /**
     * Método para la entrega de valores por defecto a algunos tipos de datos
     * especialmente definidos
     * @param className
     * @param value
     * @return
     */
    public String getDefaultValueByClass(String className, String value){
        String strSld = null;
        if (value==null){
            if ("mx.ilce.bean.BIT".equals(className)){
                strSld = "0";
            }else if ("mx.ilce.bean.Money".equals(className)){
                strSld = "0";
            }
        }else{
            strSld=value;
        }
        return strSld;
    }

    /**
     * Método encargado de limpiar la variable SIDX enviada desde la capa vista para
     * poder determinar por cual campo se debe ordenar la query. Usada principalmente
     * en el ordenamiento de las Grillas.
     * @param strData       El contenido de la variable
     * @param separador     El separador usado
     * @param numPaso       Posición para considerar en la eliminación
     * @return  String      Nombre del campo a usar
     */
    public String cleanSIDX(String strData, String separador, int numPaso){
        String strSld = strData;

        if ((strSld !=null)&&(!"".equals(strSld))){
            String[] strPaso = strSld.split(separador);
            int pos = strPaso.length - numPaso;
            if (pos>0){
                strSld = "";
                for(int i=0;i<pos;i++){
                    strSld = strSld + separador + strPaso[i];
                }
                strSld = strSld.substring(1,strSld.length());
            }
        }
        return strSld;
    }
}
