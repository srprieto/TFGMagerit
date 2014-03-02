/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.editor;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.DependientesModel;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "tablaDependientesController")
@SessionScoped
public class TablaDependientesController implements Serializable {

    private Dependencia dependiente;
    private List<Dependencia> dependientes;
    private Dependencia selectedDependiente;
    private Dependencia[] selectedDependientes;
    private DependientesModel dependientesModel;
    private List<Dependencia> filteredDependientes;
     
    @Inject @LoggedIn Usuario usuarioActual;
    
    @Inject
    DependenciaDAO dependenciaDAO;
    
    @Inject
    ArbolActivosController activos;

    public TablaDependientesController() {

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

    public Dependencia[] getSelectedDependientes() {
        return selectedDependientes;
    }

    public void setSelectedDependientes(Dependencia[] selectedDependencias) {
        this.selectedDependientes = selectedDependencias;
    }

    public List<Dependencia> getFilteredDependencias() {
        return filteredDependientes;
    }

    public void setFilteredDependencias(List<Dependencia> filteredDependientes) {
        this.filteredDependientes = filteredDependientes;
    }

    public Dependencia getSelectedDependencia() {
        return selectedDependiente;
    }

    public void setSelectedDependencia(Dependencia selectedDependencia) {
        this.selectedDependiente = selectedDependencia;
    }

    public Dependencia getDependencia() {
        return dependiente;
    }

    public void setDependencia(Dependencia dependiente) {
        this.dependiente = dependiente;
    }

    public List<Dependencia> getDependientes() {
        dependientes = dependenciaDAO.buscarTodos();
        return dependientes;
    }

    public void setDependientes(List<Dependencia> dependientes) {
        this.dependientes = dependientes;
    }

    public DependientesModel getDependienteModel() {
        Activo actual = activos.getActivoActual();
        dependientes = dependenciaDAO.buscarPorPrincipal(actual);
        dependientesModel = new DependientesModel(dependientes);
        return dependientesModel;
    }
}