/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorProyectosJPA implements GestorProyectosDAO {

    @Inject
    ProyectoDAO proyectoDAO;
  
    
    @Override
    public void crearNuevoProyecto(String nombre, String descripcion, MarcoTrabajo marcoTrabajo, Usuario creador) {
        // Crear el usuario 
        Proyecto nuevo = new Proyecto(nombre, descripcion, marcoTrabajo, creador ,null);
        proyectoDAO.crear(nuevo);
    }
}
