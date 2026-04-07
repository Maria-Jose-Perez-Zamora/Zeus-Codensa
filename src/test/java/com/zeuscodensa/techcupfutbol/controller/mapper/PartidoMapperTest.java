package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartidoMapperTest {

    @Test
    public void testToEntity() {
        MatchRequestDTO req = new MatchRequestDTO();
        req.setHomeTeam("Local");
        req.setAwayTeam("Visitante");
        req.setTournamentName("Liga");
        Match p = MatchMapper.toEntity(req);
        assertEquals("Local", p.getHomeTeam());
        assertEquals("Visitante", p.getAwayTeam());
        assertEquals("Liga", p.getTournamentName());
    }

    @Test
    public void testToDTO() {
        Match p = new Match();
        p.setHomeTeam("Local");
        p.setTournamentName("Liga2");
        p.setHomeScore(2);
        MatchResponseDTO dto = MatchMapper.toDTO(p);
        assertEquals("Local", dto.getHomeTeam());
        assertEquals("Liga2", dto.getTournamentName());
        assertEquals(2, dto.getHomeScore());
    }
}
