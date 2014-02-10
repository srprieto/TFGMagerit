/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class MarcoTrabajoJPA extends GenericoJPA<MarcoTrabajo> implements MarcoTrabajoDAO {

     @Override
    public List<MarcoTrabajo> buscarTodos() {
        Query q = em.createQuery("SELECT object(u) FROM MarcoTrabajo as u");
        return q.getResultList();
    }
}
