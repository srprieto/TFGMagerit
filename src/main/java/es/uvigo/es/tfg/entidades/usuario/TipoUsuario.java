package es.uvigo.es.tfg.entidades.usuario;


/**
 *
 * @author Saul
 */
public enum TipoUsuario {
    ADMINISTRADOR("ADMINISTRADOR"),
    ANALISTA("ANALISTA");
    
    private final String label;

  private TipoUsuario (String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
    
}
