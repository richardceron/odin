/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.validators;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author ricardo
 */
@FacesValidator("PrecioValidator")
public class PrecioValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        float precio = (float) value;
        if (precio <= 0) {
            FacesMessage msg = new FacesMessage("El precio es requerido", "El precio requerido");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        } else {
            String str = String.valueOf(value);

            if (!str.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                FacesMessage msg = new FacesMessage("Precio es invalido", "Precio invalido");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

}
