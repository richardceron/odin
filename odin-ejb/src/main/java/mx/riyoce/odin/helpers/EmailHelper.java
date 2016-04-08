/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.helpers;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.riyoce.odin.entities.Correo;
import mx.riyoce.odin.entities.Solicitud;

/**
 *
 * @author admin
 */
public class EmailHelper {
    
    public Correo generarCorreoParaEjecutivo(Solicitud s){
        try {
            Correo m = new Correo();
            m.setAcerca("Nueva solicitud de: "+s.getTipo().getNombre());
            m.setFecha(new Date());            
            m.setNombreDe("Alertas "+s.getAgencia().getNombre());
            m.setMailDe("noreply@picacho.com.mx");
            m.setNombrePara(s.getUsuario().getNombre());
            m.setMailPara(s.getUsuario().getEmail());
            
            StringBuilder sb = new StringBuilder();            
            sb.append("<div style=\"width: 100%; height: 50px; line-height: 50px; text-align: center; background-color: rgb(2, 2, 126); color: #FFF; font-size: 25px; font-family: Helvetica;\">");
            sb.append("Nueva solicitud de ");
            sb.append(s.getTipo().getNombre());
            sb.append("</div>");
            sb.append("<br/>");
            sb.append("<br/>");
            
            sb.append("Buen día ");
            sb.append(s.getUsuario().getNombre());
            sb.append(" se ha registrado una nueva solitud de ");
            sb.append(s.getTipo().getNombre());
            sb.append(".");
            
            sb.append("<br/>");
            sb.append("<br/>");
            sb.append("Datos de solicitud:");
            sb.append("<br/>");
            sb.append("<br/>");
            
            sb.append(s.getDescripcionSolicitud());
            
            sb.append("<br/>");
            sb.append("<br/>");
            sb.append("Información de contacto:");
            sb.append("<br/>");
            sb.append("<br/>");                        
            
            sb.append("<b>");
            sb.append("Nombre: ");
            sb.append("</b>");
            sb.append(s.getNombre());
            sb.append("<br/>");
            
            sb.append("<b>");
            sb.append("Email: ");
            sb.append("</b>");
            sb.append(s.getEmail());
            sb.append("<br/>");
            
            sb.append("<b>");
            sb.append("Teléfono: ");
            sb.append("</b>");
            sb.append(s.getTelefono());
            sb.append("<br/>");
            
            sb.append("<b>");
            sb.append("Comentarios: ");
            sb.append("</b>");
            sb.append(s.getComentarios());
            sb.append("<br/>");
            
            m.setCuerpo(sb.toString());
            
            return m;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al generar el email para el ejecutivo", e);
            return null;
        }
    }
    
    public Correo generarCorreoParaCliente(Solicitud s){
        try {
            Correo m = new Correo();
            m.setFecha(new Date());            
            m.setAcerca("¡ Gracias por contactarnos !");
            m.setNombreDe("Notificaciones "+s.getAgencia().getNombre());
            m.setMailDe("noreply@picacho.com.mx");
            
            m.setNombrePara(s.getNombre());
            m.setMailPara(s.getEmail());
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("<b>");
            sb.append("¡ Buen día ");
            sb.append(s.getNombre());            
            sb.append(" !");
            sb.append("</b>");
            sb.append("<br/>");
            sb.append("<br/>");
            sb.append("Gracias por tu solicitud de ");
            sb.append(s.getTipo().getNombre());
            sb.append(". En breve un ejecutivo se pondrá en contacto contigo.");
            
            sb.append("<br/>");
            sb.append("<br/>");
            
            sb.append("<img src=\"http://picacho.net/templates/picacho/resources/images/logo-picacho.png\" style=\"width: 250px; height: auto\" />");
            
            m.setCuerpo(sb.toString());
            
            return m;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al generar el email para el cliente", e);
            return null;
        }
    }
    
}
