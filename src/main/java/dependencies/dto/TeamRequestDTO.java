package dependencies.dto;

import java.util.List;

public class TeamRequestDTO {
    private String teamName;
    private String escudo;
    private String coloresUniforme;
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
