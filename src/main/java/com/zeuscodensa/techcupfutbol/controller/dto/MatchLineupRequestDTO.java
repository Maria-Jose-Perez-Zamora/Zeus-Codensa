package com.zeuscodensa.techcupfutbol.controller.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class MatchLineupRequestDTO {
    @NotBlank(message = "teamName is required")
    private String teamName;

    @NotEmpty(message = "players cannot be empty")
    private List<@NotBlank(message = "player email cannot be blank") String> players;

    public MatchLineupRequestDTO() {
    }

    public MatchLineupRequestDTO(String teamName, List<String> players) {
        this.teamName = teamName;
        this.players = players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}