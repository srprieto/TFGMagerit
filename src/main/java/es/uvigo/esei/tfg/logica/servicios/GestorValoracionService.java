/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorValoracionService {
    void crearNuevaValoracion(Double valor,String justificacion, Activo activo, Dimension dimension);
}
