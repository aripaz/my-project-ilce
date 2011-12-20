/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
package mx.ilce.servlet.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminForm;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
import mx.ilce.util.Validation;

/**
 * Servlet para poder dar el registro de usuario
 * @author ccatrilef
 */
public class srvRegister extends HttpServlet {
   
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

                                StringBuffer xmlSession = getXMLSession(user);

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
                val.setTextMessage("Problemas en la execución del Error de srvRegister");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execución de srvRegister");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    } 

    /**
     * Entrega un XML con los datos del usuario, pero sin permisos y con los
     * datos mínimos
     * @param user
     * @return
     */
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

    /**
     * Entrega un XML de menú vacio
     * @return
     */
    private StringBuffer getXMLMenuEmpty(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><qry>");
        str.append("<registro id=\"0\">");
        str.append("</registro></qry>");
        return str;
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
        return "Servlet para poder dar el registro de usuario";
    }
}
