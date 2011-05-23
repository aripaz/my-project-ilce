package mx.ilce.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;

/**
 *  Clase implementada para contener un conjunto de metodos de validacion
 * @author ccatrilef
 */
public class Validation {

    private String textMessage;

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    /**
     * Metodo para validar si un dato es Par o no
     * @param data
     * @return
     */
    public static boolean isPar(String data){
        int i =  Integer.parseInt(data) ;
        int j = i%2;
        return ((j==0)?true:false);
    }

    /**
     * Se utiliza para ejecutar el despliegue de error para cuando se valido que
     * un formulario no esta completo en la data exigida
     * @param lstVal
     * @param clase
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void executeErrorValidation(List lstVal, Class clase, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String dataVal = (String) lstVal.get(1);
            ExceptionHandler eh = new ExceptionHandler(dataVal,
                    clase.getClass(),
                    "Error de Datos de entrada","No se entrego valor en los datos");
            eh.setRutaFile(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER));
            eh.setLogFile(true);
            eh.writeToFile();
            StringBuffer xmlError = eh.getXmlError();
            request.getSession().setAttribute("xmlError", xmlError);
            request.getRequestDispatcher("/resource/jsp/xmlError.jsp").forward(request, response);
        }catch (ExceptionHandler e){
            
        }catch (Exception es){
            ExceptionHandler eh2 = new ExceptionHandler(es,clase.getClass(),this.getTextMessage());
            StringBuffer xmlError = eh2.getXmlError();
            request.getSession().setAttribute("xmlError", xmlError);
            request.getRequestDispatcher("/resource/jsp/xmlError.jsp").forward(request, response);
        }        
    }

    /**
     * Se utiliza para ejecutar el despliegue de error cuando se atrapo una
     * excepcion del tipo ExceptionHandler
     * @param eh    ExceptionHandler atrapada
     * @param request
     * @param response
     * @throws Exception
     */
    public void executeErrorHandler(ExceptionHandler eh, HttpServletRequest request, HttpServletResponse response) 
            throws Exception{
        try{
            eh.setRutaFile(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER));
            eh.setLogFile(true);
            eh.writeToFile();
            StringBuffer xmlError = eh.getXmlError();
            request.getSession().setAttribute("xmlError", xmlError);
            request.getRequestDispatcher("/resource/jsp/xmlError.jsp").forward(request, response);
        }catch(ExceptionHandler e){
            
        }
    }

    /**
     * Se utiliza para ejecutar el despliegue de error cuando se atrapo una
     * excepcion del tipo Exception
     * @param es    Exception atrapada
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void executeErrorException(Exception es, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        ExceptionHandler eh2 = new ExceptionHandler(es,this.getClass(),this.getTextMessage());
        StringBuffer xmlError = eh2.getXmlError();
        request.getSession().setAttribute("xmlError", xmlError);
        request.getRequestDispatcher("/resource/jsp/xmlError.jsp").forward(request, response);
    }

    /**
     * Validador de datos de de un formulario. Entrega un List que contiene dos
     * elementos del tipo String, el primero es el resultado de la validacion:
     * a)true: el formulario contiene los datos enviados, b)false: al menos uno
     * de los datos solicitados no viene en el formulario.
     * @param lst  listado de parametros a validar
     * @param form  formulario capturado
     * @return
     */
    public List validationForm(ArrayList lst, HashMap form){
        List sld = new ArrayList();
        String ok = "true";
        StringBuffer str = new StringBuffer();

        if ((!form.isEmpty())&&(!lst.isEmpty())) {
            Iterator it = lst.iterator();
            while(it.hasNext()){
                String st = (String) it.next();
                String res = (String) form.get(st);
                if ((res==null)||("".equals(res))){
                    ok="false";
                    str.append(st).append(",");
                }
            }
            if ("false".equals(ok)){
                str.delete(str.length()-1,str.length());
            }
        }
        sld.add(ok);
        sld.add(str.toString());
        return sld;
    }
}
