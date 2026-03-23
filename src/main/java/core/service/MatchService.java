package core.service;

import dependencies.dto.MatchRequestDTO;
import dependencies.dto.MatchResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Match;
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
        log.debug("Ejecutando validaciones para creacion de match");
        MatchValidator.validateForCreation(request);

        Match nuevoPartido = MatchMapper.toEntity(request);

        DataStorage.matches.add(nuevoPartido);
        log.info("Match registrado exitosamente local {} vs {} visitante (Tournament {})", 
                 request.getHomeTeam(), request.getAwayTeam(), request.getTournamentName());
        return MatchMapper.toDTO(nuevoPartido);
    }

    public MatchResponseDTO actualizarMarcador(String id, Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null || homeScore < 0 || awayScore < 0) {
            log.error("Violación funcional: Marcadores negativos o nulos recibidos");
            throw new BusinessRuleException("Marcadores invalidos");
        }

        Match match = findOrThrow(id);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("FINISHED");

        log.info("Marcador actualizado para el ID {} | {} - {}", id, homeScore, awayScore);
        return MatchMapper.toDTO(match);
    }

    public MatchResponseDTO registrarAlineacion(String id, String nombreEquipo, List<String> players) {
        if (players == null || players.isEmpty()) {
            throw new BusinessRuleException("La alineación no puede estar vacía");
        }

        Match match = findOrThrow(id);

        if (!match.getHomeTeam().equals(nombreEquipo) && !match.getAwayTeam().equals(nombreEquipo)) {
            log.warn("El team {} no participa en este match", nombreEquipo);
            throw new BusinessRuleException("El team especificado no participa en este match");
        }

        match.getAlineaciones().put(nombreEquipo, players);
        log.info("Alineación registrada exitosamente de {} players para el team {}", players.size(), nombreEquipo);
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
        log.debug("Tarjetas amarillas/rojas guardadas para el ID {}", id);
        return MatchMapper.toDTO(match);
    }

    public List<MatchResponseDTO> getPartidosPorArbitro(String correoArbitro) {
        return DataStorage.matches.stream()
                .filter(p -> correoArbitro.equals(p.getCorreoArbitro()))
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MatchResponseDTO asignarArbitro(String id, String correoArbitro) {
        boolean esArbitro = DataStorage.users.stream()
                .anyMatch(u -> u.getCorreo().equals(correoArbitro)
                        && u.getRole() != null
                        && u.getRole().name().equals("REFEREE"));
        if (!esArbitro) {
            log.error("Asignacion fallida: {} carece del rol REFEREE", correoArbitro);
            throw new BusinessRuleException("El correo especificado no corresponde a un árbitro registrado");
        }

        Match match = findOrThrow(id);
        match.setCorreoArbitro(correoArbitro);
        log.info("Arbitro {} asignado exitosamente al Match ID {}", correoArbitro, id);
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
                .orElseThrow(() -> new ResourceNotFoundException("Match no encontrado con su Identificador Base"));
    }
}
