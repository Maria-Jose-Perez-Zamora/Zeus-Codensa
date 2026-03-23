package core.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Registration {
    private String id;
    private String nombreEquipo;
    private String tournamentName;
    private String status;
    private String comprobantePagoUrl;
    private String fechaInscripcion;

    public Registration() {}

    public Registration(String nombreEquipo, String tournamentName, String comprobantePagoUrl) {
        this.id = UUID.randomUUID().toString();
        this.nombreEquipo = nombreEquipo;
        this.tournamentName = tournamentName;
        this.status = "PENDIENTE";
        this.comprobantePagoUrl = comprobantePagoUrl;
        this.fechaInscripcion = LocalDateTime.now().toString();
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
