/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import mx.riyoce.odin.entities.Auto;
import mx.riyoce.odin.entities.Correo;
import mx.riyoce.odin.entities.Marca;
import mx.riyoce.odin.entities.Modelo;
import mx.riyoce.odin.entities.Solicitud;
import mx.riyoce.odin.sessions.AdminSessionBean;
import mx.riyoce.odin.sessions.AutosSessionBean;
import mx.riyoce.odin.sessions.SolicitudesSessionBean;

/**
 *
 * @author admin
 */
@SessionScoped
@Named
public class SolicitudController implements Serializable {

    @Resource(name = "jms/OdinCorreosQueue")
    private Queue OdinQueue;
    @Resource(name = "jms/OdinCorreosQueueFactory")
    private ConnectionFactory OdinConnectionFactory;

    @EJB
    private SolicitudesSessionBean ssb;
    @EJB
    private AutosSessionBean asb;
    @EJB
    private AdminSessionBean adminsb;

    @Inject
    private SiteController sc;

    private Marca marca;
    
    private Solicitud solicitud;
    private Date fechaCita;
    private long modelid;
    private Modelo modelo;
    private Auto autoSolicitud;
    private String placas;
    private boolean servicioValet;
    private boolean aceptarTerminosCondiciones;

    public SolicitudController() {
        solicitud = new Solicitud();
        fechaCita = new Date();
        servicioValet = false;
        aceptarTerminosCondiciones = false;
    }
    
    public void passModelo(){
        this.modelo = asb.getModeloById(modelid);
    }
    
    public void passAuto(){
        this.modelo = asb.getModeloById(modelid);
    }

    public void generarSolicitudCotizacionAutosNuevos() {
        try {
            if (aceptarTerminosCondiciones) {
                solicitud.setAgencia(sc.getAgencia());
                solicitud.setTipo(ssb.getTiposolicitudByClave("coti_autos_nuevos"));
                solicitud.setFecha(new Date());

                StringBuilder sb = new StringBuilder();

                sb.append("<b>Auto: </b>");
                sb.append(autoSolicitud.getNombre());
                sb.append(" ");
                sb.append(autoSolicitud.getModelo().getAno());
                sb.append("<br/>");

                solicitud.setDescripcionSolicitud(sb.toString());

                List<Correo> lm = ssb.crearSolicitud(solicitud);

                for (Correo m : lm) {
                    sendJMSMessageToMensajesQueue(m);
                }
                clearInfoSolicitud();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "¡ Gracias por tu información ! Nos pondremos en contacto.", null));
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes aceptar los terminos y condiciones", null));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al generar la solicitud de cotizacion", e);
        }
    }

    public void generarSolicitudCitaServicio(boolean placas, boolean valet) {
        try {
            if (aceptarTerminosCondiciones) {
                solicitud.setTipo(ssb.getTiposolicitudByClave("cita_servicio"));
                solicitud.setFecha(new Date());

                StringBuilder sb = new StringBuilder();

                sb.append("<b>Auto: </b>");
                sb.append(autoSolicitud.getNombre());
                sb.append("<br/>");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                sb.append("<b>Fecha: </b>");
                sb.append(sdf.format(fechaCita));
                sb.append("<br/>");

                if (placas) {
                    sb.append("<b>Placas: </b>");
                    sb.append(placas);
                    sb.append("<br/>");
                }

                if (valet) {
                    sb.append("<b>Servicio de Valet: </b>");
                    sb.append(valet ? "Sí" : "No");
                    sb.append("<br/>");
                }

                solicitud.setDescripcionSolicitud(sb.toString());

                List<Correo> lm = ssb.crearSolicitud(solicitud);

                for (Correo m : lm) {
                    sendJMSMessageToMensajesQueue(m);
                }
                clearInfoSolicitud();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "¡ Gracias por tu información ! Nos pondremos en contacto.", null));
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes aceptar los terminos y condiciones", null));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al generar la solicitud de cotizacion", e);
        }
    }
    
    public void generarSolicitudContactoGeneral() {
        try {
            if (aceptarTerminosCondiciones) {
                solicitud.setTipo(ssb.getTiposolicitudByClave("contacto_general"));
                solicitud.setFecha(new Date());
                                                               
                solicitud.setDescripcionSolicitud("");

                List<Correo> lm = ssb.crearSolicitud(solicitud);

                for (Correo m : lm) {
                    sendJMSMessageToMensajesQueue(m);
                }
                clearInfoSolicitud();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "¡ Gracias por tu información ! Nos pondremos en contacto.", null));
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes aceptar los terminos y condiciones", null));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al generar la solicitud de cotizacion", e);
        }
    }

    public void clearInfoSolicitud() {
        solicitud = new Solicitud();
        fechaCita = new Date();
        placas = "";
        modelo = new Modelo();
        autoSolicitud = new Auto();
        servicioValet = false;
        aceptarTerminosCondiciones = false;
    }
    
    public List<Marca> getAllMarcas(){
        return adminsb.getMarcas();
    }        
    
    public List<Modelo> getModelosByMarca(){
        return asb.getModelosByMarca(marca);
    }

    public List<Auto> getAutosByModel() {
        return asb.getAutosByModelo(modelo);
    }

    public List<Auto> getAllAutos() {
        return asb.getAllAutos();
    }

    public Date getToday() {
        return new Date();
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

    /**
     * @return the solicitud
     */
    public Solicitud getSolicitud() {
        return solicitud;
    }

    /**
     * @param solicitud the solicitud to set
     */
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    /**
     * @return the fechaCita
     */
    public Date getFechaCita() {
        return fechaCita;
    }

    /**
     * @param fechaCita the fechaCita to set
     */
    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the autoSolicitud
     */
    public Auto getAutoSolicitud() {
        return autoSolicitud;
    }

    /**
     * @param autoSolicitud the autoSolicitud to set
     */
    public void setAutoSolicitud(Auto autoSolicitud) {
        this.autoSolicitud = autoSolicitud;
    }

    /**
     * @return the placas
     */
    public String getPlacas() {
        return placas;
    }

    /**
     * @param placas the placas to set
     */
    public void setPlacas(String placas) {
        this.placas = placas;
    }

    /**
     * @return the servicioValet
     */
    public boolean isServicioValet() {
        return servicioValet;
    }

    /**
     * @param servicioValet the servicioValet to set
     */
    public void setServicioValet(boolean servicioValet) {
        this.servicioValet = servicioValet;
    }

    /**
     * @return the aceptarTerminosCondiciones
     */
    public boolean isAceptarTerminosCondiciones() {
        return aceptarTerminosCondiciones;
    }

    /**
     * @param aceptarTerminosCondiciones the aceptarTerminosCondiciones to set
     */
    public void setAceptarTerminosCondiciones(boolean aceptarTerminosCondiciones) {
        this.aceptarTerminosCondiciones = aceptarTerminosCondiciones;
    }

    /**
     * @return the modelid
     */
    public long getModelid() {
        return modelid;
    }

    /**
     * @param modelid the modelid to set
     */
    public void setModelid(long modelid) {
        this.modelid = modelid;
    }

    /**
     * @return the marca
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

}
