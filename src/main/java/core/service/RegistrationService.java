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

        Optional<Registration> inscripcionOpt = DataStorage.registrations.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (inscripcionOpt.isEmpty()) {
            log.error("Inscripción no encontrada ID: {}", id);
            throw new ResourceNotFoundException("La registration especificada no existe");
        }

        Registration registration = inscripcionOpt.get();
        String estadoActual = registration.getStatus();
        
        // State Machine validation
        if ("PENDING".equals(estadoActual) && !"IN_REVIEW".equals(nuevoEstado)) {
            log.error("Violación transaccional: No se permite transición de {} a {}", estadoActual, nuevoEstado);
            throw new BusinessRuleException("Desde PENDING solo se puede pasar a IN_REVIEW");
        }
        
        if ("IN_REVIEW".equals(estadoActual) && (!"APPROVED".equals(nuevoEstado) && !"REJECTED".equals(nuevoEstado))) {
            log.error("Violación transaccional: No se permite transición de {} a {}", estadoActual, nuevoEstado);
            throw new BusinessRuleException("Desde IN_REVIEW solo se puede pasar a APPROVED o REJECTED");
        }
        
        if ("APPROVED".equals(estadoActual) || "REJECTED".equals(estadoActual)) {
            log.error("Violación transaccional: Intentando alterar un estado final: {}", estadoActual);
            throw new BusinessRuleException("El estado de la inscripción ya es definitivo y no puede ser modificado");
        }

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
