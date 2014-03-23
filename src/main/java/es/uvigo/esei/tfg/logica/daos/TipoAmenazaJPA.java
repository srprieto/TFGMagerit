/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Saul
 */
@Stateless
public class TipoAmenazaJPA extends GenericoJPA<TipoAmenaza> implements TipoAmenazaDAO {

    @Override
    public List<TipoAmenaza> buscarTodos() {

        Query q = em.createQuery("SELECT object(p) FROM TipoAmenaza AS p");
        return q.getResultList();

    }

    @Override
    public List<TipoAmenaza> buscarTipoActivo(TipoActivo tiposActivo) {

        Query q = em.createQuery("SELECT object(p) FROM TipoAmenaza AS p"
                + "  WHERE p.tiposActivo = :tiposActivo");
        q.setParameter("tiposActivo", tiposActivo);
        return q.getResultList();
    }
    
    @Override
    public List<TipoAmenaza> buscarDimension(Dimension dimensiones) {

        Query q = em.createQuery("SELECT object(p) FROM TipoAmenaza AS p"
                + "  WHERE p.dimensiones = :dimensiones");
        q.setParameter("dimensiones", dimensiones);
        return q.getResultList();
    }
    
     @Override
    public List<TipoAmenaza> buscarMarco(MarcoTrabajo marcoTrabajo) {

        Query q = em.createQuery("SELECT object(p) FROM TipoAmenaza AS p"
                + "  WHERE p.marcoTrabajo = :marcoTrabajo");
        q.setParameter("marcoTrabajo", marcoTrabajo);
        return q.getResultList();
    }

    @Override
    public TipoAmenaza buscarPorNombre(String nombre) {
        Query q = em.createQuery("SELECT object(u) FROM TipoAmenaza AS u "
                + "  WHERE u.nombre = :nombre");
        q.setParameter("nombre", nombre);

        List<TipoAmenaza> resultados = q.getResultList();

        if (resultados == null) {
            return null;  // No encontrado
        } else if (resultados.size() != 1) {
            return null; // No encontrado
        } else {
            return resultados.get(0);  // Devuelve el encontrado
        }
    }

}
