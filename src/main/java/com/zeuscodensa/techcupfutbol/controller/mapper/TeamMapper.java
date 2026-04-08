package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Team;

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
