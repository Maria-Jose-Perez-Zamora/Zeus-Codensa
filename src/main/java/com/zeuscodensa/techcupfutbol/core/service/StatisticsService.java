package com.zeuscodensa.techcupfutbol.core.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zeuscodensa.techcupfutbol.core.model.Match;

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
                .map(p -> buildHistoryEntry(p, teamName))
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildHistoryEntry(Match match, String teamName) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", match.getId());
        info.put("opponent", getOpponent(match, teamName));
        info.put("venue", getVenue(match, teamName));
        info.put("homeScore", match.getHomeScore());
        info.put("awayScore", match.getAwayScore());
        info.put("status", match.getStatus());
        info.put("date", match.getMatchDate());
        info.put("result", getResult(match, teamName));
        return info;
    }

    private String getOpponent(Match match, String teamName) {
        return match.getHomeTeam().equals(teamName) ? match.getAwayTeam() : match.getHomeTeam();
    }

    private String getVenue(Match match, String teamName) {
        return match.getHomeTeam().equals(teamName) ? "HOME" : "AWAY";
    }

    private String getResult(Match match, String teamName) {
        if (!"FINISHED".equals(match.getStatus())) {
            return "PENDING";
        }

        int teamGoals = match.getHomeTeam().equals(teamName) ? match.getHomeScore() : match.getAwayScore();
        int opponentGoals = match.getHomeTeam().equals(teamName) ? match.getAwayScore() : match.getHomeScore();

        if (teamGoals > opponentGoals) {
            return "WIN";
        }
        if (teamGoals == opponentGoals) {
            return "DRAW";
        }
        return "LOSS";
    }
}
