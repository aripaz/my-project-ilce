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
package mx.ilce.servlet.forma;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.User;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.ExecutionHandler;
import mx.ilce.handler.LoginHandler;
import mx.ilce.util.Validation;

/**
 * Servlet implementado para ejecutar una instrucción de eliminación de los
 * antiguos permisos y agregar los nuevos. NO PROBADO POR CAPA VISTA
 * @author ccatrilef
 */
public class srvFormaDual extends HttpServlet {
   
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
                String pk = (String) hsForm.get("$pk");
                String tipoAccion = (String) hsForm.get("$ta");
                String strWhere = (String) hsForm.get("$w");

                ArrayList arrVal = new ArrayList();
                arrVal.add("$cf");
                arrVal.add("$pk");
                arrVal.add("$ta");
                arrVal.add("$w");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    Forma forma = (Forma) request.getSession().getAttribute("forma");
                    forma.setPk(pk);
                    forma.setClaveForma(Integer.valueOf(claveForma));
                    forma.setTipoAccion(tipoAccion);
                    forma.setStrWhereQuery(strWhere);
                    List lstForma = forma.getForma(Integer.valueOf(claveForma));
                    List lstNew = null;
                    if (("0".equals(pk))&&("INSERT".equals(tipoAccion.toUpperCase()))){
                        String[] arrayData = new String[2];
                        arrayData[0] = claveForma;
                        arrayData[1] = "insert";
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
                    lstData.add(hsFormQuery);
                    lstData.add(forma);
                    ex = forma.ingresarDataPermisos(lstData);
                    ex.setExecutionOK(true);

                    actualizarData(request);

                    Integer xml = (Integer) ((ex.getObjectData()==null)?Integer.valueOf(forma.getPk()):ex.getObjectData());
                    AdminXML admXML = new AdminXML();
                    request.getSession().setAttribute("xmlTab", admXML.salidaXML(String.valueOf(xml)));
                    request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execucion del Error de srvFormaDual");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvFormaDual");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
        }
    } 

    /**
     * Actualiza a memoria los datos del usuario
     * @param request
     */
    private void actualizarData(HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            Perfil perfil = new Perfil();
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
        } catch (ExceptionHandler ex) {
            try {
                throw new ExceptionHandler(ex, this.getClass(), "Problemas al actualizar datos del usuario");
            } catch (ExceptionHandler ex1) {
                Logger.getLogger(srvFormaDual.class.getName()).log(Level.SEVERE, null, ex1);
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
        return " * Servlet implementado para ejecutar una instruccion de "
                + "eliminacion de los antiguos permisos y agregar los nuevos. NO PROBADO";
    }
}
