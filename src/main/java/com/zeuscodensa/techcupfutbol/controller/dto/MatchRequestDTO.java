package com.zeuscodensa.techcupfutbol.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class MatchRequestDTO {
    @NotBlank(message = "homeTeam is required")
    private String homeTeam;
    @NotBlank(message = "awayTeam is required")
    private String awayTeam;
    @NotBlank(message = "matchDate is required")
    private String matchDate;
    @NotBlank(message = "tournamentName is required")
    private String tournamentName;

    public MatchRequestDTO() {}

    public MatchRequestDTO(String homeTeam, String awayTeam, String matchDate, String tournamentName) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.tournamentName = tournamentName;
    }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public String getMatchDate() { return matchDate; }
    public void setMatchDate(String matchDate) { this.matchDate = matchDate; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
}
