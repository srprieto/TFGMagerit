package es.uvigo.es.tfg.entidades.proyecto;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Proyecto implements Serializable { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nombre;
    String descripcion;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date fechaCreacion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date fechaModificacion;

    @ManyToOne
    MarcoTrabajo marcoTrabajo;
    
    @ManyToOne
    Usuario creador;
    
    @ManyToOne
    Proyecto proyectoBase;

    public Proyecto() {
        this.fechaCreacion = Calendar.getInstance().getTime();
        this.fechaModificacion = this.fechaCreacion;
    }

    public Proyecto(String nombre, String descripcion, MarcoTrabajo marcoTrabajo, Usuario creador) {
        this(nombre, descripcion, marcoTrabajo, creador, null);
    }

    public Proyecto(String nombre, String descripcion,  MarcoTrabajo marcoTrabajo, Usuario creador, Proyecto proyectoBase) {
        this(); // Fija fechas
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marcoTrabajo = marcoTrabajo;
        this.creador = creador;
        this.proyectoBase = proyectoBase;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public MarcoTrabajo getMarcoTrabajo() {
        return marcoTrabajo;
    }

    public void setMarcoTrabajo(MarcoTrabajo marcoTrabajo) {
        this.marcoTrabajo = marcoTrabajo;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public Proyecto getProyectoBase() {
        return proyectoBase;
    }

    public void setProyectoBase(Proyecto proyectoBase) {
        this.proyectoBase = proyectoBase;
    }

    
    
    
}