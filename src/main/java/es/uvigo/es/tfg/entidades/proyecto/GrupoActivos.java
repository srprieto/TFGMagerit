package es.uvigo.es.tfg.entidades.proyecto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class GrupoActivos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String abreviatura;
    String nombre;

    @OneToMany(mappedBy = "grupoActivos")
    Set<Activo> activos;

    public GrupoActivos() {
    }

    public GrupoActivos(String abreviatura, String nombre) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
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

    public Set<Activo> getActivos() {
        return activos;
    }

    public void setActivos(Set<Activo> activos) {
        this.activos = activos;
    }

    public void anadirActivo(Activo activo) {
        if (activos == null) {
            activos = new HashSet<Activo>();
        }
        activo.setGrupoActivos(this);
        activos.add(activo);
    }
}
