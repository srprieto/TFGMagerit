package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
public class CriterioValoracion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;    
    
    @ManyToOne
    MarcoTrabajo marcoTrabajo;
    
    // PENDIENTE
}
