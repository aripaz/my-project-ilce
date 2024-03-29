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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.conection.ConEntidad;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase implementada para la administración de archivos de la aplicación
 * @author ccatrilef
 */
public class AdminFile {
    private static File WORKING_DIRECTORY;
    /**
     * Query para obtener datos del Usuario mediante User y password
     */
    public static String LOGIN = "LOGIN";
    /**
     * Query para obtener datos del Usuario mediante el User
     */
    public static String USER = "USER";
    /**
     * Query para obtener los datos del perfil
     */
    public static String PERFIL = "PERFIL";
    /**
     * Query para obtener los tab de aplicacion mediante la aplicacion y la
     * forma
     */
    public static String TABFORMA = "TABFORMA";
    /**
     * Query para obtener los datos del XML de Session
     */
    public static String XMLSESSION = "XMLSESSION";
    /**
     * Query para obtener los datos del XML de menu (el acordeon)
     */
    public static String XMLMENU = "XMLMENU";
    /**
     * Query para obtener los datos de la forma
     */
    public static String FORMA = "FORMA";
    /**
     * Query para obtener los datos de la forma filtrando por un listado
     * de campos
     */
    public static String FORMACAMPOS = "FORMACAMPOS";
    /**
     * Query para obtener la query en base a la forma
     */
    public static String FORMAQUERY = "FORMAQUERY";
    /**
     * Query para obtener el evento asociado a una forma
     */
    public static String EVENTO = "EVENTO";
    /**
     * Query para manejo de formas foráneas
     */
    public static String FORMASFORANEAS="FORMASFORANEAS";

    /*
     * Query para manejo de flujo de datos
     */
    public static String DISPARANOTIFICACION="DISPARANOTIFICACION";    
    /**
     * Número con el tamaño máximo de un archivo en Bytes
     */
    public static String MAXSIZEFILE = "MAXSIZEFILE";
    /**
     * Ruta del server para depositar los archivos enviados por usuarios
     */
    public static String FILESERVER = "FILESERVER";
    /**
     * Ruta del server para depositar los archivos de Log
     */
    public static String LOGFILESERVER = "LOGFILESERVER";
    /**
     * Clave del perfil administrador
     */
    public static String IDADMIN = "IDADMIN";
    /**
     * Query con los permisos
     */
    public static String PERMISOS = "PERMISOS";
    /**
     * Directorio donde se colocan los archivos asociados a usuarios
     */
    public static String BIBLIOTECA = "BIBLIOTECA";
    /**
     * Cabecera de la Ruta web del proyecto
     */
    public static String PROYECTO = "PROYECTO";

    private String rutaFile;
    private String rutaDest;
    private String nameFile;
    private Integer idUser;
    private Integer idForma;
    private Integer idRegister;
    private Bitacora bitacora;

    /**
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora  Objeto Bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el ID del registro
     * @return  Integer     ID del registro
     */
    public Integer getIdRegister() {
        return idRegister;
    }

    /**
     * Asigna el ID del registro
     * @param idRegister    ID del registro
     */
    public void setIdRegister(Integer idRegister) {
        this.idRegister = idRegister;
    }

    /**
     * Obtiene el ID de la forma
     * @return  Integer     ID de la Forma
     */
    private Integer getIdForma() {
        return idForma;
    }

    /**
     * Asigna el ID de la forma
     * @param idForma   ID de la forma
     */
    public void setIdForma(Integer idForma) {
        this.idForma = idForma;
    }

    /**
     * Obtiene el nombre del archivo
     * @return  String  Nombre del Archivo
     */
    public String getNameFile() {
        return nameFile;
    }

    /**
     * Asigna el nombre del Archivo
     * @param nameFile  Nombre del Archivo
     */
    private void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    /**
     * Obtiene la ruta del archivo
     * @return  String  Ruta del Archivo
     */
    private String getRutaFile() {
        return rutaFile;
    }

    /**
     * Asigna la ruta del archivo
     * @param rutaFile  Ruta del archivo
     */
    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    /**
     * Obtiene el ID del User
     * @return  Integer     ID del User
     */
    private Integer getIdUser() {
        return idUser;
    }

    /**
     * Asigna el ID del User
     * @param idUser    ID del User
     */
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    /**
     * Obtiene la ruta de destino del archivo
     * @return  String  Ruta de destino
     */
    private String getRutaDest() {
        return rutaDest;
    }

    /**
     * Asigna la ruta de destino de archivo
     * @param rutaDest  Ruta de destino
     */
    private void setRutaDest(String rutaDest) {
        this.rutaDest = rutaDest;
    }

