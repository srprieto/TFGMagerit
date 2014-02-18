/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Saul
 */

@Named 
@RequestScoped
public class Credenciales {
    private String login;
    private String password;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
