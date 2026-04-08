package com.zeuscodensa.techcupfutbol.core.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
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
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<User> getPlayers() { return players; }
    public void setPlayers(List<User> players) { this.players = players; }
}