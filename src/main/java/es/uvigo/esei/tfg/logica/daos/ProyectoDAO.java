/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface ProyectoDAO extends GenericoDAO<Proyecto>{
    List<Proyecto> buscarTodos();
    List<Proyecto> buscarPorCreador(Usuario creador);
    Proyecto buscarPorNombre(String nombre);
    List<Proyecto> buscarMarco(MarcoTrabajo marcoTrabajo);
}
