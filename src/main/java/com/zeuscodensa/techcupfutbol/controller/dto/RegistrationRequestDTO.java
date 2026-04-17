package com.zeuscodensa.techcupfutbol.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class RegistrationRequestDTO {
    @NotBlank(message = "teamName is required")
    private String teamName;
    @NotBlank(message = "tournamentName is required")
    private String tournamentName;
    @NotBlank(message = "comprobantePagoUrl is required")
    private String comprobantePagoUrl;

    public RegistrationRequestDTO() {}

    public RegistrationRequestDTO(String teamName, String tournamentName, String comprobantePagoUrl) {
        this.teamName = teamName;
        this.tournamentName = tournamentName;
        this.comprobantePagoUrl = comprobantePagoUrl;
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
}
