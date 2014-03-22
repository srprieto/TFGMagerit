/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class DegradacionJPA extends GenericoJPA<Degradacion> implements DegradacionDAO {

    @Override
    public List<Degradacion> buscarPorImpacto(Impacto impacto) {
        Query q = em.createQuery("SELECT object(u) FROM Degradacion as u "+
                                 "  WHERE u.impacto = :impacto");
        q.setParameter("impacto", impacto);
        return q.getResultList(); 
    }
}
