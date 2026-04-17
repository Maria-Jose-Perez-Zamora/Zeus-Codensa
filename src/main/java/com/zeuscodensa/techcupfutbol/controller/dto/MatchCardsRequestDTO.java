package com.zeuscodensa.techcupfutbol.controller.dto;

import java.util.List;
import java.util.Map;

public class MatchCardsRequestDTO {
    private Map<String, List<String>> yellowCards;
    private Map<String, List<String>> redCards;

    public MatchCardsRequestDTO() {
    }

    public MatchCardsRequestDTO(Map<String, List<String>> yellowCards, Map<String, List<String>> redCards) {
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }

    public Map<String, List<String>> getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Map<String, List<String>> yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Map<String, List<String>> getRedCards() {
        return redCards;
    }

    public void setRedCards(Map<String, List<String>> redCards) {
        this.redCards = redCards;
    }
}
