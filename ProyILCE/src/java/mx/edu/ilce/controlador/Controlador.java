/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ilce.modelo.*;
/**
 *
 * @author danielm
 */

public class Controlador extends HttpServlet {
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
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        request.getSession().setAttribute("mensaje",null);
        Usuario usuario=null;
        
            try {
                String cmd = request.getParameter("$cmd");
                if (cmd == null) {
                    out.print("<qry><error><![CDATA[Falta comando, verifique]]></error></qry>");
                    return;
                }

                /* inicia comando login */
                if (cmd.equals("login")) {

                    String aParametros[] = {"lgn", "psw"};
                    sNextAction = validaParametros(request, aParametros);
                    if (sNextAction.equals("")) {
                        Conexion m = new Conexion();
                        int nUsuario = m.getLogin(request.getParameter("lgn"), request.getParameter("psw"));
                        sNextAction = m.getError();
                        if (sNextAction.equals("")) {
                            if (nUsuario != 0) {
                                usuario = new Usuario(nUsuario);
                                request.getSession().setAttribute("usuario", usuario);
                                request.getRequestDispatcher("/vista_.jsp").forward(request, response);
                            } else {
                                request.getSession().setAttribute("mensaje", "Usuario y/o password incorrecto, verifique");
                                request.getRequestDispatcher("/login_.jsp").forward(request, response);
                            }
                        }
                    }
                } else //Verifica que la clave de usuario esté activa
                if (request.getSession().getAttribute("usuario") == null) {
                    request.getRequestDispatcher("/login_.jsp").forward(request, response);
                } else {
                    usuario = (Usuario) request.getSession().getAttribute("usuario");
                }
                
                /* Falta recuperar parametros del request para convertirlos en 
                 * reglas de reemplazo 
                 */
                usuario.setFiltrosForaneos(null);
                
                Enumeration enumReq = request.getParameterNames();
                for (; enumReq.hasMoreElements(); ) {
                    // Get the name of the request parameter
                    String parametro=(String)enumReq.nextElement();
                    String valor = request.getParameter(parametro);
                    usuario.pushFiltrosForaneos(parametro+"="+valor);
                }
                
                if (cmd.equals("appmenu")) { //Carga datos del grid
                    request.getRequestDispatcher("/appmenu.jsp").forward(request, response);
                } else if (cmd.equals("sesion")) {
                    request.getRequestDispatcher("/sesion.jsp").forward(request, response);
                } else if (cmd.equals("form")) { //Carga datos de la forma
                    String aParametros[] = {"$cf", "$ta"};
                    sNextAction = validaParametros(request, aParametros);
                    if (sNextAction.equals("")) {
                        request.getRequestDispatcher("/form.jsp").forward(request, response);
                    } else {
                        out.println(sNextAction);
                    }
                } else if (cmd.equals("grid")) { //Carga datos de la forma
                    String aParametros[] = {"$cf", "$ta"};
                    sNextAction = validaParametros(request, aParametros);
                    if (sNextAction.equals("")) {
                        request.getRequestDispatcher("/grid.jsp").forward(request, response);
                    } else {
                        out.println(sNextAction);
                    }
                }else {
                    request.getSession().setAttribute("mensaje", "Comando no válido");
                    sNextAction = "<qry><error><![CDATA[Comando no válido]]></error></qry>";
                    out.println(sNextAction);
                }
            }catch(Exception e){
             out.println("<qry><error><![CDATA[" + e.getMessage() + "</error></error></qry>");

        } finally { 
            /*out.close();*/
        }
    } 

    String validaParametros(HttpServletRequest request, String args[]) {
    String s="";
    int i;
    for (i=0; i<args.length; i++) {
            if (request.getParameter(args[i])==null) {
                s="<qry><error><![CDATA[Falta el parametro " + args[i] + ", verifique]]></error></qry>";
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
