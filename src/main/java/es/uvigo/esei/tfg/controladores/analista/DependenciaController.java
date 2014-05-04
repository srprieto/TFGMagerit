/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.analista;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorDependenciaService;
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

/**
 *
 * @author Saul
 */
@Named(value = "dependenciaController")
@SessionScoped
public class DependenciaController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private ResourceBundle messages;

    private Activo principal;
    private Activo dependiente;
    private Double grado;
    private String justificante;
    private String nombre;

    private List<String> activos;

    @Inject
    GestorDependenciaService gestorDependenciaDAO;

    @Inject
    ProyectoController proyectoController;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    ArbolActivosController arbolActivoController;

    @Inject
    DependenciaDAO dependenciaDAO;

    public DependenciaController() {

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

    public List<String> getActivos() {
        activos = gestorDependenciaDAO.devolverPosiblesDependencias();
        return activos;
    }

    public void setActivos(List<String> activos) {
        this.activos = activos;
    }

    public Activo getPrincipal() {
        return principal;
    }

    public void setPrincipal(Activo principal) {
        this.principal = principal;
    }

    public Activo getDependiente() {
        return dependiente;
    }

    public void setDependiente(Activo dependiente) {
        this.dependiente = dependiente;
    }

    public Double getGrado() {
        return grado;
    }

    public void setGrado(Double grado) {
        this.grado = grado;
    }

    public String getJustificante() {
        return justificante;
    }

    public void setJustificante(String justificante) {
        this.justificante = justificante;
    }

    /************************************************************************************************/
   
    public void doGuardar() throws IOException {

        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (grado == null) {
            anadirMensajeError(messages.getString("ERRDEP"));
        } else if (justificante.equals("")) {
            anadirMensajeError(messages.getString("ERRDEP1"));
        } else if (this.getActivos() == null) {
            anadirMensajeError(messages.getString("ERRDEP2"));
        } else if (grado < 0 || grado > 100) {
            anadirMensajeError(messages.getString("ERRDEP3"));
        } else {
            context.redirect("confirmardependencia.xhtml");
        }
    }

    public void guardarDependencia() throws IOException {

        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        
        //Redirección y mostrar mensajes
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        List<Activo> act = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        for (int i = 0; i < act.size(); i++) {
            if (act.get(i).getNombre().equals(nombre)) {
                dependiente = act.get(i);
            }
        }
        
        gestorDependenciaDAO.crearNuevaDependencia(justificante, grado, arbolActivoController.getActivoActual(), dependiente);
        anadirMensajeCorrecto(messages.getString("CORRDEP"));
        justificante = "";
        grado = null;
        context.redirect("dependencias.xhtml");
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        grado = null;
        justificante = "";
        context.redirect("dependencianueva.xhtml");
    }

}
