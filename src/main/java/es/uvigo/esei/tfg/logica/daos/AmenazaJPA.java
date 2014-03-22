/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class AmenazaJPA extends GenericoJPA<Amenaza> implements AmenazaDAO {

   @Override
     public Amenaza buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM Amenaza AS u " +
                                 "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<Amenaza> resultados = q.getResultList();

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
     
    @Override
    public List<Amenaza> buscarAmenazasProyecto(Proyecto proyecto){
        Query q = em.createQuery("SELECT object(u) FROM Amenaza as u "+
                                 "  WHERE u.proyecto = :proyecto");
        q.setParameter("proyecto", proyecto);
        return q.getResultList();
    }
}
