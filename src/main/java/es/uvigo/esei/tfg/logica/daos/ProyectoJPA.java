/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class ProyectoJPA extends GenericoJPA<Proyecto> implements ProyectoDAO {

   @Override
    public List<Proyecto> buscarTodos() {
        Query q = em.createQuery("SELECT object(u) FROM Proyecto as u");
        return q.getResultList();
    }
    
    @Override
    public List<Proyecto> buscarPorCreador(Usuario creador) {
        Query q = em.createQuery("SELECT object(u) FROM Proyecto AS u " +
                                 "  WHERE u.creador = :creador");
        q.setParameter("creador", creador);
        List<Proyecto> resultados = q.getResultList(); 
        return resultados;
    }
    
     @Override
     public Proyecto buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM Proyecto AS u " +
                                 "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<Proyecto> resultados = q.getResultList();

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
