package dependencies.mapper;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.model.Registration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InscripcionMapperTest {

    @Test
    public void testToEntity() {
        RegistrationRequestDTO req = new RegistrationRequestDTO("Alpha", "Liga", "url_pago");
        Registration i = RegistrationMapper.toEntity(req);
        assertEquals("Alpha", i.getTeamName());
        assertEquals("Liga", i.getTournamentName());
        assertEquals("url_pago", i.getComprobantePagoUrl());
    }

    @Test
    public void testToDTO() {
        Registration i = new Registration("Beta", "Liga2", "url2");
        i.setStatus("APROBADO");
        RegistrationResponseDTO dto = RegistrationMapper.toDTO(i);
        assertEquals("Beta", dto.getTeamName());
        assertEquals("APROBADO", dto.getStatus());
    }
}
