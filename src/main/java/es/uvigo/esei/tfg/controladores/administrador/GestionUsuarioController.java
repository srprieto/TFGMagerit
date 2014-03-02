/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.logica.daos.GestorUsuariosDAO;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.swing.text.TabableView;

/**
 *
 * @author Saul
 */

@Named(value = "usuarioController")
@SessionScoped
public class GestionUsuarioController implements Serializable {

     // Atributos
   
    
    private TipoUsuario tipo1;
    private String login = "";
    private String password = "";
    private String password2 = "";
    private boolean nuevoUsuario = true;
    
    
    @Inject
    GestorUsuariosDAO gestorUsuariosDAO;
    
    @Inject
    UsuarioDAO usuarioDAO;
    
    @Inject
    TablaUsuariosController tabla;
    
    @Inject @LoginController.LoggedIn Usuario usuarioActual;
    
    
    public GestionUsuarioController() {
        
    }
    

     /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    protected void anadirMensajeCorrecto(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }
    
    public String doUsuario(){
        
        String destino;
         if (login.equals("")) {
            anadirMensajeError("No se ha indicado un nombre de usuario");
            destino= "crearusuario.xhtml";
        } else if (password.equals("")) {
            anadirMensajeError("No se ha indicado una contraseña");
            destino= "crearusuario.xhtml";
        } else if (password2.equals("")) {
            anadirMensajeError("No se ha repetido la contraseña");
            destino= "crearusuario.xhtml";
        } else if (!password.equals(password2)) {
            anadirMensajeError("Las contraseñas introducidas no coinciden");
            destino= "crearusuario.xhtml";
        } else if (gestorUsuariosDAO.existeUsuario(login)) {
            anadirMensajeError("El nombre de usuario " + login + " ya existe");
            destino= "crearusuario.xhtml";
        }else {
             destino= "confirmarusuario.xhtml";
        }
         return destino;
    }
    
    public void doCrearUsuario() throws IOException {
        gestorUsuariosDAO.crearNuevoUsuario(login, password, tipo1);
        anadirMensajeCorrecto("El usuario " + login + " ha sido guardado correctamente");
        login="";
        password="";
        tipo1=null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        tabla.setSelectedUsuarios(null);
        context.redirect("usuarios.xhtml");
    }
    
    public String doActualizarUsuario() {
        String destino = null;

        if (password.equals("")) {
            anadirMensajeError("No se ha indicado una contraseña");
        } else if (password2.equals("")) {
            anadirMensajeError("No se ha repetido la contraseña");
        } else if (!password.equals(password2)) {
            anadirMensajeError("Las contraseñas introducidas no coinciden");
        } else {
            gestorUsuariosDAO.actualizarPassword(usuarioActual.getId(), password);
            gestorUsuariosDAO.actualizarDatosCliente(usuarioActual);
            destino = "usuario.actualizado";
        }
        return destino;
    }
    
   
    
    public String doCancelarModificacionUsuario() {
        String destino;
        if (nuevoUsuario) {
            // Anular los datos del nuevo cliente no guardado
            usuarioActual = null;
           
        } else {
            // Recuperar los datos originales del cliente
            usuarioActual = gestorUsuariosDAO.recuperarDatosUsuario(usuarioActual.getLogin()); // El login nunca se edita
            
        }
        destino = "usuario.cancelado";
        return destino;
    }

    public String doVerPerfil() {
        //login = loginController.getUsuarioActual().getLogin();
        //password = loginController.getUsuarioActual().getPassword();
        password2 = password;
        return "usuario.perfil";
    }
   
    // Metodos get y set
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public boolean isNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(boolean nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }
    
     public TipoUsuario getTipo() {
        return tipo1;
    }

    public void setTipo(TipoUsuario tipo1) {
        this.tipo1 = tipo1;
    }
}
