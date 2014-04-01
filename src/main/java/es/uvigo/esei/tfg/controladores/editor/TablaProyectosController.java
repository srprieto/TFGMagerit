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
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.ProyectoModel;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorProyectosService;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
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

    private String nomMarco;

    @Inject
    ProyectoDAO proyectoDAO;

    @Inject
    MarcoTrabajoDAO marcoTrabajoDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    DependenciaDAO dependenciaDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    GestorProyectosService gestorProyectosService;

    @Inject
    @LoggedIn
    Usuario usuarioActual;

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

    public String getNomMarco() {
        return nomMarco;
    }

    public void setNomMarco(String nomMarco) {
        this.nomMarco = nomMarco;
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

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public ProyectoModel getProyectoModel() {
        Usuario creador = usuarioActual;
        proyectos = proyectoDAO.buscarPorCreador(creador);
        proyectoModel = new ProyectoModel(proyectos);
        return proyectoModel;
    }

    public void update() {
        Proyecto[] seleccionados = this.getSelectedProyectos();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            this.setSelectedProyectos(null);
            anadirMensajeError("No ha seleccionado ningun proyecto");
        } else if (tamano != 1) {
            this.setSelectedProyectos(null);
            anadirMensajeError("Solo puede seleccionar un proyecto para editarlo");
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateProyecto() {
        Proyecto[] seleccionados = this.getSelectedProyectos();
        int tamano = seleccionados.length;
        Proyecto seleccionado = seleccionados[0];
        Long id = seleccionado.getId();
        this.setSelectedProyectos(null);

        if (seleccionado.getNombre().equals("")) {
            anadirMensajeError("Tienes que introducir un nombre para el proyecto");
        } else if (seleccionado.getDescripcion().equals("")) {
            anadirMensajeError("Tienes que introducir una descripción para el proyecto");
        } else if (gestorProyectosService.existeProyecto(seleccionado.getNombre()) == true && gestorProyectosService.existeId(seleccionado.getNombre()) != id) {
            anadirMensajeError("Ya existe un Proyecto con ese nombre");
        } else {
            seleccionado.setFechaModificacion(Calendar.getInstance().getTime());
            MarcoTrabajo marco = marcoTrabajoDAO.buscarPorNombre(seleccionado.getMarcoTrabajo().getNombre());
            seleccionado.setMarcoTrabajo(marco);
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

    public void eliminarProyectos() {
        Proyecto[] lista = this.getSelectedProyectos();
        
        List<Activo> activoEliminar = new ArrayList<>();
        List<Impacto> impactoEliminar = new ArrayList<>();
        List<Dependencia> dependenciasEliminar = new ArrayList<>();
        List<Valoracion> valoracionesEliminar = new ArrayList<>();
        List<Degradacion> degradacionEliminar = new ArrayList<>();
        List<Usuario> editores = new ArrayList<>();
        
        int tamano = lista.length;
        
        for (int i = 0; i < tamano; i++) {
            Proyecto pro = lista[i];
            activoEliminar = activoDAO.buscarActivosProyecto(pro);
            for (int j = 0; j < activoEliminar.size(); j++) {
                impactoEliminar = impactoDAO.buscarAmenazasActivo(activoEliminar.get(j));
                for (int s = 0; s < impactoEliminar.size(); s++) {
                    degradacionEliminar = degradacionDAO.buscarPorImpacto(impactoEliminar.get(s));
                    for (int z = 0; z < degradacionEliminar.size(); z++) {
                        degradacionDAO.eliminar(degradacionEliminar.get(z));
                    }
                    Amenaza amenazaEliminar = impactoEliminar.get(s).getAmenaza();
                    impactoDAO.eliminar(impactoEliminar.get(s));
                    amenazaDAO.eliminar(amenazaEliminar);
                }
                dependenciasEliminar = dependenciaDAO.buscarPorPrincipal(activoEliminar.get(j));
                for (int s = 0; s < dependenciasEliminar.size(); s++) {
                    dependenciaDAO.eliminar(dependenciasEliminar.get(s));
                }
                valoracionesEliminar = valoracionDAO.buscarPorActivo(activoEliminar.get(j));
                for (int s = 0; s < valoracionesEliminar.size(); s++) {
                    valoracionDAO.eliminar(valoracionesEliminar.get(s));
                }
                activoDAO.eliminar(activoEliminar.get(j));
            }

            pro.setEditores(editores);
            proyectoDAO.actualizar(pro);
            proyectoDAO.eliminar(pro);
        }
        if (tamano == 1) {
            activoEliminar.clear();
            impactoEliminar.clear();
            degradacionEliminar.clear();
            anadirMensajeCorrecto("El proyecto ha sido eliminado correctamente");
        } else {
            activoEliminar.clear();
            impactoEliminar.clear();
            degradacionEliminar.clear();
            anadirMensajeCorrecto("Los proyectos fueron eliminados correctamente");
        }
    }

    public void cancelar() throws IOException {
        this.setSelectedProyectos(null);
        RequestContext.getCurrentInstance().execute("multiEditDialog.hide();");
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedProyectos(null);
        context.redirect("misproyectos.xhtml");
    }

    public List<String> getMarcos() {
        List<String> nombres = new ArrayList<>();
        Proyecto[] proyecto = this.getSelectedProyectos();
        Proyecto elegido = proyecto[0];
        MarcoTrabajo marco = elegido.getMarcoTrabajo();
        List<MarcoTrabajo> marcos = marcoTrabajoDAO.buscarTodos();
        for (int i = 0; i < marcos.size(); i++) {
            if (marcos.get(i).getNombre().equals(marco.getNombre())) {
                nombres.add(0, marcos.get(i).getNombre());
            } else {
                nombres.add(i, marcos.get(i).getNombre());
            }
        }
        marcos.clear();
        return nombres;
    }
}
