package dependencias.dto;

import core.model.Torneo;
import java.util.List;

public class TorneoResponseDTO {
    private String id;
    private String nombreTorneo;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;
    private String estado;

    // Advanced Configuration (RF-005)
    private String reglamento;
    private String fechaCierreInscripciones;
    private String fechaInicioFaseGrupos;
    private List<String> horariosPartidos;
    private List<String> canchas;
    private String sanciones;

    public TorneoResponseDTO() {}
    
    public TorneoResponseDTO(Torneo torneo) {
        this.id = torneo.getId();
        this.nombreTorneo = torneo.getNombreTorneo();
        this.fechaInicio = torneo.getFechaInicio();
        this.fechaFin = torneo.getFechaFin();
        this.numeroEquipos = torneo.getNumeroEquipos();
        this.costoInscripcion = torneo.getCostoInscripcion();
        this.estado = torneo.getEstado();
        this.reglamento = torneo.getReglamento();
        this.fechaCierreInscripciones = torneo.getFechaCierreInscripciones();
        this.fechaInicioFaseGrupos = torneo.getFechaInicioFaseGrupos();
        this.horariosPartidos = torneo.getHorariosPartidos();
        this.canchas = torneo.getCanchas();
        this.sanciones = torneo.getSanciones();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
