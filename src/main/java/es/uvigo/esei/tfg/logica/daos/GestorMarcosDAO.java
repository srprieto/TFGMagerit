/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorMarcosDAO {
    public void crearNuevoMarco(String nombre, String descripcion);
}
