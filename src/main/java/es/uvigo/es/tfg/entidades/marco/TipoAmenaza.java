package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TipoAmenaza implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String abreviatura;
    String nombre;
    @Column(length = 1000)
    String descripcion;

    String origen;

    @ManyToOne
    MarcoTrabajo marcoTrabajo;

    @ManyToOne
    TipoAmenaza tipoAmenazaPadre;

    @OneToMany(mappedBy = "tipoAmenazaPadre")
    Set<TipoAmenaza> tiposAmenazaHijo;

    @ManyToMany
    @JoinTable(name = "TIPOAMENZA_DIMENSION")
    Set<Dimension> dimensiones;

    @ManyToMany(mappedBy = "tiposAmenaza")
    Set<TipoActivo> tiposActivo;

    public TipoAmenaza() {
    }

    public TipoAmenaza(String abreviatura, String nombre, String descripcion, String origen, MarcoTrabajo marcoTrabajo) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.origen = origen;
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public MarcoTrabajo getMarcoTrabajo() {
        return marcoTrabajo;
    }

    public void setMarcoTrabajo(MarcoTrabajo marcoTrabajo) {
        this.marcoTrabajo = marcoTrabajo;
    }

    public TipoAmenaza getTipoAmenazaPadre() {
        return tipoAmenazaPadre;
    }

    public void setTipoAmenazaPadre(TipoAmenaza tipoAmenazaPadre) {
        this.tipoAmenazaPadre = tipoAmenazaPadre;
    }

    public Set<TipoAmenaza> getTiposAmenazaHijo() {
        return tiposAmenazaHijo;
    }

    public void setTiposAmenazaHijo(Set<TipoAmenaza> tiposAmenazaHijo) {
        this.tiposAmenazaHijo = tiposAmenazaHijo;
    }

    public Set<Dimension> getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(Set<Dimension> dimensiones) {
        this.dimensiones = dimensiones;
    }

    public Set<TipoActivo> getTiposActivo() {
        return tiposActivo;
    }

    public void setTiposActivo(Set<TipoActivo> tiposActivo) {
        this.tiposActivo = tiposActivo;
    }

    public void anadirTipoAmenazaHijo(TipoAmenaza tipoAmenza) {
        if (tiposAmenazaHijo == null) {
            tiposAmenazaHijo = new HashSet<TipoAmenaza>();
        }
        tipoAmenza.setTipoAmenazaPadre(this);
        tiposAmenazaHijo.add(tipoAmenza);
    }

    public void anadirTipoActivo(TipoActivo tipoActivo) {
        if (tiposActivo == null) {
            tiposActivo = new HashSet<TipoActivo>();
        }
        tiposActivo.add(tipoActivo);
        // No hacerlo en el otro sentido aqui
    }

    public void anadirDimension(Dimension dimension) {
        if (dimensiones == null) {
            dimensiones = new HashSet<Dimension>();
        }
        dimensiones.add(dimension);
    }

    public void eliminarDimension(Dimension dimension) {
        if (dimension != null) {
            dimensiones.remove(dimension);
        }
    }

}
