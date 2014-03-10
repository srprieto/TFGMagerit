/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorMarcosBean implements GestorMarcosService {

   @Inject
    MarcoTrabajoDAO marcoDAO;
  
    
    @Override
    public void crearNuevoMarco(String nombre, String descripcion) {
        // Crear el usuario 
        MarcoTrabajo nuevo = new MarcoTrabajo(nombre, descripcion);
        marcoDAO.crear(nuevo);
    }
    
    @Override
    public boolean existeMarco(String nombre) {
        return (marcoDAO.buscarPorNombre(nombre) != null);
    }
    
     @Override
    public Long existeId (String nombre){
        MarcoTrabajo principal = marcoDAO.buscarPorNombre(nombre);
        return principal.getId();
    }
}
