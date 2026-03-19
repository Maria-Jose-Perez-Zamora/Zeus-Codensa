package validator;

import dto.InscripcionRequestDTO;
import util.DataStorage;

public class InscripcionValidator {

    public static void validateForInscripcion(InscripcionRequestDTO request) {
        if (request.getNombreEquipo() == null || request.getNombreEquipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de equipo es obligatorio");
        }
        if (request.getNombreTorneo() == null || request.getNombreTorneo().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de torneo es obligatorio");
        }
        if (request.getComprobantePagoUrl() == null || request.getComprobantePagoUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de pago es obligatorio");
        }

        boolean equipoExiste = DataStorage.teams.stream()
                .anyMatch(t -> t.getNombreEquipo().equals(request.getNombreEquipo()));
        if (!equipoExiste) {
            throw new IllegalArgumentException("El equipo especificado no existe");
        }

        boolean torneoExiste = DataStorage.torneos.stream()
                .anyMatch(t -> t.getNombreTorneo().equals(request.getNombreTorneo()) && t.getEstado().equals("ABIERTO"));
        if (!torneoExiste) {
            throw new IllegalArgumentException("El torneo no existe o no se encuentra ABIERTO para inscripciones");
        }

        boolean estaInscrito = DataStorage.inscripciones.stream()
                .anyMatch(i -> i.getNombreEquipo().equals(request.getNombreEquipo()) && i.getNombreTorneo().equals(request.getNombreTorneo()));
        if (estaInscrito) {
            throw new IllegalArgumentException("El equipo ya cuenta con un proceso de inscripcion para este torneo");
        }
    }
}
