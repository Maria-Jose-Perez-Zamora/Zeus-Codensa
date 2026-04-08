package com.zeuscodensa.techcupfutbol.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Registration {
    private String id;
    private String teamName;
    private String tournamentName;
    private String status;
    private String comprobantePagoUrl;
    private String fechaInscripcion;

    public Registration() {}

    public Registration(String teamName, String tournamentName, String comprobantePagoUrl) {
        this.id = UUID.randomUUID().toString();
        this.teamName = teamName;
        this.tournamentName = tournamentName;
        this.status = "PENDING";
        this.comprobantePagoUrl = comprobantePagoUrl;
        this.fechaInscripcion = LocalDateTime.now().toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
    public String getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(String fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}
