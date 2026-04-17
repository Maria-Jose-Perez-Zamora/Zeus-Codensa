package com.zeuscodensa.techcupfutbol.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MatchScoreRequestDTO {
    @NotNull(message = "homeScore is required")
    @Min(value = 0, message = "homeScore must be greater than or equal to 0")
    private Integer homeScore;

    @NotNull(message = "awayScore is required")
    @Min(value = 0, message = "awayScore must be greater than or equal to 0")
    private Integer awayScore;

    public MatchScoreRequestDTO() {
    }

    public MatchScoreRequestDTO(Integer homeScore, Integer awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
}
