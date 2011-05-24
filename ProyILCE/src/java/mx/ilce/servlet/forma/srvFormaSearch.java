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
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.Validation;

/**
 *  Servlet implementado para hacer busqueda general de datos
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
        Validation val = new Validation();
        try {
            AdminForm admForm = new AdminForm();
            HashMap hs = admForm.getFormulario(request);
            HashMap hsForm = (HashMap) hs.get("FORM");  //Datos

            String claveForma = (String) hsForm.get("$cf");
            String pk = (String) hsForm.get("$pk");
            String tipoAccion = (String) hsForm.get("$ta");
            String strWhere = (String) hsForm.get("$w");

            ArrayList arrVal = new ArrayList();
            arrVal.add("$cf");
            arrVal.add("$ta");

            List lstVal = val.validationForm(arrVal, hsForm);
            String blOK = (String) lstVal.get(0);
            if ("false".equals(blOK)){
                    val.executeErrorValidation(lstVal, this.getClass(), request, response);
            }else{
                Forma forma = new Forma();//(Forma) request.getSession().getAttribute("forma");
                if (forma !=null){
                    forma.setFormData(hsForm);
                    ArrayList arrayForm = (ArrayList) hs.get("arrayFORM");  //Datos
                    forma.setFormName(arrayForm);
                    forma.setPk(pk);
                    forma.setClaveForma(Integer.valueOf(claveForma));
                    forma.setTipoAccion(tipoAccion);

                    String[] strData = getArrayData(hsForm);
                    forma.setArrayData(strData);
                    String whereForm = getWhereData(hs, forma.getForma(forma.getClaveForma()));
                    if ((strWhere!=null) && (strWhere.trim().length()>0)){
                        if ((whereForm!=null) && (whereForm.trim().length()>0)){
                            forma.setStrWhereQuery(strWhere + " AND " + whereForm);
                        }else{
                            forma.setStrWhereQuery(strWhere);
                        }
                    }else{
                        if ((whereForm!=null) && (whereForm.trim().length()>0)){
                            forma.setStrWhereQuery(whereForm);
                        }
                    }
                    forma.ingresarBusquedaAvanzada();
                    StringBuffer xmlForma = forma.getXmlEntidad();
                    request.getSession().removeAttribute("xmlForma");
                    request.getSession().setAttribute("xmlForma", xmlForma);
                }
                request.getRequestDispatcher("/resource/jsp/xmlForma.jsp").forward(request, response);
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvFormaSearch");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvFormaSearch");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    } 

    private String getWhereData(HashMap hsDataForm, List lstForma) throws ExceptionHandler{
        String strSal = new String("");
        try{
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
                    if (str.length()>=4){
                        if (str.substring(str.length()-4, str.length()).trim().equals("AND")) {
                            str.delete(str.length()-4, str.length());
                        }
                    }
                }
                strSal = str.toString();
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para ejecutar la QUERY de la forma con el WHERE");
        }
        return strSal;
    }

    private String[] getArrayData(HashMap hsForm){
        String[] strSld = new String[0];

        if (!hsForm.isEmpty()){
            String[] arr = new String[hsForm.size()];
            int j=0;
            for (int i=0;i<hsForm.size();i++){
                String dato = (String) hsForm.get("$"+i);
                if ((dato!=null)&&(!"".equals(dato))){
                    arr[j]=dato;
                    j++;
                }
            }
            if (j>0){
                strSld = new String[j];
            }
            System.arraycopy(arr, 0, strSld, 0, j);
        }
        return strSld;
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
