package core.model;

import java.util.UUID;

public class Invitation {
    private String id;
    private String correoCapitan;
    private String correoJugador;
    private String nombreEquipo;
    private String status;

    public Invitation() {}

    public Invitation(String correoCapitan, String correoJugador, String nombreEquipo) {
        this.id = UUID.randomUUID().toString();
        this.correoCapitan = correoCapitan;
        this.correoJugador = correoJugador;
        this.nombreEquipo = nombreEquipo;
        this.status = "ENVIADA";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCorreoCapitan() { return correoCapitan; }
    public void setCorreoCapitan(String correoCapitan) { this.correoCapitan = correoCapitan; }
    public String getCorreoJugador() { return correoJugador; }
    public void setCorreoJugador(String correoJugador) { this.correoJugador = correoJugador; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
