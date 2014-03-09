/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.logica.daos;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.ItemValoracion;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.marco.TipoSalvaguarda;
import es.uvigo.esei.tfg.logica.daos.xml.ClassType;
import es.uvigo.esei.tfg.logica.daos.xml.ClassesType;
import es.uvigo.esei.tfg.logica.daos.xml.CriteriaType;
import es.uvigo.esei.tfg.logica.daos.xml.CriterionType;
import es.uvigo.esei.tfg.logica.daos.xml.DimensionType;
import es.uvigo.esei.tfg.logica.daos.xml.DimensionsType;
import es.uvigo.esei.tfg.logica.daos.xml.MageritExtension;
import es.uvigo.esei.tfg.logica.daos.xml.ThreatType;
import es.uvigo.esei.tfg.logica.daos.xml.ThreatsType;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Saul
 */
@Stateless
public class CargadorCatalogoJPA implements CargadorCatalogoDAO {


    // @PersistenceUnit // Cuando sea un EJB, se inyectarÃ¡, no se crearÃ¡ con EntityManagerFactory en inicializar()
    private EntityManager em;
    
    private MageritExtension mageritExtension;
    
    private Map<String, Dimension> tablaDimensiones;
    private Map<String, CriterioValoracion> tablaCriterios;
    private Map<String, TipoActivo> tablaTiposActivos;
    private Map<String, TipoAmenaza> tablaTiposAmenazas;
    private Map<String, TipoSalvaguarda> tablaTiposSalvaguardas;
     
    @Override
    public void inicializar() {
        
        // Tablas temporales
        tablaDimensiones = new HashMap<String, Dimension>();
        tablaCriterios = new HashMap<String, CriterioValoracion>();
        tablaTiposActivos = new HashMap<String, TipoActivo>();
        tablaTiposAmenazas = new HashMap<String, TipoAmenaza>();
        tablaTiposSalvaguardas = new HashMap<String, TipoSalvaguarda>();
    }

    @Override
    public void cargarRecurso(String localizacionRecurso, MarcoTrabajo marcoTrabajo) {
        try {
            JAXBContext context = JAXBContext.newInstance(MageritExtension.class);
            Unmarshaller um = context.createUnmarshaller();
            mageritExtension = (MageritExtension) um.unmarshal(new File(localizacionRecurso));
            
            cargarDimensiones(marcoTrabajo);
            cargarCriterios(marcoTrabajo);
            cargarTiposActivos(marcoTrabajo);
            cargarTiposAmenazas(marcoTrabajo);
            cargarTiposSalvaguardas(marcoTrabajo);
            
        } catch (JAXBException e) {
            System.out.println("Error cargando Catalogo MAGERIT desde " + localizacionRecurso);
        }
    }
    
    @Override
    public void alamacenarElementos() {              

        for (Dimension dimension : tablaDimensiones.values()) {
            em.persist(dimension);
        }
        
        for (CriterioValoracion criterio : tablaCriterios.values()) {
            em.persist(criterio);
        }
        
        for (TipoActivo tipoActivo : tablaTiposActivos.values()) {
            em.persist(tipoActivo);
        }
        
        for (TipoAmenaza tipoAmenaza : tablaTiposAmenazas.values()) {
            em.persist(tipoAmenaza);
        }
    }
    
    private void cargarDimensiones(MarcoTrabajo marcoTrabajo) {
        for (DimensionsType dimensiones : mageritExtension.getDimensions()) {
            for (DimensionType dimension : dimensiones.getDimension()) {
                Dimension nuevaDimension = new Dimension(dimension.getCode(), dimension.getName(), dimension.getDescription(), marcoTrabajo);
                tablaDimensiones.put(nuevaDimension.getAbreviatura(), nuevaDimension);
            }
        }
    }
    
    private void cargarCriterios(MarcoTrabajo marcoTrabajo) {
        for (CriteriaType criterios : mageritExtension.getCriteria()) {
            // Primer nivel define nombre de los criterios
            for (CriterionType criterioPrimerNivel : criterios.getCriterios()) {
                if ((criterioPrimerNivel.getSubCriteria() != null) && !criterioPrimerNivel.getSubCriteria().isEmpty()) { // Existe segundo nivel 
                    CriterioValoracion nuevoCriterio = new CriterioValoracion(criterioPrimerNivel.getCode(), criterioPrimerNivel.getName(), marcoTrabajo);
                    // Segundo nivel define valores de los items de cada criterio
                    for (CriterionType criterioSegundoNivel : criterioPrimerNivel.getSubCriteria()) {
                        ItemValoracion itemValoracion = new ItemValoracion(criterioSegundoNivel.getCode(), criterioSegundoNivel.getName(), criterioSegundoNivel.getValue());
                        nuevoCriterio.anadirItemValoracion(itemValoracion);
                    }
                    tablaCriterios.put(nuevoCriterio.getAbreviatura(), nuevoCriterio);
                }
            }
        }
    }
    
