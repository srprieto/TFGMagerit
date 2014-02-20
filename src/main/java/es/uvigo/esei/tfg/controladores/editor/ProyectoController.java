/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.logica.daos.GestorProyectosDAO;
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
    GestorProyectosDAO gestorProyectosDAO;

    @Inject
    MarcoTrabajoDAO marcoTrabajoDAO;

    @LoggedIn 
    Usuario usuarioActual;

    @Inject
    TablaProyectosController tablaProyectosController;

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
        Proyecto[] pro= tablaProyectosController.getSelectedProyectos();
        proyectoActual = pro[0];
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

    public String doProyecto() {
        String destino;
        if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el proyecto");
            destino= "crearproyecto.xhtml";
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción");
            destino= "crearproyecto.xhtml";
        } else if(gestorProyectosDAO.existeProyecto(nombre)) {
             anadirMensajeError("Ya existe un proyecto con ese nombre");
             destino= "crearproyecto.xhtml";
        }else{
             destino= "confirmarproyecto.xhtml";
        }
        return destino;
    }

    public void doCrearProyecto() throws IOException {
        gestorProyectosDAO.crearNuevoProyecto(nombre, descripcion, proyectoEnEdicion.getMarcoTrabajo(), proyectoEnEdicion.getCreador());
        anadirMensajeCorrecto("El proyecto " + nombre + " ha sido guardado correctamente");
        nombre="";
        descripcion="";
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("misproyectos.xhtml");
    }

    public String doDestino() {
        String destino;
        Proyecto[] lista = tablaProyectosController.getSelectedProyectos();
        int tamano = lista.length;
        if (tamano == 0) {
            destino = "misproyectos.xhtml";
            anadirMensajeError("debe seleccionar un proyecto");
            return destino;
        } else if (tamano != 1) {
            destino = "misproyectos.xhtml";
            anadirMensajeError("Solo puede seleccionar un proyecto para trabajar sobre el");
            return destino;
        } else {
            proyectoActual = lista[0];
            creador = usuarioActual;
            destino = "proyecto.xhtml";
            return destino;
        }
    }
}
