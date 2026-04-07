package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.validator.MatchValidator;
import com.zeuscodensa.techcupfutbol.controller.mapper.MatchMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);
    
    private final MatchValidator matchValidator;
    private final com.zeuscodensa.techcupfutbol.persistence.repository.MatchRepository matchRepository;
    private final com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository userRepository;

    public MatchService(MatchValidator matchValidator, 
                        com.zeuscodensa.techcupfutbol.persistence.repository.MatchRepository matchRepository,
                        com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository userRepository) {
        this.matchValidator = matchValidator;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    public MatchResponseDTO registrarPartido(MatchRequestDTO request) {
        log.debug("Running validations for match creation");
        matchValidator.validateForCreation(request);

        Match nuevoPartido = MatchMapper.toEntity(request);

        com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity savedEntity = matchRepository.save(
            com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper.toMatchEntity(nuevoPartido)
        );
        
        log.info("Match successfully registered: home {} vs away {} (Tournament {})", 
                 request.getHomeTeam(), request.getAwayTeam(), request.getTournamentName());
        return MatchMapper.toDTO(com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper.toMatchModel(savedEntity));
    }

    public MatchResponseDTO actualizarMarcador(String id, Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null || homeScore < 0 || awayScore < 0) {
            log.error("Functional violation: Negative or null scores received");
            throw new BusinessRuleException("Invalid scores");
        }

        Match match = findOrThrow(id);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("FINISHED");

        matchRepository.save(com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper.toMatchEntity(match));

        log.info("Score updated for ID {} | {} - {}", id, homeScore, awayScore);
        return MatchMapper.toDTO(match);
    }

    public MatchResponseDTO registrarAlineacion(String id, String teamName, List<String> players) {
        if (players == null || players.isEmpty()) {
            throw new BusinessRuleException("The lineup cannot be empty");
        }

        Match match = findOrThrow(id);

        if (!match.getHomeTeam().equals(teamName) && !match.getAwayTeam().equals(teamName)) {
            log.warn("Team {} does not participate in this match", teamName);
            throw new BusinessRuleException("The specified team does not participate in this match");
        }

        match.getAlineaciones().put(teamName, players);
        
        matchRepository.save(com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper.toMatchEntity(match));
        log.info("Lineup successfully registered: {} players for team {}", players.size(), teamName);
        return MatchMapper.toDTO(match);
    }

    public MatchResponseDTO registrarTarjetas(String id, Map<String, List<String>> amarillas, Map<String, List<String>> rojas) {
        Match match = findOrThrow(id);
        if (amarillas != null) {
            match.setYellowCards(amarillas);
        }
        if (rojas != null) {
            match.setRedCards(rojas);
        }
        
        matchRepository.save(com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper.toMatchEntity(match));
        log.debug("Yellow/red cards saved for ID {}", id);
        return MatchMapper.toDTO(match);
    }

    public List<MatchResponseDTO> getMatchesByReferee(String refereeEmail) {
        return matchRepository.findByRefereeEmail(refereeEmail).stream()
                .map(com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper::toMatchModel)
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MatchResponseDTO asignarArbitro(String id, String refereeEmail) {
        boolean esArbitro = userRepository.findByEmail(refereeEmail)
                .map(u -> com.zeuscodensa.techcupfutbol.core.model.Role.REFEREE.equals(u.getRole()))
                .orElse(false);
        if (!esArbitro) {
            log.error("Assignment failed: {} lacks the REFEREE role", refereeEmail);
            throw new BusinessRuleException("The specified email does not correspond to a registered referee");
        }

        Match match = findOrThrow(id);
        match.setRefereeEmail(refereeEmail);
        matchRepository.save(com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper.toMatchEntity(match));
        log.info("Referee {} successfully assigned to Match ID {}", refereeEmail, id);
        return MatchMapper.toDTO(match);
    }

    public List<MatchResponseDTO> getAll() {
        return matchRepository.findAll().stream()
                .map(com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper::toMatchModel)
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Match findOrThrow(String id) {
        return matchRepository.findById(id)
                .map(com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper::toMatchModel)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with its Base Identifier"));
    }
}
