package com.zeuscodensa.techcupfutbol.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class InvitationRequestDTO {
    @NotBlank(message = "captainEmail is required")
    @Email(message = "captainEmail must be valid")
    private String captainEmail;
    @NotBlank(message = "playerEmail is required")
    @Email(message = "playerEmail must be valid")
    private String playerEmail;
    @NotBlank(message = "teamName is required")
    private String teamName;

    public InvitationRequestDTO() {}

    public InvitationRequestDTO(String captainEmail, String playerEmail, String teamName) {
        this.captainEmail = captainEmail;
        this.playerEmail = playerEmail;
        this.teamName = teamName;
    }

    public String getCaptainEmail() { return captainEmail; }
    public void setCaptainEmail(String captainEmail) { this.captainEmail = captainEmail; }
    public String getPlayerEmail() { return playerEmail; }
    public void setPlayerEmail(String playerEmail) { this.playerEmail = playerEmail; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}
