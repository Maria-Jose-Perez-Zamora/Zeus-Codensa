package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.TournamentRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TournamentResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TorneoMapperTest {

    @Test
    public void testToEntity() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Liga");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        Tournament t = TournamentMapper.toEntity(req);
        assertEquals("Liga", t.getTournamentName());
        assertEquals(8, t.getNumeroEquipos());
        assertEquals(50.0, t.getCostoInscripcion());
    }

    @Test
    public void testToDTO() {
        Tournament t = new Tournament();
        t.setTournamentName("Liga2");
        t.setNumeroEquipos(10);
        TournamentResponseDTO dto = TournamentMapper.toDTO(t);
        assertEquals("Liga2", dto.getTournamentName());
        assertEquals(10, dto.getNumeroEquipos());
    }
}
