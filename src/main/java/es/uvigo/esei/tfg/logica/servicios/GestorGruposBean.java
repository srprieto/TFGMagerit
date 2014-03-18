/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorGruposBean implements GestorGruposService {
    
    @Inject 
    GrupoActivosDAO grupoActivosDAO;

    @Override
    public void crearNuevoGrupo(String abreviatura, String nombre) {
        // Crear el grupo 
        GrupoActivos nuevo = new GrupoActivos(abreviatura,nombre);
        grupoActivosDAO.crear(nuevo);
    }
}
