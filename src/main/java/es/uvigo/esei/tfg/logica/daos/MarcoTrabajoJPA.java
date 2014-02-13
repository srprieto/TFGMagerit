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
    
    @Override
    public MarcoTrabajo buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM MarcoTrabajo AS u " +
                                 "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<MarcoTrabajo> resultados = q.getResultList();

        if (resultados ==null) {
            return null;  // No encontrado
        }
        else if (resultados.size() != 1){
            return null; // No encontrado
        }
        else {
            return resultados.get(0);  // Devuelve el encontrado
        }
    }

}
