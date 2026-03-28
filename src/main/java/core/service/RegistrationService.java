package core.service;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.Registration;
import dependencies.util.DataStorage;
import core.validator.RegistrationValidator;
import dependencies.mapper.RegistrationMapper;
import dependencies.persistence.entity.RegistrationEntity;
import dependencies.persistence.mapper.EntityToModelMapper;
import dependencies.persistence.mapper.ModelToEntityMapper;
import dependencies.persistence.repository.RegistrationRepository;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.TournamentRepository;
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

    public RegistrationService() {
        this.registrationRepository = null;
        this.teamRepository = null;
        this.tournamentRepository = null;
    }

    @Autowired
    public RegistrationService(
            RegistrationRepository registrationRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository
    ) {
        this.registrationRepository = registrationRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public RegistrationResponseDTO inscribir(RegistrationRequestDTO request) {
        log.debug("Ejecutando validaciones para inscripción del team {}", request.getTeamName());
        if (registrationRepository == null) {
            RegistrationValidator.validateForInscripcion(request);
        } else {
            validateRegistrationWithRepositories(request);
        }

        Registration nuevaInscripcion = RegistrationMapper.toEntity(request);

        if (registrationRepository != null) {
            try {
                RegistrationEntity saved = registrationRepository.save(ModelToEntityMapper.toRegistrationEntity(nuevaInscripcion));
                log.info("Inscripción creada en DB con ID: {}, status {}", saved.getId(), saved.getStatus());
                return RegistrationMapper.toDTO(EntityToModelMapper.toRegistrationModel(saved));
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al crear inscripción en base de datos", ex);
            }
        }

        DataStorage.registrations.add(nuevaInscripcion);
        log.info("Inscripción creada con ID: {}, status {}", nuevaInscripcion.getId(), nuevaInscripcion.getStatus());
        return RegistrationMapper.toDTO(nuevaInscripcion);
    }

    public RegistrationResponseDTO actualizarEstado(String id, String nuevoEstado) {
        log.debug("Intentando actualizar status de la inscripción {} a {}", id, nuevoEstado);

        Optional<Registration> inscripcionOpt;

        if (registrationRepository != null) {
            try {
                inscripcionOpt = registrationRepository.findById(id).map(EntityToModelMapper::toRegistrationModel);
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar inscripción en base de datos", ex);
            }
        } else {
            inscripcionOpt = DataStorage.registrations.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst();
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

        if (registrationRepository != null) {
            try {
                RegistrationEntity saved = registrationRepository.save(ModelToEntityMapper.toRegistrationEntity(registration));
                log.info("Estado de inscripción ID {} actualizado exitosamente a {} en DB", id, nuevoEstado);
                return RegistrationMapper.toDTO(EntityToModelMapper.toRegistrationModel(saved));
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al actualizar inscripción en base de datos", ex);
            }
        }

        log.info("Estado de inscripción ID {} actualizado exitosamente a {}", id, nuevoEstado);
        return RegistrationMapper.toDTO(registration);
    }

    public List<RegistrationResponseDTO> getAll() {
        if (registrationRepository != null) {
            try {
                return registrationRepository.findAll().stream()
                        .map(EntityToModelMapper::toRegistrationModel)
                        .map(RegistrationMapper::toDTO)
                        .collect(Collectors.toList());
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar inscripciones en base de datos", ex);
            }
        }

        return DataStorage.registrations.stream()
                .map(RegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateRegistrationWithRepositories(RegistrationRequestDTO request) {
        if (request.getTeamName() == null || request.getTeamName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de team es obligatorio");
        }
        if (request.getTournamentName() == null || request.getTournamentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de tournament es obligatorio");
        }
        if (request.getComprobantePagoUrl() == null || request.getComprobantePagoUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de pago es obligatorio");
        }

        if (teamRepository == null || teamRepository.findByTeamName(request.getTeamName()).isEmpty()) {
            throw new BusinessRuleException("El team especificado no existe");
        }

        boolean torneoValido = tournamentRepository != null && tournamentRepository.findByTournamentName(request.getTournamentName())
                .map(t -> "OPEN".equals(t.getStatus()))
                .orElse(false);
        if (!torneoValido) {
            throw new BusinessRuleException("El tournament no existe o no se encuentra OPEN para registrations");
        }

        boolean estaInscrito = registrationRepository.findByTournamentName(request.getTournamentName()).stream()
                .anyMatch(i -> request.getTeamName().equals(i.getTeamName()));
        if (estaInscrito) {
            throw new BusinessRuleException("El team ya cuenta con un proceso de registration para este tournament");
        }
    }
}
