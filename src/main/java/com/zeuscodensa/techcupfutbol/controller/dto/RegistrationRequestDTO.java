package com.zeuscodensa.techcupfutbol.controller.dto;

public class RegistrationRequestDTO {
    private String teamName;
    private String tournamentName;
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
