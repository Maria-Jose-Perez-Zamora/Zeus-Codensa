package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
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
        i.setStatus("APPROVED");
        RegistrationResponseDTO dto = RegistrationMapper.toDTO(i);
        assertEquals("Beta", dto.getTeamName());
        assertEquals("APPROVED", dto.getStatus());
    }
}
