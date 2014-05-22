package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.CriterioValoracion;
import es.uvigo.es.tfg.entidades.marco.Dimension;
import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.es.tfg.entidades.marco.TipoActivo;
import es.uvigo.es.tfg.entidades.marco.TipoAmenaza;
import es.uvigo.es.tfg.entidades.proyecto.Activo;
import es.uvigo.es.tfg.entidades.proyecto.Amenaza;
import es.uvigo.es.tfg.entidades.proyecto.Degradacion;
import es.uvigo.es.tfg.entidades.proyecto.Dependencia;
import es.uvigo.es.tfg.entidades.proyecto.Impacto;
import es.uvigo.es.tfg.entidades.proyecto.Proyecto;
import es.uvigo.es.tfg.entidades.proyecto.Valoracion;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.modelos.UsuarioModel;
import es.uvigo.esei.tfg.logica.daos.ActivoDAO;
import es.uvigo.esei.tfg.logica.daos.AmenazaDAO;
import es.uvigo.esei.tfg.logica.daos.CriterioValoracionDAO;
import es.uvigo.esei.tfg.logica.daos.DegradacionDAO;
import es.uvigo.esei.tfg.logica.daos.DependenciaDAO;
import es.uvigo.esei.tfg.logica.daos.DimensionDAO;
import es.uvigo.esei.tfg.logica.daos.ImpactoDAO;
import es.uvigo.esei.tfg.logica.daos.MarcoTrabajoDAO;
import es.uvigo.esei.tfg.logica.daos.ProyectoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoActivoDAO;
import es.uvigo.esei.tfg.logica.daos.TipoAmenazaDAO;
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import es.uvigo.esei.tfg.logica.daos.ValoracionDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Saul
 */
@Named(value = "tablaUsuariosController")
@SessionScoped
public class TablaUsuariosController implements Serializable {

    //Internacionalizacion
    private Locale locale; //Nos sirve para indicar el idioma (es, en, fr...)
    private ResourceBundle messages;//Recupera los mensajes del archivo properties

    //Atributos
    private Usuario usuario;
    private List<Usuario> usuarios;
    private Usuario[] selectedUsuarios; //Lista de usuarios seleccionados por el usuario en la tabla
    private UsuarioModel usuarioModel;//modelo de usuario ,necesario para cargar los datos en la tabla

    private ExternalContext context1;

    @Inject
    MarcoTrabajoDAO marcoDAO;

    @Inject
    TipoAmenazaDAO tipoAmenazaDAO;

    @Inject
    TipoActivoDAO tipoActivoDAO;

    @Inject
    DimensionDAO dimensionDAO;

    @Inject
    ProyectoDAO proyectoDAO;

    @Inject
    ImpactoDAO impactoDAO;

    @Inject
    DegradacionDAO degradacionDAO;

    @Inject
    ActivoDAO activoDAO;

    @Inject
    AmenazaDAO amenazaDAO;

    @Inject
    DependenciaDAO dependenciaDAO;

    @Inject
    ValoracionDAO valoracionDAO;

    @Inject
    CriterioValoracionDAO criterioValoracionDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    GestorUsuariosService gestorUsuariosService;

