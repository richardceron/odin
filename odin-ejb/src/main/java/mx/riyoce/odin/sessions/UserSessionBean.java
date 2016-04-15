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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.riyoce.odin.entities.Usuario;

/**
 *
 * @author admin
 */
@Stateless
public class UserSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_odin-ejb_ejb_1.0PU")
    private EntityManager em;

    public Usuario getUserByEmail(String email) {
        try {
            Query q = em.createQuery("SELECT DISTINCT u FROM Usuario u WHERE u.activo = TRUE AND u.email = :e");
            q.setMaxResults(1);
            q.setParameter("e", email);
            return (Usuario) q.getSingleResult();
        } catch (NoResultException nre) {
            System.out.println("No se puso encontrar un usuario activo con ese mail");            
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el usuario por email", e);
            return null;
        }
    }
    
    public Usuario actualizarUsuarioSesion(Usuario u){
        try {
            u = em.merge(u);
            return u;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualziar el usuario en sesión", e);
            return null;
        }
    }

}
