package com.zeuscodensa.techcupfutbol.core.model;

public class Standing {
    private String teamName;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesDrawn;
    private int matchesLost;
    private int golesFavor;
    private int golesContra;
    private int goalDifference;
    private int points;

    public Standing(String teamName) {
        this.teamName = teamName;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.matchesDrawn = 0;
        this.matchesLost = 0;
        this.golesFavor = 0;
        this.golesContra = 0;
        this.goalDifference = 0;
        this.points = 0;
    }

    public void registrarPartido(int golesFavor, int golesContra) {
        this.matchesPlayed++;
        this.golesFavor += golesFavor;
        this.golesContra += golesContra;
        this.goalDifference = this.golesFavor - this.golesContra;

        if (golesFavor > golesContra) {
            this.matchesWon++;
            this.points += 3;
        } else if (golesFavor == golesContra) {
            this.matchesDrawn++;
            this.points += 1;
        } else {
            this.matchesLost++;
        }
    }

    public String getTeamName() { return teamName; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getMatchesWon() { return matchesWon; }
    public int getMatchesDrawn() { return matchesDrawn; }
    public int getMatchesLost() { return matchesLost; }
    public int getGolesFavor() { return golesFavor; }
    public int getGolesContra() { return golesContra; }
    public int getGoalDifference() { return goalDifference; }
    public int getPoints() { return points; }
}
