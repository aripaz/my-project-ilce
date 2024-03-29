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
package mx.ilce.servlet.grid;

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
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Forma;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LoginHandler;
import mx.ilce.handler.SpyHandler;
import mx.ilce.util.Validation;

/**
 *  Servlet encargado de cargar los datos de la grilla
 * @author ccatrilef
 */
public class srvGrid extends HttpServlet {
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
        HashMap hsForm = null;
        Validation val = new Validation();
        try {
            if (!val.validateUser(request)){
                val.executeErrorValidationUser(this.getClass(), request, response);
            }else{
                AdminForm admForm = new AdminForm();
                HashMap hs = admForm.getFormulario(request);

                hsForm = (HashMap) hs.get("FORM");  //Datos
                spy.setHsForm(hsForm);

                String claveForma = (String) hsForm.get("$cf");
                String claveAplic = (String) hsForm.get("$ca");

                String dp = (String) hsForm.get("$dp");
                String tipoAccion = (String) hsForm.get("$ta");
                if ((tipoAccion==null)||("".equals(tipoAccion))){
                    tipoAccion = "select";
                }
                String strWhere = (String) hsForm.get("$w");

                String sidx = (String) hsForm.get("sidx");
                String sord = (String) hsForm.get("sord");

                ArrayList arrVal = new ArrayList();
                arrVal.add("$cf");
                arrVal.add("$dp");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    String[] strData = getArrayData(hsForm);
                    User user = (User) request.getSession().getAttribute("user");
                    user.getClaveEmpleado();

                    arrVariables = admForm.getVariablesFromProperties(hsForm);
                    arrVariables = admForm.getVariableByObject(user, arrVariables);
                    arrVariables = admForm.cleanVariables(arrVariables);

                    Bitacora bitacora = user.getBitacora();
                    bitacora.setBitacora("");
                    bitacora.setClaveForma(Integer.valueOf(claveForma));
                    if (claveAplic!=null){
                        bitacora.setClaveAplicacion(Integer.valueOf(claveAplic));
                    }

                    Aplicacion apl = new Aplicacion();
                    apl.setClaveEmpleado(Integer.valueOf(user.getClaveEmpleado()));

                    Forma forma = (Forma) request.getSession().getAttribute("forma");
                    if ((forma !=null)&&(apl!=null)){

                        List lstF = forma.getForma(Integer.valueOf(claveForma));
                        StringBuffer xmlForma = new StringBuffer();
                        HashMap hsF = forma.getHsForma();
                        if (hsF.containsKey(Integer.valueOf(claveForma))){
                            if (lstF!=null){
                                apl.addForma(Integer.valueOf(claveForma),lstF);
                                apl.setDisplay(dp);
                                apl.setClaveForma(Integer.valueOf(claveForma));
                                apl.setTipoAccion(tipoAccion);
                                if (dp.equals("header")){
                                    if (strWhere==null){
                                         strWhere = "1=2";
                                    }else{
                                        if (strWhere.trim().length()>0){
                                            strWhere = strWhere + " AND 1=2";
                                        }else{
                                            strWhere = "1=2";
                                        }
                                    }
                                }
                                apl.setStrWhereQuery(strWhere);
                                apl.setArrayData(strData);
                                apl.setArrVariables(arrVariables);
                                apl.setClavePerfil(user.getClavePerfil());
                                String numPage = (String) hsForm.get("page");
                                String numRows = (String) hsForm.get("rows");
                                apl.setNumPage(numPage);
                                apl.setNumRows(numRows);


                                if ((sidx!=null)&&(!"".equals(sidx)) &&
                                    (sord!=null)&&(!"".equals(sord)))
                                {
                                    sidx = admForm.cleanSIDX(sidx,"_",3);
                                    apl.setOrderBY(sidx + " " + sord);
                                }

                                bitacora.setBitacora("Busqueda de datos para Grid.");
                                bitacora.setEnable(true);
                                apl.setBitacora(bitacora);
                                apl.mostrarForma();
                                xmlForma = apl.getXmlEntidad();
                            }else{
                                Exception e = new Exception();
                                ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Error de Permiso");
                                eh.setTextError("No existe la configuración de campos para la Forma solicitada");
                                eh.setDataToXML("CLAVE FORMA",claveForma);
                                eh.setDataToXML("$dp",dp);
                                eh.setDataToXML("$ta",tipoAccion);
                                eh.setStringData(eh.getDataToXML());
                                eh.setSeeStringData(true);
                                xmlForma = eh.getXmlError();
                            }
                        }else{
                            Exception e = new Exception();
                            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Error de Permiso");
                            eh.setTextError("No existe en el perfil la Forma solicitada");
                            eh.setDataToXML("CLAVE FORMA",claveForma);
                            eh.setDataToXML("$dp",dp);
                            eh.setDataToXML("$ta",tipoAccion);
                            eh.setDataToXML("CLAVE PERFIL",user.getClavePerfil().toString());
                            eh.setStringData(eh.getDataToXML());
                            eh.setSeeStringData(true);
                            xmlForma = eh.getXmlError();
                        }
                        spy.setXmlSld(xmlForma);
                        request.getSession().setAttribute("xmlGrid", xmlForma);
                    }
                    request.getRequestDispatcher("/resource/jsp/xmlGrid.jsp").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
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
            spy.SpyData("srvGrid");
        }
    } 

    /**
     * Genera un Array con la data obtenida desde el formulario, cuando esta
     * data corresponde a las que poseen los nombres $1, $2, $3, etc.
     * @param hsForm    Datos capturados desde el formulario
     * @return
     */
    private String[] getArrayData(HashMap hsForm){
        String[] strSal = null;
        int numMaxParam = 10;

        ArrayList lst = new ArrayList();
        boolean seguir = true;
        for (int i=1;i<numMaxParam&&seguir;i++){
            String strData = (String) hsForm.get("$"+i);
            if (strData!=null){
                lst.add(strData);
            }else{
                seguir = false;
            }
        }
        if (!lst.isEmpty()){
            strSal = new String[lst.size()];
            for (int i=0;i<lst.size();i++){
                strSal[i] = (String) lst.get(i);
            }
        }
        return strSal;
    }

    /**
     * Se utiliza para actualizar la data en memoria del usuario
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
        return "Servlet encargado de cargar los datos de la grilla";
    }

}
