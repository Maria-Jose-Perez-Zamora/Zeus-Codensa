package core.validator;

import dependencies.dto.MatchRequestDTO;
import dependencies.util.DataStorage;

public class MatchValidator {

    public static void validateForCreation(MatchRequestDTO request) {
        if (request.getHomeTeam() == null || request.getAwayTeam() == null) {
            throw new IllegalArgumentException("Se deben especificar ambos teams");
        }
        if (request.getHomeTeam().equals(request.getAwayTeam())) {
            throw new IllegalArgumentException("Un team no puede jugar contra si mismo");
        }
        if (request.getTournamentName() == null || request.getMatchDate() == null) {
            throw new IllegalArgumentException("El tournament y la fecha del match son obligatorios");
        }

        boolean torneoExiste = DataStorage.tournaments.stream()
                .anyMatch(t -> t.getTournamentName().equals(request.getTournamentName()));
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament especificado no existe");
        }

        boolean localInscrito = DataStorage.registrations.stream()
                .anyMatch(i -> i.getTeamName().equals(request.getHomeTeam()) && i.getTournamentName().equals(request.getTournamentName()) && i.getStatus().equals("APPROVED"));
        boolean visitanteInscrito = DataStorage.registrations.stream()
                .anyMatch(i -> i.getTeamName().equals(request.getAwayTeam()) && i.getTournamentName().equals(request.getTournamentName()) && i.getStatus().equals("APPROVED"));

        if (!localInscrito || !visitanteInscrito) {
            throw new IllegalArgumentException("Los teams deben estar inscritos y APPROVED en el tournament");
        }
    }
}
