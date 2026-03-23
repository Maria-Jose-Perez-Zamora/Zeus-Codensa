package dependencias.mapper;

import dependencias.dto.InscripcionRequestDTO;
import dependencias.dto.InscripcionResponseDTO;
import core.model.Inscripcion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InscripcionMapperTest {

    @Test
    public void testToEntity() {
        InscripcionRequestDTO req = new InscripcionRequestDTO("Alpha", "Liga", "url_pago");
        Inscripcion i = InscripcionMapper.toEntity(req);
        assertEquals("Alpha", i.getNombreEquipo());
        assertEquals("Liga", i.getNombreTorneo());
        assertEquals("url_pago", i.getComprobantePagoUrl());
    }

    @Test
    public void testToDTO() {
        Inscripcion i = new Inscripcion("Beta", "Liga2", "url2");
        i.setEstado("APROBADO");
        InscripcionResponseDTO dto = InscripcionMapper.toDTO(i);
        assertEquals("Beta", dto.getNombreEquipo());
        assertEquals("APROBADO", dto.getEstado());
    }
}
