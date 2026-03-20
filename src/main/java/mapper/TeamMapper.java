package mapper;

import dto.TeamRequestDTO;
import dto.TeamResponseDTO;
import model.Team;

public class TeamMapper {

    public static Team toEntity(TeamRequestDTO dto) {
        Team t = new Team(dto.getNombreEquipo());
        t.setEscudo(dto.getEscudo());
        t.setColoresUniforme(dto.getColoresUniforme());
        return t;
    }

    public static TeamResponseDTO toDTO(Team entity) {
        return new TeamResponseDTO(entity);
    }
}
