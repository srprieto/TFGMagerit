/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface TipoActivoDAO extends GenericoDAO<TipoActivo>{

 public List<TipoActivo> buscarTodos();
 public List<TipoActivo> buscarTipoActivosSinPadre();
 public TipoActivo buscarPorNombre(String nombre);
 List<TipoActivo> buscarMarco(MarcoTrabajo marcoTrabajo);
}
