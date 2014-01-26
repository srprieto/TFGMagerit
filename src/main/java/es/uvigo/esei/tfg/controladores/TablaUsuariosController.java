/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores;
/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.es.tfg.entidades.usuario.UsuarioModel;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.Serializable;  
import java.util.List;    
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
  
@Named(value = "tablaUsuariosController")
@SessionScoped 
public class TablaUsuariosController implements Serializable {  

    private Usuario usuario;
    private List<Usuario> usuarios;
    private Usuario selectedUsuario; 
    private Usuario[] selectedUsuarios;
    private UsuarioModel usuarioModel;  
    
    @Inject
    UsuarioDAO usuarioDAO;
    
    public TablaUsuariosController() {
        
    }
    
    public Usuario[] getSelectedUsuarios() {  
        return selectedUsuarios;  
    }  
    public void setSelectedUsuarios(Usuario[] selectedUsuarios) {  
        this.selectedUsuarios = selectedUsuarios;  
    }  
  
    public Usuario getSelectedUsuario() {  
        return selectedUsuario;  
    }  
  
    public void setSelectedUsuario(Usuario selectedUsuario) {  
        this.selectedUsuario = selectedUsuario;  
    } 
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
       if (usuarios == null) {
            usuarios = usuarioDAO.buscarTodos();
        }
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public UsuarioModel getUsuarioModel() {
        if (usuarios == null) {
            usuarios = usuarioDAO.buscarTodos();
        }
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;  
    }  

}                 