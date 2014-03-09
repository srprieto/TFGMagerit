/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.es.tfg.entidades.marco;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author srprieto
 */
@Entity
public class ItemValoracion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    BigInteger valor;
    String abreviatura;
    String nombre;

    @ManyToOne
    CriterioValoracion criterioValoracion;

    public ItemValoracion() {
    }

    public ItemValoracion(String abreviatura, String nombre, BigInteger valor) {
        this.valor = valor;
        this.abreviatura = abreviatura;
        this.nombre = nombre;
    }

    
    public ItemValoracion(String abreviatura, String nombre,BigInteger valor,  CriterioValoracion criterioValoracion) {
        this.valor = valor;
        this.abreviatura = abreviatura;
        this.nombre = nombre;
        this.criterioValoracion = criterioValoracion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
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

    public CriterioValoracion getCriterioValoracion() {
        return criterioValoracion;
    }

    public void setCriterioValoracion(CriterioValoracion criterioValoracion) {
        this.criterioValoracion = criterioValoracion;
    }

    
    
}
