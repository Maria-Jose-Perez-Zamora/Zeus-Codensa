package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<Team> getAllTeams(String nameFilter, Pageable pageable) {
        try {
            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                return teamRepository.findByTeamNameContainingIgnoreCase(nameFilter.trim(), pageable);
            }
            return teamRepository.findAll(pageable);
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar equipos con paginacion y filtros", ex);
        }
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team no encontrado con id: " + id));
    }

    public Team updateTeam(Long id, Team updatedTeam) {
        Team existing = getTeamById(id);
        existing.setTeamName(updatedTeam.getTeamName());
        existing.setEscudo(updatedTeam.getEscudo());
        existing.setColoresUniforme(updatedTeam.getColoresUniforme());
        try {
            return teamRepository.save(existing);
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al actualizar equipo", ex);
        }
    }

    public void deleteTeam(Long id) {
        getTeamById(id); // Valida que exista
        try {
            teamRepository.deleteById(id);
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al eliminar equipo", ex);
        }
    }
}
