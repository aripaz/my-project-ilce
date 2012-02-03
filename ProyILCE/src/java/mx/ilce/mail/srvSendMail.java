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
package mx.ilce.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.ilce.component.AdminForm;
import mx.ilce.component.AdminXML;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.Validation;

/**
 * Servlet implementado para el envío de mail
 * @author ccatrilef
 */
public class srvSendMail extends HttpServlet {
   
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
        Validation val = new Validation();
        String PagDispacher = (String) request.getSession().getAttribute("PagDispacher");
        try {
            String GOTSESS = (String) request.getSession().getAttribute("GOTSESS");

            if ((!val.validateUser(request)) && (GOTSESS==null)) {
                val.executeErrorValidationUser(this.getClass(), request, response);
            }else{
                AdminForm admForm = new AdminForm();
                HashMap hs = null;
                HashMap hsForm = new HashMap();

                if ((GOTSESS!=null) && ("OK".equals(GOTSESS))){
                    hsForm = getDataFromSession(request);
                }else{
                    hs = admForm.getFormulario(request);
                    hsForm = (HashMap) hs.get("FORM");
                }

                String origen = (String) hsForm.get("from");
                String destino = (String) hsForm.get("to");
                String copia = (String) hsForm.get("copy");
                String copiaO = (String) hsForm.get("copyO");
                String asunto = (String) hsForm.get("subject");
                String texto = (String) hsForm.get("message");

                ArrayList arrVal = new ArrayList();
                arrVal.add("from");
                arrVal.add("to");
                arrVal.add("subject");
                arrVal.add("message");

                List lstVal = val.validationForm(arrVal, hsForm);
                String blOK = (String) lstVal.get(0);
                if ("false".equals(blOK)){
                        val.executeErrorValidation(lstVal, this.getClass(), request, response);
                }else{
                    UtilMail utMail = new UtilMail();
                    Properties props = UtilMail.leerConfig();
                    Session session = Session.getDefaultInstance(props);
                    session.setDebug(true);
                    String separador = props.getProperty("separador");

                    Address[] addressDest = utMail.getAddressFromText(destino, separador);
                    Address[] addressCopia = utMail.getAddressFromText(copia, separador);
                    Address[] addressCopiaO = utMail.getAddressFromText(copiaO, separador);

                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(origen));
                    
                    if (addressDest!=null){
                        message.addRecipients(Message.RecipientType.TO,addressDest);
                    }else{
                        destino = (destino==null)?"":destino;
                        destino = destino.trim();
                        if (!"".equals(destino)){
                            message.addRecipient(Message.RecipientType.TO,new InternetAddress(destino));
                        }
                    }
                    if (addressCopia!=null){
                        message.addRecipients(Message.RecipientType.CC,addressCopia);
                    }else{
                        copia = (copia==null)?"":copia;
                        copia = copia.trim();
                        if (!"".equals(copia)){
                            message.addRecipient(Message.RecipientType.CC,new InternetAddress(copia));
                        }
                    }
                    if (addressCopiaO!=null){
                        message.addRecipients(Message.RecipientType.BCC,addressCopiaO);
                    }else{
                        copiaO = (copiaO==null)?"":copiaO;
                        copiaO = copiaO.trim();
                        if (!"".equals(copiaO)){
                            message.addRecipient(Message.RecipientType.BCC,new InternetAddress(copiaO));
                        }
                    }
                    message.setSubject(asunto);
                    message.setText(texto);

                    // Lo enviamos.
                    Transport t = session.getTransport("smtp");
                    String strAuth = props.getProperty("mail.smtp.auth");
                    if ("true".equals(strAuth)){
                        t.connect(props.getProperty("mail.smtp.user"),props.getProperty("pass"));
                        t.sendMessage(message, message.getAllRecipients());
                        t.close();
                    }else{
                        Transport.send(message, message.getAllRecipients());
                    }
                    
                    String strMAILPK = (String) request.getSession().getAttribute("MAILPK");
                    String strMAILBITAC = (String) request.getSession().getAttribute("MAILBITAC");
                    
                    // Cierre.
                    cleanMemory(request);

                    AdminXML admXML = new AdminXML();

                    if ((strMAILPK!=null)&&(!"".equals(strMAILPK)) &&
                        (strMAILBITAC!=null)&&(!"".equals(strMAILBITAC))){
                        request.getSession().setAttribute("xmlTab",
                                admXML.salidaXMLBitacora(strMAILPK,strMAILBITAC));
                    }else{
                        //request.getSession().setAttribute("xmlTab",
                        //        admXML.salidaXML(String.valueOf("MAIL ENVIADO")));
                        request.getSession().setAttribute("xmlTab",
                                admXML.salidaXMLResponse("Se envió su contraseña a su correo registrado; revise su correo y siga las instrucciones"));
                    }
                    /*if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                        LoginHandler lg = new LoginHandler();
                        lg.setTextExecution("Mail Enviado, revice su correo");
                        request.getSession().setAttribute("loginHand",lg);
                        cleanMemory(request);
                        request.getSession().removeAttribute("xmlTab");
                        request.getRequestDispatcher(PagDispacher).forward(request, response);
                    }else{
                        request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                    }*/
                    request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                }
            }
        }catch (ExceptionHandler eh){
            AdminXML admXML = new AdminXML();            
            request.getSession().setAttribute("xmlTab",
                        admXML.salidaXMLResponse("Se ha producido un error en el envío del correo"));
            request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            /*
            if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                LoginHandler lg = new LoginHandler();
                lg.setTextExecution(eh.getXmlError().toString());
                request.getSession().setAttribute("loginHand",lg);
                cleanMemory(request);
                request.getRequestDispatcher(PagDispacher).forward(request, response);
            }else{
                try{
                    val.executeErrorHandler(eh,request, response);
                }catch (Exception es){
                    val.setTextMessage("Problemas en la execución del Error de srvSendMail");
                    val.executeErrorException(es, request, response);
                }
            }*/
        }catch(Exception e){
            AdminXML admXML = new AdminXML();            
            request.getSession().setAttribute("xmlTab",
                        admXML.salidaXMLResponse("Se ha producido un error en el envío del correo"));
            request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
            /*
            if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                LoginHandler lg = new LoginHandler();
                lg.setTextExecution("Error al procesar el envío de Mail");
                request.getSession().setAttribute("loginHand",lg);
                cleanMemory(request);
                request.getRequestDispatcher(PagDispacher).forward(request, response);
            }else{
                val.setTextMessage("Problemas en la execución de srvSendMail");
                val.executeErrorException(e, request, response);
            }*/
        }
    } 

    /**
     * Método para limpiar de memoria los datos asociados al mail. Nos asegura
     * que ante una nueva invocación no se tomen datos de un mail anterior.
     * @param request
     */
    private void cleanMemory(HttpServletRequest request){
        request.getSession().removeAttribute("from");
        request.getSession().removeAttribute("to");
        request.getSession().removeAttribute("subject");
        request.getSession().removeAttribute("message");
        request.getSession().removeAttribute("copy");
        request.getSession().removeAttribute("copyO");
        request.getSession().removeAttribute("GOTSESS");
        request.getSession().removeAttribute("e_mail");
        request.getSession().removeAttribute("msgExist");
        request.getSession().removeAttribute("MAILPK");
        request.getSession().removeAttribute("MAILBITAC");
    }

    /**
     * Obtiene desde la Session los datos requeridos para enviar un mail
     * @param request
     * @return  HashMap     HashMap con los datos recuperados
     */
    private HashMap getDataFromSession(HttpServletRequest request){
        HashMap hs = new HashMap();

        String strFrom = (String) request.getSession().getAttribute("from");
        String strTo = (String) request.getSession().getAttribute("to");
        String strSubject = (String) request.getSession().getAttribute("subject");
        String strMessage = (String) request.getSession().getAttribute("message");
        String strCopy = (String) request.getSession().getAttribute("copy");
        String strCopyO = (String) request.getSession().getAttribute("copyO");

        if (strFrom!=null){
            hs.put("from", strFrom);
        }
        if (strTo!= null){
            hs.put("to", strTo);
        }
        if (strSubject!=null){
            hs.put("subject", strSubject);
        }
        if (strMessage!=null){
            hs.put("message", strMessage);
        }
        if (strCopy!=null){
            hs.put("copy", strCopy);
        }
        if (strCopyO!=null){
            hs.put("copyO", strCopyO);
        }
        return hs;
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
        return "Servlet implementado para el envío de mail";
    }
}
