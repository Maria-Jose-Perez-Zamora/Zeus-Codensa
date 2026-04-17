package com.zeuscodensa.techcupfutbol.controller.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeamRequestDTO {
    @NotBlank(message = "El nombre del equipo no puede estar vacío")
    private String teamName;
    private String escudo;
    private String coloresUniforme;
    
    @NotNull(message = "La lista de jugadores no puede ser nula")
    @Size(min = 7, max = 20, message = "El equipo debe tener entre 7 y 20 jugadores")
    private List<String> playerEmails;

    public TeamRequestDTO() {}

    public TeamRequestDTO(String teamName, String escudo, String coloresUniforme, List<String> playerEmails) {
        this.teamName = teamName;
        this.escudo = escudo;
        this.coloresUniforme = coloresUniforme;
        this.playerEmails = playerEmails;
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<String> getPlayerEmails() { return playerEmails; }
    public void setPlayerEmails(List<String> playerEmails) { this.playerEmails = playerEmails; }
}
