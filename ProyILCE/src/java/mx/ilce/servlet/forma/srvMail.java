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
package mx.ilce.servlet.forma;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminForm;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.mail.AdmMail;
import mx.ilce.mail.DataMail;
import mx.ilce.util.Validation;

/**
 * Servlet utilizado para recibir las peticiones de generar mail a uno o más
 * usuarios
 * @author ccatrilef
 */
public class srvMail extends HttpServlet {

    private String[][] arrVariables = null;

    /** 
     * Procesa los requerimientos HTTP de tipo GET y POST, al recibir las
     * llamadas de los métodos doGet() y doPost()
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Validation val = new Validation();
        try {
            if (!val.validateUser(request)){
                val.executeErrorValidationUser(this.getClass(), request, response);
            }else{
                AdminForm admForm = new AdminForm();
                HashMap hs = admForm.getFormulario(request);
                HashMap hsForm = (HashMap) hs.get("FORM");  //Datos

                String maillist = (String) hsForm.get("maillist");
                String strSubject = (String) hsForm.get("subject");
                String strMessage = (String) hsForm.get("message");

                if ((strSubject==null)||"".equals(strSubject)){
                    strSubject = "algo";
                }
                if ((strMessage==null)||"".equals(strMessage)){
                    strMessage = "algun mensaje";
                }

                User user = (User) request.getSession().getAttribute("user");
                arrVariables = admForm.getVariablesFromProperties(hsForm);
                arrVariables = admForm.getVariableByObject(user, arrVariables);
                arrVariables = admForm.cleanVariables(arrVariables);

                Bitacora bitacora = user.getBitacora();
                bitacora.setEnable(false);

                ArrayList arrVal = new ArrayList();
                arrVal.add("maillist");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    cleanMemory(request);

                    AdmMail adm = new AdmMail();
                    adm.setBitacora(bitacora);

                    DataMail dataMail = new DataMail();
                    dataMail.setClaveForma(Integer.valueOf(maillist));

                    HashMap hm = adm.getReceptorMail(dataMail);

                    String strFrom = user.getEmail();
                    String strTo = (String) hm.get("TO");
                    String strCopy = user.getEmail();
                    String strCopyO = (String) hm.get("COPYO");

                    request.getSession().setAttribute("from",strFrom);
                    request.getSession().setAttribute("to",strTo);
                    request.getSession().setAttribute("copy",strCopy);
                    request.getSession().setAttribute("copyO",strCopyO);
                    request.getSession().setAttribute("subject",strSubject);
                    request.getSession().setAttribute("message",strMessage);
                    request.getSession().setAttribute("GOTSESS","OK");

                    request.getRequestDispatcher("/srvSendMail").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execución del Error de srvMail");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execución de srvMail");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    }

    /**
     * Limpia de memoria los datos utilizados para el envío de mail. Nos asegura
     * que ante una nueva invocación no se tomen datos de un mail anterior.
     * @param request
     */
    private void cleanMemory(HttpServletRequest request){
        request.getSession().removeAttribute("from");
        request.getSession().removeAttribute("to");
        request.getSession().removeAttribute("subject");
        request.getSession().removeAttribute("message");
        request.getSession().removeAttribute("copy");
        request.getSession().removeAttribute("copyO");
        request.getSession().removeAttribute("GOTSESS");
        request.getSession().removeAttribute("e_mail");
        request.getSession().removeAttribute("msgExist");
    }

    /** 
     * Maneja los requerimientos HTTP del tipo GET
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Maneja los requerimientos HTTP del tipo POST
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Entrega una corta descripción del Servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet utilizado para recibir las peticiones de generar mail";
    }

}
