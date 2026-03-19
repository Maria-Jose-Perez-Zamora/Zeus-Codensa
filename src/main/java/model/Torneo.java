package model;

public class Torneo {
    private String nombreTorneo;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;
    private String estado;

    public Torneo() {}

    public Torneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
        this.estado = "ABIERTO";
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
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
