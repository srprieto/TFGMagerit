package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;
 
  
public class ProyectoModel extends ListDataModel<Proyecto> implements SelectableDataModel<Proyecto> {    
  
    public ProyectoModel() {  
    }  
  
    public ProyectoModel(List<Proyecto> data) {  
        super(data);  
    }  
      
    @Override  
    public Proyecto getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Proyecto> proyectos = (List<Proyecto>) getWrappedData();  
          
        for(Proyecto proyecto : proyectos) {  
            if(proyecto.getNombre().equals(rowKey))  
                return proyecto;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Proyecto proyecto) {  
        return proyecto.getNombre();  
    }  
}  