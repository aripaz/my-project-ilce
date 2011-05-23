/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.servlet.grid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.component.AdminForm;
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Forma;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.Validation;

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
        HashMap hsForm = null;
        Validation val = new Validation();
        try {
            AdminForm admForm = new AdminForm();
            HashMap hs = admForm.getFormulario(request);

            hsForm = (HashMap) hs.get("FORM");  //Datos

            String claveForma = (String) hsForm.get("$cf");
            String dp = (String) hsForm.get("$dp");
            String tipoAccion = "select";
            String strWhere = (String) hsForm.get("$w");

            ArrayList arrVal = new ArrayList();
            arrVal.add("$cf");
            arrVal.add("$dp");

            List lstVal = val.validationForm(arrVal, hsForm);
            String blOK = (String) lstVal.get(0);
            if ("false".equals(blOK)){
                    val.executeErrorValidation(lstVal, this.getClass(), request, response);
            }else{
                String[] strData = getArrayData(hsForm);

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
                    String numPage = (String) hsForm.get("numPage"); // admForm.getStringRequest("numPage",request);
                    String numRows = (String) hsForm.get("numRows"); //admForm.getStringRequest("numRows",request);
                    apl.setNumPage(numPage);
                    apl.setNumRows(numRows);
                    StringBuffer xmlForma = apl.getXmlEntidad();

                    request.getSession().setAttribute("xmlGrid", xmlForma);
                }
                request.getRequestDispatcher("/resource/jsp/xmlGrid.jsp").forward(request, response);
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvGrid");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvGrid");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    } 

    private String[] getArrayData(HashMap hsForm){
        String[] strSal = null;
        int numMaxParam = 10;

        ArrayList lst = new ArrayList();
        boolean seguir = true;
        for (int i=1;i<numMaxParam&&seguir;i++){
            String strData = (String) hsForm.get("$"+i);//admForm.getStringRequest("$"+i,request);
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
