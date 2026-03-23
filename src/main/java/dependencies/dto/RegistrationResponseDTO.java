package dependencies.dto;

import core.model.Registration;

public class RegistrationResponseDTO {
    private String id;
    private String nombreEquipo;
    private String tournamentName;
    private String status;
    private String comprobantePagoUrl;
    private String fechaInscripcion;

    public RegistrationResponseDTO() {}

    public RegistrationResponseDTO(Registration registration) {
        this.id = registration.getId();
        this.nombreEquipo = registration.getNombreEquipo();
        this.tournamentName = registration.getTournamentName();
        this.status = registration.getStatus();
        this.comprobantePagoUrl = registration.getComprobantePagoUrl();
        this.fechaInscripcion = registration.getFechaInscripcion();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
    public String getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(String fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}
