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
package mx.ilce.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.component.AdminForm;
import mx.ilce.handler.ExceptionHandler;

/**
 * Servlet implementado para poder acceder a la selección de elementos
 * @author ccatrilef
 */
public class SrvElement extends HttpServlet {
   
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
        try {
            AdminForm admForm = new AdminForm();
            HashMap hs = admForm.getFormulario(request);
            hsForm = (HashMap) hs.get("FORM");  //Datos
            String strOper = (String) hsForm.get("oper");
            if ("genReport".equals(strOper)){
                User user = (User) request.getSession().getAttribute("user");
                Integer idUser = 0;
                if (user!=null){
                    idUser = user.getClaveEmpleado();
                }

                AdmReport admReport = new AdmReport();
                
                String nombreRPT = (String) hsForm.get("nombreRPT");
                List lst = getIDfromForm(hsForm);
                if ((nombreRPT!=null) && (!"".equals(nombreRPT))
                    && (lst!=null) && (!lst.isEmpty())){
                    
                    GenReport gen = new GenReport();
                    Report report = new Report();
                    report.setReport(nombreRPT);

                    gen.setReport(report);
                    gen.setLstElementReport(lst);
                    gen.setIdUser(idUser);
                    gen.genReportWithElement();

                    request.getSession().setAttribute("xmlTab",
                            admReport.salidaXML(String.valueOf("REPORTE GENERADO"),gen.getRutaReport()));
                }else{
                    request.getSession().setAttribute("xmlTab",
                            admReport.salidaXML(String.valueOf("NO EXISTEN DATOS PARA EL REPORTE"),""));
                }
                request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            }else{
                AdmReport admReport = new AdmReport();
                List listElement = admReport.getElementToSelect();
                
                request.getSession().setAttribute("listElement", listElement);
                request.getRequestDispatcher("/jspPruebaElement.jsp").forward(request, response);
            }
        } catch (ExceptionHandler ex) {
                Logger.getLogger(SrvReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    private List getIDfromForm(HashMap hsForm){
        List lst = new ArrayList();

        if (hsForm!=null){
            Collection col = hsForm.values();
            if (col!=null){
                Iterator it = col.iterator();
                while (it.hasNext()){
                    String valor = (String) it.next();
                    if (hsForm.containsKey("ID"+valor)){
                        try{
                            Integer intValor = Integer.valueOf(valor);
                            lst.add(intValor);
                        }catch (Exception e){}
                    }
                }
            }
        }
        return lst;
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
