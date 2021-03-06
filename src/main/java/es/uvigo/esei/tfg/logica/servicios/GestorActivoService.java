/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorActivoService {
     void crearNuevoActivo(String codigo, String nombre, String descripcion, String responsable, String propietario, String ubicacion, Long cantidad,Proyecto proyecto, TipoActivo tipo, GrupoActivos grupo);
     Long existeId (String nombre);
     boolean existeActivo(String nombre);
}
