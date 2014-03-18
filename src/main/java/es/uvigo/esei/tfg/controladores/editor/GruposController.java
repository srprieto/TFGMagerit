/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorGruposService;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "gruposController")
@SessionScoped
public class GruposController implements Serializable {

    private String nombre;
    private String abreviatura;

    @Inject
    GrupoActivosDAO grupoActivosDAO;

    @Inject
    GestorGruposService gestorGruposService;

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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public void doGuardar() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el grupo");
            context.redirect("nuevogrupo.xhtml");
        } else if (abreviatura.equals("")) {
            anadirMensajeError("No se ha indicado una abreviatura para el grupo");
            context.redirect("nuevogrupo.xhtml");
        } else if (grupoActivosDAO.buscarPorNombre(nombre) != null) {
            anadirMensajeError("El nombre " + nombre + " ya existe");
            context.redirect("nuevogrupo.xhtml");
        } else {
            context.redirect("confirmargrupo.xhtml");
        }
    }

    public void doCrear() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorGruposService.crearNuevoGrupo(abreviatura, nombre);
        anadirMensajeCorrecto("El Grupo " + nombre + " ha sido guardado correctamente");
        nombre = "";
        abreviatura = "";
        context.redirect("activos.xhtml");
    }
}
