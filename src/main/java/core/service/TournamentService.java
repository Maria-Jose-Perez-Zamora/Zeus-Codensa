package core.service;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.Tournament;
import dependencies.util.DataStorage;
import core.validator.TournamentValidator;
import dependencies.mapper.TournamentMapper;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.mapper.EntityToModelMapper;
import dependencies.persistence.mapper.ModelToEntityMapper;
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
public class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);
    private final TournamentRepository tournamentRepository;

    public TournamentService() {
        this.tournamentRepository = null;
    }

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public TournamentResponseDTO createTorneo(TournamentRequestDTO request) {
        log.debug("Ejecutando validaciones para creación de tournament: {}", request.getTournamentName());
        TournamentValidator.validateForCreation(request);
        
        Tournament t = TournamentMapper.toEntity(request);

        if (tournamentRepository != null) {
            try {
                if (tournamentRepository.findByTournamentName(t.getTournamentName()).isPresent()) {
                    throw new BusinessRuleException("Ya existe un torneo con ese nombre");
                }

                TournamentEntity saved = tournamentRepository.save(ModelToEntityMapper.toTournamentEntity(t));
                log.info("Tournament creado exitosamente en DB con ID: {}", saved.getId());
                return TournamentMapper.toDTO(EntityToModelMapper.toTournamentModel(saved));
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al crear tournament en base de datos", ex);
            }
        }
        
        DataStorage.tournaments.add(t);
        log.info("Tournament creado exitosamente en memoria con ID: {}", t.getId());
        return TournamentMapper.toDTO(t);
    }

    public TournamentResponseDTO configurarTorneo(String id, TournamentRequestDTO configInfo) {
        log.debug("Buscando tournament con ID: {} para configuración", id);
        Optional<Tournament> torneoOpt;

        if (tournamentRepository != null) {
            try {
                torneoOpt = tournamentRepository.findById(id).map(EntityToModelMapper::toTournamentModel);
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar tournament en base de datos", ex);
            }
        } else {
            torneoOpt = DataStorage.tournaments.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();
        }

        if (torneoOpt.isEmpty()) {
            log.error("Tournament no encontrado ID: {}", id);
            throw new ResourceNotFoundException("Tournament no encontrado con ID: " + id);
        }

        Tournament t = torneoOpt.get();
        if (!"DRAFT".equals(t.getStatus()) && !"OPEN".equals(t.getStatus())) {
            log.error("Violación temporal: intento de configurar Tournament en status {}", t.getStatus());
            throw new BusinessRuleException("Solo se pueden configurar tournaments en status DRAFT o OPEN");
        }

        if (configInfo.getRules() != null) t.setRules(configInfo.getRules());
        if (configInfo.getFechaCierreInscripciones() != null) t.setFechaCierreInscripciones(configInfo.getFechaCierreInscripciones());
        if (configInfo.getFechaInicioFaseGrupos() != null) t.setFechaInicioFaseGrupos(configInfo.getFechaInicioFaseGrupos());
        if (configInfo.getHorariosPartidos() != null) t.setHorariosPartidos(configInfo.getHorariosPartidos());
        if (configInfo.getCanchas() != null) t.setCanchas(configInfo.getCanchas());
        if (configInfo.getSanctions() != null) t.setSanctions(configInfo.getSanctions());

        if (tournamentRepository != null) {
            try {
                TournamentEntity saved = tournamentRepository.save(ModelToEntityMapper.toTournamentEntity(t));
                log.info("Tournament ID {} configurado exitosamente en DB", id);
                return TournamentMapper.toDTO(EntityToModelMapper.toTournamentModel(saved));
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al actualizar tournament en base de datos", ex);
            }
        }

        log.info("Tournament ID {} configurado exitosamente", id);
        return TournamentMapper.toDTO(t);
    }

    public List<TournamentResponseDTO> getAllTorneos() {
        if (tournamentRepository != null) {
            try {
                return tournamentRepository.findAll().stream()
                        .map(EntityToModelMapper::toTournamentModel)
                        .map(TournamentMapper::toDTO)
                        .collect(Collectors.toList());
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar tournaments en base de datos", ex);
            }
        }

        log.debug("Recuperando todos los tournaments ({})", DataStorage.tournaments.size());
        return DataStorage.tournaments.stream()
                .map(TournamentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
