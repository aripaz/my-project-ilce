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
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author ccatrilef
 */
public class AdminFile {
    private static File WORKING_DIRECTORY;

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
}
