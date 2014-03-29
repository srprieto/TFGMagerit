/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.esei.tfg.logica.servicios.GestorMarcosService;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "marcosController")
@SessionScoped
public class MarcosController implements Serializable {

    private String nombre = "";
    private String descripcion = "";

    @Inject
    GestorMarcosService gestorMarcoService;

    public MarcosController() {

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

    public void doMarco() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el marco de trabajo");
            context.redirect("crearmarco.xhtml");
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción");
            context.redirect("crearmarco.xhtml");
        } else if (gestorMarcoService.existeMarco(nombre) == true) {
            anadirMensajeError("Ya existe un marco con ese nombre");
            context.redirect("crearmarco.xhtml");
        } else {
            context.redirect("nuevomarco.xhtml");
        }
    }

    public void doCrear() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorMarcoService.crearNuevoMarco(nombre, descripcion);
        anadirMensajeCorrecto("El marco de trabajo " + nombre + " ha sido creado correctamente");
        nombre = "";
        descripcion = "";
        context.redirect("marcos.xhtml");
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        nombre = "";
        descripcion = "";
        context.redirect("crearmarco.xhtml");
    }
}
