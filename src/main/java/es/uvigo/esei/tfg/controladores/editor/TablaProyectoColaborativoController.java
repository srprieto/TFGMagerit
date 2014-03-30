/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.controladores.modelos.ProyectoModel;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorProyectosService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Saul
 */
@Named(value = "tablaProyectoColaborativoController")
@SessionScoped
public class TablaProyectoColaborativoController implements Serializable {

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
    @LoginController.LoggedIn
    Usuario usuarioActual;

    public TablaProyectoColaborativoController() {

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
        Usuario editor = usuarioActual;
        List<Proyecto> resultado = new ArrayList<>();
        List <Usuario> editores = new ArrayList<>();
        List <Proyecto> proyectos = proyectoDAO.buscarTodos();
        for(int i=0;i<proyectos.size();i++){
             editores = proyectos.get(i).getEditores();
             for(int j=0;j<editores.size();j++){
                 if(editores.get(j).getLogin().equals(editor.getLogin())){
                     resultado.add(proyectos.get(i));
                 }
             }
        }
       
        proyectoModel = new ProyectoModel(resultado);
        return proyectoModel;
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
