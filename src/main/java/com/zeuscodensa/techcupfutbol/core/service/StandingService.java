package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Standing;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StandingService {

    private final com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository matchRepository;

    public StandingService(com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Standing> calcularTabla(String tournamentName) {
        Map<String, Standing> standingTable = new HashMap<>();

        List<Match> matches = matchRepository.findByTournamentName(tournamentName).stream()
                .filter(p -> p.getStatus().equals("FINISHED"))
                .collect(Collectors.toList());

        for (Match p : matches) {
            standingTable.putIfAbsent(p.getHomeTeam(), new Standing(p.getHomeTeam()));
            standingTable.putIfAbsent(p.getAwayTeam(), new Standing(p.getAwayTeam()));

            standingTable.get(p.getHomeTeam()).registrarPartido(p.getHomeScore(), p.getAwayScore());
            standingTable.get(p.getAwayTeam()).registrarPartido(p.getAwayScore(), p.getHomeScore());
        }

        return standingTable.values().stream()
                .sorted((t1, t2) -> {
                    if (t1.getPoints() != t2.getPoints()) return Integer.compare(t2.getPoints(), t1.getPoints());
                    return Integer.compare(t2.getGoalDifference(), t1.getGoalDifference());
                })
                .collect(Collectors.toList());
    }
}
