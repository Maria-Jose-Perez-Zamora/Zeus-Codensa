package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Standing;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository matchRepository;

    public StatisticsService(com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * RF-008: Máximos goleadores del tournament.
     * Retorna lista ordenada de { correoJugador, goals } descendente.
     */
    public List<Map<String, Object>> getTopScorers(String tournamentName) {
        Map<String, Integer> totales = new HashMap<>();

        matchRepository.findByTournamentName(tournamentName).stream()
                .filter(p -> "FINISHED".equals(p.getStatus()))
                .forEach(p -> p.getGoles().forEach((player, g) ->
                        totales.merge(player, g, Integer::sum)));

        return totales.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("playerEmail", e.getKey());
                    entry.put("goals", e.getValue());
                    return entry;
                })
                .collect(Collectors.toList());
    }

    /**
     * RF-008: Historial de matches de un team (todos sus matches con resultado).
     */
    public List<Map<String, Object>> getTeamHistory(String tournamentName, String teamName) {
        return matchRepository.findByTournamentName(tournamentName).stream()
                .filter(p -> p.getHomeTeam().equals(teamName) || p.getAwayTeam().equals(teamName))
                .map(p -> {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("id", p.getId());
                    info.put("opponent", p.getHomeTeam().equals(teamName) ? p.getAwayTeam() : p.getHomeTeam());
                    info.put("venue", p.getHomeTeam().equals(teamName) ? "HOME" : "AWAY");
                    info.put("homeScore", p.getHomeScore());
                    info.put("awayScore", p.getAwayScore());
                    info.put("status", p.getStatus());
                    info.put("date", p.getMatchDate());

                    String result = "PENDING";
                    if ("FINISHED".equals(p.getStatus())) {
                        int teamGoals = p.getHomeTeam().equals(teamName) ? p.getHomeScore() : p.getAwayScore();
                        int opponentGoals = p.getHomeTeam().equals(teamName) ? p.getAwayScore() : p.getHomeScore();
                        result = teamGoals > opponentGoals ? "WIN" : teamGoals == opponentGoals ? "DRAW" : "LOSS";
                    }
                    info.put("result", result);
                    return info;
                })
                .collect(Collectors.toList());
    }
}
