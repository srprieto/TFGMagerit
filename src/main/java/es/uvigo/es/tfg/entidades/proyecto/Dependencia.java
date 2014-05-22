package es.uvigo.es.tfg.entidades.proyecto;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 *
 * @author srprieto
 */
@Entity
public class Dependencia implements Serializable { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String justificacion;
    Double grado;

    @ManyToOne
    Activo activoPrincipal;    
    @ManyToOne
    Activo activoDependiente;

    public Dependencia() {
    }

    public Dependencia(String justificacion, Double grado, Activo activoPrincipal, Activo activoDependiente) {
        this.justificacion = justificacion;
        this.grado = grado;
        this.activoPrincipal = activoPrincipal;
        this.activoDependiente = activoDependiente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Double getGrado() {
        return grado;
    }

    public void setGrado(Double grado) {
        this.grado = grado;
    }

    public Activo getActivoPrincipal() {
        return activoPrincipal;
    }

    public void setActivoPrincipal(Activo activoPrincipal) {
        this.activoPrincipal = activoPrincipal;
    }

    public Activo getActivoDependiente() {
        return activoDependiente;
    }

    public void setActivoDependiente(Activo activoDependiente) {
        this.activoDependiente = activoDependiente;
    }

    
}
