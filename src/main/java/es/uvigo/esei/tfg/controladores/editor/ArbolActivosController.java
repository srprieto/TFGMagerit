/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

/**
 *
 * @author Saul
 */
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
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
import org.primefaces.context.RequestContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named(value = "arbolActivosController")
@SessionScoped
public class ArbolActivosController implements Serializable {

    private TreeNode root;
    private TreeNode[] selectedNodes;
    private Activo activoActual;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String responsable;
    private String propietario;
    private String ubicacion;
    private Long cantidad;
    private String nombreGrupo;

    private TipoActivo tiposActivos;
    private GrupoActivos grupoActivos;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    GrupoActivosDAO grupoActivosDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ProyectoController proyectoController;

    @Inject
    TablaProyectosController tablaProyectoController;

    public ArbolActivosController() {

    }

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        Proyecto actual = proyectoController.getProyectoActual();
        List<TipoActivo> tipos = new ArrayList<>();
        List<Activo> activos = activoDAO.buscarActivosProyecto(actual);
        int tamano = activos.size();
        for (int j = 0; j < tamano; j++) {
            TipoActivo principal = activos.get(j).getTipoActivo();

            TreeNode tipoActivo = new DefaultTreeNode("[" + activos.get(j).getTipoActivo().getAbreviatura() + "] " + activos.get(j).getTipoActivo().getNombre(), root);
            TreeNode activo = new DefaultTreeNode("[" + activos.get(j).getCodigo() + "] " + activos.get(j).getNombre(), tipoActivo);

            for (int i = j + 1; i < tamano; i++) {
                if (activos.get(i).getTipoActivo().equals(principal)) {
                    TreeNode activorepetido = new DefaultTreeNode("[" + activos.get(i).getCodigo() + "] " + activos.get(i).getNombre(), tipoActivo);
                    activos.remove(i);
                    tamano = activos.size();
                    i--;
                }
            }

        }
        return root;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public TipoActivo getTiposActivos() {
        return tiposActivos;
    }

    public void setTiposActivos(TipoActivo tiposActivos) {
        this.tiposActivos = tiposActivos;
    }

    public GrupoActivos getGrupoActivos() {
        return grupoActivos;
    }

