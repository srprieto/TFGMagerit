/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.UsuarioModel;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaUsuariosProyectoController")
@SessionScoped
public class TablaUsuariosProyectoController implements Serializable {

    private Usuario usuario;
    private List<Usuario> usuarios;
    private Usuario selectedUsuario;
    private Usuario[] selectedUsuarios;
    private UsuarioModel usuarioModel;
    private List<Usuario> filteredUsuarios;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    ProyectoDAO proyectoDAO;

    @Inject
    ProyectoController proyectoController;

    @Inject
    @LoggedIn
    Usuario usuarioActual;

    public TablaUsuariosProyectoController() {

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

    public List<Usuario> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    public void setFilteredUsuarios(List<Usuario> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
    }

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
        usuarios = usuarioDAO.buscarPorTipo(TipoUsuario.EDITOR);
        Usuario creador = usuarioActual;
        usuarios.remove(creador);
        List<Usuario> editores = proyectoController.getProyectoActual().getEditores();
        int tamano = editores.size();
        for (int i = 0; i < tamano; i++) {
            usuarios.remove(editores.get(i));
        }
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;
    }

    public void update() {
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun usuario");
        } else {
            RequestContext.getCurrentInstance().execute("multiDialog.show();");
        }
    }

    public void aceptarUsuarios() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        List<Proyecto> proyectos = new ArrayList<>();
        usuarios = new ArrayList<>();
        Usuario[] lista = this.getSelectedUsuarios();
        int tamano = lista.length;
        Proyecto pro = proyectoController.getProyectoActual();
        proyectos.add(pro);
        for (int i = 0; i < tamano; i++) {
            Usuario usu = lista[i];
            usuarios.add(usu);
        }
        pro.setEditores(usuarios);
        proyectoDAO.actualizar(pro);

        anadirMensajeCorrecto("Usuarios asignados correctamente");
        context.redirect("usuarios.xhtml");
    }
}
