/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.modelos;

import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Saul
 */
public class DegradacionModel extends ListDataModel<Degradacion> implements SelectableDataModel<Degradacion> {

    public DegradacionModel() {
    }

    public DegradacionModel(List<Degradacion> data) {
        super(data);
    }

    @Override
    public Degradacion getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Degradacion> degradaciones = (List<Degradacion>) getWrappedData();

        for (Degradacion degradacion : degradaciones) {
            if (degradacion.getDimension().getNombre().equals(rowKey)) {
                return degradacion;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Degradacion degradaciones) {
        return degradaciones.getDimension().getNombre();
    }
}
