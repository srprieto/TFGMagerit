/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.analista;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "degradacionController")
@SessionScoped
public class DegradacionController implements Serializable{
    
    private Double grado;
    
    private List<Impacto> impacto;
    private List<Dimension> dimension;
    
    public void DegradacionController() {

    }

     /*Funciones GET y SET*/
    
    /************************************************************************************************/
    
    public Double getGrado() {
        return grado;
    }

    public void setGrado(Double grado) {
        this.grado = grado;
    }

    public List<Impacto> getImpacto() {
        return impacto;
    }

    public void setImpacto(List<Impacto> impacto) {
        this.impacto = impacto;
    }

    public List<Dimension> getDimension() {
        return dimension;
    }

    public void setDimension(List<Dimension> dimension) {
        this.dimension = dimension;
    }
    
    /************************************************************************************************/
}