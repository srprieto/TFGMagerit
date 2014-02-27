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
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.GestorActivoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
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

    @Inject
    ProyectoController proyecto;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    GestorActivoDAO gestorDAO;

    public ArbolActivosController() {

    }

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        Proyecto actual = proyecto.getProyectoActual();
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
            Activo seleccionado = activoDAO.buscarPorNombre(builder);
            TreeNode padre = node.getParent();
            String pa = padre.getData().toString();
            String[] separadas2 = pa.split(" ", 2);
            pa = separadas2[1];
            TipoActivo nuevo = tipoActivoDAO.buscarPorNombre(pa);
            activoDAO.eliminar(seleccionado);
            if (padre.getChildren() == null) {
                tipoActivoDAO.eliminar(nuevo);
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
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
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
        Activo seleccionado = activoDAO.buscarPorNombre(builder);
        Long id = seleccionado.getId();

        if (seleccionado.getCodigo().equals("")) {
            anadirMensajeError("Tienes que introducir un codigo para el activo");
        } else if (seleccionado.getNombre().equals("")) {
            anadirMensajeError("Tienes que introducir un nombre para el activo");
        } else if (seleccionado.getDescripcion().equals("")) {
            anadirMensajeError("Tienes que introducir una descripción para el activo");
        } else if (seleccionado.getResponsable().equals("")) {
            anadirMensajeError("Tienes que introducir un responsable para el activo");
        } else if (gestorDAO.existeActivo(seleccionado.getNombre()) == true && gestorDAO.existeId(seleccionado.getNombre()) != id) {
            anadirMensajeError("Ya existe un Activo con ese nombre");
        } else {
            activoDAO.actualizar(seleccionado);
            anadirMensajeCorrecto("El proyecto ha sido modificado correctamente");
        }
    }

}
