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
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.ProyectoModel;
import es.uvigo.esei.tfg.logica.daos.GestorProyectosDAO;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaProyectosController")
@SessionScoped
public class TablaProyectosController implements Serializable {

    private Proyecto proyecto;
    private List<Proyecto> proyectos;
    private Proyecto selectedProyecto;
    private Proyecto[] selectedProyectos;
    private ProyectoModel proyectoModel;
    private List<Proyecto> filteredProyectos;

    @Inject
    ProyectoDAO proyectoDAO;
    
    @Inject
    GestorProyectosDAO gestorDAO;
     
     @Inject @LoggedIn Usuario usuarioActual;

    public TablaProyectosController() {

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

    public Proyecto[] getSelectedProyectos() {
        return selectedProyectos;
    }

    public void setSelectedProyectos(Proyecto[] selectedProyectos) {
        this.selectedProyectos = selectedProyectos;
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
        Usuario creador = usuarioActual;
        System.out.println("hola" + creador);
        proyectos = proyectoDAO.buscarPorCreador(creador);
        proyectoModel = new ProyectoModel(proyectos);
        return proyectoModel;
    }

    public void update() {
        Proyecto[] seleccionados = this.getSelectedProyectos();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun proyecto");
        } else if (tamano != 1) {
            anadirMensajeError("Solo puede seleccionar un proyecto para editarlo");
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateProyecto() {
        Proyecto[] seleccionados = this.getSelectedProyectos();
        int tamano = seleccionados.length;

        Proyecto seleccionado = seleccionados[0];
        if (seleccionado.getNombre().equals("")) {
            anadirMensajeError("Tienes que introducir un nombre para el proyecto");
        } else if (seleccionado.getDescripcion().equals("")) {
            anadirMensajeError("Tienes que introducir una descripción para el proyecto");
        } else if (gestorDAO.existeProyecto(seleccionado.getNombre()) == true) {
            anadirMensajeError("Ya existe un Proyecto con ese nombre");
        } else {
            proyectoDAO.actualizar(seleccionado);
            anadirMensajeCorrecto("El proyecto ha sido modificado correctamente");
            RequestContext.getCurrentInstance().update("form");
        }
    }

    public void eliminar() {
        Proyecto[] seleccionados = this.getSelectedProyectos();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun proyecto");
        } else {
            RequestContext.getCurrentInstance().execute("multiDialog.show();");
        }
    }
    
     public void eliminarProyectos(){
        Proyecto[] lista = this.getSelectedProyectos();
        int tamano = lista.length;
        for (int i=0; i<tamano; i++)
        {
            Proyecto pro= lista[i];
            proyectoDAO.eliminar(pro);
        }
        if(tamano == 1){
            anadirMensajeCorrecto("El proyecto ha sido eliminado correctamente");
        }else{
            anadirMensajeCorrecto("Los proyectos fueron eliminados correctamente");
        }
    }
}
