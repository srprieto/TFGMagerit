/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorImpactoBean implements GestorImpactoService {

   @Inject 
    ImpactoDAO impactoDAO;

    @Override
    public void crearNuevoImpacto(Date fecha, Activo activo, Amenaza amenaza) {
        Impacto nuevo = new Impacto(fecha,activo, amenaza);
        impactoDAO.crear(nuevo);
    }
}
