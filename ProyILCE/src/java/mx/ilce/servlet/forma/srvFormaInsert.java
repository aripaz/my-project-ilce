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
import mx.ilce.handler.SpyHandler;
import mx.ilce.util.Validation;

/**
 * Servlet implementado para insertar los datos obtenidos de un formulario en
 * la Base de datos, asociado a la forma que le corresponda, según el perfil
 * del usuario conectado. Utilizada en la creación de datos nuevos.
 * Se modificó para tambien aceptar la actualización de datos.
 * @author ccatrilef
 */
public class srvFormaInsert extends HttpServlet {

    private String[][] arrVariables = null;

    /**
     * Procesa los requerimientos HTTP de tipo GET y POST, al recibir las
     * llamadas de los métodos doGet() y doPost()
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
                AdminForm admF = new AdminForm();
                HashMap hs = admF.getFormulario(request);

                HashMap hsForm = (HashMap) hs.get("FORM");  //Datos
                HashMap hsFile = (HashMap) hs.get("FILE");  //Archivos
                spy.setHsForm(hsForm);
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
                if (claveForma!=null){
                    bitacora.setClaveForma(Integer.valueOf(claveForma));
                }
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
                    AdminFile admFile = new AdminFile();
                    Forma forma = (Forma) request.getSession().getAttribute("forma");
                    User user = (User) request.getSession().getAttribute("user");

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
                    boolean addFile = false;
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
                                    admFile.setRutaFile(dato);
                                    admFile.setIdUser(user.getClaveEmpleado());
                                    addFile = admFile.putFile();
                                    if (addFile){
                                        dato = admFile.getNameFile();
                                    }
                                }
                            }
                            dato = val.replaceComillas(dato);
                            if (hsFormQuery.containsKey(cmp.getCampo())){
                                if (hsFormQuery.get(cmp.getCampo())==null){
                                    hsFormQuery.put(cmp.getCampo(), dato);
                                }
                            }else{
                                hsFormQuery.put(cmp.getCampo(), dato);
                            }
                        }
                    }
                    ExecutionHandler ex = new ExecutionHandler();
                    List lstData = new ArrayList();
                    if (obligatorioOk){
                        lstData.add(hsFormQuery);
                        lstData.add(forma);
                        forma.setClavePerfil(user.getClavePerfil());
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
                    
                    if (addFile && (xml>0)){
                        admFile.setBitacora(bitacora);
                        admFile.setIdUser(user.getClaveEmpleado());
                        admFile.setIdForma(Integer.valueOf(claveForma));
                        admFile.setIdRegister(xml);
                        admFile.registerFile() ;
                    }

                    if (forma.getDataMail()!=null){
                        request.getSession().setAttribute("from",user.getEmail());
                        request.getSession().setAttribute("to",forma.getDataMail().getStrTo());
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
                        request.getSession().setAttribute("xmlTab",admXML.salidaXMLBitacora(String.valueOf(xml),
                                                                   String.valueOf(forma.getBitacora().getIdBitacora())));
                        spy.setXmlSld(new StringBuffer((String) request.getSession().getAttribute("xmlTab")));
                        request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                    }
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execución del Error de srvFormaInsert");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execución de srvFormaInsert");
            val.executeErrorException(e, request, response);
        } finally {
            cleanMemory(request);
            out.close();
            spy.SpyData("svrFormaInsert");
        }
    }

    /**
     * Método para limpiar de memoria los datos asociados al mail. Nos asegura
     * que ante una nueva invocación no se tomen datos de un mail anterior.
     * @param request
     */
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
    
    /**
     * Maneja los requerimientos HTTP del tipo GET
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
     * Maneja los requerimientos HTTP del tipo POST
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
     * Entrega una corta descripción del Servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet implementado para insertar y actualizar los datos obtenidos de un "
                + "formulario en la Base de datos";
    }
}
