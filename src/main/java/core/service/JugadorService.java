package core.service;

import dependencias.dto.UserResponseDTO;
import core.model.Invitacion;
import dependencias.dto.InvitacionRequestDTO;
import dependencias.dto.InvitacionResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.ResourceNotFoundException;
import core.model.User;
import core.model.Jugador;
import dependencias.util.DataStorage;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    private static final Logger log = LoggerFactory.getLogger(JugadorService.class);

    public List<UserResponseDTO> buscarJugadoresDisponibles(String nombre, String posicion) {
        log.debug("Procesando la busqueda de jugadores disponibles en el mercado. Filtros -> Nombre: {}, Posicion: {}", nombre, posicion);
        return DataStorage.users.stream()
                .filter(u -> u instanceof Jugador)
                .map(u -> (Jugador) u)
                .filter(j -> !estaEnEquipo(j.getCorreo()))
                .filter(j -> nombre == null || nombre.isBlank()
                        || j.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(j -> posicion == null || posicion.isBlank()
                        || (j.getPosicion() != null && j.getPosicion().toLowerCase().contains(posicion.toLowerCase())))
                .map(j -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setNombre(j.getNombre());
                    dto.setCorreo(j.getCorreo());
                    dto.setRole(j.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public InvitacionResponseDTO enviarInvitacion(InvitacionRequestDTO request) {
        if (request.getCorreoJugador() == null || request.getCorreoJugador().isBlank()) {
            throw new BusinessRuleException("El correo del jugador es obligatorio");
        }
        if (request.getNombreEquipo() == null || request.getNombreEquipo().isBlank()) {
            throw new BusinessRuleException("El nombre del equipo es obligatorio");
        }

        boolean jugadorExiste = DataStorage.users.stream()
                .anyMatch(u -> u instanceof Jugador && u.getCorreo().equals(request.getCorreoJugador()));
        if (!jugadorExiste) {
            log.error("El jugador destino no existe en DataStorage: {}", request.getCorreoJugador());
            throw new ResourceNotFoundException("El jugador con correo " + request.getCorreoJugador() + " no existe en el sistema");
        }

        if (estaEnEquipo(request.getCorreoJugador())) {
            log.warn("El jugador {} ya está fichado por otro equipo", request.getCorreoJugador());
            throw new BusinessRuleException("El jugador ya pertenece a un equipo, no se puede enviar invitación");
        }

        boolean yaInvitado = DataStorage.invitaciones.stream()
                .anyMatch(i -> i.getCorreoJugador().equals(request.getCorreoJugador())
                        && i.getNombreEquipo().equals(request.getNombreEquipo())
                        && i.getEstado().equals("ENVIADA"));
        if (yaInvitado) {
            log.warn("Invitación duplicada omitida hacia {}", request.getCorreoJugador());
            throw new BusinessRuleException("Ya existe una invitación pendiente para este jugador en este equipo");
        }

        Invitacion invitacion = new Invitacion(request.getCorreoCapitan(), request.getCorreoJugador(), request.getNombreEquipo());
        DataStorage.invitaciones.add(invitacion);

        log.info("Invitación generada exitosamente del capitan {} al jugador {} (Equipo: {})",
                 request.getCorreoCapitan(), request.getCorreoJugador(), request.getNombreEquipo());
        return new InvitacionResponseDTO(invitacion);
    }

    private boolean estaEnEquipo(String correo) {
        return DataStorage.teams.stream()
                .anyMatch(t -> t.getJugadores().stream()
                        .anyMatch(u -> u.getCorreo().equals(correo)));
    }
}
