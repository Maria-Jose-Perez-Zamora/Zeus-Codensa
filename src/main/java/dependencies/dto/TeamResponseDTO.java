package dependencies.dto;

import core.model.Team;
import java.util.List;
import java.util.stream.Collectors;

public class TeamResponseDTO {
    private String nombreEquipo;
    private String escudo;
    private String coloresUniforme;
    private List<UserResponseDTO> players;

    public TeamResponseDTO() {}

    public TeamResponseDTO(Team team) {
        this.nombreEquipo = team.getNombreEquipo();
        this.escudo = team.getEscudo();
        this.coloresUniforme = team.getColoresUniforme();
        this.players = team.getJugadores().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<UserResponseDTO> getJugadores() { return players; }
    public void setJugadores(List<UserResponseDTO> players) { this.players = players; }
}
