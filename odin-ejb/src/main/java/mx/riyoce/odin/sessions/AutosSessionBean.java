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
import javax.persistence.PersistenceContext;
import mx.riyoce.odin.entities.Auto;
import mx.riyoce.odin.entities.Marca;
import mx.riyoce.odin.entities.Modelo;
import mx.riyoce.odin.entities.Spec;

/**
 *
 * @author ricardo
 */

@Stateless
public class AutosSessionBean implements Serializable{

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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear al actualziar el objeto", e);
        }
    }       
    
    /*-- Inician métodos para modelos --*/
    public List<Modelo> getAllModelos(){
        try {
            return em.createQuery("SELECT lm FROM Modelo lm ORDER BY lm.nombre").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los modelos", e);
            return null;
        }
    }
    
    public List<Modelo> getModelosByMarca(Marca m){
        try {
            return em.createQuery("SELECT lm FROM Modelo lm WHERE lm.marca = :m ORDER BY lm.nombre").setParameter("m", m).getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los modelos por marca", e);
            return null;
        }
    }
    
    public Modelo getModeloById(long id){
        try {
            return em.find(Modelo.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el modelo por id", e);
            return null;
        }
    }
    /*-- Terminan métodos para modelos --*/
    
    /*-- Inician métodos para autos --*/
    public List<Auto> getAllAutos(){
        try {
            return em.createQuery("SELECT la FROM Auto la ORDER BY la.nombre").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los modelos", e);
            return null;
        }
    }
    
    public List<Auto> getAutosByModelo(Modelo m){
        try {
            return em.createQuery("SELECT la FROM Auto la WHERE la.modelo = :m ORDER BY la.nombre").setParameter("m", m).getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los autos por modelo", e);
            return null;
        }
    }
    
    public Auto getAutoById(long id){
        try {
            return em.find(Auto.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el auto por id", e);
            return null;
        }
    }
    
    public void removeSpecFromauto(Spec s){
        try {
            em.merge(s);
            em.remove(s);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar la spec del auto", e);            
        }
    }
    /*-- Terminan métodos para autos --*/
    
}
