package es.uvigo.esei.tfg.logica.daos;

import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * 
 * @param <T>
 */

public class GenericoJPA<T> implements GenericoDAO<T> {

    @PersistenceContext(unitName = "tfgPU")
    protected  EntityManager em;


    @Override
    public T crear(T entidad) {
       em.persist(entidad);      // Crea una nueva tupla en la BD con los datos de "entidad"
                                // -> ademas genera su ID           
       return entidad;
    }

    @Override
    public T actualizar(T entidad) {
       return em.merge(entidad);   // Actualiza los datos de "entidad" en su correspondiente tupla BD
    }

    @Override
    public void eliminar(T entidad) {
        em.remove(em.merge(entidad));  // Actualiza y elimina
    }

    @Override
    public T buscarPorId(Object id) {
        Class<T> claseEntidad = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                             // Identifica la clase real de las entidades gestionada por este objeto (T.class)

        return em.find(claseEntidad, id);
    }
}
