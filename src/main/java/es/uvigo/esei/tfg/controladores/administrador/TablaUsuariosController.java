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
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
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

@Named(value = "tablaUsuariosController")
@SessionScoped
public class TablaUsuariosController implements Serializable {
    
    //Internacionalizacion
    private Locale locale; //nos sirve para indicar el idioma (es, en, fr...)
    private ResourceBundle messages;//recupera los mensajes del archivo properties
    
    //Atributos
    private Usuario usuario;
    private List<Usuario> usuarios; 
    private Usuario[] selectedUsuarios; //Lista de usuarios seleccionados por el usuario en la tabla
    private UsuarioModel usuarioModel;//modelo de usuario ,necesario para cargar los datos en la tabla
    private List<Usuario> filteredUsuarios;//Lista que contiene los usuarios necesarios para el filtrado de los mismos en la tabla

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    GestorUsuariosService gestorUsuariosService;

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
    
    /*Datos que se mostraran en la tabla, primero recuperamos todos los datos y 
    posteriormente los cargamos con el modelo*/
    public UsuarioModel getUsuarioModel() {
        usuarios = usuarioDAO.buscarTodos();
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;
    }

    /*Funcion que valida si se selecciono un usuario, en caso de no seleccionar ninguno o mas 
    de uno se mostrara un mensaje de error, en caso contrario nos muestra el formulario de edición*/
    public void update() {
        
        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Atributos
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            this.setSelectedUsuarios(null);
            anadirMensajeError(messages.getString("ERRTABUSU"));
        } else if (tamano != 1) {
            this.setSelectedUsuarios(null);
            anadirMensajeError(messages.getString("ERRTABUSU1"));
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateUsuario() {
        
        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        Usuario[] seleccionados = this.getSelectedUsuarios();
        
        //Atributos
        int tamano = seleccionados.length;
        Usuario seleccionado = seleccionados[0];
        Long id = seleccionado.getId();
        this.setSelectedUsuarios(null);

        if (seleccionado.getLogin().equals("")) {
            anadirMensajeError(messages.getString("ERRTABUSU2"));
        } else if (gestorUsuariosService.existeUsuario(seleccionado.getLogin()) == true && gestorUsuariosService.existeId(seleccionado.getLogin()) != id) {
            anadirMensajeError(messages.getString("ERRTABUSU3"));
        } else {
            usuarioDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABUSU"));
            RequestContext.getCurrentInstance().update("form");
        }
    }

    public void eliminar() {
        
        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Atributos
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRTABUSU4"));
        } else {
            RequestContext.getCurrentInstance().execute("multiUsuarioDialog.show();");
        }
    }

    public void eliminarUsuarios() {
        
        //internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Atributos
        Usuario[] lista = this.getSelectedUsuarios();
        int tamano = lista.length;
        Usuario user;
        
        for (int i = 0; i < tamano; i++) {
            user = lista[i];
            usuarioDAO.eliminar(user);
        }
        if (tamano == 1) {
            anadirMensajeCorrecto(messages.getString("CORRTABUSU1"));
        } else {
            anadirMensajeCorrecto(messages.getString("CORRTABUSU2"));
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
