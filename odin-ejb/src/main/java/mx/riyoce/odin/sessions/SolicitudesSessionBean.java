/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.sessions;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.riyoce.odin.entities.Solicitud;
import mx.riyoce.odin.entities.TipoSolicitud;

/**
 *
 * @author ricardo
 */

@Stateless
public class SolicitudesSessionBean implements Serializable{

    @PersistenceContext(unitName = "mx.riyoce_odin-ejb_ejb_1.0PU")
    private EntityManager em;
        
    public void crearSolicitud(Solicitud s){
        try {
            em.persist(s);            
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la solicitud", e);
        }
    }
    
    public TipoSolicitud getTipoSolicitudById(long id){
        try {
            return em.find(TipoSolicitud.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al trar el tipo de solicitud por id", e);
            return null;
        }
    }
    
}
