package dependencies.dto;

import core.model.Team;
import java.util.List;
import java.util.stream.Collectors;

public class TeamResponseDTO {
    private String teamName;
    private String escudo;
    private String coloresUniforme;
    private List<UserResponseDTO> players;

    public TeamResponseDTO() {}

    public TeamResponseDTO(Team team) {
        this.teamName = team.getTeamName();
        this.escudo = team.getEscudo();
        this.coloresUniforme = team.getColoresUniforme();
        this.players = team.getPlayers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<UserResponseDTO> getPlayers() { return players; }
    public void setPlayers(List<UserResponseDTO> players) { this.players = players; }
}
