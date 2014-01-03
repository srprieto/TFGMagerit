package es.uvigo.esei.tfg.logica.daos;

/**
 *
 * 
 * 
 */

public interface GenericoDAO<T> {
    T crear(T entidad);
    T actualizar(T entidad);
    void eliminar(T entidad);
    T buscarPorId(Object id);
}
