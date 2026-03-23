package dependencias.dto;

import java.util.List;

public class TorneoRequestDTO {
    private String nombreTorneo;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;

    // Advanced Configuration (RF-005)
    private String reglamento;
    private String fechaCierreInscripciones;
    private String fechaInicioFaseGrupos;
    private List<String> horariosPartidos;
    private List<String> canchas;
    private String sanciones;

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

    public String getReglamento() { return reglamento; }
    public void setReglamento(String reglamento) { this.reglamento = reglamento; }
    public String getFechaCierreInscripciones() { return fechaCierreInscripciones; }
    public void setFechaCierreInscripciones(String fechaCierreInscripciones) { this.fechaCierreInscripciones = fechaCierreInscripciones; }
    public String getFechaInicioFaseGrupos() { return fechaInicioFaseGrupos; }
    public void setFechaInicioFaseGrupos(String fechaInicioFaseGrupos) { this.fechaInicioFaseGrupos = fechaInicioFaseGrupos; }
    public List<String> getHorariosPartidos() { return horariosPartidos; }
    public void setHorariosPartidos(List<String> horariosPartidos) { this.horariosPartidos = horariosPartidos; }
    public List<String> getCanchas() { return canchas; }
    public void setCanchas(List<String> canchas) { this.canchas = canchas; }
    public String getSanciones() { return sanciones; }
    public void setSanciones(String sanciones) { this.sanciones = sanciones; }
}
