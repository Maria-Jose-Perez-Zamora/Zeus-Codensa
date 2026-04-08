package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.validator.TournamentValidator;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);
    private final ITournamentRepository tournamentRepository;
    private final TournamentValidator tournamentValidator;

    @Autowired
    public TournamentService(ITournamentRepository tournamentRepository, TournamentValidator tournamentValidator) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentValidator = tournamentValidator;
    }

    public Tournament createTorneo(Tournament newTournament) {
        log.debug("Ejecutando validaciones para creación de tournament: {}", newTournament.getTournamentName());
        tournamentValidator.validateForCreation(newTournament);
        
        try {
            if (tournamentRepository.findByTournamentName(newTournament.getTournamentName()).isPresent()) {
                throw new BusinessRuleException("Ya existe un torneo con ese nombre");
            }

            Tournament saved = tournamentRepository.save(newTournament);
            log.info("Tournament creado exitosamente en DB con ID: {}", saved.getId());
            return saved;
        } catch (Exception ex) {
            if (ex instanceof BusinessRuleException) {
                throw (BusinessRuleException) ex;
            }
            throw new PersistenceAccessException("Error al crear tournament en base de datos", ex);
        }
    }

    public Tournament configurarTorneo(String id, Tournament configInfo) {
        log.debug("Buscando tournament con ID: {} para configuración", id);
        Optional<Tournament> torneoOpt;

        try {
            torneoOpt = tournamentRepository.findById(id);
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar tournament en base de datos", ex);
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

        try {
            Tournament saved = tournamentRepository.save(t);
            log.info("Tournament ID {} configurado exitosamente en DB", id);
            return saved;
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al actualizar tournament en base de datos", ex);
        }
    }

    public List<Tournament> getAllTorneos() {
        try {
            return tournamentRepository.findAll();
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar tournaments en base de datos", ex);
        }
    }

    public Tournament getTorneoById(String id) {
        log.debug("Buscando tournament con ID: {}", id);
        Optional<Tournament> torneoOpt;

        try {
            torneoOpt = tournamentRepository.findById(id);
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar tournament en base de datos", ex);
        }

        if (torneoOpt.isEmpty()) {
            throw new ResourceNotFoundException("Tournament no encontrado con ID: " + id);
        }

        return torneoOpt.get();
    }
}
