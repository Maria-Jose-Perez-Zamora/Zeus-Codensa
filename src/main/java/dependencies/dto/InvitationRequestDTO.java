package dependencies.dto;

public class InvitationRequestDTO {
    private String captainEmail;
    private String playerEmail;
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
