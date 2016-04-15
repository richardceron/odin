/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import mx.riyoce.odin.entities.Usuario;
import mx.riyoce.odin.sessions.UserSessionBean;

/**
 *
 * @author admin
 */
@Named
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private UserSessionBean usb;

    private String email;
    private String password;
    private String confirmPass;

    private Usuario usuario;
        

    public UserController() {
        usuario = null;
    }

    public void actualizarUsuario(){
        try {
            usuario = usb.actualizarUsuarioSesion(usuario);
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Error al actualziar al usuario", e);
        }
    }
    
    public void cancelarActualizarUsuario(){
        try {
            usuario = usb.getUserByEmail(email);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Información actualziada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Error al actualziar al usuario", e);
        }
    }
    
    public void login() {
        try {            
            HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String destination = r.getHeader("referer") + "?faces-redirect=true";
            try {
                r.login(email, password);
                usuario = usb.getUserByEmail(email);

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                e.getMessage(), null));
            }            

            FacesContext.getCurrentInstance().getExternalContext().redirect(destination);
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            ex.getMessage(), null));
        }
    }

    public String logout() {        
        usuario = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmPass
     */
    public String getConfirmPass() {
        return confirmPass;
    }

    /**
     * @param confirmPass the confirmPass to set
     */
    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
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

}
