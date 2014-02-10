/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "activoController")
@SessionScoped
public class ActivoController implements Serializable {

    private Activo activoEnEdicion;
    
    private List<TipoActivo> tiposActivos;
    private List<CriterioValoracion> valorBase;
    private List<GrupoActivos> grupoActivos;
 
    
    @Inject 
    ActivoDAO activoDAO;
    
    @Inject
    TipoActivoDAO  tipoActivoDAO;
    
    @Inject
    GrupoActivosDAO  grupoActivoDAO;
            
    public ActivoController() {
        
        
    }
    
    @PostConstruct
    private void inicializar(){
        activoEnEdicion = new Activo();
        tiposActivos = tipoActivoDAO.buscarTodos();
        grupoActivos = grupoActivoDAO.buscarTodos();
    }

    public Activo getActivoEnEdicion() {
        return activoEnEdicion;
    }

    public void setActivoEnEdicion(Activo activoEnEdicion) {
        this.activoEnEdicion = activoEnEdicion;
    }
    
     public List<TipoActivo> getTiposActivos() {
        return tiposActivos;
    }

    public void setTiposActivos(List<TipoActivo> tiposActivos) {
        this.tiposActivos = tiposActivos;
    }
    
    public List<CriterioValoracion> getValorBase() {
        return valorBase;
    }

    public void setValorBase(List<CriterioValoracion> valorBase) {
        this.valorBase = valorBase;
    }
    
     public List<GrupoActivos> getGrupoActivos() {
        return grupoActivos;
    }

    public void setGrupoActivos(List<GrupoActivos> grupoActivos) {
        this.grupoActivos = grupoActivos;
    }
    
    public void doGuargar(){
         activoDAO.crear(activoEnEdicion);  
    }
}
