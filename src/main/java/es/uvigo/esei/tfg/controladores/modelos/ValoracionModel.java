/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Saul
 */
public class ValoracionModel extends ListDataModel<Valoracion> implements SelectableDataModel<Valoracion>{
    
    public  ValoracionModel() {  
    }  
  
    public  ValoracionModel(List<Valoracion> data) {  
        super(data);  
    }  
      
    @Override  
    public Valoracion getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Valoracion> valoraciones = (List<Valoracion>) getWrappedData();  
          
        for(Valoracion valoracion : valoraciones) {  
            if(valoracion.getDimension().getNombre().equals(rowKey))  
                return valoracion;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Valoracion valoracion) {  
        return valoracion.getDimension().getNombre();  
    }  
} 

