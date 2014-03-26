/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoAmenazaDAO;
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

/**
 *
 * @author Saul
 */
@Named(value = "arbolAmenazasController")
@SessionScoped
public class ArbolAmenazasController implements Serializable {

    private TreeNode root;
    private TreeNode[] selectedNodes;
    private String codigo;
    private String nomTipo;
    private String nombre;
    private String descripcion;
    private Double probabilidadOcurrencia;
    private Double gradoDegradacionBase;
    
    private Amenaza amenazaActual;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    TipoAmenazaDAO tipoAmenazaDAO;

    @Inject
    ArbolActivosController arbolActivosController;

    @Inject
    ProyectoController proyectoController;

    public ArbolAmenazasController() {

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

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        Proyecto actual = proyectoController.getProyectoActual();
        List<TipoAmenaza> tipos = new ArrayList<>();
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
        List<Amenaza> amenazas = new ArrayList<Amenaza>();
        for (int z = 0; z < impactos.size(); z++) {
            amenazas.add(impactos.get(z).getAmenaza());
        }
        int tamano = amenazas.size();
        for (int j = 0; j < tamano; j++) {
            TipoAmenaza principal = amenazas.get(j).getTipoAmenaza();

            TreeNode tipoAmenaza = new DefaultTreeNode("[" + amenazas.get(j).getTipoAmenaza().getAbreviatura() + "] " + amenazas.get(j).getTipoAmenaza().getNombre(), root);
            TreeNode amenaza = new DefaultTreeNode("[" + amenazas.get(j).getCodigo() + "] " + amenazas.get(j).getNombre(), tipoAmenaza);

            for (int i = j + 1; i < tamano; i++) {
                if (amenazas.get(i).getTipoAmenaza().equals(principal)) {
                    TreeNode amenazarepetida = new DefaultTreeNode("[" + amenazas.get(i).getCodigo() + "] " + amenazas.get(i).getNombre(), tipoAmenaza);
                    amenazas.remove(i);
                    tamano = amenazas.size();
                    i--;
                }
            }

        }
        return root;
    }

    public Amenaza getAmenazaActual() {
        return amenazaActual;
    }

