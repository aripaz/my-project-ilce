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
 * Servlet implementado para el envio de mail
 * @author ccatrilef
 */
public class srvSendMail extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
                    // Cierre.
                    cleanMemory(request);

                    AdminXML admXML = new AdminXML();
                    request.getSession().setAttribute("xmlTab",admXML.salidaXML(String.valueOf("MAIL ENVIADO")));

                    if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                        request.getRequestDispatcher(PagDispacher).forward(request, response);
                    }else{
                        request.getRequestDispatcher("/resource/jsp/xmlTab.jsp").forward(request, response);
                    }
                }
            }
        }catch (ExceptionHandler eh){
            if ((PagDispacher!=null)&&(!"".equals(PagDispacher))){
                request.getSession().setAttribute("xmlTab",eh.getXmlError());
                request.getRequestDispatcher(PagDispacher).forward(request, response);
            }else{
                try{
                    val.executeErrorHandler(eh,request, response);
                }catch (Exception es){
                    val.setTextMessage("Problemas en la execucion del Error de srvForma");
                    val.executeErrorException(es, request, response);
                }
            }
        }catch(Exception e){
            val.setTextMessage("Problemas en la execucion de srvForma");
            val.executeErrorException(e, request, response);
        } finally {
        }
    } 

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
    }

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
