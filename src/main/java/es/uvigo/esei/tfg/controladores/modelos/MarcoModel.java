package es.uvigo.esei.tfg.controladores.modelos; 
  
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;
  
  
public class MarcoModel extends ListDataModel<MarcoTrabajo> implements SelectableDataModel<MarcoTrabajo> {    
  
    public MarcoModel() {  
    }  
  
    public MarcoModel(List<MarcoTrabajo> data) {  
        super(data);  
    }  
      
    @Override  
    public MarcoTrabajo getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<MarcoTrabajo> marcos = (List<MarcoTrabajo>) getWrappedData();  
          
        for(MarcoTrabajo marco : marcos) {  
            if(marco.getNombre().equals(rowKey))  
                return marco;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(MarcoTrabajo marco) {  
        return marco.getNombre();  
    }  
}  
 