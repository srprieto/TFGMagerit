package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.modelos.MarcoModel;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.CriterioValoracionDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.daos.DimensionDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorMarcosService;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoAmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

@Named(value = "tablaMarcosController")
@SessionScoped
public class TablaMarcosController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private ResourceBundle messages;

    //Atributos
    private List<MarcoTrabajo> marcos;
    private MarcoTrabajo[] selectedMarcos;//Devuelve los marcos seleccionados, implementada por Primefaces
    private MarcoModel marcoModel;
    private boolean disponible = false;//variable que no nos deja modificar datos si otro usuario esta realizando cambios

    @Inject
    MarcoTrabajoDAO marcoDAO;

    @Inject
    TipoAmenazaDAO tipoAmenazaDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    DimensionDAO dimensionDAO;

    @Inject
    ProyectoDAO proyectoDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    DependenciaDAO dependenciaDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    CriterioValoracionDAO criterioValoracionDAO;

    @Inject
    GestorMarcosService gestorMarcoService;

    @Inject
    FicherosController ficherosController;

    public TablaMarcosController() {

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
    
    /***********************************************************************************************/

    public MarcoTrabajo[] getSelectedMarcos() {
        return selectedMarcos;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public List<MarcoTrabajo> getMarcos() {
        marcos = marcoDAO.buscarTodos();
        return marcos;
    }

    public void setMarcos(List<MarcoTrabajo> marcos) {
        this.marcos = marcos;
    }

    public void setSelectedMarcos(MarcoTrabajo[] selectedMarcos) {
        this.selectedMarcos = selectedMarcos;
    }    
    
    //Función que usamos para cargar la tabla marcos
    public MarcoModel getMarcoModel() {
        marcos = marcoDAO.buscarTodos();
        marcoModel = new MarcoModel(marcos);
        return marcoModel;
    }
    
    /***********************************************************************************************/
    
    /*Función que comprueba que hemos seleccionado algun marco para borrarlo*/
    public void eliminar() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        
        //Atributos
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRTABMAR"));
        } else {
            RequestContext.getCurrentInstance().execute("multiMarcoDialog.show();");
        }
    }
    
    /*Funcion que elimina los Marcos seleccionados, así como todos sus elementos asociados
    en la base de datos*/
    public void eliminarMarcos() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        
        //Atributos: Listas donde se almacenan todos los elementos relacionados con el marco
        //para su posterior borrado
        List<TipoAmenaza> tipoAmenazaEliminar = new ArrayList<>();
        List<TipoActivo> tipoActivoEliminar = new ArrayList<>();
        List<Dimension> dimensionesEliminar = new ArrayList<>();
        List<CriterioValoracion> criteriosEliminar = new ArrayList<>();
        List<Proyecto> proyectosEliminar = new ArrayList<>();
        List<Activo> activoEliminar = new ArrayList<>();
        List<Impacto> impactoEliminar = new ArrayList<>();
        List<Degradacion> degradacionEliminar = new ArrayList<>();
        List<Dependencia> dependenciasEliminar = new ArrayList<>();
        List<Valoracion> valoracionesEliminar = new ArrayList<>();
        List<Usuario> editores = new ArrayList<>();
        
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();

        Amenaza amenazaEliminar;
        int tamano = seleccionados.length;
        
        //Bucles para eliminar todos los datos asociados al marco en la base de datos para evitar la excepción
        for (int i = 0; i < tamano; i++) {
            MarcoTrabajo seleccionado = seleccionados[i];
            proyectosEliminar = proyectoDAO.buscarMarco(seleccionado);
            for (int r = 0; r < proyectosEliminar.size(); r++) {
                activoEliminar = activoDAO.buscarActivosProyecto(proyectosEliminar.get(r));
                for (int j = 0; j < activoEliminar.size(); j++) {
                    impactoEliminar = impactoDAO.buscarAmenazasActivo(activoEliminar.get(j));
                    for (int s = 0; s < impactoEliminar.size(); s++) {
                        degradacionEliminar = degradacionDAO.buscarPorImpacto(impactoEliminar.get(s));
                        for (int z = 0; z < degradacionEliminar.size(); z++) {
                            degradacionDAO.eliminar(degradacionEliminar.get(z));
                        }
                        degradacionEliminar.clear();
                        amenazaEliminar = impactoEliminar.get(s).getAmenaza();
                        impactoDAO.eliminar(impactoEliminar.get(s));
                        amenazaDAO.eliminar(amenazaEliminar);
                    }
                    impactoEliminar.clear();
                    dependenciasEliminar = dependenciaDAO.buscarPorPrincipal(activoEliminar.get(j));
                    for (int s = 0; s < dependenciasEliminar.size(); s++) {
                        dependenciaDAO.eliminar(dependenciasEliminar.get(s));
                    }
                    dependenciasEliminar.clear();
                    valoracionesEliminar = valoracionDAO.buscarPorActivo(activoEliminar.get(j));
                    for (int s = 0; s < valoracionesEliminar.size(); s++) {
                        valoracionDAO.eliminar(valoracionesEliminar.get(s));
                    }
                    valoracionesEliminar.clear();
                    activoDAO.eliminar(activoEliminar.get(j));
                }
                activoEliminar.clear();
                proyectosEliminar.get(r).setEditores(editores);
                proyectoDAO.actualizar(proyectosEliminar.get(r));
                proyectoDAO.eliminar(proyectosEliminar.get(r));
            }
            proyectosEliminar.clear();
            tipoActivoEliminar = tipoActivoDAO.buscarMarco(seleccionado);
            for (int j = 0; j < tipoActivoEliminar.size(); j++) {
                tipoActivoDAO.eliminar(tipoActivoEliminar.get(j));
            }
            tipoActivoEliminar.clear();
            tipoAmenazaEliminar = tipoAmenazaDAO.buscarMarco(seleccionado);
            for (int j = 0; j < tipoAmenazaEliminar.size(); j++) {
                tipoAmenazaDAO.eliminar(tipoAmenazaEliminar.get(j));
            }
            tipoAmenazaEliminar.clear();
            dimensionesEliminar = dimensionDAO.buscarMarco(seleccionado);
            for (int j = 0; j < dimensionesEliminar.size(); j++) {
                dimensionDAO.eliminar(dimensionesEliminar.get(j));
            }
            dimensionesEliminar.clear();
            criteriosEliminar = criterioValoracionDAO.buscarMarco(seleccionado);
            for (int j = 0; j < criteriosEliminar.size(); j++) {
                criterioValoracionDAO.eliminar(criteriosEliminar.get(j));
            }
            criteriosEliminar.clear();
            marcoDAO.eliminar(seleccionado);
        }
        if (tamano == 1) {
            anadirMensajeCorrecto(messages.getString("CORRTABMAR"));
        } else {
            anadirMensajeCorrecto(messages.getString("CORRTABMAR1"));
        }
    }
    
    /*Función que comprueba que se ha seleccionado un solo marco para poder editarlo*/
    public void update() {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        
        //Atributos
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            this.setSelectedMarcos(null);
            anadirMensajeError(messages.getString("ERRTABMAR"));
        } else if (tamano != 1) {
            this.setSelectedMarcos(null);
            anadirMensajeError(messages.getString("ERRTABMAR1"));
        } else {
            RequestContext.getCurrentInstance().execute("multiMarcoEditDialog.show();");
        }
    }

    /*Función que comprueba que todos los datos del formulario de edición estan correctos, y ademas,
    una vez ha comprobado que están correctos, actualiza el marco en la BD*/
    public void updateMarco() {
        
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        
        int tamano = seleccionados.length;
        MarcoTrabajo seleccionado = seleccionados[0];
        Long id = seleccionado.getId();
        
        this.setSelectedMarcos(null);

        if (seleccionado.getNombre().equals("")) {
            anadirMensajeError(messages.getString("ERRTABMAR2"));
        } else if (seleccionado.getDescripcion().equals("")) {
            anadirMensajeError(messages.getString("ERRTABMAR3"));
        } else if (gestorMarcoService.existeMarco(seleccionado.getNombre()) == true && gestorMarcoService.existeId(seleccionado.getNombre()) != id) {
            anadirMensajeError(messages.getString("ERRTABMAR4"));
        } else {
            marcoDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABMAR2"));
            RequestContext.getCurrentInstance().update("form");
        }
    }
    
    /*Función que comprueba que hemos seleccionado un marco para poder cargar un archivo xml sobre el*/
    public void fichero() throws IOException {
        
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);
        
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        
        if (tamano == 0) {
            this.setSelectedMarcos(null);
            anadirMensajeError(messages.getString("ERRTABMAR5"));
        } else if (tamano != 1) {
            this.setSelectedMarcos(null);
            anadirMensajeError(messages.getString("ERRTABMAR6"));
        } else {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect("marcoxml.xhtml");
        }
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedMarcos(null);
        context.redirect("marcos.xhtml");
    }

    public void cancelar() throws IOException {
        this.setSelectedMarcos(null);
        RequestContext.getCurrentInstance().execute("multiMarcoEditDialog.hide();");
    }

    public void atras1() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedMarcos(null);
        context.redirect("indexadministrador.xhtml");
    }
}
