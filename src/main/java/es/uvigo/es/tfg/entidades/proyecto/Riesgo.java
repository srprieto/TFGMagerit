/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.es.tfg.entidades.proyecto;

import es.uvigo.es.tfg.entidades.marco.Dimension;

/**
 *
 * @author Saul
 */
public class Riesgo {
    
    private int valor;
    private String gravedad;
    
    private Dimension dimension;
    
    private Impacto impacto;
    
   
    public Riesgo() {
        
    }
    
    public Riesgo(int valor, String gravedad,Dimension dimension,Impacto impacto) {
        this.valor = valor;
        this.gravedad = gravedad;
        this.dimension=dimension;
        this.impacto=impacto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public void setImpacto(Impacto impacto) {
        this.impacto = impacto;
    }
}