package com.zeuscodensa.techcupfutbol.controller.dto;

import java.util.List;

public class TournamentRequestDTO {
    private String tournamentName;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;

    // Advanced Configuration (RF-005)
    private String rules;
    private String fechaCierreInscripciones;
    private String fechaInicioFaseGrupos;
    private List<String> horariosPartidos;
    private List<String> canchas;
    private String sanctions;

    public TournamentRequestDTO() {}

    public TournamentRequestDTO(String tournamentName, String fechaInicio, String fechaFin, Integer numeroEquipos, Double costoInscripcion) {
        this.tournamentName = tournamentName;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroEquipos = numeroEquipos;
        this.costoInscripcion = costoInscripcion;
    }

    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public Integer getNumeroEquipos() { return numeroEquipos; }
    public void setNumeroEquipos(Integer numeroEquipos) { this.numeroEquipos = numeroEquipos; }
    public Double getCostoInscripcion() { return costoInscripcion; }
    public void setCostoInscripcion(Double costoInscripcion) { this.costoInscripcion = costoInscripcion; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }
    public String getFechaCierreInscripciones() { return fechaCierreInscripciones; }
    public void setFechaCierreInscripciones(String fechaCierreInscripciones) { this.fechaCierreInscripciones = fechaCierreInscripciones; }
    public String getFechaInicioFaseGrupos() { return fechaInicioFaseGrupos; }
    public void setFechaInicioFaseGrupos(String fechaInicioFaseGrupos) { this.fechaInicioFaseGrupos = fechaInicioFaseGrupos; }
    public List<String> getHorariosPartidos() { return horariosPartidos; }
    public void setHorariosPartidos(List<String> horariosPartidos) { this.horariosPartidos = horariosPartidos; }
    public List<String> getCanchas() { return canchas; }
    public void setCanchas(List<String> canchas) { this.canchas = canchas; }
    public String getSanctions() { return sanctions; }
    public void setSanctions(String sanctions) { this.sanctions = sanctions; }
}
