/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.esei.tfg.controladores.editor.ArbolActivosController;
import es.uvigo.esei.tfg.controladores.editor.ProyectoController;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Stateless
public class GestorDependenciaJPA implements GestorDependenciaDAO {

    @Inject
    DependenciaDAO dependenciaDAO;
    
    @Inject
    ProyectoController proyectoController;
    
    @Inject
    ActivoDAO activoDAO;
    
    @Inject
    ArbolActivosController arbolActivoController;
    
    @Override
    public void crearNuevaDependencia(String justificacion, Double grado, Activo activoPrincipal, Activo activoDependiente) {
        // Crear el usuario 
        Dependencia nuevo = new Dependencia(justificacion,grado,activoPrincipal,activoDependiente);
        dependenciaDAO.crear(nuevo);
    }
    
    @Override
    public List<String> devolverPosiblesDependencias() {
        
        Proyecto actual = proyectoController.getProyectoActual();
        List <Activo> activos = activoDAO.buscarActivosProyecto(actual);
        Activo seleccionado = arbolActivoController.getActivoActual();
        for (int i = 0; i < activos.size(); i++) {
            if (activos.get(i).getNombre().equals(seleccionado.getNombre())) {
                activos.remove(i);
            }
        }
        List<Dependencia> asociados = dependenciaDAO.buscarPorPrincipal(seleccionado);
        for (int i = 0; i < activos.size(); i++) {
            for (int j = 0; j < asociados.size(); j++) {
                if (asociados.get(j).getActivoDependiente().getNombre().equals(activos.get(i).getNombre())) {
                    System.out.println(activos.get(i).getNombre());
                    activos.remove(i);
                }
            }
        }
        List<String> nombres = new ArrayList<>();
        for(int i=0; i<activos.size();i++){
                nombres.add(activos.get(i).getNombre());
        }
        return nombres;
    }
}
