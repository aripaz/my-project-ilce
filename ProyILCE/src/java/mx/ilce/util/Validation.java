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
 * Clase implementada para contener un conjunto de métodos de validación
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
     * Método para validar si un dato es Par o no
     * @param data
     * @return
     */
    public static boolean isPar(String data){
        int i =  Integer.parseInt(data) ;
        int j = i%2;
        return ((j==0)?true:false);
    }

    /**
     * Método para el reemplazo de comillas(') a doble comilla('') en los datos
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
     * Método para ejecutar el despliegue de error para cuando se validó que
     * un formulario no está completo en la data exigida
     * @param lstVal    Data enviada
     * @param clase     Clase donde se produjo el error
     * @param request   Request de la Session HTML
     * @param response  Response de la Session HTML
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
     * Método para ejecutar el despliegue de error cuando no se obtiene data de la
     * consulta enviada
     * @param dataVal   Data enviada
     * @param clase     Clase donde se produjo el error
     * @param message   Mensaje enviado
     * @param request   Request de la Session HTML
     * @param response  Response de la Session HTML
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
     * Método utilizado para ejecutar el despliegue de error cuando se atrapo una
     * excepción del tipo ExceptionHandler
     * @param eh    ExceptionHandler atrapada
     * @param request   Request de la Session HTML
     * @param response  Response de la Session HTML
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
     * Método utilizado para ejecutar el despliegue de error cuando se atrapo una
     * excepción del tipo Exception
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
     * elementos del tipo String, el primero es el resultado de la validación:
     * a)TRUE: el formulario contiene los datos enviados, b)FALSE: al menos uno
     * de los datos solicitados no viene en el formulario.
     * @param lst  Listado de parámetros a validar
     * @param form  Formulario capturado
     * @return
     */
    public List validationForm(ArrayList lst, HashMap form){
        List sld = new ArrayList();
        String ok = "true";
        StringBuilder str = new StringBuilder();

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
     * Método que valida que estén en memoria el User y el Perfil del usuario conectado
     * @param request   Request de la Session HTML
     * @return  boolean     Validador
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
     * Método que se utiliza para ejecutar el despliegue de error para cuando
     * se validó que un usuario no está correctamente conectado.
     * @param clase     Clase donde se produjo el error
     * @param request   Request de la Session HTML
     * @param response  Response de la Session HTML
     * @throws ServletException
     * @throws IOException
     */
    public void executeErrorValidationUser(Class clase, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String dataVal = "";
            ExceptionHandler eh = new ExceptionHandler(dataVal,
                    clase.getClass(),
                    "Error de Datos de Usuario","No se encontro en el objeto Session los datos del Usuario");
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
     * Método para validar si el datos entregado es numérico o no
     * @param strDato   Dato a validar
     * @return boolean  Respuesta TRUE o FALSE, según corresponda
     */
    public boolean isNumberInteger(String strDato){
        boolean sld = true;
        try{
            Integer integ = new Integer(strDato);
            if (integ==null){
                sld = false;
            }
        }catch(Exception e){
            sld = false;
        }
        return sld;
    }

    /**
     * Método para validar si el datos entregado es numérico o no
     * @param strDato   Dato a validar
     * @return boolean  Respuesta TRUE o FALSE, según corresponda
     */
    public boolean isNumberDouble(String strDato){
        boolean sld = true;
        try{
            Double dbl = new Double(strDato);
            if (dbl==null){
                sld = false;
            }
        }catch(Exception e){
            sld = false;
        }
        return sld;
    }
}
