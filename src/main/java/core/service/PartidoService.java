package core.service;

import dependencias.dto.PartidoRequestDTO;
import dependencias.dto.PartidoResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Partido;
import core.model.Partido;
import dependencias.util.DataStorage;
import core.validator.PartidoValidator;
import dependencias.mapper.PartidoMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PartidoService {

    private static final Logger log = LoggerFactory.getLogger(PartidoService.class);

    public PartidoResponseDTO registrarPartido(PartidoRequestDTO request) {
        log.debug("Ejecutando validaciones para creacion de partido");
        PartidoValidator.validateForCreation(request);

        Partido nuevoPartido = PartidoMapper.toEntity(request);

        DataStorage.partidos.add(nuevoPartido);
        log.info("Partido registrado exitosamente local {} vs {} visitante (Torneo {})", 
                 request.getEquipoLocal(), request.getEquipoVisitante(), request.getNombreTorneo());
        return PartidoMapper.toDTO(nuevoPartido);
    }

    public PartidoResponseDTO actualizarMarcador(String id, Integer marcadorLocal, Integer marcadorVisitante) {
        if (marcadorLocal == null || marcadorVisitante == null || marcadorLocal < 0 || marcadorVisitante < 0) {
            log.error("Violación funcional: Marcadores negativos o nulos recibidos");
            throw new BusinessRuleException("Marcadores invalidos");
        }

        Partido partido = findOrThrow(id);
        partido.setMarcadorLocal(marcadorLocal);
        partido.setMarcadorVisitante(marcadorVisitante);
        partido.setEstado("FINALIZADO");

        log.info("Marcador actualizado para el ID {} | {} - {}", id, marcadorLocal, marcadorVisitante);
        return PartidoMapper.toDTO(partido);
    }

    public PartidoResponseDTO registrarAlineacion(String id, String nombreEquipo, List<String> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            throw new BusinessRuleException("La alineación no puede estar vacía");
        }

        Partido partido = findOrThrow(id);

        if (!partido.getEquipoLocal().equals(nombreEquipo) && !partido.getEquipoVisitante().equals(nombreEquipo)) {
            log.warn("El equipo {} no participa en este partido", nombreEquipo);
            throw new BusinessRuleException("El equipo especificado no participa en este partido");
        }

        partido.getAlineaciones().put(nombreEquipo, jugadores);
        log.info("Alineación registrada exitosamente de {} jugadores para el equipo {}", jugadores.size(), nombreEquipo);
        return PartidoMapper.toDTO(partido);
    }

    public PartidoResponseDTO registrarTarjetas(String id, Map<String, List<String>> amarillas, Map<String, List<String>> rojas) {
        Partido partido = findOrThrow(id);
        if (amarillas != null) {
            partido.setTarjetasAmarillas(amarillas);
        }
        if (rojas != null) {
            partido.setTarjetasRojas(rojas);
        }
        log.debug("Tarjetas amarillas/rojas guardadas para el ID {}", id);
        return PartidoMapper.toDTO(partido);
    }

    public List<PartidoResponseDTO> getPartidosPorArbitro(String correoArbitro) {
        return DataStorage.partidos.stream()
                .filter(p -> correoArbitro.equals(p.getCorreoArbitro()))
                .map(PartidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PartidoResponseDTO asignarArbitro(String id, String correoArbitro) {
        boolean esArbitro = DataStorage.users.stream()
                .anyMatch(u -> u.getCorreo().equals(correoArbitro)
                        && u.getRole() != null
                        && u.getRole().name().equals("ARBITRO"));
        if (!esArbitro) {
            log.error("Asignacion fallida: {} carece del rol ARBITRO", correoArbitro);
            throw new BusinessRuleException("El correo especificado no corresponde a un árbitro registrado");
        }

        Partido partido = findOrThrow(id);
        partido.setCorreoArbitro(correoArbitro);
        log.info("Arbitro {} asignado exitosamente al Partido ID {}", correoArbitro, id);
        return PartidoMapper.toDTO(partido);
    }

    public List<PartidoResponseDTO> getAll() {
        return DataStorage.partidos.stream()
                .map(PartidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Partido findOrThrow(String id) {
        return DataStorage.partidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado con su Identificador Base"));
    }
}
