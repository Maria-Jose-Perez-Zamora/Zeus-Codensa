package dependencies.mapper;

import dependencies.dto.MatchRequestDTO;
import dependencies.dto.MatchResponseDTO;
import core.model.Match;

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
