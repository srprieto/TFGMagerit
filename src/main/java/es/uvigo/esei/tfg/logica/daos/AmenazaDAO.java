/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface AmenazaDAO extends GenericoDAO<Amenaza>{
    Amenaza buscarPorNombre(String nombre);
    List<Amenaza> buscarAmenazasProyecto(Proyecto proyecto);
}
