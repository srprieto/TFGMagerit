package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TipoSalvaguarda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String abreviatura;
    String nombre;

    @ManyToOne
    MarcoTrabajo marcoTrabajo;

    @ManyToOne
    TipoSalvaguarda tipoSalvaguardaPadre;

    @OneToMany(mappedBy = "tipoSalvaguardaPadre")
    Set<TipoSalvaguarda> tiposSalvaguardaHijo;

    public TipoSalvaguarda() {
    }

    public TipoSalvaguarda(String abreviatura, String nombre, MarcoTrabajo marcoTrabajo) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
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

    public MarcoTrabajo getMarcoTrabajo() {
        return marcoTrabajo;
    }

    public void setMarcoTrabajo(MarcoTrabajo marcoTrabajo) {
        this.marcoTrabajo = marcoTrabajo;
    }

    public TipoSalvaguarda getTipoSalvaguardaPadre() {
        return tipoSalvaguardaPadre;
    }

    public void setTipoSalvaguardaPadre(TipoSalvaguarda tipoSalvaguardaPadre) {
        this.tipoSalvaguardaPadre = tipoSalvaguardaPadre;
    }

    public Set<TipoSalvaguarda> getTiposSalvaguardaHijo() {
        return tiposSalvaguardaHijo;
    }

    public void setTiposSalbaguardaHijo(Set<TipoSalvaguarda> tiposSalvaguardaHijo) {
        this.tiposSalvaguardaHijo = tiposSalvaguardaHijo;
    }

    public void anadirTipoSalvaguardaHijo(TipoSalvaguarda tipoSalvaguarda) {
        if (tiposSalvaguardaHijo == null) {
            tiposSalvaguardaHijo = new HashSet<TipoSalvaguarda>();
        }
        tipoSalvaguarda.setTipoSalvaguardaPadre(this);
        tiposSalvaguardaHijo.add(tipoSalvaguarda);
    }

}
