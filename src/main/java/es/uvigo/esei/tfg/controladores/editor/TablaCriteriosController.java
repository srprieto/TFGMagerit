/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.ItemValoracion;
import es.uvigo.esei.tfg.logica.daos.CriterioValoracionDAO;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "tablaCriteriosController")
@SessionScoped
public class TablaCriteriosController implements Serializable {
    
    private CriterioValoracion valoracion;
    private List<CriterioValoracion> valoraciones;
    private List<ItemValoracion> items;
    
    @Inject
    CriterioValoracionDAO   criterioValoracionDAO;
    
    @Inject
    ArbolActivosController arbolActivosController;
    
    /*Funciones GET y SET*/
    
    /************************************************************************************************/

    public List<ItemValoracion> getItems(CriterioValoracion criterio) {
        items = criterio.getItemsValoracion();
        return items;
    }

    public void setItems(List<ItemValoracion> items) {
        this.items = items;
    }

    public CriterioValoracion getValoracion() {
        return valoracion;
    }

    public void setValoracion(CriterioValoracion valoracion) {
        this.valoracion = valoracion;
    }

    public List<CriterioValoracion> getValoraciones() {
        valoraciones = criterioValoracionDAO.buscarMarco(arbolActivosController.getActivoActual().getProyecto().getMarcoTrabajo());
        return valoraciones;
    }

    public void setValoraciones(List<CriterioValoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

   /************************************************************************************************/
   
}
