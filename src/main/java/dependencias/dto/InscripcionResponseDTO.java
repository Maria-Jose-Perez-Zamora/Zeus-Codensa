package dependencias.dto;

import core.model.Inscripcion;

public class InscripcionResponseDTO {
    private String id;
    private String nombreEquipo;
    private String nombreTorneo;
    private String estado;
    private String comprobantePagoUrl;
    private String fechaInscripcion;

    public InscripcionResponseDTO() {}

    public InscripcionResponseDTO(Inscripcion inscripcion) {
        this.id = inscripcion.getId();
        this.nombreEquipo = inscripcion.getNombreEquipo();
        this.nombreTorneo = inscripcion.getNombreTorneo();
        this.estado = inscripcion.getEstado();
        this.comprobantePagoUrl = inscripcion.getComprobantePagoUrl();
        this.fechaInscripcion = inscripcion.getFechaInscripcion();
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
