package core.validator;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.util.DataStorage;

public class RegistrationValidator {

    public static void validateForInscripcion(RegistrationRequestDTO request) {
        if (request.getNombreEquipo() == null || request.getNombreEquipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de team es obligatorio");
        }
        if (request.getTournamentName() == null || request.getTournamentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de tournament es obligatorio");
        }
        if (request.getComprobantePagoUrl() == null || request.getComprobantePagoUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de pago es obligatorio");
        }

        boolean equipoExiste = DataStorage.teams.stream()
                .anyMatch(t -> t.getNombreEquipo().equals(request.getNombreEquipo()));
        if (!equipoExiste) {
            throw new IllegalArgumentException("El team especificado no existe");
        }

        boolean torneoExiste = DataStorage.tournaments.stream()
                .anyMatch(t -> t.getTournamentName().equals(request.getTournamentName()) && t.getStatus().equals("OPEN"));
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament no existe o no se encuentra OPEN para registrations");
        }

        boolean estaInscrito = DataStorage.registrations.stream()
                .anyMatch(i -> i.getNombreEquipo().equals(request.getNombreEquipo()) && i.getTournamentName().equals(request.getTournamentName()));
        if (estaInscrito) {
            throw new IllegalArgumentException("El team ya cuenta con un proceso de registration para este tournament");
        }
    }
}
