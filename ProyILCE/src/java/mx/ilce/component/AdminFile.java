package mx.ilce.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import mx.ilce.handler.ExceptionHandler;

/**
 *  Clase implementada para la administracion de archivos de la aplicacion
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
     * Numero con el tamaño maximo de un archivo en Bytes
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

    public static String PERMISOS = "PERMISOS";

    /**
     * Lee la configuracion de la base de datos a utilizar presente en el
     * archivo ProyILCE.properties, ubicado en el directorio WEB-INF de la
     * aplicacion
     * @return
     * @throws Exception
     */
    public static Properties leerConfig() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
	try {
            String separador = String.valueOf(File.separator);
            URL url = AdminFile.class.getResource("AdminFile.class");

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
            throw new ExceptionHandler(u,AdminFile.class,"Problemas para leer el archivo de configuracion");
	} catch(IOException e) {
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para leer el archivo de configuracion");
        }finally{
            try{
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminFile.class,"Problemas para cerrar el archivo de configuracion");
            }
        }
	return prop;
    }

    /**
     * Lee el archivo de properties que contiene los ID de query configurados
     * @return
     * @throws Exception
     */
    public static Properties leerIdQuery() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
	try {
            String separador = String.valueOf(File.separator);
            URL url = AdminFile.class.getResource("AdminFile.class");

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
            throw new ExceptionHandler(u,AdminFile.class,"Problemas para leer el archivo de Queries");
	} catch(IOException e) {
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para leer el archivo de Queries");
	}finally{
            try {
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminFile.class,"Problemas para cerrar el archivo de Queries");
            }
        }
	return prop;
    }


    /**
     * Obtiene el valor de una palabra clave (key), desde un arreglo de
     * properties
     * @param prop      Listado de properties obtenido desde el archivo de configuracion
     * @param strKey    Palabra usada como Key para la busqueda dentro del propertie
     * @return
     */
    public static String getKey(Properties prop, String key) throws ExceptionHandler{
        String sld = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                sld = prop.getProperty(key);
            }
	}catch(Exception e){
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para obtener la llave desde el properties");
	}
        return sld;
    }

    /**
     * Entrega el id de la query que le corresponde segun el archivo de
     * configuracion.
     * @param prop  Listado de properties obtenido desde el archivo de configuracion
     * @param key   Palabra usada como Key para la busqueda dentro del propertie
     * @return
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
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para obtener el ID de la Query");
	}
        return sld;
    }

    /**
     * Borra un archivo desde el servidor. Antes de borrar el archivo, se valida
     * que la ruta que se entrega corresponda con el de la configuracion, destinada
     * a los archivos enviados por el usuario
     * @param hsFile    HashMap con los datos del archivo
     * @return
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
            throw new ExceptionHandler(ex,AdminFile.class,"Problemas para borrar los archivos desde el Server");
        }
        return isOK;
    }
}