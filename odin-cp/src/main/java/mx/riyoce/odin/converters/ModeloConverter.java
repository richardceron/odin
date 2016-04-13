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
import mx.riyoce.odin.entities.Modelo;
import mx.riyoce.odin.sessions.AutosSessionBean;

/**
 *
 * @author ricardo
 */
@FacesConverter(value = "ModeloConverter")
public class ModeloConverter implements Converter {

    @EJB
    private AutosSessionBean aus;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return aus.getModeloById(Long.parseLong(value));
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a modelo", e);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return ((Modelo) value).toString();
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
            return "";
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a string", e);
            return "";
        }
    }
}
