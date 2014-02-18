package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.esei.tfg.controladores.modelos.MarcoModel;
import es.uvigo.esei.tfg.logica.daos.GestorMarcosDAO;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

@Named(value = "tablaMarcosController")
@SessionScoped
public class TablaMarcosController implements Serializable {

    private List<MarcoTrabajo> marcos;
    private MarcoTrabajo selectedMarco;
    private MarcoTrabajo[] selectedMarcos;
    private MarcoModel marcoModel;
    private boolean disponible = false;

    @Inject
    MarcoTrabajoDAO marcoDAO;
    
    @Inject
    GestorMarcosDAO gestorMarcoDAO;

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

    public MarcoTrabajo getSelectedMarco() {
        return selectedMarco;
    }

    public void setSelectedMarco(MarcoTrabajo selectedMarco) {
        this.selectedMarco = selectedMarco;
    }

    public MarcoModel getMarcoModel() {
        marcos = marcoDAO.buscarTodos();
        marcoModel = new MarcoModel(marcos);
        return marcoModel;
    }

    public void eliminar() {
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun marco");
        } else {
            RequestContext.getCurrentInstance().execute("multiMarcoDialog.show();");
        }
    }

    public void eliminarMarcos() {

        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        for (int i = 0; i < tamano; i++) {
            MarcoTrabajo seleccionado = seleccionados[i];
            marcoDAO.eliminar(seleccionado);
        }
        if (tamano == 1) {
            anadirMensajeCorrecto("El marco ha sido eliminado correctamente");
        } else {
            anadirMensajeCorrecto("Los marcos fueron eliminados correctamente");

        }
    }

    public void update() {
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;
        if (tamano == 0) {
            anadirMensajeError("No ha seleccionado ningun marco");
        } else if (tamano != 1) {
            anadirMensajeError("Solo puede seleccionar un marco para editarlo");
        } else {
            RequestContext.getCurrentInstance().execute("multiMarcoEditDialog.show();");
        }
    }

    public void updateMarco() {
        MarcoTrabajo[] seleccionados = this.getSelectedMarcos();
        int tamano = seleccionados.length;

        MarcoTrabajo seleccionado = seleccionados[0];
        if (seleccionado.getNombre().equals("")) {
            anadirMensajeError("Tienes que introducir un nombre para el marco");
        } else if (seleccionado.getDescripcion().equals("")) {
            anadirMensajeError("Tienes que introducir una descripcion para el marco");
        } else if (gestorMarcoDAO.existeMarco(seleccionado.getNombre()) == true) {
            anadirMensajeError("Ya existe un marco con ese nombre");
        } else {
            marcoDAO.actualizar(seleccionado);
            anadirMensajeCorrecto("El marco ha sido modificado correctamente");
            RequestContext.getCurrentInstance().update("form");
        }
    }

}
