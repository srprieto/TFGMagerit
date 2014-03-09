/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.esei.tfg.controladores.modelos.ValoracionModel;
import es.uvigo.esei.tfg.logica.daos.DimensionDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */

@Named(value = "tablaValoracionController")
@SessionScoped
public class TablaValoracionController implements Serializable{
    private Valoracion valoracion;
    private List<Valoracion> valoraciones;
    private Valoracion selectedValoracion;
    private Valoracion[] selectedValoraciones;
    private ValoracionModel valoracionModel;
    private List<Valoracion> filteredValoracion;
    
    @Inject
    DimensionDAO dimensionDAO;
    
    @Inject
    ProyectoController proyectoController;
    
    @Inject
    ValoracionDAO valoracionDAO;
    
    @Inject
    ArbolActivosController arbolActivosController;
    
    
     public TablaValoracionController() {

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

    public Valoracion getValoracion() {
        return valoracion;
    }

    public void setValoracion(Valoracion valoracion) {
        this.valoracion = valoracion;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public Valoracion getSelectedValoracion() {
        return selectedValoracion;
    }

    public void setSelectedValoracion(Valoracion selectedValoracion) {
        this.selectedValoracion = selectedValoracion;
    }

    public Valoracion[] getSelectedValoraciones() {
        return selectedValoraciones;
    }

    public void setSelectedValoraciones(Valoracion[] selectedValoraciones) {
        this.selectedValoraciones = selectedValoraciones;
    }

    public void setValoracionModel(ValoracionModel valoracionModel) {
        this.valoracionModel = valoracionModel;
    }

    public List<Valoracion> getFilteredValoracion() {
        return filteredValoracion;
    }

    public void setFilteredValoracion(List<Valoracion> filteredValoracion) {
        this.filteredValoracion = filteredValoracion;
    }
    
    public ValoracionModel getValoracionModel() {
        valoraciones = valoracionDAO.buscarTodos(arbolActivosController.getActivoActual());
        valoracionModel = new ValoracionModel(valoraciones);
        return valoracionModel;
    }
    
}
