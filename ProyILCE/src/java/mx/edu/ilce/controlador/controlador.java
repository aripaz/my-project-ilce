/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ilce.modelo.*;
/**
 *
 * @author danielm
 */

public class controlador extends HttpServlet {
String sNextAction="";
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
        String cmd=request.getParameter("$cmd");
        if (cmd==null) {
            sNextAction="<error><![CDATA[Falta comando, verifique]]></error>";
        }

        /* inicia comando login */
        if (cmd.equals("login")) {
                String aParametros[]={"email","password","clave_aplicacion"};
                sNextAction=valida_parametros(request, aParametros);
                if (sNextAction.equals("")) {
                ConnDef m = new ConnDef();
                int nUsuario=m.getLogin(request.getParameter("email"),request.getParameter("password"));
                    sNextAction= m.getError();
                    if (sNextAction.equals("")) {
                        if (nUsuario!=0) {

                            request.getSession().setAttribute("clave_usuario", nUsuario);
                            request.getRequestDispatcher("/vista.jsp").forward(request, response);
                             }
                        else {
                            request.getSession().setAttribute("mensaje","Usuario y/o password incorrecto, verifique");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        }
                  }
              }
         }
        else
            //Verifica que la clave de usuario esté activa
            if (request.getSession().getAttribute("clave_usuario")==null)
                request.getRequestDispatcher("/login.jsp").forward(request, response);

            if (cmd.equals("menu")) { //Carga datos de menu

            }
            else if (cmd.equals("sesion")) { //Carga datos de sesion

            }
            else if (cmd.equals("")) { //Carga datos de sesion

            }

            else {
                sNextAction="<error><![CDATA[Comando no válido]]></error>";
        }
        /* Termina comando login */

        out.print("<cmd type=" + cmd + ">");
        out.print(sNextAction);

        } finally { 
            out.close();
        }
    } 

    String valida_parametros(HttpServletRequest request, String args[]) {
    String s="";
    int i;
    for (i=0; i<args.length; i++) {
            if (request.getParameter(args[i])==null) {
                s="<error><![CDATA[Falta el parametro " + args[i] + ", verifique]]></error>";
                break;
        }
    }
    return s;
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
