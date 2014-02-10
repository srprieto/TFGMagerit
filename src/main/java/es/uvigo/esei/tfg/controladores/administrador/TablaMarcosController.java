/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.esei.tfg.controladores.modelos.MarcoModel;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
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

  
@Named(value = "tablaMarcosController")
@SessionScoped 
public class TablaMarcosController implements Serializable{
    
    private MarcoTrabajo marco;
    private List<MarcoTrabajo> marcos;
    private MarcoTrabajo selectedMarco;
    private MarcoModel marcoModel;  
    private List<MarcoTrabajo> filteredMarcos;
    
    @Inject
    MarcoTrabajoDAO marcoDAO;
    
    @Inject
    MarcosController marcoController;
    
    public TablaMarcosController() {
        
    }
    
    /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    protected void anadirMensajeCorrecto(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }

    public MarcoTrabajo getMarco() {
        return marco;
    }

    public void setMarco(MarcoTrabajo marco) {
        this.marco = marco;
    }

    public List<MarcoTrabajo> getMarcos() {
        marcos = marcoDAO.buscarTodos();
        return marcos;
    }

    public void setMarcos(List<MarcoTrabajo> marcos) {
        this.marcos = marcos;
    }

    public MarcoTrabajo getSelectedMarco() {
        return selectedMarco;
    }

    public void setSelectedMarco(MarcoTrabajo selectedMarco) {
        System.out.println(selectedMarco);
        this.selectedMarco = selectedMarco;
    }

    public List<MarcoTrabajo> getFilteredMarcos() {
        return filteredMarcos;
    }

    public void setFilteredMarcos(List<MarcoTrabajo> filteredMarcos) {
        this.filteredMarcos = filteredMarcos;
    }
    
    public MarcoModel getMarcoModel() {
        marcos = marcoDAO.buscarTodos();
        marcoModel = new MarcoModel(marcos);
        return marcoModel;   
    }  
    
     public void eliminarMarcos(){
         MarcoTrabajo seleccionado= this.getSelectedMarco();
         marcoDAO.eliminar(seleccionado);
         anadirMensajeCorrecto("El marco ha sido eliminado correctamente");
     }
     
     public void updateMarco(){
         MarcoTrabajo seleccionado= this.getSelectedMarco();
         seleccionado.setNombre(marcoController.getNombre());
         seleccionado.setDescripcion(marcoController.getDescripcion());
         marcoDAO.actualizar(seleccionado);
         marcoController.setNombre(null);
         marcoController.setDescripcion(null);
         anadirMensajeCorrecto("El marco ha sido modificado correctamente");
     }
}  
