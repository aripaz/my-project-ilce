package mx.ilce.servlet.forma;

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
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
import mx.ilce.handler.SpyHandler;
import mx.ilce.util.Validation;

/**
 *  Servelt implementado para manejar el borrado de datos
 * @author ccatrilef
 */
public class srvFormaDelete extends HttpServlet {
   
    private String[][] arrVariables = null;

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
        SpyHandler spy = new SpyHandler();
        Validation val = new Validation();
        try {
            if (!val.validateUser(request)){
                val.executeErrorValidationUser(this.getClass(), request, response);
            }else{
                //Obtenemos los datos del formulario
                AdminForm admForm = new AdminForm();
                HashMap hs = admForm.getFormulario(request);

                HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
                HashMap hsFormQuery = new HashMap();
                spy.setHsForm(hsForm);

                String claveForma = (String) hsForm.get("$cf");
                String claveAplic = (String) hsForm.get("$ca");

                String pk = (String) hsForm.get("$pk");
                String strWhere = (String) hsForm.get("$w");
                String tipoAccion = "delete";

                ArrayList arrVal = new ArrayList();
                arrVal.add("$cf");

                User user = (User) request.getSession().getAttribute("user");
                arrVariables = admForm.getVariablesFromProperties(hsForm);
                arrVariables = admForm.getVariableByObject(user, arrVariables);
                arrVariables = admForm.cleanVariables(arrVariables);

                if ((pk==null)&&(strWhere==null)){
                    arrVal.add("$pk");
                    arrVal.add("$w");
                }
                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    User usr = (User) request.getSession().getAttribute("user");
                    Bitacora bitacora = usr.getBitacora();
                    bitacora.setClaveForma(Integer.valueOf(claveForma));
                    if (claveAplic!=null){
                        bitacora.setClaveAplicacion(Integer.valueOf(claveAplic));
                    }
                    if (pk!=null){
                        bitacora.setClaveRegistro(Integer.valueOf(pk));
                    }

                    Forma forma = (Forma) request.getSession().getAttribute("forma");
                    forma.setPk(pk);
                    forma.setClaveForma(Integer.valueOf(claveForma));
                    forma.setTipoAccion(tipoAccion);
                    forma.setStrWhereQuery(strWhere);
                    forma.setClavePerfil(usr.getClavePerfil());
                    forma.setArrVariables(arrVariables);

                    ExecutionHandler ex = new ExecutionHandler();
                    List lstData = new ArrayList();

                    lstData.add(hsFormQuery);
                    lstData.add(forma);
                    bitacora.setBitacora("Eliminar dato.");
                    forma.setBitacora(bitacora);
                    ex = forma.eliminarEntidad(lstData);
                    if ("0".equals(forma.getPk())){
                        forma.setPk("1");
                    }
                    if (!ex.isExecutionOK()){
                        ex.setObjectData(Integer.valueOf(0));
                    }
                    
                    actualizarData(request);

                    Integer xml = (Integer) ((ex.getObjectData()==null)?Integer.valueOf(forma.getPk()):ex.getObjectData());
                    AdminXML admXML = new AdminXML();
                    //request.getSession().setAttribute("xmlTab", admXML.salidaXML(String.valueOf(xml)));
                    request.getSession().setAttribute("xmlTab", admXML.salidaXMLBitacora(String.valueOf(xml),
                                                                String.valueOf(forma.getBitacora().getIdBitacora())));
                    spy.setXmlSld(new StringBuffer((String) request.getSession().getAttribute("xmlTab")));
                    request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvDelete");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvDelete");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
            spy.SpyData("svrFormaDelete");
        }
    }

    /**
     * Actualiza a memoria los datos del usuario
     * @param request
     */
    private void actualizarData(HttpServletRequest request) throws ExceptionHandler{
        User user = (User) request.getSession().getAttribute("user");
        Perfil perfil = new Perfil();
        perfil.setBitacora(user.getBitacora());
        perfil.getBitacora().setEnable(false);

        LoginHandler lg = perfil.login(user);
        if (lg.isLogin()) {
            user = (User) lg.getObjectData();
            if (user != null) {
                List lst = perfil.getLstAplicacion();
                Forma forma = new Forma();
                forma.getFormasByAplications(lst);
                request.getSession().setAttribute("perfil", perfil);
                request.getSession().setAttribute("forma", forma);
            }
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
