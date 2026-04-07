package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamValidator {

    private final TeamRepository teamRepository;

    public TeamValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void validateForCreation(TeamRequestDTO request) {
        if (request.getTeamName() == null || request.getTeamName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name del team no puede estar vacío");
        }
        
        boolean exists = teamRepository.findByTeamName(request.getTeamName()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("El name del team ya existe");
        }

        List<String> players = request.getPlayerEmails();
        if (players == null || players.size() < 7 || players.size() > 20) {
            throw new IllegalArgumentException("Un team debe tener entre 7 y 20 players inscritos inicialmente");
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
