/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.GestorActivoDAO;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "activoController")
@SessionScoped
public class ActivoController implements Serializable {

    private Activo activoEnEdicion;

    private List<TipoActivo> tiposActivos;
    private List<CriterioValoracion> valorBase;
    private List<GrupoActivos> grupoActivos;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String responsable;
    private String propietario;
    private String ubicacion;
    private Long cantidad;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    GestorActivoDAO gestorActivoDAO;

    @Inject
    ProyectoController proyecto;

    @Inject
    GrupoActivosDAO grupoActivoDAO;

    public ActivoController() {

    }

    @PostConstruct
    private void inicializar() {
        activoEnEdicion = new Activo();
        tiposActivos = tipoActivoDAO.buscarTodos();
        grupoActivos = grupoActivoDAO.buscarTodos();
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Activo getActivoEnEdicion() {
        return activoEnEdicion;
    }

    public void setActivoEnEdicion(Activo activoEnEdicion) {
        this.activoEnEdicion = activoEnEdicion;
    }

    public List<TipoActivo> getTiposActivos() {
        return tiposActivos;
    }

    public void setTiposActivos(List<TipoActivo> tiposActivos) {
        this.tiposActivos = tiposActivos;
    }

    public List<CriterioValoracion> getValorBase() {
        return valorBase;
    }

    public void setValorBase(List<CriterioValoracion> valorBase) {
        this.valorBase = valorBase;
    }

    public List<GrupoActivos> getGrupoActivos() {
        return grupoActivos;
    }

    public void setGrupoActivos(List<GrupoActivos> grupoActivos) {
        this.grupoActivos = grupoActivos;
    }

    public String doGuargar() {
        String destino;
        if (codigo.equals("")) {
            anadirMensajeError("No se ha indicado un codigo para el activo");
            destino = "crearactivo.xhtml";
        } else if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el activo");
            destino = "crearactivo.xhtml";
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción para el activo");
            destino = "crearactivo.xhtml";
        } else if (responsable.equals("")) {
            anadirMensajeError("No se ha indicado un responsable para el activo");
            destino = "crearactivo.xhtml";
        } else if (gestorActivoDAO.existeActivo(nombre) == true) {
            anadirMensajeError("El nombre introducido ya esta almacenado, no pueden existir dos activos con el mismo nombre");
            destino = "crearactivo.xhtml";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < codigo.length(); x++) {
                if (codigo.charAt(x) != ' ') {
                    sb.append(codigo.charAt(x));
                }else{
                    sb.append("_");
                }
            }
            setCodigo(sb.toString());
            destino = "confirmaractivo.xhtml";
        }
        return destino;
    }

    public void guardarActivo() throws IOException {
        gestorActivoDAO.crearNuevoActivo(codigo, nombre, descripcion, responsable, propietario, ubicacion, null, cantidad, proyecto.getProyectoActual(), activoEnEdicion.getTipoActivo(), activoEnEdicion.getGrupoActivos());
        anadirMensajeCorrecto("El Activo " + nombre + " ha sido guardado correctamente");
        nombre = "";
        descripcion = "";
        codigo = "";
        responsable = "";
        ubicacion ="";
        cantidad = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("proyectonuevo.xhtml");
    }

}
