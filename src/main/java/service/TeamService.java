package service;

import dto.TeamRequestDTO;
import dto.TeamResponseDTO;
import model.Team;
import model.User;
import util.DataStorage;
import validator.TeamValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        TeamValidator.validateForCreation(requestDTO);

        Team newTeam = new Team(requestDTO.getNombreEquipo());
        newTeam.setEscudo(requestDTO.getEscudo());
        newTeam.setColoresUniforme(requestDTO.getColoresUniforme());
        
        List<User> foundUsers = new ArrayList<>();
        if (requestDTO.getJugadorCorreos() != null) {
            for (String correo : requestDTO.getJugadorCorreos()) {
                DataStorage.users.stream()
                    .filter(u -> u.getCorreo().equals(correo))
                    .findFirst()
                    .ifPresent(foundUsers::add);
            }
        }
        newTeam.setJugadores(foundUsers);

        DataStorage.teams.add(newTeam);
        return new TeamResponseDTO(newTeam);
    }

    public List<TeamResponseDTO> getAllTeams() {
        return DataStorage.teams.stream()
                .map(TeamResponseDTO::new)
                .collect(Collectors.toList());
    }
}
