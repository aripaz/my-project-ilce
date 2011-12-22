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
package mx.ilce.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.bean.User;
import mx.ilce.component.AdminForm;
import mx.ilce.handler.ExceptionHandler;

/**
 * Servlet encargado de la generación de reportes
 * @author ccatrilef
 */
public class SrvReport extends HttpServlet {

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
        try {
            AdminForm admForm = new AdminForm();
            HashMap hs = admForm.getFormulario(request);
            hsForm = (HashMap) hs.get("FORM");  //Datos
            String strOper = (String) hsForm.get("oper");
            if ("genReport".equals(strOper)){
                User user = (User) request.getSession().getAttribute("user");
                Integer idUser = 0;
                if (user!=null){
                    idUser = user.getClaveEmpleado();
                }

                String claveForma = (String) hsForm.get("$cf");
                String claveAplic = (String) hsForm.get("$ca");
                String strWhere = (String) hsForm.get("$w");
                String display = (String) hsForm.get("$dp");
                String idReport = (String) hsForm.get("$rep");

                if (claveForma!=null){
                    hsForm.put("$cf", claveForma);
                }
                if (claveAplic!=null){
                    hsForm.put("$ca", claveAplic);
                }
                if (display!=null) {
                    hsForm.put("$dp", display);
                }

                String[] strData = getArrayData(hsForm);

                // Report contendra las variables enviadas al Servlet
                Report report = new Report();
                report.setIdReport(Integer.valueOf(idReport));
                report.setArrData(strData);
                report.setStrWhere(strWhere);

                GenReport gen = new GenReport();
                gen.setIdUser(idUser);
                gen.setReport(report);
                gen.getDocument();
                AdmReport admReport = new AdmReport();

                request.getSession().setAttribute("xmlTab",
                        admReport.salidaXML(String.valueOf("PDF GENERADO"),gen.getRutaReport()));
                request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            }else if ("frmADDRPT".equals(strOper)){
                addReportFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDRPT".equals(strOper)){
                addReport(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDRPTX".equals(strOper)){
                cleanAddReport(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELRPT".equals(strOper)){
                delReport(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmUPDRPT".equals(strOper)){
                updateReportFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDRPT".equals(strOper)){
                updateReport(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDRPTX".equals(strOper)){
                cleanUpdateReport(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmSTR".equals(strOper)){
                structFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmADDSTR".equals(strOper)){
                addStructFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDSTR".equals(strOper)){
                addStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDSTRX".equals(strOper)){
                cleanAddStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELSTR".equals(strOper)){
                delStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmUPDSTR".equals(strOper)){
                updateStructFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDSTR".equals(strOper)){
                updateStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDSTRX".equals(strOper)){
                cleanUpdateStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmCNFSTR".equals(strOper)){
                configStructFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmADDCNFSTR".equals(strOper)){
                addConfigStructFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDCNFSTR".equals(strOper)){
                addConfigStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDCNFSTRX".equals(strOper)){
                cleanAddConfigStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELCNFSTR".equals(strOper)){
                delConfigStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("btnUPDCNFSTR".equals(strOper) ){
                updateConfigStructBtn(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmUPDCNFSTR".equals(strOper)){
                updateConfigStructFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmUPDCNFSTRCH".equals(strOper)){
                updateConfigStructChange(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDCNFSTR".equals(strOper)){
                updateConfigStruct(request, hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDCNFSTRX".equals(strOper)){
                cleanUpdateConfigStruct(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("OKBUTTON".equals(strOper)) {
                request.getSession().setAttribute("OKBUTTON", "OK");
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("SEEELEM".equals(strOper)) {
                seeElement(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("SEEELEMX".equals(strOper)) {
                cleanSeeElement(request);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmADDELEM".equals(strOper)) {
                addElementFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmUPDELEM".equals(strOper)){
                updateElementFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDELEM".equals(strOper)){
                updateElement(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDELEMX".equals(strOper)){
                updateElementClean(request);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELELEM".equals(strOper)){
                delElement(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("btnUPDELEM".equals(strOper)){
                updateElementBtn(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDELEM".equals(strOper)) {
                addElement(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmADDCNFELEM".equals(strOper)){
                addConfigElemFrm(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmADDCNFELEMX".equals(strOper)){
                cleanAddConfigElem(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmADDCNFELEMCH".equals(strOper)){
                addConfigElemChange(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDCNFELEM".equals(strOper)){
                addConfigElem(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELCNFELEM".equals(strOper)){
                delConfigElem(request, hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("frmUPDCNFELEM".equals(strOper)){
                updateConfigElemFrm(request, hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmUPDCNFELEMCH".equals(strOper)){
                updateConfigElemChange(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDCNFELEM".equals(strOper)){
                updateConfigElem(request,hsForm);
                cleanUpdateConfigElem(request);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("frmUPDCNFELEMX".equals(strOper)){
                cleanUpdateConfigElem(request);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else if ("SEEQUERY".equals(strOper)) {
                seeQuery(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("ADDQUERY".equals(strOper)){
                addQuery(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("UPDQUERY".equals(strOper)){
                updateQuery(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("DELQUERY".equals(strOper)){
                delQuery(request,hsForm);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }else if ("SEEQUERYX".equals(strOper)){
                cleanSeeQuery(request);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
//-----------------------------------------------------
            }else{
                AdmReport adm = new AdmReport();
                List lstReport = (List) adm.getListReport();
                request.getSession().setAttribute("lstReport", lstReport);
                request.getRequestDispatcher("/Report.jsp").forward(request, response);
            }
        } catch (ExceptionHandler ex) {
                Logger.getLogger(SrvReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    /**
     * Método para activar el formulario para agregar reportes
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void addReportFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        request.getSession().removeAttribute("ADDCNFSTR");
        request.getSession().removeAttribute("ADDSTR");
        request.getSession().removeAttribute("frmADDCNFSTR");
        request.getSession().removeAttribute("frmADDCNFTEXT");
        request.getSession().removeAttribute("frmADDSTR");
        request.getSession().removeAttribute("frmCNFSTR");
        request.getSession().removeAttribute("frmUPDCNFSTR");
        request.getSession().removeAttribute("frmUPDCNFTEXT");
        request.getSession().removeAttribute("frmUPDRPT");
        request.getSession().removeAttribute("frmUPDSTR");
        request.getSession().removeAttribute("frmSTR");
        request.getSession().removeAttribute("seeBUTTON");

        request.getSession().setAttribute("frmADDRPT", "OK");
    }

    /**
     * Método para agregar un reporte
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void addReport(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String nombreRPT = (String) hsForm.get("nombreRPT");

        Report report = new Report();
        report.setReport(nombreRPT);

        AdmReport adm = new AdmReport();
        adm.setReport(report);
        adm.addReport();

        List lstReport = (List) adm.getListReport();

        request.getSession().setAttribute("lstReport", lstReport);
    }

    /**
     * Método para limpiar de memoria los datos del formulario para agregar reportes
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void cleanAddReport(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        request.getSession().removeAttribute("frmADDRPT");
        request.getSession().removeAttribute("frmSTR");
        request.getSession().removeAttribute("idReport");
    }

    /**
     * Método para eliminar un reporte
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void delReport(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        //OK - ir agregando los otros delete anidados
        String idReport = (String) hsForm.get("idReport");

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));

        AdmReport adm = new AdmReport();
        adm.setReport(report);
        adm.deleteReport();

        List lstReport = (List) adm.getListReport();

        request.getSession().removeAttribute("frmSTR");
        request.getSession().removeAttribute("idReport");
        request.getSession().removeAttribute("lstStructure");

        request.getSession().setAttribute("lstReport", lstReport);
    }

    /**
     * Método para activar el formulario para actualizar un reporte
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void updateReportFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");

        Report reportUPD = new Report();
        reportUPD.setIdReport(Integer.valueOf(idReport));

        AdmReport adm = new AdmReport();
        adm.setReport(reportUPD);
        reportUPD = adm.getReportById();

        request.getSession().removeAttribute("frmSTR");

        request.getSession().setAttribute("frmUPDRPT", "OK");
        request.getSession().setAttribute("idReport", idReport);
        request.getSession().setAttribute("reportUPD", reportUPD);
    }

    /**
     * Método para actualizar un reporte
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void updateReport(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");
        String nombreRPT = (String) hsForm.get("nombreRPT");

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));
        report.setReport(nombreRPT);

        AdmReport adm = new AdmReport();
        adm.setReport(report);
        adm.updateReport();

        List lstStructure = adm.getListStructure();
        if (lstStructure.isEmpty()){
            request.getSession().setAttribute("ADDSTR", "OK");
        }

        List lstReport = (List) adm.getListReport();

        request.getSession().removeAttribute("frmCNFSTR");
        request.getSession().removeAttribute("frmUPDRPT");
        request.getSession().removeAttribute("reportUPD");

        request.getSession().setAttribute("frmSTR", "OK");
        request.getSession().setAttribute("idReport", idReport);
        request.getSession().setAttribute("lstReport", lstReport);
        request.getSession().setAttribute("lstStructure", lstStructure);
    }

    /**
     * Método para limpiar de memoria el formulario para actualizar un reporte
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void cleanUpdateReport(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");
        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));

        AdmReport adm = new AdmReport();
        adm.setReport(report);

        List lstStructure = adm.getListStructure();
        if (lstStructure.isEmpty()){
            request.getSession().setAttribute("ADDSTR", "OK");
        }
        request.getSession().removeAttribute("frmUPDRPT");

        request.getSession().setAttribute("frmSTR", "OK");
        request.getSession().setAttribute("idReport", idReport);
        request.getSession().setAttribute("lstStructure", lstStructure);
    }

    /**
     * Método para activar el formulario para mostrar una estructura
     * @param request
     * @param hsForm
     * @throws ExceptionHandler
     */
    private void structFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");
        if ("0".equals(idReport)){

            request.getSession().removeAttribute("ADDSTR");
            request.getSession().removeAttribute("confSTRMAIN");
            request.getSession().removeAttribute("frmADDCNFSTR");
            request.getSession().removeAttribute("frmADDSTR");
            request.getSession().removeAttribute("frmCNFSTR");
            request.getSession().removeAttribute("frmSTR");
            request.getSession().removeAttribute("idReport");
            request.getSession().removeAttribute("idStructure");
            request.getSession().removeAttribute("lstStructure");

        }else{
            Report report = new Report();
            report.setIdReport(Integer.valueOf(idReport));

            AdmReport adm = new AdmReport();
            adm.setReport(report);

            List lstStructure = adm.getListStructure();

            if (lstStructure.isEmpty()){
                request.getSession().setAttribute("ADDSTR", "OK");
            }

            request.getSession().removeAttribute("confSTRMAIN");
            request.getSession().removeAttribute("frmADDCNFSTR");
            request.getSession().removeAttribute("frmADDSTR");
            request.getSession().removeAttribute("frmCNFSTR");
            request.getSession().removeAttribute("frmUPDSTR");
            request.getSession().removeAttribute("idStructure");

            request.getSession().setAttribute("frmSTR", "OK");
            request.getSession().setAttribute("idReport", idReport);
            request.getSession().setAttribute("lstStructure", lstStructure);
        }
    }

    private void addStructFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        AdmReport adm = new AdmReport();
        List lstTypeStructure = adm.getListTypeStructure();

        request.getSession().removeAttribute("frmADDCNFSTR");
        request.getSession().removeAttribute("frmCNFSTR");
        request.getSession().removeAttribute("frmSTR");
        request.getSession().removeAttribute("frmUPDSTR");

        request.getSession().setAttribute("frmADDSTR", "OK");
        request.getSession().setAttribute("lstTypeStructure", lstTypeStructure);
    }

    private void addStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");
        String nombreSTR = (String) hsForm.get("nombreSTR");
        String idTypeStructure = (String) hsForm.get("idTypeStructure");

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));

        Structure struct = new Structure();
        struct.setIdTypeStructure(Integer.valueOf(idTypeStructure));
        struct.setStructure(nombreSTR);

        AdmReport adm = new AdmReport();
        adm.setReport(report);
        //adm.setConfig(conf);
        adm.setStructure(struct);
        adm.addStructure();

        List lstStructure = adm.getListStructure();
        if (lstStructure.isEmpty()){
            request.getSession().setAttribute("ADDSTR", "OK");
        }

        request.getSession().removeAttribute("frmADDSTR");
        request.getSession().removeAttribute("lstTypeStructure");

        request.getSession().setAttribute("idReport", idReport);
        request.getSession().setAttribute("frmSTR", "OK");
        request.getSession().setAttribute("lstStructure", lstStructure);
    }

    private void cleanAddStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idReport = (String) hsForm.get("idReport");

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));

        AdmReport adm = new AdmReport();
        adm.setReport(report);

        List lstStructure = adm.getListStructure();
        if (lstStructure.isEmpty()){
            request.getSession().setAttribute("ADDSTR", "OK");
        }

        request.getSession().removeAttribute("frmADDSTR");
        request.getSession().removeAttribute("lstTypeStructure");

        request.getSession().setAttribute("frmSTR", "OK");
        request.getSession().setAttribute("idReport", idReport);
        request.getSession().setAttribute("lstStructure", lstStructure);
    }

    private void delStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idReport = (String) hsForm.get("idReport");

        Config conf = new Config();
        conf.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.deleteStructure();

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));
        adm.setReport(report);

        List lstStructure = adm.getListStructure();
        if (lstStructure.isEmpty()){
            request.getSession().setAttribute("ADDSTR", "OK");
        }

        request.getSession().removeAttribute("frmUPDSTR");
        request.getSession().removeAttribute("frmCNFSTR");
        request.getSession().removeAttribute("idStructure");

        request.getSession().setAttribute("lstStructure", lstStructure);
    }

    private void updateStructFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idReport = (String) hsForm.get("idReport");

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));

        Structure struct = new Structure();
        struct.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setReport(report);
        adm.setStructure(struct);

        Structure confSTR = adm.getStructureData();

        List lstTypeStructure = adm.getListTypeStructure();

        request.getSession().removeAttribute("frmCNFSTR");

        request.getSession().setAttribute("confSTR", confSTR);
        request.getSession().setAttribute("lstTypeStructure", lstTypeStructure);
        request.getSession().setAttribute("frmUPDSTR", "OK");
    }

    private void updateStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
                String idReport = (String) hsForm.get("idReport");
                String idStructure = (String) hsForm.get("idStructure");
                String nombreSTR = (String) hsForm.get("nombreSTR");
                String idTypeStructure = (String) hsForm.get("idTypeStructure");
                AdmReport adm = new AdmReport();

                Structure struct = new Structure();
                struct.setIdStructure(Integer.valueOf(idStructure));
                struct.setStructure(nombreSTR);
                struct.setIdTypeStructure(Integer.valueOf(idTypeStructure));

                adm.setStructure(struct);
                adm.updateStructure();

                Report report = new Report();
                report.setIdReport(Integer.valueOf(idReport));
                adm.setReport(report);

                List lstStructure = adm.getListStructure();
                if (lstStructure.isEmpty()){
                    request.getSession().setAttribute("ADDSTR", "OK");
                }

                request.getSession().setAttribute("idReport", idReport);
                request.getSession().setAttribute("lstStructure", lstStructure);

                request.getSession().removeAttribute("idStructure");
                request.getSession().removeAttribute("nombreSTR");
                request.getSession().removeAttribute("idTypeStructure");
                request.getSession().removeAttribute("confSTR");
                request.getSession().removeAttribute("frmUPDSTR");
    }

    private void cleanUpdateStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        request.getSession().removeAttribute("nombreSTR");
        request.getSession().removeAttribute("idTypeStructure");
        request.getSession().removeAttribute("confSTR");
        request.getSession().removeAttribute("frmUPDSTR");
    }
//-------------------------------------
    private void configStructFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idReport = (String) hsForm.get("idReport");
        AdmReport adm = new AdmReport();
        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));
        adm.setReport(report);

        Structure struct = new Structure();
        struct.setIdStructure(Integer.valueOf(idStructure));
        adm.setStructure(struct);

        //elemento seleccionado
        Structure confSTRMAIN = adm.getStructureData();

        //listado de la configuracion
        List lstConfigStructure = adm.getListConfigStructure();

        if ((lstConfigStructure==null)||(lstConfigStructure.isEmpty())){
            request.getSession().setAttribute("ADDCNFSTR", "OK");
            request.getSession().removeAttribute("seeBUTTON");
        }else{
            request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        }

        request.getSession().removeAttribute("frmUPDSTR");
        request.getSession().removeAttribute("idConfigStructure");
        request.getSession().removeAttribute("lstElement");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("frmUPDELEM");
        request.getSession().removeAttribute("frmADDELEM");
        request.getSession().removeAttribute("idElement");
        request.getSession().removeAttribute("btnUPDELEM");
        request.getSession().removeAttribute("lstElement");

        request.getSession().setAttribute("confSTRMAIN", confSTRMAIN);
        request.getSession().setAttribute("idStructure", idStructure);
        request.getSession().setAttribute("lstConfigStructure", lstConfigStructure);
        request.getSession().setAttribute("frmCNFSTR", "OK");
    }

    private void addConfigStructFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idTypeConfig = (String) hsForm.get("idTypeConfig");
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");
        AdmReport adm = new AdmReport();
        adm.setStructure(confSTRMAIN);
        List lstTypeConfig = adm.getListTypeConfig();

        if (idTypeConfig!=null){
            Config conf = new Config();
            conf.setIdTypeConfig(Integer.valueOf(idTypeConfig));
            adm.setConfig(conf);
            List lstTypeValue = adm.getListTypeValue();
            request.getSession().setAttribute("lstTypeValue", lstTypeValue);

            List lstConfigValue = adm.getListConfigValue();

            if ((lstConfigValue==null)||(lstConfigValue.isEmpty())){
                List lstMeasure = adm.getListMeasure();
                request.getSession().setAttribute("lstMeasure", lstMeasure);
                request.getSession().removeAttribute("lstConfigValue");
            }else{
                request.getSession().setAttribute("lstConfigValue", lstConfigValue);
                request.getSession().removeAttribute("lstMeasure");
            }
        }
        request.getSession().removeAttribute("seeBUTTON");
        request.getSession().removeAttribute("frmCNFSTR");

        request.getSession().setAttribute("idTypeConfig", idTypeConfig);
        request.getSession().setAttribute("frmADDCNFSTR", "OK");
        request.getSession().setAttribute("lstTypeConfig", lstTypeConfig);
    }

    private void addConfigStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idTypeConfig = (String) hsForm.get("idTypeConfig");
        String idTypeValue = (String) hsForm.get("idTypeValue");
        String idConfigValue = (String) hsForm.get("idConfigValue");
        String txtConfigValue = (String) hsForm.get("txtConfigValue");
        String idMeasure = (String) hsForm.get("idMeasure");
        String idStructure = (String) hsForm.get("idStructure");
        String idReport = (String) hsForm.get("idReport");

        AdmReport adm = new AdmReport();

        Config conf = new Config();
        conf.setIdStructure(Integer.valueOf(idStructure));
        conf.setIdTypeConfig(Integer.valueOf(idTypeConfig));
        conf.setIdTypeValue(Integer.valueOf(idTypeValue));
        if (idConfigValue!=null){
            conf.setIdConfigValue(Integer.valueOf(idConfigValue));
        }
        conf.setConfigValue((txtConfigValue==null)?"":txtConfigValue);
        if (idMeasure!=null){
            conf.setIdMeasure(Integer.valueOf(idMeasure));
        }
        conf.setIdStructure(Integer.valueOf(idStructure));

        adm.setConfig(conf);
        adm.addConfigStructure();

        Report report = new Report();
        report.setIdReport(Integer.valueOf(idReport));
        adm.setReport(report);

        Structure struct = new Structure();
        struct.setIdStructure(Integer.valueOf(idStructure));
        adm.setStructure(struct);

        //elemento seleccionado
        Structure confSTRMAIN = adm.getStructureData();
        //listado de la configuracion
        List lstConfigStructure = adm.getListConfigStructure();

        if ((lstConfigStructure==null)||(lstConfigStructure.isEmpty())){
            request.getSession().setAttribute("ADDCNFSTR", "OK");
            request.getSession().removeAttribute("seeBUTTON");
        }else{
            request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        }

        request.getSession().removeAttribute("lstTypeValue");
        request.getSession().removeAttribute("lstMeasure");
        request.getSession().removeAttribute("lstConfigValue");
        request.getSession().removeAttribute("idTypeConfig");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("frmADDCNFSTR");
        request.getSession().removeAttribute("ADDCNFSTR");
        request.getSession().setAttribute("confSTRMAIN", confSTRMAIN);
        request.getSession().setAttribute("lstConfigStructure", lstConfigStructure);
        request.getSession().setAttribute("frmCNFSTR", "OK");
    }

    //OK
    private void cleanAddConfigStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        request.getSession().removeAttribute("ADDCNFSTR");
        List lstConfigStructure = (List) request.getSession().getAttribute("lstConfigStructure");

        if ((lstConfigStructure==null)||(lstConfigStructure.isEmpty())){
            request.getSession().setAttribute("ADDCNFSTR", "OK");
            request.getSession().removeAttribute("seeBUTTON");
        }else{
            request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        }

        request.getSession().removeAttribute("lstTypeValue");
        request.getSession().removeAttribute("lstMeasure");
        request.getSession().removeAttribute("lstConfigValue");
        request.getSession().removeAttribute("idTypeConfig");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("frmADDCNFSTR");
        request.getSession().setAttribute("lstConfigStructure", lstConfigStructure);
        request.getSession().setAttribute("frmCNFSTR", "OK");
    }

    private void delConfigStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idConfigStructure = (String) hsForm.get("idConfigStructure");
        Config conf = new Config();
        conf.setIdConfigStructure(Integer.valueOf(idConfigStructure));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.deleteConfigStructure();

        String idStructure = (String) hsForm.get("idStructure");
        conf.setIdStructure(Integer.valueOf(idStructure));
        adm.setConfig(conf);

        Structure struct = new Structure();
        struct.setIdStructure(Integer.valueOf(idStructure));
        adm.setStructure(struct);

        List lstConfigStructure = adm.getListConfigStructure();

        request.getSession().setAttribute("lstConfigStructure", lstConfigStructure);
        request.getSession().setAttribute("frmCNFSTR", "OK");
    }

    private void updateConfigStructBtn(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idConfigStructure = (String) hsForm.get("idConfigStructure");

        request.getSession().removeAttribute("idMeasure");
        request.getSession().removeAttribute("idTypeConfig");
        request.getSession().removeAttribute("idConfigValue");
        request.getSession().removeAttribute("frmUPDCNFSTR");
        request.getSession().removeAttribute("lstConfigValue");
        request.getSession().removeAttribute("lstMeasure");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("lstTypeValue");

        request.getSession().removeAttribute("txtConfigValue");
        request.getSession().removeAttribute("configCNFSTR");

        request.getSession().setAttribute("btnUPDCNF", "btnUPDCNF");
        request.getSession().setAttribute("idConfigStructure", idConfigStructure);
    }

    private void updateConfigStructFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idConfigStructure = (String) hsForm.get("idConfigStructure");
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");
        //RECUPERAR DATOS PARA MODIFICAR
        AdmReport adm = new AdmReport();
        adm.setStructure(confSTRMAIN);
        Config conf = new Config();
        conf.setIdConfigStructure(Integer.valueOf(idConfigStructure));

        adm.setConfig(conf);
        Config configCNFSTR = adm.getConfigStructure();

        String idTypeConfig = configCNFSTR.getIdTypeConfig().toString();
        String idTypeValue = configCNFSTR.getIdTypeValue().toString();

        List lstTypeConfig = adm.getListTypeConfig();
        if (idTypeConfig!=null){

            conf.setIdTypeConfig(Integer.valueOf(idTypeConfig));
            adm.setConfig(conf);
            List lstTypeValue = adm.getListTypeValue();
            request.getSession().setAttribute("lstTypeValue", lstTypeValue);

            List lstConfigValue = adm.getListConfigValue();
            String txtConfigValue = "";
            if ((lstConfigValue==null)||(lstConfigValue.isEmpty())){
                String idMeasure = configCNFSTR.getIdMeasure().toString();
                List lstMeasure = adm.getListMeasure();
                txtConfigValue = configCNFSTR.getConfigValue();
                request.getSession().setAttribute("lstMeasure", lstMeasure);
                request.getSession().setAttribute("idMeasure", idMeasure);
                request.getSession().removeAttribute("lstConfigValue");
            }else{
                String idConfigValue = configCNFSTR.getIdConfigValue().toString();
                request.getSession().setAttribute("lstConfigValue", lstConfigValue);
                request.getSession().setAttribute("idConfigValue", idConfigValue);
                request.getSession().removeAttribute("lstMeasure");
            }
            request.getSession().setAttribute("txtConfigValue", txtConfigValue);
        }

        request.getSession().setAttribute("configCNFSTR", configCNFSTR);
        request.getSession().setAttribute("idTypeConfig", idTypeConfig);
        request.getSession().setAttribute("idTypeValue", idTypeValue);
        request.getSession().setAttribute("frmUPDCNFSTR", "OK");
        request.getSession().setAttribute("lstTypeConfig", lstTypeConfig);
        request.getSession().setAttribute("idConfigStructure", idConfigStructure);

        request.getSession().removeAttribute("seeBUTTON");
    }

    private void updateConfigStructChange(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");

        String idTypeConfig = (String) hsForm.get("idTypeConfig");
        String idConfigStructure = (String) hsForm.get("idConfigStructure");

        //RECUPERAR DATOS PARA MODIFICAR
        AdmReport adm = new AdmReport();
        adm.setStructure(confSTRMAIN);
        
        Config conf = new Config();
        conf.setIdConfigStructure(Integer.valueOf(idConfigStructure));

        adm.setConfig(conf);

        List lstTypeConfig = adm.getListTypeConfig();
        if (idTypeConfig!=null){
            conf.setIdTypeConfig(Integer.valueOf(idTypeConfig));
            adm.setConfig(conf);
            List lstTypeValue = adm.getListTypeValue();
            request.getSession().setAttribute("lstTypeValue", lstTypeValue);

            List lstConfigValue = adm.getListConfigValue();

            if ((lstConfigValue==null)||(lstConfigValue.isEmpty())){
                List lstMeasure = adm.getListMeasure();
                request.getSession().setAttribute("lstMeasure", lstMeasure);
                request.getSession().removeAttribute("lstConfigValue");
            }else{
                request.getSession().setAttribute("lstConfigValue", lstConfigValue);
                request.getSession().removeAttribute("lstMeasure");
            }
        }
        request.getSession().removeAttribute("idTypeValue");
        request.getSession().removeAttribute("idConfigValue");
        request.getSession().removeAttribute("idMeasure");
        request.getSession().removeAttribute("configCNFSTR");

        request.getSession().setAttribute("txtConfigValue", "");
        request.getSession().setAttribute("idTypeConfig", idTypeConfig);
        request.getSession().setAttribute("frmUPDCNFSTR", "OK");
        request.getSession().setAttribute("lstTypeConfig", lstTypeConfig);
        request.getSession().setAttribute("idConfigStructure", idConfigStructure);
    }

    private void updateConfigStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idConfigStructure = (String) hsForm.get("idConfigStructure");
        String idTypeConfig = (String) hsForm.get("idTypeConfig");
        String idTypeValue = (String) hsForm.get("idTypeValue");
        String idConfigValue = (String) hsForm.get("idConfigValue");
        String txtConfigValue = (String) hsForm.get("txtConfigValue");
        String idMeasure = (String) hsForm.get("idMeasure");

        Config conf = new Config();
        conf.setIdConfigStructure(Integer.valueOf(idConfigStructure));
        conf.setIdTypeConfig(Integer.valueOf(idTypeConfig));
        conf.setIdTypeValue(Integer.valueOf(idTypeValue));
        if (idConfigValue!=null){
            conf.setIdConfigValue(Integer.valueOf(idConfigValue));
        }
        if (txtConfigValue!=null){
            conf.setConfigValue(txtConfigValue);
        }else{
            conf.setConfigValue("");
        }
        if (idMeasure!=null){
            conf.setIdMeasure(Integer.valueOf(idMeasure));
        }

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.updateConfigStructure();
        conf.setIdStructure(Integer.valueOf(idStructure));
        adm.setConfig(conf);

        Structure struct = new Structure();
        struct.setIdStructure(Integer.valueOf(idStructure));
        adm.setStructure(struct);

        List lstConfigStructure = adm.getListConfigStructure();

        if ((lstConfigStructure==null)||(lstConfigStructure.isEmpty())){
            request.getSession().setAttribute("ADDCNFSTR", "OK");
            request.getSession().removeAttribute("seeBUTTON");
        }else{
            request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        }

        request.getSession().removeAttribute("idMeasure");
        request.getSession().removeAttribute("idTypeConfig");
        request.getSession().removeAttribute("idConfigValue");
        request.getSession().removeAttribute("frmUPDCNFSTR");
        request.getSession().removeAttribute("lstMeasure");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("lstTypeValue");
        request.getSession().removeAttribute("lstConfigValue");
        request.getSession().removeAttribute("txtConfigValue");
        request.getSession().removeAttribute("configCNFSTR");

        request.getSession().setAttribute("lstConfigStructure", lstConfigStructure);
        request.getSession().setAttribute("frmCNFSTR", "OK");
    }

    private void cleanUpdateConfigStruct(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        List lstConfigStructure = (List) request.getSession().getAttribute("lstConfigStructure");

        if ((lstConfigStructure==null)||(lstConfigStructure.isEmpty())){
            request.getSession().setAttribute("ADDCNFSTR", "OK");
            request.getSession().removeAttribute("seeBUTTON");
        }else{
            request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        }

        request.getSession().removeAttribute("idMeasure");
        request.getSession().removeAttribute("idTypeConfig");
        request.getSession().removeAttribute("idConfigValue");
        request.getSession().removeAttribute("frmUPDCNFSTR");
        request.getSession().removeAttribute("lstMeasure");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("lstTypeValue");
        request.getSession().removeAttribute("lstConfigValue");
        request.getSession().removeAttribute("txtConfigValue");
        request.getSession().removeAttribute("configCNFSTR");
    }
//------------------------------------
    private void seeElement(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        ElementStruct elem = new ElementStruct();
        elem.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setElementStruct(elem);

        List lstElement = adm.getListElement();

        request.getSession().setAttribute("lstElement", lstElement);
        request.getSession().setAttribute("seeBUTTON", "SEEELEM");
    }

    private void cleanSeeElement(HttpServletRequest request)
    {
        request.getSession().setAttribute("seeBUTTON", "OKBUTTON");

        request.getSession().removeAttribute("btnUPDELEM");
        request.getSession().removeAttribute("idElement");
        request.getSession().removeAttribute("idTypeConfigElem");
        request.getSession().removeAttribute("frmADDELEM");
        request.getSession().removeAttribute("frmCNFELEM");
        request.getSession().removeAttribute("frmUPDELEM");

        request.getSession().removeAttribute("lstElement");
        request.getSession().removeAttribute("lstTypeConfig");
        request.getSession().removeAttribute("lstTypeConfigElem");
        request.getSession().removeAttribute("lstTypeValueElem");
        request.getSession().removeAttribute("lstConfigElem");
        request.getSession().removeAttribute("lstConfigValueElem");
    }

    private void addElementFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");

        AdmReport adm = new AdmReport();
        adm.setStructure(confSTRMAIN);

        List lstTypeElement = adm.getListTypeElement();

        request.getSession().removeAttribute("idElement");
        request.getSession().removeAttribute("btnUPDELEM");
        request.getSession().removeAttribute("lstElement");
        request.getSession().removeAttribute("frmUPDELEM");
        request.getSession().removeAttribute("frmCNFELEM");

        request.getSession().setAttribute("frmADDELEM", "OK");
        request.getSession().setAttribute("lstTypeElement", lstTypeElement);
    }

    private void addElement(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idTypeElement = (String) hsForm.get("idTypeElement");
        String txtElementValue = (String) hsForm.get("txtElementValue");

        AdmReport adm = new AdmReport();

        ElementStruct element = new ElementStruct();
        element.setIdStructure(Integer.valueOf(idStructure));
        element.setIdTypeElement(Integer.valueOf(idTypeElement));
        element.setValueElement(txtElementValue);

        adm.setElementStruct(element);
        adm.addElementStruct();

        List lstElement = adm.getListElement();

        request.getSession().setAttribute("lstElement", lstElement);

        request.getSession().removeAttribute("frmADDELEM");
    }

    private void delElement(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idElement = (String) hsForm.get("idElement");

        ElementStruct element = new ElementStruct();
        element.setIdElementStruct(Integer.valueOf(idElement));
        element.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setElementStruct(element);
        adm.deleteElementStruct();

        List lstElement = adm.getListElement();

        request.getSession().setAttribute("lstElement", lstElement);
    }

    private void updateElementFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");
        String idElement = (String) hsForm.get("idElement");
        AdmReport adm = new AdmReport();

        adm.setStructure(confSTRMAIN);

        ElementStruct element = new ElementStruct();
        element.setIdElementStruct(Integer.valueOf(idElement));

        adm.setElementStruct(element);
        element = adm.getElementStructById();

        List lstTypeElement = adm.getListTypeElement();
        request.getSession().setAttribute("lstTypeElement", lstTypeElement);
        request.getSession().setAttribute("idTypeElement", element.getIdTypeElement().toString());
        request.getSession().setAttribute("txtElementValue", (element.getValueElement()==null)?"":element.getValueElement());
        request.getSession().setAttribute("frmUPDELEM", "OK");

        request.getSession().removeAttribute("frmADDELEM");
    }

    private void updateElement(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String idElement = (String) hsForm.get("idElement");
        String idTypeElement = (String) hsForm.get("idTypeElement");
        String txtElementValue = (String) hsForm.get("txtElementValue");

        ElementStruct element = new ElementStruct();
        element.setIdTypeElement(Integer.valueOf(idTypeElement));
        element.setValueElement(txtElementValue);
        element.setIdElementStruct(Integer.valueOf(idElement));
        element.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setElementStruct(element);
        adm.updateElementStruct();

        List lstElement = adm.getListElement();

        request.getSession().setAttribute("lstElement", lstElement);

        request.getSession().removeAttribute("frmUPDELEM");
    }

    private void updateElementBtn(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");

        ElementStruct element = new ElementStruct();
        element.setIdElementStruct(Integer.valueOf(idElement));

        AdmReport adm = new AdmReport();
        adm.setElementStruct(element);
        List lstConfigElem = adm.getListConfigElement();

        request.getSession().setAttribute("idElement", idElement);
        request.getSession().setAttribute("lstConfigElem", lstConfigElem);
        request.getSession().setAttribute("btnUPDELEM", "OK");
        request.getSession().setAttribute("frmCNFELEM", "OK");
    }

    private void updateElementClean(HttpServletRequest request)
    {
        request.getSession().removeAttribute("frmUPDELEM");
    }

//--------------------
    private void addConfigElemFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        AdmReport adm = new AdmReport();
        Structure confSTRMAIN = (Structure) request.getSession().getAttribute("confSTRMAIN");
        adm.setStructure(confSTRMAIN);
        String idElement = (String) hsForm.get("idElement");
        if (idElement!=null){
            ElementStruct elem = new ElementStruct();
            elem.setIdElementStruct(Integer.valueOf(idElement));
            adm.setElementStruct(elem);

            List lstTypeConfigElem = adm.getListTypeConfigElem();
            request.getSession().setAttribute("lstTypeConfigElem", lstTypeConfigElem);
            request.getSession().setAttribute("frmADDCNFELEM", "OK");

            request.getSession().removeAttribute("frmCNFELEM");
        }
    }

    private void addConfigElemChange(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idTypeConfigElem = (String) hsForm.get("idTypeConfigElem");

        ElementStruct elem = new ElementStruct();
        AdmReport adm = new AdmReport();

        if (idTypeConfigElem!=null){
            elem.setIdTypeConfig(Integer.valueOf(idTypeConfigElem));
            adm.setElementStruct(elem);
            List lstTypeValueElem = adm.getListTypeValueElem();

            List lstConfigValueElem = adm.getListConfigValueElem();

            if ((lstConfigValueElem==null)||(lstConfigValueElem.isEmpty())){
                List lstMeasureElem = adm.getListMeasureElem();
                request.getSession().setAttribute("lstMeasureElem", lstMeasureElem);
                request.getSession().removeAttribute("lstConfigValueElem");
            }else{
                request.getSession().setAttribute("lstConfigValueElem", lstConfigValueElem);
                request.getSession().removeAttribute("lstMeasureElem");
            }
            request.getSession().setAttribute("lstTypeValueElem", lstTypeValueElem);
        }
        request.getSession().setAttribute("idTypeConfigElem", idTypeConfigElem);
    }

    private void addConfigElem(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");
        String idTypeConfigElem = (String) hsForm.get("idTypeConfigElem");
        String idTypeValueElem = (String) hsForm.get("idTypeValueElem");
        String idConfigValueElem = (String) hsForm.get("idConfigValueElem");
        String txtConfigValueElem = (String) hsForm.get("txtConfigValueElem");
        String idMeasureElem = (String) hsForm.get("idMeasureElem");

        ElementStruct elem = new ElementStruct();
        elem.setIdElementStruct(Integer.valueOf(idElement));
        elem.setIdTypeConfig(Integer.valueOf(idTypeConfigElem));
        elem.setIdTypeValue(Integer.valueOf(idTypeValueElem));
        if (idConfigValueElem!=null){
            elem.setIdConfigValue(Integer.valueOf(idConfigValueElem));
        }
        if (idMeasureElem!=null){
            elem.setIdMeasure(Integer.valueOf(idMeasureElem));
        }
        elem.setConfigValue(txtConfigValueElem);

        AdmReport adm = new AdmReport();
        adm.setElementStruct(elem);
        adm.addConfigElement();

        List lstConfigElem = adm.getListConfigElement();

        request.getSession().setAttribute("lstConfigElem", lstConfigElem);
        request.getSession().setAttribute("frmCNFELEM", "OK");

        cleanAddConfigElem(request,hsForm);
    }

    private void cleanAddConfigElem(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");

        ElementStruct element = new ElementStruct();
        element.setIdElementStruct(Integer.valueOf(idElement));

        AdmReport adm = new AdmReport();
        adm.setElementStruct(element);
        List lstConfigElem = adm.getListConfigElement();

        request.getSession().setAttribute("idElement", idElement);
        request.getSession().setAttribute("lstConfigElem", lstConfigElem);
        request.getSession().setAttribute("btnUPDELEM", "OK");
        request.getSession().setAttribute("frmCNFELEM", "OK");

        request.getSession().removeAttribute("frmADDCNFELEM");

        request.getSession().removeAttribute("lstTypeConfigElem");
        request.getSession().removeAttribute("lstTypeValueElem");
        request.getSession().removeAttribute("lstConfigValueElem");
        request.getSession().removeAttribute("lstMeasureElem");

        request.getSession().removeAttribute("idTypeConfigElem");
        request.getSession().removeAttribute("idTypeValueElem");
        request.getSession().removeAttribute("idConfigValueElem");
        request.getSession().removeAttribute("idMeasureElem");

        request.getSession().removeAttribute("txtConfigValueElem");
    }

    private void delConfigElem(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");
        String idConfigElement = (String) hsForm.get("idConfigElement");
        
        if (idConfigElement!=null){
            ElementStruct elem = new ElementStruct();
            elem.setIdElementStruct(Integer.valueOf(idElement));
            elem.setIdConfigElement(Integer.valueOf(idConfigElement));

            AdmReport adm = new AdmReport();
            adm.setElementStruct(elem);

            adm.deleteConfigElement();

            List lstConfigElem = adm.getListConfigElement();
            request.getSession().setAttribute("lstConfigElem", lstConfigElem);
            request.getSession().setAttribute("frmCNFELEM", "OK");
        }
    }

    private void updateConfigElemFrm(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");
        String idConfigElement = (String) hsForm.get("idConfigElement");

        if (idConfigElement!=null){
            request.getSession().setAttribute("idConfigElement", idConfigElement);

            ElementStruct elem = new ElementStruct();
            elem.setIdElementStruct(Integer.valueOf(idElement));
            elem.setIdConfigElement(Integer.valueOf(idConfigElement));

            AdmReport adm = new AdmReport();
            adm.setElementStruct(elem);

            ElementStruct elemCNF = adm.getConfigElement();

            List lstConfigElem = adm.getListConfigElement();
            request.getSession().setAttribute("lstConfigElem", lstConfigElem);

            String idTypeConfigElem = elemCNF.getIdTypeConfig().toString();
            String idTypeValueElem = elemCNF.getIdTypeValue().toString();
            if (elemCNF.getIdConfigValue()!=null){
                String idConfigValueElem = elemCNF.getIdConfigValue().toString();
                request.getSession().setAttribute("idConfigValueElem", idConfigValueElem);
            }
            String txtConfigValueElem = elemCNF.getConfigValue();
            if (elemCNF.getIdMeasure()!=null){
                String idMeasureElem = elemCNF.getIdMeasure().toString();
                request.getSession().setAttribute("idMeasureElem", idMeasureElem);
            }

            request.getSession().setAttribute("idTypeConfigElem", idTypeConfigElem);
            request.getSession().setAttribute("idTypeValueElem", idTypeValueElem);
            request.getSession().setAttribute("txtConfigValueElem", txtConfigValueElem);

            List lstTypeConfigElem = adm.getListTypeConfigElem();
            request.getSession().setAttribute("lstTypeConfigElem", lstTypeConfigElem);

            if (idTypeConfigElem!=null){
                elem.setIdTypeConfig(elemCNF.getIdTypeConfig());
                adm.setElementStruct(elem);
                List lstTypeValueElem = adm.getListTypeValueElem();

                List lstConfigValueElem = adm.getListConfigValueElem();

                if ((lstConfigValueElem==null)||(lstConfigValueElem.isEmpty())){
                    List lstMeasureElem = adm.getListMeasureElem();
                    request.getSession().setAttribute("lstMeasureElem", lstMeasureElem);
                    request.getSession().removeAttribute("lstConfigValueElem");
                }else{
                    request.getSession().setAttribute("lstConfigValueElem", lstConfigValueElem);
                    request.getSession().removeAttribute("lstMeasureElem");
                }
                request.getSession().setAttribute("lstTypeValueElem", lstTypeValueElem);
            }

            request.getSession().setAttribute("idConfigElement", idConfigElement);
            request.getSession().setAttribute("idElement", idElement);
            request.getSession().setAttribute("elemCNF", elemCNF);
            request.getSession().setAttribute("frmUPDCNFELEM", "OK");
        }
    }

    private void updateConfigElemChange(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idConfigElement = (String) hsForm.get("idConfigElement");
        String idTypeConfigElem = (String) hsForm.get("idTypeConfigElem");

        AdmReport adm = new AdmReport();
        ElementStruct elem = new ElementStruct();
        elem.setIdTypeConfig(Integer.valueOf(idTypeConfigElem));

        if (idTypeConfigElem!=null){
            elem.setIdTypeConfig(Integer.valueOf(idTypeConfigElem));
            adm.setElementStruct(elem);
            List lstTypeValueElem = adm.getListTypeValueElem();

            List lstConfigValueElem = adm.getListConfigValueElem();

            if ((lstConfigValueElem==null)||(lstConfigValueElem.isEmpty())){
                List lstMeasureElem = adm.getListMeasureElem();
                request.getSession().setAttribute("lstMeasureElem", lstMeasureElem);
                request.getSession().removeAttribute("lstConfigValueElem");
            }else{
                request.getSession().setAttribute("lstConfigValueElem", lstConfigValueElem);
                request.getSession().removeAttribute("lstMeasureElem");
            }
            request.getSession().setAttribute("lstTypeValueElem", lstTypeValueElem);
            request.getSession().setAttribute("idTypeConfigElem", idTypeConfigElem);
        }

        request.getSession().setAttribute("idConfigElement", idConfigElement);

        request.getSession().removeAttribute("idMeasureElem");
        request.getSession().removeAttribute("idTypeValueElem");
        request.getSession().removeAttribute("idConfigValueElem");
        request.getSession().removeAttribute("txtConfigValueElem");
    }

    private void updateConfigElem(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idElement = (String) hsForm.get("idElement");
        String idConfigElement = (String) hsForm.get("idConfigElement");
        String idTypeConfigElem = (String) hsForm.get("idTypeConfigElem");
        String idTypeValueElem = (String) hsForm.get("idTypeValueElem");
        String idConfigValueElem = (String) hsForm.get("idConfigValueElem");
        String txtConfigValueElem = (String) hsForm.get("txtConfigValueElem");
        String idMeasureElem = (String) hsForm.get("idMeasureElem");

        ElementStruct elem = new ElementStruct();
        elem.setIdElementStruct(Integer.valueOf(idElement));
        elem.setIdConfigElement(Integer.valueOf(idConfigElement));
        elem.setIdTypeConfig(Integer.valueOf(idTypeConfigElem));
        elem.setIdTypeValue(Integer.valueOf(idTypeValueElem));
        if (idConfigValueElem!=null){
            elem.setIdConfigValue(Integer.valueOf(idConfigValueElem));
        }
        if (idMeasureElem!=null){
            elem.setIdMeasure(Integer.valueOf(idMeasureElem));
        }
        elem.setConfigValue(txtConfigValueElem);

        AdmReport adm = new AdmReport();
        adm.setElementStruct(elem);
        adm.updateConfigElement();

        List lstConfigElem = adm.getListConfigElement();
        request.getSession().setAttribute("lstConfigElem", lstConfigElem);
    }

    private void cleanUpdateConfigElem(HttpServletRequest request) throws ExceptionHandler
    {
        request.getSession().removeAttribute("frmUPDCNFELEM");

        request.getSession().removeAttribute("lstTypeConfigElem");
        request.getSession().removeAttribute("lstTypeValueElem");
        request.getSession().removeAttribute("lstConfigValueElem");
        request.getSession().removeAttribute("lstMeasureElem");

        request.getSession().removeAttribute("idTypeConfigElem");
        request.getSession().removeAttribute("idTypeValueElem");
        request.getSession().removeAttribute("idConfigValueElem");
        request.getSession().removeAttribute("idMeasureElem");

        request.getSession().removeAttribute("txtConfigValueElem");
    }
//--------------------------------------
    private void seeQuery(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");

        Config conf = new Config();
        conf.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        Config configQUERY = adm.getQueryConfig();

        request.getSession().setAttribute("configQUERY", configQUERY);
        request.getSession().setAttribute("seeBUTTON", "SEEQUERY");
    }

    private void addQuery(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idStructure = (String) hsForm.get("idStructure");
        String txtQuery = (String) hsForm.get("txtQuery");
        String isExtern = (String) hsForm.get("isExtern");

        Config conf = new Config();
        conf.setIdStructure(Integer.valueOf(idStructure));
        conf.setQuery((txtQuery==null)?"":txtQuery);
        conf.setIsExtern(Integer.valueOf(isExtern));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.addQueryStructure();

        Config configQUERY = adm.getQueryConfig();

        request.getSession().setAttribute("configQUERY", configQUERY);
        request.getSession().setAttribute("seeBUTTON", "SEEQUERY");
    }

    private void updateQuery(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idQueryReport = (String) hsForm.get("idQueryReport");
        String idStructure = (String) hsForm.get("idStructure");
        String txtQuery = (String) hsForm.get("txtQuery");
        String isExtern = (String) hsForm.get("isExtern");

        Config conf = new Config();
        conf.setIdQuery(Integer.valueOf(idQueryReport));
        conf.setIdStructure(Integer.valueOf(idStructure));
        conf.setQuery((txtQuery==null)?"":txtQuery);
        conf.setIsExtern(Integer.valueOf(isExtern));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.updateQueryStructure();

        Config configQUERY = adm.getQueryConfig();

        request.getSession().setAttribute("configQUERY", configQUERY);
        request.getSession().setAttribute("seeBUTTON", "SEEQUERY");
    }

    private void delQuery(HttpServletRequest request, HashMap hsForm) throws ExceptionHandler
    {
        String idQueryReport = (String) hsForm.get("idQueryReport");
        String idStructure = (String) hsForm.get("idStructure");

        Config conf = new Config();
        conf.setIdQuery(Integer.valueOf(idQueryReport));
        conf.setIdStructure(Integer.valueOf(idStructure));

        AdmReport adm = new AdmReport();
        adm.setConfig(conf);
        adm.deleteQueryStructure();

        Config configQUERY = adm.getQueryConfig();

        request.getSession().setAttribute("configQUERY", configQUERY);
        request.getSession().setAttribute("seeBUTTON", "SEEQUERY");
    }

    private void cleanSeeQuery(HttpServletRequest request)
    {
        request.getSession().setAttribute("seeBUTTON", "OKBUTTON");
        request.getSession().removeAttribute("configQUERY");
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
