package es.uvigo.es.tfg.entidades.proyecto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Degradacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double grado;
    Double probabilidad;
    
    @ManyToOne
    Impacto impacto;  // TODO: Es necesaria ¿?

    public Degradacion() {
    }

    public Degradacion(Double grado, Double probabilidad, Impacto impacto) {
        this.grado = grado;
        this.probabilidad = probabilidad;
        this.impacto = impacto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrado() {
        return grado;
    }

    public void setGrado(Double grado) {
        this.grado = grado;
    }

    public Double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public void setImpacto(Impacto impacto) {
        this.impacto = impacto;
    }

    
}
