/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.esei.tfg.logica.daos.DimensionDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorValoracionService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "valoracionController")
@SessionScoped
public class ValoracionController implements Serializable {

    private Double valor;
    private String justificacion;
    private Dimension dimension;
    private String nomDimension;
    private List<Dimension> dimensiones;

    @Inject
    DimensionDAO dimensionDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    GestorValoracionService gestorValoracionService;

    @Inject
    ProyectoController proyectoController;

    @Inject
    ArbolActivosController arbolActivosController;

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

    public List<String> getDimensiones() {
        dimensiones = dimensionDAO.buscarTodos(proyectoController.getProyectoActual().getMarcoTrabajo());
        List<Valoracion> seleccionadas = valoracionDAO.buscarPorActivo(arbolActivosController.getActivoActual());
        List<Dimension> valoradas = new ArrayList<>();
        for (int z = 0; z < seleccionadas.size(); z++) {
            valoradas.add(seleccionadas.get(z).getDimension());
        }

        for (int j = 0; j < dimensiones.size(); j++) {
            Dimension actual = dimensiones.get(j);
            for (int x = 0; x < valoradas.size(); x++) {
                if (valoradas.get(x).getNombre().equals(actual.getNombre())) {
                    dimensiones.remove(j);
                    j--;
                }
            }
        }

        List<String> nomDimensiones = new ArrayList<>();
        for (int i = 0; i < dimensiones.size(); i++) {
            nomDimensiones.add(dimensiones.get(i).getNombre());
        }

        return nomDimensiones;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getNomDimension() {
        return nomDimension;
    }

    public void setNomDimension(String nomDimension) {
        this.nomDimension = nomDimension;
    }

    public void setDimensiones(List<Dimension> dimensiones) {
        this.dimensiones = dimensiones;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public String doGuardar() {

        String destino;
        if (nomDimension.equals("")) {
            anadirMensajeError("No se ha indicado una dimension");
            destino = "valoracionnueva.xhtml";
        } else if (valor == null || valor < 0 || valor > 10) {
            anadirMensajeError("No se ha indicado un valor, tiene que introducir un valor entre 0 y 10");
            destino = "valoracionnueva.xhtml";
        } else if (justificacion.equals("")) {
            anadirMensajeError("No se ha indicado una justificaión, tiene que introducir una justificación para la valoración");
            destino = "valoracionnueva.xhtml";
        } else {
            destino = "confirmarvaloracion.xhtml";
        }
        return destino;
    }

    public void doCrear() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorValoracionService.crearNuevaValoracion(valor, justificacion, arbolActivosController.getActivoActual(), dimensionDAO.buscarPorNombre(nomDimension));
        anadirMensajeCorrecto("La valoración ha sido almacenada correctamente");
        valor = null;
        justificacion = "";
        context.redirect("valoraciones.xhtml");
    }

}
