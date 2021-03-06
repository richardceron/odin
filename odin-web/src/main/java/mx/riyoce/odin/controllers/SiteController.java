/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.riyoce.odin.entities.Agencia;
import mx.riyoce.odin.entities.Modelo;
import mx.riyoce.odin.sessions.AdminSessionBean;
import mx.riyoce.odin.sessions.AutosSessionBean;

/**
 *
 * @author admin
 */

@SessionScoped
@Named
public class SiteController implements Serializable{
    
    @EJB
    private AdminSessionBean adminsb;
    @EJB
    private AutosSessionBean asb;
    
    private Agencia agencia;
    
    public SiteController(){
        agencia = null;
    }
    
    @PostConstruct
    public void init(){
        getAgencia();
    }

    public List<Modelo> getModelosByCurrenSiteMarca(){
        return asb.getModelosByMarca(agencia.getMarca());
    }
    
    /**
     * @return the agencia
     */
    public Agencia getAgencia() {
        if (agencia == null) {
            String dom = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            agencia = adminsb.getAgenciaByDominio(dom);
        }
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }
    
}
