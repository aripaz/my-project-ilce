/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.servlet.forma;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminForm;
import mx.ilce.conection.ConEntidad;
import mx.ilce.controller.Forma;

/**
 *
 * @author vaio
 */
public class srvFormaSearch extends HttpServlet {
   
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
            HashMap hs = admForm.getFormulario(request);

            Forma forma = (Forma) request.getSession().getAttribute("forma");
            if (forma !=null){

                HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
                forma.setFormData(hsForm);
                ArrayList arrayForm = (ArrayList) hs.get("arrayFORM");  //Datos
                forma.setFormName(arrayForm);

                String claveForma = (String) hsForm.get("$cf");
                String pk = (String) hsForm.get("$pk");
                String tipoAccion = (String) hsForm.get("$ta");
                String strWhere = (String) hsForm.get("$w");

                forma.setPk(pk);
                forma.setClaveForma(Integer.valueOf(claveForma));
                forma.setTipoAccion(tipoAccion);

                String[] strData = new String[1];
                strData[0] = pk;
                forma.setArrayData(strData);
                String whereForm = getWhereData(hs, forma.getForma(forma.getClaveForma()));
                if ((strWhere!=null) && (strWhere.trim().length()>0)){
                    if ((whereForm!=null) && (whereForm.trim().length()>0)){
                        forma.setStrWhereQuery(strWhere + " AND " + whereForm);
                    }
                }else{
                    if ((whereForm!=null) && (whereForm.trim().length()>0)){
                        forma.setStrWhereQuery(whereForm);
                    }
                }
                forma.ingresarBusquedaAvanzada();
                StringBuffer xmlForma = forma.getXmlEntidad();
                request.getSession().setAttribute("xmlForma", xmlForma);
            }
            request.getRequestDispatcher("/resource/jsp/xmlForma.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        } finally { 
            out.close();
        }
    } 

    private String getWhereData(HashMap hsDataForm, List lstForma){
        String strSal = new String("");
        int numMaxParam = 10;
        HashMap hsForm = (HashMap) hsDataForm.get("FORM");
        StringBuffer str = new StringBuffer();

        if ((lstForma != null) && (!lstForma.isEmpty())){
            String[][] strCampos = new String[lstForma.size()][2];
            StringBuffer strQuery = new StringBuffer("select ");
            String nameTable ="";
            Iterator it = lstForma.iterator();
            int i=0;
            while (it.hasNext()){
                CampoForma cmp = (CampoForma) it.next();
                String strData = (String) hsForm.get(cmp.getCampo());
                strCampos[i][0]= cmp.getCampo();
                strCampos[i++][1]= strData;
                nameTable = cmp.getTabla();
                strQuery.append(cmp.getCampo()).append(",");
            }
            strQuery.delete(strQuery.length()-1, strQuery.length());
            strQuery.append(" from ").append(nameTable).append(" where 1=2");
            try{
                ConEntidad con = new ConEntidad();
                HashCampo hsCmp = con.getDataByQuery(strQuery.toString(), new String[0]);
                if (hsCmp.getLengthCampo()>0){
                    for (int j=0;j<i;j++){
                        Campo cmp = hsCmp.getCampoByNameDB(strCampos[j][0]);
                        if ( (strCampos[j][1]!=null) && (!"".equals(strCampos[j][1])) )  {
                            if ("java.lang.String".equals(cmp.getTypeDataAPL()))
                            {
                                str.append(strCampos[j][0]).append(" = ").append(("'"+strCampos[j][1]+"'"));
                            }else{
                                str.append(strCampos[j][0]).append(" = ").append(strCampos[j][1]);
                            }
                            str.append(" AND ");
                        }
                    }
                    str.delete(str.length()-4, str.length());
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            strSal = str.toString();
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
