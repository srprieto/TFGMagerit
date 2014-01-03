package es.uvigo.es.tfg.entidades.proyecto;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Valoracion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double valor;

    @ManyToOne
    Activo activo;

    @ManyToOne
    Dimension dimension;

    public Valoracion() {
    }

    public Valoracion(Double valor, Activo activo, Dimension dimension) {
        this.valor = valor;
        this.activo = activo;
        this.dimension = dimension;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Activo getActivo() {
        return activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    
    
}
