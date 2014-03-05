/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.proyecto.Activo;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Saul
 */
public class ActivoModel extends ListDataModel<Activo> implements SelectableDataModel<Activo>{
    
    public ActivoModel() {  
    }  
  
    public ActivoModel(List<Activo> data) {  
        super(data);  
    }  
      
    @Override  
    public Activo getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Activo> activos = (List<Activo>) getWrappedData();  
          
        for(Activo activo : activos) {  
            if(activo.getNombre().equals(rowKey))  
                return activo;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Activo activo) {  
        return activo.getNombre();  
    }  
} 

