/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.servlet.grid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.component.AdminForm;
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Forma;

/**
 *  Servlet encargado de cargar los datos de la grilla
 * @author ccatrilef
 */
public class srvGrid extends HttpServlet {
   
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
            AdminForm admForm = new AdminForm();
/*
            String claveForma = admForm.getStringRequest("$cf",request);
            String dp = admForm.getStringRequest("$dp",request);
            String tipoAccion = "select";
            String strWhere = admForm.getStringRequest("$w",request);
            String[] strData = getArrayData(admForm, request);
*/

            HashMap hs = admForm.getFormulario(request);

            HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
            HashMap hsFile = (HashMap) hs.get("FILE");  //Archivos
            HashMap hsFormQuery = new HashMap();

            String claveForma = (String) hsForm.get("$cf");
            String dp = (String) hsForm.get("$dp");
            String tipoAccion = "select";
            String strWhere = (String) hsForm.get("$w");
            String[] strData = getArrayData(admForm, request);

            Forma forma = (Forma) request.getSession().getAttribute("forma");
            Aplicacion apl = new Aplicacion();
            if ((forma !=null)&&(apl!=null)){
                apl.addForma(Integer.valueOf(claveForma),forma.getForma(Integer.valueOf(claveForma)));
                apl.setDisplay(dp);
                apl.setClaveForma(Integer.valueOf(claveForma));
                apl.setTipoAccion(tipoAccion);
                apl.setStrWhereQuery(strWhere);
                apl.setArrayData(strData);
                apl.mostrarForma();
                String numPage = admForm.getStringRequest("numPage",request);
                String numRows = admForm.getStringRequest("numRows",request);
                apl.setNumPage(numPage);
                apl.setNumRows(numRows);
                StringBuffer xmlForma = apl.getXmlEntidad();
                
                request.getSession().setAttribute("xmlGrid", xmlForma);
            }
            request.getRequestDispatcher("/resource/jsp/xmlGrid.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            out.close();
        }
    } 

    private String[] getArrayData(AdminForm admForm, HttpServletRequest request){
        String[] strSal = null;
        int numMaxParam = 10;

        ArrayList lst = new ArrayList();
        boolean seguir = true;
        for (int i=1;i<numMaxParam&&seguir;i++){
            String strData = admForm.getStringRequest("$"+i,request);
            if (strData!=null){
                lst.add(strData);
            }else{
                seguir = false;
            }
        }
        if (!lst.isEmpty()){
            strSal = new String[lst.size()];
            for (int i=0;i<lst.size();i++){
                strSal[i] = (String) lst.get(i);
            }
        }
        return strSal;
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