    /**
     * Lee la configuración de la base de datos a utilizar presente en el
     * archivo ProyILCE.properties
     * @return  Properties  Properties obtenidas del archivo
     * @throws Exception
     */
    public static Properties leerConfig() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
        URL url = null;
	try {
            String separador = String.valueOf(File.separator);
            url = AdminFile.class.getResource("AdminFile.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
		f = f.getParentFile().getParentFile();
		f = f.getParentFile().getParentFile();
		WORKING_DIRECTORY = f.getParentFile();
            }
            fichero = new File(WORKING_DIRECTORY + separador + "ProyILCE.properties");
            if (fichero.exists()){
                is=new FileInputStream(fichero.getAbsolutePath());
                prop.load(is);
            }
        } catch(URISyntaxException u){
            ExceptionHandler eh = new ExceptionHandler(u,AdminFile.class,
                             "Problemas para leer el archivo de configuración");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	} catch(IOException e) {
            ExceptionHandler eh = new ExceptionHandler(e,AdminFile.class,
                             "Problemas para leer el archivo de configuración");
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
                throw new ExceptionHandler(e,AdminFile.class,
                        "Problemas para cerrar el archivo de configuración");
            }
        }
	return prop;
    }

    /**
     * Lee el archivo de properties que contiene los ID de queries configuradas
     * @return  Properties  Properties con los ID de queries obtenidas
     * @throws Exception
     */
    public static Properties leerIdQuery() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
        URL url = null;
	try {
            String separador = String.valueOf(File.separator);
            url = AdminFile.class.getResource("AdminFile.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
		f = f.getParentFile().getParentFile();
		f = f.getParentFile().getParentFile();
		WORKING_DIRECTORY = f.getParentFile();
            }
            fichero = new File(WORKING_DIRECTORY + separador + "IdQuery.properties");
            if (fichero.exists()){
                is=new FileInputStream(fichero.getAbsolutePath());
                prop.load(is);
            }
        } catch(URISyntaxException u){
            ExceptionHandler eh = new ExceptionHandler(u,AdminFile.class,
                                        "Problemas para leer el archivo de Queries");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	} catch(IOException e) {
            ExceptionHandler eh = new ExceptionHandler(e,AdminFile.class,
                                        "Problemas para leer el archivo de Queries");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	}finally{
            try {
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminFile.class,
                        "Problemas para cerrar el archivo de Queries");
            }
        }
	return prop;
    }


    /**
     * Obtiene el valor de una palabra clave (key), desde un arreglo de
     * properties
     * @param prop      Listado de properties obtenido desde el archivo de configuracion
     * @param strKey    Palabra usada como Key para la busqueda dentro del propertie
     * @return  String  Valor de la Key en el listado de Properties
     */
    public static String getKey(Properties prop, String key) throws ExceptionHandler{
        String sld = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                sld = prop.getProperty(key);
            }
	}catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,AdminFile.class,
                                        "Problemas para obtener la llave desde el properties");
            eh.setDataToXML("KEY",key);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	}
        return sld;
    }

    /**
     * Entrega el id de la query que le corresponde según el archivo de
     * configuración.
     * @param prop  Listado de properties obtenido desde el archivo de configuración
     * @param key   Palabra usada como Key para la busqueda dentro del propertie
     * @return  Integer     ID de la query en el listado de Properties
     */
    public static Integer getIdQuery(Properties prop, String key) throws ExceptionHandler{
        Integer sld = Integer.valueOf(0);
        String str = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                str = prop.getProperty(key);
            }
            sld = Integer.valueOf(str);
	}catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,AdminFile.class,
                                        "Problemas para obtener el ID de la Query");
            eh.setDataToXML("KEY",key);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	}
        return sld;
    }

    /**
     * Borra un archivo desde el servidor. Antes de borrar el archivo, se valida
     * que la ruta que se entrega corresponda con el de la configuración, destinada
     * a los archivos enviados por el usuario
     * @param hsFile    HashMap con los datos del archivo
     * @return  boolean     Resultado de la operación de borrado del archivo temporal
     */
    public static boolean deleFileFromServer(HashMap hsFile) throws ExceptionHandler{
        boolean isOK = false;
        try {
            if (hsFile!=null){
                if (!hsFile.isEmpty()){
                    Properties prop = AdminFile.leerConfig();
                    String FileServerPath = AdminFile.getKey(prop, AdminFile.FILESERVER);
                    Collection col = hsFile.values();
                    Iterator it = col.iterator();
                    while (it.hasNext()){
                        String str = (String) it.next();
                        int len = FileServerPath.length();
                        String strSub = str.substring(0, len);
                        if (FileServerPath.equals(strSub)){
                            File file = new File(str);
                            file.delete();
                        }
                    }
                    isOK=true;
                }
            }
        } catch (Exception ex) {
            ExceptionHandler eh = new ExceptionHandler(ex,AdminFile.class,
                                        "Problemas para borrar los archivos desde el Server");
            eh.setDataToXML(hsFile);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return isOK;
    }

    /**
     * Método para invocar la copia del archivo subido al directorio Temporal
     * hasta el repositorio de archivos
     * @return  boolean     Resultado de la operación de copia
     * @throws ExceptionHandler
     */
    public boolean putFile() throws ExceptionHandler{
        boolean sld = false;

        String ruta = this.getRutaFile();
        if (ruta!=null){
            Properties prop = leerConfig();
            String rutaBibl = getKey(prop, BIBLIOTECA);
            String Proyecto = getKey(prop, PROYECTO);
            String[] data = rutaBibl.split("/");

            URL url = AdminFile.class.getResource("AdminFile.class");
            String[] urlDir = url.getPath().split("/");

            boolean seguir = true;
            int pos=0;
            for (int i=0;i<urlDir.length&&seguir;i++){
                if (urlDir[i].equals(Proyecto)){
                   pos = urlDir.length - i;
                   seguir= false;
                }
            }
            File fWork = null;
            if(url.getProtocol().equals("file")) {
                try {
                    fWork = new File(url.toURI());
                    for (int j=0;j<pos;j++){
                        fWork = fWork.getParentFile();
                    }
                } catch (URISyntaxException ex) {}
            }
            File srcDir = new File(fWork.getPath());
            for (int i=1;i<data.length;i++){
                srcDir = new File(srcDir.getPath() + fWork.separator + data[i]);
                if (!srcDir.exists()) {
                    srcDir.mkdir();
                }
            }
            rutaBibl = srcDir.getPath() + srcDir.separator;

            String nameF = this.getRutaFile();
            nameF = nameF.replace(getKey(prop, FILESERVER),"");
            this.setNameFile(nameF);
            if (rutaBibl!=null){
                rutaBibl = rutaBibl + getIdUser();
                setRutaDest(rutaBibl);  
                try {
                    if (copiarArchivo()){
                        String nameSld = getKey(prop, "BIBLIOTECA");
                        nameSld = nameSld + getIdUser() + "/" + this.getNameFile();
                        this.setNameFile(nameSld);
                        sld = true;
                    }
                } catch (IOException ex) {}
            }
        }
        return sld;
    }

    /**
     * Método que efectúa la copia de un archivo desde una ruta de Origen a
     * una de Destino
     * @return  boolean     Resultado de la operación de copia
     * @throws IOException
     */
    private boolean copiarArchivo() throws IOException{
        boolean sld = false;
        FileInputStream in = null;
        FileOutputStream out = null;
        File srcDir = null;
        String source = null;
        String dest = null;
        try{
            //asignamos la ruta del usuario
            dest = this.getRutaDest();
            source = this.getRutaFile();
            srcDir = new File(dest);
            //crear directorio si no existe
            if (!srcDir.exists()) {
                srcDir.mkdir();
            }

            dest = dest + File.separator + this.getNameFile();

            //copiar archivo subido a destino
            in = (new FileInputStream(source));
            out = (new FileOutputStream(dest));
            int c;
            while( (c = in.read() ) != -1)
                out.write(c);
            sld = true;
        }
        catch(Exception e)
        {
            ExceptionHandler eh = new ExceptionHandler(e,AdminFile.class,
                                        "Problemas al copiar el archivo");
            eh.setDataToXML("ORIGEN",source);
            eh.setDataToXML("DESTINO",dest);
            eh.setDataToXML("ARCHIVO",this.getNameFile());
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            try {
                throw eh;
            } catch (ExceptionHandler ex) {
            }
        }finally{
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
        }
        return sld;
    }

    /**
     * Método que registra en la base de datos, el ingreso de un archivo en el
     * repositorio
     * @return  Integer     ID del registro del archivo
     * @throws ExceptionHandler
     */
    public Integer registerFile() throws ExceptionHandler{
        Integer sld = null;
            String nameF = this.getNameFile();
            if (nameF.length()>255){
                nameF = nameF.substring(0,254);
            }
            String query = "insert into archivo "
                    + "(archivo,fecha,clave_empleado,clave_forma,clave_registro)"
                    + " values ('" + nameF + "'"
                    + " , getDate()," + this.getIdUser()
                    + ", " + this.getIdForma()
                    + ", " + this.getIdRegister() + ")";

            
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setQueryInsert(query);
            CampoForma campoForma = new CampoForma();
            campoForma.setTabla("archivo");
            dataTransfer.setCampoForma(campoForma);
            
            ConEntidad conE = new ConEntidad();
            conE.setBitacora(this.getBitacora());
            conE.ingresaEntidad(dataTransfer);

        return sld;
    }
}