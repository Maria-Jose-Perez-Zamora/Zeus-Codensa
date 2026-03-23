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
        List<String> equiposAprobados = DataStorage.registrations.stream()
                .filter(i -> i.getTournamentName().equals(tournamentName) && i.getStatus().equals("APROBADO"))
                .map(Registration::getNombreEquipo)
                .collect(Collectors.toList());

        if (equiposAprobados.isEmpty() || equiposAprobados.size() % 2 != 0) {
            throw new IllegalArgumentException("Para generar llaves se requiere una cantidad par de teams aprobados en el tournament.");
        }
        
        return strategy.generarLlaves(equiposAprobados, phase);
    }
}
