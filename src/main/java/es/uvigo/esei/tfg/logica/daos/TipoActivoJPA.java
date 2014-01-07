/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */

@Stateless
public class TipoActivoJPA extends GenericoJPA<TipoActivo> implements TipoActivoDAO{
    
    @Override
    public List<TipoActivo> buscarTodos() {
      
       Query q = em.createQuery("SELECT object(p) FROM Tipoactivo AS p");
       return q.getResultList();
      
    }
 
}
