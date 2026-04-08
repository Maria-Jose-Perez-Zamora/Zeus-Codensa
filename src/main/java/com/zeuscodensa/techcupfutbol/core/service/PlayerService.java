package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IInvitationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final IUserRepository userRepository;
    private final ITeamRepository teamRepository;
    private final IInvitationRepository invitationRepository;

    public PlayerService(IUserRepository userRepository, ITeamRepository teamRepository, IInvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
    }

    public List<User> buscarJugadoresDisponibles(String name, String position) {
        log.debug("Procesando la busqueda de players disponibles en BD. Filtros -> Nombre: {}, Posicion: {}", name, position);
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PLAYER)
                .filter(j -> !estaEnEquipo(j.getEmail()))
                .filter(j -> name == null || name.isBlank() || j.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(j -> {
                    if (position == null || position.isBlank()) return true;
                    if (j instanceof Player p) {
                        return p.getPosition() != null && p.getPosition().toLowerCase().contains(position.toLowerCase());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public Invitation enviarInvitacion(Invitation request) {
        if (request.getPlayerEmail() == null || request.getPlayerEmail().isBlank()) {
            throw new BusinessRuleException("Player email is required");
        }
        if (request.getTeamName() == null || request.getTeamName().isBlank()) {
            throw new BusinessRuleException("Team name is required");
        }

        User targetPlayer = userRepository.findByEmail(request.getPlayerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Player with email " + request.getPlayerEmail() + " does not exist in the system"));

        if (targetPlayer.getRole() != Role.PLAYER) {
            throw new BusinessRuleException("Target user is not a player");
        }

        if (estaEnEquipo(request.getPlayerEmail())) {
            log.warn("Player {} is already signed to another team", request.getPlayerEmail());
            throw new BusinessRuleException("The player already belongs to a team, invitation cannot be sent");
        }

        boolean yaInvitado = invitationRepository.findByPlayerEmail(request.getPlayerEmail()).stream()
                .anyMatch(i -> i.getTeamName().equals(request.getTeamName()) && ("PENDING".equals(i.getStatus()) || "ENVIADA".equals(i.getStatus())));
        
        if (yaInvitado) {
            log.warn("Duplicate invitation skipped for {}", request.getPlayerEmail());
            throw new BusinessRuleException("A pending invitation already exists for this player in this team");
        }

        Invitation newInv = new Invitation();
        newInv.setId(java.util.UUID.randomUUID().toString());
        newInv.setCaptainEmail(request.getCaptainEmail());
        newInv.setPlayerEmail(request.getPlayerEmail());
        newInv.setTeamName(request.getTeamName());
        newInv.setStatus("PENDING");

        log.info("Invitation successfully generated from captain {} to player {} (Team: {})",
                 request.getCaptainEmail(), request.getPlayerEmail(), request.getTeamName());
                 
        return invitationRepository.save(newInv);
    }

    private boolean estaEnEquipo(String email) {
        return teamRepository.findAll().stream()
                .anyMatch(t -> t.getPlayers() != null && t.getPlayers().stream().anyMatch(u -> email.equals(u.getEmail())));
    }

    public void processInvitation(String invitationId, String playerEmail, String status) {
        Invitation inv = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!inv.getPlayerEmail().equals(playerEmail)) {
            throw new BusinessRuleException("El jugador no es el destinatario de esta invitación.");
        }

        if (!"PENDING".equals(inv.getStatus()) && !"ENVIADA".equals(inv.getStatus())) {
            throw new BusinessRuleException("La invitación ya fue respondida o no está pendiente.");
        }
        
        if ("ACEPTADA".equalsIgnoreCase(status) || "ACCEPTED".equalsIgnoreCase(status)) {
            if (estaEnEquipo(playerEmail)) {
                throw new BusinessRuleException("El jugador ya pertenece a un equipo.");
            }

            Team targetTeam = teamRepository.findByTeamName(inv.getTeamName())
                    .orElseThrow(() -> new ResourceNotFoundException("El equipo " + inv.getTeamName() + " ya no existe."));

            User player = userRepository.findByEmail(playerEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado en el sistema"));

            if (targetTeam.getPlayers() == null) {
                targetTeam.setPlayers(new java.util.ArrayList<>());
            }
            
            targetTeam.getPlayers().add(player);
            teamRepository.save(targetTeam);

            inv.setStatus("ACEPTADA");
            invitationRepository.save(inv);
            
            log.info("El jugador {} aceptó la invitación al equipo {}", playerEmail, targetTeam.getTeamName());

        } else if ("DECLINADA".equalsIgnoreCase(status) || "DECLINED".equalsIgnoreCase(status)) {
            inv.setStatus("DECLINADA");
            invitationRepository.save(inv);
            log.info("El jugador {} declinó la invitación al equipo {}", playerEmail, inv.getTeamName());
        } else {
            throw new BusinessRuleException("Estado de aceptación inválido. Use ACEPTADA o DECLINADA.");
        }
    }
}

