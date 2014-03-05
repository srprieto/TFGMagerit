/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "dependenciaController")
@SessionScoped
public class DependenciaController implements Serializable {

    private Activo principal;
    private Activo dependiente;
    private Double grado;
    private String justificante;

    List<Activo> activos;

    private Dependencia dependenciaEnEdicion;

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

    @PostConstruct
    private void inicializar() {
        dependenciaEnEdicion = new Dependencia();
        activos = this.getActivos();

    }

    public List<Activo> getActivos() {
        Proyecto actual = proyectoController.getProyectoActual();
        activos = activoDAO.buscarActivosProyecto(actual);
        Activo seleccionado = arbolActivoController.getActivoActual();
        for (int i = 0; i < activos.size(); i++) {
            if (activos.get(i).getNombre().equals(seleccionado.getNombre())) {
                activos.remove(i);
            }
        }
        List<Dependencia> asociados = dependenciaDAO.buscarPorPrincipal(seleccionado);
        for (int i = 0; i < activos.size(); i++) {
            for (int j = 0; j < asociados.size(); j++) {
                if (asociados.get(j).getActivoDependiente().getNombre().equals(activos.get(i).getNombre())) {
                    activos.remove(i);
                }
            }
        }

            return activos;
        }

    public void setActivos(List<Activo> activos) {
        this.activos = activos;
    }

    public Dependencia getDependenciaEnEdicion() {
        return dependenciaEnEdicion;
    }

    public void setDependenciaEnEdicion(Dependencia dependenciaEnEdicion) {
        this.dependenciaEnEdicion = dependenciaEnEdicion;
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

}
