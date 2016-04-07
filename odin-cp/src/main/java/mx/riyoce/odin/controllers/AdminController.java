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
import mx.riyoce.odin.entities.Agencia;
import mx.riyoce.odin.entities.Usuario;
import mx.riyoce.odin.entities.GrupoAutomotriz;
import mx.riyoce.odin.entities.Imagen;
import mx.riyoce.odin.entities.Marca;
import mx.riyoce.odin.entities.Perfil;
import mx.riyoce.odin.entities.Rol;
import mx.riyoce.odin.sessions.AdminSessionBean;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class AdminController implements Serializable {

    @EJB
    private AdminSessionBean asb;

    private Marca marca;
    private GrupoAutomotriz grupo;
    private Agencia agencia;
    private Perfil perfil;
    private Rol rol;
    private Usuario usuario;

    private List<Usuario> usuarios;
    private List<Rol> roles;
    private List<Perfil> perfiles;
    private List<Agencia> agencias;
    private List<GrupoAutomotriz> grupos;
    private List<Marca> marcas;

    private boolean editarUsuario;
    private boolean editarMarca;
    private boolean editarAgencia;
    private boolean editarGrupo;
    private boolean editarPerfil;

    public AdminController() {
        usuario = new Usuario();
        marca = new Marca();
        grupo = new GrupoAutomotriz();
        agencia = new Agencia();
        perfil = new Perfil();
        rol = new Rol();
    }

    @PostConstruct
    public void init() {
        initLists();
    }

    public void initLists() {
        marcas = asb.getMarcas();
        grupos = asb.getGruposAutomotrices();
        agencias = asb.getAgencias();
        usuarios = asb.getUsuarios();
    }

    /*-- Metodos para usuarios --*/
    public void crearUsuario() {
        try {
            if (!asb.checkIfEmailExist(usuario.getEmail())) {
                asb.persist(usuario);
                cancelarEdicionUsuario();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario creado con éxito", ""));
            } else{
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Ya existe un usuario con ese correo", ""));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el usuario", e);
        }
    }

    public void actualizarUsuario() {
        try {
            asb.merge(usuario);
            cancelarEdicionUsuario();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario editado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el usuario", e);
        }
    }

    public void cancelarEdicionUsuario() {
        usuario = new Usuario();
        editarUsuario = false;
    }

    /*-- Fin metodos para usuarios --*/
 /*-- Metodos para agencias --*/
    public void crearAgencia() {
        try {
            asb.persist(agencia);
            cancelarEdicionAgencia();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Agencia creada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear al creaer el ejecutivo", e);
        }
    }

    public void addFileToAgencia(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (file != null) {
                Imagen i = new Imagen();
                byte[] bytes = IOUtils.toByteArray(file.getInputstream());
                i.setContent(bytes);
                i.setMime(file.getContentType());
                i.setFileName(file.getFileName());
                agencia.setLogo(i);
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Logo cargado con éxito", ""));
            } else {
                System.out.println("El archivo es null");
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al cargar el logo al grupo", e);
        }
    }

    public void onPointSelectAgencia(PointSelectEvent event) {
        try {
            LatLng latlng = event.getLatLng();
            agencia.setLatitud(latlng.getLat());
            agencia.setLongitud(latlng.getLng());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Coordenadas seleccionadas con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al obtener las coordenadas de la agencia", e);
        }
    }

    public void actualizarAgencia() {
        try {
            asb.merge(agencia);
            cancelarEdicionAgencia();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Agencia editada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar al creaer el ejecutivo", e);
        }
    }

    public void cancelarEdicionAgencia() {
        agencia = new Agencia();
        agencias = asb.getAgencias();
        editarAgencia = false;
    }

    /*-- Fin metodos para agencias --*/
 /*-- Metodos para grupos --*/
    public void crearGrupoAutomotriz() {
        try {
            asb.persist(grupo);
            cancelarEdicionGrupoAutomotriz();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo automotriz creado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear al creaer el Grupo automotriz", e);
        }
    }

    public void addFileToGrupo(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (file != null) {
                Imagen i = new Imagen();
                byte[] bytes = IOUtils.toByteArray(file.getInputstream());
                i.setContent(bytes);
                i.setMime(file.getContentType());
                i.setFileName(file.getFileName());
                grupo.setLogo(i);
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Logo cargado con éxito", ""));
            } else {
                System.out.println("El archivo es null");
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al cargar el logo al grupo", e);
        }
    }

    public void actualizarGrupoAutomotriz() {
        try {
            asb.merge(grupo);
            cancelarEdicionGrupoAutomotriz();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo automotriz editado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar al actualizar el Grupo automotriz", e);
        }
    }

    public void cancelarEdicionGrupoAutomotriz() {
        grupo = new GrupoAutomotriz();
        grupos = asb.getGruposAutomotrices();
        editarGrupo = false;
    }

    /*-- Fin metodos para grupos --*/
 /*-- Metodos para marcas --*/
    public void crearMarca() {
        try {
            asb.persist(marca);
            cancelarEdicionMarca();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Marca creada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la marca", e);
        }
    }

    public void addFileToMarca(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (file != null) {
                Imagen i = new Imagen();
                byte[] bytes = IOUtils.toByteArray(file.getInputstream());
                i.setContent(bytes);
                i.setMime(file.getContentType());
                i.setFileName(file.getFileName());
                marca.setLogo(i);
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Logo cargado con éxito", ""));
            } else {
                System.out.println("El archivo es null");
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al cargar el logo a la marca", e);
        }
    }

    public void actualizarMarca() {
        try {
            asb.merge(marca);
            cancelarEdicionMarca();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Marca editada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar la marca", e);
        }
    }

    public void cancelarEdicionMarca() {
        marca = new Marca();
        editarMarca = false;
        marcas = asb.getMarcas();
    }

    /*-- Fin metodos para marcas --*/
    /**
     * @return the agencia
     */
    public Agencia getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the perfil
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(Rol rol) {
        this.rol = rol;
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

    /**
     * @return the editarMarca
     */
    public boolean isEditarMarca() {
        return editarMarca;
    }

    /**
     * @param editarMarca the editarMarca to set
     */
    public void setEditarMarca(boolean editarMarca) {
        this.editarMarca = editarMarca;
    }

    /**
     * @return the grupo
     */
    public GrupoAutomotriz getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(GrupoAutomotriz grupo) {
        this.grupo = grupo;
    }

    /**
     * @return the roles
     */
    public List<Rol> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    /**
     * @return the perfiles
     */
    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    /**
     * @param perfiles the perfiles to set
     */
    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    /**
     * @return the agencias
     */
    public List<Agencia> getAgencias() {
        return agencias;
    }

    /**
     * @param agencias the agencias to set
     */
    public void setAgencias(List<Agencia> agencias) {
        this.agencias = agencias;
    }

    /**
     * @return the grupos
     */
    public List<GrupoAutomotriz> getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(List<GrupoAutomotriz> grupos) {
        this.grupos = grupos;
    }

    /**
     * @return the marcas
     */
    public List<Marca> getMarcas() {
        return marcas;
    }

    /**
     * @param marcas the marcas to set
     */
    public void setMarcas(List<Marca> marcas) {
        this.marcas = marcas;
    }

    /**
     * @return the editarAgencia
     */
    public boolean isEditarAgencia() {
        return editarAgencia;
    }

    /**
     * @param editarAgencia the editarAgencia to set
     */
    public void setEditarAgencia(boolean editarAgencia) {
        this.editarAgencia = editarAgencia;
    }

    /**
     * @return the editarGrupo
     */
    public boolean isEditarGrupo() {
        return editarGrupo;
    }

    /**
     * @param editarGrupo the editarGrupo to set
     */
    public void setEditarGrupo(boolean editarGrupo) {
        this.editarGrupo = editarGrupo;
    }

    /**
     * @return the editarPerfil
     */
    public boolean isEditarPerfil() {
        return editarPerfil;
    }

    /**
     * @param editarPerfil the editarPerfil to set
     */
    public void setEditarPerfil(boolean editarPerfil) {
        this.editarPerfil = editarPerfil;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the editarUsuario
     */
    public boolean isEditarUsuario() {
        return editarUsuario;
    }

    /**
     * @param editarUsuario the editarUsuario to set
     */
    public void setEditarUsuario(boolean editarUsuario) {
        this.editarUsuario = editarUsuario;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
