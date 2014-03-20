/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class ImpactoJPA extends GenericoJPA<Impacto> implements ImpactoDAO {

    @Override
    public List<Impacto> buscarAmenazasActivo(Activo activo){
        Query q = em.createQuery("SELECT object(u) FROM Impacto as u "+
                                 "  WHERE u.activo = :activo");
        q.setParameter("activo", activo);
        return q.getResultList();
    }
    
    
}
