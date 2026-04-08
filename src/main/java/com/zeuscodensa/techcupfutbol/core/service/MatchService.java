package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.validator.MatchValidator;
import com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);
    
    private final MatchValidator matchValidator;
    private final IMatchRepository matchRepository;
    private final IUserRepository userRepository;

    public MatchService(MatchValidator matchValidator, 
                        IMatchRepository matchRepository,
                        IUserRepository userRepository) {
        this.matchValidator = matchValidator;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    public Match registrarPartido(Match nuevoPartido) {
        log.debug("Running validations for match creation");
        matchValidator.validateForCreation(nuevoPartido);

        Match saved = matchRepository.save(nuevoPartido);
        
        log.info("Match successfully registered: home {} vs away {} (Tournament {})", 
                 nuevoPartido.getHomeTeam(), nuevoPartido.getAwayTeam(), nuevoPartido.getTournamentName());
        return saved;
    }

    public Match actualizarMarcador(String id, Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null || homeScore < 0 || awayScore < 0) {
            log.error("Functional violation: Negative or null scores received");
            throw new BusinessRuleException("Invalid scores");
        }

        Match match = findOrThrow(id);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("FINISHED");

        Match saved = matchRepository.save(match);

        log.info("Score updated for ID {} | {} - {}", id, homeScore, awayScore);
        return saved;
    }

    public Match registrarAlineacion(String id, String teamName, List<String> players) {
        if (players == null || players.isEmpty()) {
            throw new BusinessRuleException("The lineup cannot be empty");
        }

        Match match = findOrThrow(id);

        if (!match.getHomeTeam().equals(teamName) && !match.getAwayTeam().equals(teamName)) {
            log.warn("Team {} does not participate in this match", teamName);
            throw new BusinessRuleException("The specified team does not participate in this match");
        }

        match.getAlineaciones().put(teamName, players);
        
        Match saved = matchRepository.save(match);
        log.info("Lineup successfully registered: {} players for team {}", players.size(), teamName);
        return saved;
    }

    public Match registrarTarjetas(String id, Map<String, List<String>> amarillas, Map<String, List<String>> rojas) {
        Match match = findOrThrow(id);
        if (amarillas != null) {
            match.setYellowCards(amarillas);
        }
        if (rojas != null) {
            match.setRedCards(rojas);
        }
        
        Match saved = matchRepository.save(match);
        log.debug("Yellow/red cards saved for ID {}", id);
        return saved;
    }

    public List<Match> getMatchesByReferee(String refereeEmail) {
        return matchRepository.findByRefereeEmail(refereeEmail);
    }

    public Match asignarArbitro(String id, String refereeEmail) {
        boolean esArbitro = userRepository.findByEmail(refereeEmail)
                .map(u -> com.zeuscodensa.techcupfutbol.core.model.Role.REFEREE.equals(u.getRole()))
                .orElse(false);
        if (!esArbitro) {
            log.error("Assignment failed: {} lacks the REFEREE role", refereeEmail);
            throw new BusinessRuleException("The specified email does not correspond to a registered referee");
        }

        Match match = findOrThrow(id);
        match.setRefereeEmail(refereeEmail);
        Match saved = matchRepository.save(match);
        log.info("Referee {} successfully assigned to Match ID {}", refereeEmail, id);
        return saved;
    }

    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    private Match findOrThrow(String id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with its Base Identifier"));
    }
}
