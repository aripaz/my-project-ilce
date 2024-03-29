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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminForm;
import mx.ilce.conection.ConEntidad;
import mx.ilce.controller.Forma;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.SpyHandler;
import mx.ilce.util.Validation;

/**
 *  Servlet implementado para hacer búsqueda general de datos
 * @author ccatrilef
 */
public class srvFormaSearch extends HttpServlet {

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
                AdminForm admForm = new AdminForm();
                HashMap hs = admForm.getFormulario(request);
                HashMap hsForm = (HashMap) hs.get("FORM");
                hsForm = (HashMap) hs.get("FORM");
                spy.setHsForm(hsForm);

                String claveForma = (String) hsForm.get("$cf");
                String claveAplic = (String) hsForm.get("$ca");

                String pk = (String) hsForm.get("$pk");
                String tipoAccion = (String) hsForm.get("$ta");
                String strWhere = (String) hsForm.get("$w");

                User usr = (User) request.getSession().getAttribute("user");
                arrVariables = admForm.getVariablesFromProperties(hsForm);
                arrVariables = admForm.getVariableByObject(usr, arrVariables);
                arrVariables = admForm.cleanVariables(arrVariables);

                Bitacora bitacora = usr.getBitacora();
                bitacora.setClaveForma(Integer.valueOf(claveForma));
                if (claveAplic!=null){
                    bitacora.setClaveAplicacion(Integer.valueOf(claveAplic));
                }

