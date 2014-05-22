/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.controladores.analista.ProyectoController;
import es.uvigo.esei.tfg.controladores.analista.TablaProyectosController;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorMarcosService;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Saul
 */
@Named(value = "marcosController")
@SessionScoped
public class MarcosController implements Serializable {
    
    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    //Atributos
    private String nombre = "";
    private String descripcion = "";
    private Date fechaCreacion= null;
    private int volver=0;
    
    //"Acceso" a metodos de otras clases
    @Inject
    GestorMarcosService gestorMarcoService;
    
    @Inject
    ProyectoController proyectoController;
    
    @Inject
    MarcoTrabajoDAO marcoTrabajoDAO;
    
    @Inject
    TablaProyectosController tablaProyectosController;
    
    public MarcosController() {

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
    
    /*Funciones GET y SET*/
    
    /************************************************************************************************/

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVolver() {
        return volver;
    }

    public void setVolver(int volver) {
        this.volver = volver;
    }
    
    
    /************************************************************************************************/
    
    public void atras1() throws IOException{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (volver==0){
            context.redirect("crearproyecto.xhtml");
        }else{
            context.redirect("misproyectos.xhtml");
        }
    }
    
    public void detalles() throws IOException{
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        MarcoTrabajo actual = proyectoController.getProyectoEnEdicion().getMarcoTrabajo();
        nombre = actual.getNombre();
        descripcion= actual.getDescripcion();
        fechaCreacion = actual.getFechaCreacion();
        volver=0;
        context.redirect("detallesmarco.xhtml");
        
    }
    
     public void detallesModificar() throws IOException{
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        Proyecto[] seleccionados = tablaProyectosController.getSelectedProyectos();
        Proyecto seleccionado = seleccionados[0];
        
        MarcoTrabajo actual = marcoTrabajoDAO.buscarPorNombre(seleccionado.getMarcoTrabajo().getNombre());
        nombre = actual.getNombre();
        descripcion= actual.getDescripcion();
        fechaCreacion = actual.getFechaCreacion();
        volver=1;
        context.redirect("detallesmarco.xhtml");
        
    }
    
    /*Funcion que valida los datos del formulario de creación de un nuevo marco*/
    public void doMarco() throws IOException {
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Atributos
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes con el redireccionamiento.
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();//Necesario para redireccionar al usuario a otra vista
        
        /*Comprobamos que el campo nombre y descripción no esten vacios, y además, comprobamos que no 
        existe otro marco con el mismo nombre en la BD. Si todo es correcto, redireccionamos a la vista
        de confirmación de los datos*/
        if (nombre.equals("")) {
            anadirMensajeError(messages.getString("ERRMAR"));
        } else if (descripcion.equals("")) {
            anadirMensajeError(messages.getString("ERRMAR1"));
        } else if (gestorMarcoService.existeMarco(nombre) == true) {
            anadirMensajeError(messages.getString("ERRMAR2"));
        } else {
            context.redirect("nuevomarco.xhtml");
        }
    }
    
    /*Función que almacena en la base de datos el marco creado por el administrador*/
    public void doCrear() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        //Atributos
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes con el redireccionamiento.
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();//Necesario para redireccionar al usuario a otra vista
        
        gestorMarcoService.crearNuevoMarco(nombre, descripcion);
        anadirMensajeCorrecto(messages.getString("CORRMAR")+ " "+ nombre+" " + messages.getString("CORRMAR1"));
        nombre = "";
        descripcion = "";
        context.redirect("marcos.xhtml");
    }
    
    /*Funcion que se corresponde con el boton atras de la vista de confirmación de los datos de un marco,
    es necesario ya que, si se cancela la confirmacino debemos borrar los datos almacenados en el 
    controlador*/
    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        nombre = "";
        descripcion = "";
        context.redirect("crearmarco.xhtml");
    }
}