package core.model;

import java.util.UUID;

public class Invitation {
    private String id;
    private String captainEmail;
    private String playerEmail;
    private String teamName;
    private String status;

    public Invitation() {}

    public Invitation(String captainEmail, String playerEmail, String teamName) {
        this.id = UUID.randomUUID().toString();
        this.captainEmail = captainEmail;
        this.playerEmail = playerEmail;
        this.teamName = teamName;
        this.status = "PENDING"; // Formerly ENVIADA
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCaptainEmail() { return captainEmail; }
    public void setCaptainEmail(String captainEmail) { this.captainEmail = captainEmail; }
    public String getPlayerEmail() { return playerEmail; }
    public void setPlayerEmail(String playerEmail) { this.playerEmail = playerEmail; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
