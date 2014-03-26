/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorActivoService;
import es.uvigo.esei.tfg.logica.daos.GrupoActivosDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "activoController")
@SessionScoped
public class ActivoController implements Serializable {

    private Activo activoEnEdicion;

    private List<TipoActivo> tiposActivos;
    private List<CriterioValoracion> valorBase;
    private List<GrupoActivos> grupoActivos;

    private String nombreGrupo;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String responsable;
    private String propietario;
    private String ubicacion;
    private Long cantidad;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    GrupoActivosDAO grupoActivoDAO;

    @Inject
    DependenciaDAO dependenciaDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    GestorActivoService gestorActivoService;

    @Inject
    ProyectoController proyectoController;

    @Inject
    ArbolActivosController arbolActivosController;

    @Inject
    AmenazaController amenazaController;

    public ActivoController() {

    }

    @PostConstruct
    private void inicializar() {
        activoEnEdicion = new Activo();
        tiposActivos = tipoActivoDAO.buscarTodos();
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

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Activo getActivoEnEdicion() {
        return activoEnEdicion;
    }

    public void setActivoEnEdicion(Activo activoEnEdicion) {
        this.activoEnEdicion = activoEnEdicion;
    }

    public List<TipoActivo> getTiposActivos() {
        return tiposActivos;
    }

    public void setTiposActivos(List<TipoActivo> tiposActivos) {
        this.tiposActivos = tiposActivos;
    }

    public List<CriterioValoracion> getValorBase() {
        return valorBase;
    }

    public void setValorBase(List<CriterioValoracion> valorBase) {
        this.valorBase = valorBase;
    }

    public void setGrupoActivos(List<GrupoActivos> grupoActivos) {
        this.grupoActivos = grupoActivos;
    }

    public String doGuargar() {
        String destino;
        if (codigo.equals("")) {
            anadirMensajeError("No se ha indicado un codigo para el activo");
            destino = "crearactivo.xhtml";
        } else if (nombre.equals("")) {
            anadirMensajeError("No se ha indicado un nombre para el activo");
            destino = "crearactivo.xhtml";
        } else if (descripcion.equals("")) {
            anadirMensajeError("No se ha indicado una descripción para el activo");
            destino = "crearactivo.xhtml";
        } else if (responsable.equals("")) {
            anadirMensajeError("No se ha indicado un responsable para el activo");
            destino = "crearactivo.xhtml";
        } else if (nombreGrupo.equals("")) {
            anadirMensajeError("No se ha indicado un grupo para el activo, si no existe uno puede crear uno nuevo");
            destino = "crearactivo.xhtml";
        } else {
            int valor = 1;
            List<Activo> lista = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getNombre().equals(nombre)) {
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
                destino = "confirmaractivo.xhtml";
            } else {
                anadirMensajeError("Ya existe un Activo con ese nombre");
                destino = "crearactivo.xhtml";
            }
        }
        return destino;
    }

