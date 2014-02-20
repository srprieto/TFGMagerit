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
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import java.io.Serializable;  
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
import javax.inject.Inject;
import javax.inject.Named;
  
import org.primefaces.model.DefaultTreeNode;  
import org.primefaces.model.TreeNode;  

@Named(value = "arbolActivosController")
@SessionScoped
public class ArbolActivosController implements Serializable {  
    
    
    @Inject
    ActivoDAO activoDAO;
    
    @Inject
    TipoActivoDAO tipoActivoDAO;
    
    @Inject
    ProyectoController proyectoController;
  
    private TreeNode root;  
    
    private TreeNode selectedNode;  
  
    public ArbolActivosController () { 

    }  
  
    public TreeNode getRoot() {
        Proyecto actual= proyectoController.getProyectoActual();
        List<Activo> activos = activoDAO.buscarActivosProyecto(actual);
        
        
        root = new DefaultTreeNode("Root", null);  
        TreeNode node0 = new DefaultTreeNode("Node 0", root);  
        TreeNode node1 = new DefaultTreeNode("Node 1", root);  
        TreeNode node2 = new DefaultTreeNode("Node 2", root);  
  
        TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);  
        TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);  
  
        TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);  
        TreeNode node11 = new DefaultTreeNode("Node 1.1", node1);  
  
        TreeNode node000 = new DefaultTreeNode("Node 0.0.0", node00);  
        TreeNode node001 = new DefaultTreeNode("Node 0.0.1", node00);  
        TreeNode node010 = new DefaultTreeNode("Node 0.1.0", node01);  
  
        TreeNode node100 = new DefaultTreeNode("Node 1.0.0", node10); 
        
        return root;  
    }  
  
    public TreeNode getSelectedNode() {  
        return selectedNode;  
    }  
  
    public void setSelectedNode(TreeNode selectedNode) {  
        this.selectedNode = selectedNode;  
    }  
      
    public void displaySelectedSingle() {  
        if(selectedNode != null) {  
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());  
  
            FacesContext.getCurrentInstance().addMessage(null, message);  
        }  
    }
}  
           