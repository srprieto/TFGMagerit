/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.modelos.UsuarioModel;
import es.uvigo.esei.tfg.logica.daos.GestorUsuariosDAO;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaUsuariosController")
@SessionScoped
public class TablaUsuariosController implements Serializable {

    private Usuario usuario;
    private List<Usuario> usuarios;
    private Usuario selectedUsuario;
    private Usuario[] selectedUsuarios;
    private UsuarioModel usuarioModel;
    private List<Usuario> filteredUsuarios;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    GestorUsuariosDAO gestorDAO;

    public TablaUsuariosController() {

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
        usuarios = usuarioDAO.buscarTodos();
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;
    }

    public void update() {
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            this.setSelectedUsuarios(null);
            anadirMensajeError("No ha seleccionado ningun usuario");
        } else if (tamano != 1) {

            this.setSelectedUsuarios(null);
            anadirMensajeError("Solo puede seleccionar un usuario para editarlo");
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateUsuario() {
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        Usuario seleccionado = seleccionados[0];
        Long id = seleccionado.getId();
        this.setSelectedUsuarios(null);

        if (seleccionado.getLogin().equals("")) {
            anadirMensajeError("Tienes que introducir un login para el usuario");
        } else if (gestorDAO.existeUsuario(seleccionado.getLogin()) == true && gestorDAO.existeId(seleccionado.getLogin()) != id) {
            anadirMensajeError("Ya existe un usuario con ese login");
        } else {
            usuarioDAO.actualizar(seleccionado);
            anadirMensajeCorrecto("El usuario ha sido modificado correctamente");
            RequestContext.getCurrentInstance().update("form");
        }
    }

    public void eliminar() {
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun usuario");
        } else {
            RequestContext.getCurrentInstance().execute("multiUsuarioDialog.show();");
        }
    }

    public void eliminarUsuarios() {
        Usuario[] lista = this.getSelectedUsuarios();
        int tamano = lista.length;
        for (int i = 0; i < tamano; i++) {
            Usuario user = lista[i];
            usuarioDAO.eliminar(user);
        }
        if (tamano == 1) {
            anadirMensajeCorrecto("El usuario ha sido eliminado correctamente");
        } else {
            anadirMensajeCorrecto("Los usuarios fueron eliminados correctamente");
        }
    }

    public void cancelar() throws IOException {
        this.setSelectedUsuarios(null);
        RequestContext.getCurrentInstance().execute("multiEditDialog.hide();");
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedUsuarios(null);
        context.redirect("usuarios.xhtml");
    }
    
    public void atras1() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedUsuarios(null);
        context.redirect("indexadministrador.xhtml");
    }
}
