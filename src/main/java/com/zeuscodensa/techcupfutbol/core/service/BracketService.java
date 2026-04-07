package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.KnockoutBracket;
import com.zeuscodensa.techcupfutbol.core.service.strategy.BracketGenerationStrategy;
import com.zeuscodensa.techcupfutbol.core.service.strategy.RandomDrawStrategy;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BracketService {
    private BracketGenerationStrategy strategy;
    private final RegistrationRepository registrationRepository;

    public BracketService(RegistrationRepository registrationRepository) {
        this.strategy = new RandomDrawStrategy();
        this.registrationRepository = registrationRepository;
    }

    public void setStrategy(BracketGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<KnockoutBracket> generarLlaves(String tournamentName, String phase) {
        List<String> approvedTeams = registrationRepository.findByTournamentName(tournamentName).stream()
                .filter(i -> i.getStatus().equals("APPROVED"))
                .map(com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity::getTeamName)
                .collect(Collectors.toList());

        if (approvedTeams.isEmpty() || approvedTeams.size() % 2 != 0) {
            throw new IllegalArgumentException("An even number of approved teams is required to generate brackets.");
        }
        
        return strategy.generarLlaves(approvedTeams, phase);
    }
}
