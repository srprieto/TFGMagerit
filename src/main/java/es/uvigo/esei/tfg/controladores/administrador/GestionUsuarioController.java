/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "usuarioController")
@SessionScoped
public class GestionUsuarioController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private ResourceBundle messages;

    // Atributos
    private TipoUsuario tipo1;
    private String login = "";
    private String password = "";
    private String password2 = "";
    private boolean nuevoUsuario = true;
    
    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    TablaUsuariosController tablaUsuariosController;

    @Inject
    GestorUsuariosService gestorUsuariosService;

    @Inject
    @LoginController.LoggedIn
    Usuario usuarioActual;

    public GestionUsuarioController() {

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

    /**
     * Mensajes de error y correctos Añade un mensaje de error a la jeraquia de
     * componetes de la página JSF
     *
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    protected void anadirMensajeCorrecto(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }

    /*Funcion doUsuario() nos redirecciona a la vista de confirmacion de usuario en caso de que todos los
     valores sean correctos, en caso contrario muestra el mensaje de error correspondiente y nose redirecciona
     nuevamente a la vista de creación de usuarios*/
    public void doUsuario() throws IOException {
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (login.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU"));
            context.redirect("crearusuario.xhtml");
        } else if (password.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU1"));
            context.redirect("crearusuario.xhtml");
        } else if (password2.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU2"));
            context.redirect("crearusuario.xhtml");
        } else if (!password.equals(password2)) {
            anadirMensajeError(messages.getString("ERRUSU3"));
            context.redirect("crearusuario.xhtml");
        } else if (gestorUsuariosService.existeUsuario(login)) {
            anadirMensajeError(messages.getString("ERRUSU4") +" " + login +" "+ messages.getString("ERRUSU5"));
            context.redirect("crearusuario.xhtml");
        } else {
            context.redirect("confirmarusuario.xhtml");
        }
    }

    /*Funcion encriptar() encripta mediante el algoritmo MD5 la clave que le pasamos como parametro*/
    public String encriptar(String password) {
        String algorithm = "MD5";
        byte[] plainText = password.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }

     /*Funcion doUsuario() crea un nuevo usuario en la base de datos con los datos introducidos*/
    public void doCrearUsuario() throws IOException {
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String nuevoPass = this.encriptar(password);
        gestorUsuariosService.crearNuevoUsuario(login, nuevoPass, tipo1);
        anadirMensajeCorrecto(messages.getString("CORRUSU")+ " " + login + " "+messages.getString("CORRUSU1"));
        login = "";
        password = "";
        tipo1 = null;
        tablaUsuariosController.setSelectedUsuarios(null);
        context.redirect("usuarios.xhtml");
    }
    
    /*Funcion atras() hace referencia al boton Atras de la vista de creación de un usuario, ya que necesitamos 
    limpiar los atributos para que no se muestren los datos una vez abandonada la vista*/
    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        login = "";
        password = "";
        tipo1 = null;
        context.redirect("crearusuario.xhtml");
    }
}
