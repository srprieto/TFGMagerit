package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.eclipse.persistence.annotations.CascadeOnDelete;


/**
 *
 * @author srprieto
 */
@Entity
public class TipoActivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String abreviatura;
    String nombre;
    @Column(length = 1000)
    String descripcion;

    @ManyToOne
    MarcoTrabajo marcoTrabajo;

    @ManyToOne(cascade = CascadeType.ALL)
    TipoActivo tipoActivoPadre;

    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "tipoActivoPadre")
    Set<TipoActivo> tiposActivoHijo;

    @ManyToMany
    @JoinTable(name = "TIPOACTIVO_TIPOAMENZA")
    @CascadeOnDelete
    Set<TipoAmenaza> tiposAmenaza;

    public TipoActivo() {
    }

    public TipoActivo(String abreviatura, String nombre, String descripcion, MarcoTrabajo marcoTrabajo) {
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

    public TipoActivo getTipoActivoPadre() {
        return tipoActivoPadre;
    }

    public void setTipoActivoPadre(TipoActivo tipoActivoPadre) {
        this.tipoActivoPadre = tipoActivoPadre;
    }

    public Set<TipoActivo> getTiposActivoHijo() {
        return tiposActivoHijo;
    }

    public void setTiposActivoHijo(Set<TipoActivo> tiposActivoHijo) {
        this.tiposActivoHijo = tiposActivoHijo;
    }

    public Set<TipoAmenaza> getTiposAmenaza() {
        return tiposAmenaza;
    }

    public void setTiposAmenaza(Set<TipoAmenaza> tiposAmenaza) {
        this.tiposAmenaza = tiposAmenaza;
    }

    public void anadirTipoActivoHijo(TipoActivo tipoActivo) {
        if (tiposActivoHijo == null) {
            tiposActivoHijo = new HashSet<TipoActivo>();
        }
        tipoActivo.setTipoActivoPadre(this);
        tiposActivoHijo.add(tipoActivo);
    }

    public void anadirTipoAmenaza(TipoAmenaza tipoAmenza) {
        if (tiposAmenaza == null) {
            tiposAmenaza = new HashSet<TipoAmenaza>();
        }
        tiposAmenaza.add(tipoAmenza);
    }

}
