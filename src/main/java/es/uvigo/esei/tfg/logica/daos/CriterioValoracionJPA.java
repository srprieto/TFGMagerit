/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class CriterioValoracionJPA extends GenericoJPA<CriterioValoracion> implements CriterioValoracionDAO {

     @Override
    public List<CriterioValoracion> buscarMarco(MarcoTrabajo marcoTrabajo) {

        Query q = em.createQuery("SELECT object(p) FROM CriterioValoracion AS p"
                + "  WHERE p.marcoTrabajo = :marcoTrabajo");
        q.setParameter("marcoTrabajo", marcoTrabajo);
        return q.getResultList();
    }
}
