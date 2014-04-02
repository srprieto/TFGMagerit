/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Riesgo;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DimensionDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoAmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorAmenazasService;
import es.uvigo.esei.tfg.logica.servicios.GestorDegradacionService;
import es.uvigo.esei.tfg.logica.servicios.GestorImpactoService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Saul
 */
@Named(value = "amenazaController")
@SessionScoped
public class AmenazaController implements Serializable {

    private List<TipoAmenaza> tiposAmenazas;

    private String codigo;
    private String nomTipo;
    private String nombre;
    private String descripcion;
    private Double probabilidadOcurrencia;
    private Double gradoDegradacionBase;

    private TipoAmenaza tipoAmenaza;

    @Inject
    TipoAmenazaDAO tipoAmenazaDAO;

    @Inject
    DimensionDAO dimensionDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ActivoController activoController;

    @Inject
    ArbolActivosController arbolActivosController;

    @Inject
    ProyectoController proyectoController;

    @Inject
    GestorAmenazasService gestorAmenazasService;

    @Inject
    GestorDegradacionService gestorDegradacionService;

    @Inject
    GestorImpactoService gestorImpactoService;

    public void AmenazaController() {

    }

    /**
     * Añade un mensaje de error a la jeraquia de componetes de la página JSF
     *
     * @param mensaje
     */
    protected void anadirMensajeError(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    protected void anadirMensajeCorrecto(String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null));
    }

    public TipoAmenaza getTipoAmenaza() {
        return tipoAmenaza;
    }

    public void setTipoAmenaza(TipoAmenaza tipoAmenaza) {
        this.tipoAmenaza = tipoAmenaza;
    }

    public String getNomTipo() {
        return nomTipo;
    }

    public void setNomTipo(String nomTipo) {
        this.nomTipo = nomTipo;
    }

    public List<TipoAmenaza> getTiposAmenazas() {
        return tiposAmenazas;
    }

    public void setTiposAmenazas(List<TipoAmenaza> tiposAmenazas) {
        this.tiposAmenazas = tiposAmenazas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getProbabilidadOcurrencia() {
        return probabilidadOcurrencia;
    }

    public void setProbabilidadOcurrencia(Double probabilidadOcurrencia) {
        this.probabilidadOcurrencia = probabilidadOcurrencia;
    }

    public Double getGradoDegradacionBase() {
        return gradoDegradacionBase;
    }

    public void setGradoDegradacionBase(Double gradoDegradacionBase) {
        this.gradoDegradacionBase = gradoDegradacionBase;
    }

    public List<Double> getProbabilidad() {
        List<Double> probabilidades = new ArrayList<>();
        probabilidades.add(100 * 1.0);
        probabilidades.add(10 * 1.0);
        probabilidades.add(1 * 1.0);
        probabilidades.add(0.1);
        probabilidades.add(0.01);

        return probabilidades;
    }

    public String doGuargar() {
        String destino;
        if (codigo.equals("")) {
            anadirMensajeError("No se ha indicado un codigo para la amenaza");
            destino = "crearamenaza.xhtml";
        } else if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para la amenaza");
            destino = "crearamenaza.xhtml";
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción para la amenaza");
            destino = "crearamenaza.xhtml";
        } else if (gradoDegradacionBase == null) {
            anadirMensajeError("No se ha indicado una degradación para la amenaza");
            destino = "crearamenaza.xhtml";
        } else {

            int valor = 1;
            List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(arbolActivosController.getActivoActual());
            for (int j = 0; j < impactos.size(); j++) {
                if (impactos.get(j).getAmenaza().getNombre().equals(nombre)) {
                    valor = 0;
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < codigo.length(); x++) {
                if (codigo.charAt(x) != ' ') {
                    sb.append(codigo.charAt(x));
                } else {
                    sb.append("_");
                }
            }
            setCodigo(sb.toString());
            if (valor == 1) {
                destino = "confirmaramenaza.xhtml";
            } else {
                anadirMensajeError("Ya existe una Amenaza con ese nombre");
                destino = "crearamenaza.xhtml";
            }
        }
        return destino;
    }

    public List<String> getTipos() {
        List<TipoAmenaza> posibles = tipoAmenazaDAO.buscarTipoActivo(arbolActivosController.getActivoActual().getTipoActivo());
        List<String> nombres = new ArrayList<>();
        for (int i = 0; i < posibles.size(); i++) {
            nombres.add(posibles.get(i).getNombre());
        }
        return nombres;
    }

    public void guardarAmenaza() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        List<Dimension> dimensiones = new ArrayList<>();
        List<TipoAmenaza> tipos = new ArrayList<>();
        List<Impacto> impact = new ArrayList<>();
        Activo actual;
        List<TipoAmenaza> tipo = tipoAmenazaDAO.buscarMarco(arbolActivosController.getActivoActual().getProyecto().getMarcoTrabajo());
        for (int i = 0; i < tipo.size(); i++) {
            if (tipo.get(i).getNombre().equals(nomTipo)) {
                tipoAmenaza = tipo.get(i);
            }
        }
        Amenaza creada = gestorAmenazasService.crearAmenaza(codigo, nombre, descripcion, probabilidadOcurrencia, gradoDegradacionBase, tipoAmenaza, proyectoController.getProyectoActual());
        gestorImpactoService.crearNuevoImpacto(Calendar.getInstance().getTime(), arbolActivosController.getActivoActual(), creada);
        dimensiones = dimensionDAO.buscarTodos(arbolActivosController.getActivoActual().getProyecto().getMarcoTrabajo());
        for (int i = 0; i < dimensiones.size(); i++) {
            tipos = tipoAmenazaDAO.buscarDimension(dimensiones.get(i));
            for (int j = 0; j < tipos.size(); j++) {
                if (tipos.get(j).getNombre().equals(nomTipo)) {
                    actual = arbolActivosController.getActivoActual();
                    impact = impactoDAO.buscarAmenazasActivo(actual);
                    for (int z = 0; z < impact.size(); z++) {
                        if (impact.get(z).getAmenaza().getNombre().equals(nombre)) {
                            gestorDegradacionService.crearNuevaDegradacion(gradoDegradacionBase, probabilidadOcurrencia, impact.get(z), dimensiones.get(i));
                        }
                    }
                }
            }
        }
        anadirMensajeCorrecto("La Amenaza " + nombre + " ha sido guardada correctamente");
        nombre = "";
        descripcion = "";
        codigo = "";
        nomTipo = "";
        probabilidadOcurrencia = null;
        gradoDegradacionBase = null;
        context.redirect("amenazas.xhtml");
    }

    public List<Degradacion> getImpactoRepercutido() {

        List<Degradacion> degradaciones = new ArrayList<>();
        List<Degradacion> resultado = new ArrayList<>();

        Double valor;
        Double degradacion;
        Double gradoReal;
        Double v;
        int val;

        Degradacion principal;
        Amenaza seleccionada;

        Activo actual = arbolActivosController.getActivoActual();
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);
        List<Valoracion> valorAcumulado = valoracionDAO.buscarPorActivo(actual);

        if (!valorAcumulado.isEmpty()) {
            for (int i = 0; i < impactos.size(); i++) {
                seleccionada = impactos.get(i).getAmenaza();
                degradaciones = degradacionDAO.buscarPorImpacto(impactos.get(i));
                for (int j = 0; j < degradaciones.size(); j++) {
                    principal = degradaciones.get(j);
                    for (int r = 0; r < valorAcumulado.size(); r++) {
                        if (principal.getDimension().getNombre().equals(valorAcumulado.get(r).getDimension().getNombre()) && actual.getNombre().equals(valorAcumulado.get(r).getActivo().getNombre())) {
                            Degradacion nuevaDegradacion = new Degradacion();
                            valor = valorAcumulado.get(r).getValor();
                            degradacion = principal.getGrado();
                            v = valor * (degradacion / 100);
                            val = this.round(v);
                            gradoReal = val * 1.0;
                            nuevaDegradacion.setGrado(gradoReal);
                            nuevaDegradacion.setDimension(principal.getDimension());
                            nuevaDegradacion.setImpacto(impactos.get(i));
                            nuevaDegradacion.setProbabilidad(principal.getProbabilidad());
                            resultado.add(nuevaDegradacion);
                        }
                    }
                }
            }
        } 
        return resultado;

    }
    
    public List<Degradacion> getImpactoRepercutido(Activo activo) {

        List<Degradacion> degradaciones = new ArrayList<>();
        List<Degradacion> resultado = new ArrayList<>();

        Double valor;
        Double degradacion;
        Double gradoReal;
        Double v;
        int val;

        Degradacion principal;
        Amenaza seleccionada;

        Activo actual = activo;
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);
        List<Valoracion> valorAcumulado = valoracionDAO.buscarPorActivo(actual);

        if (!valorAcumulado.isEmpty()) {
            for (int i = 0; i < impactos.size(); i++) {
                seleccionada = impactos.get(i).getAmenaza();
                degradaciones = degradacionDAO.buscarPorImpacto(impactos.get(i));
                for (int j = 0; j < degradaciones.size(); j++) {
                    principal = degradaciones.get(j);
                    for (int r = 0; r < valorAcumulado.size(); r++) {
                        if (principal.getDimension().getNombre().equals(valorAcumulado.get(r).getDimension().getNombre()) && actual.getNombre().equals(valorAcumulado.get(r).getActivo().getNombre())) {
                            Degradacion nuevaDegradacion = new Degradacion();
                            valor = valorAcumulado.get(r).getValor();
                            degradacion = principal.getGrado();
                            v = valor * (degradacion / 100);
                            val = this.round(v);
                            gradoReal = val * 1.0;
                            nuevaDegradacion.setGrado(gradoReal);
                            nuevaDegradacion.setDimension(principal.getDimension());
                            nuevaDegradacion.setImpacto(impactos.get(i));
                            nuevaDegradacion.setProbabilidad(principal.getProbabilidad());
                            resultado.add(nuevaDegradacion);
                        }
                    }
                }
            }
        } 
        return resultado;

    }


    public List<Degradacion> getImpacto() {

        List<Degradacion> degradaciones = new ArrayList<>();
        List<Degradacion> resultado = new ArrayList<>();

        Double valor;
        Double degradacion;
        Double gradoReal;
        Double v;
        int val;

        Degradacion principal;
        Amenaza seleccionada;

        Activo actual = arbolActivosController.getActivoActual();
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);
        List<Valoracion> valorAcumulado = activoController.getModeloValor();

        for (int i = 0; i < impactos.size(); i++) {
            seleccionada = impactos.get(i).getAmenaza();
            degradaciones = degradacionDAO.buscarPorImpacto(impactos.get(i));
            for (int j = 0; j < degradaciones.size(); j++) {
                principal = degradaciones.get(j);
                for (int r = 0; r < valorAcumulado.size(); r++) {
                    if (principal.getDimension().getNombre().equals(valorAcumulado.get(r).getDimension().getNombre()) && actual.getNombre().equals(valorAcumulado.get(r).getActivo().getNombre())) {
                        Degradacion nuevaDegradacion = new Degradacion();
                        valor = valorAcumulado.get(r).getValor();
                        degradacion = principal.getGrado();
                        v = valor * (degradacion / 100);
                        val = this.round(v);
                        gradoReal = val * 1.0;
                        nuevaDegradacion.setGrado(gradoReal);
                        nuevaDegradacion.setDimension(principal.getDimension());
                        nuevaDegradacion.setImpacto(impactos.get(i));
                        nuevaDegradacion.setProbabilidad(principal.getProbabilidad());
                        resultado.add(nuevaDegradacion);
                    }
                }
            }
        }
        return resultado;

    }

    private int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    public List<Degradacion> getImpacto(Activo activo) {

        List<Degradacion> degradaciones = new ArrayList<>();
        List<Degradacion> resultado = new ArrayList<>();
        List<Valoracion> valorAcumulado = activoController.getModeloValor();
        Double valor;
        Double degradacion;
        Double gradoReal;
        Double v;
        Degradacion principal;
        Amenaza seleccionada;
        int val;

        Activo actual = activo;
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);

        for (int i = 0; i < impactos.size(); i++) {
            seleccionada = impactos.get(i).getAmenaza();
            degradaciones = degradacionDAO.buscarPorImpacto(impactos.get(i));
            for (int j = 0; j < degradaciones.size(); j++) {
                principal = degradaciones.get(j);
                for (int r = 0; r < valorAcumulado.size(); r++) {
                    if (principal.getDimension().getNombre().equals(valorAcumulado.get(r).getDimension().getNombre()) && actual.getNombre().equals(valorAcumulado.get(r).getActivo().getNombre())) {
                        Degradacion nuevaDegradacion = new Degradacion();
                        valor = valorAcumulado.get(r).getValor();
                        degradacion = principal.getGrado();
                        v = valor * (degradacion / 100);
                        val = this.round(v);
                        gradoReal = val * 1.0;
                        nuevaDegradacion.setGrado(gradoReal);
                        nuevaDegradacion.setDimension(principal.getDimension());
                        nuevaDegradacion.setImpacto(impactos.get(i));
                        nuevaDegradacion.setProbabilidad(principal.getProbabilidad());
                        resultado.add(nuevaDegradacion);
                    }
                }
            }
        }
        return resultado;

    }
    
    public List<Riesgo> getRiesgoRepercutido() {

        Activo actual = arbolActivosController.getActivoActual();
        
        List<Riesgo> resultado = new ArrayList<>();
        List<Degradacion> impacto = this.getImpactoRepercutido(actual);
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);

        Amenaza seleccionada;
        Double valor;
        String daño;
        int val = 0;

        for (int i = 0; i < impactos.size(); i++) {
            seleccionada = impactos.get(i).getAmenaza();
            for (int j = 0; j < impacto.size(); j++) {
                if (seleccionada.getNombre().equals(impacto.get(j).getImpacto().getAmenaza().getNombre())) {
                    valor = impacto.get(j).getGrado();
                    Riesgo riesgo = new Riesgo();
                    if (valor == 0 && impacto.get(j).getProbabilidad() < 10) {
                        val = 0;
                    } else if (valor == 0 && impacto.get(j).getProbabilidad() >= 10) {
                        val = 1;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 0;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 2;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 3;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 2;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 4;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 6;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 5;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 7;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 9;
                    } else if (valor >= 9 && valor <= 10 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 8;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 100) {
                        val = 10;
                    }

                    if (val == 0) {
                        daño = "Despreciable";
                    } else if (val >= 1 && val <= 2) {
                        daño = "Bajo";
                    } else if (val >= 3 && val <= 5) {
                        daño = "Apreciable";
                    } else if (val >= 6 && val <= 8) {
                        daño = "Importante";
                    } else {
                        daño = "Crítico";
                    }
                    riesgo.setGravedad(daño);
                    riesgo.setValor(val);
                    riesgo.setDimension(impacto.get(j).getDimension());
                    riesgo.setImpacto(impacto.get(j).getImpacto());

                    resultado.add(riesgo);
                }
            }
        }
        return resultado;
    }

    public List<Riesgo> getRiesgo() {

        List<Riesgo> resultado = new ArrayList<>();
        List<Degradacion> impacto = this.getImpacto(arbolActivosController.getActivoActual());

        Activo actual = arbolActivosController.getActivoActual();
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);

        Amenaza seleccionada;
        Double valor;
        String daño;
        int val = 0;

        for (int i = 0; i < impactos.size(); i++) {
            seleccionada = impactos.get(i).getAmenaza();
            for (int j = 0; j < impacto.size(); j++) {
                if (seleccionada.getNombre().equals(impacto.get(j).getImpacto().getAmenaza().getNombre())) {
                    valor = impacto.get(j).getGrado();
                    Riesgo riesgo = new Riesgo();
                    if (valor == 0 && impacto.get(j).getProbabilidad() < 10) {
                        val = 0;
                    } else if (valor == 0 && impacto.get(j).getProbabilidad() >= 10) {
                        val = 1;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 0;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 2;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 3;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 2;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 4;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 6;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 5;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 7;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 9;
                    } else if (valor >= 9 && valor <= 10 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 8;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 100) {
                        val = 10;
                    }

                    if (val == 0) {
                        daño = "Despreciable";
                    } else if (val >= 1 && val <= 2) {
                        daño = "Bajo";
                    } else if (val >= 3 && val <= 5) {
                        daño = "Apreciable";
                    } else if (val >= 6 && val <= 8) {
                        daño = "Importante";
                    } else {
                        daño = "Crítico";
                    }
                    riesgo.setGravedad(daño);
                    riesgo.setValor(val);
                    riesgo.setDimension(impacto.get(j).getDimension());
                    riesgo.setImpacto(impacto.get(j).getImpacto());

                    resultado.add(riesgo);
                }
            }
        }
        return resultado;
    }

    public List<Riesgo> getRiesgo(Activo activo) {

        List<Riesgo> resultado = new ArrayList<>();
        List<Degradacion> impacto = this.getImpacto(activo);

        Activo actual = activo;
        List<Impacto> impactos = impactoDAO.buscarAmenazasActivo(actual);

        Amenaza seleccionada;
        Double valor;
        String daño;
        int val = 0;

        for (int i = 0; i < impactos.size(); i++) {
            seleccionada = impactos.get(i).getAmenaza();
            for (int j = 0; j < impacto.size(); j++) {
                if (seleccionada.getNombre().equals(impacto.get(j).getImpacto().getAmenaza().getNombre())) {
                    valor = impacto.get(j).getGrado();
                    Riesgo riesgo = new Riesgo();
                    if (valor == 0 && impacto.get(j).getProbabilidad() < 10) {
                        val = 0;
                    } else if (valor == 0 && impacto.get(j).getProbabilidad() >= 10) {
                        val = 1;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 0;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 2;
                    } else if (valor >= 1 && valor <= 2 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 3;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 2;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 4;
                    } else if (valor >= 3 && valor <= 5 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 6;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 5;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 1) {
                        val = 7;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= 10 && impacto.get(j).getProbabilidad() <= 100) {
                        val = 9;
                    } else if (valor >= 9 && valor <= 10 && impacto.get(j).getProbabilidad() == (1 / 100)) {
                        val = 8;
                    } else if (valor >= 6 && valor <= 8 && impacto.get(j).getProbabilidad() >= (1 / 10) && impacto.get(j).getProbabilidad() <= 100) {
                        val = 10;
                    }

                    if (val == 0) {
                        daño = "Despreciable";
                    } else if (val >= 1 && val <= 2) {
                        daño = "Bajo";
                    } else if (val >= 3 && val <= 5) {
                        daño = "Apreciable";
                    } else if (val >= 6 && val <= 8) {
                        daño = "Importante";
                    } else {
                        daño = "Crítico";
                    }
                    riesgo.setGravedad(daño);
                    riesgo.setValor(val);
                    riesgo.setDimension(impacto.get(j).getDimension());
                    riesgo.setImpacto(impacto.get(j).getImpacto());

                    resultado.add(riesgo);
                }
            }
        }
        return resultado;
    }

}
