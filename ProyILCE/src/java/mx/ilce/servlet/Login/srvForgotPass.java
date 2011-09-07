/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.util.Validation;

/**
 *  Servlet utilizado para la recuperacion de password por el usuario
 * @author ccatrilef
 */
public class srvForgotPass extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
        String PagDispacher = "/forgotPassResp.jsp";
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

                if (!perfil.existUser()){
                    request.getSession().setAttribute("e_mail",e_mail);
                    request.getSession().setAttribute("msgExist", "No Existe un usuario usando ese mail");
                    request.getRequestDispatcher("/forgotPass.jsp").forward(request, response);
                }else{
                    ExecutionHandler exeHn = perfil.enviarPasswordPerdido();

                    user = (User) exeHn.getObjectData();

                    String strFrom = "ILCE";
                    String strTo = e_mail;
                    String strSubject = "Recuperacion de password";
                    StringBuilder strMessage = new StringBuilder("");
                    
                    strMessage.append("Se esta enviado el siguiente mail como solicitud de recuperacion");
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
            if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                request.getSession().setAttribute("xmlTab",eh.getXmlError());
                request.getRequestDispatcher(PagDispacher).forward(request, response);
            }else{
                try{
                    val.executeErrorHandler(eh,request, response);
                }catch (Exception es){
                    val.setTextMessage("Problemas en la execucion del Error de srvForgotPass");
                    val.executeErrorException(es, request, response);
                }
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvForgotPass");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    }

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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
