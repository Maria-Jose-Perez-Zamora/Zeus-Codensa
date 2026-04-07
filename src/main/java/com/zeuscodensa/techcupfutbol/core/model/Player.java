package com.zeuscodensa.techcupfutbol.core.model;

public class Player extends User {
    private String position;
    private Integer jerseyNumber;

    public Player() {}

    public Player(String position, Integer jerseyNumber) {
        this.position = position;
        this.jerseyNumber = jerseyNumber;
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
}
