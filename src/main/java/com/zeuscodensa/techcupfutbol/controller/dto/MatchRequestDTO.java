package com.zeuscodensa.techcupfutbol.controller.dto;

public class MatchRequestDTO {
    private String homeTeam;
    private String awayTeam;
    private String matchDate;
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
