package core.validator;

import dependencias.dto.TorneoRequestDTO;
import dependencias.util.DataStorage;

public class TorneoValidator {
    public static void validateForCreation(TorneoRequestDTO request) {
        if (request.getNombreTorneo() == null || request.getNombreTorneo().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio");
        }
        if (request.getNumeroEquipos() == null || request.getNumeroEquipos() <= 1) {
            throw new IllegalArgumentException("Debe haber por lo menos 2 equipos permitidos");
        }
        if (request.getCostoInscripcion() == null || request.getCostoInscripcion() < 0) {
            throw new IllegalArgumentException("El costo de inscripcion no puede ser negativo");
        }

        boolean exists = DataStorage.torneos.stream()
                .anyMatch(t -> t.getNombreTorneo().equals(request.getNombreTorneo()));
        if (exists) {
            throw new IllegalArgumentException("Ya existe un torneo con este nombre");
        }
    }
}
