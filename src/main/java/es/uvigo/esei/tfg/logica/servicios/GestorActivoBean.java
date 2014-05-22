/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorActivoBean implements GestorActivoService {

  @Inject
  ActivoDAO activoDAO;
  
  @Override
    public void crearNuevoActivo(String codigo, String nombre, String descripcion, String responsable, String propietario, String ubicacion, Long cantidad,Proyecto proyecto, TipoActivo tipo, GrupoActivos grupo) {
        // Crear el usuario 
        Activo nuevo = new Activo(codigo,nombre,descripcion,responsable,propietario,ubicacion,cantidad,proyecto,tipo,grupo);
        activoDAO.crear(nuevo);
    }
    
    @Override
    public boolean existeActivo(String nombre) {
        return (activoDAO.buscarPorNombre(nombre) != null);
    }
    
    @Override
    public Long existeId (String nombre){
        Activo principal = activoDAO.buscarPorNombre(nombre);
        return principal.getId();
    }
    
}
