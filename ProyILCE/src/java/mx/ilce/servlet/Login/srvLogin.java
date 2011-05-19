package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.component.AdminXML;
import mx.ilce.conection.ConSession;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.LoginHandler;

/**
 *
 * @author vaio
 */
public class srvLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String lgn = (String) request.getParameter("lgn");
            String psw = (String) request.getParameter("psw");
            User user = new User();
            user.setLogin(lgn);
            user.setPassword(psw);

            Perfil perfil = new Perfil();
            LoginHandler lg = perfil.login(user);
            if (lg.isLogin()){
                user = (User) lg.getObjectData();
                if (user != null){
                    List lst = perfil.getLstAplicacion();

                    Forma forma = new Forma();
                    forma.getFormasByAplications(lst);

                    request.getSession().setAttribute("user", user);
                    request.getSession().setAttribute("perfil", perfil);
                    request.getSession().setAttribute("forma", forma);

                    AdminXML adm = new AdminXML();
                    StringBuffer xmlSession = adm.getSessionXML(user);
                    StringBuffer xmlMenu = adm.getMenuXML(user);

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
        }catch(SQLException e){
            e.printStackTrace();
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
