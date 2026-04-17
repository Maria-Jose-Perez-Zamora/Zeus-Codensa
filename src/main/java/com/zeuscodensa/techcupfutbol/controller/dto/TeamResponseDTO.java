package com.zeuscodensa.techcupfutbol.controller.dto;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import java.util.List;
import java.util.stream.Collectors;

public class TeamResponseDTO {
    private Long id;
    private String teamName;
    private String escudo;
    private String coloresUniforme;
    private String captainName;
    private List<UserResponseDTO> players;

    public TeamResponseDTO() {}

    public TeamResponseDTO(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.escudo = team.getEscudo();
        this.coloresUniforme = team.getColoresUniforme();
        this.captainName = team.getCaptainName();
        this.players = team.getPlayers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public String getCaptainName() { return captainName; }
    public void setCaptainName(String captainName) { this.captainName = captainName; }
    public List<UserResponseDTO> getPlayers() { return players; }
    public void setPlayers(List<UserResponseDTO> players) { this.players = players; }
}
