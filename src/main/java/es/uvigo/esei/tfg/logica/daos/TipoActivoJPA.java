/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
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
      
       Query q = em.createQuery("SELECT object(p) FROM TipoActivo AS p");
       return q.getResultList();
      
    }
    
    @Override
    public List<TipoActivo> buscarTipoActivosSinPadre(){
        Query q = em.createQuery("SELECT object(u) FROM TipoActivo as u "+
                                 "  WHERE u.tipoActivoPadre = null");
        return q.getResultList();
    }
 
}
