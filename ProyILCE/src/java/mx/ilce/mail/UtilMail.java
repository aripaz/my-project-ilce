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
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtener direcciones de mail");
            eh.setStringData(entrada);
            eh.setSeeStringData(true);
            throw eh;
        }
        return addressDest;
    }

    public static Properties leerConfig()throws ExceptionHandler{
        Properties props = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
	try {
            String separador = String.valueOf(File.separator);
            URL url = UtilMail.class.getResource("UtilMail.class");

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
            throw new ExceptionHandler(u,UtilMail.class,"Problemas para leer el archivo de configuracion de Mail");
	} catch(IOException e) {
            throw new ExceptionHandler(e,UtilMail.class,"Problemas para leer el archivo de configuracion de Mail");
        }finally{
            try{
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,UtilMail.class,"Problemas para leer el archivo de configuracion de Mail");
            }
        }
        return props;
    }
}
