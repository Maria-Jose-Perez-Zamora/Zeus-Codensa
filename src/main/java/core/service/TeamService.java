package core.service;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.Team;
import core.model.User;
import dependencies.util.DataStorage;
import core.validator.TeamValidator;
import dependencies.mapper.TeamMapper;
import dependencies.persistence.entity.TeamEntity;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.mapper.EntityToModelMapper;
import dependencies.persistence.mapper.ModelToEntityMapper;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService() {
        this.teamRepository = null;
        this.userRepository = null;
    }

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        log.debug("Ejecutando validaciones para creacion de team: {}", requestDTO.getTeamName());
        TeamValidator.validateForCreation(requestDTO);

        Team newTeam = TeamMapper.toEntity(requestDTO);

        if (teamRepository != null) {
            try {
                if (teamRepository.findByTeamName(newTeam.getTeamName()).isPresent()) {
                    throw new BusinessRuleException("Ya existe un equipo con ese nombre");
                }

                List<UserEntity> foundPlayers = new ArrayList<>();
                if (requestDTO.getPlayerEmails() != null && userRepository != null) {
                    for (String correo : requestDTO.getPlayerEmails()) {
                        userRepository.findByEmail(correo).ifPresent(foundPlayers::add);
                    }
                }

                TeamEntity entity = ModelToEntityMapper.toTeamEntity(newTeam);
                entity.setPlayers(foundPlayers);
                TeamEntity saved = teamRepository.save(entity);
                log.info("Equipo {} registrado exitosamente en DB", newTeam.getTeamName());
                return TeamMapper.toDTO(EntityToModelMapper.toTeamModel(saved));
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al crear equipo en base de datos", ex);
            }
        }
        
        List<User> foundUsers = new ArrayList<>();
        if (requestDTO.getPlayerEmails() != null) {
            for (String correo : requestDTO.getPlayerEmails()) {
                DataStorage.users.stream()
                    .filter(u -> u.getEmail().equals(correo))
                    .findFirst()
                    .ifPresent(foundUsers::add);
            }
            log.debug("Se asociaron {} players al team", foundUsers.size());
        }
        newTeam.setPlayers(foundUsers);

        DataStorage.teams.add(newTeam);
        log.info("Equipo {} registrado exitosamente en memoria", newTeam.getTeamName());
        return TeamMapper.toDTO(newTeam);
    }

    public List<TeamResponseDTO> getAllTeams() {
        if (teamRepository != null) {
            try {
                return teamRepository.findAll().stream()
                        .map(EntityToModelMapper::toTeamModel)
                        .map(TeamMapper::toDTO)
                        .collect(Collectors.toList());
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar equipos en base de datos", ex);
            }
        }

        return DataStorage.teams.stream()
                .map(TeamMapper::toDTO)
                .collect(Collectors.toList());
    }
}
