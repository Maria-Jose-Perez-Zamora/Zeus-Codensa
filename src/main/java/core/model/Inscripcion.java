package core.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Inscripcion {
    private String id;
    private String nombreEquipo;
    private String nombreTorneo;
    private String estado;
    private String comprobantePagoUrl;
    private String fechaInscripcion;

    public Inscripcion() {}

    public Inscripcion(String nombreEquipo, String nombreTorneo, String comprobantePagoUrl) {
        this.id = UUID.randomUUID().toString();
        this.nombreEquipo = nombreEquipo;
        this.nombreTorneo = nombreTorneo;
        this.estado = "PENDIENTE";
        this.comprobantePagoUrl = comprobantePagoUrl;
        this.fechaInscripcion = LocalDateTime.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
    public String getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(String fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}
