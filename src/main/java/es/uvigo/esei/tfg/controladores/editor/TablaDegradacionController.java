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

    public void update() {
        Degradacion[] seleccionados = this.getSelectedDegradaciones();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            this.setSelectedDegradaciones(null);
            anadirMensajeError("No ha seleccionado ninguna degradación");
        } else if (tamano != 1) {
            this.setSelectedDegradaciones(null);
            anadirMensajeError("Solo puede seleccionar una degradacion para editarla");
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }
    
    public void updateDegradacion() {
        Degradacion[] seleccionados = this.getSelectedDegradaciones();
        int tamano = seleccionados.length;
        Degradacion seleccionado = seleccionados[0];
        this.setSelectedDegradaciones(null);

        if (seleccionado.getGrado()== null) {
            anadirMensajeError("Tienes que introducir un grado para la degradación");
        } else if (seleccionado.getProbabilidad() == null) {
            anadirMensajeError("Tienes que introducir una probabilidad de ocurrencia para la degradación");
        } else {
            degradacionDAO.actualizar(seleccionado);
            anadirMensajeCorrecto("La degradacion ha sido modificado correctamente");
            RequestContext.getCurrentInstance().update("form");
        }

    }
}
