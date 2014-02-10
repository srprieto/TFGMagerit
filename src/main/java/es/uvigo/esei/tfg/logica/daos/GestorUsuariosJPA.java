package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */

@Stateless
public class GestorUsuariosJPA implements GestorUsuariosDAO {

    @Inject
    UsuarioDAO usuarioDAO;

    @Override
    public boolean autenticarUsuario(String login, String passwordPlano) {
        Usuario usuario;
        boolean resultado = false;
        

        usuario = usuarioDAO.buscarPorLogin(login);
        if (usuario != null) {
            if (usuario.getPassword().equals(passwordPlano)) {
                resultado = true;
               
            }
        }

        return resultado;
    }

    @Override
    public Usuario recuperarDatosUsuario(String login) {
        return usuarioDAO.buscarPorLogin(login);
    }

    @Override
    public void crearNuevoUsuario(String login, String password, TipoUsuario tipusu) {
        // Crear el usuario 
        Usuario nuevo = new Usuario(login, password, tipusu, Calendar.getInstance().getTime(),null);
        usuarioDAO.crear(nuevo);
    }
    
    @Override
    public void eliminarUsuario(Usuario usuario) {
        // Crear el usuario 
        usuarioDAO.eliminar(usuario);
    }

    @Override
    public Usuario actualizarDatosCliente(Usuario datosUsuario) {
        return usuarioDAO.actualizar(datosUsuario);
    }

    @Override
    public Usuario actualizarPassword(long idUsuario, String password) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        usuario.setPassword(password);
        return usuarioDAO.actualizar(usuario);
    }

    @Override
    public Usuario actualizarUltimoAcceso(long idUsuario) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        usuario.setUltimoAcceso(Calendar.getInstance().getTime());  // Tiempo actual
        return usuarioDAO.actualizar(usuario);
    }

    @Override
    public boolean existeUsuario(String login) {
        return (usuarioDAO.buscarPorLogin(login) != null);
    }
    
    @Override
    public TipoUsuario tipoUsuario(String login){
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        return usuario.getTipo();
    
    }
}
