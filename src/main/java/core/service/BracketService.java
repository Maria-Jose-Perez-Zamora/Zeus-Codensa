package core.service;

import core.model.KnockoutBracket;
import core.model.Registration;
import core.service.strategy.BracketGenerationStrategy;
import core.service.strategy.RandomDrawStrategy;
import dependencies.util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BracketService {
    private BracketGenerationStrategy strategy;

    public BracketService() {
        this.strategy = new RandomDrawStrategy();
    }

    public void setStrategy(BracketGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<KnockoutBracket> generarLlaves(String tournamentName, String phase) {
        List<String> approvedTeams = DataStorage.registrations.stream()
                .filter(i -> i.getTournamentName().equals(tournamentName) && i.getStatus().equals("APPROVED"))
                .map(Registration::getTeamName)
                .collect(Collectors.toList());

        if (approvedTeams.isEmpty() || approvedTeams.size() % 2 != 0) {
            throw new IllegalArgumentException("An even number of approved teams is required to generate brackets.");
        }
        
        return strategy.generarLlaves(approvedTeams, phase);
    }
}
