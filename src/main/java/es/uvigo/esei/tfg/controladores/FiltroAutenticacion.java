/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.tfg.controladores;

/**
 * Filtro de URL para evitar el acceso a las "zonas privadas" de otros tipos de
 * usuarios por parte de usuarios no autenticados o usuarios autenticado de otro
 * tipo
 *
 * @author saul
 */
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "FiltroAutenticacion",
        urlPatterns = {"/faces/administrador/*",
            "/faces/editor/*",})
public class FiltroAutenticacion implements Filter {

    @Inject
    LoginController loginController;

    public FiltroAutenticacion() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean aceptarPeticion = true;

        if (!loginController.isAutenticado()) {
            // No se trata de un usuario autenticado
            aceptarPeticion = false;
        } else {
            // Comprobar que el tipo de la zona web a la que se quiere acceder 
            // se corresponde con el tipo de usuario autenticado en la aplicación

            String tipoUsuarioAutenticado = loginController.getUsuarioActual().getTipo().getLabel().toString().toLowerCase();

            // Recuperar el path de la URL y extraer el tipo
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String tipoUsuarioURL = extraerTipoUsuarioURL(httpServletRequest.getPathInfo());
            if (!tipoUsuarioAutenticado.equalsIgnoreCase(tipoUsuarioURL)) {
                aceptarPeticion = false;
            }
        }
        if (aceptarPeticion) {
            // Dejar continuar la petición
            chain.doFilter(request, response);
        } else {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            // Redirir a la página de login del tipo de usuario que corresponda
            httpServletRequest.getSession().invalidate();
            String ctxPath = httpServletRequest.getContextPath();
            ((HttpServletResponse) response).sendRedirect(ctxPath + "/index.xhtml");
        }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig
    ) {
    }

    private String extraerTipoUsuarioURL(String path) {
        String tipoURL = "";

        Pattern patron = Pattern.compile(".*/(.+)/.*");
        Matcher matcher = patron.matcher(path);

        if (matcher.find()) {
            tipoURL = matcher.group(1);
        }
        return tipoURL;
    }
}
