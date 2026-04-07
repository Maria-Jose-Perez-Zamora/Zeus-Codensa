package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TournamentRepository;
import org.springframework.stereotype.Component;

@Component
public class MatchValidator {

    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    public MatchValidator(TournamentRepository tournamentRepository, RegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    public void validateForCreation(MatchRequestDTO request) {
        if (request.getHomeTeam() == null || request.getAwayTeam() == null) {
            throw new IllegalArgumentException("Se deben especificar ambos teams");
        }
        if (request.getHomeTeam().equals(request.getAwayTeam())) {
            throw new IllegalArgumentException("Un team no puede jugar contra si mismo");
        }
        if (request.getTournamentName() == null || request.getMatchDate() == null) {
            throw new IllegalArgumentException("El tournament y la fecha del match son obligatorios");
        }

        boolean torneoExiste = tournamentRepository.findByTournamentName(request.getTournamentName()).isPresent();
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament especificado no existe");
        }

        boolean localInscrito = registrationRepository.existsByTeamNameAndTournamentNameAndStatus(request.getHomeTeam(), request.getTournamentName(), "APPROVED");
        boolean visitanteInscrito = registrationRepository.existsByTeamNameAndTournamentNameAndStatus(request.getAwayTeam(), request.getTournamentName(), "APPROVED");

        if (!localInscrito || !visitanteInscrito) {
            throw new IllegalArgumentException("Los teams deben estar inscritos y APPROVED en el tournament");
        }
    }
}
