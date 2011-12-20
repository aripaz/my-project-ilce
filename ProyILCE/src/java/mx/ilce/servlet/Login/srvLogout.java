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
 * Servlet implementado para efectuar el logout del usuario
 * @author ccatrilef
 */
public class srvLogout extends HttpServlet {
   
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

            request.getSession().removeAttribute("xmlTab");
            request.getSession().removeAttribute("from");
            request.getSession().removeAttribute("to");
            request.getSession().removeAttribute("subject");
            request.getSession().removeAttribute("message");
            request.getSession().removeAttribute("copy");
            request.getSession().removeAttribute("copyO");
            request.getSession().removeAttribute("GOTSESS");
            request.getSession().removeAttribute("e_mail");
            request.getSession().removeAttribute("msgExist");
            request.getSession().removeAttribute("MAILPK");
            request.getSession().removeAttribute("MAILBITAC");

            LoginHandler lg = new LoginHandler();
            lg.setTextExecution("Sesión finalizada");
            request.getSession().setAttribute("loginHand",lg);

            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (ExceptionHandler ex) {
            Logger.getLogger(srvLogout.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
            out.close();
        }
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
        return "Servlet para ser invocado para efectuar el logout del usuario";
    }
}
