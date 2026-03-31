package core.service;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.Team;
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
    private final TeamValidator teamValidator;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, TeamValidator teamValidator) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamValidator = teamValidator;
    }

    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        log.debug("Ejecutando validaciones para creacion de team: {}", requestDTO.getTeamName());
        teamValidator.validateForCreation(requestDTO);

        Team newTeam = TeamMapper.toEntity(requestDTO);

        try {
            if (teamRepository.findByTeamName(newTeam.getTeamName()).isPresent()) {
                throw new BusinessRuleException("Ya existe un equipo con ese nombre");
            }

            List<UserEntity> foundPlayers = new ArrayList<>();
            if (requestDTO.getPlayerEmails() != null) {
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

    public List<TeamResponseDTO> getAllTeams() {
        try {
            return teamRepository.findAll().stream()
                    .map(EntityToModelMapper::toTeamModel)
                    .map(TeamMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al consultar equipos en base de datos", ex);
        }
    }
}
