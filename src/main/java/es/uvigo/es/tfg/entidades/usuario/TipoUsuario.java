/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.es.tfg.entidades.usuario;


/**
 *
 * @author Saul
 */
public enum TipoUsuario {
    ADMINISTRADOR("ADMINISTRADOR"),
    TRABAJADOR("TRABAJADOR");
    
    private final String label;

  private TipoUsuario (String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
    
}
