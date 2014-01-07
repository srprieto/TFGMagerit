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
      
       Query q = em.createQuery("SELECT object(p) FROM Grupoactivos AS p");
       return q.getResultList();
      
    }
}
