package validator;

import dto.TeamRequestDTO;
import util.DataStorage;

public class TeamValidator {

    public static void validateForCreation(TeamRequestDTO request) {
        if (request.getNombreEquipo() == null || request.getNombreEquipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        
        boolean exists = DataStorage.teams.stream()
                .anyMatch(t -> t.getNombreEquipo().equals(request.getNombreEquipo()));
        if (exists) {
            throw new IllegalArgumentException("El nombre del equipo ya existe");
        }
    }
}
