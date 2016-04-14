/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.servlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import mx.riyoce.odin.entities.Attachment;
import mx.riyoce.odin.entities.Correo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author admin
 */
@WebServlet(name = "SendGenericMail", urlPatterns = {"/SendGenericMail"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5, // 5MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)      // 50MB
public class SendGenericMail extends HttpServlet {

    @Resource(name = "jms/OdinCorreosQueue")
    private Queue OdinQueue;
    @Resource(name = "jms/OdinCorreosQueueFactory")
    private ConnectionFactory OdinConnectionFactory;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Properties props = new Properties();
            props.load(getServletContext().getResourceAsStream("/WEB-INF/genericMails.properties"));

            String clave = request.getParameter("clave");

            String[] emails = props.getProperty(clave).split(",");
            List<Attachment> attachments = new LinkedList<>();

            for (Part file : request.getParts()) {                
                String fileName = getFileName(file);                
                if (!fileName.equals("")) {
                    Attachment a = new Attachment();
                    a.setFilename(fileName);
                    System.out.println(file.getContentType());
                    a.setMime(file.getContentType());
                    a.setContent(IOUtils.toByteArray(file.getInputStream()));
                    attachments.add(a);
                }                                                                               
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<div style=\"width: 100%; height: 50px; line-height: 50px; text-align: center; background-color: rgb(2, 2, 126); color: #FFF; font-size: 25px; font-family: Helvetica;\">");
            sb.append("Nueva solicitud de ");
            sb.append(request.getParameter("acerca"));
            sb.append(" - ");
            sb.append(request.getParameter("name_agencia"));
            sb.append("</div>");
            sb.append("<br/>");
            sb.append("<br/>");

            Map params = request.getParameterMap();
            Iterator i = params.keySet().iterator();

            while (i.hasNext()) {
                String key = (String) i.next();
                if (key.contains("data_")) {
                    String value = ((String[]) params.get(key))[0];
                    sb.append("<b>");
                    sb.append(key.replace("data_", ""));
                    sb.append(":</b> ");
                    sb.append(value);
                    sb.append("<br/>");
                }
            }

            for (String email : emails) {
                Correo m = new Correo();
                m.setType("text/html");
                m.setFecha(new Date());
                m.setAcerca(request.getParameter("acerca"));
                m.setNombreDe("Alertas " + request.getParameter("name_agencia"));
                m.setMailDe("noreply@picacho.com.mx");

                m.setNombrePara("Ejecutivo " + request.getParameter("name_agencia"));
                m.setMailPara(email);

                m.setCuerpo(sb.toString());

                m.setAttachments(attachments);

                sendJMSMessageToMensajesQueue(m);
            }

            response.sendRedirect("gracias-por-tu-informacion.xhtml");

        } catch (IOException | ServletException | JMSException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al enviar el mail generico", e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= " + contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private Message createJMSMessageForjmsMensajesQueue(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage tm = session.createObjectMessage();
        tm.setObject((Serializable) messageData);
        return tm;
    }

    private void sendJMSMessageToMensajesQueue(Object messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = OdinConnectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(OdinQueue);
            messageProducer.send(createJMSMessageForjmsMensajesQueue(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
