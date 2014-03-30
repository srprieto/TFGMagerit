/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Saul
 */

@Local
public interface UsuarioDAO extends GenericoDAO<Usuario> {
    
    public Usuario buscarPorLogin(String login);
    public List<Usuario> buscarPorTipo(TipoUsuario tipo);
    public List<Usuario> buscarTodos();
    public int contador();
    public List<Usuario> usuario(String pass);
}
