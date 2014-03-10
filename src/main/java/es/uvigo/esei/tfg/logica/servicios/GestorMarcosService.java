/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.servicios;

import javax.ejb.Local;

/**
 *
 * @author Saul
 */
@Local
public interface GestorMarcosService {
    public void crearNuevoMarco(String nombre, String descripcion);
    public boolean existeMarco(String nombre);
    public Long existeId (String nombre);
}
