/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Saul
 */
@Named(value = "arbolAmenazasController")
@SessionScoped
public class ArbolAmenazasController implements Serializable{

    private TreeNode root;
    private TreeNode[] selectedNodes;
    
    @Inject
    ProyectoController proyectoController;
    
    @Inject
    ImpactoDAO impactoDAO;
    
    @Inject
    ArbolActivosController arbolActivosController;
    
    public ArbolAmenazasController() {

    }

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        Proyecto actual = proyectoController.getProyectoActual();
        List<TipoAmenaza> tipos = new ArrayList<>();
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
        List<Amenaza> amenazas = new ArrayList<Amenaza>();
        for (int z=0;z<impactos.size();z++){
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
    

}
