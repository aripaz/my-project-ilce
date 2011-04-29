/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.servlet.forma;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.controller.Forma;

/**
 *
 * @author vaio
 */
public class srvForma extends HttpServlet {

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
            String claveForma = (String) request.getParameter("$cf");
            String pk = (String) request.getParameter("$pk");
            String tipoAccion = (String) request.getParameter("$ta");
            if (claveForma==null){
                claveForma = (String) request.getSession().getAttribute("$cf");
            }
            if (pk==null){
                pk = (String) request.getSession().getAttribute("$pk");
            }
            if (tipoAccion==null){
                tipoAccion = (String) request.getSession().getAttribute("$ta");
            }
            Forma forma = (Forma) request.getSession().getAttribute("forma");

            if (forma !=null){
                forma.setPk(pk);
                forma.setClaveForma(Integer.valueOf(claveForma));
                forma.setTipoAccion(tipoAccion);
                forma.mostrarForma();
                StringBuffer xmlForma = forma.getXmlEntidad();
                request.getSession().setAttribute("xmlForma", xmlForma);
            }
            request.getRequestDispatcher("/resource/jsp/xmlForma.jsp").forward(request, response);
        }catch (Exception e){
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