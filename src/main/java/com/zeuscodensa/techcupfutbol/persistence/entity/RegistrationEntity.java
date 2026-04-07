package com.zeuscodensa.techcupfutbol.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String tournamentName;

    @Column(nullable = false)
    private String status;

    private String comprobantePagoUrl;

    private String fechaInscripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComprobantePagoUrl() {
        return comprobantePagoUrl;
    }

    public void setComprobantePagoUrl(String comprobantePagoUrl) {
        this.comprobantePagoUrl = comprobantePagoUrl;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}
