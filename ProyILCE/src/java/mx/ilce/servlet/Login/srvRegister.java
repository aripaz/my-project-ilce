package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.ilce.util.Validation;

/**
 * Servlet para dar el registro del usuario
 * @author ccatrilef
 */
public class srvRegister extends HttpServlet {
   
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

            List lstAreas = (List) request.getSession().getAttribute("lstAreas");

            Bitacora bitacora = new Bitacora(request);
            Perfil perfil = new Perfil();
            perfil.setBitacora(bitacora);

            String nombre = (String) hsForm.get("nombre");
            String appPat = (String) hsForm.get("appPat");
            String appMat = (String) hsForm.get("appMat");
            String e_mail = (String) hsForm.get("e_mail");
            String passw1 = (String) hsForm.get("passw1");
            String passw2 = (String) hsForm.get("passw2");
            String cmbArea = (String) hsForm.get("cmbArea");

            if ((lstAreas!=null)
             && (nombre!=null) && (!"".equals(nombre))
             && (appPat!=null) && (!"".equals(appPat))
             && (appMat!=null) && (!"".equals(appMat))
             && (e_mail!=null)  && (!"".equals(e_mail))
             && (passw1!=null)   && (!"".equals(passw1))
             && (cmbArea!=null)   && (!"0".equals(cmbArea))
             ) {

                User user = new User();
                user.setNombre(nombre);
                user.setApellidoPaterno(appPat);
                user.setApellidoMaterno(appMat);
                user.setEmail(e_mail);
                user.setPassword(passw1);
                user.setLogin(e_mail);
                user.setClaveArea(Integer.valueOf(cmbArea));

                perfil.setUser(user);
                LoginHandler lg = new LoginHandler();

                if (!perfil.existUser()){
                    ExecutionHandler exeHn = perfil.registrarUsuario();
                    if (exeHn.isExecutionOK()){
                        perfil.getBitacora().setEnable(true);
                        perfil.setArrVariables(new String[0][0]);
                        lg = perfil.login(user);
                        if (lg.isLogin()){
                            user = (User) lg.getObjectData();
                            if (user != null){
                                if(user.getBitacora()!=null){
                                    user.getBitacora().setEnable(false);
                                }
                                Forma forma = new Forma();
                                forma.setAliasTab("");
                                forma.setHsForma(new HashMap());
                                forma.setPk("0");
                                forma.setTipoAccion("");

                                user.getBitacora().setBitacora("");
                                request.getSession().setAttribute("user", user);
                                request.getSession().setAttribute("perfil", perfil);
                                request.getSession().setAttribute("forma", forma);

                                //StringBuffer xmlSession = adm.getSessionXML(user, arrVariables);
                                StringBuffer xmlSession = getXMLSession(user);

                                //StringBuffer xmlMenu = adm.getMenuXML(user,arrVariables);
                                StringBuffer xmlMenu = getXMLMenuEmpty();

                                request.getSession().setAttribute("xmlSession", xmlSession );
                                request.getSession().setAttribute("xmlMenu",xmlMenu);
                                request.getSession().setAttribute("user",user);
                                request.getSession().setAttribute("registerOK","OK");

                                request.getRequestDispatcher("/vista.jsp").forward(request, response);
                            }else{
                                lg.setTextExecution("Error en obtención de datos del Usuario, aunque se logro Login");
                                request.getSession().setAttribute("loginHand",lg);
                                request.getRequestDispatcher("/index.jsp").forward(request, response);
                            }
                        }
                    }else{
                        lg.setTextExecution("Error en el Login del Usuario, aunque se logro registrar");
                        request.getSession().setAttribute("loginHand",lg);
                        request.getRequestDispatcher("/index.jsp").forward(request, response);
                    }
                }else{
                    request.getSession().setAttribute("nombre", nombre);
                    request.getSession().setAttribute("appPat", appPat);
                    request.getSession().setAttribute("appMat", appMat);
                    request.getSession().setAttribute("e_mail", e_mail);
                    request.getSession().setAttribute("passw1", passw1);
                    request.getSession().setAttribute("passw2", passw2);
                    request.getSession().setAttribute("lstAreas", lstAreas);

                    request.getSession().setAttribute("lstAreas", lstAreas);
                    request.getSession().setAttribute("msgExist", "Existe un usuario usando ese mail");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
            }else{
                lstAreas = perfil.getListArea();
                request.getSession().setAttribute("lstAreas", lstAreas);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (ExceptionHandler eh) {
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

    private StringBuffer getXMLSession(User user){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append("<registro id=\"0\">");
        str.append("<clave_empleado tipo_dato=\"string\"><![CDATA[");
        str.append(user.getClaveEmpleado());
        str.append("]]></clave_empleado>");
        str.append("<nombre tipo_dato=\"string\"><![CDATA[");
        str.append(user.getNombre());
        str.append("]]></nombre>");
        str.append("<apellido_paterno tipo_dato=\"string\"><![CDATA[");
        str.append(user.getApellidoPaterno());
        str.append("]]></apellido_paterno>");
        str.append("<apellido_materno tipo_dato=\"string\"><![CDATA[");
        str.append(user.getApellidoMaterno());
        str.append("]]></apellido_materno>");
        str.append("<email tipo_dato=\"string\"><![CDATA[");
        str.append(user.getEmail());
        str.append("]]></email>");
        str.append("<clave_perfil tipo_dato=\"integer\"><![CDATA[");
        str.append(0);
        str.append("]]></clave_perfil>");
        str.append("<foto tipo_dato=\"string\"><![CDATA[]]></foto>");
        str.append("</registro>");
        return str;
    }

    private StringBuffer getXMLMenuEmpty(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><qry>");
        str.append("<registro id=\"0\">");
        str.append("</registro></qry>");
        return str;
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
