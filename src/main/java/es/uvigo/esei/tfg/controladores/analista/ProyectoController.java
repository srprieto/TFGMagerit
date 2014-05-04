/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.analista;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.logica.servicios.GestorProyectosService;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "proyectoController")
@SessionScoped
public class ProyectoController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    private Proyecto proyectoEnEdicion;
    private Proyecto proyectoActual;

    private boolean mostrar = false;
    private String nombre = "";
    private String descripcion = "";
   
    private Usuario creador;
    private MarcoTrabajo marcoelegido;
    private List<MarcoTrabajo> marco;

    @Inject
    GestorProyectosService gestorProyectosService;

    @Inject
    MarcoTrabajoDAO marcoTrabajoDAO;

    @Inject
    @LoggedIn
    Usuario usuarioActual;

    @Inject
    TablaProyectosController tablaProyectosController;
    
    @Inject
    TablaProyectoColaborativoController tablaProyectoColaborativoController;

    public ProyectoController() {

    }

    @PostConstruct
    private void inicializar() {
        proyectoEnEdicion = new Proyecto();
        marco = marcoTrabajoDAO.buscarTodos();
        proyectoEnEdicion.setCreador(usuarioActual);
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proyecto getProyectoActual() {
        return proyectoActual;
    }

    public void setProyectoActual(Proyecto proyectoActual) {
        this.proyectoActual = proyectoActual;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public Proyecto getProyectoEnEdicion() {
        return proyectoEnEdicion;
    }

    public void setProyectoEnEdicion(Proyecto proyectoEnEdicion) {
        this.proyectoEnEdicion = proyectoEnEdicion;
    }

    public List<MarcoTrabajo> getMarco() {
        return marco;
    }

    public void setMarco(List<MarcoTrabajo> marco) {
        this.marco = marco;
    }

    public MarcoTrabajo getMarcoelegido() {
        return marcoelegido;
    }

    public void setMarcoelegido(MarcoTrabajo marcoelegido) {
        this.marcoelegido = marcoelegido;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
    
   /************************************************************************************************/
    
   
    public void doProyecto() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        if (nombre.equals("")) {
            anadirMensajeError(messages.getString("ERRPRO"));
        } else if (descripcion.equals("")) {
            anadirMensajeError(messages.getString("ERRPRO1"));
        } else if (gestorProyectosService.existeProyecto(nombre)) {
            anadirMensajeError(messages.getString("ERRPRO2"));
        } else {
            context.redirect("confirmarproyecto.xhtml");
        }
    }

    public void doCrearProyecto() throws IOException {
       
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        gestorProyectosService.crearNuevoProyecto(nombre, descripcion, proyectoEnEdicion.getMarcoTrabajo(), proyectoEnEdicion.getCreador());
        anadirMensajeCorrecto(messages.getString("CORRPRO")+" "+ nombre +" "+messages.getString("CORRPRO1"));
        nombre = "";
        descripcion = "";
        context.redirect("misproyectos.xhtml");
    }

    public void doDestino() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        Proyecto[] lista = tablaProyectosController.getSelectedProyectos();
        int tamano = lista.length;
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRPRO3"));
        } else if (tamano != 1) {
            anadirMensajeError(messages.getString("ERRPRO4"));
        } else {
            proyectoActual = lista[0];
            creador = usuarioActual;
            mostrar = true;
            context.redirect("activos.xhtml");
        }
    }
    
     public void doDestino1() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        Proyecto[] lista = tablaProyectoColaborativoController.getSelectedProyectos();
        int tamano = lista.length;
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRPRO3"));
        } else if (tamano != 1) {
            anadirMensajeError(messages.getString("ERRPRO4"));
        } else {
            proyectoActual = lista[0];
            creador = usuarioActual;
            mostrar = false;
            context.redirect("activos.xhtml");
        }
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        nombre = "";
        descripcion = "";
        context.redirect("crearproyecto.xhtml");
    }
}
