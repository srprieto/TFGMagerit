/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.logica.daos.GestorUsuariosDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Saul
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private Usuario usuarioActual = null;
    private String login = "";
    private String password = "";
    private boolean autenticado = false;

    @Inject
    GestorUsuariosDAO gestorUsuariosDAO;
    
    public LoginController() {
    }
     /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    
    // Acciones para paginas JSF
    public String doLogin() {
        String destino;
        TipoUsuario tipo;
        if("".equals(login))
        {
            anadirMensajeError("Debe introducir un Login");
            destino = "fallo.login";
        }else if("".equals(password)){
            anadirMensajeError("Debe introducir una Contraseña");
            destino = "fallo.login";
        }else if (gestorUsuariosDAO.autenticarUsuario(login, password)) {
            usuarioActual = gestorUsuariosDAO.recuperarDatosUsuario(login);
            tipo = usuarioActual.getTipo();
            if(tipo == TipoUsuario.ADMINISTRADOR)
            {
                destino = "exito.loginadmin";
                autenticado= true;
            }else{
                destino = "exito.logineditor";
                autenticado= true;
            }
        } else {
            anadirMensajeError("Login o Password incorrectos");
            destino = "fallo.login";
            autenticado= false;
        }

        return destino;
    }
    
    
    public void doLogout() throws IOException {
        gestorUsuariosDAO.actualizarUltimoAcceso(usuarioActual.getId());
        usuarioActual = null;
        autenticado= false;
        login = "";
        password = "";
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
        ((HttpSession) ctx.getSession(false)).invalidate();
        ctx.redirect(ctxPath + "/index.xhtml");        
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
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

}
