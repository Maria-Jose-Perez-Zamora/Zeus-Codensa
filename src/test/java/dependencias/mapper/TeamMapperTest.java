package dependencias.mapper;

import dependencias.dto.TeamRequestDTO;
import dependencias.dto.TeamResponseDTO;
import core.model.Team;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamMapperTest {

    @Test
    public void testToEntity() {
        TeamRequestDTO req = new TeamRequestDTO("Delta", "escudo.png", "Azul", Arrays.asList("j@a.com"));
        Team t = TeamMapper.toEntity(req);
        assertEquals("Delta", t.getNombreEquipo());
        assertEquals("escudo.png", t.getEscudo());
        assertEquals("Azul", t.getColoresUniforme());
    }

    @Test
    public void testToDTO() {
        Team t = new Team("Epsilon");
        t.setColoresUniforme("Rojo");
        TeamResponseDTO dto = TeamMapper.toDTO(t);
        assertEquals("Epsilon", dto.getNombreEquipo());
        assertEquals("Rojo", dto.getColoresUniforme());
    }
}
