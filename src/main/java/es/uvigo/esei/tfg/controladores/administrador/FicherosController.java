package es.uvigo.esei.tfg.controladores.administrador;

import es.uvigo.es.tfg.entidades.marco.MarcoTrabajo;
import es.uvigo.esei.tfg.logica.servicios.CargadorCatalogoService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import org.primefaces.model.UploadedFile;

@Named(value = "ficherosController")
@SessionScoped
public class FicherosController implements Serializable {

    private UploadedFile file;

    @Inject
    TablaMarcosController tablaMarcosController;

    @Inject
    CargadorCatalogoService cargadorCatalogoService;

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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (file != null) {
            File folder = new File("C:/Users/Saul/Downloads/uploads");
            InputStream stream = file.getInputstream();
            String prefix = FilenameUtils.getBaseName(file.getFileName());
            String suffix = FilenameUtils.getExtension(file.getFileName());
            File file1 = File.createTempFile(prefix + "-", "." + suffix, folder);
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(file1);

            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
                IOUtils.closeQuietly(input);
            }
            MarcoTrabajo[] seleccionados = tablaMarcosController.getSelectedMarcos();
            MarcoTrabajo seleccionado = seleccionados[0];
            cargadorCatalogoService.cargarRecurso(file1.getAbsolutePath(), seleccionado);
            anadirMensajeCorrecto("El archivo xml ha sido cargado correctamente");
            context.redirect("marcos.xhtml");
        }
    }

    private MarcoTrabajo[] getSelectedMarcos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
