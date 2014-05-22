package es.uvigo.es.tfg.entidades.proyecto;

import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
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
public class Amenaza implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String codigo;    
    String nombre;
    String descripcion;

    Double probabilidadOcurrencia;
    Double gradoDegradacionBase;
    
    @ManyToOne
    TipoAmenaza tipoAmenaza;
    
    @ManyToOne
    Proyecto proyecto;

    public Amenaza() {
    }

    public Amenaza(String codigo, String nombre, String descripcion, Double probabilidadOcurrencia, Double gradoDegradacionBase, TipoAmenaza tipoAmenaza, Proyecto proyecto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.probabilidadOcurrencia = probabilidadOcurrencia;
        this.gradoDegradacionBase = gradoDegradacionBase;
        this.tipoAmenaza = tipoAmenaza;
        this.proyecto = proyecto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getProbabilidadOcurrencia() {
        return probabilidadOcurrencia;
    }

    public void setProbabilidadOcurrencia(Double probabilidadOcurrencia) {
        this.probabilidadOcurrencia = probabilidadOcurrencia;
    }

    public Double getGradoDegradacionBase() {
        return gradoDegradacionBase;
    }

    public void setGradoDegradacionBase(Double gradoDegradacionBase) {
        this.gradoDegradacionBase = gradoDegradacionBase;
    }

    public TipoAmenaza getTipoAmenaza() {
        return tipoAmenaza;
    }

    public void setTipoAmenaza(TipoAmenaza tipoAmenaza) {
        this.tipoAmenaza = tipoAmenaza;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
    

}
