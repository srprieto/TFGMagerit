/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.es.tfg.entidades.usuario;

import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;  

/**
 *
 * @author Saul
 */
@Entity
public class Usuario implements Serializable  {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToMany
    List<Proyecto> proyectos;
    
    private String login;
    private String password;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date   fechaAlta;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date   ultimoAcceso;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
   
    
    public Usuario() {
    }
    
    public Usuario(String login, String password, TipoUsuario tipo, Date fechaAlta, Date ultimoAcceso) {
        this.login = login;
        this.password = password;
        this.tipo = tipo;
        this.fechaAlta = fechaAlta;
        this.ultimoAcceso = ultimoAcceso;

    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
            return false;
        }
        if (this.tipo != other.tipo && (this.tipo == null || !this.tipo.equals(other.tipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", proyectos=" + proyectos + ", login=" + login + ", password=" + password + ", fechaAlta=" + fechaAlta + ", ultimoAcceso=" + ultimoAcceso + ", tipo=" + tipo + '}';
    }
    
    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
 
}
