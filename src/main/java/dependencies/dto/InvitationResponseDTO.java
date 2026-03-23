package dependencies.dto;

import core.model.Invitation;

public class InvitationResponseDTO {
    private String id;
    private String playerEmail;
    private String teamName;
    private String status;
    private String message;

    public InvitationResponseDTO() {}

    public InvitationResponseDTO(Invitation inv) {
        this.id = inv.getId();
        this.playerEmail = inv.getPlayerEmail();
        this.teamName = inv.getTeamName();
        this.status = inv.getStatus();
        this.message = "Invitation successfully sent to player " + inv.getPlayerEmail();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlayerEmail() { return playerEmail; }
    public void setPlayerEmail(String playerEmail) { this.playerEmail = playerEmail; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
