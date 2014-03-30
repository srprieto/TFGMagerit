/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.Credenciales;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;


/**
 *
 * @author Saul
 */

@Stateless
public class UsuarioJPA extends GenericoJPA<Usuario> implements UsuarioDAO {
        
     @Inject 
     Credenciales credenciales;
    
     @Override
     public Usuario buscarPorLogin(String login) {
        Query q = em.createQuery("SELECT object(u) FROM Usuario AS u " +
                                 "  WHERE u.login = :login");
        q.setParameter("login", login);

        List<Usuario> resultados = q.getResultList();

        if (resultados ==null) {
            return null;  // No encontrado
        }
        else if (resultados.size() != 1){
            return null; // No encontrado
        }
        else {
            return resultados.get(0);  // Devuelve el encontrado
        }
    }

    @Override
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        Query q = em.createQuery("SELECT object(u) FROM Usuario as u "+
                                 "  WHERE u.tipo = :tipo");
        q.setParameter("tipo", tipo);
        return q.getResultList();      
    }

    @Override
    public List<Usuario> buscarTodos() {
        Query q = em.createQuery("SELECT object(u) FROM Usuario as u");
        return q.getResultList();
    }

    /**
     *
     * @return
     */
    @Override
    public int contador() {
        Query q = em.createQuery("SELECT count(u) FROM Usuario as u");
        return q.getFirstResult();
    }
    
    @Override
    public List<Usuario> usuario(String pass){
        Query q =em.createQuery("SELECT object(u) FROM Usuario as u "+
                                " WHERE u.login=:login AND u.password=:password");
           q.setParameter("login", credenciales.getLogin());
           q.setParameter("password", pass);
           return q.getResultList();
    }
    
    
}
