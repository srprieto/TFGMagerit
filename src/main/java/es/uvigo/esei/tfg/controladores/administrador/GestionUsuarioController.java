package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.usuario.TipoUsuario;
import es.uvigo.es.tfg.entidades.usuario.Usuario;
import es.uvigo.esei.tfg.controladores.LoginController;
import es.uvigo.esei.tfg.logica.servicios.GestorUsuariosService;
import es.uvigo.esei.tfg.logica.daos.UsuarioDAO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Saul
 */
@Named(value = "usuarioController")
@SessionScoped
public class GestionUsuarioController implements Serializable {

    //Internacionalizacion
    private Locale locale;
    private ResourceBundle messages;

    // Atributos
    private TipoUsuario tipo1;
    private String login = "";
    private String password = "";
    private String password2 = "";
    private boolean nuevoUsuario = true;
    private int valor = 0; // variable necesaria para control si el que modifica los datos es un 
                          //administrador o un analista y redireccionar a una vista u otra dependiendo del valor

    //Necesario para poder realizar el redireccionamiento a otra vista
    private ExternalContext context1;

    @Inject
    TablaUsuariosController tablaUsuariosController;
    
   //"Acceso" a metodos de otras clases
    @Inject
    GestorUsuariosService gestorUsuariosService;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    @LoginController.LoggedIn
    Usuario usuarioActual;

    public GestionUsuarioController() {

    }

    //Funciones GET y SET
    /**
     * **********************************************************************************************
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public boolean isNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(boolean nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public TipoUsuario getTipo() {
        return tipo1;
    }

    public void setTipo(TipoUsuario tipo1) {
        this.tipo1 = tipo1;
    }

    /**
     * **********************************************************************************************
     */
    /**
     * Mensajes de error y correctos Añade un mensaje de error a la jeraquia de
     * componetes de la página JSF
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

    /*Funcion doUsuario() nos redirecciona a la vista de confirmacion de usuario en caso de que todos los
     valores sean correctos, en caso contrario muestra el mensaje de error correspondiente.*/
    public void doUsuario() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        if (login.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU"));
        } else if (password.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU1"));
        } else if (password2.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU2"));
        } else if (!password.equals(password2)) {
            anadirMensajeError(messages.getString("ERRUSU3"));
        } else if (gestorUsuariosService.existeUsuario(login)) {
            anadirMensajeError(messages.getString("ERRUSU4") + " " + login + " " + messages.getString("ERRUSU5"));
        } else {
            context1.redirect("confirmarusuario.xhtml");
        }
    }

    /*Funcion encriptar() encripta mediante el algoritmo MD5 la clave que le pasamos como parametro*/
    public String encriptar(String password) {

        String algorithm = "MD5";//Algoritmo de encriptación usado para encriptar la clave de Usuario en la base de datos. En nuestro caso usaremos MD%, pero se podría usar cualquier otro cifrado.
        byte[] plainText = password.getBytes();
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.toString());
        }
        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }

    /*Funcion doUsuario() crea un nuevo usuario en la base de datos con los datos introducidos*/
    public void doCrearUsuario() throws IOException {
        
        //Internacionalización
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        String nuevoPass = this.encriptar(password);

        gestorUsuariosService.crearNuevoUsuario(login, nuevoPass, tipo1);
        anadirMensajeCorrecto(messages.getString("CORRUSU") + " " + login + " " + messages.getString("CORRUSU1"));
        login = "";
        password = "";
        tipo1 = null;
        tablaUsuariosController.setSelectedUsuarios(null);
        context1.redirect("usuarios.xhtml");
    }

    /*Funcion atras() hace referencia al boton Atras de la vista de creación de un usuario, ya que necesitamos 
     limpiar los atributos para que no se muestren los datos una vez abandonada la vista*/
    public void atras() throws IOException {

        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        login = "";
        password = "";
        tipo1 = null;

        context1.redirect("crearusuario.xhtml");
    }

    public void modificar() throws IOException {

        valor = 0;

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        context1.redirect("modificardatos.xhtml");
    }
    
    //Estas dos funciones modificar1()  y doModificarUsuario(), sirven para modificar los datos personales de cada usuario
    public void modificar1() throws IOException {

        valor = 1;

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        context1.redirect("modificardatos.xhtml");
    }

    public void doModificarUsuario() throws IOException {
        //Internacionalizacion
        locale = new Locale("default");//añdir es, en...
        messages = ResourceBundle.getBundle("inter.mensajes", locale);

        context1 = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);//Necesario para mostrar los mensajes del sistema una vez realizada la redirección a otra vista

        //Atributos
        Usuario seleccionado = usuarioActual;
        Long id = seleccionado.getId();

        if (seleccionado.getLogin().equals("")) {
            anadirMensajeError(messages.getString("ERRTABUSU2"));
        } else if (password.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU1"));
        } else if (password2.equals("")) {
            anadirMensajeError(messages.getString("ERRUSU2"));
        } else if (!password.equals(password2)) {
            anadirMensajeError(messages.getString("ERRUSU3"));
        } else if (gestorUsuariosService.existeUsuario(seleccionado.getLogin()) == true && gestorUsuariosService.existeId(seleccionado.getLogin()) != id) {
            anadirMensajeError(messages.getString("ERRTABUSU3"));
        } else {
            String nuevoPass = this.encriptar(password);
            seleccionado.setPassword(nuevoPass);
            usuarioDAO.actualizar(seleccionado);
            anadirMensajeCorrecto(messages.getString("CORRTABUSU"));
            if (valor == 0) {
                context1.redirect("indexeditor.xhtml");
            } else {
                context1.redirect("indexadministrador.xhtml");
            }
        }
    }
}
