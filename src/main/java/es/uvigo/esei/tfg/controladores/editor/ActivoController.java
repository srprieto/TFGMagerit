/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores.editor;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.GrupoActivos;
import es.uvigo.es.tfg.entidades.proyecto.Riesgo;
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
import java.util.Locale;
import java.util.ResourceBundle;
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
    
    
    //Internacionalizacion
    private Locale locale;
    private  ResourceBundle messages;
    
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
    private String nomtip;
    private String destip;
    private String abrtip;
    private int valor;
    
    private ExternalContext context1; 

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
    
    /*Funciones GET y SET*/
    
    /************************************************************************************************/
    
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }


    
    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNomtip() {
        return nomtip;
    }

    public void setNomtip(String nomtip) {
        this.nomtip = nomtip;
    }

    public String getDestip() {
        return destip;
    }

    public void setDestip(String destip) {
        this.destip = destip;
    }

    public String getAbrtip() {
        return abrtip;
    }

    public void setAbrtip(String abrtip) {
        this.abrtip = abrtip;
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
    
    /************************************************************************************************/

    public void atras1() throws IOException{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (valor==0){
            context.redirect("crearactivo.xhtml");
        }else{
            context.redirect("activos.xhtml");
        }
    }
    
     public void detalles1() throws IOException{
        valor=1;
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        nomtip = activoEnEdicion.getTipoActivo().getNombre();
        abrtip = activoEnEdicion.getTipoActivo().getAbreviatura();
        destip = activoEnEdicion.getTipoActivo().getDescripcion();
        context1.redirect("detallestipo.xhtml");
    }
   
    public void detalles() throws IOException{
        valor=0;
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        nomtip = activoEnEdicion.getTipoActivo().getNombre();
        abrtip = activoEnEdicion.getTipoActivo().getAbreviatura();
        destip = activoEnEdicion.getTipoActivo().getDescripcion();
        context1.redirect("detallestipo.xhtml");
    }
    
    public void doGuargar() throws IOException {
        
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        
        if (codigo.equals("")) {
            anadirMensajeError(messages.getString("ERRACT"));
        } else if (nombre.equals("")) {
            anadirMensajeError(messages.getString("ERRACT1"));
        } else if (descripcion.equals("")) {
            anadirMensajeError(messages.getString("ERRACT2"));
        } else if (responsable.equals("")) {
            anadirMensajeError(messages.getString("ERRACT3"));
        } else if (nombreGrupo.equals("")) {
            anadirMensajeError(messages.getString("ERRACT4"));
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
                context1.redirect("confirmaractivo.xhtml");
            } else {
                anadirMensajeError(messages.getString("ERRACT5"));
            }
        }

    }

    public void atras() throws IOException {
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        nombre = "";
        descripcion = "";
        codigo = "";
        responsable = "";
        ubicacion = "";
        cantidad = null;
        nombreGrupo = "";
        context1.redirect("crearactivo.xhtml");
    }

    public void guardarActivo() throws IOException {
        
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes",locale);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        
        gestorActivoService.crearNuevoActivo(codigo, nombre, descripcion, responsable, propietario, ubicacion, null, cantidad, proyectoController.getProyectoActual(), activoEnEdicion.getTipoActivo(), grupoActivoDAO.buscarPorNombre(nombreGrupo));
        anadirMensajeCorrecto(messages.getString("CORRACT")+" "+ nombre +" "+messages.getString("CORRACT1"));
        nombre = "";
        descripcion = "";
        codigo = "";
        responsable = "";
        ubicacion = "";
        cantidad = null;
        nombreGrupo = "";
        context1.redirect("activos.xhtml");
    }
    
    public void doGrupo() throws IOException{
          context1 = FacesContext.getCurrentInstance().getExternalContext();
          context1.redirect("nuevogrupo.xhtml");
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
        
        boolean esta = false;

        Activo activo;
        Activo principal;
        Activo valorado;
        Valoracion inicial;

        List<Activo> activos = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        List<Valoracion> resultado = new ArrayList<>();
        List<Dependencia> superior = new ArrayList<>();
        List<Dependencia> superiores = new ArrayList<>();
        List<Valoracion> resultadoCorrecto = new ArrayList<>();
        List<Valoracion> valores = new ArrayList<>();
        List<Valoracion> depen = new ArrayList<>();
        List<Valoracion> provisional = new ArrayList<>();

        for (int x = 0; x < activos.size(); x++) {
            activo = activoDAO.buscarPorNombre(activos.get(x).getNombre());
            superior = dependenciaDAO.buscarporDependiente(activo);
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
                            principal = superior.get(j).getActivoPrincipal();
                            depen = valoracionDAO.buscarPorActivo(principal);
                            for (int z = 0; z < depen.size(); z++) {
                                Valoracion valorNuevo = new Valoracion();
                                valorNuevo.setDimension(depen.get(z).getDimension());
                                valorNuevo.setActivo(activo);
                                valorNuevo.setValor(depen.get(z).getValor());
                                provisional.add(valorNuevo);
                            }
                            depen.clear();
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        inicial = provisional.get(s);

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
                            principal = superior.get(j).getActivoPrincipal();
                            depen = valoracionDAO.buscarPorActivo(principal);
                            for (int z = 0; z < depen.size(); z++) {
                                Valoracion valorNuevo = new Valoracion();
                                valorNuevo.setDimension(depen.get(z).getDimension());
                                valorNuevo.setActivo(activo);
                                valorNuevo.setValor(depen.get(z).getValor());
                                provisional.add(valorNuevo);
                            }
                            depen.clear();
                        }
                    }
                    for (int s = 0; s < provisional.size(); s++) {
                        inicial = provisional.get(s);

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
                valores.clear();
            }
            superior.clear();
        }
        provisional.clear();
        for (int i = 0; i < resultado.size(); i++) {
            valorado = resultado.get(i).getActivo();
            superiores = dependenciaDAO.buscarporDependiente(valorado);
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
                        inicial = provisional.get(s);
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
                        inicial = provisional.get(s);
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
                valores.clear();
            }
            superiores.clear();
        }
        return resultadoCorrecto;
    }

    public List<Degradacion> getImpactoAcumulado() {

        boolean sinDimension = true;
        Degradacion seleccionada;
        Degradacion valor;

        List<Degradacion> impactoAcumulado = new ArrayList<>();
        List<Activo> activos = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        List<Degradacion> resultado = new ArrayList<>();

        for (int p = 0; p < activos.size(); p++) {
            resultado = amenazaController.getImpacto(activos.get(p));
            for (int s = 0; s < resultado.size(); s++) {
                seleccionada = resultado.get(s);
                for (int i = s + 1; i < resultado.size(); i++) {
                    if (seleccionada.getDimension().getNombre().equals(resultado.get(i).getDimension().getNombre())) {
                        sinDimension = false;
                        valor = new Degradacion();
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
                if (sinDimension == true) {
                    valor = new Degradacion();
                    valor.setGrado(seleccionada.getGrado());
                    valor.setDimension(seleccionada.getDimension());
                    valor.setImpacto(seleccionada.getImpacto());
                    impactoAcumulado.add(valor);
                }
            }
        }
        return impactoAcumulado;
    }

    public List<Riesgo> getRiesgoAcumulado() {

        boolean sinDimension = true;
        Riesgo seleccionada;
        Riesgo valor;

        List<Riesgo> riesgoAcumulado = new ArrayList<>();
        List<Activo> activos = activoDAO.buscarActivosProyecto(proyectoController.getProyectoActual());
        List<Riesgo> resultado = new ArrayList<>();

        for (int p = 0; p < activos.size(); p++) {
            resultado = amenazaController.getRiesgo(activos.get(p));
            for (int s = 0; s < resultado.size(); s++) {
                seleccionada = resultado.get(s);
                for (int i = s + 1; i < resultado.size(); i++) {
                    if (seleccionada.getDimension().getNombre().equals(resultado.get(i).getDimension().getNombre())) {
                        sinDimension = false;
                        valor = new Riesgo();
                        if (seleccionada.getValor() < resultado.get(i).getValor()) {
                            valor.setValor(resultado.get(i).getValor());
                            valor.setDimension(resultado.get(i).getDimension());
                            valor.setImpacto(resultado.get(i).getImpacto());
                            valor.setGravedad(resultado.get(i).getGravedad());
                            resultado.remove(i);
                            i--;
                        } else {
                            valor.setValor(seleccionada.getValor());
                            valor.setDimension(seleccionada.getDimension());
                            valor.setImpacto(seleccionada.getImpacto());
                            valor.setGravedad(seleccionada.getGravedad());
                            resultado.remove(i);
                            i--;
                        }
                        riesgoAcumulado.add(valor);

                    }

                }
                if (sinDimension == true) {
                    valor = new Riesgo();
                    valor.setValor(seleccionada.getValor());
                    valor.setDimension(seleccionada.getDimension());
                    valor.setImpacto(seleccionada.getImpacto());
                    valor.setGravedad(seleccionada.getGravedad());
                    riesgoAcumulado.add(valor);

                }
            }
        }
        return riesgoAcumulado;
    }

}
