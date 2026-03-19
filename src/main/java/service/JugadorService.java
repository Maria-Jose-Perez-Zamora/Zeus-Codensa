package service;

import dto.UserResponseDTO;
import model.Invitacion;
import dto.InvitacionRequestDTO;
import dto.InvitacionResponseDTO;
import model.User;
import model.Jugador;
import util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    /**
     * RF-003: Busca jugadores disponibles (sin equipo) filtrando por nombre y/o posición.
     */
    public List<UserResponseDTO> buscarJugadoresDisponibles(String nombre, String posicion) {
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

    /**
     * RF-003: Envía una invitación de un capitán a un jugador disponible.
     */
    public InvitacionResponseDTO enviarInvitacion(InvitacionRequestDTO request) {
        if (request.getCorreoJugador() == null || request.getCorreoJugador().isBlank()) {
            throw new IllegalArgumentException("El correo del jugador es obligatorio");
        }
        if (request.getNombreEquipo() == null || request.getNombreEquipo().isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }

        boolean jugadorExiste = DataStorage.users.stream()
                .anyMatch(u -> u instanceof Jugador && u.getCorreo().equals(request.getCorreoJugador()));
        if (!jugadorExiste) {
            throw new IllegalArgumentException("El jugador con correo " + request.getCorreoJugador() + " no existe en el sistema");
        }

        if (estaEnEquipo(request.getCorreoJugador())) {
            throw new IllegalArgumentException("El jugador ya pertenece a un equipo, no se puede enviar invitación");
        }

        boolean yaInvitado = DataStorage.invitaciones.stream()
                .anyMatch(i -> i.getCorreoJugador().equals(request.getCorreoJugador())
                        && i.getNombreEquipo().equals(request.getNombreEquipo())
                        && i.getEstado().equals("ENVIADA"));
        if (yaInvitado) {
            throw new IllegalArgumentException("Ya existe una invitación pendiente para este jugador en este equipo");
        }

        Invitacion invitacion = new Invitacion(request.getCorreoCapitan(), request.getCorreoJugador(), request.getNombreEquipo());
        DataStorage.invitaciones.add(invitacion);

        return new InvitacionResponseDTO(invitacion);
    }

    private boolean estaEnEquipo(String correo) {
        return DataStorage.teams.stream()
                .anyMatch(t -> t.getJugadores().stream()
                        .anyMatch(u -> u.getCorreo().equals(correo)));
    }
}
