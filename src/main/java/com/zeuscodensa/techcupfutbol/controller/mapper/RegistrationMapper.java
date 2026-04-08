package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Registration;

public class RegistrationMapper {

    public static Registration toEntity(RegistrationRequestDTO dto) {
        return new Registration(
                dto.getTeamName(),
                dto.getTournamentName(),
                dto.getComprobantePagoUrl()
        );
    }

    public static RegistrationResponseDTO toDTO(Registration entity) {
        return new RegistrationResponseDTO(entity);
    }
}
