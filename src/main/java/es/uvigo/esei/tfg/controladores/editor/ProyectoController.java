/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

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

    // Atributos
    private Proyecto proyectoEnEdicion;
    private Proyecto proyectoActual;

    private String nombre = "";
    private String descripcion = "";
    private List<MarcoTrabajo> marco;
    private Usuario creador;
    private MarcoTrabajo marcoelegido;

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

    public void doProyecto() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el proyecto");
            context.redirect("crearproyecto.xhtml");
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción");
            context.redirect("crearproyecto.xhtml");
        } else if (gestorProyectosService.existeProyecto(nombre)) {
            anadirMensajeError("Ya existe un proyecto con ese nombre");
            context.redirect("crearproyecto.xhtml");
        } else {
            context.redirect("confirmarproyecto.xhtml");
        }
    }

    public void doCrearProyecto() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorProyectosService.crearNuevoProyecto(nombre, descripcion, proyectoEnEdicion.getMarcoTrabajo(), proyectoEnEdicion.getCreador());
        anadirMensajeCorrecto("El proyecto " + nombre + " ha sido guardado correctamente");
        nombre = "";
        descripcion = "";
        context.redirect("misproyectos.xhtml");
    }

    public void doDestino() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        Proyecto[] lista = tablaProyectosController.getSelectedProyectos();
        int tamano = lista.length;
        if (tamano == 0) {
            anadirMensajeError("debe seleccionar un proyecto");
            context.redirect("misproyectos.xhtml");
        } else if (tamano != 1) {
            anadirMensajeError("Solo puede seleccionar un proyecto para trabajar sobre el");
            context.redirect("misproyectos.xhtml");
        } else {
            proyectoActual = lista[0];
            creador = usuarioActual;
            context.redirect("activos.xhtml");
        }
    }
    
     public void doDestino1() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        Proyecto[] lista = tablaProyectoColaborativoController.getSelectedProyectos();
        int tamano = lista.length;
        if (tamano == 0) {
            anadirMensajeError("debe seleccionar un proyecto");
            context.redirect("misproyectos.xhtml");
        } else if (tamano != 1) {
            anadirMensajeError("Solo puede seleccionar un proyecto para trabajar sobre el");
            context.redirect("misproyectos.xhtml");
        } else {
            proyectoActual = lista[0];
            creador = usuarioActual;
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