    public void setAmenazaActual(Amenaza amenazaActual) {
        this.amenazaActual = amenazaActual;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomTipo() {
        return nomTipo;
    }

    public void setNomTipo(String nomTipo) {
        this.nomTipo = nomTipo;
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

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public void atras() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedNodes(null);
        arbolActivosController.setSelectedNodes(null);
        context.redirect("activos.xhtml");
    }

    public void eliminar() {
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar al menos una amenaza para eliminarla");
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

    public void eliminarAmenaza() {
        String builder;

        for (TreeNode node : selectedNodes) {
            builder = node.getData().toString();
            String[] separadas1 = builder.split(" ", 2);
            builder = separadas1[1];
            Activo seleccionado = arbolActivosController.getActivoActual();
            List<Impacto> relaciones = impactoDAO.buscarAmenazasActivo(seleccionado);
            for (int i = 0; i < relaciones.size(); i++) {
                if (relaciones.get(i).getAmenaza().getNombre().equals(builder)) {
                    Amenaza selected = relaciones.get(i).getAmenaza();
                    TreeNode padre = node.getParent();
                    String pa = padre.getData().toString();
                    String[] separadas2 = pa.split(" ", 2);
                    pa = separadas2[1];
                    TipoAmenaza nuevo = tipoAmenazaDAO.buscarPorNombre(pa);
                    List<Impacto> impactosEliminar = impactoDAO.buscarActivoAmenazas(selected);
                    for (int j = 0; j < impactosEliminar.size(); j++) {
                        if (impactosEliminar.get(j).getActivo().getNombre().equals(seleccionado.getNombre())) {
                            List<Degradacion> degradacionEliminar = degradacionDAO.buscarPorImpacto(impactosEliminar.get(j));
                            for (int z = 0; z < degradacionEliminar.size(); z++) {
                                degradacionDAO.eliminar(degradacionEliminar.get(z));
                            }
                            impactoDAO.eliminar(impactosEliminar.get(j));
                        }
                    }
                    amenazaDAO.eliminar(selected);
                    if (padre.getChildren() == null) {
                        tipoAmenazaDAO.eliminar(nuevo);
                    }
                }
            }
        }
        int tamano = selectedNodes.length;
        if (tamano == 1) {
            anadirMensajeCorrecto("La Amenaza ha sido eliminada correctamente");
        } else {
            anadirMensajeCorrecto("Las Amenazas fueron eliminadas correctamente");
        }
    }

    public void update() {
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar una amenaza para editarla");
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
            List<Impacto> lista = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getAmenaza().getNombre().equals(builder)) {
                    Amenaza seleccionado = lista.get(i).getAmenaza();
                    codigo = seleccionado.getCodigo();
                    nombre = seleccionado.getNombre();
                    descripcion = seleccionado.getDescripcion();
                    probabilidadOcurrencia = seleccionado.getProbabilidadOcurrencia();
                    gradoDegradacionBase = seleccionado.getGradoDegradacionBase();
                    RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar un activo para editarlo");
        }
    }

    public void updateAmenaza() {
        String builder;
        TreeNode node = selectedNodes[0];
        builder = node.getData().toString();
        String[] separadas1 = builder.split(" ", 2);
        builder = separadas1[1];
        List<Impacto> lista = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getAmenaza().getNombre().equals(builder)) {
                Amenaza seleccionado = lista.get(i).getAmenaza();
                Long id = seleccionado.getId();
                if (codigo.equals("")) {
                    anadirMensajeError("Tienes que introducir un codigo para la Amenaza");
                } else if (nombre.equals("")) {
                    anadirMensajeError("Tienes que introducir un nombre para la Amenaza");
                } else if (descripcion.equals("")) {
                    anadirMensajeError("Tienes que introducir una descripción para la Amenaza");
                } else if (seleccionado.getGradoDegradacionBase() == null) {
                    anadirMensajeError("Tienes que introducir un grado de degradacion para la Amenaza");
                } else if (seleccionado.getGradoDegradacionBase() == null) {
                    anadirMensajeError("Tienes que introducir un grado de degradacion para la Amenaza");
                } else {
                    int valor = 1;
                    List<Impacto> listanueva = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
                    for (int j = 0; j < listanueva.size(); j++) {
                        if (lista.get(j).getAmenaza().getNombre().equals(nombre) && lista.get(j).getAmenaza().getId() != seleccionado.getId()) {
                            valor = 0;
                        }
                    }

                    if (valor == 0) {
                        anadirMensajeError("Ya existe una Amenaza con ese nombre");
                    } else {
                        TipoAmenaza tip = tipoAmenazaDAO.buscarPorNombre(nomTipo);
                        seleccionado.setCodigo(codigo);
                        seleccionado.setNombre(nombre);
                        seleccionado.setDescripcion(descripcion);
                        seleccionado.setGradoDegradacionBase(gradoDegradacionBase);
                        seleccionado.setProbabilidadOcurrencia(probabilidadOcurrencia);
                        seleccionado.setTipoAmenaza(tip);
                        amenazaDAO.actualizar(seleccionado);
                        anadirMensajeCorrecto("La Amenaza ha sido modificada correctamente");
                        RequestContext.getCurrentInstance().update("form");
                    }
                }
            }
        }
    }

    public void valoracion() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        int valor = 0;
        if (selectedNodes == null) {
            anadirMensajeError("Tienes que seleccionar una amenaza para valorarla");
            context.redirect("amenazas.xhtml");
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
            List<Impacto> lista = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getAmenaza().getNombre().equals(builder)) {
                    amenazaActual = lista.get(i).getAmenaza();
                    context.redirect("valoracionamenaza.xhtml");
                }
            }
        } else if (valor == 1) {
            anadirMensajeError("No puedes seleccionar un Tipo");
            context.redirect("amenazas.xhtml");
        } else if (valor == 3) {
            anadirMensajeError("Solo puede seleccionar una amenaza para valorarla");
            context.redirect("amenazas.xhtml");
        }
    }
}
