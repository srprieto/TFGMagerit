/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.esei.tfg.controladores.modelos.DegradacionModel;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Saul
 */
@Named(value = "tablaDegradacionController")
@SessionScoped
public class TablaDegradacionController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    private Degradacion degradacion;
    private List<Degradacion> degradaciones;
    private Degradacion selectedDegradacion;
    private Degradacion[] selectedDegradaciones;
    private DegradacionModel degradacionModel;
    private List<Degradacion> filteredDegradacion;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    ArbolActivosController arbolActivosController;

    @Inject
    ArbolAmenazasController arbolAmenazasController;

    public TablaDegradacionController() {

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
    
    public Degradacion getDegradacion() {
        return degradacion;
    }

    public void setDegradacion(Degradacion degradacion) {
        this.degradacion = degradacion;
    }

    public List<Degradacion> getDegradaciones() {
        return degradaciones;
    }

    public void setDegradaciones(List<Degradacion> degradaciones) {
        this.degradaciones = degradaciones;
    }

    public Degradacion getSelectedDegradacion() {
        return selectedDegradacion;
    }

    public void setSelectedDegradacion(Degradacion selectedDegradacion) {
        this.selectedDegradacion = selectedDegradacion;
    }

    public Degradacion[] getSelectedDegradaciones() {
        return selectedDegradaciones;
    }

    public void setSelectedDegradaciones(Degradacion[] selectedDegradaciones) {
        this.selectedDegradaciones = selectedDegradaciones;
    }

    public DegradacionModel getDegradacionModel() {
        List<Impacto> impacto = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
        for (int i = 0; i < impacto.size(); i++) {
            if (impacto.get(i).getAmenaza().getNombre().equals(arbolAmenazasController.getAmenazaActual().getNombre())) {
                degradaciones = degradacionDAO.buscarPorImpacto(impacto.get(i));
                degradacionModel = new DegradacionModel(degradaciones);
            }
        }
        return degradacionModel;
    }

    public void setDegradacionModel(DegradacionModel degradacionModel) {
        this.degradacionModel = degradacionModel;
    }

    public List<Degradacion> getFilteredDegradacion() {
        return filteredDegradacion;
    }

    public void setFilteredDegradacion(List<Degradacion> filteredDegradacion) {
        this.filteredDegradacion = filteredDegradacion;
    }
    
    /************************************************************************************************/

    public void update() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Degradacion[] seleccionados = this.getSelectedDegradaciones();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            this.setSelectedDegradaciones(null);
            anadirMensajeError(messages.getString("ERRTABDEG"));
        } else if (tamano != 1) {
            this.setSelectedDegradaciones(null);
            anadirMensajeError(messages.getString("ERRTABDEG1"));
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }
    
    public void updateDegradacion() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Degradacion[] seleccionados = this.getSelectedDegradaciones();
        int tamano = seleccionados.length;
        Degradacion seleccionado = seleccionados[0];
        this.setSelectedDegradaciones(null);

        if (seleccionado.getGrado()== null) {
            anadirMensajeError(messages.getString("ERRTABDEG2"));
        } else if (seleccionado.getProbabilidad() == null) {
            anadirMensajeError(messages.getString("ERRTABDEG3"));
        } else {
            degradacionDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABDEG"));
            RequestContext.getCurrentInstance().update("form");
        }

    }
}
