/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorDegradacionBean implements GestorDegradacionService {

    @Inject
    DegradacionDAO degradacionDAO;
    
    @Override
    public void crearNuevaDegradacion(Double grado, Double probabilidad, Impacto impacto, Dimension dimension) {
        
        Degradacion nuevo = new Degradacion(grado, probabilidad, impacto, dimension);
        degradacionDAO.crear(nuevo);
    }
}
