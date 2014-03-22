/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorDegradacionService {
    void crearNuevaDegradacion(Double grado, Double probabilidad, Impacto impacto, Dimension dimension);
}
