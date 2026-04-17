package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.validator.TeamValidator;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);
    private final ITeamRepository teamRepository;
    private final IUserRepository userRepository;
    private final TeamValidator teamValidator;

    @Autowired
    public TeamService(ITeamRepository teamRepository, IUserRepository userRepository, TeamValidator teamValidator) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamValidator = teamValidator;
    }

    public Team createTeam(Team newTeam, List<String> playerEmails) {
        log.debug("Ejecutando validaciones para creacion de team: {}", newTeam.getTeamName());
        teamValidator.validateForCreation(newTeam.getTeamName(), playerEmails);

        try {
            if (teamRepository.findByTeamName(newTeam.getTeamName()).isPresent()) {
                throw new BusinessRuleException("Ya existe un equipo con ese nombre");
            }

            List<User> foundPlayers = new ArrayList<>();
            if (newTeam.getCaptainEmail() != null) {
                userRepository.findByEmail(newTeam.getCaptainEmail()).ifPresent(foundPlayers::add);
            }
            if (playerEmails != null) {
                for (String correo : playerEmails) {
                    userRepository.findByEmail(correo).ifPresent(foundPlayers::add);
                }
            }
            newTeam.setPlayers(foundPlayers);

            Team saved = teamRepository.save(newTeam);
            log.info("Equipo {} registrado exitosamente en DB", newTeam.getTeamName());
            return saved;
        } catch (Exception ex) {
            if (ex instanceof BusinessRuleException) {
                throw (BusinessRuleException) ex;
            }
            throw new PersistenceAccessException("Error al crear equipo en base de datos", ex);
        }
    }

    public List<Team> getAllTeams() {
        try {
            return teamRepository.findAll();
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar equipos en base de datos", ex);
        }
    }
}
