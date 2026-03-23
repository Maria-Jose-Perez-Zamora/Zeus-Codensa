package dependencies.mapper;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.model.Registration;

public class RegistrationMapper {

    public static Registration toEntity(RegistrationRequestDTO dto) {
        return new Registration(
                dto.getNombreEquipo(),
                dto.getTournamentName(),
                dto.getComprobantePagoUrl()
        );
    }

    public static RegistrationResponseDTO toDTO(Registration entity) {
        return new RegistrationResponseDTO(entity);
    }
}
