package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.springframework.stereotype.Component;

@Component
public class MatchValidator {

    private final ITournamentRepository tournamentRepository;
    private final IRegistrationRepository registrationRepository;

    public MatchValidator(ITournamentRepository tournamentRepository, IRegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    public void validateForCreation(Match match) {
        if (match.getHomeTeam() == null || match.getAwayTeam() == null) {
            throw new IllegalArgumentException("Se deben especificar ambos teams");
        }
        if (match.getHomeTeam().equals(match.getAwayTeam())) {
            throw new IllegalArgumentException("Un team no puede jugar contra si mismo");
        }
        if (match.getTournamentName() == null || match.getMatchDate() == null) {
            throw new IllegalArgumentException("El tournament y la fecha del match son obligatorios");
        }

        boolean torneoExiste = tournamentRepository.findByTournamentName(match.getTournamentName()).isPresent();
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament especificado no existe");
        }

        boolean localInscrito = registrationRepository.existsByTeamNameAndTournamentNameAndStatus(match.getHomeTeam(), match.getTournamentName(), "APPROVED");
        boolean visitanteInscrito = registrationRepository.existsByTeamNameAndTournamentNameAndStatus(match.getAwayTeam(), match.getTournamentName(), "APPROVED");

        if (!localInscrito || !visitanteInscrito) {
            throw new IllegalArgumentException("Los teams deben estar inscritos y APPROVED en el tournament");
        }
    }
}
