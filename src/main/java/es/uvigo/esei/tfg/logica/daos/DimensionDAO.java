/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface DimensionDAO extends GenericoDAO<Dimension>{
    List<Dimension> buscarTodos(MarcoTrabajo marcoTrabajo);
    Dimension buscarPorNombre(String nombre);
    List<Dimension> buscarMarco(MarcoTrabajo marcoTrabajo);
}