    public TablaUsuariosController() {

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
    /**
     * ********************************************************************************************
     */
    public Usuario[] getSelectedUsuarios() {
        return selectedUsuarios;
    }

    public void setSelectedUsuarios(Usuario[] selectedUsuarios) {
        this.selectedUsuarios = selectedUsuarios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        usuarios = usuarioDAO.buscarTodos();
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /*Datos que se mostraran en la tabla, primero recuperamos todos los datos y 
     posteriormente los cargamos con el modelo*/
    public UsuarioModel getUsuarioModel() {
        usuarios = usuarioDAO.buscarTodos();
        usuarioModel = new UsuarioModel(usuarios);
        return usuarioModel;
    }

    /**
     * *********************************************************************************************
     */

    /*Funcion que valida si se selecciono un usuario, en caso de no seleccionar ninguno o mas 
     de uno se mostrara un mensaje de error, en caso contrario nos muestra el formulario de edición*/
    public void update() {

        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        //Atributos
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;

        if (tamano == 0) {
            this.setSelectedUsuarios(null);
            anadirMensajeError(messages.getString("ERRTABUSU"));
        } else if (tamano != 1) {
            this.setSelectedUsuarios(null);
            anadirMensajeError(messages.getString("ERRTABUSU1"));
        } else {
            RequestContext.getCurrentInstance().execute("multiEditDialog.show();");
        }
    }

    public void updateUsuario() {

        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        //Atributos
        Usuario[] seleccionados = this.getSelectedUsuarios();
        int tamano = seleccionados.length;
        Usuario seleccionado = seleccionados[0];
        Long id = seleccionado.getId();
        this.setSelectedUsuarios(null);

        if (seleccionado.getLogin().equals("")) {
            anadirMensajeError(messages.getString("ERRTABUSU2"));
        } else if (gestorUsuariosService.existeUsuario(seleccionado.getLogin()) == true && gestorUsuariosService.existeId(seleccionado.getLogin()) != id) {
            anadirMensajeError(messages.getString("ERRTABUSU3"));
        } else {
            usuarioDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABUSU"));
            RequestContext.getCurrentInstance().update("form");//Necesario para que se actualice la tabla de la vista de usuarios una vez modificados los datos de un usuario.
        }
    }

    public void eliminar() {

        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        //Atributos
        Usuario[] seleccionados = this.getSelectedUsuarios();

        int tamano = seleccionados.length;

        if (tamano == 0) {
            anadirMensajeError(messages.getString("ERRTABUSU4"));
        } else {
            RequestContext.getCurrentInstance().execute("multiUsuarioDialog.show();");
        }
    }

    public void eliminarUsuarios() {

        //internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        //Atributos
        Usuario[] lista = this.getSelectedUsuarios();
        int tamano = lista.length;
        Usuario user;

        List<Activo> activoEliminar = new ArrayList<>();
        List<Impacto> impactoEliminar = new ArrayList<>();
        List<Dependencia> dependenciasEliminar = new ArrayList<>();
        List<Valoracion> valoracionesEliminar = new ArrayList<>();
        List<Degradacion> degradacionEliminar = new ArrayList<>();
        List<Usuario> editores = new ArrayList<>();
        List<Proyecto> proyectosUsuario = new ArrayList<>();

        for (int i = 0; i < tamano; i++) {
            proyectosUsuario = proyectoDAO.buscarPorCreador(lista[i]);
            for (int t = 0; t < proyectosUsuario.size(); t++) {
                Proyecto pro = proyectosUsuario.get(t);
                activoEliminar = activoDAO.buscarActivosProyecto(pro);
                for (int j = 0; j < activoEliminar.size(); j++) {
                    impactoEliminar = impactoDAO.buscarAmenazasActivo(activoEliminar.get(j));
                    for (int s = 0; s < impactoEliminar.size(); s++) {
                        degradacionEliminar = degradacionDAO.buscarPorImpacto(impactoEliminar.get(s));
                        for (int z = 0; z < degradacionEliminar.size(); z++) {
                            degradacionDAO.eliminar(degradacionEliminar.get(z));
                        }
                        Amenaza amenazaEliminar = impactoEliminar.get(s).getAmenaza();
                        impactoDAO.eliminar(impactoEliminar.get(s));
                        amenazaDAO.eliminar(amenazaEliminar);
                    }
                    dependenciasEliminar = dependenciaDAO.buscarPorPrincipal(activoEliminar.get(j));
                    for (int s = 0; s < dependenciasEliminar.size(); s++) {
                        dependenciaDAO.eliminar(dependenciasEliminar.get(s));
                    }
                    valoracionesEliminar = valoracionDAO.buscarPorActivo(activoEliminar.get(j));
                    for (int s = 0; s < valoracionesEliminar.size(); s++) {
                        valoracionDAO.eliminar(valoracionesEliminar.get(s));
                    }
                    activoDAO.eliminar(activoEliminar.get(j));
                }

                pro.setEditores(editores);
                proyectoDAO.actualizar(pro);
                proyectoDAO.eliminar(pro);
            }
            user = lista[i];
            usuarioDAO.eliminar(user);
        }

        if (tamano == 1) {
            anadirMensajeCorrecto(messages.getString("CORRTABUSU1"));
        } else {
            anadirMensajeCorrecto(messages.getString("CORRTABUSU2"));
        }
    }

    public void cancelar() throws IOException {
        this.setSelectedUsuarios(null);
        RequestContext.getCurrentInstance().execute("multiEditDialog.hide();");
    }

    public void atras() throws IOException {
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedUsuarios(null);
        context1.redirect("usuarios.xhtml");
    }

    public void atras1() throws IOException {
        context1 = FacesContext.getCurrentInstance().getExternalContext();
        this.setSelectedUsuarios(null);
        context1.redirect("indexadministrador.xhtml");
    }
}
