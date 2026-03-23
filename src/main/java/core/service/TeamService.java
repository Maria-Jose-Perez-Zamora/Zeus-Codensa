package core.service;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.model.Team;
import core.model.User;
import core.model.User;
import dependencies.util.DataStorage;
import core.validator.TeamValidator;
import dependencies.mapper.TeamMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        log.debug("Ejecutando validaciones para creacion de team: {}", requestDTO.getNombreEquipo());
        TeamValidator.validateForCreation(requestDTO);

        Team newTeam = TeamMapper.toEntity(requestDTO);
        
        List<User> foundUsers = new ArrayList<>();
        if (requestDTO.getJugadorCorreos() != null) {
            for (String correo : requestDTO.getJugadorCorreos()) {
                DataStorage.users.stream()
                    .filter(u -> u.getCorreo().equals(correo))
                    .findFirst()
                    .ifPresent(foundUsers::add);
            }
            log.debug("Se asociaron {} players al team", foundUsers.size());
        }
        newTeam.setJugadores(foundUsers);

        DataStorage.teams.add(newTeam);
        log.info("Equipo {} registrado exitosamente en memoria", newTeam.getNombreEquipo());
        return TeamMapper.toDTO(newTeam);
    }

    public List<TeamResponseDTO> getAllTeams() {
        return DataStorage.teams.stream()
                .map(TeamMapper::toDTO)
                .collect(Collectors.toList());
    }
}
