package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamValidator {

    private final ITeamRepository teamRepository;

    public TeamValidator(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void validateForCreation(String teamName, List<String> players) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("El name del team no puede estar vacío");
        }

        boolean exists = teamRepository.findByTeamName(teamName).isPresent();
        if (exists) {
            throw new IllegalArgumentException("El name del team ya existe");
        }

        // Players can be empty at creation time — captains add them separately via InvitePlayers.
        // The 7-player minimum is enforced at tournament inscription time (InscripcionValidator).
        if (players != null && !players.isEmpty()) {
            if (players.size() > 20) {
                throw new IllegalArgumentException("Un team no puede tener más de 20 players");
            }

            long distinctCount = players.stream().distinct().count();
            if (distinctCount < players.size()) {
                throw new IllegalArgumentException("Existen correos duplicados en la solicitud del team");
            }

            for (String correo : players) {
                if (teamRepository.existsByPlayersEmail(correo)) {
                    throw new IllegalArgumentException("El player " + correo + " ya pertenece a otro team");
                }
            }
        }
    }
}
