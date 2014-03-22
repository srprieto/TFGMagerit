/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface TipoAmenazaDAO extends GenericoDAO<TipoAmenaza>{
    List<TipoAmenaza> buscarTodos();
    List<TipoAmenaza> buscarTipoActivo(TipoActivo tiposActivo);
    List<TipoAmenaza> buscarDimension(Dimension dimensiones);
    TipoAmenaza buscarPorNombre(String nombre);
}
