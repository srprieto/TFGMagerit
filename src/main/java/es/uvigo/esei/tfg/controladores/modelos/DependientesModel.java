/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Saul
 */

public class DependientesModel extends ListDataModel<Dependencia> implements SelectableDataModel<Dependencia> {    
  
    public DependientesModel() {  
    }  
  
    public DependientesModel(List<Dependencia> data) {  
        super(data);  
    }  
      
    @Override  
    public Dependencia getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Dependencia> dependientes = (List<Dependencia>) getWrappedData();  
          
        for(Dependencia dependiente : dependientes) {  
            String s = String.valueOf(dependiente.getId());
            if(s.equals(rowKey))  
                return dependiente;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Dependencia dependiente) {  
        return dependiente.getId();  
    }  
}  
 