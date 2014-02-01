/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Saul
 */
public class UsuarioModel extends ListDataModel<Usuario> implements SelectableDataModel<Usuario>{
    
    public UsuarioModel() {  
    }  
  
    public UsuarioModel(List<Usuario> data) {  
        super(data);  
    }  
      
    @Override  
    public Usuario getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Usuario> usuarios = (List<Usuario>) getWrappedData();  
          
        for(Usuario usuario : usuarios) {  
            if(usuario.getLogin().equals(rowKey))  
                return usuario;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Usuario usuario) {  
        return usuario.getLogin();  
    }  
} 

