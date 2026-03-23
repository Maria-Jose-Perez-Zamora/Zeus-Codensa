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

    public List<UserResponseDTO> buscarJugadoresDisponibles(String name, String posicion) {
        log.debug("Procesando la busqueda de players disponibles en el mercado. Filtros -> Nombre: {}, Posicion: {}", name, posicion);
        return DataStorage.users.stream()
                .filter(u -> u instanceof Player)
                .map(u -> (Player) u)
                .filter(j -> !estaEnEquipo(j.getCorreo()))
                .filter(j -> name == null || name.isBlank()
                        || j.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(j -> posicion == null || posicion.isBlank()
                        || (j.getPosicion() != null && j.getPosicion().toLowerCase().contains(posicion.toLowerCase())))
                .map(j -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setName(j.getName());
                    dto.setCorreo(j.getCorreo());
                    dto.setRole(j.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public InvitationResponseDTO enviarInvitacion(InvitationRequestDTO request) {
        if (request.getCorreoJugador() == null || request.getCorreoJugador().isBlank()) {
            throw new BusinessRuleException("El correo del player es obligatorio");
        }
        if (request.getNombreEquipo() == null || request.getNombreEquipo().isBlank()) {
            throw new BusinessRuleException("El name del team es obligatorio");
        }

        boolean jugadorExiste = DataStorage.users.stream()
                .anyMatch(u -> u instanceof Player && u.getCorreo().equals(request.getCorreoJugador()));
        if (!jugadorExiste) {
            log.error("El player destino no existe en DataStorage: {}", request.getCorreoJugador());
            throw new ResourceNotFoundException("El player con correo " + request.getCorreoJugador() + " no existe en el sistema");
        }

        if (estaEnEquipo(request.getCorreoJugador())) {
            log.warn("El player {} ya está fichado por otro team", request.getCorreoJugador());
            throw new BusinessRuleException("El player ya pertenece a un team, no se puede enviar invitación");
        }

        boolean yaInvitado = DataStorage.invitations.stream()
                .anyMatch(i -> i.getCorreoJugador().equals(request.getCorreoJugador())
                        && i.getNombreEquipo().equals(request.getNombreEquipo())
                        && i.getStatus().equals("PENDING"));
        if (yaInvitado) {
            log.warn("Invitación duplicada omitida hacia {}", request.getCorreoJugador());
            throw new BusinessRuleException("Ya existe una invitación pendiente para este player en este team");
        }

        Invitation invitation = new Invitation(request.getCorreoCapitan(), request.getCorreoJugador(), request.getNombreEquipo());
        DataStorage.invitations.add(invitation);

        log.info("Invitación generada exitosamente del capitan {} al player {} (Equipo: {})",
                 request.getCorreoCapitan(), request.getCorreoJugador(), request.getNombreEquipo());
        return new InvitationResponseDTO(invitation);
    }

    private boolean estaEnEquipo(String correo) {
        return DataStorage.teams.stream()
                .anyMatch(t -> t.getJugadores().stream()
                        .anyMatch(u -> u.getCorreo().equals(correo)));
    }

    public void acceptInvitation(String invitationId, String playerEmail) {
        Invitation inv = DataStorage.invitations.stream()
                .filter(i -> i.getId().equals(invitationId) && i.getCorreoJugador().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!inv.getStatus().equals("PENDING") && !inv.getStatus().equals("ENVIADA")) {
            throw new BusinessRuleException("La invitación ya fue respondida o no está pendiente.");
        }

        if (estaEnEquipo(playerEmail)) {
            throw new BusinessRuleException("El jugador ya pertenece a un equipo.");
        }

        core.model.Team targetTeam = DataStorage.teams.stream()
                .filter(t -> t.getNombreEquipo().equals(inv.getNombreEquipo()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El equipo " + inv.getNombreEquipo() + " ya no existe."));

        User player = DataStorage.users.stream()
                .filter(u -> u.getCorreo().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado en el sistema"));

        targetTeam.getJugadores().add(player);
        inv.setStatus("ACCEPTED");
        log.info("El jugador {} aceptó la invitación al equipo {}", playerEmail, targetTeam.getNombreEquipo());
    }

    public void declineInvitation(String invitationId, String playerEmail) {
        Invitation inv = DataStorage.invitations.stream()
                .filter(i -> i.getId().equals(invitationId) && i.getCorreoJugador().equals(playerEmail))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!inv.getStatus().equals("PENDING") && !inv.getStatus().equals("ENVIADA")) {
            throw new BusinessRuleException("La invitación ya fue respondida o no está pendiente.");
        }

        inv.setStatus("DECLINED");
        log.info("El jugador {} declinó la invitación al equipo {}", playerEmail, inv.getNombreEquipo());
    }
}
