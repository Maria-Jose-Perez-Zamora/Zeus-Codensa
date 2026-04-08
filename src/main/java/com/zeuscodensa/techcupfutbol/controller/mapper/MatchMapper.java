package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Match;

public class MatchMapper {

    public static Match toEntity(MatchRequestDTO dto) {
        return new Match(
                dto.getHomeTeam(),
                dto.getAwayTeam(),
                dto.getMatchDate(),
                dto.getTournamentName()
        );
    }

    public static MatchResponseDTO toDTO(Match entity) {
        return new MatchResponseDTO(entity);
    }
}
