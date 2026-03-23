package core.service;

import dependencies.dto.UserResponseDTO;
import core.model.Invitation;
import dependencies.dto.InvitationRequestDTO;
import dependencies.dto.InvitationResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.ResourceNotFoundException;
import core.model.User;
import core.model.Player;
import dependencies.util.DataStorage;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    public List<UserResponseDTO> buscarJugadoresDisponibles(String name, String position) {
        log.debug("Procesando la busqueda de players disponibles en el mercado. Filtros -> Nombre: {}, Posicion: {}", name, position);
        return DataStorage.users.stream()
                .filter(u -> u instanceof Player)
                .map(u -> (Player) u)
                .filter(j -> !estaEnEquipo(j.getEmail()))
                .filter(j -> name == null || name.isBlank()
                        || j.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(j -> position == null || position.isBlank()
                        || (j.getPosition() != null && j.getPosition().toLowerCase().contains(position.toLowerCase())))
                .map(j -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setName(j.getName());
                    dto.setEmail(j.getEmail());
                    dto.setRole(j.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public InvitationResponseDTO enviarInvitacion(InvitationRequestDTO request) {
        if (request.getPlayerEmail() == null || request.getPlayerEmail().isBlank()) {
            throw new BusinessRuleException("Player email is required");
        }
        if (request.getTeamName() == null || request.getTeamName().isBlank()) {
            throw new BusinessRuleException("Team name is required");
        }

        boolean jugadorExiste = DataStorage.users.stream()
                .anyMatch(u -> u instanceof Player && u.getEmail().equals(request.getPlayerEmail()));
        if (!jugadorExiste) {
            log.error("Target player not found in DataStorage: {}", request.getPlayerEmail());
            throw new ResourceNotFoundException("Player with email " + request.getPlayerEmail() + " does not exist in the system");
        }

        if (estaEnEquipo(request.getPlayerEmail())) {
            log.warn("Player {} is already signed to another team", request.getPlayerEmail());
            throw new BusinessRuleException("The player already belongs to a team, invitation cannot be sent");
        }

        boolean yaInvitado = DataStorage.invitations.stream()
                .anyMatch(i -> i.getPlayerEmail().equals(request.getPlayerEmail())
                        && i.getTeamName().equals(request.getTeamName())
                        && i.getStatus().equals("PENDING"));
        if (yaInvitado) {
            log.warn("Duplicate invitation skipped for {}", request.getPlayerEmail());
            throw new BusinessRuleException("A pending invitation already exists for this player in this team");
        }

        Invitation invitation = new Invitation(request.getCaptainEmail(), request.getPlayerEmail(), request.getTeamName());
        DataStorage.invitations.add(invitation);

        log.info("Invitation successfully generated from captain {} to player {} (Team: {})",
                 request.getCaptainEmail(), request.getPlayerEmail(), request.getTeamName());
        return new InvitationResponseDTO(invitation);
    }

    private boolean estaEnEquipo(String email) {
        return DataStorage.teams.stream()
                .anyMatch(t -> t.getPlayers().stream()
                        .anyMatch(u -> u.getEmail().equals(email)));
    }

    public void acceptInvitation(String invitationId, String playerEmail) {
        Invitation inv = DataStorage.invitations.stream()
                .filter(i -> i.getId().equals(invitationId) && i.getPlayerEmail().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!inv.getStatus().equals("PENDING") && !inv.getStatus().equals("ENVIADA")) {
            throw new BusinessRuleException("La invitación ya fue respondida o no está pendiente.");
        }

        if (estaEnEquipo(playerEmail)) {
            throw new BusinessRuleException("El jugador ya pertenece a un equipo.");
        }

        core.model.Team targetTeam = DataStorage.teams.stream()
                .filter(t -> t.getTeamName().equals(inv.getTeamName()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El equipo " + inv.getTeamName() + " ya no existe."));

        User player = DataStorage.users.stream()
                .filter(u -> u.getEmail().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado en el sistema"));

        targetTeam.getPlayers().add(player);
        inv.setStatus("ACCEPTED");
        log.info("El jugador {} aceptó la invitación al equipo {}", playerEmail, targetTeam.getTeamName());
    }

    public void declineInvitation(String invitationId, String playerEmail) {
        Invitation inv = DataStorage.invitations.stream()
                .filter(i -> i.getId().equals(invitationId) && i.getPlayerEmail().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!inv.getStatus().equals("PENDING") && !inv.getStatus().equals("ENVIADA")) {
            throw new BusinessRuleException("La invitación ya fue respondida o no está pendiente.");
        }

        inv.setStatus("DECLINED");
        log.info("El jugador {} declinó la invitación al equipo {}", playerEmail, inv.getTeamName());
    }
}
