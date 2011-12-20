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
package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.util.Validation;

/**
 * Servlet utilizado para la recuperación de password por el usuario
 * @author ccatrilef
 */
public class srvForgotPass extends HttpServlet {
   
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
        HashMap hsForm = null;
        Validation val = new Validation();
        String PagDispacher = "/index.jsp";
        try {
            AdminForm admForm = new AdminForm();
            HashMap hs = admForm.getFormulario(request);
            hsForm = (HashMap) hs.get("FORM");
            String e_mail = (String) hsForm.get("e_mail");

            Bitacora bitacora = new Bitacora(request);
            Perfil perfil = new Perfil();
            perfil.setBitacora(bitacora);

            if ((e_mail!=null) && (!"".equals(e_mail))){
                User user = new User();
                user.setEmail(e_mail);
                perfil.setUser(user);

                AdminXML admXML = new AdminXML();
                if (!perfil.existUser()){
                    //request.getSession().setAttribute("e_mail",e_mail);
                    //request.getSession().setAttribute("msgExist", "No Existe un usuario usando ese mail");
                    //request.getRequestDispatcher(PagDispacher).forward(request, response);
                    request.getSession().setAttribute("xmlTab",
                            admXML.salidaXMLResponse("El correo indicado no está registrado, verifique"));
                    request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                }else{
                    ExecutionHandler exeHn = perfil.enviarPasswordPerdido();

                    user = (User) exeHn.getObjectData();

                    String strFrom = "ILCE";
                    String strTo = e_mail;
                    String strSubject = "Recuperacion de password";
                    StringBuilder strMessage = new StringBuilder("");
                    
                    strMessage.append("Se esta enviado el siguiente mail como solicitud de recuperación");
                    strMessage.append(" recibido en nuestro sistema.\n");
                    strMessage.append("\nUsuario: ");
                    strMessage.append(e_mail);
                    strMessage.append("\nPassword: ");
                    strMessage.append(user.getPassword());
                    strMessage.append("\n\nUtilice la data anterior para poder ingresar a nuestro sitio.");
                    strMessage.append("\n\nAtte\nAdministrador SAEP ILCE");
                    strMessage.append("");

                    cleanMemory(request);

                    request.getSession().setAttribute("from",strFrom);
                    request.getSession().setAttribute("to",strTo);
                    request.getSession().setAttribute("subject",strSubject);
                    request.getSession().setAttribute("message",strMessage.toString());
                    request.getSession().setAttribute("GOTSESS","OK");
                    request.getSession().setAttribute("PagDispacher",PagDispacher);

                    request.getRequestDispatcher("/srvSendMail").forward(request, response);
                }
            }
        } catch (ExceptionHandler eh) {
            AdminXML admXML = new AdminXML();
            request.getSession().setAttribute("xmlTab",
                        admXML.salidaXMLResponse("Se ha producido un error en el envío del correo de recuperación"));
            request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            /*
            if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                LoginHandler lg = new LoginHandler();
                lg.setTextExecution(eh.getTextMessage().toString());
                request.getSession().setAttribute("loginHand",lg);
                cleanMemory(request);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }else{
                try{
                    val.executeErrorHandler(eh,request, response);
                }catch (Exception es){
                    val.setTextMessage("Problemas en la execución del Error de srvForgotPass");
                    val.executeErrorException(es, request, response);
                }
            }*/
        }catch(Exception e){
            AdminXML admXML = new AdminXML();
            request.getSession().setAttribute("xmlTab",
                        admXML.salidaXMLResponse("Se ha producido un error en el envío del correo de recuperación"));
            request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            /*val.setTextMessage("Problemas en la execución de srvForgotPass");
            val.executeErrorException(e, request, response);*/
        } finally {
            out.close();
        }
    }

    /**
     * Método para limpiar de memoria los datos asociados al mail. Nos asegura
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
        return "Servlet utilizado para la recuperación de password por el usuario";
    }

}
