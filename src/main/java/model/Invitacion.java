package model;

import java.util.UUID;

public class Invitacion {
    private String id;
    private String correoCapitan;
    private String correoJugador;
    private String nombreEquipo;
    private String estado;

    public Invitacion() {}

    public Invitacion(String correoCapitan, String correoJugador, String nombreEquipo) {
        this.id = UUID.randomUUID().toString();
        this.correoCapitan = correoCapitan;
        this.correoJugador = correoJugador;
        this.nombreEquipo = nombreEquipo;
        this.estado = "ENVIADA";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCorreoCapitan() { return correoCapitan; }
    public void setCorreoCapitan(String correoCapitan) { this.correoCapitan = correoCapitan; }
    public String getCorreoJugador() { return correoJugador; }
    public void setCorreoJugador(String correoJugador) { this.correoJugador = correoJugador; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