    public void setGrupoActivos(GrupoActivos grupoActivos) {
        this.grupoActivos = grupoActivos;
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

    public Activo getActivoActual() {
        return activoActual;
    }

    public void setActivoActual(Activo activoActual) {
        this.activoActual = activoActual;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
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

    public void eliminar() {
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos un activo para eliminarlo");
            valor = 2;
        } else {
            int tamano = selectedNodes.length;
            for (int i = 0; i < tamano; i++) {
                if (selectedNodes[i].getParent().equals(root)) {
                    valor = 1;
                }
            }
        }
        if (valor == 0) {
            RequestContext.getCurrentInstance().execute("multiDialog.show();");
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
        }
    }

    public void eliminarActivo() {
        String builder;

        for (TreeNode node : selectedNodes) {
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            List<Activo> lista = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getNombre().equals(builder)) {
                    Activo seleccionado = lista.get(i);
                    TreeNode padre = node.getParent();
                    String pa = padre.getData().toString();
                    String[] separadas2 = pa.split(" ", 2);
                    pa = separadas2[1];
                    TipoActivo nuevo = tipoActivoDAO.buscarPorNombre(pa);
                    List<Impacto> impactoEliminar = impactoDAO.buscarAmenazasActivo(seleccionado);
                    for (int j = 0; j < impactoEliminar.size(); j++) {
                        List<Degradacion> degradacionEliminar = degradacionDAO.buscarPorImpacto(impactoEliminar.get(j));
                        for (int z = 0; z < degradacionEliminar.size(); z++) {
                            degradacionDAO.eliminar(degradacionEliminar.get(z));
                        }
                        Amenaza amenazaEliminar = impactoEliminar.get(j).getAmenaza();
                        impactoDAO.eliminar(impactoEliminar.get(j));
                        amenazaDAO.eliminar(amenazaEliminar);
                    }
                    activoDAO.eliminar(seleccionado);
                    if (padre.getChildren() == null) {
                        tipoActivoDAO.eliminar(nuevo);
                    }
                }
            }
        }
        int tamano = selectedNodes.length;
        if (tamano == 1) {
            anadirMensajeCorrecto("El Activo ha sido eliminado correctamente");
        } else {
            anadirMensajeCorrecto("Los Activos fueron eliminados correctamente");
        }
    }

    public void update() {
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos un activo para editarlo");
            valor = 2;
        } else {
            int tamano = selectedNodes.length;
            if (tamano != 1) {
                valor = 3;
            }
            for (int i = 0; i < tamano; i++) {
                if (selectedNodes[i].getParent().equals(root)) {
                    valor = 1;
                }
            }
        }
        if (valor == 0) {
            String builder;
            TreeNode node = selectedNodes[0];
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            List<Activo> lista = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getNombre().equals(builder)) {
                    Activo seleccionado = lista.get(i);
                    codigo = seleccionado.getCodigo();
                    nombre = seleccionado.getNombre();
                    descripcion = seleccionado.getDescripcion();
                    responsable = seleccionado.getResponsable();
                    propietario = seleccionado.getPropietario();
                    ubicacion = seleccionado.getUbicacion();
                    tiposActivos = seleccionado.getTipoActivo();
                    grupoActivos = seleccionado.getGrupoActivos();
                    RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar un activo para editarlo");
        }
    }

    public void updateActivo() {
        String builder;
        TreeNode node = selectedNodes[0];
        builder = node.getData().toString();
        String[] separadas1 = builder.split(" ", 2);
        builder = separadas1[1];
        List<Activo> lista = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equals(builder)) {
                Activo seleccionado = lista.get(i);
                Long id = seleccionado.getId();
                if (codigo.equals("")) {
                    anadirMensajeError("Tienes que introducir un codigo para el activo");
                } else if (nombre.equals("")) {
                    anadirMensajeError("Tienes que introducir un nombre para el activo");
                } else if (descripcion.equals("")) {
                    anadirMensajeError("Tienes que introducir una descripción para el activo");
                } else if (seleccionado.getResponsable().equals("")) {
                    anadirMensajeError("Tienes que introducir un responsable para el activo");
                } else {
                    int valor = 1;
                    List<Activo> listanueva = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
                    for (int j = 0; j < listanueva.size(); j++) {
                        if (listanueva.get(j).getNombre().equals(nombre) && listanueva.get(j).getId() != seleccionado.getId()) {
                            valor = 0;
                        }
                    }
                    if (valor == 0) {
                        anadirMensajeError("Ya existe un Activo con ese nombre");
                    } else {
                        seleccionado.setCodigo(codigo);
                        seleccionado.setNombre(nombre);
                        seleccionado.setDescripcion(descripcion);
                        seleccionado.setPropietario(propietario);
                        seleccionado.setResponsable(responsable);
                        seleccionado.setUbicacion(ubicacion);
                        seleccionado.setTipoActivo(tiposActivos);
                        seleccionado.setGrupoActivos(grupoActivosDAO.buscarPorNombre(nombreGrupo));
                        activoDAO.actualizar(seleccionado);
                        anadirMensajeCorrecto("El proyecto ha sido modificado correctamente");
                        RequestContext.getCurrentInstance().update("form");
                    }
                }
            }
        }
    }

    public void doDestino() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos un activo para editarlo");
            context.redirect("activos.xhtml");
            valor = 2;
        } else {
            int tamano = selectedNodes.length;
            if (tamano != 1) {
                valor = 3;
            }
            for (int i = 0; i < tamano; i++) {
                if (selectedNodes[i].getParent().equals(root)) {
                    valor = 1;
                }
            }
        }
        if (valor == 0) {
            String builder;
            TreeNode node = selectedNodes[0];
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            List<Activo> listanueva = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int j = 0; j < listanueva.size(); j++) {
                if (listanueva.get(j).getNombre().equals(builder)) {
                    activoActual = listanueva.get(j);
                    context.redirect("dependencias.xhtml");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
            context.redirect("activos.xhtml");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar un activo para editarlo");
            context.redirect("activos.xhtml");
        }
    }

    public void valoracion() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos un activo para valorarlo");
            context.redirect("activos.xhtml");
            valor = 2;
        } else {
            int tamano = selectedNodes.length;
            if (tamano != 1) {
                valor = 3;
            }
            for (int i = 0; i < tamano; i++) {
                if (selectedNodes[i].getParent().equals(root)) {
                    valor = 1;
                }
            }
        }
        if (valor == 0) {
            String builder;
            TreeNode node = selectedNodes[0];
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            List<Activo> listanueva = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int j = 0; j < listanueva.size(); j++) {
                if (listanueva.get(j).getNombre().equals(builder)) {
                    activoActual = listanueva.get(j);
                    context.redirect("valoraciones.xhtml");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
            context.redirect("activos.xhtml");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar un activo para valorarlo");
            context.redirect("activos.xhtml");
        }
    }

    public void amenazas() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos un activo para valorarlo");
            context.redirect("activos.xhtml");
            valor = 2;
        } else {
            int tamano = selectedNodes.length;
            if (tamano != 1) {
                valor = 3;
            }
            for (int i = 0; i < tamano; i++) {
                if (selectedNodes[i].getParent().equals(root)) {
                    valor = 1;
                }
            }
        }
        if (valor == 0) {
            String builder;
            TreeNode node = selectedNodes[0];
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            List<Activo> listanueva = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int j = 0; j < listanueva.size(); j++) {
                if (listanueva.get(j).getNombre().equals(builder)) {
                    activoActual = listanueva.get(j);
                    context.redirect("amenazas.xhtml");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
            context.redirect("activos.xhtml");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar un activo para valorarlo");
            context.redirect("activos.xhtml");
        }
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedNodes(null);
        tablaProyectoController.setSelectedProyectos(null);
        context.redirect("misproyectos.xhtml");
    }
}
