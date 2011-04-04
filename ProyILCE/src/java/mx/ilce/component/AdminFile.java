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
	private static Properties leerConfig() throws Exception{
		Properties prop = new Properties();
		InputStream is = null;
		File f = null;
		String separador = String.valueOf(File.separator);
		try {
			URL url = AdminFile.class.getResource("AdminFile.class");

			if(url.getProtocol().equals("file")) {
				f = new File(url.toURI());
				//QUITAR LA LINEA SIGUIENTE DEL CODIGO (ubicar el archivo de properties en el directorio WEB-INF)
				//f = f.getParentFile();
				f = f.getParentFile().getParentFile();
				f = f.getParentFile().getParentFile();
				WORKING_DIRECTORY = f.getParentFile();
			}
			File fichero = new File(WORKING_DIRECTORY + separador + "ProyILCE.properties");
			if (fichero.exists()){
				is=new FileInputStream(fichero.getAbsolutePath());
				prop.load(is);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static String getKey(String strKey){
		String sld = "";
		try{
			Properties prop = leerConfig();
			Enumeration e = prop.keys();
			if (e.hasMoreElements()){
				sld = prop.getProperty(strKey);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sld;
	}
}
