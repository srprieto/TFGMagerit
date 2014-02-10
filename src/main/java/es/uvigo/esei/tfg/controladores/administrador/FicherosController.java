/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uvigo.esei.tfg.controladores.administrador;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext; 
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
/**
 *
 * @author Saul
 */

@Named(value = "ficherosController")
@SessionScoped
public class FicherosController implements Serializable{
    
private UploadedFile file;  
  
    public UploadedFile getFile() {  
        return file;  
    }  
  
    public void setFile(UploadedFile file) {  
        this.file = file;  
    }  
  
    public void upload() {  
        if(file != null) {  
            FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");  
            FacesContext.getCurrentInstance().addMessage(null, msg);  
        }  
    }  
}  
          
