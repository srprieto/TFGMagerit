/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.logica.daos.GestorUsuariosDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {
   
    // Atributos
    private Usuario usuarioEnEdicion;
    private Usuario usuarioActual = null;
    private final TipoUsuario tipusu = null;
    private String login = "";
    private String password = "";
    private String password2 = "";
    private boolean nuevoUsuario = true;
    
    // EJBs
    @EJB
    private GestorUsuariosDAO gestorUsuariosDAO;
   
    
    public UsuarioController() {
        
    }

    @PostConstruct
    private void inicializar(){
        usuarioEnEdicion = new Usuario();
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

    public String doNuevoUsuario() {
        nuevoUsuario = true;
        usuarioActual = new Usuario();
        login = "";
        password = "";
        password2 = "";

        return "usuario.nuevo";
    }

    public String doCrearUsuario() {
        String destino = null;

        if (login.equals("")) {
            anadirMensajeError("No se ha indicado un nombre de usuario");
        } else if (password.equals("")) {
            anadirMensajeError("No se ha indicado una contraseña");
        } else if (password2.equals("")) {
            anadirMensajeError("No se ha repetido la contraseña");
        } else if (!password.equals(password2)) {
            anadirMensajeError("Las contraseñas introducidas no coinciden");
        } else if (gestorUsuariosDAO.existeUsuario(login)) {
            anadirMensajeError("El nombre de usuario " + login + " ya existe");
        } else {
            usuarioActual = gestorUsuariosDAO.crearNuevoUsuario(login, password, tipusu);
            nuevoUsuario = false;
            destino = "usuario.creado";
        }

        return destino;
    }

    public String doActualizarUsuario() {
        String destino = null;

        if (password.equals("")) {
            anadirMensajeError("No se ha indicado una contraseÃ±a");
        } else if (password2.equals("")) {
            anadirMensajeError("No se ha repetido la contraseÃ±a");
        } else if (!password.equals(password2)) {
            anadirMensajeError("Las contraseÃ±as introducidas no coinciden");
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
        login = usuarioActual.getLogin();
        password = usuarioActual.getPassword();
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
    
     public Usuario getUsuarioEnEdicion() {
        return usuarioEnEdicion;
    }

    public void setUsuarioEnEdicion(Usuario usuarioEnEdicion) {
        this.usuarioEnEdicion = usuarioEnEdicion;
    }
}
