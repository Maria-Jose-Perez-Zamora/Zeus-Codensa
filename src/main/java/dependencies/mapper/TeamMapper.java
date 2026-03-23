package dependencies.mapper;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.model.Team;

public class TeamMapper {

    public static Team toEntity(TeamRequestDTO dto) {
        Team t = new Team(dto.getTeamName());
        t.setEscudo(dto.getEscudo());
        t.setColoresUniforme(dto.getColoresUniforme());
        return t;
    }

    public static TeamResponseDTO toDTO(Team entity) {
        return new TeamResponseDTO(entity);
    }
}
