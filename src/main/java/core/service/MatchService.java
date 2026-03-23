package core.service;

import dependencies.dto.MatchRequestDTO;
import dependencies.dto.MatchResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Match;
import dependencies.util.DataStorage;
import core.validator.MatchValidator;
import dependencies.mapper.MatchMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    public MatchResponseDTO registrarPartido(MatchRequestDTO request) {
        log.debug("Running validations for match creation");
        MatchValidator.validateForCreation(request);

        Match nuevoPartido = MatchMapper.toEntity(request);

        DataStorage.matches.add(nuevoPartido);
        log.info("Match successfully registered: home {} vs away {} (Tournament {})", 
                 request.getHomeTeam(), request.getAwayTeam(), request.getTournamentName());
        return MatchMapper.toDTO(nuevoPartido);
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
        log.debug("Yellow/red cards saved for ID {}", id);
        return MatchMapper.toDTO(match);
    }

    public List<MatchResponseDTO> getMatchesByReferee(String refereeEmail) {
        return DataStorage.matches.stream()
                .filter(p -> refereeEmail.equals(p.getRefereeEmail()))
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MatchResponseDTO asignarArbitro(String id, String refereeEmail) {
        boolean esArbitro = DataStorage.users.stream()
                .anyMatch(u -> u.getEmail().equals(refereeEmail)
                        && u.getRole() != null
                        && u.getRole().name().equals("REFEREE"));
        if (!esArbitro) {
            log.error("Assignment failed: {} lacks the REFEREE role", refereeEmail);
            throw new BusinessRuleException("The specified email does not correspond to a registered referee");
        }

        Match match = findOrThrow(id);
        match.setRefereeEmail(refereeEmail);
        log.info("Referee {} successfully assigned to Match ID {}", refereeEmail, id);
        return MatchMapper.toDTO(match);
    }

    public List<MatchResponseDTO> getAll() {
        return DataStorage.matches.stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Match findOrThrow(String id) {
        return DataStorage.matches.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with its Base Identifier"));
    }
}
