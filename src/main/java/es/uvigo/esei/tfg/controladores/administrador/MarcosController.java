/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.esei.tfg.logica.daos.GestorMarcosDAO;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */

@Named(value = "marcosController")
@SessionScoped
public class MarcosController implements Serializable{
    
    private String nombre = "";
    private String descripcion = "";

    
    @Inject
    GestorMarcosDAO gestorMarcoDAO;
    
    public MarcosController(){
        
    }
    
    /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    protected void anadirMensajeCorrecto(String mensaje){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
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
    
    public String doMarco()
    {
        String destino;
        if(nombre.equals("")){
            anadirMensajeError("No se ha indicado un nombre para el marco de trabajo");
            destino = "crearmarco.xhtml";
             return destino;
        }else if(descripcion.equals("")){
            anadirMensajeError("No se ha indicado una descripción");
            destino = "crearmarco.xhtml";
             return destino;
        }else{
            destino = "nuevomarco.xhtml";
            return destino;
        }  
    }
    
    public void doCrear(){
        gestorMarcoDAO.crearNuevoMarco(nombre, descripcion);
        nombre="";
        descripcion="";
        anadirMensajeCorrecto("El marco de trabajo ha sido creado");
    }
}