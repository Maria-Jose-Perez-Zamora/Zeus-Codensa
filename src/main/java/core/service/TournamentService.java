package core.service;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Tournament;
import core.model.Tournament;
import dependencies.util.DataStorage;
import core.validator.TournamentValidator;
import dependencies.mapper.TournamentMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);

    public TournamentResponseDTO createTorneo(TournamentRequestDTO request) {
        log.debug("Ejecutando validaciones para creación de tournament: {}", request.getTournamentName());
        TournamentValidator.validateForCreation(request);
        
        Tournament t = TournamentMapper.toEntity(request);
        
        DataStorage.tournaments.add(t);
        log.info("Tournament creado exitosamente en memoria con ID: {}", t.getId());
        return TournamentMapper.toDTO(t);
    }

    public TournamentResponseDTO configurarTorneo(String id, TournamentRequestDTO configInfo) {
        log.debug("Buscando tournament con ID: {} para configuración", id);
        Optional<Tournament> torneoOpt = DataStorage.tournaments.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

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

        log.info("Tournament ID {} configurado exitosamente", id);
        return TournamentMapper.toDTO(t);
    }

    public List<TournamentResponseDTO> getAllTorneos() {
        log.debug("Recuperando todos los tournaments ({})", DataStorage.tournaments.size());
        return DataStorage.tournaments.stream()
                .map(TournamentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
