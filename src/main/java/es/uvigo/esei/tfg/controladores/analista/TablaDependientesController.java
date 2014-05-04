/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.analista;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController.LoggedIn;
import es.uvigo.esei.tfg.controladores.modelos.DependientesModel;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaDependientesController")
@SessionScoped
public class TablaDependientesController implements Serializable {
    
    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
    private Dependencia dependiente;
    private List<Dependencia> dependientes;
    private Dependencia selectedDependiente;
    private Dependencia[] selectedDependientes;
    private DependientesModel dependientesModel;
    private List<Dependencia> filteredDependientes;

    @Inject
    @LoggedIn
    Usuario usuarioActual;

    @Inject
    DependenciaDAO dependenciaDAO;

    @Inject
    ArbolActivosController arbolActivosController;

    public TablaDependientesController() {

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

    public Dependencia[] getSelectedDependientes() {
        return selectedDependientes;
    }

    public void setSelectedDependientes(Dependencia[] selectedDependencias) {
        this.selectedDependientes = selectedDependencias;
    }

    public List<Dependencia> getFilteredDependencias() {
        return filteredDependientes;
    }

    public void setFilteredDependencias(List<Dependencia> filteredDependientes) {
        this.filteredDependientes = filteredDependientes;
    }

    public Dependencia getSelectedDependencia() {
        return selectedDependiente;
    }

    public void setSelectedDependencia(Dependencia selectedDependencia) {
        this.selectedDependiente = selectedDependencia;
    }

    public Dependencia getDependencia() {
        return dependiente;
    }

    public void setDependencia(Dependencia dependiente) {
        this.dependiente = dependiente;
    }

    public List<Dependencia> getDependientes() {
        dependientes = dependenciaDAO.buscarTodos();
        return dependientes;
    }

    public void setDependientes(List<Dependencia> dependientes) {
        this.dependientes = dependientes;
    }

    public DependientesModel getDependienteModel() {
        Activo actual = arbolActivosController.getActivoActual();
        dependientes = dependenciaDAO.buscarPorPrincipal(actual);
        dependientesModel = new DependientesModel(dependientes);
        return dependientesModel;
    }
    
    /************************************************************************************************/

    public void update() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Dependencia[] seleccionados = this.getSelectedDependientes();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            this.setSelectedDependientes(null);
            anadirMensajeError(messages.getString("ERRTABDEP"));
        } else if (tamano != 1) {
            this.setSelectedDependientes(null);
            anadirMensajeError(messages.getString("ERRTABDEP1"));
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateDependencia() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Dependencia[] seleccionados = this.getSelectedDependientes();
        int tamano = seleccionados.length;
        Dependencia seleccionado = seleccionados[0];
        this.setSelectedDependientes(null);

        if (seleccionado.getGrado() == null) {
            anadirMensajeError(messages.getString("ERRTABDEP2"));
        } else if (seleccionado.getJustificacion().equals("")) {
            anadirMensajeError(messages.getString("ERRTABDEP3"));
        } else {
            dependenciaDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABDEP"));
            RequestContext.getCurrentInstance().update("form");
        }

    }

    public void eliminar() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Dependencia[] seleccionados = this.getSelectedDependientes();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRTABDEP"));
        } else {
            RequestContext.getCurrentInstance().execute("multiDialog.show();");
        }
    }

    public void eliminarDependencias() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        Dependencia[] lista = this.getSelectedDependientes();
        int tamano = lista.length;
        
        for (int i = 0; i < tamano; i++) {
            Dependencia dep = lista[i];
            dependenciaDAO.eliminar(dep);
        }
        if (tamano == 1) {
            anadirMensajeCorrecto(messages.getString("ERRTABDEP4"));
        } else {
            anadirMensajeCorrecto(messages.getString("ERRTABDEP5"));
        }
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedDependientes(null);
        arbolActivosController.setSelectedNodes(null);
        context.redirect("activos.xhtml");
    }
}
