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
package mx.ilce.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase implementada para contener utilidades asociadas al envio de mail
 * @author ccatrilef
 */
class UtilMail {

    private static File WORKING_DIRECTORY;

    /**
     * Obtiene un Objeto Address[] que contiene los mail recuperados de un
     * texto, en el cual estan separados por un caracter entregado como dato
     * de entrada. Se requiere este objeto pues es el que maneja el Controller
     * Java para envio de mail.
     * @param entrada       Texto con los mail
     * @param separador     Separador de los mail en el texto
     * @return Address[]    Objeto con los mail
     * @throws ExceptionHandler
     */
    public Address[] getAddressFromText(String entrada, String separador) throws ExceptionHandler{
        String destino = entrada;
        String[] destinos = null;
        Address[] addressDest = null;
        try {
            if ((destino!=null)&&(destino.contains(separador))){
                destinos = destino.split(separador);
                String[] paso = new String[destinos.length];
                int j=0;
                for (int i=0; i<destinos.length; i++){
                    String data = (destinos[i]==null)?"":destinos[i];
                    data = data.trim();
                    if (!"".equals(data)){
                        paso[j++]=data;
                    }
                }
                addressDest = new InternetAddress[j];
                for (int i=0; i<j; i++){
                    addressDest[i] = new InternetAddress(paso[i]);
                }
            }
        } catch (AddressException e) {
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para obtener direcciones de mail");
            eh.setDataToXML("ENTRADA", entrada);
            eh.setDataToXML("SEPARADOR", separador);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return addressDest;
    }

    /**
     * Método para leer la configuración de los datos del mail en el archivo
     * de properties mail.properties
     * @return Properties   Objeto Properties con los datos leidos
     * @throws ExceptionHandler
     */
    public static Properties leerConfig()throws ExceptionHandler{
        Properties props = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
        URL url = null;
	try {
            String separador = String.valueOf(File.separator);
            url = UtilMail.class.getResource("UtilMail.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
		f = f.getParentFile().getParentFile();
		f = f.getParentFile().getParentFile();
		WORKING_DIRECTORY = f.getParentFile();
            }
            fichero = new File(WORKING_DIRECTORY + separador + "mail.properties");
            if (fichero.exists()){
                is=new FileInputStream(fichero.getAbsolutePath());
                props.load(is);
            }
        } catch(URISyntaxException u){
            ExceptionHandler eh = new ExceptionHandler(u,UtilMail.class,
                             "Problemas para leer el archivo de configuración de Mail");
            eh.setDataToXML("ARCHIVO",((url==null)?"":url.toString()));
            eh.setDataToXML("FILE",((f==null)?"":f.toString()));
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
	} catch(IOException e) {
            throw new ExceptionHandler(e,UtilMail.class,
                    "Problemas para leer el archivo de configuración de Mail");
        }finally{
            try{
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,UtilMail.class,
                        "Problemas para leer el archivo de configuración de Mail");
            }
        }
        return props;
    }
}
