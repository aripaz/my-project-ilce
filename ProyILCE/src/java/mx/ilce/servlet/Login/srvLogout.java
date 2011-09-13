package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LoginHandler;

/**
 *  Servlet para ser invocado pra efectuar el logout del usuario
 * @author ccatrilef
 */
public class srvLogout extends HttpServlet {
   
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
        try {
            User user = (User) request.getSession().getAttribute("user");

            Bitacora bitacora = user.getBitacora();
            bitacora.setBitacora("Logout del usuario");
            bitacora.setEnable(true);

            Perfil perfil = new Perfil();
            perfil.setUser(user);
            perfil.setBitacora(bitacora);

            perfil.cerrarSession();

            request.getSession().removeAttribute("user");
            request.getSession().removeAttribute("forma");
            request.getSession().removeAttribute("perfil");
            request.getSession().removeAttribute("xmlSession");
            request.getSession().removeAttribute("xmlMenu");
            LoginHandler lg = new LoginHandler();
            lg.setTextExecution("Session Cerrada");
            request.getSession().setAttribute("loginHand",lg);

            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (ExceptionHandler ex) {
            Logger.getLogger(srvLogout.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
            out.close();
        }
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
