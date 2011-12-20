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
package mx.ilce.importDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.component.AdminFile;
import mx.ilce.component.AdminForm;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.Validation;

/**
 * Servlet implementado para realizar la carga de archivos a la Base de Datos
 * @author ccatrilef
 */
public class srvImportDB extends HttpServlet {
   
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
            AdminForm admF = new AdminForm();
            HashMap hs = admF.getFormulario(request);
            HashMap hsForm = (HashMap) hs.get("FORM");  //Archivos
            HashMap hsFile = (HashMap) hs.get("FILE");  //Archivos
            AdminFile admFile = new AdminFile();
            String rutaUpload = "";
            if ((hsFile!=null)&&(!hsFile.isEmpty())){
                rutaUpload = (String) hsFile.get("archivoBD");
                admFile.setRutaFile(rutaUpload);
                admFile.setIdUser(0);
                boolean addFile = admFile.putFile();
                if (addFile){
                    String dato = admFile.getNameFile();
                }
                String strTypeFile = (String) hsForm.get("typeFile");
                String strStopError = (String) hsForm.get("stopError");
                String strDisplay = (String) hsForm.get("display");
                AdmImportDB admImp = new AdmImportDB();
                admImp.setIdArchivoCarga(Integer.valueOf(strTypeFile));
                if ((strStopError==null)||("Y".equals(strStopError))){
                    admImp.setStopError(true);
                }else{
                    admImp.setStopError(false);
                }

                /*
                //TODO: En caso que se cambie a una carga de archivos muy
                //grandes, cambiar al uso del Daemon para que continue solo
                // y entregue un mensaje "PROCESANDO" o algo parecido, mientras
                // por detras sigue trabajando con el archivo
                DaemonCarga dc = new DaemonCarga();
                admImp.setRutaFile(rutaUpload);
                dc.setAdmImport(admImp);
                dc.setDisplay(strDisplay);
                dc.setDaemon(true);
                dc.start();
                */
                
                admImp.setRutaFile(rutaUpload);
                admImp.insertEstadoValidando();
                admImp.procesarArchivo();
                admImp.updateEstadoCargando();

                String sldData = "";
                String sldError = "";
                StringBuffer xmlForma = new StringBuffer("");
                if (admImp.isExistError()){
                    if ("XML".equals(strDisplay)){
                        sldError =  admImp.getXMLError();
                        sldData = admImp.getStrQuery();
                        xmlForma = new StringBuffer(sldError);
                    }else{
                        sldError = admImp.getStrError();
                        sldData = admImp.getStrQuery();
                        xmlForma = new StringBuffer(sldError);
                    }
                    xmlForma = new StringBuffer("NOT OK");
                }else{
                    if ("XML".equals(strDisplay)){
                        sldData = admImp.getStrQuery();
                        xmlForma = new StringBuffer(sldData);
                    }else{
                        sldData = admImp.getStrQuery();
                        xmlForma = new StringBuffer(sldData);
                    }
                    //ejecución de las queries
                    boolean sld = admImp.processQuery();

                    if (!sld){
                        xmlForma.append(admImp.getStrError());
                        admImp.setStoreProcedure(AdmImportDB.processERROR);
                    }else{
                        admImp.setStoreProcedure(AdmImportDB.processCARGA);
                    }

                    admImp.addToDataStoreProcedure(admImp.getIdEstadoCarga());
                    admImp.processStoreProcedure();

                    admImp.updateEstadoFinalizado();
                    xmlForma = new StringBuffer("OK");
                }
                request.getSession().setAttribute("xmlForma", xmlForma);
                if ("XML".equals(strDisplay)){
                    request.getRequestDispatcher("/resource/jsp/xmlForma.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/jspPruebaCarga.jsp").forward(request, response);
                }
            }else{
                AdmImportDB admImp = new AdmImportDB();
                List lst = admImp.getListArchivos();

                request.getSession().setAttribute("lstArch", lst);
                request.getRequestDispatcher("/jspPruebaCarga.jsp").forward(request, response);
            }
        }catch (ExceptionHandler eh){
            try{
                val.executeErrorHandler(eh,request, response);
            }catch (Exception es){
                val.setTextMessage("Problemas en la execución del Error de srvImportDB");
                val.executeErrorException(es, request, response);
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execución de srvImportDB");
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