    private void cargarTiposActivos(MarcoTrabajo marcoTrabajo) {
        for (ClassesType clases : mageritExtension.getClasses()) {
            String etiquetaPadre = clases.getUnder();
            TipoActivo tipoActivoPadre = null;
            if ((etiquetaPadre != null) && !("none".equals(etiquetaPadre))) {
                tipoActivoPadre = tablaTiposActivos.get(etiquetaPadre);
            }
            for (ClassType clase : clases.getClasses()) {
                cargarTipoActivoRecursivo(clase, tipoActivoPadre, marcoTrabajo);
            }
        }
    }
    
    private void cargarTipoActivoRecursivo(ClassType clase, TipoActivo tipoActivoPadre, MarcoTrabajo marcoTrabajo) {
        TipoActivo tipoActivo = new TipoActivo(clase.getCode(), clase.getName(), clase.getDescription(), marcoTrabajo);
        if (tipoActivoPadre != null) {
            tipoActivoPadre.anadirTipoActivoHijo(tipoActivo);
        } else {
            tipoActivo.setTipoActivoPadre(null);
        }
        // Anotar tipo activo creado
        tablaTiposActivos.put(tipoActivo.getAbreviatura(), tipoActivo);
        // Procesar sub tipos (si los hay)
        List<ClassType> subClases = clase.getSubClasses();
        if ((subClases != null) && !subClases.isEmpty()) {
            for (ClassType claseHija : subClases) {
                cargarTipoActivoRecursivo(claseHija, tipoActivo, marcoTrabajo);
            }
        }
    }
    
    private void cargarTiposAmenazas(MarcoTrabajo marcoTrabajo) {
        for (ThreatsType threats : mageritExtension.getThreats()) {
            String etiquetaPadre = threats.getUnder();
            TipoAmenaza tipoAmenazaPadre = null;
            if ((etiquetaPadre != null) && !("none".equals(etiquetaPadre))) {
                tipoAmenazaPadre = tablaTiposAmenazas.get(etiquetaPadre);
            }
            for (ThreatType threat : threats.getThreats()) {
                cargarTipoAmenazaRecursivo(threat, tipoAmenazaPadre, marcoTrabajo);
            }
        }
    }
    
    private void cargarTipoAmenazaRecursivo(ThreatType threat, TipoAmenaza tipoAmenazaPadre, MarcoTrabajo marcoTrabajo) {
        TipoAmenaza tipoAmenaza = new TipoAmenaza(threat.getCode(), threat.getName(), threat.getDescription(), threat.getTho().name(), marcoTrabajo);
        if (tipoAmenazaPadre != null) {
            tipoAmenazaPadre.anadirTipoAmenazaHijo(tipoAmenaza);
        } else {
            tipoAmenaza.setTipoAmenazaPadre(null);
        }
        // Anotar tipo amenaza creada
        tablaTiposAmenazas.put(tipoAmenaza.getAbreviatura(), tipoAmenaza);

        // Procesar sub tipos (si los hay)
        List<ThreatType> subThreats = threat.getSubThreats();
        if ((subThreats != null) && !subThreats.isEmpty()) {
            for (ThreatType threatHija : subThreats) {
                cargarTipoAmenazaRecursivo(threatHija, tipoAmenaza, marcoTrabajo);
            }
        }

        // Vincular con dimensiones
        List<String> refDimensiones = threat.getDimensions();
        if ((refDimensiones != null) && (!refDimensiones.isEmpty())) {
            for (String refDimension : refDimensiones) {
                Dimension dimension = tablaDimensiones.get(refDimension);
                if (dimension != null) {
                    tipoAmenaza.anadirDimension(dimension);
                }
            }
        }
        
        Set<String> activos = new HashSet<String>();

        // Vincular con activos
        List<String> refTiposActivos = threat.getClasses();
        if ((refTiposActivos != null) && (!refTiposActivos.isEmpty())) {
            for (String refTipoActivo : refTiposActivos) {
                TipoActivo tipoActivo = tablaTiposActivos.get(refTipoActivo);
                if (tipoActivo != null) {
                    activos.add(tipoActivo.getAbreviatura());
                    
                    tipoAmenaza.anadirTipoActivo(tipoActivo);
                    tipoActivo.anadirTipoAmenaza(tipoAmenaza);
                }
            }
        }
      
    }
    
    private void cargarTiposSalvaguardas(MarcoTrabajo marcoTrabajo) {
        // PENDIENTE
    }
    
    
}

