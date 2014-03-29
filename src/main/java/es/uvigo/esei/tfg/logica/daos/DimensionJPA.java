/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class DimensionJPA extends GenericoJPA<Dimension> implements DimensionDAO {

    @Override
    public List<Dimension> buscarTodos(MarcoTrabajo marcoTrabajo) {
        Query q = em.createQuery("SELECT object(u) FROM Dimension as u"
                + "  WHERE u.marcoTrabajo = :marcoTrabajo");
        q.setParameter("marcoTrabajo", marcoTrabajo);
        return q.getResultList();
    }
    

    @Override
    public Dimension buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM Dimension AS u "
                + "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<Dimension> resultados = q.getResultList();

        if (resultados == null) {
            return null;  // No encontrado
        } else if (resultados.size() != 1) {
            return null; // No encontrado
        } else {
            return resultados.get(0);  // Devuelve el encontrado
        }
    }
    
    @Override
    public List<Dimension> buscarMarco(MarcoTrabajo marcoTrabajo) {

        Query q = em.createQuery("SELECT object(p) FROM Dimension AS p"
                + "  WHERE p.marcoTrabajo = :marcoTrabajo");
        q.setParameter("marcoTrabajo", marcoTrabajo);
        return q.getResultList();
    }
    
}
