/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class DependenciaJPA extends GenericoJPA<Dependencia> implements DependenciaDAO {

    @Override
    public List<Dependencia> buscarTodos() {
        Query q = em.createQuery("SELECT object(u) FROM Dependencia as u");
        return q.getResultList();
    }

    @Override
    public List<Dependencia> buscarPorPrincipal(Activo activoPrincipal) {
        Query q = em.createQuery("SELECT object(u) FROM Dependencia AS u "
                + "  WHERE u.activoPrincipal = :activoPrincipal");
        q.setParameter("activoPrincipal", activoPrincipal);
        List<Dependencia> resultados = q.getResultList();
        return resultados;
    }

    @Override
    public List<Dependencia> buscarporDependiente(Activo activoDependiente) {
        Query q = em.createQuery("SELECT object(u) FROM Dependencia AS u "
                + "  WHERE u.activoDependiente = :activoDependiente");
        q.setParameter("activoDependiente", activoDependiente);
        List<Dependencia> resultados = q.getResultList();
        if (resultados == null) {
            return null;  // No encontrado
        } else {
            return resultados;
        }
    }
}
