/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.sessions;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.riyoce.odin.entities.Agencia;
import mx.riyoce.odin.entities.Usuario;
import mx.riyoce.odin.entities.GrupoAutomotriz;
import mx.riyoce.odin.entities.Marca;
import mx.riyoce.odin.entities.Perfil;
import mx.riyoce.odin.entities.Rol;

/**
 *
 * @author ricardo
 */
@Stateless
public class AdminSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_odin-ejb_ejb_1.0PU")
    private EntityManager em;

    public void persist(Object o) {
        try {
            em.persist(o);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear al persistir el objeto", e);
        }
    }

    public void merge(Object o) {
        try {
            em.merge(o);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualziar el objeto", e);
        }
    }

    /* Inician métodos para usuarios */
    public List<Usuario> getUsuarios() {
        try {
            return em.createQuery("SELECT DISTINCT lu FROM Usuario lu ORDER BY lu.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los usuarios", e);
            return null;
        }
    }

    public Usuario getUsuarioById(long id) {
        try {
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el usuario por id", e);
            return null;
        }
    }

    public boolean checkIfEmailExist(String email) {
        boolean flag = true;
        try {
            Usuario u = null;
            u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email").setParameter("email", email).setMaxResults(1).getSingleResult();
            if (u == null) {
                flag = false;
            }
        } catch (NoResultException e) {
            flag = false;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al verificar si el email existe", e);
        }
        return flag;
    }

    /* Terminan métodos para usuarios */
    /* Inician métodos para perfiles */
    public List<Perfil> getPerfiles() {
        try {
            return em.createQuery("SELECT DISTINCT lp FROM Perfil lp ORDER BY lp.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los perfiles", e);
            return null;
        }
    }

    public Perfil getPerfilById(long id) {
        try {
            return em.find(Perfil.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el perfil por id", e);
            return null;
        }
    }

    /* Terminan métodos para perfiles */

    /* Inician métodos para roles */
    public boolean crearRol(Rol r){
        boolean flag = false;
        try {
            if (!checkIfRolExist(r)) {
                em.persist(r);
                flag = true;
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al tcrear el rol", e);
        }
        return flag;
    }
    
    public List<Rol> getRoles() {
        try {
            return em.createQuery("SELECT DISTINCT lr FROM Rol lr ORDER BY lr.agencia.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los perfiles", e);
            return null;
        }
    }

    public boolean checkIfRolExist(Rol r) {
        boolean flag = true;
        try {
            Query q = em.createQuery("SELECT r FROM Rol r WHERE r.usuario = :u AND r.perfil = :p AND r.agencia = :a");
            q.setMaxResults(1);
            q.setParameter("u", r.getUsuario());
            q.setParameter("p", r.getPerfil());
            q.setParameter("a", r.getAgencia());
            Rol rol_from_db = (Rol) q.getSingleResult();
            if (rol_from_db == null) {
                flag = false;
            }
        } catch (NoResultException nre) {
            flag = false;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al revisar si existe el rol", e);
        }
        return flag;
    }

    public void eliminarRol(Rol r) {
        try {
            r = em.merge(r);
            em.remove(r);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar el rol", e);
        }
    }

    /* Terminan métodos para roles */

    /* Inician métodos para agencias */
    public List<Agencia> getAgencias() {
        try {
            return em.createQuery("SELECT DISTINCT la FROM Agencia la ORDER BY la.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las agencias", e);
            return null;
        }
    }

    public Agencia getAgenciaById(long id) {
        try {
            return em.find(Agencia.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer la agencia por id", e);
            return null;
        }
    }

    public Agencia getAgenciaByDominio(String dom) {
        try {
            Query q = em.createQuery("SELECT a FROM Agencia a WHERE a.url LIKE :dom");
            q.setParameter("dom", "%" + dom + "%");
            q.setMaxResults(1);
            return (Agencia) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer la agencia por dominio", e);
            return null;
        }
    }

    public List<Agencia> getAgenciasByMarca(Marca m) {
        try {
            Query q = em.createQuery("SELECT DISTINCT la FROM Agencia la WHERE la.marca = :m ORDER BY la.nombre ASC");
            q.setParameter("m", m);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las agencias por marca", e);
            return null;
        }
    }

    public List<Agencia> getAgenciasByGrupo(GrupoAutomotriz g) {
        try {
            Query q = em.createQuery("SELECT DISTINCT la FROM Agencia la WHERE la.grupo = :g ORDER BY la.nombre ASC");
            q.setParameter("g", g);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las agencias por marca", e);
            return null;
        }
    }

    /* Terminan métodos para agencias */

    /* Inician métodos para grupo */
    public List<GrupoAutomotriz> getGruposAutomotrices() {
        try {
            return em.createQuery("SELECT DISTINCT lg FROM GrupoAutomotriz lg ORDER BY lg.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los grupos", e);
            return null;
        }
    }

    public GrupoAutomotriz getGrupoAutomotrizById(long id) {
        try {
            return em.find(GrupoAutomotriz.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el GrupoAutomotriz por id", e);
            return null;
        }
    }

    /* Terminan métodos para grupos */

    /* Inician métodos para marcas */
    public List<Marca> getMarcas() {
        try {
            return em.createQuery("SELECT DISTINCT lm FROM Marca lm ORDER BY lm.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los ejecutivos", e);
            return null;
        }
    }

    public List<Marca> getMarcasGroup(GrupoAutomotriz g) {
        try {
            Query q = em.createQuery("SELECT DISTINCT lm FROM Marca lm, Agencia a WHERE a.marca = lm AND a.grupo = :g ORDER BY lm.nombre ASC");
            q.setParameter("g", g);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las marcas del grupo", e);
            return null;
        }
    }

    public Marca getMarcaById(long id) {
        try {
            return em.find(Marca.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer la marca por id", e);
            return null;
        }
    }
    /* Terminan métodos para marcas */

}
