/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface ValoracionDAO extends GenericoDAO<Valoracion>{
    List<Valoracion> buscarTodos(Activo activo);
    List<Valoracion> buscarPorActivo(Activo activo);
}
