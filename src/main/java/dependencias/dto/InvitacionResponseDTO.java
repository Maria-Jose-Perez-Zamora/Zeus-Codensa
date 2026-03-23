package dependencias.dto;

import core.model.Invitacion;

public class InvitacionResponseDTO {
    private String id;
    private String correoJugador;
    private String nombreEquipo;
    private String estado;
    private String mensaje;

    public InvitacionResponseDTO() {}

    public InvitacionResponseDTO(Invitacion inv) {
        this.id = inv.getId();
        this.correoJugador = inv.getCorreoJugador();
        this.nombreEquipo = inv.getNombreEquipo();
        this.estado = inv.getEstado();
        this.mensaje = "Invitación enviada exitosamente al jugador " + inv.getCorreoJugador();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCorreoJugador() { return correoJugador; }
    public void setCorreoJugador(String correoJugador) { this.correoJugador = correoJugador; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
