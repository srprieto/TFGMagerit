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
public interface GestorUsuariosDAO {
     public boolean autenticarUsuario(String login, String passwordPlano);
     public Usuario recuperarDatosUsuario(String login);
     public void crearNuevoUsuario(String login, String password, TipoUsuario tipusu);
     public Usuario actualizarDatosCliente(Usuario datosUsuario);
     public Usuario actualizarPassword(long idUsuario, String password);
     public Usuario actualizarUltimoAcceso(long idUsuario);
     public boolean existeUsuario(String login);
     public TipoUsuario tipoUsuario(String login);
     public void eliminarUsuario(Usuario usuario); 
     public List<Usuario> usuario();
}
