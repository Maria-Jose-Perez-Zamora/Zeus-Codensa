package dependencias.mapper;

import dependencias.dto.PartidoRequestDTO;
import dependencias.dto.PartidoResponseDTO;
import core.model.Partido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartidoMapperTest {

    @Test
    public void testToEntity() {
        PartidoRequestDTO req = new PartidoRequestDTO();
        req.setEquipoLocal("Local");
        req.setEquipoVisitante("Visitante");
        req.setNombreTorneo("Liga");
        Partido p = PartidoMapper.toEntity(req);
        assertEquals("Local", p.getEquipoLocal());
        assertEquals("Visitante", p.getEquipoVisitante());
        assertEquals("Liga", p.getNombreTorneo());
    }

    @Test
    public void testToDTO() {
        Partido p = new Partido();
        p.setEquipoLocal("Local");
        p.setNombreTorneo("Liga2");
        p.setMarcadorLocal(2);
        PartidoResponseDTO dto = PartidoMapper.toDTO(p);
        assertEquals("Local", dto.getEquipoLocal());
        assertEquals("Liga2", dto.getNombreTorneo());
        assertEquals(2, dto.getMarcadorLocal());
    }
}
