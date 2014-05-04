/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.analista;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.UsuarioModel;
import es.uvigo.esei.tfg.logica.servicios.GestorProyectosService;
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaUsuarioProyectoController")
@SessionScoped
public class TablaUsuarioProyectoController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    private Usuario usuario;
    private List<Usuario> usuarios;
    private Usuario selectedUsuario;
    private Usuario[] selectedUsuarios;
    private UsuarioModel usuarioModel;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    ProyectoDAO proyectoDAO;

    @Inject
    ProyectoController proyectoController;

    @Inject
    GestorUsuariosService gestorDAO;

    @Inject
    GestorProyectosService gestorProDAO;

    @Inject
    @LoggedIn
    Usuario usuarioActual;

    public TablaUsuarioProyectoController() {

    }

    /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
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
    
     /*Funciones GET y SET*/
    
    /************************************************************************************************/

    public Usuario[] getSelectedUsuarios() {
        return selectedUsuarios;
    }

    public void setSelectedUsuarios(Usuario[] selectedUsuarios) {
        this.selectedUsuarios = selectedUsuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        usuarios = usuarioDAO.buscarTodos();
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public UsuarioModel getUsuarioModel() {
        usuarios = proyectoController.getProyectoActual().getEditores();
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;
    }
    
    /************************************************************************************************/

    public void eliminar() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRTABUSUPRO"));
        } else {
            RequestContext.getCurrentInstance().execute("multiDialog.show();");
        }
    }

    public void eliminarUsuario() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        
        for (int i = 0; i < tamano; i++) {
            Usuario seleccionado = seleccionados[i];
            proyectoController.getProyectoActual().getEditores().remove(seleccionado);
            seleccionado.getProyectos().remove(proyectoController.getProyectoActual());
            proyectoDAO.actualizar(proyectoController.getProyectoActual());
            usuarioDAO.actualizar(seleccionado);
        }
        anadirMensajeCorrecto(messages.getString("CORRTABUSUPRO"));
        RequestContext.getCurrentInstance().update("form");
    }
    
     public void atras() throws IOException
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();  
        this.setSelectedUsuarios(null);
        context.redirect("usuarios.xhtml");
    }

}
