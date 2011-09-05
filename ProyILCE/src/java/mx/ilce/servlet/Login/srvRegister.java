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
            String email = (String) hsForm.get("e_mail");
            String pass = (String) hsForm.get("passw1");
            String cmbArea = (String) hsForm.get("cmbArea");

            if ((lstAreas!=null)
             && (nombre!=null) && (!"".equals(nombre))
             && (appPat!=null) && (!"".equals(appPat))
             && (appMat!=null) && (!"".equals(appMat))
             && (email!=null)  && (!"".equals(email))
             && (pass!=null)   && (!"".equals(pass))
             && (cmbArea!=null)   && (!"0".equals(pass))
             ) {

                User user = new User();
                user.setNombre(nombre);
                user.setApellidoPaterno(appPat);
                user.setApellidoMaterno(appMat);
                user.setEmail(email);
                user.setPassword(pass);
                user.setLogin(email);
                user.setClaveArea(Integer.valueOf(cmbArea));

                perfil.setUser(user);
                ExecutionHandler exeHn = perfil.registrarUsuario();
                LoginHandler lg = new LoginHandler();
                if (exeHn.isExecutionOK()){
                    perfil.getBitacora().setEnable(true);
                    lg = perfil.login(user);
                    if (lg.isLogin()){
                        user = (User) lg.getObjectData();
                        if (user != null){
                            if(user.getBitacora()!=null){
                                user.getBitacora().setEnable(false);
                            }
                            bitacora = user.getBitacora();
                            List lst = perfil.getLstAplicacion();
                            Forma forma = new Forma();
                            //bitacora.setBitacora("Obtencion de Formas");
                            //forma.setBitacora(bitacora);
                            forma.getFormasByAplications(lst);

                            user.getBitacora().setBitacora("");
                            request.getSession().setAttribute("user", user);
                            request.getSession().setAttribute("perfil", perfil);
                            request.getSession().setAttribute("forma", forma);

                            AdminXML adm = new AdminXML();
                            String[][] arrVariables = null;

                            //bitacora.setBitacora("Obtener datos Session");
                            //adm.setBitacora(bitacora);
                            StringBuffer xmlSession = adm.getSessionXML(user, arrVariables);

                            //bitacora.setBitacora("Obtener datos Menu");
                            //adm.setBitacora(bitacora);
                            StringBuffer xmlMenu = adm.getMenuXML(user,arrVariables);

                            request.getSession().setAttribute("xmlSession", xmlSession );
                            request.getSession().setAttribute("xmlMenu",xmlMenu);
                            request.getSession().setAttribute("user",user);

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
