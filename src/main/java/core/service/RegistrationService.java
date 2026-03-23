package core.service;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Registration;
import core.model.Registration;
import dependencies.util.DataStorage;
import core.validator.RegistrationValidator;
import dependencies.mapper.RegistrationMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    public RegistrationResponseDTO inscribir(RegistrationRequestDTO request) {
        log.debug("Ejecutando validaciones para inscripción del team {}", request.getTeamName());
        RegistrationValidator.validateForInscripcion(request);

        Registration nuevaInscripcion = RegistrationMapper.toEntity(request);

        DataStorage.registrations.add(nuevaInscripcion);
        log.info("Inscripción creada con ID: {}, status {}", nuevaInscripcion.getId(), nuevaInscripcion.getStatus());
        return RegistrationMapper.toDTO(nuevaInscripcion);
    }

    public RegistrationResponseDTO actualizarEstado(String id, String nuevoEstado) {
        log.debug("Intentando actualizar status de la inscripción {} a {}", id, nuevoEstado);
        if (!nuevoEstado.equals("PENDIENTE") && 
            !nuevoEstado.equals("EN_REVISION") && 
            !nuevoEstado.equals("APROBADO") && 
            !nuevoEstado.equals("RECHAZADO")) {
            log.error("Violación transaccional: status '{}' no permitido", nuevoEstado);
            throw new BusinessRuleException("Estado no valido para registration");
        }

        Optional<Registration> inscripcionOpt = DataStorage.registrations.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (inscripcionOpt.isEmpty()) {
            log.error("Inscripción no encontrada ID: {}", id);
            throw new ResourceNotFoundException("La registration especificada no existe");
        }

        Registration registration = inscripcionOpt.get();
        registration.setStatus(nuevoEstado);

        log.info("Estado de inscripción ID {} actualizado exitosamente a {}", id, nuevoEstado);
        return RegistrationMapper.toDTO(registration);
    }

    public List<RegistrationResponseDTO> getAll() {
        return DataStorage.registrations.stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
