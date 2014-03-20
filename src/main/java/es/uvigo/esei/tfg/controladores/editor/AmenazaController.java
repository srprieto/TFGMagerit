/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoAmenazaDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorActivoBean;
import es.uvigo.esei.tfg.logica.servicios.GestorAmenazasBean;
import es.uvigo.esei.tfg.logica.servicios.GestorAmenazasService;
import es.uvigo.esei.tfg.logica.servicios.GestorImpactoBean;
import es.uvigo.esei.tfg.logica.servicios.GestorImpactoService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "amenazaController")
@SessionScoped
public class AmenazaController implements Serializable {

    private List<TipoAmenaza> tiposAmenazas;

    private String codigo;
    private String nomTipo;
    private String nombre;
    private String descripcion;
    private Double probabilidadOcurrencia;
    private Double gradoDegradacionBase;

    @Inject
    TipoAmenazaDAO tipoAmenazaDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    ArbolActivosController arbolActivosController;

    @Inject
    ImpactoDAO impactoDAO;
    
    @Inject
    GestorAmenazasService gestorAmenazasService;
    
    @Inject
    ProyectoController proyectoController;
    
    @Inject
    GestorImpactoService gestorImpactoService;

    public void AmenazaController() {

    }
    /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     *
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    protected void anadirMensajeCorrecto(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }

    public String getNomTipo() {
        return nomTipo;
    }

    public void setNomTipo(String nomTipo) {
        this.nomTipo = nomTipo;
    }
    
    public List<TipoAmenaza> getTiposAmenazas() {
        return tiposAmenazas;
    }
    
    public void setTiposAmenazas(List<TipoAmenaza> tiposAmenazas) {
        this.tiposAmenazas = tiposAmenazas;
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

    public Double getProbabilidadOcurrencia() {
        return probabilidadOcurrencia;
    }

    public void setProbabilidadOcurrencia(Double probabilidadOcurrencia) {
        this.probabilidadOcurrencia = probabilidadOcurrencia;
    }

    public Double getGradoDegradacionBase() {
        return gradoDegradacionBase;
    }

    public void setGradoDegradacionBase(Double gradoDegradacionBase) {
        this.gradoDegradacionBase = gradoDegradacionBase;
    }

    public String doGuargar() {
        String destino;
        if (codigo.equals("")) {
            anadirMensajeError("No se ha indicado un codigo para la amenaza");
            destino = "crearamenaza.xhtml";
        } else if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para la amenaza");
            destino = "crearamenaza.xhtml";
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción para la amenaza");
            destino = "crearamenaza.xhtml";
        } else {

            int valor = 1;
            List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
            for (int j = 0; j < impactos.size(); j++) {
                if (impactos.get(j).getAmenaza().getNombre().equals(nombre)) {
                    valor = 0;
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < codigo.length(); x++) {
                if (codigo.charAt(x) != ' ') {
                    sb.append(codigo.charAt(x));
                } else {
                    sb.append("_");
                }
            }
            setCodigo(sb.toString());
            if (valor == 1) {
                destino = "confirmaramenaza.xhtml";
            } else {
                anadirMensajeError("Ya existe una Amenaza con ese nombre");
                destino = "crearamenaza.xhtml";
            }
        }
        return destino;
    }

   public List<String> getTipos(){
       List<TipoAmenaza> posibles = tipoAmenazaDAO.buscarTipoActivo(arbolActivosController.getActivoActual().getTipoActivo());
       List<String> nombres = new ArrayList<>();
       for(int i=0; i<posibles.size();i++){
           nombres.add(posibles.get(i).getNombre());
       }
       return nombres;
   }
   
    public void guardarAmenaza() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorAmenazasService.crearNuevaAmenaza(codigo, nombre, descripcion, probabilidadOcurrencia, gradoDegradacionBase, tipoAmenazaDAO.buscarPorNombre(nomTipo),proyectoController.getProyectoActual());
        gestorImpactoService.crearNuevoImpacto(Calendar.getInstance().getTime(), arbolActivosController.getActivoActual(), amenazaDAO.buscarPorNombre(nombre));
        anadirMensajeCorrecto("El Activo " + nombre + " ha sido guardado correctamente");
        nombre = "";
        descripcion = "";
        codigo = "";
        nomTipo = "";
        probabilidadOcurrencia = null;
        gradoDegradacionBase= null;
        context.redirect("amenazas.xhtml");
    }


}