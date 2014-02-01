/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.logica.daos.GestorUsuariosDAO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private Usuario usuarioActual = null;
    private List<TipoUsuario> tipusu;
    private TipoUsuario tipo1=null;
    private String login = "";
    private String password = "";
    private String password2 = "";
    private boolean nuevoUsuario = true;
    
    
    @Inject
    private GestorUsuariosDAO gestorUsuariosDAO;
    
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
        if (gestorUsuariosDAO.autenticarUsuario(login, password)) {
            usuarioActual = gestorUsuariosDAO.recuperarDatosUsuario(login);
            password2 = password;
            nuevoUsuario = false;
            tipo = usuarioActual.getTipo();
            if(tipo == TipoUsuario.ADMINISTRADOR)
            {
                destino = "indexadministrador.xhtml";
            }else{
                destino = "indextrabajador.xhtml";
            }
        } else {
            destino = "login.xhtml";
        }

        return destino;
    }

    public String doLogout() {
        String destino;
        if (usuarioActual != null) {
            gestorUsuariosDAO.actualizarUltimoAcceso(usuarioActual.getId());
            // carroCompraController.vaciarCarro();
        }
        usuarioActual = null;
        login = "";
        password = "";
        password2 = "";

        nuevoUsuario = true;

        // Limpiar los objetos de sesiÃ³n (vaciar la sesiÃ³n)
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();        
        
        destino = "login.xhtml";
        
        return destino;
    }
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

}
