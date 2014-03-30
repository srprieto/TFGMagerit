package es.uvigo.esei.tfg.logica.servicios;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jasypt.util.password.BasicPasswordEncryptor;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorUsuariosBean implements GestorUsuariosService {

    @Inject
    UsuarioDAO usuarioDAO;
    
    @Override
    public boolean autenticarUsuario(String login, String passwordPlano) {
        Usuario usuario;
        boolean resultado = false;

        usuario = usuarioDAO.buscarPorLogin(login);
        if (usuario != null) {
            if (passwordPlano.equals("") && !usuario.getPassword().equals("")) {
                resultado = false;
            } else {
                if (comprobarPassword(passwordPlano, usuario.getPassword()) == true) {
                    resultado = true;
                }
            }
        }
        return resultado;
    }
    
    @Override
    public boolean comprobarPassword(String passwordPlano, String passwordEncriptado) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return (passwordEncryptor.checkPassword(passwordPlano, passwordEncriptado));
    }
    
    @Override
    public Usuario actualizarPassword(long idUsuario, String passwordPlano) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String passwordEncriptado = passwordEncryptor.encryptPassword(passwordPlano);

        usuario.setPassword(passwordEncriptado);
        return usuarioDAO.actualizar(usuario);
    }

    @Override
    public Usuario recuperarDatosUsuario(String login) {
        return usuarioDAO.buscarPorLogin(login);
    }

    @Override
    public void crearNuevoUsuario(String login, String password, TipoUsuario tipusu) {
        // Crear el usuario 
        Usuario nuevo = new Usuario(login, password, tipusu, Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
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
    public TipoUsuario tipoUsuario(String login) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        return usuario.getTipo();

    }

    @Override
    public List<Usuario> usuario(String pass) {
        return usuarioDAO.usuario(pass);
    }

    @Override
    public Long existeId(String nombre) {
        Usuario principal = usuarioDAO.buscarPorLogin(nombre);
        return principal.getId();
    }

}
