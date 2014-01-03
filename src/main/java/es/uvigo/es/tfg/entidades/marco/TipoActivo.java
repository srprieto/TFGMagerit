package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TipoActivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String abreviatura;
    String nombre;
    String descripción;
    
    @ManyToOne
    MarcoTrabajo marcoTrabajo;

    @ManyToOne
    TipoActivo tipoActivoPadre;

    @OneToMany(mappedBy = "tipoActivoPadre")
    Set<TipoActivo> tiposActivoHijo;

    @ManyToMany
    @JoinTable(name = "TIPOACTIVO_TIPOAMENZA")  // Es necesario
    Set<TipoAmenaza> tiposAmenaza;



    public TipoActivo() {
    }

    public TipoActivo(String abreviatura, String nombre, String descripción, MarcoTrabajo marcoTrabajo) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
        this.descripción = descripción;
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

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
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
            tiposActivoHijo= new HashSet<TipoActivo>();
        }
        tipoActivo.setTipoActivoPadre(this);
        tiposActivoHijo.add(tipoActivo);
    }
    
    
    public void anadirTipoAmenaza(TipoAmenaza tipoAmenza) {
        if (tiposAmenaza == null) {
            tiposAmenaza = new HashSet<TipoAmenaza>();
        }
        // OMITIRLO: daria un bucle infinito -> tipoAmenza.anadirTipoActivo(this); // TODO: es necesario esto ¿?
        tiposAmenaza.add(tipoAmenza);
    }
    
    
    
}
