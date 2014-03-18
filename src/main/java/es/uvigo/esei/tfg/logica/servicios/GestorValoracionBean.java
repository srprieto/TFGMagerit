/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorValoracionBean implements GestorValoracionService {

    @Inject
    ValoracionDAO valoracionDAO;

    @Override
    public void crearNuevaValoracion(Double valor, String justificacion,Activo activo, Dimension dimension) {
        // Crear el grupo 
        Valoracion nuevo = new Valoracion(valor,justificacion, activo, dimension);
        valoracionDAO.crear(nuevo);
    }
}
