package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamMapperTest {

    @Test
    public void testToEntity() {
        TeamRequestDTO req = new TeamRequestDTO("Delta", "escudo.png", "Azul", Arrays.asList("user.test1-a@escuelaing.edu.co"));
        Team t = TeamMapper.toEntity(req);
        assertEquals("Delta", t.getTeamName());
        assertEquals("escudo.png", t.getEscudo());
        assertEquals("Azul", t.getColoresUniforme());
    }

    @Test
    public void testToDTO() {
        Team t = new Team("Epsilon");
        t.setColoresUniforme("Rojo");
        TeamResponseDTO dto = TeamMapper.toDTO(t);
        assertEquals("Epsilon", dto.getTeamName());
        assertEquals("Rojo", dto.getColoresUniforme());
    }
}
