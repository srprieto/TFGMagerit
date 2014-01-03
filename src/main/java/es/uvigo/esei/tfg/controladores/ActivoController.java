/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
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
    
    @Inject 
    ActivoDAO activoDAO;
    
    //@Inject
    //TipoActivoDAO  tipoActivoDAO;
            
    public ActivoController() {
        
        
    }
    
    
    @PostConstruct
    private void inicializar(){
        activoEnEdicion = new Activo();
        //tiposActivos = tipoActivoDAO.buscarTodos();
        
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
    
    public void doGuargar(){
           activoDAO.crear(activoEnEdicion);
    
    }
}
