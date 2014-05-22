package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import javax.persistence.Column;
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
public class Dimension implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String abreviatura;
    String nombre;
    @Column(length = 1000)
    String descripcion;

    @ManyToOne
    MarcoTrabajo marcoTrabajo;
    
    

    public Dimension() {
    }

    public Dimension(String abreviatura, String nombre, String descripcion, MarcoTrabajo marcoTrabajo) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marcoTrabajo = marcoTrabajo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MarcoTrabajo getMarcoTrabajo() {
        return marcoTrabajo;
    }

    public void setMarcoTrabajo(MarcoTrabajo marcoTrabajo) {
        this.marcoTrabajo = marcoTrabajo;
    }

}
