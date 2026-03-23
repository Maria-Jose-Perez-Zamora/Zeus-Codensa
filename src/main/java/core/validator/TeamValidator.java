package core.validator;

import dependencies.dto.TeamRequestDTO;
import dependencies.util.DataStorage;

import java.util.List;

public class TeamValidator {

    public static void validateForCreation(TeamRequestDTO request) {
        if (request.getNombreEquipo() == null || request.getNombreEquipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El name del team no puede estar vacío");
        }
        
        boolean exists = DataStorage.teams.stream()
                .anyMatch(t -> t.getNombreEquipo().equals(request.getNombreEquipo()));
        if (exists) {
            throw new IllegalArgumentException("El name del team ya existe");
        }

        List<String> players = request.getJugadorCorreos();
        if (players == null || players.size() < 7 || players.size() > 20) {
            throw new IllegalArgumentException("Un team debe tener entre 7 y 20 players inscritos inicialmente");
        }

        long distinctCount = players.stream().distinct().count();
        if (distinctCount < players.size()) {
            throw new IllegalArgumentException("Existen correos duplicados en la solicitud del team");
        }

        for (String correo : players) {
            boolean yaEnEquipo = DataStorage.teams.stream()
                    .anyMatch(t -> t.getJugadores().stream()
                            .anyMatch(u -> u.getCorreo().equals(correo)));
            if (yaEnEquipo) {
                throw new IllegalArgumentException("El player " + correo + " ya pertenece a otro team");
            }
        }
    }
}
