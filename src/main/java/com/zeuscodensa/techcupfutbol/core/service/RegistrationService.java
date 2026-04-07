package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationResponseDTO;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.validator.RegistrationValidator;
import com.zeuscodensa.techcupfutbol.controller.mapper.RegistrationMapper;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    private final RegistrationRepository registrationRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final RegistrationValidator registrationValidator;

    @Autowired
    public RegistrationService(
            RegistrationRepository registrationRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository,
            RegistrationValidator registrationValidator
    ) {
        this.registrationRepository = registrationRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.registrationValidator = registrationValidator;
    }

    public RegistrationResponseDTO inscribir(RegistrationRequestDTO request) {
        log.debug("Ejecutando validaciones para inscripción del team {}", request.getTeamName());
        registrationValidator.validateForInscripcion(request);

        Registration nuevaInscripcion = RegistrationMapper.toEntity(request);

        try {
            RegistrationEntity saved = registrationRepository.save(ModelToEntityMapper.toRegistrationEntity(nuevaInscripcion));
            log.info("Inscripción creada en DB con ID: {}, status {}", saved.getId(), saved.getStatus());
            return RegistrationMapper.toDTO(EntityToModelMapper.toRegistrationModel(saved));
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al crear inscripción en base de datos", ex);
        }
    }

    public RegistrationResponseDTO actualizarEstado(String id, String nuevoEstado) {
        log.debug("Intentando actualizar status de la inscripción {} a {}", id, nuevoEstado);

        Optional<Registration> inscripcionOpt;

        try {
            inscripcionOpt = registrationRepository.findById(id).map(EntityToModelMapper::toRegistrationModel);
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al consultar inscripción en base de datos", ex);
        }

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

        try {
            RegistrationEntity saved = registrationRepository.save(ModelToEntityMapper.toRegistrationEntity(registration));
            log.info("Estado de inscripción ID {} actualizado exitosamente a {} en DB", id, nuevoEstado);
            return RegistrationMapper.toDTO(EntityToModelMapper.toRegistrationModel(saved));
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al actualizar inscripción en base de datos", ex);
        }
    }

    public List<RegistrationResponseDTO> getAll() {
        try {
            return registrationRepository.findAll().stream()
                    .map(EntityToModelMapper::toRegistrationModel)
                    .map(RegistrationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al consultar inscripciones en base de datos", ex);
        }
    }
}
