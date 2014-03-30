/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface CriterioValoracionDAO extends GenericoDAO<CriterioValoracion>{
    List<CriterioValoracion> buscarMarco(MarcoTrabajo marcoTrabajo);
}
