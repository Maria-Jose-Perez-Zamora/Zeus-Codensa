package core.validator;

import dependencies.dto.TournamentRequestDTO;
import dependencies.util.DataStorage;

public class TournamentValidator {
    public static void validateForCreation(TournamentRequestDTO request) {
        if (request.getTournamentName() == null || request.getTournamentName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name del tournament es obligatorio");
        }
        if (request.getNumeroEquipos() == null || request.getNumeroEquipos() <= 1) {
            throw new IllegalArgumentException("Debe haber por lo menos 2 teams permitidos");
        }
        if (request.getCostoInscripcion() == null || request.getCostoInscripcion() < 0) {
            throw new IllegalArgumentException("El costo de registration no puede ser negativo");
        }

        boolean exists = DataStorage.tournaments.stream()
                .anyMatch(t -> t.getTournamentName().equals(request.getTournamentName()));
        if (exists) {
            throw new IllegalArgumentException("Ya existe un tournament con este name");
        }
    }
}
