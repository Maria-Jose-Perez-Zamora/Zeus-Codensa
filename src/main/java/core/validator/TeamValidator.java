package core.validator;

import dependencias.dto.TeamRequestDTO;
import dependencias.util.DataStorage;

import java.util.List;

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

        List<String> jugadores = request.getJugadorCorreos();
        if (jugadores == null || jugadores.size() < 7 || jugadores.size() > 20) {
            throw new IllegalArgumentException("Un equipo debe tener entre 7 y 20 jugadores inscritos inicialmente");
        }

        long distinctCount = jugadores.stream().distinct().count();
        if (distinctCount < jugadores.size()) {
            throw new IllegalArgumentException("Existen correos duplicados en la solicitud del equipo");
        }

        for (String correo : jugadores) {
            boolean yaEnEquipo = DataStorage.teams.stream()
                    .anyMatch(t -> t.getJugadores().stream()
                            .anyMatch(u -> u.getCorreo().equals(correo)));
            if (yaEnEquipo) {
                throw new IllegalArgumentException("El jugador " + correo + " ya pertenece a otro equipo");
            }
        }
    }
}
