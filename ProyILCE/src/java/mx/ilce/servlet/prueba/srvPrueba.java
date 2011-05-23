/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.servlet.prueba;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConSession;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.Validation;

/**
 *
 * @author vaio
 */
public class srvPrueba extends HttpServlet {
   
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
            ConSession con = new ConSession();

            User user = new User();
                user.setLogin("dmartine@ilce.edu.mx");
                user.setPassword("prueba");
            user = con.getUser(user);

            Perfil perfil = con.getPerfil(user);
            List lst = perfil.getLstAplicacion();

            Forma forma = new Forma();
            forma.getFormasByAplications(lst);

            AdminXML adm = new AdminXML();
            StringBuffer strSession = adm.getSessionXML(user);
            StringBuffer strMenu = adm.getMenuXML(user);

            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("perfil", perfil);
            request.getSession().setAttribute("xmlSession", strSession);
            request.getSession().setAttribute("xmlMenu", strMenu);
            //request.getRequestDispatcher("/resource/jsp/xmlSession.jsp").forward(request, response);

            AdminForm admF = new AdminForm();
            HashMap hs = admF.getFormulario(request);
            HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
            HashMap hsFile = (HashMap) hs.get("FILE");  //ARCHIVOS
// PRUEBA PARA OBTENER EL TABXML - GRIDXML - FORMAXML
/*
            StringBuffer strTab = adm.getTabXML(perfil);

            String[] strData = new String[1];
            HashCampo hsCmp = con.getDataByIdQuery(Integer.valueOf(10), strData);

            List lstF = (List)forma.getForma(2);
            StringBuffer strGrid = adm.getGridByData(hsCmp,lstF,1,10);
            strGrid.append("");
            hsCmp = con.getDataByIdQuery(Integer.valueOf(13), strData);
            StringBuffer strForma = adm.getFormaByData(hsCmp, lstF,2);
            strForma.append("");

            request.getSession().setAttribute("forma", forma);

            request.getRequestDispatcher("/resource/jsp/xmlSession.jsp").forward(request, response);
*/
// FIN - PRUEBA

// PRUEBA DE VALIDACION DE ENVIO DE DATOS
            forma.setFormData(hsForm);
            ArrayList arrayForm = (ArrayList) hs.get("arrayFORM");  //Datos
            forma.setFormName(arrayForm);

            String claveForma = (String) hsForm.get("$cf");
            String pk = (String) hsForm.get("$pk");
            String tipoAccion = (String) hsForm.get("$ta");
            String strWhere = (String) hsForm.get("$w");

            ArrayList arrVal = new ArrayList();
            arrVal.add("$cf");
            arrVal.add("$pk");
            arrVal.add("$ta");

            List lstVal = val.validationForm(arrVal, hsForm);
            String blOK = (String) lstVal.get(0);
            if ("false".equals(blOK)){
                    val.executeErrorValidation(lstVal, this.getClass(), request, response);
            }else{
                request.getRequestDispatcher("/resource/jsp/xmlGrid.jsp").forward(request, response);
            }

// FIN - PRUEBA

//  PRUEBA DE INSERT-UPDATE-DELETE DE FORMA
/*
            String strQuery = (String) hsForm.get("$query");

            ConEntidad conE = new ConEntidad();
            //String strQuery = "insert into CTR_PRB (strData) values ('una prueba')";
            //String strQuery = "update CTR_PRB set strData = 'update prueba' where idClave = 14";

            conE.setQuery(strQuery);

            CampoForma cmpForma = new CampoForma();
            cmpForma.setTabla("CTR_PRB");
            conE.setCampoForma(cmpForma);
            //conE.editarEntidad();
            conE.ingresaEntidad();

            HashCampo hsCmp = conE.getHashCampo();
            Integer xml = (Integer) hsCmp.getObjData();
            request.getSession().setAttribute("xmlTab", String.valueOf(xml));
            request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
*/

// FIN - PRUEBA DE INSERT-UPDATE-DELETE DE FORMA

//  PRUEBA DE MANEJO DE EXCEPCI0NES
/*
            String query = (String) hsForm.get("$query");
            String s1 = (String) hsForm.get("$1");
            String s2 = (String) hsForm.get("$2");

            clsMedio med = new clsMedio();

            String[] strData = new String[2];
            strData[0]=s1;
            strData[1]=s2;
            StringBuffer sld = med.prueba(query, strData);

            request.getSession().setAttribute("xmlGrid", sld);
            request.getRequestDispatcher("/resource/jsp/xmlGrid.jsp").forward(request, response);
 * 
 */
//  FIN DE PRUEBA DE MANEJO DE EXCEPCIONES
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvForma");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvForma");
            val.executeErrorException(e, request, response);
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
