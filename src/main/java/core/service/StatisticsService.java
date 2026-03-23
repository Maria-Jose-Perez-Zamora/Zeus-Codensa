package core.service;

import core.model.Match;
import core.model.Standing;
import dependencies.util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    /**
     * RF-008: Máximos goleadores del tournament.
     * Retorna lista ordenada de { correoJugador, goals } descendente.
     */
    public List<Map<String, Object>> getMaximosGoleadores(String tournamentName) {
        Map<String, Integer> totales = new HashMap<>();

        DataStorage.matches.stream()
                .filter(p -> p.getTournamentName().equals(tournamentName) && "FINISHED".equals(p.getStatus()))
                .forEach(p -> p.getGoles().forEach((player, g) ->
                        totales.merge(player, g, Integer::sum)));

        return totales.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("correoJugador", e.getKey());
                    entry.put("goals", e.getValue());
                    return entry;
                })
                .collect(Collectors.toList());
    }

    /**
     * RF-008: Historial de matches de un team (todos sus matches con resultado).
     */
    public List<Map<String, Object>> getHistorialEquipo(String tournamentName, String nombreEquipo) {
        return DataStorage.matches.stream()
                .filter(p -> p.getTournamentName().equals(tournamentName))
                .filter(p -> p.getHomeTeam().equals(nombreEquipo) || p.getAwayTeam().equals(nombreEquipo))
                .map(p -> {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("id", p.getId());
                    info.put("rival", p.getHomeTeam().equals(nombreEquipo) ? p.getAwayTeam() : p.getHomeTeam());
                    info.put("condicion", p.getHomeTeam().equals(nombreEquipo) ? "LOCAL" : "VISITANTE");
                    info.put("homeScore", p.getHomeScore());
                    info.put("awayScore", p.getAwayScore());
                    info.put("status", p.getStatus());
                    info.put("fecha", p.getMatchDate());

                    String resultado = "PENDIENTE";
                    if ("FINISHED".equals(p.getStatus())) {
                        int golesEquipo = p.getHomeTeam().equals(nombreEquipo) ? p.getHomeScore() : p.getAwayScore();
                        int golesRival = p.getHomeTeam().equals(nombreEquipo) ? p.getAwayScore() : p.getHomeScore();
                        resultado = golesEquipo > golesRival ? "VICTORIA" : golesEquipo == golesRival ? "EMPATE" : "DERROTA";
                    }
                    info.put("resultado", resultado);
                    return info;
                })
                .collect(Collectors.toList());
    }
}
