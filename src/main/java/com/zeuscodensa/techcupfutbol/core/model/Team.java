package com.zeuscodensa.techcupfutbol.core.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private Long id;
    private String teamName;
    private String escudo;
    private String coloresUniforme;
    private List<User> players;

    public Team() {
        this.players = new ArrayList<>();
    }

    public Team(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<User> getPlayers() { return players; }
    public void setPlayers(List<User> players) { this.players = players; }

    /** Returns the name of the first player with CAPTAIN role, or null if none found. */
    public String getCaptainName() {
        if (players == null) return null;
        return players.stream()
                .filter(u -> u.getRole() != null && u.getRole().name().equals("CAPTAIN"))
                .map(User::getName)
                .findFirst()
                .orElse(null);
    }
}