    public void guardarActivo() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        gestorActivoService.crearNuevoActivo(codigo, nombre, descripcion, responsable, propietario, ubicacion, null, cantidad, proyectoController.getProyectoActual(), activoEnEdicion.getTipoActivo(), grupoActivoDAO.buscarPorNombre(nombreGrupo));
        anadirMensajeCorrecto("El Activo " + nombre + " ha sido guardado correctamente");
        nombre = "";
        descripcion = "";
        codigo = "";
        responsable = "";
        ubicacion = "";
        cantidad = null;
        nombreGrupo = "";
        context.redirect("activos.xhtml");
    }

    public List<String> getGrupoActivos() {
        grupoActivos = grupoActivoDAO.buscarTodos();
        List<String> grupos = new ArrayList<>();
        for (int i = 0; i < grupoActivos.size(); i++) {
            grupos.add(grupoActivos.get(i).getNombre());
        }

        return grupos;
    }

    public List<Valoracion> getModeloValor() {
        //buscar activos superiores
        boolean esta = false;
        List<Activo> activos = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        List<Valoracion> resultado = new ArrayList<>();
        List<Valoracion> resultadoCorrecto = new ArrayList<>();
        List<Valoracion> valores = new ArrayList<>();
        List<Valoracion> depen = new ArrayList<>();
        List<Valoracion> provisional = new ArrayList<>();
        for (int x = 0; x < activos.size(); x++) {
            Activo activo = activoDAO.buscarPorNombre(activos.get(x).getNombre());
            List<Dependencia> superior = dependenciaDAO.buscarporDependiente(activo);
            if (superior.isEmpty()) {
                valores = valoracionDAO.buscarPorActivo(activo);
                for (int i = 0; i < valores.size(); i++) {
                    resultado.add(valores.get(i));
                }
            } else {
                valores = valoracionDAO.buscarPorActivo(activo);
                if (valores.isEmpty()) {
                    for (int j = 0; j < superior.size(); j++) {
                        if (superior.get(j).getGrado() > 20) {
                            Activo principal = superior.get(j).getActivoPrincipal();
                            depen = valoracionDAO.buscarPorActivo(principal);
                            for (int z = 0; z < depen.size(); z++) {
                                Valoracion valorNuevo = new Valoracion();
                                valorNuevo.setDimension(depen.get(z).getDimension());
                                valorNuevo.setActivo(activo);
                                valorNuevo.setValor(depen.get(z).getValor());
                                provisional.add(valorNuevo);
                            }
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        Valoracion inicial = provisional.get(s);

                        for (int f = s + 1; f < provisional.size(); f++) {
                            if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() < provisional.get(f).getValor()) {
                                inicial.setValor(provisional.get(f).getValor());
                                provisional.remove(f);
                                f--;
                            } else if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() >= provisional.get(f).getValor()) {
                                provisional.remove(f);
                                f--;
                            }
                        }
                        resultado.add(inicial);
                    }
                    provisional.clear();
                } else {
                    for (int r = 0; r < valores.size(); r++) {
                        provisional.add(valores.get(r));
                    }
                    for (int j = 0; j < superior.size(); j++) {
                        if (superior.get(j).getGrado() > 20) {
                            Activo principal = superior.get(j).getActivoPrincipal();
                            depen = valoracionDAO.buscarPorActivo(principal);
                            for (int z = 0; z < depen.size(); z++) {
                                Valoracion valorNuevo = new Valoracion();
                                valorNuevo.setDimension(depen.get(z).getDimension());
                                valorNuevo.setActivo(activo);
                                valorNuevo.setValor(depen.get(z).getValor());
                                provisional.add(valorNuevo);
                            }
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        Valoracion inicial = provisional.get(s);

                        for (int f = s + 1; f < provisional.size(); f++) {
                            if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() < provisional.get(f).getValor()) {
                                inicial.setValor(provisional.get(f).getValor());
                                provisional.remove(f);
                                f--;
                            } else if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() >= provisional.get(f).getValor()) {
                                provisional.remove(f);
                                f--;
                            }
                        }
                        resultado.add(inicial);
                    }
                    provisional.clear();
                }
            }
        }
        provisional.clear();
        for (int i = 0; i < resultado.size(); i++) {

            Activo valorado = resultado.get(i).getActivo();
            List<Dependencia> superiores = dependenciaDAO.buscarporDependiente(valorado);
            if (superiores.isEmpty()) {
                for (int j = 0; j < resultado.size(); j++) {
                    if (resultado.get(j).getActivo().getNombre().equals(valorado.getNombre())) {
                        if (resultadoCorrecto.contains(resultado.get(j))) {
                            //no hacemos nada
                        } else {
                            resultadoCorrecto.add(resultado.get(j));
                        }
                    }
                }
            } else {

                valores = valoracionDAO.buscarPorActivo(valorado);
                if (valores.isEmpty()) {
                    for (int j = 0; j < superiores.size(); j++) {

                        for (int p = 0; p < resultado.size(); p++) {
                            if (superiores.get(j).getActivoPrincipal().getNombre().equals(resultado.get(p).getActivo().getNombre())) {
                                if (superiores.get(j).getGrado() > 20) {
                                    Valoracion valorNuevo = new Valoracion();
                                    valorNuevo.setDimension(resultado.get(p).getDimension());
                                    valorNuevo.setActivo(valorado);
                                    valorNuevo.setValor(resultado.get(p).getValor());
                                    provisional.add(valorNuevo);
                                }
                            }
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        Valoracion inicial = provisional.get(s);

                        for (int f = s + 1; f < provisional.size(); f++) {
                            if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() < provisional.get(f).getValor()) {
                                inicial.setValor(provisional.get(f).getValor());
                                provisional.remove(f);
                                f--;
                            } else if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() >= provisional.get(f).getValor()) {
                                provisional.remove(f);
                                f--;
                            }
                        }
                        for (int g = 0; g < resultadoCorrecto.size(); g++) {

                            if (resultadoCorrecto.get(g).getActivo().getNombre().equals(inicial.getActivo().getNombre()) && resultadoCorrecto.get(g).getDimension().getNombre().equals(inicial.getDimension().getNombre())) {
                                esta = true;
                            }
                        }
                        if (esta == false) {
                            resultadoCorrecto.add(inicial);
                        }
                    }
                    esta = false;
                    provisional.clear();
                } else {
                    for (int k = 0; k < valores.size(); k++) {
                        provisional.add(valores.get(k));
                    }
                    for (int j = 0; j < superiores.size(); j++) {

                        for (int p = 0; p < resultado.size(); p++) {
                            if (superiores.get(j).getActivoPrincipal().getNombre().equals(resultado.get(p).getActivo().getNombre())) {
                                if (superiores.get(j).getGrado() > 20) {
                                    Valoracion valorNuevo = new Valoracion();
                                    valorNuevo.setDimension(resultado.get(p).getDimension());
                                    valorNuevo.setActivo(valorado);
                                    valorNuevo.setValor(resultado.get(p).getValor());
                                    provisional.add(valorNuevo);
                                }
                            }
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        Valoracion inicial = provisional.get(s);
                        for (int f = s + 1; f < provisional.size(); f++) {
                            if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() < provisional.get(f).getValor()) {
                                inicial.setValor(provisional.get(f).getValor());
                                provisional.remove(f);
                                f--;
                            } else if (provisional.get(f).getDimension().getNombre() == inicial.getDimension().getNombre() && inicial.getValor() >= provisional.get(f).getValor()) {
                                provisional.remove(f);
                                f--;
                            }
                        }
                        for (int g = 0; g < resultadoCorrecto.size(); g++) {

                            if (resultadoCorrecto.get(g).getActivo().getNombre().equals(inicial.getActivo().getNombre()) && resultadoCorrecto.get(g).getDimension().getNombre().equals(inicial.getDimension().getNombre())) {
                                esta = true;
                            }
                        }
                        if (esta == false) {
                            resultadoCorrecto.add(inicial);
                        }
                    }
                    esta = false;
                    provisional.clear();
                }
            }
        }
        return resultadoCorrecto;
    }

    public List<Degradacion> getImpactoAcumulado() {

        List<Degradacion> impactoAcumulado = new ArrayList<>();
        List<Activo> activos = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());

        for (int p = 0; p < activos.size(); p++) {
            List<Degradacion> resultado = amenazaController.getImpacto(activos.get(p));
            for (int s = 0; s < resultado.size(); s++) {
                Degradacion seleccionada = resultado.get(s);

                for (int i = s + 1; i < resultado.size(); i++) {
                    if (seleccionada.getDimension().getNombre().equals(resultado.get(i).getDimension().getNombre())) {
                        Degradacion valor = new Degradacion();
                        if (seleccionada.getGrado() < resultado.get(i).getGrado()) {
                            valor.setGrado(resultado.get(i).getGrado());
                            valor.setDimension(resultado.get(i).getDimension());
                            valor.setImpacto(resultado.get(i).getImpacto());
                            resultado.remove(i);
                            i--;
                        } else {
                            valor.setGrado(seleccionada.getGrado());
                            valor.setDimension(seleccionada.getDimension());
                            valor.setImpacto(seleccionada.getImpacto());
                            resultado.remove(i);
                            i--;
                        }
                        impactoAcumulado.add(valor);
                    }
                }
            }
        }
        return impactoAcumulado;
    }

}
