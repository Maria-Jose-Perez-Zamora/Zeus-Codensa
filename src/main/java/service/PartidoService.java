package service;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import model.Partido;
import util.DataStorage;
import validator.PartidoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartidoService {

    public PartidoResponseDTO registrarPartido(PartidoRequestDTO request) {
        PartidoValidator.validateForCreation(request);

        Partido nuevoPartido = new Partido(
                request.getEquipoLocal(),
                request.getEquipoVisitante(),
                request.getFechaPartido(),
                request.getNombreTorneo()
        );

        DataStorage.partidos.add(nuevoPartido);
        return new PartidoResponseDTO(nuevoPartido);
    }

    public PartidoResponseDTO actualizarMarcador(String id, Integer marcadorLocal, Integer marcadorVisitante) {
        if (marcadorLocal == null || marcadorVisitante == null || marcadorLocal < 0 || marcadorVisitante < 0) {
            throw new IllegalArgumentException("Marcadores invalidos");
        }

        Partido partido = findOrThrow(id);
        partido.setMarcadorLocal(marcadorLocal);
        partido.setMarcadorVisitante(marcadorVisitante);
        partido.setEstado("FINALIZADO");

        return new PartidoResponseDTO(partido);
    }

    /**
     * RF-008: Registrar alineación de un equipo para un partido dado.
     */
    public PartidoResponseDTO registrarAlineacion(String id, String nombreEquipo, List<String> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            throw new IllegalArgumentException("La alineación no puede estar vacía");
        }

        Partido partido = findOrThrow(id);

        if (!partido.getEquipoLocal().equals(nombreEquipo) && !partido.getEquipoVisitante().equals(nombreEquipo)) {
            throw new IllegalArgumentException("El equipo especificado no participa en este partido");
        }

        partido.getAlineaciones().put(nombreEquipo, jugadores);
        return new PartidoResponseDTO(partido);
    }

    /**
     * RF-008: Retorna los partidos asignados a un árbitro (vista "mis partidos").
     */
    public List<PartidoResponseDTO> getPartidosPorArbitro(String correoArbitro) {
        return DataStorage.partidos.stream()
                .filter(p -> correoArbitro.equals(p.getCorreoArbitro()))
                .map(PartidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Asigna un árbitro a un partido (solo árbitros registrados).
     */
    public PartidoResponseDTO asignarArbitro(String id, String correoArbitro) {
        boolean esArbitro = DataStorage.users.stream()
                .anyMatch(u -> u.getCorreo().equals(correoArbitro)
                        && u.getRole() != null
                        && u.getRole().name().equals("ARBITRO"));
        if (!esArbitro) {
            throw new IllegalArgumentException("El correo especificado no corresponde a un árbitro registrado");
        }

        Partido partido = findOrThrow(id);
        partido.setCorreoArbitro(correoArbitro);
        return new PartidoResponseDTO(partido);
    }

    public List<PartidoResponseDTO> getAll() {
        return DataStorage.partidos.stream()
                .map(PartidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    private Partido findOrThrow(String id) {
        return DataStorage.partidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
    }
}
