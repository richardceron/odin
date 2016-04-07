/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import mx.riyoce.odin.entities.Auto;
import mx.riyoce.odin.entities.Marca;
import mx.riyoce.odin.entities.Modelo;
import mx.riyoce.odin.entities.Spec;
import mx.riyoce.odin.sessions.AutosSessionBean;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class AutosController implements Serializable {

    @EJB
    private AutosSessionBean asb;

    private Modelo modelo;
    private Auto auto;
    private Spec spec;

    private List<Modelo> modelos;
    private List<Auto> autos;

    private Marca marcaForFilter;
    private Modelo modeloForFilter;

    private boolean editModelo;
    private boolean editAuto;

    public AutosController() {
        modelo = new Modelo();
        auto = new Auto();
        spec = new Spec();

        marcaForFilter = null;
        modeloForFilter = null;
    }

    @PostConstruct
    public void init() {
        initLists();
    }

    public void initLists() {
        modelos = asb.getAllModelos();
        autos = asb.getAllAutos();
    }

    /*-- Inician métodos para autos --*/
    public void crearAuto() {
        try {
            if (auto.getSpecs().size() > 0) {
                asb.persist(auto);
                cancelarEdicionAuto();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Auto creado con éxito", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes agregar al menos una spec", ""));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el auto", e);
        }
    }

    public void addSpecToAuto() {
        try {
            if (!spec.getNombre().equals("") && !spec.getDescription().equals("")) {
                auto.getSpecs().add(spec);
                spec.setAuto(auto);
                spec = new Spec();
            } else{
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Los campos de spcec son obligatorios", ""));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al agregar una spec al auto", e);
        }
    }

    public void removeSpecFromAuto(Spec s) {
        try {
            auto.getSpecs().remove(s);
            if (s.getId() != null) {
                asb.removeSpecFromauto(s);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar la spec del auto", e);
        }
    }
    
    public void filterAutisByModelo(){
        try {
            if (modeloForFilter != null) {
                autos = asb.getAutosByModelo(modeloForFilter);
            } else{
                autos = asb.getAllAutos();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al filtrar los autos por modelo", e);
        }
    }

    public void actualizarAuto() {
        try {
            asb.merge(auto);
            cancelarEdicionAuto();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Auto actualizado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el auto", e);
        }
    }

    public void cancelarEdicionAuto() {
        auto = new Auto();
        autos = asb.getAllAutos();
        editAuto = false;
    }

    /*-- Terminan métodos para autos --*/

 /*-- Inician métodos para modelos --*/
    public void crearModelo() {
        try {
            asb.persist(modelo);
            cancelarEdicionModelo();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Modelo creado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el modelo", e);
        }
    }

    public void actualizarModelo() {
        try {
            asb.merge(modelo);
            cancelarEdicionModelo();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Modelo actualizado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el modelo", e);
        }
    }

    public void filterModelosByMarca() {
        try {
            if (marcaForFilter != null) {
                modelos = asb.getModelosByMarca(marcaForFilter);
            } else {
                modelos = asb.getAllModelos();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al filtrar los modelos por marca", e);
        }
    }

    public void cancelarEdicionModelo() {
        modelo = new Modelo();
        modelos = asb.getAllModelos();
        editModelo = false;
    }

    /*-- Terminan métodos para modelos --*/
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
     * @return the auto
     */
    public Auto getAuto() {
        return auto;
    }

    /**
     * @param auto the auto to set
     */
    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    /**
     * @return the spec
     */
    public Spec getSpec() {
        return spec;
    }

    /**
     * @param spec the spec to set
     */
    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    /**
     * @return the modelos
     */
    public List<Modelo> getModelos() {
        return modelos;
    }

    /**
     * @param modelos the modelos to set
     */
    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    /**
     * @return the autos
     */
    public List<Auto> getAutos() {
        return autos;
    }

    /**
     * @param autos the autos to set
     */
    public void setAutos(List<Auto> autos) {
        this.autos = autos;
    }

    /**
     * @return the marcaForFilter
     */
    public Marca getMarcaForFilter() {
        return marcaForFilter;
    }

    /**
     * @param marcaForFilter the marcaForFilter to set
     */
    public void setMarcaForFilter(Marca marcaForFilter) {
        this.marcaForFilter = marcaForFilter;
    }

    /**
     * @return the modeloForFilter
     */
    public Modelo getModeloForFilter() {
        return modeloForFilter;
    }

    /**
     * @param modeloForFilter the modeloForFilter to set
     */
    public void setModeloForFilter(Modelo modeloForFilter) {
        this.modeloForFilter = modeloForFilter;
    }

    /**
     * @return the editModelo
     */
    public boolean isEditModelo() {
        return editModelo;
    }

    /**
     * @param editModelo the editModelo to set
     */
    public void setEditModelo(boolean editModelo) {
        this.editModelo = editModelo;
    }

    /**
     * @return the editAuto
     */
    public boolean isEditAuto() {
        return editAuto;
    }

    /**
     * @param editAuto the editAuto to set
     */
    public void setEditAuto(boolean editAuto) {
        this.editAuto = editAuto;
    }

}
