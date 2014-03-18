/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class GrupoActivosJPA extends GenericoJPA<GrupoActivos> implements GrupoActivosDAO {

    @Override
    public List<GrupoActivos> buscarTodos() {
      
       Query q = em.createQuery("SELECT object(p) FROM GrupoActivos AS p");
       return q.getResultList();
      
    }
    
    @Override
     public GrupoActivos buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM GrupoActivos AS u " +
                                 "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<GrupoActivos> resultados = q.getResultList();

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
