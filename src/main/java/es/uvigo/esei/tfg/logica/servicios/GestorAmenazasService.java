/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorAmenazasService {
    Amenaza crearAmenaza(String codigo, String nombre, String descripcion, Double probabilidadOcurrencia , Double gradoDegradacionBase, TipoAmenaza tipoAmenaza,Proyecto proyecto);
    void crearNuevaAmenaza(String codigo, String nombre, String descripcion, Double probabilidadOcurrencia , Double gradoDegradacionBase, TipoAmenaza tipoAmenaza,Proyecto proyecto);
}
