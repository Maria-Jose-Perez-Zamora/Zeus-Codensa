package dependencies.dto;

import core.model.Invitation;

public class InvitationResponseDTO {
    private String id;
    private String correoJugador;
    private String nombreEquipo;
    private String status;
    private String mensaje;

    public InvitationResponseDTO() {}

    public InvitationResponseDTO(Invitation inv) {
        this.id = inv.getId();
        this.correoJugador = inv.getCorreoJugador();
        this.nombreEquipo = inv.getNombreEquipo();
        this.status = inv.getStatus();
        this.mensaje = "Invitación enviada exitosamente al player " + inv.getCorreoJugador();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCorreoJugador() { return correoJugador; }
    public void setCorreoJugador(String correoJugador) { this.correoJugador = correoJugador; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
