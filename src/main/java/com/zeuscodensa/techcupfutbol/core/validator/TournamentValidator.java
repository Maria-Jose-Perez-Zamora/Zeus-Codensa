package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.springframework.stereotype.Component;

@Component
public class TournamentValidator {
    
    private final ITournamentRepository tournamentRepository;

    public TournamentValidator(ITournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public void validateForCreation(Tournament tournament) {
        if (tournament.getTournamentName() == null || tournament.getTournamentName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name del tournament es obligatorio");
        }
        if (tournament.getNumeroEquipos() == null || tournament.getNumeroEquipos() <= 1) {
            throw new IllegalArgumentException("Debe haber por lo menos 2 teams permitidos");
        }
        if (tournament.getCostoInscripcion() == null || tournament.getCostoInscripcion() < 0) {
            throw new IllegalArgumentException("El costo de registration no puede ser negativo");
        }

        boolean exists = tournamentRepository.findByTournamentName(tournament.getTournamentName()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Ya existe un tournament con este name");
        }
    }
}
