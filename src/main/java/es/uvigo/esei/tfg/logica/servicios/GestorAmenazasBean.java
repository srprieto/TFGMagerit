/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorAmenazasBean implements GestorAmenazasService {

    @Inject
    AmenazaDAO amenazaDAO;

    @Override
    public void crearNuevaAmenaza(String codigo, String nombre, String descripcion, Double probabilidadOcurrencia, Double gradoDegradacionBase, TipoAmenaza tipoAmenaza, Proyecto proyecto) {
        // Crear el usuario 
        Amenaza nuevo = new Amenaza(codigo, nombre, descripcion, probabilidadOcurrencia, gradoDegradacionBase, tipoAmenaza, proyecto);
        amenazaDAO.crear(nuevo);
    }

    @Override
    public Amenaza crearAmenaza(String codigo, String nombre, String descripcion, Double probabilidadOcurrencia, Double gradoDegradacionBase, TipoAmenaza tipoAmenaza, Proyecto proyecto) {
        // Crear el usuario 
        Amenaza nuevo = new Amenaza(codigo, nombre, descripcion, probabilidadOcurrencia, gradoDegradacionBase, tipoAmenaza, proyecto);
        amenazaDAO.crear(nuevo);

        return nuevo;
    }
}
