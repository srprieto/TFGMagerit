package es.uvigo.es.tfg.entidades.proyecto;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
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
public class Activo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String codigo;
    String nombre;
    String descripcion;
    String responsable;
    String propietario;  
    String ubicacion;
    Double valorBase;
    Long cantidad;

    @ManyToOne
    Proyecto proyecto;

    @ManyToOne
    TipoActivo tipoActivo;

    @ManyToOne
    GrupoActivos grupoActivos;
   
    @OneToMany(mappedBy = "activoPrincipal")
    Set<Dependencia> dependencias;

    @OneToMany(mappedBy = "activo")
    Set<Valoracion> valoraciones;

    public Activo() {
    }

    public Activo(String codigo, String nombre, String descripcion, String responsable, String propietario, String ubicacion, Double valorBase, Long cantidad, Proyecto proyecto,TipoActivo tipo, GrupoActivos grupo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.propietario = propietario;
        this.ubicacion = ubicacion;
        this.valorBase = valorBase;
        this.cantidad = cantidad;
        this.proyecto = proyecto;
        this.tipoActivo = tipo;
        this.grupoActivos = grupo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public TipoActivo getTipoActivo() {
        return tipoActivo;
    }

    public void setTipoActivo(TipoActivo tipoActivo) {
        this.tipoActivo = tipoActivo;
    }

 
    public Set<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(Set<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }

    public Set<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(Set<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public GrupoActivos getGrupoActivos() {
        return grupoActivos;
    }

    public void setGrupoActivos(GrupoActivos grupoActivos) {
        this.grupoActivos = grupoActivos;
    }


    public void anadirDependencia(Dependencia dependencia) {
        if (dependencias == null) {
            dependencias = new HashSet<Dependencia>();
        }
        dependencia.setActivoPrincipal(this);
        dependencias.add(dependencia);
    }

    public void anadirValoracion(Valoracion valoracion) {
        if (valoraciones == null) {
            valoraciones = new HashSet<Valoracion>();
        }
        valoracion.setActivo(this);
        valoraciones.add(valoracion);
    }

}
