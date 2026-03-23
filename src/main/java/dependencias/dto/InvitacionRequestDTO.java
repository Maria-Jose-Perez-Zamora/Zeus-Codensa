package dependencias.dto;

public class InvitacionRequestDTO {
    private String correoCapitan;
    private String correoJugador;
    private String nombreEquipo;

    public InvitacionRequestDTO() {}

    public InvitacionRequestDTO(String correoCapitan, String correoJugador, String nombreEquipo) {
        this.correoCapitan = correoCapitan;
        this.correoJugador = correoJugador;
        this.nombreEquipo = nombreEquipo;
    }

    public String getCorreoCapitan() { return correoCapitan; }
    public void setCorreoCapitan(String correoCapitan) { this.correoCapitan = correoCapitan; }
    public String getCorreoJugador() { return correoJugador; }
    public void setCorreoJugador(String correoJugador) { this.correoJugador = correoJugador; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
}
