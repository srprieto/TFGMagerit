/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface ActivoDAO extends GenericoDAO<Activo>{

    List<Activo> buscarPorTipoActivo(TipoActivo tipo);
    List<Activo> buscarActivosProyecto(Proyecto proyecto);
    Activo buscarPorNombre(String nombre);
    
}
