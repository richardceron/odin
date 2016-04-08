/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.authenticators;

import javax.mail.PasswordAuthentication;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alfonso
 */
public class AutentificadorDeMail extends javax.mail.Authenticator {

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = "noreply@picacho.com.mx";
        String password = "Kabongo696*";
        return new PasswordAuthentication(username, password);
    }
}
