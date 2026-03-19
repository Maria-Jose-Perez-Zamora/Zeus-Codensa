package dto;

public class TorneoRequestDTO {
    private String nombreTorneo;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;

    public TorneoRequestDTO() {}

    public TorneoRequestDTO(String nombreTorneo, String fechaInicio, String fechaFin, Integer numeroEquipos, Double costoInscripcion) {
        this.nombreTorneo = nombreTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroEquipos = numeroEquipos;
        this.costoInscripcion = costoInscripcion;
    }

    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public Integer getNumeroEquipos() { return numeroEquipos; }
    public void setNumeroEquipos(Integer numeroEquipos) { this.numeroEquipos = numeroEquipos; }
    public Double getCostoInscripcion() { return costoInscripcion; }
    public void setCostoInscripcion(Double costoInscripcion) { this.costoInscripcion = costoInscripcion; }
}
