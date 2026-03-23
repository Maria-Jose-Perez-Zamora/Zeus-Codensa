package dependencias.mapper;

import dependencias.dto.TeamRequestDTO;
import dependencias.dto.TeamResponseDTO;
import core.model.Team;

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
