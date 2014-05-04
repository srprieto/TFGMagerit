/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.analista;

import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorGruposService;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
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
    
    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;

    private String nombre;
    private String abreviatura;

    private ExternalContext context1;
     
    @Inject
    GrupoActivosDAO grupoActivosDAO;

    @Inject
    GestorGruposService gestorGruposService;

    public GruposController() {

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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    
    /************************************************************************************************/

    public void doGuardar() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
         //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        
        if (nombre.equals("")) {
            anadirMensajeError(messages.getString("ERRGRU"));
        } else if (abreviatura.equals("")) {
            anadirMensajeError(messages.getString("ERRGRU1"));
        } else if (grupoActivosDAO.buscarPorNombre(nombre) != null) {
            anadirMensajeError(messages.getString("ERRGRU2"));
        } else {
            context1.redirect("confirmargrupo.xhtml");
        }
    }

    public void doCrear() throws IOException {
        
         //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        
        gestorGruposService.crearNuevoGrupo(abreviatura, nombre);
        anadirMensajeCorrecto(messages.getString("CORRGRU")+" "+ nombre +" "+messages.getString("CORRGRU1"));
        nombre = "";
        abreviatura = "";
        context1.redirect("crearactivo.xhtml");
    }
}