                ArrayList arrVal = new ArrayList();
                arrVal.add("$cf");
                arrVal.add("$ta");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);

                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    Forma formaMem = (Forma) request.getSession().getAttribute("forma");
                    Forma forma = new Forma();
                    forma = formaMem;                    
                    if (forma !=null){
                        forma.setFormData(hsForm);
                        ArrayList arrayForm = (ArrayList) hs.get("arrayFORM");  //Datos
                        forma.setFormName(arrayForm);
                        forma.setPk(pk);
                        forma.setClaveForma(Integer.valueOf(claveForma));
                        forma.setTipoAccion(tipoAccion);
                        forma.setArrVariables(arrVariables);
                        forma.setClavePerfil(usr.getClavePerfil());
                        String[] strData = getArrayData(hsForm);
                        if (pk!=null){
                            String[] strDataAdic = new String[strData.length+1];
                            strDataAdic[0]=pk;
                            for(int i=0;i<strData.length;i++){
                                strDataAdic[i+1]=strData[i];
                            }
                            strData = strDataAdic;
                        }
                        forma.setArrayData(strData);
                        String whereForm = getWhereData(hs, forma.getForma(forma.getClaveForma()));
                        forma.setStrWhereQuery("");
                        if ((strWhere!=null) && (strWhere.trim().length()>0)){
                            if ((whereForm!=null) && (whereForm.trim().length()>0)){
                                forma.setStrWhereQuery(strWhere + " AND " + whereForm);
                            }else{
                                forma.setStrWhereQuery(strWhere);
                            }
                        }else{
                            if ((whereForm!=null) && (whereForm.trim().length()>0)){
                                forma.setStrWhereQuery(whereForm);
                            }
                        }
                        forma.setIncludeForaneo(false);
                        forma.setBitacora(bitacora);
                        forma.getBitacora().setEnable(false);

                        String registerOK = (String) request.getSession().getAttribute("registerOK");
                        StringBuffer xmlForma = new StringBuffer("");
                        if ((registerOK!=null) && ("OK".equals(registerOK))){
                            xmlForma = getXMLFormaEmpty();
                        }else{
                            forma.ingresarBusquedaAvanzada();
                            xmlForma = forma.getXmlEntidad();
                        }

                        request.getSession().removeAttribute("xmlForma");
                        request.getSession().setAttribute("xmlForma", xmlForma);
                        spy.setXmlSld(xmlForma);
                    }
                    request.getRequestDispatcher("/resource/jsp/xmlForma.jsp").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execución del Error de srvFormaSearch");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execución de srvFormaSearch");
            val.executeErrorException(e, request, response);
        } finally {
            out.close();
            spy.SpyData("srvFormaSearch");
        }
    } 

    /**
     * Entrega un XML de datos vacios
     * @return
     */
    private StringBuffer getXMLFormaEmpty(){
        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append("<qry>");
        str.append("<registro id='0'>");
        str.append("</registro>");
        str.append("</qry>");
        return str;
    }

    /**
     * Genera un String con la estructura adicional de una query, con la data
     * entregada esto servirá de complemento a la query principal, para ayudar
     * en el filtro de datos
     * @param hsDataForm    HashMap con los datos capturados del formulario
     * @param lstForma  Listado de campos de la forma
     * @return
     * @throws ExceptionHandler
     */
    private String getWhereData(HashMap hsDataForm, List lstForma) throws ExceptionHandler{
        String strSal = "";
        try{
            HashMap hsForm = (HashMap) hsDataForm.get("FORM");
            StringBuilder str = new StringBuilder();

            if ((lstForma != null) && (!lstForma.isEmpty())){
                String[][] strCampos = new String[lstForma.size()][2];
                StringBuilder strQuery = new StringBuilder("select ");
                String nameTable ="";
                Iterator it = lstForma.iterator();
                int i=0;                
                while (it.hasNext()){
                    CampoForma cmp = (CampoForma) it.next();
                    String strData = (String) hsForm.get(cmp.getCampo());
                    if ((strData!=null)&&(!"".equals(strData))) {
                        strCampos[i][0]= cmp.getCampo();
                        strCampos[i++][1]= strData;
                        nameTable = cmp.getTabla();
                        strQuery.append(cmp.getCampo()).append(",");
                    }
                }
                if (i>0){
                    strQuery.delete(strQuery.length()-1, strQuery.length());
                    strQuery.append(" from ").append(nameTable).append(" where 1=2");
                    ConEntidad con = new ConEntidad();

                    DataTransfer dataTransfer = new DataTransfer();
                    dataTransfer.setQuery(strQuery.toString());
                    dataTransfer.setArrData(new String[0]);
                    dataTransfer.setArrVariables(arrVariables);
                    HashCampo hsCmp = con.getDataByQuery(dataTransfer);

                    if (hsCmp.getLengthCampo()>0){
                        for (int j=0;j<i;j++){
                            Campo cmp = hsCmp.getCampoByNameDB(strCampos[j][0]);
                            if ( (strCampos[j][1]!=null) && (!"".equals(strCampos[j][1])) )  {
                                if ("java.lang.String".equals(cmp.getTypeDataAPL()))
                                {
                                    str.append(strCampos[j][0]).append(" = ").append(("'"+strCampos[j][1]+"'"));
                                }else{
                                    str.append(strCampos[j][0]).append(" = ").append(strCampos[j][1]);
                                }
                                str.append(" AND ");
                            }
                        }
                        if (str.length()>=4){
                            if (str.substring(str.length()-4, str.length()).trim().equals("AND")) {
                                str.delete(str.length()-4, str.length());
                            }
                        }
                    }
                }
                strSal = str.toString();
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                      "Problemas para ejecutar la QUERY de la forma con el WHERE");
            eh.setDataToXML(hsDataForm);
            eh.setDataToXML(lstForma);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
            //throw new ExceptionHandler(ex,this.getClass(),"Problemas para ejecutar la QUERY de la forma con el WHERE");
        }
        return strSal;
    }

    /**
     * Genera un Array con la data obtenida desde el formulario, cuando esta
     * data corresponde a las que poseen los nombres $1, $2, $3, etc.
     * @param hsForm    Datos capturados desde el formulario
     * @return
     */
    private String[] getArrayData(HashMap hsForm){
        String[] strSld = new String[0];

        if (!hsForm.isEmpty()){
            String[] arr = new String[hsForm.size()];
            int j=0;
            for (int i=0;i<hsForm.size();i++){
                String dato = (String) hsForm.get("$"+i);
                if ((dato!=null)&&(!"".equals(dato))){
                    arr[j]=dato;
                    j++;
                }
            }
            if (j>0){
                strSld = new String[j];
            }
            System.arraycopy(arr, 0, strSld, 0, j);
        }
        return strSld;
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
        return "Servlet implementado para hacer busqueda general de datos";
    }
}
