/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores;

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

    /**
     * Creates a new instance of EnumeradoController
     */
    public TipoUsuarioController() {
    }
    
    public SelectItem[] getTipoValues() {
        SelectItem[] items = new SelectItem[TipoUsuario.values().length];
        int i = 0;
        for(TipoUsuario g: TipoUsuario.values()) {
        items[i++] = new SelectItem(g, g.getLabel());
        }
        return items;
    }
    
}
