/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
     * Numero con el tamaño maximo de un archivo en Bytes
     */
    public static String MAXSIZEFILE = "MAXSIZEFILE";

    /**
     * Ruta del server para depositar los archivos enviados por usuarios
     */
    public static String FILESERVER = "FILESERVER";
    /**
     * Lee la configuracion de la base de datos a utilizar presente en el
     * archivo ProyILCE.properties, ubicado en el directorio WEB-INF de la
     * aplicacion
     * @return
     * @throws Exception
     */
    public static Properties leerConfig() throws Exception{
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
	} catch(IOException e) {
            e.printStackTrace();
	}finally{
            if (is != null){
                is.close();
            }
        }
	return prop;
    }

    /**
     * Lee el archivo de properties que contiene los ID de query configurados
     * @return
     * @throws Exception
     */
    public static Properties leerIdQuery() throws Exception{
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
	} catch(IOException e) {
            e.printStackTrace();
	}finally{
            if (is != null){
                is.close();
            }
        }
	return prop;
    }

    /**
     * Obtiene el valor de una palabra clave (key), desde un arreglo de
     * properties
     * @param prop
     * @param strKey
     * @return
     */
    public String getKey(Properties prop, String key){
        String sld = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                sld = prop.getProperty(key);
            }
	}catch(Exception e){
            e.printStackTrace();
	}
        return sld;
    }

    /**
     * Entrega el id de la query que le corresponde segun el archivo de
     * configuracion.
     * @param prop
     * @param key
     * @return
     */
    public Integer getIdQuery(Properties prop, String key){
        Integer sld = Integer.valueOf(0);
        String str = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                str = prop.getProperty(key);
            }
            sld = sld.valueOf(str);
	}catch(Exception e){
            e.printStackTrace();
	}
        return sld;
    }

    /**
     * Borra un archivo desde el servidor. Antes de borrar el archivo, se valida
     * que la ruta que se entrega corresponda con el de la configuracion, destinada
     * a los archivos enviados por el usuario
     * @param hsFile
     * @return
     */
    public static boolean deleFileFromServer(HashMap hsFile){
        boolean isOK = false;
        try {
            if (hsFile!=null){
                if (!hsFile.isEmpty()){
                    Properties prop = AdminFile.leerConfig();
                    AdminFile adm = new AdminFile();
                    String FileServerPath = adm.getKey(prop, AdminFile.FILESERVER);
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
            ex.printStackTrace();
        }
        return isOK;
    }
}
