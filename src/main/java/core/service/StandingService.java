package core.service;

import core.model.Match;
import core.model.Standing;
import dependencies.util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StandingService {

    public List<Standing> calcularTabla(String tournamentName) {
        Map<String, Standing> standingTable = new HashMap<>();

        List<Match> matches = DataStorage.matches.stream()
                .filter(p -> p.getTournamentName().equals(tournamentName) && p.getStatus().equals("FINISHED"))
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
