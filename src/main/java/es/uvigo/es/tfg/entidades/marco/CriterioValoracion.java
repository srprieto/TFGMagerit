package es.uvigo.es.tfg.entidades.marco;

import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



@Entity
public class CriterioValoracion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;    
    
    String abreviatura;
    
    String nombre;
    
    @ManyToOne
    MarcoTrabajo marcoTrabajo;
    
    
    @OneToMany(mappedBy = "criterioValoracion", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    List<ItemValoracion> itemsValoracion;

    public CriterioValoracion() {
    }

    public CriterioValoracion(String abreviatura, String nombre, MarcoTrabajo marcoTrabajo) {
        this(abreviatura, nombre, marcoTrabajo, new ArrayList<ItemValoracion>());
    }

    
    public CriterioValoracion(String abreviatura, String nombre, MarcoTrabajo marcoTrabajo, List<ItemValoracion> itemsValoracion) {
        this.abreviatura = abreviatura;
        this.nombre = nombre;
        this.marcoTrabajo = marcoTrabajo;
        this.itemsValoracion = itemsValoracion;
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

    public List<ItemValoracion> getItemsValoracion() {
        return itemsValoracion;
    }

    public void setItemsValoracion(List<ItemValoracion> itemsValoracion) {
        this.itemsValoracion = itemsValoracion;
    }
    
    public void anadirItemValoracion(ItemValoracion item) {
        if (this.itemsValoracion != null) {
            this.itemsValoracion = new ArrayList<ItemValoracion>();
        }
        item.setCriterioValoracion(this);
        this.itemsValoracion.add(item);
    }
    
}
