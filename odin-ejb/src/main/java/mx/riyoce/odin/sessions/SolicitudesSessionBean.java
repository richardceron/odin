/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.sessions;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.riyoce.odin.entities.Agencia;
import mx.riyoce.odin.entities.Correo;
import mx.riyoce.odin.entities.Solicitud;
import mx.riyoce.odin.entities.TipoSolicitud;
import mx.riyoce.odin.entities.Usuario;
import mx.riyoce.odin.helpers.EmailHelper;

/**
 *
 * @author ricardo
 */
@Stateless
public class SolicitudesSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_odin-ejb_ejb_1.0PU")
    private EntityManager em;

    public List<Correo> crearSolicitud(Solicitud s) {
        List<Correo> lm = new LinkedList<>();
        try {
            Usuario u = getSiguienteEjecutivo(s.getAgencia(), s.getTipo());
            s.setUsuario(u);
            em.persist(s);
            EmailHelper eh = new EmailHelper();
            Correo mail_ejecutivo = eh.generarCorreoParaEjecutivo(s);
            lm.add(mail_ejecutivo);

            for (Usuario user : getUsuariosCopiasSolicitudes(s)) {
                Correo m = new Correo();
                m.setFecha(new Date());
                m.setAcerca(mail_ejecutivo.getAcerca());
                m.setCuerpo(mail_ejecutivo.getCuerpo());
                m.setMailPara(user.getEmail());
                m.setNombrePara(user.getNombre());
                m.setMailDe(mail_ejecutivo.getMailDe());
                m.setNombreDe(mail_ejecutivo.getNombreDe());
                lm.add(m);
            }

            lm.add(eh.generarCorreoParaCliente(s));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la solicitud", e);
        }
        return lm;
    }

    public List<Solicitud> getSolicitudesByUser(Usuario u) {
        try {
            return em.createQuery("SELECT DISTINCT ls FROM Solicitud ls WHERE ls.usuario = :u ORDER BY ls.fecha DESC").setParameter("u", u).getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las solicitudes por usuario", e);
            return null;
        }
    }

    public TipoSolicitud getTipoSolicitudById(long id) {
        try {
            return em.find(TipoSolicitud.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el tipo de solicitud por id", e);
            return null;
        }
    }

    public TipoSolicitud getTiposolicitudByClave(String clave) {
        try {
            Query q = em.createQuery("SELECT ts FROM TipoSolicitud ts WHERE ts.clave = :clave");
            q.setParameter("clave", clave);
            return (TipoSolicitud) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el tipo de solicitud por clave", e);
            return null;
        }
    }

    public Usuario getSiguienteEjecutivo(Agencia a, TipoSolicitud tipo) {
        Usuario u = null;
        try {
            Query q = em.createQuery("SELECT s FROM Solicitud s WHERE s.agencia = :a AND s.tipo = :t ORDER BY s.fecha DESC");
            q.setMaxResults(1);
            q.setParameter("a", a);
            q.setParameter("t", tipo);
            Solicitud s = (Solicitud) q.getSingleResult();

            Query q1 = em.createQuery("SELECT r.usuario FROM Rol r WHERE r.agencia = :a AND r.perfil = :p AND r.usuario.id > :uid");
            q1.setMaxResults(1);
            q1.setParameter("a", a);
            q1.setParameter("p", s.getTipo().getPerfil());
            q1.setParameter("uid", s.getUsuario().getId());

            u = (Usuario) q1.getSingleResult();

        } catch (NoResultException nre) {
            Query q = em.createQuery("SELECT r.usuario FROM Rol r WHERE r.agencia = :a AND r.perfil = :p ORDER BY r.usuario.id");
            q.setMaxResults(1);
            q.setParameter("a", a);
            q.setParameter("p", tipo.getPerfil());
            u = (Usuario) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el siguiente ejecutivo", e);
        }
        return u;
    }

    public List<Usuario> getUsuariosCopiasSolicitudes(Solicitud s) {
        try {
            Query q = em.createQuery("SELECT DISTINCT lr.usuario FROM Rol lr WHERE lr.agencia = :a AND lr.perfil.clave = :clave");
            q.setParameter("a", s.getAgencia());
            q.setParameter("clave", "copia_" + s.getTipo().getPerfil().getClave());
            return q.getResultList();
        } catch (NoResultException nre) {
            System.out.println("No hay usuarios para enviar copias");
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer a los usuarios para copias de solicitudes", e);
            return null;
        }
    }

}
