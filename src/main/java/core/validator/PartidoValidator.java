package core.validator;

import dependencias.dto.PartidoRequestDTO;
import dependencias.util.DataStorage;

public class PartidoValidator {

    public static void validateForCreation(PartidoRequestDTO request) {
        if (request.getEquipoLocal() == null || request.getEquipoVisitante() == null) {
            throw new IllegalArgumentException("Se deben especificar ambos equipos");
        }
        if (request.getEquipoLocal().equals(request.getEquipoVisitante())) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra si mismo");
        }
        if (request.getNombreTorneo() == null || request.getFechaPartido() == null) {
            throw new IllegalArgumentException("El torneo y la fecha del partido son obligatorios");
        }

        boolean torneoExiste = DataStorage.torneos.stream()
                .anyMatch(t -> t.getNombreTorneo().equals(request.getNombreTorneo()));
        if (!torneoExiste) {
            throw new IllegalArgumentException("El torneo especificado no existe");
        }

        boolean localInscrito = DataStorage.inscripciones.stream()
                .anyMatch(i -> i.getNombreEquipo().equals(request.getEquipoLocal()) && i.getNombreTorneo().equals(request.getNombreTorneo()) && i.getEstado().equals("APROBADO"));
        boolean visitanteInscrito = DataStorage.inscripciones.stream()
                .anyMatch(i -> i.getNombreEquipo().equals(request.getEquipoVisitante()) && i.getNombreTorneo().equals(request.getNombreTorneo()) && i.getEstado().equals("APROBADO"));

        if (!localInscrito || !visitanteInscrito) {
            throw new IllegalArgumentException("Los equipos deben estar inscritos y APROBADOS en el torneo");
        }
    }
}
