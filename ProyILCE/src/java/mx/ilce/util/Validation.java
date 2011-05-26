package mx.ilce.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.component.AdminFile;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;

/**
 *  Clase implementada para contener un conjunto de metodos de validacion
 * @author ccatrilef
 */
public class Validation {

    private String textMessage;

    /**
     * Obtiene un texto de mensaje
     * @return
     */
    public String getTextMessage() {
        return textMessage;
    }

    /**
     * Asigna un texto de mensaje
     * @param textMessage   Texto del mesnaje
     */
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
     * Metodo para el reemplazo de comillas(') a doble comilla('') en los datos
     * que se entregan a una query
     * @param strData
     * @return
     */
    public String replaceComillas(String strData){
        String sld = strData;
        if ((strData!=null)&&(!"".equals(strData))){
            sld = strData.replaceAll("'", "''");
        }
        return sld;
    }

    /**
     * Metodo para ejecutar el despliegue de error para cuando se valido que
     * un formulario no esta completo en la data exigida
     * @param lstVal    Data enviada
     * @param clase     Clase donde se produjo el error
     * @param request   Request de la session HTML
     * @param response  Response de la session HTML
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
     * Metodo para ejecutar el despliegue de error cuando no se obtiene data de la 
     * consulta enviada
     * @param dataVal   Data enviada
     * @param clase     Clase donde se produjo el error
     * @param message   Mensaje enviado
     * @param request   Request de la session HTML
     * @param response  Response de la session HTML
     * @throws ServletException
     * @throws IOException
     */
    public void executeErrorEmptyData(String dataVal, Class clase, String message) throws ExceptionHandler {
        try{
            ExceptionHandler eh = new ExceptionHandler(dataVal,
                    clase.getClass(),
                    "Error de Respuesta Vacia",message);
            eh.setRutaFile(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER));
            eh.setLogFile(true);
            eh.writeToFile();
            StringBuffer xmlError = eh.getXmlError();
            throw eh;
        }catch (Exception es){
            ExceptionHandler eh2 = new ExceptionHandler(es,clase.getClass(),this.getTextMessage());
            StringBuffer xmlError = eh2.getXmlError();
            throw eh2;
        }
    }

    /**
     * Se utiliza para ejecutar el despliegue de error cuando se atrapo una
     * excepcion del tipo ExceptionHandler
     * @param eh    ExceptionHandler atrapada
     * @param request   Request de la session HTML
     * @param response  Response de la session HTML
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
     * @param request   Request de la session HTML
     * @param response  Response de la session HTML
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

    /**
     * Valida que esten en memoria el user y el perfil del usuario conectado
     * @param request   Request de la session HTML
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean validateUser(HttpServletRequest request)
            throws ServletException, IOException{
        boolean sld = true;
        try{
            Perfil perfil = (Perfil) request.getSession().getAttribute("perfil");
            User user = (User) request.getSession().getAttribute("user");
            if ((perfil==null)||(user==null)){
                sld=false;
            }
        }catch(Exception e){
            sld=false;
        }
        return sld;
    }

    /**
     * Se utiliza para ejecutar el despliegue de error para cuando se valido que
     * un usuario no esta validamente conectado.
     * @param clase     Clase donde se produjo el error
     * @param request   Request de la session HTML
     * @param response  Response de la session HTML
     * @throws ServletException
     * @throws IOException
     */
    public void executeErrorValidationUser(Class clase, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String dataVal = "";
            ExceptionHandler eh = new ExceptionHandler(dataVal,
                    clase.getClass(),
                    "Error de Datos de Usuario","No se encontro en Session los datos del Usuario");
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
}
