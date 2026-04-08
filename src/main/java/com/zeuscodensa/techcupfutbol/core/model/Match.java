package com.zeuscodensa.techcupfutbol.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Match {
    private String id;
    private String homeTeam;
    private String awayTeam;
    private String matchDate;
    private Integer homeScore;
    private Integer awayScore;
    private String status;
    private String tournamentName;
    private String refereeEmail;
    // Alineaciones: mapa equipoNombre -> lista de correos de players
    private Map<String, List<String>> alineaciones;
    // Goleadores: mapa correoJugador -> cantidad de goals anotados
    private Map<String, Integer> goals;
    // Tarjetas (RF-006): mapa nombreJugador(minuto) -> motivo/cantidad
    private Map<String, List<String>> yellowCards;
    private Map<String, List<String>> redCards;

    public Match() {
        this.alineaciones = new HashMap<>();
        this.goals = new HashMap<>();
        this.yellowCards = new HashMap<>();
        this.redCards = new HashMap<>();
    }

    public Match(String homeTeam, String awayTeam, String matchDate, String tournamentName) {
        this.id = UUID.randomUUID().toString();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.tournamentName = tournamentName;
        this.status = "SCHEDULED";
        this.homeScore = 0;
        this.awayScore = 0;
        this.alineaciones = new HashMap<>();
        this.goals = new HashMap<>();
        this.yellowCards = new HashMap<>();
        this.redCards = new HashMap<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public String getMatchDate() { return matchDate; }
    public void setMatchDate(String matchDate) { this.matchDate = matchDate; }
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getRefereeEmail() { return refereeEmail; }
    public void setRefereeEmail(String refereeEmail) { this.refereeEmail = refereeEmail; }
    public Map<String, List<String>> getAlineaciones() { return alineaciones; }
    public void setAlineaciones(Map<String, List<String>> alineaciones) { this.alineaciones = alineaciones; }
    public Map<String, Integer> getGoles() { return goals; }
    public void setGoles(Map<String, Integer> goals) { this.goals = goals; }
    public Map<String, List<String>> getYellowCards() { return yellowCards; }
    public void setYellowCards(Map<String, List<String>> yellowCards) { this.yellowCards = yellowCards; }
    public Map<String, List<String>> getRedCards() { return redCards; }
    public void setRedCards(Map<String, List<String>> redCards) { this.redCards = redCards; }
}
