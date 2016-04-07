/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author ricardo
 */
@Entity
public class Correo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreDe;
    private String mailDe;
    private String nombrePara;    
    private String mailPara;

    @Lob @Basic(fetch=EAGER)
    private String cuerpo;
    private String type;
    private String acerca;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;

    @OneToMany(mappedBy="correo", cascade=CascadeType.ALL)
    private List<Attachment> attachments;
    
    @ManyToOne
    private Solicitud solicitud;
    
    public Correo(){
        attachments = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Correo)) {
            return false;
        }
        Correo other = (Correo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    /**
     * @return the nombreDe
     */
    public String getNombreDe() {
        return nombreDe;
    }

    /**
     * @param nombreDe the nombreDe to set
     */
    public void setNombreDe(String nombreDe) {
        this.nombreDe = nombreDe;
    }

    /**
     * @return the mailDe
     */
    public String getMailDe() {
        return mailDe;
    }

    /**
     * @param mailDe the mailDe to set
     */
    public void setMailDe(String mailDe) {
        this.mailDe = mailDe;
    }

    /**
     * @return the nombrePara
     */
    public String getNombrePara() {
        return nombrePara;
    }

    /**
     * @param nombrePara the nombrePara to set
     */
    public void setNombrePara(String nombrePara) {
        this.nombrePara = nombrePara;
    }

    /**
     * @return the mailPara
     */
    public String getMailPara() {
        return mailPara;
    }

    /**
     * @param mailPara the mailPara to set
     */
    public void setMailPara(String mailPara) {
        this.mailPara = mailPara;
    }

    /**
     * @return the cuerpo
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * @param cuerpo the cuerpo to set
     */
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the acerca
     */
    public String getAcerca() {
        return acerca;
    }

    /**
     * @param acerca the acerca to set
     */
    public void setAcerca(String acerca) {
        this.acerca = acerca;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
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
    
}
