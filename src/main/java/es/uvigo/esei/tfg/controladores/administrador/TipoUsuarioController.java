/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.model.SelectItem;

/**
 *
 * @author Saul
 */
@Named(value = "tipoUsuarioController")
@SessionScoped
public class TipoUsuarioController implements Serializable {

 
    public TipoUsuarioController() {
    }
    
    /*Función necesaria para mostrar los tipos de usuario en un select*/
    public SelectItem[] getTipoValues() {
        SelectItem[] items = new SelectItem[TipoUsuario.values().length];
        int i = 0;
        for (TipoUsuario g : TipoUsuario.values()) {
            items[i++] = new SelectItem(g, g.getLabel());
        }
        return items;
    }

}
