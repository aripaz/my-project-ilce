package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminXML;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LoginHandler;

/**
 * Servlet implementado pata manejar el ingreso del usuario
 * @author ccatrilef
 */
public class srvLogin extends HttpServlet {

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
        LoginHandler lg = new LoginHandler();
        try {
            cleanSessionMemory(request);
            
            Bitacora bitacora = new Bitacora(request);
            bitacora.setEnable(true);
            bitacora.setBitacora("Ingreso del usuario");

            String lgn = (String) request.getParameter("lgn");
            String psw = (String) request.getParameter("psw");
            User user = new User();
            user.setBitacora(bitacora);
            user.setLogin(lgn);
            user.setPassword(psw);

            Perfil perfil = new Perfil();
            perfil.setBitacora(bitacora);
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

                    forma.setStrWhereQuery("cf.clave_perfil="+perfil.getClavePerfil() );
                    forma.getFormasByAplications(lst);

                    user.getBitacora().setBitacora("");
                    request.getSession().setAttribute("user", user);
                    request.getSession().setAttribute("perfil", perfil);
                    request.getSession().setAttribute("forma", forma);

                    AdminXML adm = new AdminXML();
                    String[][] arrVariables = null;

                    StringBuffer xmlSession = adm.getSessionXML(user, arrVariables);
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
            }else{
                lg.setTextExecution("Usuario o password incorrecto, verifique");
                request.getSession().setAttribute("loginHand",lg);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }catch (ExceptionHandler eh){
            try{
                lg.setTextExecution(eh.getTextMessage().toString());
                request.getSession().setAttribute("loginHand",lg);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }catch (Exception es){
                lg.setTextExecution("Problemas en la execucion del Error de srvLogin.");
                request.getSession().setAttribute("loginHand",lg);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }catch(Exception e){
            lg.setTextExecution("Problemas patra efectuar el Login.");
            request.getSession().setAttribute("loginHand",lg);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } finally {
            out.close();
        }
    }

    /**
     * Método para limpiar de memoria los datos asociados al Login.
     * @param request
     */
    private void cleanSessionMemory(HttpServletRequest request){

        request.getSession().removeAttribute("loginHand");
        request.getSession().removeAttribute("xmlSession");
        request.getSession().removeAttribute("xmlMenu");
        request.getSession().removeAttribute("xmlForma");
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("perfil");
        request.getSession().removeAttribute("forma");
        request.getSession().removeAttribute("lstAreas");
        request.getSession().removeAttribute("registerOK");
        request.getSession().removeAttribute("xmlTab");
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
        return "Servlet implementado pata manejar el ingreso del usuario";
    }

}
