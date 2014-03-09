/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class ValoracionJPA extends GenericoJPA<Valoracion> implements ValoracionDAO {
    
   @Override
    public List<Valoracion> buscarTodos(Activo activo) {
        Query q = em.createQuery("SELECT object(u) FROM Valoracion as u"+
                                 "  WHERE u.activo = :activo");
        q.setParameter("activo", activo);
        return q.getResultList(); 
    }
}
