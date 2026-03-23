package dependencias.mapper;

import dependencias.dto.TorneoRequestDTO;
import dependencias.dto.TorneoResponseDTO;
import core.model.Torneo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TorneoMapperTest {

    @Test
    public void testToEntity() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Liga");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        Torneo t = TorneoMapper.toEntity(req);
        assertEquals("Liga", t.getNombreTorneo());
        assertEquals(8, t.getNumeroEquipos());
        assertEquals(50.0, t.getCostoInscripcion());
    }

    @Test
    public void testToDTO() {
        Torneo t = new Torneo();
        t.setNombreTorneo("Liga2");
        t.setNumeroEquipos(10);
        TorneoResponseDTO dto = TorneoMapper.toDTO(t);
        assertEquals("Liga2", dto.getNombreTorneo());
        assertEquals(10, dto.getNumeroEquipos());
    }
}
