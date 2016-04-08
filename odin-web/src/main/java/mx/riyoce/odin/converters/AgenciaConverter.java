/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.converters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.riyoce.odin.entities.Agencia;
import mx.riyoce.odin.sessions.AdminSessionBean;

/**
 *
 * @author ricardo
 */

@FacesConverter(value = "AgenciaConverter")
public class AgenciaConverter implements Converter{

    @EJB
    private AdminSessionBean asb;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return asb.getAgenciaById(Long.parseLong(value));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a Agencia", e);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return ((Agencia)value).toString();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a string", e);
            return "";
        }
    }
    
}
