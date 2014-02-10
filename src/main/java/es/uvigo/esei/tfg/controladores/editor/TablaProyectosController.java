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
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.controladores.modelos.ProyectoModel;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import java.io.Serializable;  
import java.util.List;    
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
  
@Named(value = "tablaProyectosController")
@SessionScoped 
public class TablaProyectosController implements Serializable { 

    private Proyecto proyecto;
    private List<Proyecto> proyectos;
    private Proyecto selectedProyecto;
    private ProyectoModel proyectoModel;  
    private List<Proyecto> filteredProyectos;

    
    @Inject
    ProyectoDAO proyectoDAO;
    
    @Inject
    LoginController loginController;
    
    public TablaProyectosController() {
        
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
    
    public List<Proyecto> getFilteredProyectos() {   
        return filteredProyectos;  
    }  
  
    public void setFilteredProyectos(List<Proyecto> filteredProyectos) {  
        this.filteredProyectos = filteredProyectos;  
    }
  
    public Proyecto getSelectedProyecto() { 
        return selectedProyecto;  
    }  
  
    public void setSelectedProyecto(Proyecto selectedProyecto) {  
        this.selectedProyecto = selectedProyecto;  
    } 
    
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<Proyecto> getProyectos() {
        proyectos = proyectoDAO.buscarTodos();
        return proyectos;
    }

    public void setUsuarios(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
    
    public ProyectoModel getProyectoModel() {
        Usuario creador = loginController.getUsuarioActual();
        proyectos = proyectoDAO.buscarPorCreador(creador);
        proyectoModel = new ProyectoModel(proyectos);
        return proyectoModel;   
    }  
}                 