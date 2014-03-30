/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.security.MessageDigest;
import javax.inject.Qualifier;


/**
 *
 * @author Saul
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private Usuario usuarioActual;
    private boolean autenticado = false;
    
    @Inject 
    Credenciales credenciales;
    
    @Inject
    GestorUsuariosService gestorUsuariosService;
    
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
    
    // Acciones para paginas JSF
    public String doLogin() {
        String destino;
        TipoUsuario tipo;
        String pass = this.encriptar(credenciales.getPassword());
        List<Usuario> results = gestorUsuariosService.usuario(pass);
        if(!results.isEmpty()){
            usuarioActual = results.get(0);
        }
        if (results.isEmpty()) {
            anadirMensajeError("Login o Password incorrectos");
            destino = "fallo.login";
            autenticado= false;
        } else {  
            
            tipo = usuarioActual.getTipo();
            if(tipo == TipoUsuario.ADMINISTRADOR)
            {
                destino = "exito.loginadmin";
                autenticado= true;
            }else{
                destino = "exito.logineditor";
                autenticado= true;
            }
        }

        return destino;
    }
    
    
    public void doLogout() throws IOException {
        gestorUsuariosService.actualizarUltimoAcceso(usuarioActual.getId());
        usuarioActual = null;
        autenticado= false;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
        ((HttpSession) ctx.getSession(false)).invalidate();
        ctx.redirect(ctxPath + "/index.xhtml");        
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
    
    @Produces @LoggedIn Usuario getUsuarioActual() {return usuarioActual;}
    
    @Qualifier
    @Retention(RUNTIME)
    @Target({TYPE, METHOD, PARAMETER, FIELD})
    public @interface LoggedIn {}
}