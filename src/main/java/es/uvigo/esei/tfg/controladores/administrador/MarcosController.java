/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.esei.tfg.logica.servicios.GestorMarcosService;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
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
    
    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    //Atributos
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
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (nombre.equals("")) {
            anadirMensajeError(messages.getString("ERRMAR"));
            context.redirect("crearmarco.xhtml");
        } else if (descripcion.equals("")) {
            anadirMensajeError(messages.getString("ERRMAR1"));
            context.redirect("crearmarco.xhtml");
        } else if (gestorMarcoService.existeMarco(nombre) == true) {
            anadirMensajeError(messages.getString("ERRMAR2"));
            context.redirect("crearmarco.xhtml");
        } else {
            context.redirect("nuevomarco.xhtml");
        }
    }

    public void doCrear() throws IOException {
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorMarcoService.crearNuevoMarco(nombre, descripcion);
        anadirMensajeCorrecto(messages.getString("CORRMAR")+ " "+ nombre+" " + messages.getString("CORRMAR1"));
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
