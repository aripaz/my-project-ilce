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
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
import mx.ilce.util.Validation;

/**
 * Servlet implementado para insertar los datos obtenidos de un formulario en
 * la Base de datos, asociado a la forma que le corresponda, segun el perfil
 * del usuario conectado. Utilizada en la creacion de datos nuevos.
 * Se modifico para tambien aceptar la modificacion de datos
 * @author ccatrilef
 */
public class srvFormaInsert extends HttpServlet {

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
        Validation val = new Validation();
        try {
            if (!val.validateUser(request)){
                val.executeErrorValidationUser(this.getClass(), request, response);
            }else{
                //Obtenemos los datos del formulario
                AdminForm admF = new AdminForm();
                HashMap hs = admF.getFormulario(request);

                HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
                HashMap hsFile = (HashMap) hs.get("FILE");  //Archivos
                HashMap hsFormQuery = new HashMap();

                String claveForma = (String) hsForm.get("$cf");
                String claveAplic = (String) hsForm.get("$ca");

                String pk = (String) hsForm.get("$pk");
                String tipoAccion = (String) hsForm.get("$ta");

                User usr = (User) request.getSession().getAttribute("user");
                arrVariables = admF.getVariablesFromProperties(hsForm);
                arrVariables = admF.getVariableByObject(usr, arrVariables);
                arrVariables = admF.cleanVariables(arrVariables);

                Bitacora bitacora = usr.getBitacora();
                bitacora.setClaveForma(Integer.valueOf(claveForma));
                if (claveAplic!=null){
                    bitacora.setClaveAplicacion(Integer.valueOf(claveAplic));
                }
                if (pk!=null){
                    bitacora.setClaveRegistro(Integer.valueOf(pk));
                }

                ArrayList arrVal = new ArrayList();
                arrVal.add("$cf");
                arrVal.add("$pk");
                arrVal.add("$ta");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    Forma forma = (Forma) request.getSession().getAttribute("forma");

                    forma.setPk(pk);
                    forma.setClaveForma(Integer.valueOf(claveForma));
                    forma.setTipoAccion(tipoAccion);
                    forma.setArrVariables(arrVariables);
                    List lstForma = forma.getForma(Integer.valueOf(claveForma));
                    List lstNew = null;
                    if (("0".equals(pk))&&("INSERT".equals(tipoAccion.toUpperCase()))){
                        String[] arrayData = new String[2];
                        arrayData[0] = claveForma;
                        arrayData[1] = "insert";
                        
                        bitacora.setBitacora("Busqueda de forma que no esta en el perfil.");
                        forma.setBitacora(bitacora);
                        lstNew = forma.getNewFormaById(arrayData);
                        if (!lstNew.isEmpty()){
                            lstForma = lstNew;
                            forma.addForma(Integer.valueOf(claveForma), lstNew);
                        }
                    }
                    //Analizamos segun la forma obtenida
                    boolean obligatorioOk = true;
                    if ((!lstForma.isEmpty())&&(obligatorioOk)){
                        Iterator it = lstForma.iterator();
                        while ((it.hasNext())&&(obligatorioOk)){
                            CampoForma cmp = (CampoForma) it.next();
                            String dato = null;
                            if (!"file".equals(cmp.getTipoControl())){
                                dato = (String) hsForm.get(cmp.getCampo());
                            }else{
                                //Si es NULL, por el formato del formulario no se subio el archivo
                                if (hsFile!=null){
                                    dato = (String) hsFile.get(cmp.getCampo());
                                }
                            }
                            dato = val.replaceComillas(dato);
                            hsFormQuery.put(cmp.getCampo(), dato);
                        }
                    }
                    ExecutionHandler ex = new ExecutionHandler();
                    List lstData = new ArrayList();
                    if (obligatorioOk){
                        lstData.add(hsFormQuery);
                        lstData.add(forma);
                        if ("0".equals(forma.getPk())){     // Es un nuevo elemento
                            bitacora.setBitacora("Agregar dato.");
                            forma.setBitacora(bitacora);
                            ex = forma.ingresarEntidad(lstData);
                        }else{
                            bitacora.setBitacora("Editar dato.");
                            forma.setBitacora(bitacora);
                            ex = forma.editarEntidad(lstData);
                        }
                        ex.setExecutionOK(true);
                    }else{
                        //Si hubo falla se eliminan los archivo subidos
                        ex.setExecutionOK(false);
                        AdminFile.deleFileFromServer(hsFile);
                    }
                    //actualizarData(request);
                    Integer xml = (Integer) ((ex.getObjectData()==null)?Integer.valueOf(forma.getPk()):ex.getObjectData());

                    if (forma.getDataMail()!=null){
                        User user = (User) request.getSession().getAttribute("user");
                        request.getSession().setAttribute("from",user.getEmail());
                        //request.getSession().setAttribute("to",forma.getDataMail().getStrTo());
                        request.getSession().setAttribute("to",user.getEmail());
                        request.getSession().setAttribute("copy",user.getEmail());
                        request.getSession().setAttribute("copyO",forma.getDataMail().getStrCopyO());
                        request.getSession().setAttribute("subject",forma.getDataMail().getSubJect());
                        request.getSession().setAttribute("message",forma.getDataMail().getTexto());
                        request.getSession().setAttribute("MAILPK",xml.toString());
                        request.getSession().setAttribute("MAILBITAC",forma.getBitacora().getIdBitacora().toString());
                        request.getSession().setAttribute("GOTSESS","OK");

                        request.getRequestDispatcher("/srvSendMail").forward(request, response);                        
                    }else{

                        AdminXML admXML = new AdminXML();
                        //request.getSession().setAttribute("xmlTab",admXML.salidaXML(String.valueOf(xml)));
                        request.getSession().setAttribute("xmlTab",admXML.salidaXMLBitacora(String.valueOf(xml),
                                                                   String.valueOf(forma.getBitacora().getIdBitacora())));
                        request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                    }
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvFormaInsert");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvFormaInsert");
            val.executeErrorException(e, request, response);
        } finally {
            cleanMemory(request);
            out.close();
        }
    }

    private void cleanMemory(HttpServletRequest request){
        request.getSession().removeAttribute("from");
        request.getSession().removeAttribute("to");
        request.getSession().removeAttribute("subject");
        request.getSession().removeAttribute("message");
        request.getSession().removeAttribute("copy");
        request.getSession().removeAttribute("copyO");
        request.getSession().removeAttribute("GOTSESS");
        request.getSession().removeAttribute("e_mail");
        request.getSession().removeAttribute("msgExist");
        request.getSession().removeAttribute("MAILPK");
        request.getSession().removeAttribute("MAILBITAC");
    }

    /**
     * Actualiza a memoria los datos del usuario
     * @param request
     */
    private void actualizarData(HttpServletRequest request) throws ExceptionHandler {
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
