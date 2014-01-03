package es.uvigo.es.tfg.entidades.proyecto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class Impacto implements Serializable { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    Long id;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date fecha;

    @ManyToOne
    Activo activo;
    
    @ManyToOne
    Amenaza amenaza;
    
    @OneToMany(mappedBy = "impacto")
    Set<Degradacion> degradaciones;

    public Impacto() {
    }

    public Impacto(Date fecha, Activo activo, Amenaza amenaza) {
        this.fecha = fecha;
        this.activo = activo;
        this.amenaza = amenaza;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Activo getActivo() {
        return activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Amenaza getAmenaza() {
        return amenaza;
    }

    public void setAmenaza(Amenaza amenaza) {
        this.amenaza = amenaza;
    }

    public Set<Degradacion> getDegradaciones() {
        return degradaciones;
    }

    public void setDegradaciones(Set<Degradacion> degradaciones) {
        this.degradaciones = degradaciones;
    }

    public void anadirDegradacion(Degradacion degradacion) {
        if (degradaciones == null) {
            degradaciones = new HashSet<Degradacion>();
        }
        degradacion.setImpacto(this);
        degradaciones.add(degradacion);
    }
    
}